package perondi.orderms.config;

import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabitMqConfig {

    public static final String FILA_PEDIDOS = "btgpactual-pedidos-feitos";

    @Bean
    public Declarable criarFilaPedidos() {
        return new Queue(FILA_PEDIDOS);
    }

    @Bean
    public JacksonJsonMessageConverter jacksonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
