package nastya.ru.myproductsapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name="receipts")
public class Receipt {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "document_title")
    @NotNull
    @Size(max = 255)
    private String documentTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private Product receiptedProduct;

    @Column(name = "quantity")
    @NotNull
    private Integer quantity;

    public Receipt() {
    }

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

    public Product getReceiptedProduct() {
        return receiptedProduct;
    }

    public void setReceiptedProduct(Product deliveredProduct) {
        this.receiptedProduct = deliveredProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int numberOfDeliveredProducts) {
        this.quantity = numberOfDeliveredProducts;
    }
}