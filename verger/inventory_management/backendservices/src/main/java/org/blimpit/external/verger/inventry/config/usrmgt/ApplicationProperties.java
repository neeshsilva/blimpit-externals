package org.blimpit.external.verger.inventry.config.usrmgt;

import org.blimpit.external.verger.inventry.Constants;
import org.blimpit.external.verger.inventry.controller.feature.FeatureMgtController;
import org.blimpit.external.verger.inventry.model.usermgt.Feature;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ApplicationProperties {

    private static ApplicationProperties applicationProperties;

    private Properties properties;
    private List<Feature> featureList;

    private ApplicationProperties() {
        properties = new Properties();
        featureList = new ArrayList<Feature>();
        InputStream input = null;



        try {
//            File path = new File(".");
//            String curPath = path.getCanonicalPath();
//            String configpath = curPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" +
//                    File.separator + "config.properties";
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
           // input = new FileInputStream(configpath);

            // load a properties file
            properties.load(input);


            for (Object key : properties.keySet()){
                if(key instanceof String ){
                    if(((String) key).startsWith("feature")){
                        Feature feature = new Feature();
                        String name = ((String) key);
                        feature.setFeatureId(name);
                        feature.setFeatureName(properties.getProperty((String)key));
                        featureList.add(feature);
                    }

                }
            }

        } catch (IOException ex) {
            //TODO:handle errors
              ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    //TODO ;handle errors
                      e.printStackTrace();
                }
            }
        }

    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }



public Feature[] getFeatures(){
        return featureList.toArray(new Feature[featureList.size()]);
}







    public static ApplicationProperties getInstance() {
        if (applicationProperties == null) {
            synchronized (ApplicationProperties.class) {
                if (applicationProperties == null) {
                    applicationProperties = new ApplicationProperties();
                }
            }
        }
        return applicationProperties;
    }


}