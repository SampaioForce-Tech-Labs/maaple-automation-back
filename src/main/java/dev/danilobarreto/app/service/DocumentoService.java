package dev.danilobarreto.app.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentoService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentoService.class);


    @Autowired
    private GridFsTemplate gridFsTemplate;


    public void salvarDocumento(MultipartFile arquivo) throws IOException {
        InputStream inputStream = arquivo.getInputStream();

        gridFsTemplate.store(inputStream, arquivo.getOriginalFilename(), arquivo.getContentType());
    }


    public List<GridFSFile> listarDocumentos() {
        List<GridFSFile> documentos = new ArrayList<>();
        gridFsTemplate.find(Query.query(Criteria.where("filename").exists(true)))
                .forEach(documentos::add);
        return documentos;
    }

    public List<GridFSFile> buscarDocumentosPorRazaoSocialAss(String razaoSocialAss) {
        List<GridFSFile> documentos = new ArrayList<>();
        gridFsTemplate.find(Query.query(Criteria.where("razaoSocialAss").is(razaoSocialAss)))
                .forEach(documentos::add);
        return documentos;
    }

    public void atualizarDocumento(MultipartFile file, String id) {
        ObjectId objectId = new ObjectId(id);
        GridFSFile existingFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(objectId)));

        if (existingFile != null) {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(objectId)));
            // Save new document with same ID
            try {
                gridFsTemplate.store(file.getInputStream(), file.getContentType());
            } catch (IOException e) {
                throw new RuntimeException("Failed to store updated file", e);
            }
        } else {
            throw new RuntimeException("Document not found with id: " + id);
        }
    }
}