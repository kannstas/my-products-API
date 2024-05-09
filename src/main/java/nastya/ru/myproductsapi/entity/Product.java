package nastya.ru.myproductsapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "id")
    @NotNull
    private UUID id;

    @Column(name = "title")
    @NotEmpty
    @Size(max = 255)
    private String title;

    @Column(name = "description")
    @Size(max = 4096)
    private String description;

    @Column(name = "price")
    @Min(0)
    private double price;

    @Column(name = "quantity")
    private Integer quantity;

    @OneToMany(mappedBy = "receiptedProduct")
    private List<Receipt> receiptList;

    @OneToMany(mappedBy = "soldProduct")
    private List<Sale> saleList;

    public Product() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Receipt> getReceiptList() {
        return receiptList;
    }

    public void setReceiptList(List<Receipt> receiptList) {
        this.receiptList = receiptList;
    }

    public List<Sale> getSaleList() {
        return saleList;
    }

    public void setSaleList(List<Sale> saleList) {
        this.saleList = saleList;
    }
}
