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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListener;

import com.jworx.logfacade.Log;
import com.jworx.logfacade.LogFactory;

public class EventLogger implements AtmosphereResourceEventListener {
    private Log log = LogFactory.getLog(EventLogger.class);

    @Override
    public void onSuspend(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        log.debug("onSuspend [remoteAddr=%s, remotePort=%s]", event.getResource().getRequest().getRemoteAddr(), event
                .getResource().getRequest().getRemotePort());
    }

    @Override
    public void onResume(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        log.debug("onResume [remoteAddr=%s, remotePort=%s]", event.getResource().getRequest().getRemoteAddr(), event
                .getResource().getRequest().getRemotePort());
    }

    @Override
    public void onDisconnect(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        log.debug("onDisconnect [remoteAddr=%s, remotePort=%s]", event.getResource().getRequest().getRemoteAddr(),
            event.getResource().getRequest().getRemotePort());
    }

    @Override
    public void onBroadcast(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        log.debug("onBroadcast[remoteAddr=%s, remotePort=%s]", event.getResource().getRequest().getRemoteAddr(), event
                .getResource().getRequest().getRemotePort());
    }

    @Override
    public void onThrowable(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
        log.debug("onThrowable [remoteAddr=%s, remotePort=%s]", event.getResource().getRequest().getRemoteAddr(), event
                .getResource().getRequest().getRemotePort());
    }
}
