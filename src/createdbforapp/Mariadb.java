/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package createdbforapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author jvm
 */
public class Mariadb {

    void createDb(Connection conn, String jdbcdrivername, String url, String dbname, String username, String password) {
        try {
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
        } catch (SQLException ex) {
            System.out.println("Exeption transaction");
            System.out.println(ex);
        }
    }
    
}
