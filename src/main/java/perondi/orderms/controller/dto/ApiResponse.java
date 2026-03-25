package perondi.orderms.controller.dto;

import java.util.List;
import java.util.Map;

public record ApiResponse<T>(Map<String, Object> soma,
                             List<T> data,
                             PaginacaoResponse paginacaoResponse) {
}
