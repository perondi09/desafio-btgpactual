package perondi.orderms.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ItemPedidoModel {

    private String produto;

    private Integer quantidade;

    private BigDecimal precoProduto;
}
