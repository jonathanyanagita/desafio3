package com.desafio3.desafio3.Rest.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfosItemPedidoDto {

    private String nomeProduto;
    private BigDecimal precoUnitario;
    private Integer quantidade;

}
