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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthDTOs.JwtResponse> login(@RequestBody @Valid AuthDTOs.LoginRequest request) {
        AuthDTOs.JwtResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@RequestBody @Valid AuthDTOs.RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
    }
}