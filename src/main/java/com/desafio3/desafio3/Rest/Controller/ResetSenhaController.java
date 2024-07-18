package com.desafio3.desafio3.Rest.Controller;

import com.desafio3.desafio3.Domain.Entity.Usuario;
import com.desafio3.desafio3.Domain.Repository.UsuarioRepository;
import com.desafio3.desafio3.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class ResetSenhaController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EmailService emailService;

    @PostMapping("/resetar-senha")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email) {
        Usuario user = (Usuario) usuarioRepository.findByLogin(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email não encontrado");
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setValidadeToken(LocalDateTime.now().plusHours(1)); // Token válido por 1 hora
        usuarioRepository.save(user);

        // Enviar o token por e-mail
        emailService.sendEmail(user.getLogin(), "Reset de Senha", "Para resetar sua senha, use o token: " + token);

        return ResponseEntity.ok("Token de reset de senha enviado para o e-mail");
    }

    @PostMapping("/resetar-senha/token")
    public ResponseEntity<?> confirmResetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
        Usuario user = usuarioRepository.findByResetToken(token);
        if (user == null || user.getValidadeToken().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido ou expirado");
        }

        user.setSenha(new BCryptPasswordEncoder().encode(newPassword));
        user.setResetToken(null); // Remover o token após a redefinição da senha
        user.setValidadeToken(null);
        usuarioRepository.save(user);

        return ResponseEntity.ok("Senha resetada com sucesso");
    }

}
