package com.desafio3.desafio3.Service;

import com.desafio3.desafio3.Domain.Entity.Produto;
import com.desafio3.desafio3.Domain.Repository.ProdutoRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto getProdutoById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado!"));
    }

    public Produto createProduto(Produto produto) {
        return repository.save(produto);
    }

    public void deleteProduto(Integer id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado!"));
        repository.delete(produto);
    }

    public void updateProduto(Integer id, Produto produto) {
        Produto produtoExistente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado!"));
        produto.setId(produtoExistente.getId());
        repository.save(produto);
    }

    public List<Produto> findProdutos(Produto filtro) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Produto> example = Example.of(filtro, matcher);
        return repository.findAll(example);
    }

    public Produto desativarProduto(Integer id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado!"));
        produto.setAtivo(false);
        return repository.save(produto);
    }

    public Produto ativarProduto(Integer id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado!"));
        produto.setAtivo(true);
        return repository.save(produto);
    }
}
