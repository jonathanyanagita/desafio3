package com.desafio3.desafio3.Rest.Dto;

import com.desafio3.desafio3.Domain.Enums.UserRoles;

public record CadastroDto(String login, String senha, UserRoles role) {
}
