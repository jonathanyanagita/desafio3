package com.desafio3.desafio3.Domain.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "É obrigatório inserir nome do produto.")
    private String nome;
    private String descricao;

    @NotNull(message = "É obrigatório inserir preço do produto.")
    private BigDecimal preco;

    private Integer estoque;

    public Produto(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
