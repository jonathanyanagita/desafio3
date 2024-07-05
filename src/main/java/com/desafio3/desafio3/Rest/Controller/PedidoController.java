package com.desafio3.desafio3.Rest.Controller;

import com.desafio3.desafio3.Domain.Entity.Pedido;
import com.desafio3.desafio3.Rest.Dto.PedidoDto;
import com.desafio3.desafio3.Service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer createPedido(@RequestBody PedidoDto dto){
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

}
