/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.muchiri.dao;

import com.muchiri.exceptions.DatabaseConnectionException;
import com.muchiri.model.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author developer
 */
@ManagedBean(name = "productDao")
@RequestScoped
public class ProductDao {

    private final Logger log = LoggerFactory.getLogger(ProductDao.class);

    public static Connection connection;
    public static ResultSet resultSet;
    public static PreparedStatement preparedStatement;

    public final Connection getConnection() throws DatabaseConnectionException {
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/crud_db", "mysql", "mysql123");

        } catch (SQLException e) {
            log.error("Error connectiong to database");
            //throw new DatabaseConnectionException("Error while connecting to database", e);
        } catch (ClassNotFoundException e) {
            //throw new DatabaseConnectionException(e.getMessage(), e);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return connection;
    }

    public void createProduct(Product product) throws DatabaseConnectionException {
        System.out.println(product.getName());
        System.out.println(product.getQuantity());
        System.out.println(product.getBuyingPrice());
        System.out.println(product.getSellingPrice());
        connection = null;
        preparedStatement = null;
        try {
            connection = getConnection();
            System.out.println("This is the connection log"+connection.toString());
            String query = "INSERT INTO products(name, quantity, buying_price, selling_price) VALUES (?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getQuantity());
            preparedStatement.setBigDecimal(3, product.getBuyingPrice());
            preparedStatement.setBigDecimal(4, product.getSellingPrice());

            preparedStatement.executeUpdate();
            
            FacesContext.getCurrentInstance().addMessage("Success: ",  new FacesMessage(FacesMessage.SEVERITY_INFO, "Product created successfully", null));

        } catch (SQLException e) {
            log.error("Error while creating product", e);
        } finally {
            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("Error while clossing resources", e);
            }
        }
    }

}
