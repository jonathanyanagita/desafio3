package com.desafio3.desafio3.Service;

import com.desafio3.desafio3.Domain.Entity.Pedido;
import com.desafio3.desafio3.Domain.Enums.PedidoStatus;
import com.desafio3.desafio3.Rest.Dto.PedidoDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PedidoService {

    Pedido salvar(PedidoDto dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, PedidoStatus status);

}
