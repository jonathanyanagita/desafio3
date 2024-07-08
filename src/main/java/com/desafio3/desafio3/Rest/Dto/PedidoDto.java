package com.desafio3.desafio3.Rest.Dto;

import com.desafio3.desafio3.Validation.NotEmptyList;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {

    @NotNull(message = "É obrigatório informar id do usuario.")
    private Integer usuario;

    private BigDecimal total;

    @NotEmptyList(message = "O pedido deve conter pelo menos um item.")
    private List<ItemPedidoDto> items;

}
