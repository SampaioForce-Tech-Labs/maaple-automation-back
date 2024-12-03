package dev.danilobarreto.app.service;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import dev.danilobarreto.app.model.Cliente;
import dev.danilobarreto.app.model.mongoDB.Contrato;
import dev.danilobarreto.app.repository.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private Contrato contrato;

    public void salvarContrato(Contrato contrato) {
        contratoRepository.save(contrato);
    }

    public List<Contrato> listarContratos() {
        return contratoRepository.findAll();
    }

    public Contrato findContratoByName(String nomeArquivo) {
        return contratoRepository.findByNomeArquivo(nomeArquivo);
    }

    public List<Contrato> findContratoByRazaoSocialAss(String razaoSocialAss) {
        return contratoRepository.findByRazaoSocialAss(razaoSocialAss);
    }

    public void editarContrato(String nomeArquivo, Contrato novoContrato) {
        Contrato contrato = findContratoByName(nomeArquivo);
        if (contrato != null) {
            contrato.setNomeArquivo(novoContrato.getNomeArquivo());
            contrato.setConteudoPdf(novoContrato.getConteudoPdf());
            contratoRepository.save(contrato);
        }
    }

    public void excluirContrato(String nomeArquivo) {
        Contrato contrato = findContratoByName(nomeArquivo);
        if (contrato != null) {
            contratoRepository.delete(contrato);
        }
    }
}
