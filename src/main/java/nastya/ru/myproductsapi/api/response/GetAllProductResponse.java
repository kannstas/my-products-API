package nastya.ru.myproductsapi.api.response;

import java.util.List;

public class GetAllProductResponse {
    private List<GetProductResponse> productResponses;

    public GetAllProductResponse(List<GetProductResponse> productResponses) {
        this.productResponses = productResponses;
    }

    public List<GetProductResponse> getProductResponses() {
        return productResponses;
    }
}