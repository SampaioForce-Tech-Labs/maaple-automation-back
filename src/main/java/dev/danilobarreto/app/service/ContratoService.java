package dev.danilobarreto.app.service;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import dev.danilobarreto.app.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ContratoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void preencherContrato(Long id, String caminhoPDForiginal, String caminhoPDFpreenchido) throws Exception {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        Cliente cliente = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Cliente c = new Cliente();
            c.setId(rs.getLong("id"));
            c.setCnpjCpf(rs.getString("cnpj_cpf"));
            c.setRazaoSocial(rs.getString("razao_social"));
            c.setBairro(rs.getString("bairro"));
            c.setCidade(rs.getString("cidade"));
            c.setEstado(rs.getString("estado"));
            c.setTelefone(rs.getString("telefone"));
            c.setEmail(rs.getString("email"));
            c.setCep(rs.getString("cep"));
            c.setDataInclusao(rs.getDate("data_inclusao").toLocalDate());
            c.setEndereco(rs.getString("endereco"));
            return c;
        });

        PdfReader reader = new PdfReader(caminhoPDForiginal);
        PdfWriter writer = new PdfWriter(caminhoPDFpreenchido);
        PdfDocument document = new PdfDocument(reader, writer);

        PdfAcroForm form = PdfAcroForm.getAcroForm(document, true);

        form.getField("razao-social").setValue(cliente.getRazaoSocial());
        form.getField("cnpj-cpf").setValue(cliente.getCnpjCpf());
        form.getField("endereco").setValue(cliente.getEndereco());
        form.getField("bairro").setValue(cliente.getBairro());
        form.getField("cidade").setValue(cliente.getCidade());
        form.getField("estado").setValue(cliente.getEstado());
        form.getField("cep").setValue(cliente.getCep());
        form.getField("email").setValue(cliente.getEmail());
        form.getField("telefone").setValue(cliente.getTelefone());

        document.close();
    }


}
