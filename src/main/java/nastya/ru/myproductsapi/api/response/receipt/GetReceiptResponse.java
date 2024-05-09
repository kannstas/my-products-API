package nastya.ru.myproductsapi.api.response.receipt;

import nastya.ru.myproductsapi.api.response.product.GetProductResponse;

import java.util.UUID;

public class GetReceiptResponse {
    private UUID id;
    private String documentTitle;
    private GetProductResponse productResponse;

    private Integer quantity;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public GetProductResponse getProductResponse() {
        return productResponse;
    }

    public void setProductResponse(GetProductResponse productResponse) {
        this.productResponse = productResponse;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}