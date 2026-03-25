package perondi.orderms.listener.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.math.BigDecimal;

public record ItemPedidoEvent(String produto,
                              Integer quantidade,
                              @JsonAlias("preco")
                              BigDecimal precoProduto) {
}
