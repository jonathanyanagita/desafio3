package com.desafio3.desafio3.Domain.Repository;

import com.desafio3.desafio3.Domain.Entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
}
