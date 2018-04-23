package org.blimpit.external.verger.inventry.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Product {

    private String name;

    public Product(){

    }

    public Product(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
