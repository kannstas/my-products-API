package nastya.ru.myproductsapi.service;

import nastya.ru.myproductsapi.api.request.CreateProductRequest;
import nastya.ru.myproductsapi.api.request.UpdateProductRequest;
import nastya.ru.myproductsapi.api.response.GetAllProductResponse;
import nastya.ru.myproductsapi.api.response.GetProductResponse;
import nastya.ru.myproductsapi.entity.Product;
import nastya.ru.myproductsapi.exception.IdNotFoundException;
import nastya.ru.myproductsapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper modelMapper;
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
        when(modelMapper.map(product, GetProductResponse.class)).thenReturn(expectedResponse);

        GetProductResponse response = productService.findById(product.getId());

        verify(productRepository, times(1)).findById(product.getId());

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    void testFindProductByIdNotFoundShouldThrow() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setTitle("Test Title");
        product.setDescription("Test Description");
        product.setPrice(9.53);
        product.setStock(true);

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

        Product product2 = new Product();
        product2.setId(UUID.randomUUID());
        product2.setTitle("Product 2");

        List<Product> productList = Arrays.asList(product1, product2);

        GetProductResponse productResponse1 = new GetProductResponse();
        productResponse1.setId(product1.getId());
        productResponse1.setTitle(product1.getTitle());

        GetProductResponse productResponse2 = new GetProductResponse();
        productResponse2.setId(product2.getId());
        productResponse2.setTitle(product2.getTitle());

        List<GetProductResponse> expectedProductsResponse = Arrays.asList(productResponse1, productResponse2);

        when(productRepository.findAll()).thenReturn(productList);
        when(modelMapper.map(product1, GetProductResponse.class)).thenReturn(productResponse1);
        when(modelMapper.map(product2, GetProductResponse.class)).thenReturn(productResponse2);

        GetAllProductResponse response = productService.findAll();

        assertEquals(expectedProductsResponse, response.getProductResponses());

    }


    @Test
    void testSaveProductSuccess() {
        CreateProductRequest request = new CreateProductRequest(
                "Test Title",
                "Test Description",
                9.53,
                true);

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setTitle("Test Title");
        product.setDescription("Test Description");
        product.setPrice(9.53);
        product.setStock(true);

        when(modelMapper.map(request, Product.class)).thenReturn(product);

        productService.save(request);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product productToBeSaved = captor.getValue();

        assertThat(productToBeSaved.getTitle()).isEqualTo(request.getTitle());
        assertThat(productToBeSaved.getDescription()).isEqualTo(request.getDescription());
        assertThat(productToBeSaved.getPrice()).isEqualTo(request.getPrice());
        assertThat(productToBeSaved.isStock()).isEqualTo(request.isStock());
    }

    @Test
    void testUpdateProductSuccess() {
        UUID id = UUID.randomUUID();
        UpdateProductRequest request = new UpdateProductRequest(
                id,
                "Test Title2",
                "Test Description2",
                10.53,
                false);

        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setTitle("Old Title");
        existingProduct.setDescription("Old Description");
        existingProduct.setPrice(10.0);
        existingProduct.setStock(true);

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
