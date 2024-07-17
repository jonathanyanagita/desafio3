package com.desafio3.desafio3.Service;

import com.desafio3.desafio3.Domain.Entity.Usuario;
import com.desafio3.desafio3.Domain.Repository.UsuarioRepository;
import com.desafio3.desafio3.Rest.Dto.AuthenticationDto;
import com.desafio3.desafio3.Rest.Dto.CadastroDto;
import com.desafio3.desafio3.Rest.Dto.LoginResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return usuarioRepository.findByLogin(login);
    }

}