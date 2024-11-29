package dev.danilobarreto.app.service;

import dev.danilobarreto.app.model.Cliente;
import dev.danilobarreto.app.repository.ClienteRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public void processarPlanilha(MultipartFile file) throws Exception {
        List<Cliente> clientes = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                Cliente cliente = new Cliente();
                cliente.setCnpjCpf(getCellValue(row.getCell(0)));
                cliente.setRazaoSocial(getCellValue(row.getCell(1)));
                cliente.setBairro(getCellValue(row.getCell(2)));
                cliente.setCidade(getCellValue(row.getCell(3)));
                cliente.setEstado(getCellValue(row.getCell(4)));
                cliente.setTelefone(getCellValue(row.getCell(5)));
                cliente.setEmail(getCellValue(row.getCell(6)));
                cliente.setCep(getCellValue(row.getCell(7)));
                cliente.setEndereco(getCellValue(row.getCell(8)));
                cliente.setDataInclusao(parseDateFromCell(row.getCell(9)));

                clientes.add(cliente);
            }
        } catch (Exception e) {
            throw new Exception("Erro ao processar a planilha: " + e.getMessage(), e);
        }

        clienteRepository.saveAll(clientes);
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private LocalDate parseDateFromCell(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return null;
        }

        if (DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else {
            throw new IllegalArgumentException("Formato inválido de data na planilha.");
        }
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }
}
