package nastya.ru.myproductsapi.repository;

import nastya.ru.myproductsapi.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID> {
}
