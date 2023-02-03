/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.muchiri.dao;

import com.muchiri.exceptions.DatabaseConnectionException;
import com.muchiri.model.Product;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author developer
 */
@ManagedBean(name = "productDao")
@SessionScoped
public class ProductDao implements Serializable {

    private final Logger log = LoggerFactory.getLogger(ProductDao.class);

    public static Connection connection;
    public static ResultSet resultSet;
    public static PreparedStatement preparedStatement;

    public final Connection getConnection() throws DatabaseConnectionException {
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/crud_db", "mysql", "mysql123");

        } catch (SQLException e) {
            log.error("Error while connecting to database", e);
        } catch (ClassNotFoundException e) {
            log.error("Driver not found!", e);
        } catch (InstantiationException ex) {
            log.error(ex.getMessage());
        } catch (IllegalAccessException ex) {
            log.error(ex.getMessage());
        }

        return connection;
    }

    public void createProduct(Product product) throws DatabaseConnectionException {
        connection = null;
        preparedStatement = null;
        try {
            connection = getConnection();
            String query = "INSERT INTO products(name, quantity, buying_price, selling_price) VALUES (?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getQuantity());
            preparedStatement.setBigDecimal(3, product.getBuyingPrice());
            preparedStatement.setBigDecimal(4, product.getSellingPrice());

            preparedStatement.executeUpdate();

            FacesContext.getCurrentInstance().addMessage("Success: ", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product created successfully", null));
            log.info("Product created successfully!");

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

    public List<Product> getAllProducts() {
        log.info("Get all product called");
        connection = null;
        preparedStatement = null;
        resultSet = null;
        List<Product> products = new ArrayList<>();

        try {
            String query = "SELECT id, name, quantity, buying_price, selling_price FROM products ORDER BY id DESC";
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getLong("id"));
                product.setName(resultSet.getString("name"));
                product.setQuantity(resultSet.getInt("quantity"));
                product.setBuyingPrice(resultSet.getBigDecimal("buying_price"));
                product.setSellingPrice(resultSet.getBigDecimal("selling_price"));
                products.add(product);
            }
        } catch (Exception e) {
            log.error("Error while retrieving products!", e);
        } finally {
            try {
                if (resultSet != null && !resultSet.isClosed()) {
                    resultSet.close();
                }
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                log.error("Error clossing resources!", e);
            }
        }
        return products;
    }

    public void updateProduct(Product product) {
        connection = null;
        preparedStatement = null;
        int rowCount = 0;

        try {
            String query = "UPDATE products SET name = " + product.getName() + ", quantity = " + product.getQuantity() + ", buying_price = " + product.getBuyingPrice() + ", selling_price = " + product.getSellingPrice() + " WHERE id = " + product.getId();
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            rowCount = preparedStatement.executeUpdate();

            if (rowCount == 1) {
                log.info("Product updated successfully");
                FacesContext.getCurrentInstance().addMessage("Success: ", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product updated successfully", null));
            } else {
                log.error("Error updating the product");
                FacesContext.getCurrentInstance().addMessage("Success: ", new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated unsuccesfull! Try again later.", null));
            }

        } catch (Exception e) {
            log.error("Error updating the product");
        }
    }

    public void deleteProduct(Long id) {
        System.out.println("delete product dao called");
        System.out.println(id);
        connection = null;
        preparedStatement = null;
        int rowCount = 0;

        try {
            String query = "DELETE FROM products WHERE id = " + id;
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            rowCount = preparedStatement.executeUpdate();
            System.out.println("Row count: "+rowCount);

            if (rowCount == 1) {
                FacesContext.getCurrentInstance().addMessage("Success", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product deleted successfully", null));
            } else {
                FacesContext.getCurrentInstance().addMessage("Success", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete unsuccessful! Try again later.", null));
            }
        } catch (Exception e) {
            log.error("Error deleting product!");
        } finally {
            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                log.error("Error closing resources");
            }
        }
    }

}
