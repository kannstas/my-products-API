package nastya.ru.myproductsapi.repository;

import nastya.ru.myproductsapi.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {
}
