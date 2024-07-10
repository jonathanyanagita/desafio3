package com.desafio3.desafio3.Rest.Controller;

import com.desafio3.desafio3.Domain.Entity.ItemPedido;
import com.desafio3.desafio3.Domain.Entity.Pedido;
import com.desafio3.desafio3.Domain.Entity.Produto;
import com.desafio3.desafio3.Domain.Enums.PedidoStatus;
import com.desafio3.desafio3.Rest.Dto.AtualizaçãoStatusPedidoDto;
import com.desafio3.desafio3.Rest.Dto.InfosItemPedidoDto;
import com.desafio3.desafio3.Rest.Dto.InfosPedidoDto;
import com.desafio3.desafio3.Rest.Dto.PedidoDto;
import com.desafio3.desafio3.Service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer createPedido(@RequestBody @Valid PedidoDto dto){
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("{id}")
    public InfosPedidoDto getById(@PathVariable Integer id){
        return service
                .obterPedidoCompleto(id)
                .map(pedido -> converter(pedido))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido não encontrado!"));
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Integer id, @RequestBody AtualizaçãoStatusPedidoDto dto){
        String novoStatus = dto.getNovoStatus();
        service.atualizaStatus(id, PedidoStatus.valueOf(novoStatus));
    }

    private InfosPedidoDto converter(Pedido pedido){
        return InfosPedidoDto
                .builder().id(pedido.getId())
                .nomeUsuario(pedido.getUsuario().getNome())
                .cpf(pedido.getUsuario().getCpf())
                .dataPedido(pedido.getData())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converter(pedido.getItens())).build();
    }

    private List<InfosItemPedidoDto> converter(List<ItemPedido> items){

        if (CollectionUtils.isEmpty(items)){
            return Collections.emptyList();
        }

        return items.stream().map(
                item -> InfosItemPedidoDto.builder()
                        .nomeProduto(item.getProduto().getNome())
                        .precoUnitario(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade()).build()
        ).collect(Collectors.toList());
    }

    @Cacheable("cache-filtro-data")
    @GetMapping("/data/{data}")
    public List<InfosPedidoDto> getById(@PathVariable("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime data){
        return service.obterPedidoCompletoData(data)
                .stream().map(pedido -> converter(pedido)).toList();
    }
}