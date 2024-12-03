package dev.danilobarreto.app.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import dev.danilobarreto.app.model.mongoDB.Documento;
import dev.danilobarreto.app.repository.DocumentoRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentoService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentoService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;


    public void salvarDocumento(MultipartFile arquivo) {
        try {
            DBObject doc = new BasicDBObject();
            doc.put("nome", arquivo.getOriginalFilename());
            doc.put("arquivo", arquivo.getBytes());

            mongoTemplate.insert(doc, "documentos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Documento buscarDocumentoPorId(String documentoId) {
        return documentoRepository.findById(documentoId).orElse(null);
    }


    public List<Documento> listarDocumentos() {
        return documentoRepository.findAll();
    }
}