package com.desafio3.desafio3.Rest.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfosPedidoDto {

    private Integer id;
    private String nomeUsuario;
    private String cpf;
    private BigDecimal total;
    private LocalDateTime dataPedido;
    private String status;
    private List<InfosItemPedidoDto> items;
}
