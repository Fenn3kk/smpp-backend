package br.ufsm.smpp.controller;

import br.ufsm.smpp.dto.AuthDTOs;
import br.ufsm.smpp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para lidar com as operações de autenticação,
 * como login e registro de novos usuários.
 * Este é o ponto de entrada para usuários que acessam o sistema.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * Serviço que contém a lógica de negócio para autenticação e registro.
     * Injetado via construtor pelo Lombok (@RequiredArgsConstructor).
     */
    private final AuthService authService;

    /**
     * Endpoint para autenticar um usuário e gerar um token JWT.
     *
     * @param request O corpo da requisição contendo as credenciais de login (e-mail e senha),
     *                validado com @Valid.
     * @return ResponseEntity com status 200 (OK) e um {@link AuthDTOs.JwtResponse}
     *         contendo o token e dados básicos do usuário no corpo.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthDTOs.JwtResponse> login(@RequestBody @Valid AuthDTOs.LoginRequest request) {
        AuthDTOs.JwtResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para registrar um novo usuário no sistema.
     *
     * @param request O corpo da requisição contendo os dados para o cadastro
     *                do novo usuário, validado com @Valid.
     * @return ResponseEntity com status 201 (CREATED) e uma mensagem de sucesso no corpo.
     */
    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@RequestBody @Valid AuthDTOs.RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
    }
}