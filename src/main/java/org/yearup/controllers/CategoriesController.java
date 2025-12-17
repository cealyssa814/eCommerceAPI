package org.yearup.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
import java.util.List;
@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController
{
    private final CategoryDao categoryDao;
    private final ProductDao productDao;
    // FIX: Use constructor injection with @Autowired
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao)
    {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }
    // GET /categories
    @GetMapping
    public List<Category> getAll()
    {
        List<Category> categories = categoryDao.getAllCategories();
        if (categories == null || categories.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No categories found.");
        }
        return categories;
    }
    // GET /categories/{id}
    @GetMapping("/{id}")
    public Category getById(@PathVariable int id)
    {
        Category category = categoryDao.getById(id);
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
        }
        return category;
    }

    @GetMapping("/{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        List<Product> products = productDao.listByCategoryId(categoryId);
        if (products == null || products.isEmpty()) { // FIX: prevent 500 if no products
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No products found for this category.");
        }
        return products;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category)
    {
        if (category == null || category.getName() == null || category.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name is required."); // FIX: validate input
        }
        return categoryDao.create(category);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        if (categoryDao.getById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found for update.");
        }
        categoryDao.update(id, category);
    }
    // DELETE /categories/{id} (ADMIN only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int id)
    {
        if (categoryDao.getById(id) == null) { // FIX: check existence
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found for deletion.");
        }
        categoryDao.delete(id); // FIX: DAO handles foreign key errors gracefully
    }
}

