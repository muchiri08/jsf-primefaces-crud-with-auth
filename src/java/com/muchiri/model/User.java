/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.muchiri.model;

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

/**
 *
 * @author developer
 */
@ManagedBean
@SessionScoped
public class User {
    
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
    
    public String submit(){
        try{
            System.out.println("starting class");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/crud_db", "mysql", "mysql123");
            System.out.println(conn);
            String sql = "SELECT username, role FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            String encryptedPassword = DatatypeConverter.printHexBinary(digest);
         
            statement.setString(1, username);
            statement.setString(2, encryptedPassword);
            ResultSet rs = statement.executeQuery();
            
            if(rs.next()){
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("role", rs.getString("role"));
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("role", rs.getString("username"));
                return "/home";
            } else{
                //FacesContext.getCurrentInstance().addMessage("loginForm:username", new FacesMessage("Invalid username or password"));
                FacesContext.getCurrentInstance().addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null));
                return "";
            }
            
        } catch(SQLException e){
            FacesContext.getCurrentInstance().addMessage("message", new FacesMessage("An error occured, please try again later."));
            return null;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
