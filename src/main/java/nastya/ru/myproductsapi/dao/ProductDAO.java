package nastya.ru.myproductsapi.dao;

import nastya.ru.myproductsapi.exception.BusinessLogicException;
import nastya.ru.myproductsapi.exception.IdNotFoundException;
import nastya.ru.myproductsapi.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ProductDAO {
    private final List<Product> products = new ArrayList<>();

    public Product findById(UUID id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        throw new IdNotFoundException("product", id);
    }

    public List<Product> findAll() {
        return products;
    }

    public void save(Product product) {
        products.add(product);
    }

    public void update(Product productToUpdate) {
        for (int index = 0; index < products.size(); index++) {
            Product product = products.get(index);
            if (product.getId().equals(productToUpdate.getId())) {
                products.set(index, productToUpdate);
                return;
            }
        }
        throw new BusinessLogicException("невозможно обновить product");
    }

    public void delete(UUID id) {
        for (int index = 0; index < products.size(); index++) {
            Product product = products.get(index);
            if (product.getId().equals(id)) {
                products.remove(product);
            }
        }
    }
}