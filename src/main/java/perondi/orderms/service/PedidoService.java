package perondi.orderms.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import perondi.orderms.controller.dto.Pedidosresponse;
import perondi.orderms.listener.dto.PedidoEvent;
import perondi.orderms.models.ItemPedidoModel;
import perondi.orderms.models.PedidosModel;
import perondi.orderms.repository.PedidoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class PedidoService {

    private PedidoRepository pedidoRepository;
    private MongoTemplate mongoTemplate;

    public PedidoService(PedidoRepository pedidoRepository, MongoTemplate mongoTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void salvar(PedidoEvent pedido ){

        var pedidos = new PedidosModel();
        pedidos.setCodigoPedido(pedido.codigoPedido());
        pedidos.setCodigoCliente(pedido.codigoCliente());
        pedidos.setItens(getPedidosItens(pedido));
        pedidos.setValorPedido(getValorPedido(pedido));

        pedidoRepository.save(pedidos);
    }

    public List<Pedidosresponse> getPedidosByCodigoCliente(Long codigoCliente){
        var pedidosPorCliente = pedidoRepository.findByCodigoCliente(codigoCliente, PageRequest.of(0, 10));

        return pedidosPorCliente.map(Pedidosresponse::fromEntity).toList();
    }

    public BigDecimal findTotalGastoPorCodigoCliente (Long codigoCliente){

        var aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("codigoCliente").is(codigoCliente)),
                Aggregation.group("codigoCliente").sum("valorPedido").as("totalGasto")
        );

        var resultado = mongoTemplate.aggregate(aggregation, "pedidos", Map.class);
        var mapeado = resultado.getUniqueMappedResult();

        if (mapeado == null || mapeado.get("totalGasto") == null) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(mapeado.get("totalGasto").toString());
    }

    private BigDecimal getValorPedido(PedidoEvent pedido) {
        return pedido.itens()
                .stream()
                .map(i -> i.precoProduto().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

    }

    private static List<ItemPedidoModel> getPedidosItens(PedidoEvent pedidos){
        return pedidos.itens().stream()
                .map(item -> new ItemPedidoModel(item.produto(), item.quantidade(), item.precoProduto()))
                .toList();
    }
}
