package org.blimpit.external.verger.inventry.config;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("verger")
public class InvMgtServiceResourceConfig extends ResourceConfig{
    public InvMgtServiceResourceConfig(){
      // configure importing packages here
      //  packages("com.fasterxml.jackson.jaxrs.json");
      //  packages("com.mysql.*");
    }
}
