package nastya.ru.myproductsapi.util.conversion;

import nastya.ru.myproductsapi.api.request.product.CreateProductRequest;
import nastya.ru.myproductsapi.api.request.receipt.CreateReceiptRequest;
import nastya.ru.myproductsapi.api.request.sale.CreateSaleRequest;
import nastya.ru.myproductsapi.api.response.product.GetProductResponse;
import nastya.ru.myproductsapi.api.response.receipt.GetReceiptResponse;
import nastya.ru.myproductsapi.api.response.sale.GetSaleResponse;
import nastya.ru.myproductsapi.entity.Product;
import nastya.ru.myproductsapi.entity.Receipt;
import nastya.ru.myproductsapi.entity.Sale;
import org.modelmapper.ModelMapper;

public class Convert {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static GetProductResponse toResponse(Product product) {
        return modelMapper.map(product, GetProductResponse.class);
    }

    public static GetSaleResponse toSaleResponse(Sale sale, GetProductResponse productResponse) {
        GetSaleResponse saleResponse = modelMapper.map(sale, GetSaleResponse.class);
        saleResponse.setProductResponse(productResponse);
        return saleResponse;
    }
    public static GetReceiptResponse toReceiptResponse(Receipt receipt, GetProductResponse productResponse) {
        GetReceiptResponse receiptResponse =  modelMapper.map(receipt, GetReceiptResponse.class);
        receiptResponse.setProductResponse(productResponse);
        return receiptResponse;
    }

    public static Product toProduct(CreateProductRequest productRequest) {
        return modelMapper.map(productRequest, Product.class);
    }

    public static Sale toSale(CreateSaleRequest request) {
        return modelMapper.map(request, Sale.class);
    }
    public static Receipt toReceipt(CreateReceiptRequest request) {
        return modelMapper.map(request, Receipt.class);
    }

}