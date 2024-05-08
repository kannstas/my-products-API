package nastya.ru.myproductsapi.exception;

import java.util.UUID;

public class IdNotFoundException extends BusinessLogicException{
    public IdNotFoundException(String paramName, UUID id) {
        super("В таблице products нет %s с id=%s".formatted(paramName, id));
    }
}