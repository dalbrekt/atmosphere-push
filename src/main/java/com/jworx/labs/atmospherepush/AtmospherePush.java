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
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.jersey.SuspendResponse;

import com.jworx.logfacade.Log;
import com.jworx.logfacade.LogFactory;

@Path("/pubsub/{topic}")
@Produces("text/html;charset=ISO-8859-1")
public class AtmospherePush {
    private Log log = LogFactory.getLog(AtmospherePush.class);

    private @PathParam("topic")
    Broadcaster topic;
    private static Timer timer;

    public AtmospherePush() throws IllegalAccessException, InstantiationException {
        if (timer == null) {
            timer = new Timer("atmoshpere-timer");
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    try {
                        publish();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }

                }
            }, 1000, 1000);
        }

    }

    @GET
    public SuspendResponse<String> subscribe() {
        return new SuspendResponse.SuspendResponseBuilder<String>().broadcaster(topic).outputComments(true)
                .addListener(new EventLogger()).build();
    }

    public void publish() throws IllegalAccessException, InstantiationException {
        if (topic != null) {
            Date now = new Date();
            log.debug("publish: %s", now);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            topic.broadcast(formatter.format(now));
        }
    }
}
