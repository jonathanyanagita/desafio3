package com.desafio3.desafio3.Rest.Dto;

import com.desafio3.desafio3.Domain.Enums.UserRoles;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastroDto{

    @NotEmpty
    @Email(message = "Favor inserir email válido. Exemplo: nome@dominio.com.br")
    private String login;

    @NotEmpty
    @Size(min = 6, message = "Senha deve conter no mínimo 6 caracteres.")
    private String senha;

    private UserRoles role;
}
