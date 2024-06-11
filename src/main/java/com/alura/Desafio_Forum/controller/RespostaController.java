package com.alura.Desafio_Forum.controller;

import com.alura.Desafio_Forum.domain.Usuario;
import com.alura.Desafio_Forum.dto.request.RespostaDto;
import com.alura.Desafio_Forum.service.RespostaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/respostas")
@SecurityRequirement(name = "bearer-key")
public class RespostaController {
    @Autowired
    private RespostaService service;




}
