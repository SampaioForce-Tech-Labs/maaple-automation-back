package dev.danilobarreto.app.controller;

import dev.danilobarreto.app.service.ClienteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {


    @Autowired
    private ClienteService clienteService;

    @PostMapping("/upload")
    @Transactional
    public ResponseEntity<String> uploadPlanilha(@RequestParam("file") MultipartFile file) {
        try {
            clienteService.processarPlanilha(file);
            return ResponseEntity.ok("Planilha processada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a planilha: " + e.getMessage());
        }
    }


}