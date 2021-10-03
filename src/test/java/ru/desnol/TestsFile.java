package ru.desnol;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;
import static com.codeborne.xlstest.XLS.containsText;
import java.io.*;
import net.lingala.zip4j.ZipFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class TestsFile {

    @Test
    void declaimTextTest() throws Exception {
        try (FileReader reader = new FileReader("src/test/resources/example.txt");
             BufferedReader br = new BufferedReader(reader)) {
            // read line by line
            String line;
            while ((line = br.readLine()) != null) ;
            assert ("Mariya Logutenko").contains("Mariya");
        }
    }

    @Test
    void declaimPdfTest() throws Exception {
        PDF pdf = new PDF(getClass().getClassLoader().getResource("cookBook.pdf"));
        assertThat(pdf.author, equalTo("Myra Gupta"));
    }

    @Test
    void declaimXlsTest() throws Exception {
        XLS xls = new XLS(getClass().getClassLoader().getResource("test.xls"));
        assertThat(xls, containsText("розовый"));
    }

    @Test
    void declaimZipTest() throws Exception {
        ZipFile zipFile = new ZipFile("./src/test/resources/archiv.zip");
        if (zipFile.isEncrypted())
            zipFile.setPassword("admin".toCharArray());
        assertThat(zipFile.getFileHeaders().get(0).toString()).contains("name.txt");
        zipFile.extractAll("./src/test/resources/extractzip");

        String result;
        try (FileInputStream stream = new FileInputStream("./src/test/resources/extractzip/name.txt")) {
            result = new String(stream.readAllBytes(), "UTF-8");
        }
        assertThat(result).contains("Mariya");
    }

    @Test
    void declaimDocTest() throws Exception {
        try (FileInputStream stream = new FileInputStream("src/test/resources/frog.docx")) {
            XWPFDocument docxFile = new XWPFDocument(OPCPackage.open(stream));
            XWPFWordExtractor extractor = new XWPFWordExtractor(docxFile);
            assert ("В полднях от горячих лучей солнца стал плавиться снег").contains("лучей");
        }
    }
}