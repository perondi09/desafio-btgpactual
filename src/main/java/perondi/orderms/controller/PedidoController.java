package perondi.orderms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import perondi.orderms.controller.dto.ApiResponse;
import perondi.orderms.controller.dto.Pedidosresponse;
import perondi.orderms.controller.dto.PaginacaoResponse;
import perondi.orderms.service.PedidoService;

import java.util.Map;

@RestController
public class PedidoController {

    PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/cliente/{codigoCliente}/pedidos")
    public ResponseEntity<ApiResponse<Pedidosresponse>> listarPedidosPorCliente(@PathVariable("codigoCliente") Long codigoCliente,
                                                                                @RequestParam(name = "pagina", defaultValue = "0") Integer pagina,
                                                                                @RequestParam(name = "tamanho", defaultValue = "10") Integer tamanho) {

        var pedidosPage = pedidoService.getPedidosByCodigoCliente(codigoCliente);
        var totalGasto = pedidoService.findTotalGastoPorCodigoCliente(codigoCliente);
        var totalPaginas = (int) Math.ceil((double) pedidosPage.size() / tamanho);
        var paginacao = new PaginacaoResponse(pagina, tamanho, pedidosPage.size(), totalPaginas);
        var soma = Map.of("totalGasto", (Object) totalGasto);

        return ResponseEntity.ok(new ApiResponse<>(soma, pedidosPage, paginacao));

    }
}


