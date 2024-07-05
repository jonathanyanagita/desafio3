package com.desafio3.desafio3.Domain.Repository;

import com.desafio3.desafio3.Domain.Entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}
