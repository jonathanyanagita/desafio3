package com.desafio3.desafio3.Domain.Repository;

import com.desafio3.desafio3.Domain.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
