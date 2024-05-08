package nastya.ru.myproductsapi.service;

import nastya.ru.myproductsapi.api.request.CreateProductRequest;
import nastya.ru.myproductsapi.api.request.UpdateProductRequest;
import nastya.ru.myproductsapi.api.response.GetAllProductResponse;
import nastya.ru.myproductsapi.api.response.GetProductResponse;
import nastya.ru.myproductsapi.dao.ProductDAO;
import nastya.ru.myproductsapi.exception.BusinessLogicException;
import nastya.ru.myproductsapi.exception.IdNotFoundException;
import nastya.ru.myproductsapi.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductDAO productDAO;
    private final ModelMapper modelMapper;

    public ProductService(ProductDAO productDAO, ModelMapper modelMapper) {
        this.productDAO = productDAO;
        this.modelMapper = modelMapper;
    }

    public GetProductResponse findById(UUID id) {
        Product product = productDAO.findById(id);
        if (product == null) {
            throw new IdNotFoundException("product", id);
        }
        return toResponse(product);
    }

    public GetAllProductResponse findAll() {
        List<Product> products = Optional.ofNullable(productDAO.findAll())
                .orElseThrow(() -> new BusinessLogicException("Лист products пуст"));

        return new GetAllProductResponse(products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    public void save(CreateProductRequest request) {
        Product product = toProduct(request);
        product.setId(UUID.randomUUID());

        productDAO.save(product);
    }

    public void update(UpdateProductRequest request) {
        productDAO.update(
                toProduct(request)
        );
    }

    public void delete(UUID id) {
        productDAO.delete(id);
    }

    private GetProductResponse toResponse(Product product) {
        return modelMapper.map(product, GetProductResponse.class);
    }

    private Product toProduct(CreateProductRequest productRequest) {
        return modelMapper.map(productRequest, Product.class);
    }

    private Product toProduct(UpdateProductRequest productRequest) {
        return modelMapper.map(productRequest, Product.class);
    }
}