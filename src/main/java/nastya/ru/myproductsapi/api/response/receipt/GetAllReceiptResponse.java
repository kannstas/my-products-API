package nastya.ru.myproductsapi.api.response.receipt;

import java.util.List;

public class GetAllReceiptResponse {
    private List<GetReceiptResponse> receiptResponses;

    public GetAllReceiptResponse(List<GetReceiptResponse> receiptResponses) {
        this.receiptResponses = receiptResponses;
    }

    public List<GetReceiptResponse> getReceiptResponses() {
        return receiptResponses;
    }
}