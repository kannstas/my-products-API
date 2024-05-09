package nastya.ru.myproductsapi.controller;

import jakarta.validation.Valid;
import nastya.ru.myproductsapi.api.request.product.CreateProductRequest;
import nastya.ru.myproductsapi.api.request.product.UpdateProductRequest;
import nastya.ru.myproductsapi.api.response.product.GetAllProductResponse;
import nastya.ru.myproductsapi.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static nastya.ru.myproductsapi.util.validator.ProductRequestValidator.validatePrice;
import static nastya.ru.myproductsapi.util.validator.ProductRequestValidator.validateSort;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Object find(@RequestParam(required = false) UUID id) {
        if (id != null) {
            return productService.findById(id);
        } else {
            return productService.findAll();
        }
    }

    @GetMapping("/filter")
    public GetAllProductResponse findAllProductsWithFilter(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer quantity) {

        validateSort(sort);
        validatePrice(minPrice, maxPrice);

        return productService.findByCriteria(sort, title, minPrice, maxPrice, quantity);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> save(@RequestBody @Valid CreateProductRequest request) {
        productService.save(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid UpdateProductRequest request) {
        productService.update(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> delete(@RequestParam UUID id) {
        productService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}