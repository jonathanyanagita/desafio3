package com.desafio3.desafio3.Service;

import com.desafio3.desafio3.Domain.Entity.Pedido;
import com.desafio3.desafio3.Domain.Enums.PedidoStatus;
import com.desafio3.desafio3.Domain.Repository.PedidoRepository;
import com.desafio3.desafio3.Rest.Dto.PedidoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface PedidoService {

    Pedido salvar(PedidoDto dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    List<Pedido> obterPedidoCompletoData(LocalDate data);

    List<Pedido> findPedidosByMes(String mes);

    void atualizaStatus(Integer id, PedidoStatus status);

    List<Pedido> findPedidosBySemana(LocalDate data);
}
