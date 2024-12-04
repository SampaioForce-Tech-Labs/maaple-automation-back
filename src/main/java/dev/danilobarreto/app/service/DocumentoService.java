package dev.danilobarreto.app.service;

import com.mongodb.client.gridfs.model.GridFSFile;
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


}