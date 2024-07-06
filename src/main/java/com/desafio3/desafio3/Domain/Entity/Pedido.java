package com.desafio3.desafio3.Domain.Entity;

import com.desafio3.desafio3.Domain.Enums.PedidoStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime data;
    private BigDecimal total;

    @ManyToOne
    private Usuario usuario;

    @OneToMany
    private List<ItemPedido> itens;

    @Enumerated(EnumType.STRING)
    private PedidoStatus status;
}
