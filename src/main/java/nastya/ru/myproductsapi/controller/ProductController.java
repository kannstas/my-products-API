package nastya.ru.myproductsapi.controller;

import jakarta.validation.Valid;
import nastya.ru.myproductsapi.api.request.CreateProductRequest;
import nastya.ru.myproductsapi.api.request.UpdateProductRequest;
import nastya.ru.myproductsapi.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Object find (@RequestParam (required = false) UUID id) {
        if(id!= null) {
            return productService.findById(id);
        } else {
            return productService.findAll();
        }
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