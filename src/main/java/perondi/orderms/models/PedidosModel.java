package perondi.orderms.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "pedidos")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PedidosModel {

    @MongoId
    private Long codigoPedido;

    @Indexed(name="codigo_cliente_index")
    private Long codigoCliente;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal valorPedido;

    private List<ItemPedidoModel> itens;
}
