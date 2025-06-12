package service;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;
import productDAO.IProductDAO;
import productDAO.ProductDAO;

public class ProductService implements IProductService {
    private IProductDAO productDao;

    public ProductService() {
        this.productDao = new ProductDAO();
    }

    @Override
    public void createProduct(Product product) {
        validateProduct(product);
        try {
            productDao.insertProduct(product);
        } catch (SQLException ex) {
            Logger.getLogger(ProductService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Product getProductById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID không hợp lệ!");
        }
        return productDao.selectProduct(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productDao.selectAllProducts();
    }

    @Override
    public boolean updateProduct(Product product) {
        if (product.getId() <= 0) {
            throw new IllegalArgumentException("ID không hợp lệ!");
        }
        validateProduct(product);
        try {
            return productDao.updateProduct(product);
        } catch (SQLException ex) {
            Logger.getLogger(ProductService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean deleteProduct(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID không hợp lệ!");
        }
        try {
            return productDao.deleteProduct(id);
        } catch (SQLException ex) {
            Logger.getLogger(ProductService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống!");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Giá sản phẩm không được âm!");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Số lượng tồn kho không được âm!");
        }
    }
}
