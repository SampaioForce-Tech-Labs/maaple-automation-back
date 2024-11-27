package dev.danilobarreto.app.service;

import dev.danilobarreto.app.model.Cliente;
import dev.danilobarreto.app.repository.ClienteRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PlanilhaService {

    @Autowired
    private ClienteRepository clienteRepository;

    public void processarPlanilha(MultipartFile file) throws Exception {
        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            throw new IllegalArgumentException("O arquivo deve ser no formato .xlsx");
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                // Ler as células da linha (cada célula é uma coluna)
                String cnpjCpf = row.getCell(0).getStringCellValue();
                String razaoSocial = row.getCell(1).getStringCellValue();
                String bairro = row.getCell(2).getStringCellValue();
                String cidade = row.getCell(3).getStringCellValue();
                String estado = row.getCell(4).getStringCellValue();
                String telefone = row.getCell(5).getStringCellValue();
                String email = row.getCell(6).getStringCellValue();
                String cep = row.getCell(7).getStringCellValue();
                // LocalDate dataInclusao = row.getCell(8).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String endereco = row.getCell(9).getStringCellValue();


                // Criar objeto Cliente e preencher os campos
                Cliente cliente = new Cliente();
                cliente.setCnpjCpf(cnpjCpf);
                cliente.setRazaoSocial(razaoSocial);
                // Preencher os outros campos...


                clienteRepository.save(cliente);
            }
        }
    }
}
