# EasyShop API

## Description
The EasyShop API is a backend application designed to power an online e-commerce platform. This project was built with Spring Boot and uses MySQL for database management. The project simulates key functionalities of an online store, such as managing products, categories, user profiles, and shopping carts.

We were provided with an initial project codebase that included basic DAO interfaces, models, and some controllers. Our task was to review the existing code, fix bugs, and expand the application with new features such as shopping cart functionality and user profile management.

## Home Screen
![img.png](img.png)


## Cart Screen
![img_1.png](img_1.png)

## Profile Screen
![img_2.png](img_2.png)

## Favorite Piece of Code
Below is my favorite piece of code from the project. This snippet is from the MyShoppingCartDao class, specifically the 'getByUserId' method. This piece holds special significance because of the challenges I faced while debugging it.
```
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
```
## Why This Code is My Favorite
When testing the program’s getCart method, I encountered a persistent server 500 error. After much effort debugging, I discovered that the issue stemmed from a subtle mistake: misplacing the featured and image_url columns in the ResultSet.

This mismatch caused the Product constructor to throw an error when mapping the database results to the object. While the application didn’t break outright, this issue blocked the cart retrieval functionality.

After resolving the problem and seeing the method work as intended, I felt a great sense of accomplishment and relief. This experience reinforced the importance of careful attention to detail when working with database queries and mapping.

## Work Completed
Phase 1
----------------------------------
Filled out all methods in CategoriesController.

Phase 2
----------------------------------
- Fixed bug in MySqlProductDao search method by adding extra logic to query for '>=' and adjusted maxPrice to be used correctly.
- Corrected code in our updateProduct method in the ProductsController class to pass in the id and product.


Phase 3
----------------------------------
1. Updated ShoppingCartDao interface by adding additional methods for: POST, PUT, DELETE.

2. Created MySqlShoppingCartDao

3. Update ShoppingCartController by: 
- Adding correct annotations
- Adding a constructor
- Adjusting the getCart() method to return shoppingCartDao.getUserId(userId);
- Implemented additional methods for: POST, PUT, DELETE

Phase 4
----------------------------------
1. Updated ProfileDao interface by adding the getUser and updateUser methods.

2. Updated the given MySqlProfileDao by implementing and Overriding our new methods.

3. Created the ProfileController