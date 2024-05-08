package nastya.ru.myproductsapi.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class UpdateProductRequest {
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

    public UpdateProductRequest(UUID id, String title, String description, double price, boolean isStock) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.isStock = isStock;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}