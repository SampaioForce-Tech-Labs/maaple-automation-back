package dev.danilobarreto.app.controller;


import com.itextpdf.io.exceptions.IOException;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import dev.danilobarreto.app.model.mongoDB.Documento;
import dev.danilobarreto.app.service.DocumentoService;
import org.apache.commons.compress.utils.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private DocumentoService documentoService;

    @GetMapping("/listar")
    public ResponseEntity<List<Map<String, String>>> listarDocumentos() {
        List<GridFSFile> documentos = documentoService.listarDocumentos();

        List<Map<String, String>> documentosListados = documentos.stream()
                .map(doc -> {
                    Map<String, String> docInfo = new HashMap<>();
                    docInfo.put("id", doc.getId().toString());
                    docInfo.put("nome", doc.getFilename());
                    return docInfo;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(documentosListados);
    }

    @PostMapping("/upload")
    public String uploadDocumento(@RequestParam("file") MultipartFile file) {
        try {
            documentoService.salvarDocumento(file);
            return "Documento enviado com sucesso!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao salvar o documento: " + e.getMessage();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocumento(@PathVariable String id) {
        try {
            if (id.length() != 24 || !ObjectId.isValid(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("ID de documento inválido".getBytes());
            }

            // Convertendo o ID para ObjectId
            ObjectId objectId = new ObjectId(id);

            // Busca o arquivo no GridFS usando o ObjectId
            GridFSFile file = documentoService.buscarDocumentoPorId(String.valueOf(objectId));

            if (file != null) {
                // Obtém o fluxo de download do GridFS
                GridFSDownloadStream downloadStream = (GridFSDownloadStream) gridFsTemplate.getResource(file.getId().toString()).getInputStream();

                // Converte o conteúdo do arquivo em bytes
                byte[] fileContent = IOUtils.toByteArray(downloadStream);

                // Retorna o arquivo com os headers adequados
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                        .body(fileContent);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            // Logando erro e retornando Internal Server Error
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

