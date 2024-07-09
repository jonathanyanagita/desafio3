package com.desafio3.desafio3.Service.impl;

import com.desafio3.desafio3.Domain.Entity.ItemPedido;
import com.desafio3.desafio3.Domain.Entity.Pedido;
import com.desafio3.desafio3.Domain.Entity.Produto;
import com.desafio3.desafio3.Domain.Entity.Usuario;
import com.desafio3.desafio3.Domain.Enums.PedidoStatus;
import com.desafio3.desafio3.Domain.Repository.ItemPedidoRepository;
import com.desafio3.desafio3.Domain.Repository.PedidoRepository;
import com.desafio3.desafio3.Domain.Repository.ProdutoRepository;
import com.desafio3.desafio3.Domain.Repository.UsuarioRepository;
import com.desafio3.desafio3.Exception.PedidoNaoEncontradoException;
import com.desafio3.desafio3.Exception.RegraDeNegocioException;
import com.desafio3.desafio3.Rest.Dto.ItemPedidoDto;
import com.desafio3.desafio3.Rest.Dto.PedidoDto;
import com.desafio3.desafio3.Service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
        pedido.setData(LocalDateTime.now());

        BigDecimal totalPedido = BigDecimal.ZERO;

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());

        for (ItemPedido item : itemsPedido) {
            BigDecimal subtotalItem = item.getProduto().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
            totalPedido = totalPedido.add(subtotalItem);
        }

        pedido.setTotal(totalPedido);
        pedido.setUsuario(usuario);
        pedido.setStatus(PedidoStatus.REALIZADO);

        repository.save(pedido);
        itemPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItems(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, PedidoStatus status) {
        repository.findById(id).map( pedido -> {
            pedido.setStatus(status);
            return repository.save(pedido);
        }).orElseThrow(()->new PedidoNaoEncontradoException());
    }

    public List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDto> items){
        if (items.isEmpty()){
            throw new RegraDeNegocioException("Não é possivel realizar um pedido sem items!");
        }

        return items
                .stream()
                .map( dto -> {
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

    @PatchMapping
    public void updateStatus(){

    }



}
