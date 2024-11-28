package dev.danilobarreto.app.controller;

import dev.danilobarreto.app.service.ClienteService;
import dev.danilobarreto.app.service.PlanilhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PlanilhaService planilhaService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPlanilha(@RequestParam("file") MultipartFile file) {
        try {
            planilhaService.processarPlanilha(file);
            return ResponseEntity.ok("Planilha processada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a planilha: " + e.getMessage());
        }
    }


}