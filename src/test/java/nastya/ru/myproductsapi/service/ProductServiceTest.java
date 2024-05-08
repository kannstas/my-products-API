package nastya.ru.myproductsapi.service;

import nastya.ru.myproductsapi.api.request.CreateProductRequest;
import nastya.ru.myproductsapi.api.request.UpdateProductRequest;
import nastya.ru.myproductsapi.api.response.GetAllProductResponse;
import nastya.ru.myproductsapi.api.response.GetProductResponse;
import nastya.ru.myproductsapi.dao.ProductDAO;
import nastya.ru.myproductsapi.exception.IdNotFoundException;
import nastya.ru.myproductsapi.model.Product;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductDAO productDAO;

    @Test
    void testFindProductByIdSuccess() {
        Product product = new Product(
                UUID.randomUUID(),
                "Test Title",
                "Test Description",
                9.53,
                true);

        when(productDAO.findById(product.getId())).thenReturn(product);

        GetProductResponse response = productService.findById(product.getId());

        verify(productDAO, times(1)).findById(product.getId());

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    void testFindProductByIdNotFoundShouldThrow() {
        Product product = new Product(
                UUID.randomUUID(),
                "Test Title",
                "Test Description",
                9.53,
                true);
        when(productDAO.findById(product.getId())).thenReturn(null);

        assertThatThrownBy(() -> productService.findById(product.getId()))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessageContaining("В листе products нет %s с id=%s".formatted("product", product.getId()));
    }

    @Test
    void testFindAllProductsSuccess() {
        Product product = new Product(
                UUID.randomUUID(),
                "Test Title",
                "Test Description",
                9.53,
                true);
        when(productDAO.findAll())
                .thenReturn(List.of(product));

        GetAllProductResponse response = productService.findAll();
        GetProductResponse result = response.getProductResponses().stream()
                .findFirst()
                .orElseThrow();

        assertThat(response.getProductResponses().size()).isEqualTo(1);
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    void testSaveProductSuccess() {
        CreateProductRequest request = new CreateProductRequest(
                "Test Title",
                "Test Description",
                9.53,
                true);

        productService.save(request);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productDAO).save(captor.capture());

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

        productService.update(request);

        ArgumentCaptor <Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productDAO).update(captor.capture());
        Product productToUpdate = captor.getValue();

        assertThat(productToUpdate)
                .usingRecursiveComparison()
                .isEqualTo(request);
    }

    @Test
    void deleteSuccess() {
        Product product = new Product(
                UUID.randomUUID(),
                "Test Title",
                "Test Description",
                9.53,
                true);

        productService.delete(product.getId());

        verify(productDAO).delete(product.getId());
    }
}
