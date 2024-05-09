package nastya.ru.myproductsapi.service;

import nastya.ru.myproductsapi.api.request.product.CreateProductRequest;
import nastya.ru.myproductsapi.api.request.product.UpdateProductRequest;
import nastya.ru.myproductsapi.api.response.product.GetAllProductResponse;
import nastya.ru.myproductsapi.api.response.product.GetProductResponse;
import nastya.ru.myproductsapi.entity.Product;
import nastya.ru.myproductsapi.exception.IdNotFoundException;
import nastya.ru.myproductsapi.repository.ProductRepository;
import nastya.ru.myproductsapi.util.conversion.Convert;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static nastya.ru.myproductsapi.util.conversion.Convert.toProduct;
import static nastya.ru.myproductsapi.util.conversion.Convert.toResponse;
import static nastya.ru.myproductsapi.util.filter.ProductSpecification.*;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public GetProductResponse findById(UUID id) {
        return toResponse(
                productRepository.findById(id)
                        .orElseThrow(() -> new IdNotFoundException("products", "product", id))
        );
    }

    @Transactional(readOnly = true)
    public GetAllProductResponse findAll() {
        List<GetProductResponse> products = productRepository.findAll()
                .stream()
                .map(Convert::toResponse)
                .toList();

        return new GetAllProductResponse(products);
    }

    @Transactional(readOnly = true)
    public GetAllProductResponse findByCriteria(String sort,
                                                String title,
                                                Double minPrice,
                                                Double maxPrice,
                                                Integer quantity) {

        Specification<Product> spec = Specification.where(null);

        if (title != null && !title.isEmpty()) {
            spec = spec.and(titleContains(title));
        }
        if (minPrice != null) {
            spec = spec.and(priceGreaterThan(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(priceLessThan(maxPrice));
        }
        if (quantity != null) {
            spec = spec.and(quantityIs(quantity));
        }
        if (sort != null && !sort.isEmpty()) {
            Sort sortOrder = Sort.by(sort).ascending();
            return new GetAllProductResponse(productRepository.findAll(spec, sortOrder)
                    .stream()
                    .map(Convert::toResponse)
                    .toList());
        }
        return new GetAllProductResponse(productRepository.findAll(spec)
                .stream()
                .map(Convert::toResponse)
                .toList());
    }

    @Transactional
    public void save(CreateProductRequest request) {
        Product product = toProduct(request);
        product.setId(UUID.randomUUID());
        productRepository.save(product);
    }

    @Transactional
    public void update(UpdateProductRequest request) {
        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new IdNotFoundException("products", "product", request.getId()));

        Optional.ofNullable(request.getTitle()).ifPresent(product::setTitle);
        Optional.ofNullable(request.getDescription()).ifPresent(product::setDescription);
        Optional.of(request.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(request.getQuantity()).ifPresent(product::setQuantity);

    }

    @Transactional
    public void delete(UUID id) {
        productRepository.deleteById(id);
    }

}