/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/**
 *
 * @author julia
 */
public class MySQLConnection {

    private Connection connection;
    private static final MySQLConnection INSTANCE = new MySQLConnection();
    private static String pathOfActivePropopertyFile = "src/main/java/Connection/connect.properties";

    private MySQLConnection() {
    }

    public void connect() {
        try {
            //load properties file
            Properties props = new Properties();
            props.load(new FileInputStream(pathOfActivePropopertyFile));
            //read props
            String probsUser = props.getProperty("user");
            String probsWW = props.getProperty("password");
            String probsUrl = props.getProperty("dburl");
            //connect to database
            connection = DriverManager.getConnection(probsUrl, probsUser, probsWW);
        } catch (Exception ex) {
            System.out.println("geen connectie gemaakt:" + ex);
        }
    }

    public static Connection getConnection() {
        try {
            if (INSTANCE.connection == null) {
                INSTANCE.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return INSTANCE.connection;
    }

    //Toegevoegd om voor het testen het pad te veranderen naar het de testdatabase, zodat de DAOs hier verbinding op maken
    public static void setPathOfActivePropopertyFiletoTest() {
        pathOfActivePropopertyFile = "src/main/java/Connection/connectTestDB.properties";
    }
}
