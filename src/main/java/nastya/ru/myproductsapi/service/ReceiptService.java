package nastya.ru.myproductsapi.service;

import nastya.ru.myproductsapi.api.request.receipt.CreateReceiptRequest;
import nastya.ru.myproductsapi.api.request.receipt.UpdateReceiptRequest;
import nastya.ru.myproductsapi.api.response.product.GetProductResponse;
import nastya.ru.myproductsapi.api.response.receipt.GetAllReceiptResponse;
import nastya.ru.myproductsapi.api.response.receipt.GetReceiptResponse;
import nastya.ru.myproductsapi.entity.Product;
import nastya.ru.myproductsapi.entity.Receipt;
import nastya.ru.myproductsapi.exception.IdNotFoundException;
import nastya.ru.myproductsapi.repository.ProductRepository;
import nastya.ru.myproductsapi.repository.ReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static nastya.ru.myproductsapi.util.conversion.Convert.*;

@Service
public class ReceiptService {
    private ReceiptRepository receiptRepository;
    private ProductRepository productRepository;

    public ReceiptService(ReceiptRepository receiptRepository, ProductRepository productRepository) {
        this.receiptRepository = receiptRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public GetReceiptResponse findById(UUID id) {

        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("sales", "sale", id));

        GetProductResponse productResponse = toResponse
                (productRepository.findById(receipt.getReceiptedProduct().getId()).orElseThrow());

        return toReceiptResponse(receipt, productResponse);
    }

    @Transactional(readOnly = true)
    public GetAllReceiptResponse findAll() {
        List<GetReceiptResponse> sales = receiptRepository.findAll()
                .stream()
                .map(receipt -> {
                    GetProductResponse productResponse = toResponse
                            (productRepository.findById(receipt.getReceiptedProduct().getId()).orElseThrow());
                    return toReceiptResponse(receipt, productResponse);
                })
                .toList();

        return new GetAllReceiptResponse(sales);
    }

    @Transactional
    public void save(CreateReceiptRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IdNotFoundException("products", "product", request.getProductId()));

        Receipt receipt = toReceipt(request);
        receipt.setId(UUID.randomUUID());
        receipt.setReceiptedProduct(product);
        product.setQuantity(
                increaseNumber(product.getQuantity(), receipt.getQuantity())
        );

        receiptRepository.save(receipt);
    }

    @Transactional
    public void update(UpdateReceiptRequest request) {
        Receipt receipt = receiptRepository.findById(request.getId())
                .orElseThrow(() -> new IdNotFoundException("receipts", "receipt", request.getId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IdNotFoundException("products", "product", request.getProductId()));

        Optional.ofNullable(request.getDocumentTitle()).ifPresent(receipt::setDocumentTitle);
        receipt.setReceiptedProduct(product);


        Optional.ofNullable(request.getQuantity()).ifPresent(quantity -> product.setQuantity(
                changeNumber(receipt.getQuantity(), product.getQuantity(), request.getQuantity())
        ));

        Optional.ofNullable(request.getQuantity()).ifPresent(receipt::setQuantity);
    }

    @Transactional
    public void delete(UUID id) {
        receiptRepository.deleteById(id);
    }

    private int increaseNumber(int presentNumber, int newNumber) {
        return presentNumber + newNumber;
    }

    private int changeNumber(int receiptNumber, int presentQuantity, int requestQuantity) {
        int recoverQuantity = presentQuantity - receiptNumber;
        return increaseNumber(recoverQuantity, requestQuantity);
    }
}