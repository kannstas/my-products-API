package nastya.ru.myproductsapi.api.request.sale;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class UpdateSaleRequest {
    @NotNull
    private UUID id;
    @Size(max = 255)
    private String documentTitle;

    @NotNull
    private UUID productId;

    @Min(1)
    private Integer quantity;

    public String getDocumentTitle() {
        return documentTitle;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}