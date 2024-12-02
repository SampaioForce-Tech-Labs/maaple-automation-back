package dev.danilobarreto.app.controller;

import dev.danilobarreto.app.model.mongoDB.Contrato;
import dev.danilobarreto.app.service.ContratoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @GetMapping("/buscar/{nomeArquivo}")
    public ResponseEntity<String> buscarContrato(@RequestParam("nomeArquivo") String nomeArquivo) {
        try {
            contratoService.findContratoByName(nomeArquivo);

            return ResponseEntity.ok("Contrato enviado!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao buscar contrato: " + e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Contrato>> listarContratos() {
        List<Contrato> contratos = contratoService.listarContratos();
        return ResponseEntity.ok(contratos);
    }

    @PostMapping("/salvar")
    public ResponseEntity<String> salvarContrato(@RequestBody Contrato contrato) {
        contratoService.salvarContrato(contrato);
        return ResponseEntity.ok("Contrato salvo com sucesso!");
    }

    @PutMapping("/editar/{nomeArquivo}")
    public ResponseEntity<String> editarContrato(@PathVariable String nomeArquivo, @RequestBody Contrato contrato) {
        contratoService.editarContrato(nomeArquivo, contrato);
        return ResponseEntity.ok("Contrato editado com sucesso!");
    }

    @DeleteMapping("/excluir/{nomeArquivo}")
    public ResponseEntity<String> excluirContrato(@PathVariable String nomeArquivo) {
        contratoService.excluirContrato(nomeArquivo);
        return ResponseEntity.ok("Contrato excluído com sucesso!");
    }

    @GetMapping("/download/{nomeArquivo}")
    public ResponseEntity<Resource> baixarContrato(@PathVariable String nomeArquivo) {
        return null;
    }
}


