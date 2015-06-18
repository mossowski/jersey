package com.misys.async_rest.resource;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ManagedAsync;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.misys.async_rest.dao.ProductDao;
import com.misys.async_rest.model.Product;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/products")
public class ProductResource {

    @Context
    ProductDao dao;

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param response
     */
    @GET
    @ManagedAsync
    public void getProducts(@Suspended final AsyncResponse response) {

        ListenableFuture<Collection<Product>> productsFuture = this.dao.getProductsAsync();
        Futures.addCallback(productsFuture, new FutureCallback<Collection<Product>>() {
            @Override
            public void onSuccess(Collection<Product> products) {
                response.resume(products);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @param response
     */
    @GET
    @ManagedAsync
    @Path("/{id}")
    public void getProduct(@PathParam("id") String id, @Suspended final AsyncResponse response) {

        ListenableFuture<Product> productFuture = this.dao.getProductAsync(id);
        Futures.addCallback(productFuture, new FutureCallback<Product>() {
            @Override
            public void onSuccess(Product product) {
                response.resume(product);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param product
     * @param response
     */
    @POST
    @ManagedAsync
    public void addProduct(@Valid @NotNull Product product, @Suspended final AsyncResponse response) {

        ListenableFuture<Product> productFuture = this.dao.addProductAsync(product);
        Futures.addCallback(productFuture, new FutureCallback<Product>() {
            @Override
            public void onSuccess(Product addedProduct) {
                response.resume(addedProduct);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @param productToUpdate
     * @param response
     */
    @PUT
    @ManagedAsync
    @Path("/{id}")
    public void updateProduct(@PathParam("id") String id, @Valid @NotNull Product productToUpdate, @Suspended final AsyncResponse response) {

        productToUpdate.setId(id);
        ListenableFuture<Product> productFuture = this.dao.updateProductAsync(productToUpdate);
        Futures.addCallback(productFuture, new FutureCallback<Product>() {
            @Override
            public void onSuccess(Product product) {
                response.resume(product);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @param response
     */
    @DELETE
    @ManagedAsync
    @Path("/{id}")
    public void deleteProduct(@PathParam("id") String id, @Suspended final AsyncResponse response) {

        ListenableFuture<Product> productFuture = this.dao.deleteProductAsync(id);
        Futures.addCallback(productFuture, new FutureCallback<Product>() {
            @Override
            public void onSuccess(Product product) {
                response.resume(product);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

}
