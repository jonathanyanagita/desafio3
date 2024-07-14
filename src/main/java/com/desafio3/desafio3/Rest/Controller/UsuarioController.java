package com.desafio3.desafio3.Rest.Controller;

import com.desafio3.desafio3.Domain.Entity.Usuario;
import com.desafio3.desafio3.Domain.Repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario createUsuario(@RequestBody @Valid Usuario usuario) {
        return repository.save(usuario);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUsuario(@PathVariable Integer id, @RequestBody @Valid Usuario usuario){
        repository.findById(id).map(usuarioExistente -> {usuario.setId(usuarioExistente.getId());
                    repository.save(usuario);
                    return usuarioExistente;
                }).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario não encontrado!"));
    }

    @GetMapping
    public Usuario getUsuarioById(@PathVariable Integer id){
        return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario não encontrado!"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete( @PathVariable Integer id ){
        repository.findById(id).map( usuario -> {repository.delete(usuario);
            return usuario;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario não encontrado") );

    }
}
