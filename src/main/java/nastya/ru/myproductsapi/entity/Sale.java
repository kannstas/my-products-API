package nastya.ru.myproductsapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "document_title")
    @NotNull
    @Size(max = 255)
    private String documentTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private Product soldProduct;

    @Column(name="quantity")
    @NotNull
    private Integer quantity;

    @Column(name = "cost")
    @NotNull
    private Double cost;

    public Sale() {
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

    public Product getSoldProduct() {
        return soldProduct;
    }

    public void setSoldProduct(Product soldProduct) {
        this.soldProduct = soldProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int numberOfSoldProducts) {
        this.quantity = numberOfSoldProducts;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}