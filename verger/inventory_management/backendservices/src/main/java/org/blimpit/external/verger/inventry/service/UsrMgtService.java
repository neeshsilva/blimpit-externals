package org.blimpit.external.verger.inventry.service;


import org.blimpit.external.verger.inventry.Constants;
import org.blimpit.external.verger.inventry.controller.feature.FeatureMgtController;
import org.blimpit.external.verger.inventry.controller.usrmgt.UsrMgtController;
import org.blimpit.external.verger.inventry.model.usermgt.Credential;
import org.blimpit.external.verger.inventry.model.usermgt.Feature;
import org.blimpit.external.verger.inventry.model.usermgt.ResponseStatus;
import org.blimpit.external.verger.inventry.model.usermgt.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("usrmgtservice")
public class UsrMgtService {
    private UsrMgtController usrMgtController = UsrMgtController.getInstance();
    private FeatureMgtController featureMgtController = FeatureMgtController.getInstance();



    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseStatus login(Credential credential){
     return usrMgtController.login(credential);
    }


    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseStatus register(User user){
        return usrMgtController.register(user);
    }



    @GET
    @Path("/features")
    @Produces(MediaType.APPLICATION_JSON)
    public Feature[] features( ){
        return featureMgtController.getFeatures();
    }


    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseStatus update(User user ){
        return usrMgtController.updateUserInfo(user);
    }



    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User[] users(@QueryParam(Constants.STATUS) final String status){
        return usrMgtController.getUsers(Constants.STATUS, status);
    }


}
