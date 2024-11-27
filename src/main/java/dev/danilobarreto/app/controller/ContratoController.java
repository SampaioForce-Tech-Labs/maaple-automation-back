package dev.danilobarreto.app.controller;

import dev.danilobarreto.app.repository.ClienteRepository;
import dev.danilobarreto.app.service.ContratoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;

@RequestMapping("/api/contratos")
@RestController
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/gerar-contrato")
    public String mostrarFormularioDeContrato(Model model) {
        model.addAttribute("clientes", clienteRepository.findAll());
        return "gerar-contrato";
    }


    @PostMapping("/gerar-contrato")
    public String gerarContrato(@RequestParam String nomeCliente, @RequestParam String modeloContrato, Model model) {
        String contratoGerado = contratoService.gerarContrato(nomeCliente, modeloContrato);
        model.addAttribute("contrato", contratoGerado);
        return "contrato-gerado";
    }


    @PostMapping("/exportar-contrato-pdf")
    public ResponseEntity<Resource> exportarContratoPDF(@RequestParam String nomeCliente, @RequestParam String modeloContrato) throws Exception {
        String contratoGerado = contratoService.gerarContrato(nomeCliente, modeloContrato);


        String caminhoArquivo = "/tmp/contrato_" + nomeCliente + ".pdf";
        contratoService.exportarContratoParaPDF(contratoGerado, caminhoArquivo);

        File file = new File(caminhoArquivo);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .body((Resource) resource);
    }
}
