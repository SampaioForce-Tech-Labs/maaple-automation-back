package dev.danilobarreto.app.controller;

import dev.danilobarreto.app.service.ContratoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @PostMapping("/gerar")
    public ResponseEntity<String> gerarContrato(@RequestParam Long id) {
        try {
            String camingoPDFOriginal = "src/main/resources/contrato.pdf";
            String caminhoPDFPreenchido = "src/main/resources/contrato_preenchido.pdf";

            contratoService.preencherContrato(id, camingoPDFOriginal, caminhoPDFPreenchido);

            return ResponseEntity.ok("Contrato gerado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao gerar contrato: " + e.getMessage());
        }


    }

    @GetMapping("/download/{nomeArquivo}")
    public ResponseEntity<Resource> baixarContrato(@PathVariable String nomeArquivo) {
        return null;
    }
}


