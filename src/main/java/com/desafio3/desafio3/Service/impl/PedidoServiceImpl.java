package com.desafio3.desafio3.Service.impl;

import com.desafio3.desafio3.Domain.Entity.ItemPedido;
import com.desafio3.desafio3.Domain.Entity.Pedido;
import com.desafio3.desafio3.Domain.Entity.Produto;
import com.desafio3.desafio3.Domain.Entity.Usuario;
import com.desafio3.desafio3.Domain.Repository.ItemPedidoRepository;
import com.desafio3.desafio3.Domain.Repository.PedidoRepository;
import com.desafio3.desafio3.Domain.Repository.ProdutoRepository;
import com.desafio3.desafio3.Domain.Repository.UsuarioRepository;
import com.desafio3.desafio3.Exception.RegraDeNegocioException;
import com.desafio3.desafio3.Rest.Dto.ItemPedidoDto;
import com.desafio3.desafio3.Rest.Dto.PedidoDto;
import com.desafio3.desafio3.Service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDto dto) {
        Integer idUsuario = dto.getUsuario();
        Usuario usuario = usuarioRepository
                .findById(idUsuario)
                .orElseThrow(()-> new RegraDeNegocioException("Código do usário inválido!"));

        Pedido pedido = new Pedido();
        pedido.setData(LocalDate.now());
        pedido.setTotal(dto.getTotal());
        pedido.setUsuario(usuario);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        repository.save(pedido);
        itemPedidoRepository.saveAll(itemsPedido);
        pedido.setItemPedido(itemsPedido);
        return pedido;
    }

    public List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDto> items){
        if (items.isEmpty()){
            throw new RegraDeNegocioException("Não é possivel realizar um pedido sem items!");
        }

        return items.stream().map(dto->{
            Integer idProduto = dto.getProduto();
            Produto produto = produtoRepository
                    .findById(idProduto)
                    .orElseThrow(()-> new RegraDeNegocioException("Código de produto inválido: " + idProduto));

            ItemPedido itempedido = new ItemPedido();
            itempedido.setQuantidade(dto.getQuantidade());
            itempedido.setPedido(pedido);
            itempedido.setProduto(produto);
            return itempedido;
        } ).collect(Collectors.toList());
    }
}
