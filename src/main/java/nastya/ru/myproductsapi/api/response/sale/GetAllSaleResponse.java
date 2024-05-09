package nastya.ru.myproductsapi.api.response.sale;

import java.util.List;

public class GetAllSaleResponse {
    private List<GetSaleResponse> saleResponses;

    public GetAllSaleResponse(List<GetSaleResponse> saleResponses) {
        this.saleResponses = saleResponses;
    }

    public List<GetSaleResponse> getSaleResponses() {
        return saleResponses;
    }
}