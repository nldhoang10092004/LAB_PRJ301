package service;

import java.util.List;
import model.Product;

public interface IProductService {
    void createProduct(Product product);
    Product getProductById(int id);
    List<Product> getAllProducts();
    boolean updateProduct(Product product);
    boolean deleteProduct(int id);
}
