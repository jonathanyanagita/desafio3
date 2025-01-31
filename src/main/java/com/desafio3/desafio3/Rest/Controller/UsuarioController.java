package com.desafio3.desafio3.Rest.Controller;

import com.desafio3.desafio3.Domain.Entity.Usuario;
import com.desafio3.desafio3.Domain.Repository.UsuarioRepository;
import com.desafio3.desafio3.Exception.RegraDeNegocioException;
import com.desafio3.desafio3.Rest.Dto.AuthenticationDto;
import com.desafio3.desafio3.Rest.Dto.CadastroDto;
import com.desafio3.desafio3.Rest.Dto.LoginResponseDto;
import com.desafio3.desafio3.Service.AuthorizationService;
import com.desafio3.desafio3.Service.TokenService;
import jakarta.validation.Constraint;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class UsuarioController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDto data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadatro(@RequestBody @Valid CadastroDto data, BindingResult result){
        if(result.hasErrors()){
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        if(this.repository.findByLogin(data.getLogin()) != null) {
            throw new RegraDeNegocioException("Usuário já cadastrado");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getSenha());
        Usuario newUser = new Usuario(data.getLogin(), encryptedPassword, data.getRole());
        this.repository.save(newUser);
        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }
}
