/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiteshop.daos.mysql;

import Connection.ConnectionFactory;
import Connection.JDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import kiteshop.daos.ProductDaoInterface;
import kiteshop.pojos.BestelRegel;
import kiteshop.pojos.Product;
import kiteshop.utilities.ProjectLog;

/**
 *
 * @author julia
 */
public class ProductDaoSql implements ProductDaoInterface {

    ConnectionFactory factory = new ConnectionFactory();
    private final Logger logger = ProjectLog.getLogger();

    public ProductDaoSql() {
    }

    @Override
    public void createProduct(Product product) {
        String sql = "INSERT INTO product"
                + "(productID, productnaam, voorraad, prijs)"
                + "values (?,?,?,?)";
        try (Connection connection = factory.createConnection(factory.getConnectorType());
                PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, 0);
            statement.setString(2, product.getNaam());
            statement.setInt(3, product.getVoorraad());
            statement.setBigDecimal(4, product.getPrijs());
            statement.execute();
            System.out.println("Product " + product.getNaam() + " is succesvol toegevoegd");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Product> readProductByName(String productnaam) {
        List<Product> producten = new ArrayList<>();

        String query = "Select * from product where productnaam LIKE ? ";
        try (Connection connection = factory.createConnection(factory.getConnectorType());
                PreparedStatement statement = connection.prepareStatement(query);) {

            statement.setString(1, "%" + productnaam + "%");
            try (ResultSet result = statement.executeQuery();) {
            while (result.next()) {
                    Product p = new Product();
                p.setProductID(result.getInt(1));
                p.setNaam(result.getString(2));
                p.setVoorraad(result.getInt(3));
                p.setPrijs(result.getBigDecimal(4));
                    producten.add(p);
            }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return producten;
    }

    @Override
    public Product readProductByID(int productID) {
        Product p = new Product();
        String query = "Select * from product where productID = ?";
        try (Connection connection = factory.createConnection(factory.getConnectorType());
                PreparedStatement statement = connection.prepareStatement(query);) {

            statement.setInt(1, productID);
            try (ResultSet result = statement.executeQuery();) {
            while (result.next()) {
                p.setProductID(result.getInt(1));
                p.setNaam(result.getString(2));
                p.setVoorraad(result.getInt(3));
                p.setPrijs(result.getBigDecimal(4));
            }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return p;
    }

    @Override
    public List<Product> readAllProducten() {
        List<Product> producten = new ArrayList<>();
        String query = "Select * from product";
        try (Connection connection = factory.createConnection(factory.getConnectorType());
                Statement statement = connection.createStatement();) {
            
            try (ResultSet result = statement.executeQuery(query);) {
            while (result.next()) {
                Product p = new Product();
                p.setProductID(result.getInt(1));
                p.setNaam(result.getString(2));
                p.setVoorraad(result.getInt(3));
                p.setPrijs(result.getBigDecimal(4));
                producten.add(p);
            }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return producten;
    }

    @Override
    public void updateProduct(Product product) {
        System.out.println("updaten naar database");
        String update = "UPDATE product SET productnaam = ?, voorraad = ?, prijs = ? where productID = ?";
        try (Connection connection = factory.createConnection(factory.getConnectorType());
                PreparedStatement statement = connection.prepareStatement(update)) {

            statement.setString(1, product.getNaam());
            statement.setInt(2, product.getVoorraad());
            statement.setBigDecimal(3, product.getPrijs());
            statement.setInt(4, product.getProductID());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(Product product) {
        String query1 = " DELETE FROM product "
                + " WHERE productID = " + product.getProductID();
        String query = " DELETE FROM bestel_regel "
                + " WHERE productID = " + product.getProductID();
        try (Connection connection = factory.createConnection(factory.getConnectorType());
                Statement statement = connection.createStatement();) {
            //First delete 'children' dwz bestelregel
            statement.executeUpdate(query);
            //Then delete 'mother' dwz product
            statement.executeUpdate(query1);
            logger.info("Deleting");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
