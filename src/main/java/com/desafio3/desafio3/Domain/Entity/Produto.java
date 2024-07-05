package com.desafio3.desafio3.Domain.Entity;

import jakarta.persistence.*;
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
    private String nome;
    private String descricao;
    private BigDecimal preco;

    @OneToOne
    private ItemPedido itemPedido;

    @ManyToOne
    private Estoque estoque;

    public Produto(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
