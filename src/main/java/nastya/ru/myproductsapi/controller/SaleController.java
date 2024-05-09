package nastya.ru.myproductsapi.controller;

import jakarta.validation.Valid;
import nastya.ru.myproductsapi.api.request.sale.CreateSaleRequest;
import nastya.ru.myproductsapi.api.request.sale.UpdateSaleRequest;
import nastya.ru.myproductsapi.service.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/sales")
public class SaleController {
    private SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }
    @GetMapping
    public Object find(@RequestParam(required = false) UUID id) {
        if (id != null) {
            return saleService.findById(id);
        } else {
            return saleService.findAll();
        }
    }

    @PostMapping
    public ResponseEntity<HttpStatus> save(@RequestBody @Valid CreateSaleRequest request) {
        saleService.save(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid UpdateSaleRequest request) {
        saleService.update(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> delete(@RequestParam UUID id) {
        saleService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}