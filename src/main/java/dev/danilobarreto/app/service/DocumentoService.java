package dev.danilobarreto.app.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import dev.danilobarreto.app.model.Cliente;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class DocumentoService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentoService.class);


    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private ClienteService clienteService;


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

    public byte[] preencherDocumento(MultipartFile file, String razaoSocial) throws IOException {
        Optional<Cliente> clienteOptional = clienteService.findClienteByRazaoSocial(razaoSocial);

        if (clienteOptional.isEmpty()) {
            throw new IllegalArgumentException("Cliente with razaoSocial '" + razaoSocial + "' not found.");
        }

        Cliente cliente = clienteOptional.get();

        // Define field mappings for placeholders
        Map<String, String> fieldMappings = new HashMap<>();
        fieldMappings.put("EDITABLE_CNPJ", cliente.getCnpjCpf());
        fieldMappings.put("EDITABLE_NOME", cliente.getRazaoSocial());
        fieldMappings.put("EDITABLE_BAIRRO", cliente.getBairro());
        fieldMappings.put("EDITABLE_CIDADE", cliente.getCidade());
        fieldMappings.put("EDITABLE_ESTADO", cliente.getEstado());
        fieldMappings.put("EDITABLE_TELEFONE", cliente.getTelefone());
        fieldMappings.put("EDITABLE_EMAIL", cliente.getEmail());
        fieldMappings.put("EDITABLE_CEP", cliente.getCep());

        // Load the PDF document
        try (PDDocument document = PDDocument.load(file.getInputStream())) {

            // Iterate through all pages to find and replace placeholders
            for (PDPage page : document.getPages()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();

                // Convert PDRectangle to Rectangle2D
                Rectangle2D region = new Rectangle2D.Float(
                        page.getBBox().getLowerLeftX(),
                        page.getBBox().getLowerLeftY(),
                        page.getBBox().getWidth(),
                        page.getBBox().getHeight()
                );

                stripper.addRegion("placeholderRegion", region);
                stripper.extractRegions(page);

                // Extract text from the page
                String pageText = stripper.getTextForRegion("placeholderRegion");

                // Replace each placeholder with the corresponding value
                for (Map.Entry<String, String> entry : fieldMappings.entrySet()) {
                    pageText = pageText.replace(entry.getKey(), entry.getValue());
                }

                // Write the modified text back to the page
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true)) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(50, 750); // Adjust coordinates as needed
                    contentStream.showText(pageText);
                    contentStream.endText();
                }
            }

            // Save the updated document to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);

            return outputStream.toByteArray();
        }
    }
}