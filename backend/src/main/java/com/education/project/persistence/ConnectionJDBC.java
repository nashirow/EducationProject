package com.education.project.persistence;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Singleton de la connexion vers la
 * base de données. Cette classe est à
 * utiliser tant que la couche de données
 * utilise JDBC.
 */
public class ConnectionJDBC {

    /**
     * Instance
     */
    private static ConnectionJDBC connectionJDBC = null;

    /**
     * Connexion JDBC
     */
    private Connection connection;

    /**
     * Identifiant utilisateur
     */
    @Value("${db.user}")
    private String user;

    /**
     * Mot de passe utilisateur
     */
    @Value("${db.password}")
    private String password;

    /**
     * URL de la base de données
     */
    @Value("${db.url}")
    private String url;

    /**
     * Driver utilisé déterminant la base de données à utiliser
     */
    @Value("${db.driver}")
    private String driver;

    /**
     * Construit l'unique instance de ConnectionJDBC.
     */
    private ConnectionJDBC(){
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url,user,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// ConnectionJDBC()

    /**
     * Récupère l'unique instance ConnectionJDBC
     * @return instance ConnectionJDBC
     */
    public static ConnectionJDBC getInstance(){
        if(connectionJDBC == null){
            connectionJDBC = new ConnectionJDBC();
        }
        return connectionJDBC;
    }// getInstance()

    /**
     * Récupère l'unique connexion active vers la base de données.
     * @return connexion JDBC
     */
    public Connection getConnectionInstance(){
        return this.connection;
    }// getConnectionInstance()

}// ConnectionJDBC
