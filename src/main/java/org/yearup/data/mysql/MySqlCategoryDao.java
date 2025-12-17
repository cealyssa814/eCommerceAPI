package org.yearup.data.mysql;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.yearup.data.CategoryDao;
import org.yearup.data.mysql.MySqlDaoBase;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {

    @Autowired
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY category_id";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet row = statement.executeQuery()) {

            while (row.next()) {
                Category category = mapRow(row);
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);

            try (ResultSet row = statement.executeQuery()) {
                if (row.next()) {
                    return mapRow(row);
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category create(Category category) {
        String sql = "INSERT INTO categories(name, description) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Creating category failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newCategoryId = generatedKeys.getInt(1);
                    return getById(newCategoryId);
                } else {
                    throw new RuntimeException("Creating category failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int categoryId, Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Category not found for update");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId) {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Category not found for deletion");
            }

        } catch (SQLException e) {
            // FIX: Handle foreign key constraint error
            if ("23000".equals(e.getSQLState())) {
                throw new IllegalStateException("Category is in use and cannot be deleted.");
            }
            throw new RuntimeException(e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setName(name);
        category.setDescription(description);
        return category;
    }
}