/*
 Copyright 2011 Tony Dalbrekt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package com.jworx.labs.atmospherepush;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.jersey.SuspendResponse;

import com.jworx.logfacade.Log;
import com.jworx.logfacade.LogFactory;

@Path("/pubsub")
@Produces("text/html;charset=ISO-8859-1")
public class AtmospherePush {
    private final Log log = LogFactory.getLog(AtmospherePush.class);
    private static final ConcurrentHashMap<String, Future<?>> futures = new ConcurrentHashMap<String, Future<?>>();
    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GET
    @Path("/subscribe/{topic}")
    public SuspendResponse<String> subscribe(final @PathParam("topic") Broadcaster feed,
            final @PathParam("topic") String topic) {

        if (topic.isEmpty()) {
            throw new WebApplicationException();
        }

        if (feed.getAtmosphereResources().size() == 0) {

            final Future<?> future = feed.scheduleFixedBroadcast(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    return newDate();
                }
            }, 1, TimeUnit.SECONDS);

            futures.put(topic, future);
        }

        return new SuspendResponse.SuspendResponseBuilder<String>().broadcaster(feed).outputComments(true)
                .addListener(new EventLogger()).build();
    }

    public String newDate() {
        String now = FORMATTER.format(new Date());
        log.debug("publish: %s", now);
        return now;
    }
    
    @GET
    @Path("/unsubscribe/{topic}")
    public String unsubscribe(final @PathParam("topic") Broadcaster feed,
                             final @PathParam("topic") String topic) {
        feed.resumeAll();
        futures.get(topic).cancel(true);
        log.info("Unsubscribes from %s", topic);
        return "DONE";
    }
}
