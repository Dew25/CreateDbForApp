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
        Connection conn = null;
        Properties props = new Properties();
        String jdbcdrivername = null;
        String dbname = null;
        String username = null;
        String password = null;
        String root = null;
        String rootpassword = null;
        String url = null;
        try {
            try(InputStream in = Files.newInputStream(Paths.get("database.properties"))){
                props.load(in);
                jdbcdrivername = props.getProperty("jdbcdrivername");
                url = props.getProperty("url");
                root = props.getProperty("root");
                rootpassword = props.getProperty("rootpassword");
                dbname = props.getProperty("dbname");
                username = props.getProperty("username");
                password = props.getProperty("password");
            } catch (IOException ex) {
               System.out.println("Failed read properties.");
               System.out.println(ex);
               System.out.println("jdbcdrivername: (com.mysql.jdbc.Driver) ");
               jdbcdrivername = scanner.nextLine();
               System.out.println("url");
               url = scanner.nextLine();
               System.out.println("root");
               root = scanner.nextLine();
               System.out.println("rootpassword: ");
               rootpassword = scanner.nextLine();
               System.out.println("Your database name: ");
               dbname = scanner.nextLine();
               System.out.println("Your database name: ");
               dbname = scanner.nextLine();
               System.out.println("Username your database: ");
               username = scanner.nextLine();
               System.out.println("Password your database: ");
               password = scanner.nextLine();
            }
            if(url != null && url.equals("jdbc:mysql://localhost")){ 
                Class.forName(jdbcdrivername).getDeclaredConstructor().newInstance();
                url = url+"?serverTimezone=Europe/Tallinn&useSSL=false&useUnicode=true&characterEncoding=utf8";
                try {
                    conn = DriverManager.getConnection(url, root, rootpassword);
                } catch (SQLException ex) {
                    System.out.println("Error connection to database");
                    do{
                       System.out.println("URL database (jdbc:mysql://localhost):");
                       url = scanner.nextLine();
                       if(!url.equals("jdbc:mysql://localhost"))
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
                Mariadb mariadb = new Mariadb();
                mariadb.createDb(conn, jdbcdrivername,url,dbname,username,password);
            }    
        } catch (ClassNotFoundException 
                | NoSuchMethodException 
                | SecurityException 
                | InstantiationException 
                | IllegalAccessException 
                | IllegalArgumentException 
                | InvocationTargetException ex){ 
            System.out.println("Failed create database: ");
            System.out.println(ex);
        }
       System.out.println("Pres any key for exit");
       scanner.next();
       System.out.println("Goodbye!");
    }//end main
}
