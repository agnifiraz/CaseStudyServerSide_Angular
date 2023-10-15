package com.info5059.casestudy.pdfexample;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayInputStream;

//@CrossOrigin
@RestController
public class Controller {
    @RequestMapping(value = "/ExamplePDF", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> exampleReport() {
        // get formatted pdf as a stream
        ByteArrayInputStream bis = Generator.generateReport();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=examplereport.pdf");
        // dump stream to browser
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
