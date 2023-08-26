package guru.qa;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.codeborne.xlstest.XLS;
import com.codeborne.pdftest.PDF;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ZipArchiveFilesTests {
    private static String zipFile = "zip/sample-zip.zip";
    private static String targetPdfFileName = "sample.pdf";
    private static String targetCsvFileName = "sample.csv";
    private static String targetXlsFileName = "sample.xlsx";
    private static String expectedContent = "Договор аренды";

    private static ClassLoader cl = ZipArchiveFilesTests.class.getClassLoader();

    private InputStream getInputStreamForFileFromZip(String fileName) throws IOException {
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream(zipFile));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            if (entry.getName().equals(fileName)) {
                return is;
            }
        }
        throw new IOException("Файл " + fileName + " не найден в архиве");
    }

    @Test
    @DisplayName("Pdf файл начинается с нужной строки")
    void findContentInPdfInZip() throws IOException {
        try (InputStream inputStream = getInputStreamForFileFromZip(targetPdfFileName)) {
            PDF pdf = new PDF(inputStream);
            String pdfText = pdf.text;
            assertThat(pdfText).startsWith(expectedContent);
        }
    }

    @Test
    @DisplayName("Xls файл начинается с нужной строки")
    void findContentInXlsInZip() throws IOException {
        try (InputStream inputStream = getInputStreamForFileFromZip(targetXlsFileName)) {
            XLS xls = new XLS(inputStream);
            String firstCellValue = xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue();
            assertThat(firstCellValue).contains(expectedContent);
        }
    }

    @Test
    @DisplayName("Csv файл начинается с нужной строки")
    void findContentInCsvInZip() throws Exception {
        try (InputStream inputStream = getInputStreamForFileFromZip(targetCsvFileName);
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String firstValue = reader.readNext()[0];
            assertThat(firstValue).contains(expectedContent);
        }
    }
}