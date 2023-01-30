/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.muchiri;

import javax.faces.bean.ManagedBean;

/**
 *
 * @author developer
 */
@ManagedBean
public class Navigator {
    
    String contentPage = "addproduct.xhtml";
    
    public String showProducts(){
        contentPage = "products.xhtml";
        
        return contentPage;
    }

    public String getContentPage() {
        return contentPage;
    }

    public void setContentPage(String contentPage) {
        this.contentPage = contentPage;
    }
    
    public void addProductPage(){
        this.contentPage = "addproduct.xhtml";
    }
    
    public void allProductsPage(){
        this.contentPage = "products.xhtml";
    }
    
    
    
}
