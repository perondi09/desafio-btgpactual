package perondi.orderms.controller.dto;

import perondi.orderms.models.PedidosModel;

import java.math.BigDecimal;

public record Pedidosresponse(Long codigoPedido, Long codigoCliente, BigDecimal valorTotal) {

    public static Pedidosresponse fromEntity(PedidosModel pedidosModel) {
        return new Pedidosresponse(pedidosModel.getCodigoPedido(), pedidosModel.getCodigoCliente(), pedidosModel.getValorPedido());
    }
}
