package perondi.orderms.controller.dto;

import org.springframework.data.domain.Page;

public record PaginacaoResponse(Integer pagina, Integer tamanho, Integer elementosTotais, Integer paginasTotais) {

        public static PaginacaoResponse fromPage(Page<?> page) {
                return new PaginacaoResponse(page.getNumber(), page.getSize(), (int) page.getTotalElements(), page.getTotalPages());
        }
}
