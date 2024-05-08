package nastya.ru.myproductsapi.service;

import nastya.ru.myproductsapi.api.request.CreateProductRequest;
import nastya.ru.myproductsapi.api.request.UpdateProductRequest;
import nastya.ru.myproductsapi.api.response.GetAllProductResponse;
import nastya.ru.myproductsapi.api.response.GetProductResponse;
import nastya.ru.myproductsapi.entity.Product;
import nastya.ru.myproductsapi.exception.IdNotFoundException;
import nastya.ru.myproductsapi.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static nastya.ru.myproductsapi.util.filter.ProductSpecification.*;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public GetProductResponse findById(UUID id) {
        return toResponse(
                productRepository.findById(id)
                        .orElseThrow(() -> new IdNotFoundException("product", id))
        );
    }

    @Transactional(readOnly = true)
    public GetAllProductResponse findAll() {
        List<GetProductResponse> products = productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return new GetAllProductResponse(products);
    }

    @Transactional(readOnly = true)
    public GetAllProductResponse findByCriteria(String sort, String title, Double minPrice, Double maxPrice, Boolean isStock) {

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
        if (isStock != null) {
            spec = spec.and(isStock(isStock));
        }
        if (sort != null && !sort.isEmpty()) {
            Sort sortOrder = Sort.by(sort).ascending();
            return new GetAllProductResponse(productRepository.findAll(spec, sortOrder)
                    .stream()
                    .map(this::toResponse)
                    .toList());
        }
        return new GetAllProductResponse(productRepository.findAll(spec)
                .stream()
                .map(this::toResponse)
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
                .orElseThrow(() -> new IdNotFoundException("product", request.getId()));

        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.isStock());
    }

    @Transactional
    public void delete(UUID id) {
        productRepository.deleteById(id);
    }

    private GetProductResponse toResponse(Product product) {
        return modelMapper.map(product, GetProductResponse.class);
    }

    private Product toProduct(CreateProductRequest productRequest) {
        return modelMapper.map(productRequest, Product.class);
    }
}