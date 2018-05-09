package org.blimpit.external.verger.inventry.controller.feature;

import org.blimpit.external.verger.inventry.Constants;
import org.blimpit.external.verger.inventry.config.usrmgt.ApplicationProperties;
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

public class FeatureMgtController {

    private static FeatureMgtController featureMgtController;
    private static MySQLConnector mySQLConnector;
    private static ApplicationProperties applicationProperties;

    private String host;
    private String port;
    private String db;
    private String username;
    private String password;
    private String featureTableName;
    private String featureUserMapper;


    private FeatureMgtController() {
        applicationProperties = ApplicationProperties.getInstance();
        host = applicationProperties.getValue(Constants.DB_HOST);
        port = applicationProperties.getValue(Constants.DB_PORT);
        db = applicationProperties.getValue(Constants.DB_NAME);
        username = applicationProperties.getValue(Constants.DB_USERNAME);
        password = applicationProperties.getValue(Constants.DB_PASSWORD);
        featureTableName = applicationProperties.getValue(Constants.FEATURE_TABLE_);
        featureUserMapper = applicationProperties.getValue(Constants.FEATURE_USER_MAPPER);

        try {
            mySQLConnector = (MySQLConnector) MySQLConnector.
                    getInstance(host, port, db, username, password);
            storeFeatures();
        } catch (ConnectorException e) {
            //TODO:error handling
            e.printStackTrace();

        }
    }


    public Feature[] getFeatures() {
        List<Feature> featureList = new ArrayList<Feature>();
        try {

            Record[] records = mySQLConnector.read(featureTableName);
            if (records.length > 0) {
                for (Record record : records) {
                    Feature feature = new Feature();
                    String f_id = record.getRecordAttributes().get(Constants.FEATURE_Id);
                    String f_name = record.getRecordAttributes().get(Constants.FEATURE_NAME);
                    feature.setFeatureId(f_id);
                    feature.setFeatureName(f_name);
                    featureList.add(feature);
                }

            }
        } catch (ConnectorException e) {
            //TODO: Error handling
            e.printStackTrace();
        }
        return featureList.toArray(new Feature[featureList.size()]);
    }


    public ResponseStatus addUserFeatureMapping(User user) {
        Feature[] feature = user.getFeatures();
        ResponseStatus responseStatus = new ResponseStatus();
        try {
            Map<String, String> featureMap = new HashMap<String, String>();
            for (Feature ft : feature) {
                featureMap.put(Constants.F_ID, ft.getFeatureId());
                featureMap.put(Constants.U_ID, user.getUsername());
            }

            mySQLConnector.insert(featureUserMapper, featureMap);


        } catch (ConnectorException e) {
            responseStatus.setSuccess(true);
            responseStatus.setMessage("Cannot add  user to feature mapping, " +
                    e.getMessage());
        }
        return responseStatus;
    }

    public ResponseStatus addFeatures(Feature feature) {
        Map<String, String> featureMap = new HashMap<String, String>();
        ResponseStatus responseStatus = new ResponseStatus();
        try {

            featureMap.put(Constants.FEATURE_Id, feature.getFeatureId());
            featureMap.put(Constants.FEATURE_NAME, feature.getFeatureName());

            mySQLConnector.insert(featureTableName, featureMap);


        } catch (ConnectorException e) {
            responseStatus.setSuccess(true);
            responseStatus.setMessage("Cannot add  feature to feature mapper, " +
                    e.getMessage());
        }
        return responseStatus;
    }

    public Feature[] getFeatures(String key, String value) {
        List<Feature> featureMap = new ArrayList();
        try {


            Record[] records = mySQLConnector.read(featureTableName, key, value);

            for (Record record : records) {
                Feature feature = new Feature();

                Map<String, String> recordMap = record.getRecordAttributes();
                feature.setFeatureId(recordMap.get(Constants.FEATURE_Id));
                feature.setFeatureName(recordMap.get(Constants.FEATURE_NAME));
                featureMap.add(feature);

            }

        } catch (ConnectorException e) {
            //TODO; Error handling
        }
        return featureMap.toArray(new Feature[featureMap.size()]);
    }


    public static FeatureMgtController getInstance() {
        if (featureMgtController == null) {
            synchronized (FeatureMgtController.class) {
                if (featureMgtController == null) {
                    featureMgtController = new FeatureMgtController();
                }
            }
        }
        return featureMgtController;
    }


    public boolean storeFeatures() throws ConnectorException {
    Feature[] features =  applicationProperties.getFeatures();
    Map<String, String> map = new HashMap<String, String>();
    for(Feature feature: features){
        map.put(Constants.FEATURE_Id, feature.getFeatureId());
        map.put(Constants.FEATURE_NAME, feature.getFeatureName());
        mySQLConnector.insert(featureTableName,map);
    }

    return true;

    }


}
