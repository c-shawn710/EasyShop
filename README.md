# EasyShop

```
MyShoppingCartDao.java
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
                         rs.getBoolean("featured"),
                         rs.getString("image_url")
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
```
