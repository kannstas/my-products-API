package nastya.ru.myproductsapi.service;

import nastya.ru.myproductsapi.api.request.product.CreateProductRequest;
import nastya.ru.myproductsapi.api.request.product.UpdateProductRequest;
import nastya.ru.myproductsapi.api.response.product.GetAllProductResponse;
import nastya.ru.myproductsapi.api.response.product.GetProductResponse;
import nastya.ru.myproductsapi.entity.Product;
import nastya.ru.myproductsapi.exception.IdNotFoundException;
import nastya.ru.myproductsapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    void testFindProductByIdSuccess() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setTitle("Test Title");

        GetProductResponse expectedResponse = new GetProductResponse();
        expectedResponse.setId(product.getId());
        expectedResponse.setTitle(product.getTitle());

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        GetProductResponse response = productService.findById(product.getId());

        verify(productRepository, times(1)).findById(product.getId());

        assertThat(product.getId()).isEqualTo(response.getId());
        assertThat(product.getTitle()).isEqualTo(response.getTitle());

    }

    @Test
    void testFindProductByIdNotFoundShouldThrow() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setTitle("Test Title");
        product.setDescription("Test Description");
        product.setPrice(9.53);
        product.setQuantity(10);

        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(product.getId()))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessageContaining("В таблице products нет %s с id=%s".formatted("product", product.getId()));
    }

    @Test
    void testFindAllProductsSuccess() {
        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        product1.setTitle("Product 1");
        product1.setPrice(9.33);

        Product product2 = new Product();
        product2.setId(UUID.randomUUID());
        product2.setTitle("Product 2");
        product2.setPrice(302.1);

        List<Product> productList = Arrays.asList(product1, product2);

        List<GetProductResponse> expectedProductsResponse = getGetProductResponses(product1, product2);

        when(productRepository.findAll()).thenReturn(productList);

        GetAllProductResponse response = productService.findAll();
        GetAllProductResponse expectedResponse = new GetAllProductResponse(expectedProductsResponse);

        assertThat(expectedResponse)
                .usingRecursiveComparison()
                .isEqualTo(response);

    }

    private static List<GetProductResponse> getGetProductResponses(Product product1, Product product2) {
        GetProductResponse productResponse1 = new GetProductResponse();
        productResponse1.setId(product1.getId());
        productResponse1.setTitle(product1.getTitle());
        productResponse1.setPrice(product1.getPrice());

        GetProductResponse productResponse2 = new GetProductResponse();
        productResponse2.setId(product2.getId());
        productResponse2.setTitle(product2.getTitle());
        productResponse2.setPrice(product2.getPrice());

        List<GetProductResponse> expectedProductsResponse = Arrays.asList(productResponse1, productResponse2);
        return expectedProductsResponse;
    }


    @Test
    void testSaveProductSuccess() {
        CreateProductRequest request = new CreateProductRequest();
        request.setTitle("Test Title");
        request.setDescription("Test Description");
        request.setPrice(9.56);
        request.setQuantity(10);

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setTitle("Test Title");
        product.setDescription("Test Description");
        product.setPrice(9.53);
        product.setQuantity(10);


        productService.save(request);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product productToBeSaved = captor.getValue();

        assertThat(request)
                .usingRecursiveComparison()
                .isEqualTo(productToBeSaved);
    }

    @Test
    void testUpdateProductSuccess() {
        UUID id = UUID.randomUUID();
        UpdateProductRequest request = new UpdateProductRequest();
        request.setId(id);
        request.setTitle("Test Title");
        request.setDescription("Test Description");
        request.setPrice(9.56);
        request.setQuantity(10);

        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setTitle("Old Title");
        existingProduct.setDescription("Old Description");
        existingProduct.setPrice(10.0);
        existingProduct.setQuantity(10);

        when(productRepository.findById(request.getId())).thenReturn(Optional.of(existingProduct));

        productService.update(request);

        assertThat(request)
                .usingRecursiveComparison()
                .isEqualTo(existingProduct);
    }

    @Test
    void deleteSuccess() {
        Product product = new Product();
        product.setId(UUID.randomUUID());

        productService.delete(product.getId());

        verify(productRepository).deleteById(product.getId());
    }
}
