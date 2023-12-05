package com.info5059.casestudy.purchaseOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.info5059.casestudy.vendor.VendorRepository;
import com.info5059.casestudy.product.ProductRepository;
import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;

import java.io.ByteArrayInputStream;

//@CrossOrigin
@RestController
public class PurchaseOrderPDFController {

    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @GetMapping(value = "/PurchaseOrderPDF", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> streamPDF(HttpServletRequest request) throws IOException {

        // get formatted pdf as a stream
        String porderid = request.getParameter("porderid");
        try {
            ByteArrayInputStream bis = PurchaseOrderPDFGenerator.generatePurchaseOrder(
                    porderid,
                    purchaseOrderRepository,
                    vendorRepository,
                    productRepository);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=examplepurchaseOrder.pdf");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        // headers.add("Content-Disposition", "inline;
        // filename=examplepurchaseOrder.pdf");
    }
}
