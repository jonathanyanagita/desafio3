package com.desafio3.desafio3.Service;

import com.desafio3.desafio3.Domain.Entity.Pedido;
import com.desafio3.desafio3.Rest.Dto.PedidoDto;

public interface PedidoService {

    Pedido salvar(PedidoDto dto);
}
