package dev.danilobarreto.app.controller;


import com.itextpdf.io.exceptions.IOException;
import com.mongodb.client.gridfs.model.GridFSFile;
import dev.danilobarreto.app.service.DocumentoService;
import org.apache.commons.compress.utils.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<byte[]> downloadDocumento(@PathVariable("id") String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(objectId)));

            if (file != null) {
                GridFsResource resource = gridFsTemplate.getResource(file);
                InputStream inputStream = resource.getInputStream();
                byte[] fileContent = IOUtils.toByteArray(inputStream);

                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(fileContent);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar/{razaoSocialAss}")
    public ResponseEntity<List<Map<String, String>>> buscarPorRazaoSocialAss(@PathVariable("razaoSocialAss") String razaoSocialAss) {
        List<GridFSFile> documentos = documentoService.buscarDocumentosPorRazaoSocialAss(razaoSocialAss);

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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDocumento(@PathVariable("id") String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(objectId)));
            return ResponseEntity.ok("Documento deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar o documento: " + e.getMessage());
        }
    }
}

