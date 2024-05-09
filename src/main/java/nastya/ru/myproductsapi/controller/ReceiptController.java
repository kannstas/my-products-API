package nastya.ru.myproductsapi.controller;

import jakarta.validation.Valid;
import nastya.ru.myproductsapi.api.request.receipt.CreateReceiptRequest;
import nastya.ru.myproductsapi.api.request.receipt.UpdateReceiptRequest;
import nastya.ru.myproductsapi.service.ReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    private ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }
    @GetMapping
    public Object find(@RequestParam(required = false) UUID id) {
        if (id != null) {
            return receiptService.findById(id);
        } else {
            return receiptService.findAll();
        }
    }

    @PostMapping
    public ResponseEntity<HttpStatus> save(@RequestBody @Valid CreateReceiptRequest request) {
        receiptService.save(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid UpdateReceiptRequest request) {
        receiptService.update(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> delete(@RequestParam UUID id) {
        receiptService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}