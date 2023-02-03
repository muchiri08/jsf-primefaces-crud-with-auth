/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.muchiri.model;

import com.muchiri.Navigator;
import com.muchiri.dao.ProductDao;
import com.muchiri.exceptions.DatabaseConnectionException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.PrimeFaces;

/**
 *
 * @author developer
 */
@ManagedBean
@SessionScoped
public class Product implements Serializable {
    
    private Long id;
    private String name;
    private int quantity;
    private BigDecimal buyingPrice;
    private BigDecimal sellingPrice;
    
    private List<Product> products;
    private Product selectedProduct;
    
    @ManagedProperty(value = "#{productDao}")
    private ProductDao productDao;
    @ManagedProperty(value = "#{navigator}")
    private Navigator navigator;
    
    public Navigator getNavigator() {
        return navigator;
    }
    
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }
    
    public Product getSelectedProduct() {
        return selectedProduct;
    }
    
    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }
    
    public List<Product> getProducts() {
        return products;
    }
    
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    public ProductDao getProductDao() {
        return productDao;
    }
    
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getBuyingPrice() {
        return buyingPrice;
    }
    
    public void setBuyingPrice(BigDecimal buyingPrice) {
        this.buyingPrice = buyingPrice;
    }
    
    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }
    
    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    //Operations
    public void saveProduct(Product product) throws DatabaseConnectionException {
        productDao.createProduct(product);
        clear();
        init();
    }
    
    public void clear() {
        this.name = "";
        this.quantity = 0;
        this.buyingPrice = BigDecimal.ZERO;
        this.sellingPrice = BigDecimal.ZERO;
    }
    
    @PostConstruct
    public void init() {
        
        products = productDao.getAllProducts();
    }
    
    public void onEdit(Product product) {
        System.out.println("onEdit called");
        selectedProduct = product;
        PrimeFaces.current().executeInitScript("PF('editProductDialog').show();");
    }
    
    public void updateProduct(Product product) {
        productDao.updateProduct(product);
        navigator.allProductsPage();
        init();
    }
    
    public void deleteProduct(Product product) {
        System.out.println("Product id is: " + product.id);
        productDao.deleteProduct(product.id);
        navigator.allProductsPage();
        init();
    }
    
}
