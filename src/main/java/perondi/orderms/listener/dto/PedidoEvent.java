package perondi.orderms.listener.dto;

import java.util.List;

public record PedidoEvent(long codigoPedido,
                          long codigoCliente,
                          List<ItemPedidoEvent> itens) {
}
