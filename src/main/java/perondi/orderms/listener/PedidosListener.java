package perondi.orderms.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import perondi.orderms.listener.dto.PedidoEvent;
import perondi.orderms.service.PedidoService;

import static perondi.orderms.config.RabitMqConfig.FILA_PEDIDOS;

@Component
public class PedidosListener {

    private final Logger logger = LoggerFactory.getLogger(PedidosListener.class);

    private PedidoService pedidoService;

    public PedidosListener(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @RabbitListener(queues = FILA_PEDIDOS)
    public void listen(PedidoEvent pedidoEvent) {
        logger.info("Recebendo pedido: {}", pedidoEvent);

        pedidoService.salvar(pedidoEvent);
    }
}
