/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package createdbforapp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jvm
 */
public class CreateDbForApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         
        Scanner scanner = new Scanner(System.in);
        String quit = "";
        try {
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
            String url = "jdbc:mysql://localhost?serverTimezone=Europe/Tallinn&useSSL=false&useUnicode=true&characterEncoding=utf8";
            String username = "root";
            String password = "";
            Connection conn = null;
            try {
               conn = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                System.out.println("Error connection to database");
               do{
                   System.out.println("URL database(jdbc:mysql://localhost): ");
                   url = scanner.nextLine();
                   if(!url.contains("?"))
                       url = url+"?serverTimezone=Europe/Tallinn&useSSL=false&useUnicode=true&characterEncoding=utf8";
                   System.out.println("Login database admin (root): ");
                   username = scanner.nextLine();
                   System.out.println("Password for admin: ");
                   password = scanner.nextLine();
                   try {   
                        conn = DriverManager.getConnection(url, username, password);
                        quit = "q";
                   } catch (SQLException ex1) {
                       System.out.println("Connection faled. \n(For exit press 'q', to continue press any key)\n");
                       quit = scanner.next();
                   }
               }while(!quit.equals("q"));
               if (conn == null){
                   System.out.println("Goodbye!");
                   System.exit(0);
               }
            }
            Properties props = new Properties();
            String dbname=null;
            try(InputStream in = Files.newInputStream(Paths.get("database.properties"))){
                props.load(in);
                dbname = props.getProperty("dbname");
                username = props.getProperty("username");
                password = props.getProperty("password");
            } catch (IOException ex) {
               Logger.getLogger(CreateDbForApp.class.getName()).log(Level.SEVERE, "Failed read properties.");
               System.out.println("Your database name: ");
               dbname = scanner.nextLine();
               System.out.println("Username your database: ");
               username = scanner.nextLine();
               System.out.println("Password your database: ");
               password = scanner.nextLine();
            }
            
            String query = "CREATE DATABASE "+dbname+" CHARACTER SET utf8 COLLATE utf8_general_ci";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.execute();
            System.out.println("Database \""+dbname+"\" created");
            query = "CREATE USER '"+username+"'@'localhost' IDENTIFIED BY '"+password+"'";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.execute();
            query = "GRANT ALL PRIVILEGES ON "+dbname+" . * TO '"+username+"'@'localhost'";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.execute();
            query = "FLUSH PRIVILEGES";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.execute();
            System.out.println("Username \""+username+"\" with ALL PRIVELEGES to database \""+dbname+"\" created.");
            
            
        } catch (ClassNotFoundException 
               | NoSuchMethodException 
               | SecurityException 
               | InstantiationException 
               | IllegalAccessException 
               | IllegalArgumentException 
               | InvocationTargetException 
               | SQLException ex) {
            System.out.println("Failed create database: ");
            System.out.println(ex);
        }
       System.out.println("Pres any key for exit");
       scanner.next();
       System.out.println("Goodbye!");
    }//end main
}
