package org.blimpit.external.verger.inventry.service;


import org.blimpit.external.verger.inventry.model.Product;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("invmgtservice")
public class InvMgtService {

    @GET
    @Path("products")
    @Produces(MediaType.APPLICATION_JSON)
    public Product[] getProducts(){
        List<Product> productList = new ArrayList<Product>();
        Product one = new Product("one");
        Product two = new Product("two");
        productList.add(one);
        productList.add(two);
        return productList.toArray(new Product[productList.size()]);
    }
}
