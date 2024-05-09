package nastya.ru.myproductsapi.exception;

import java.util.UUID;

public class IdNotFoundException extends BusinessLogicException{
    public IdNotFoundException(String tableName, String paramName, UUID id) {
        super("В таблице %s нет %s с id=%s".formatted(tableName, paramName, id));
    }
}