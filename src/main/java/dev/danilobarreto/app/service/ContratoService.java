package dev.danilobarreto.app.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import dev.danilobarreto.app.model.Cliente;
import dev.danilobarreto.app.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContratoService {

    @Autowired
    private ClienteRepository clienteRepository;

    public String gerarContrato(String nomeCliente, String modeloContrato) {

        Cliente cliente = clienteRepository.findByRazaoSocial(nomeCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + nomeCliente));


        String contratoGerado = modeloContrato
                .replace("NOME", cliente.getRazaoSocial())
                .replace("CPF/CNPJ", cliente.getCnpjCpf())
                .replace("endereço", cliente.getEndereco() != null ? cliente.getEndereco() : "N/D")
                .replace("bairro", cliente.getBairro() != null ? cliente.getBairro() : "N/D")
                .replace("cidade/estado", cliente.getCidade() + "/" + cliente.getEstado())
                .replace("CEP", cliente.getCep() != null ? cliente.getCep() : "N/D")
                .replace("endereço eletrônico", cliente.getEmail() != null ? cliente.getEmail() : "N/D")
                .replace("telefone", cliente.getTelefone() != null ? cliente.getTelefone() : "N/D");

        return contratoGerado;
    }

    public void exportarContratoParaPDF(String contratoGerado, String caminhoArquivo) throws Exception {
        PdfWriter writer = new PdfWriter(caminhoArquivo);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Adiciona o conteúdo do contrato ao PDF
        document.add(new Paragraph(contratoGerado));
        document.close();
    }
}
