/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.muchiri.model;

import com.muchiri.dao.ProductDao;
import com.muchiri.exceptions.DatabaseConnectionException;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author developer
 */
@ManagedBean
@RequestScoped
public class Product implements Serializable{

    private Long id;
    private String name;
    private int quantity;
    private BigDecimal buyingPrice;
    private BigDecimal sellingPrice;

    @ManagedProperty(value = "#{productDao}")
    private ProductDao productDao;

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
    }
    public void clear() {
       this.name = "";
       this.quantity = 0;
       this.buyingPrice = BigDecimal.ZERO;
       this.sellingPrice = BigDecimal.ZERO;
    }

}
