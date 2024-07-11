package com.desafio3.desafio3.Domain.Repository;

import com.desafio3.desafio3.Domain.Entity.Pedido;
import com.desafio3.desafio3.Domain.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByUsuario(Usuario usuario);

    @Query("select p from Pedido p left join fetch p.itens where p.id = :id")
    Optional<Pedido> findByIdFetchItems(@Param("id") Integer id);

    @Query("select p from Pedido p left join fetch p.itens where FUNCTION('DATE_FORMAT', p.data, '%Y-%m-%d') IN :datas")
    List<Pedido> findByDataFetchItems(@Param("datas") LocalDate datas);

    @Query("SELECT p FROM Pedido p WHERE MONTH(p.data) = :month AND YEAR(p.data) = :year")
    List<Pedido> findByMesFetchItems(@Param("month") int month, @Param("year") int year);

    @Query("SELECT p FROM Pedido p WHERE p.data BETWEEN :startDate AND :endDate")
    List<Pedido> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
