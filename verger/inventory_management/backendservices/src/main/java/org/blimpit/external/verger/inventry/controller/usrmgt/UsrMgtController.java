package org.blimpit.external.verger.inventry.controller.usrmgt;


import org.blimpit.external.verger.inventry.Constants;
import org.blimpit.external.verger.inventry.config.usrmgt.ApplicationProperties;
import org.blimpit.external.verger.inventry.controller.feature.FeatureMgtController;
import org.blimpit.external.verger.inventry.model.usermgt.Credential;
import org.blimpit.external.verger.inventry.model.usermgt.Feature;
import org.blimpit.external.verger.inventry.model.usermgt.ResponseStatus;
import org.blimpit.external.verger.inventry.model.usermgt.User;
import org.blimpit.utils.connectors.ConnectorException;
import org.blimpit.utils.connectors.mysql.MySQLConnector;
import org.blimpit.utils.connectors.mysql.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsrMgtController {

    private static UsrMgtController usrMgtController;
    private static FeatureMgtController featureMgtController;
    private static MySQLConnector mySQLConnector;
    private static ApplicationProperties applicationProperties;

    private String host;
    private String port;
    private String db;
    private String username;
    private String password;
    private String tableName;


    private UsrMgtController() {
        applicationProperties = ApplicationProperties.getInstance();
        featureMgtController = FeatureMgtController.getInstance();
        host = applicationProperties.getValue(Constants.DB_HOST);
        port = applicationProperties.getValue(Constants.DB_PORT);
        db = applicationProperties.getValue(Constants.DB_NAME);
        username = applicationProperties.getValue(Constants.DB_USERNAME);
        password = applicationProperties.getValue(Constants.DB_PASSWORD);
        tableName = applicationProperties.getValue(Constants.DB_MGT_USERS);
        try {
            mySQLConnector = (MySQLConnector) MySQLConnector.
                    getInstance(host, port, db, username, password);
        } catch (ConnectorException e) {
            //TODO:error handling
            e.printStackTrace();

        }
    }


    public ResponseStatus login(Credential credential) {
        ResponseStatus responseStatus = new ResponseStatus();
        String username = null;
        String password = null;
        try {
            Record[] records = mySQLConnector.read(tableName, Constants.USERNAME, credential.getUsername());
            if (records.length > 0) {
                Record record = records[0];
                Map<String, String> attributes = record.getRecordAttributes();
                username = attributes.get(Constants.USERNAME);
                password = attributes.get(Constants.PASSWORD);
                if (username.equals(credential.getUsername()) && password.equals(credential.getPassword())) {
                    responseStatus.setSuccess(true);
                    responseStatus.setMessage(Constants.LOGIN_SUCESS);
                } else {
                    responseStatus.setSuccess(false);
                    responseStatus.setMessage("Login failed due to password mismatch");
                }
            } else {
                responseStatus.setSuccess(false);
                responseStatus.setMessage("No user found");
            }

        } catch (ConnectorException e) {
            responseStatus.setSuccess(false);
            responseStatus.setMessage("Login failed due to password mismatch");
        }
        return responseStatus;
    }


    public ResponseStatus register(User user) {
        ResponseStatus responseStatus = new ResponseStatus();
        Map<String, String> map = new HashMap<String, String>();

        map.put(Constants.USERNAME, user.getUsername());
        map.put(Constants.PASSWORD, user.getPassword());
        map.put(Constants.NAME, user.getName());
        map.put(Constants.DESIGNATION, user.getDesignation());
        map.put(Constants.STATUS, user.getStatus());
        Feature[] features = user.getFeatures();

        try {
            boolean insert = mySQLConnector.insert(tableName, map);


            if (insert) {
                responseStatus.setSuccess(true);
                responseStatus.setMessage("Sent for acceptance from the admin ");
            } else {
                responseStatus.setSuccess(false);
                responseStatus.setMessage("Login failed due to password mismatch");
            }
        } catch (ConnectorException e) {
            responseStatus.setSuccess(true);
            responseStatus.setMessage("Cannot register user, check mysql server is up and running, " +
                    e.getMessage());
        }
        return responseStatus;
    }


    public ResponseStatus updateUserInfo(User user) {
        ResponseStatus responseStatus = new ResponseStatus();
        Map<String, String> map = new HashMap<String, String>();

        map.put(Constants.USERNAME, user.getUsername());
        map.put(Constants.PASSWORD, user.getPassword());
        map.put(Constants.NAME, user.getName());
        map.put(Constants.DESIGNATION, user.getDesignation());
        map.put(Constants.STATUS, user.getStatus());
//        Feature[] features = user.getFeatures();
//
//        for (Feature feature: features){
//            map.put(Constants.F_ID, feature.getFeatureId());
//            map.put(Constants.U_ID, user.getUsername());
//        }

     ResponseStatus status =   featureMgtController.addUserFeatureMapping(user);

     if(status.isSuccess()) {

         try {
             boolean insert = mySQLConnector.update(tableName, Constants.USERNAME, user.getUsername(), map);
             if (insert ) {
                 responseStatus.setSuccess(true);
                 responseStatus.setMessage("Cannot register user, check mysql server is up and running");
             } else {
                 responseStatus.setSuccess(false);
                 responseStatus.setMessage("Login failed due to password mismatch");
             }
         } catch (ConnectorException e) {
             responseStatus.setSuccess(true);
             responseStatus.setMessage("Cannot register user, check mysql server is up and running, " +
                     e.getMessage());
         }
     }else{
         responseStatus = status;
     }
        return responseStatus;
    }


    public User[] getUsers(String key, String value) {
        List<User> userMap = new ArrayList<User>();
        try {
            Record record[] = mySQLConnector.read(tableName,key, value);
           for (Record rec: record){
             Map<String, String> attributes = rec.getRecordAttributes();
             User user = new User();
             user.setName(attributes.get(Constants.NAME));
             user.setUsername(attributes.get(Constants.USERNAME));
             user.setPassword(attributes.get(Constants.PASSWORD));
             user.setDesignation(attributes.get(Constants.DESIGNATION));
              Feature feature[] = featureMgtController.getFeatures(Constants.USERNAME,attributes.get(Constants.USERNAME) );
              user.setFeatures(feature);
              userMap.add(user);
            }
        } catch (ConnectorException e) {
            //TODO : Error handling
        }
        return userMap.toArray(new User[userMap.size()]);
    }








    public static UsrMgtController getInstance() {
        if (usrMgtController == null) {
            synchronized (UsrMgtController.class) {
                if (usrMgtController == null) {
                    usrMgtController = new UsrMgtController();
                }
            }
        }
        return usrMgtController;
    }


}
