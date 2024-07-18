package com.desafio3.desafio3.Rest.Controller;

import com.desafio3.desafio3.Domain.Entity.Produto;
import com.desafio3.desafio3.Domain.Repository.ProdutoRepository;
import com.desafio3.desafio3.Exception.RegraDeNegocioException;
import com.desafio3.desafio3.Service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public Produto getProdutoById(@PathVariable Integer id) {
        return service.getProdutoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto createProduto(@RequestBody @Valid Produto produto) {
        return service.createProduto(produto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduto(@PathVariable Integer id) {
        service.deleteProduto(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduto(@PathVariable Integer id, @RequestBody @Valid Produto produto) {
        service.updateProduto(id, produto);
    }

    @Cacheable("cache-busca-produto")
    @GetMapping
    public List<Produto> findProdutos(Produto filtro) {
        return service.findProdutos(filtro);
    }

    @PutMapping("/desativar/{id}")
    public ResponseEntity<Produto> desativarProduto(@PathVariable Integer id) {
        Produto produto = service.desativarProduto(id);
        return ResponseEntity.ok().body(produto);
    }

    @PutMapping("/ativar/{id}")
    public ResponseEntity<Produto> ativarProduto(@PathVariable Integer id) {
        Produto produto = service.ativarProduto(id);
        return ResponseEntity.ok().body(produto);
    }
}