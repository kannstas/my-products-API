package nastya.ru.myproductsapi.util.validator;

import nastya.ru.myproductsapi.exception.BusinessLogicException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
@Component
@Validated
public class ProductRequestValidator{

    public static void validateSort(String sort) {
        if(sort!=null) {
            if(!sort.equals("title") && !sort.equals("price")) {
                throw new BusinessLogicException("Невозможно отcортировать по заданному критерию");
            }
        }
    }
    public static void validatePrice(Double minPrice, Double maxPrice) {
        if(minPrice!=null && maxPrice!=null) {
            if (minPrice < 0) {
                throw new BusinessLogicException("Минимальная цена не может быть меньше 0");
            }
            if (maxPrice < minPrice) {
                throw new BusinessLogicException("Максимальная цена не может быть ниже минимальной");
            }
        }
    }
}