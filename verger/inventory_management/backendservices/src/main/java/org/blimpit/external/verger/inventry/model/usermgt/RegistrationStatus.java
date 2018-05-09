package org.blimpit.external.verger.inventry.model.usermgt;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum RegistrationStatus {

    PENDING, ACCEPTED, REJECTED;


    public static RegistrationStatus fromString(final String s) {
        return RegistrationStatus.valueOf(s);
    }

    }
