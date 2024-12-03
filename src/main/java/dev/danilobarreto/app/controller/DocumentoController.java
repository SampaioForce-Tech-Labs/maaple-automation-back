package dev.danilobarreto.app.controller;


import dev.danilobarreto.app.model.mongoDB.Documento;
import dev.danilobarreto.app.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @GetMapping("/listar")
    public ResponseEntity<List<Documento>> listarDocumentos() {
        List<Documento> documentos = documentoService.listarDocumentos();
        return ResponseEntity.ok(documentos);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocumento(@RequestParam("arquivo") MultipartFile arquivo) {
        if (arquivo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Arquivo não pode estar vazio");
        }
        try {
            documentoService.salvarDocumento(arquivo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Documento salvo com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar documento");
        }
    }

    @GetMapping("/download/{documentoId}")
    public ResponseEntity<byte[]> downloadDocumento(@PathVariable String documentoId) {
        try {

            Documento documento = documentoService.buscarDocumentoPorId(documentoId);

            if (documento == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            byte[] fileContent = documento.getConteudo();


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(documento.getNome())
                    .build());


            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
