package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MyShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    public MyShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        String query = """
                SELECT sc.product_id, sc.quantity, p.name, p.price, p.category_id, p.description, p.color, p.stock, p.image_url, p.featured
                FROM shopping_cart sc
                JOIN products p ON sc.product_id = p.product_id
                WHERE sc.user_id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             statement.setInt(1, userId);

             ResultSet rs = statement.executeQuery();

             while (rs.next()) {
                 Product product = new Product(
                         rs.getInt("product_id"),
                         rs.getString("name"),
                         rs.getBigDecimal("price"),
                         rs.getInt("category_id"),
                         rs.getString("description"),
                         rs.getString("color"),
                         rs.getInt("stock"),
                         rs.getBoolean("image_url"),
                         rs.getString("featured")
                 );

                 ShoppingCartItem item = new ShoppingCartItem();
                 item.setProduct(product);
                 item.setQuantity(rs.getInt("quantity"));

                 cart.add(item);
             }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving shopping cart.");
        }
        return cart;
    }

    @Override
    public void addItem(int userId, int productId, int quantity) {
        String checkQuery = "SELECT COUNT(*) FROM shopping_cart WHERE user_id = ? AND product_Id = ?";
        String insertQuery = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateQuery = "UPDATE shopping_cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
             checkStatement.setInt(1, userId);
             checkStatement.setInt(2, productId);

             ResultSet rs = checkStatement.executeQuery();
             rs.next();
             int count = rs.getInt(1);

             if (count == 0) {
                 //Insert new row
                 try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                     insertStatement.setInt(1, userId);
                     insertStatement.setInt(2, productId);
                     insertStatement.setInt(3, quantity);
                     insertStatement.executeUpdate();
                 }
             } else {
                 //Update existing row
                 try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                     updateStatement.setInt(1, quantity);
                     updateStatement.setInt(2, userId);
                     updateStatement.setInt(3, productId);
                     updateStatement.executeUpdate();
                 }
             }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding item to cart.");
        }
    }

    public void updateItem(int userId, int productId, int quantity) {
        String query = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             statement.setInt(1, quantity);
             statement.setInt(2, userId);
             statement.setInt(3, productId);

             statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating item in cart.");
        }
    }

    public void clearCart(int userId) {
        String query = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             statement.setInt(1, userId);

             statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error clearing cart.");
        }
    }
}
