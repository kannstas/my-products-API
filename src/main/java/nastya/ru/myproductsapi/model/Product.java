package nastya.ru.myproductsapi.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

public class Product {
    @NotNull
    private UUID id;
    @NotEmpty
    @Size(max = 255)
    private String title;
    @Size(max = 4096)
    private String description;
    @Min(0)
    private double price;
    private boolean isStock;

    public Product(UUID id, String title, String description, double price, boolean isStock) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.isStock = isStock;
    }

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

    public boolean isStock() {
        return isStock;
    }

    public void setStock(boolean stock) {
        isStock = stock;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(price, product.price) == 0 && isStock == product.isStock && Objects.equals(id, product.id) && Objects.equals(title, product.title) && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, price, isStock);
    }
}