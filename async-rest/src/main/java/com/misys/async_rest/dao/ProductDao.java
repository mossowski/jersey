package com.misys.async_rest.dao;

import static com.mongodb.client.model.Filters.eq;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.bson.Document;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.misys.async_rest.Main;
import com.misys.async_rest.model.Product;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class ProductDao {

	private ListeningExecutorService service;

    // ---------------------------------------------------------------------------------------------------

    public ProductDao() {

        this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public Collection<Product> getProducts() {
        
        Map<String, Product> products = new ConcurrentHashMap<String, Product>();
        
        for (Document cursor : Main.database.getProducts().find()) {
            Product product = new Product();
            product.setId(cursor.get("id").toString());
            product.setName(cursor.get("name").toString());
            products.put(product.getId(), product);
        }
        
        return products.values();
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public ListenableFuture<Collection<Product>> getProductsAsync() {

        ListenableFuture<Collection<Product>> future = this.service
                .submit(new Callable<Collection<Product>>() {
                    @Override
                    public Collection<Product> call() throws Exception {
                        return getProducts();
                    }
                });

        return future;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public Product getProduct(String id) {

        Document document = Main.database.getProducts().find(eq("id", id)).first();
        Product product = new Product();
        
        product.setId(document.get("id").toString());
        product.setName(document.get("name").toString());
        
        System.out.println("\n------------- GET PRODUCT WITH ID ----------------");
        System.out.println(" id   : " + document.get("id"));
        System.out.println(" name : " + document.get("name"));
        System.out.println("-------------------------------------------------");       
        
        return product;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Product> getProductAsync(final String id) {

        ListenableFuture<Product> future = this.service.submit(new Callable<Product>() {
            @Override
            public Product call() throws Exception {
                return getProduct(id);
            }
        });

        return future;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param product
     * @return
     */
    public Product addProduct(Product product) {

        product.setId(UUID.randomUUID().toString());
        
        Document document = new Document();
        document.put("id", product.getId());
        document.put("name", product.getName());
        
        Main.database.getProducts().insertOne(document);

        return product;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param product
     * @return
     */
    public ListenableFuture<Product> addProductAsync(final Product product) {

        ListenableFuture<Product> future = this.service.submit(new Callable<Product>() {
            @Override
            public Product call() throws Exception {
                return addProduct(product);
            }
        });

        return future;
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param product
     * @return
     */
    public Product updateProduct(Product product) {

        String productId = product.getId();
        String productName = product.getName();
        
        Map<String, Object> changes = new ConcurrentHashMap<String, Object>();
        changes.put("id", productId);
        changes.put("name", productName);
        
        UpdateResult result = Main.database.getProducts().updateOne(eq("id", product.getId()), new Document("$set", new Document(changes)));
        
        System.out.println("\n------------- UPDATE PRODUCT WITH ID -------------");
        System.out.println(" id     : " + productId);
        System.out.println(" name   : " + productName);
        System.out.println(" result :" + result);
        System.out.println("-------------------------------------------------");       
        
        return product;
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param product
     * @return
     */
    public ListenableFuture<Product> updateProductAsync(final Product product) {

        ListenableFuture<Product> future = this.service.submit(new Callable<Product>() {
            @Override
            public Product call() throws Exception {
                return updateProduct(product);
            }
        });

        return future;
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public Product deleteProduct(String id) {

        Product productToDelete = getProduct(id);
        DeleteResult result = Main.database.getProducts().deleteOne(eq("id", id));
        
        System.out.println("\n------------- DELETE PRODUCT WITH ID -------------");
        System.out.println(" id       : " + id);
        System.out.println(" name     : " + productToDelete.getName());
        System.out.println(" result   : " + result);
        System.out.println("-------------------------------------------------"); 
        
        return productToDelete;
        
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Product> deleteProductAsync(final String id) {

        ListenableFuture<Product> future = this.service.submit(new Callable<Product>() {
            @Override
            public Product call() throws Exception {
                return deleteProduct(id);
            }
        });

        return future;
    }

}