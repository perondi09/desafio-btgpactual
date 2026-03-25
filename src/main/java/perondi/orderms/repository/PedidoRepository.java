package perondi.orderms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import perondi.orderms.models.PedidosModel;

public interface PedidoRepository extends MongoRepository<PedidosModel, Long> {
    Page<PedidosModel> findByCodigoCliente(Long codigoCliente, Pageable pageable);
}
