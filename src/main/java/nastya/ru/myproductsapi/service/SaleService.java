package nastya.ru.myproductsapi.service;

import nastya.ru.myproductsapi.api.request.sale.CreateSaleRequest;
import nastya.ru.myproductsapi.api.request.sale.UpdateSaleRequest;
import nastya.ru.myproductsapi.api.response.product.GetProductResponse;
import nastya.ru.myproductsapi.api.response.sale.GetAllSaleResponse;
import nastya.ru.myproductsapi.api.response.sale.GetSaleResponse;
import nastya.ru.myproductsapi.entity.Product;
import nastya.ru.myproductsapi.entity.Sale;
import nastya.ru.myproductsapi.exception.BusinessLogicException;
import nastya.ru.myproductsapi.exception.IdNotFoundException;
import nastya.ru.myproductsapi.repository.ProductRepository;
import nastya.ru.myproductsapi.repository.SaleRepository;
import nastya.ru.myproductsapi.util.conversion.Convert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static nastya.ru.myproductsapi.util.conversion.Convert.*;

@Service
public class SaleService {
    private SaleRepository saleRepository;
    private ProductRepository productRepository;

    public SaleService(SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public GetSaleResponse findById(UUID id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("sales", "sale", id));

        GetProductResponse productResponse = toResponse
                (productRepository.findById(sale.getSoldProduct().getId()).orElseThrow());

        return toSaleResponse(sale, productResponse);

    }

    @Transactional(readOnly = true)
    public GetAllSaleResponse findAll() {
        List<GetSaleResponse> sales = saleRepository.findAll()
                .stream()
                .map(sale -> {
                    GetProductResponse productResponse = toResponse
                            (productRepository.findById(sale.getSoldProduct().getId()).orElseThrow());
                    return Convert.toSaleResponse(sale, productResponse);
                })
                .toList();

        return new GetAllSaleResponse(sales);
    }

    @Transactional
    public void save(CreateSaleRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IdNotFoundException("products", "product", request.getProductId()));

        if (request.getQuantity() > product.getQuantity()) {
            throw new BusinessLogicException("Невозможно продать %s товаров, их всего %s".formatted(request.getQuantity(), product.getQuantity()));
        }

        Sale sale = toSale(request);

        sale.setId(UUID.randomUUID());
        sale.setCost(
                priceCalculate(sale.getQuantity(), product.getPrice())
        );
        sale.setSoldProduct(product);

        product.setQuantity(
                reduceNumber(product.getQuantity(), sale.getQuantity())
        );

        saleRepository.save(sale);
    }

    @Transactional
    public void update(UpdateSaleRequest request) {
        Sale sale = saleRepository.findById(request.getId())
                .orElseThrow(() -> new IdNotFoundException("sales", "sale", request.getId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IdNotFoundException("products", "product", request.getProductId()));

        Optional.ofNullable(request.getDocumentTitle()).ifPresent(sale::setDocumentTitle);
        sale.setSoldProduct(product);

        Optional.ofNullable(request.getQuantity()).ifPresent(quantity -> product.setQuantity(
                changeNumber(sale.getQuantity(), product.getQuantity(), request.getQuantity())
        ));

        if (request.getQuantity()!= null) {
            if (request.getQuantity() > product.getQuantity()) {
                throw new BusinessLogicException("Невозможно продать %s товаров, их всего %s".formatted(request.getQuantity(), product.getQuantity()));
            }
        }

        Optional.ofNullable(request.getQuantity()).ifPresent(sale::setQuantity);

        sale.setCost(priceCalculate(sale.getQuantity(), product.getPrice()));
    }

    @Transactional
    public void delete(UUID id) {
        saleRepository.deleteById(id);
    }

    private double priceCalculate(int quantity, double price) {
        return quantity * price;
    }

    private int reduceNumber(int originalNumber, int reduceNumber) {
        return originalNumber - reduceNumber;
    }

    private int changeNumber(int soldQuantity, int presentQuantity, int requestQuantity) {
        int recoverQuantity = soldQuantity + presentQuantity;
        return reduceNumber(recoverQuantity, requestQuantity);
    }
}