package com.desafio3.desafio3.Rest.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {

    private Integer usuario;
    private BigDecimal total;
    private List<ItemPedidoDto> items;

}
