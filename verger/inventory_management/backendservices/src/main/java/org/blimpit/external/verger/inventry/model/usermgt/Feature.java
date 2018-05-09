package org.blimpit.external.verger.inventry.model.usermgt;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Feature {


    private String featureId;

    private String featureName;

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
}
