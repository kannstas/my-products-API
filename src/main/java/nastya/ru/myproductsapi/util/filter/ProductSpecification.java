package nastya.ru.myproductsapi.util.filter;

import nastya.ru.myproductsapi.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> isStock(boolean isStock) {
        return (productRoot, cq, cb) ->
                cb.equal(productRoot.get("isStock"), isStock);
    }

    public static Specification<Product> titleContains(String title) {
        return (productRoot, cq, cb) -> cb.like(productRoot.get("title"), "%" + title + "%");
    }

    public static Specification<Product> priceLessThan(double maxPrice) {
        return (productRoot, cq, cb) -> cb.lessThanOrEqualTo(productRoot.get("price"), maxPrice);
    }

    public static Specification<Product> priceGreaterThan(double minPrice) {
        return (productRoot, cq, cb) -> cb.greaterThanOrEqualTo(productRoot.get("price"), minPrice);
    }
}