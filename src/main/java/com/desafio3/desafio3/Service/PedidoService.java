package com.desafio3.desafio3.Service;

import com.desafio3.desafio3.Domain.Entity.ItemPedido;
import com.desafio3.desafio3.Domain.Entity.Pedido;
import com.desafio3.desafio3.Domain.Entity.Produto;
import com.desafio3.desafio3.Domain.Entity.Usuario;
import com.desafio3.desafio3.Domain.Enums.PedidoStatus;
import com.desafio3.desafio3.Domain.Repository.ItemPedidoRepository;
import com.desafio3.desafio3.Domain.Repository.PedidoRepository;
import com.desafio3.desafio3.Domain.Repository.ProdutoRepository;
import com.desafio3.desafio3.Domain.Repository.UsuarioRepository;
import com.desafio3.desafio3.Exception.NaoEncontradoException;
import com.desafio3.desafio3.Exception.RegraDeNegocioException;
import com.desafio3.desafio3.Rest.Dto.ItemPedidoDto;
import com.desafio3.desafio3.Rest.Dto.PedidoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService{

    private final PedidoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    @Transactional
    public Pedido salvar(PedidoDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Usuario usuario = (Usuario) usuarioRepository.findByLogin(email);

        Pedido pedido = new Pedido();
        pedido.setData(LocalDateTime.now());
        BigDecimal totalPedido = BigDecimal.ZERO;

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        //testar se produto algum produto esta desabilitado
        for (ItemPedido item : itemsPedido) {
            boolean ativo = item.getProduto().getAtivo();
            if(ativo == false){
                String mensagem = String.format("O produto número '%d' está desabilitado, portanto não é possivel efeturar o pedido com o mesmo.", item.getProduto().getId());
                throw new RegraDeNegocioException(mensagem);
            }
        }

        //calcular o total baseado nos itens do pedido
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

    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItems(id);
    }

    public List<Pedido> obterPedidoCompletoData(LocalDate data) {
        return repository.findByDataFetchItems(data);
    }

    public List<Pedido> findPedidosByMes(String mes) {
        YearMonth yearMonth = YearMonth.parse(mes);
        int year = yearMonth.getYear();
        int month = yearMonth.getMonthValue();
        return repository.findByMesFetchItems(month, year);
    }

    @Transactional
    public void atualizaStatus(Integer id, PedidoStatus status) {
        repository.findById(id).map(pedido -> {pedido.setStatus(status);
            return repository.save(pedido);
        }).orElseThrow(()->new NaoEncontradoException(String.format("Pedido número %d não encontrado!",id)));
    }


    public List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDto> items){
        if (items.isEmpty()){
            throw new RegraDeNegocioException("Não é possivel realizar um pedido sem items!");
        }

        return items.stream().map( dto -> {
            Integer idProduto = dto.getProduto();
            Produto produto = produtoRepository.findById(idProduto)
                    .orElseThrow(()-> new RegraDeNegocioException("Código de produto inválido: " + idProduto));

            //verificar se quantidade do produto em estoque é suficiente
            if (dto.getQuantidade() > produto.getEstoque()) {
                throw new RegraDeNegocioException("Quantidade solicitada para o produto " + idProduto + " é maior do que a quantidade disponível em estoque.");
            } produto.setEstoque(produto.getEstoque() - dto.getQuantidade());

            ItemPedido itempedido = new ItemPedido();
            itempedido.setQuantidade(dto.getQuantidade());
            itempedido.setPedido(pedido);
            itempedido.setProduto(produto);
            return itempedido;
        } ).collect(Collectors.toList());
    }

    public void deletePedido(Integer id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado!"));
        repository.delete(pedido);
    }

    public List<Pedido> findPedidosBySemana(LocalDate data) {
        LocalDate startOfWeek = data.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = data.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = endOfWeek.atTime(LocalTime.MAX);
        return repository.findByDateRange(startDateTime, endDateTime);
    }



}
