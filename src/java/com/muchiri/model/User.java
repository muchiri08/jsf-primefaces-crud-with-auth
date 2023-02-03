/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.muchiri.model;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.LoggerFactory;

/**
 *
 * @author developer
 */
@ManagedBean
@SessionScoped
public class User implements Serializable{
    
    private final org.slf4j.Logger log = LoggerFactory.getLogger(User.class);

    private String id;
    private String username;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String submit() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/crud_db", "mysql", "mysql123");
            String sql = "SELECT username, role FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = conn.prepareStatement(sql);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            String encryptedPassword = DatatypeConverter.printHexBinary(digest);

            statement.setString(1, username);
            statement.setString(2, encryptedPassword);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("role", rs.getString("role"));
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("role", rs.getString("username"));
                log.info(username+" logged in successfully");
                return "/home";
            } else {
                FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null));
                log.error("Invalid username or password");
                return null;
            }

        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage("Invalid username or password."));
            log.error("Error login in!", e);

            return null;
        } catch (NoSuchAlgorithmException ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        log.info("Logged out successfully.");
        return "index?faces-redirect=true";
    }

}
