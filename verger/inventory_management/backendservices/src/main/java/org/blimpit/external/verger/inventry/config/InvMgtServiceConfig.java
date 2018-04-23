package org.blimpit.external.verger.inventry.config;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("verger")
public class InvMgtServiceConfig extends ResourceConfig{

    public InvMgtServiceConfig(){
        packages("org.blimpit.external.verger.inventry");
    }
}
