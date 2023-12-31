package com.info5059.casestudy.purchaseOrder;

import com.info5059.casestudy.vendor.Vendor;
import com.info5059.casestudy.vendor.VendorRepository;
import com.info5059.casestudy.product.Product;
import com.info5059.casestudy.product.ProductRepository;
import com.info5059.casestudy.product.QRCodeGenerator;
import com.info5059.casestudy.pdfexample.Generator;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PurchaseOrderPDFGenerator - a class for creating dynamic product
 * purchaseOrder output in
 * PDF format using the iText 7 library
 *
 * @author Agnita
 */
public abstract class PurchaseOrderPDFGenerator extends AbstractPdfView {
        public static ByteArrayInputStream generatePurchaseOrder(
                        String porderid,
                        PurchaseOrderRepository purchaseOrderRepository,
                        VendorRepository vendorRepository,
                        ProductRepository productRepository) throws IOException {

                PurchaseOrder purchaseOrder = new PurchaseOrder();
                URL imageUrl = Generator.class.getResource("/static/images/logo.png");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                PdfWriter writer = new PdfWriter(baos);
                // Initialize PDF document to be written to a stream not a file
                PdfDocument pdf = new PdfDocument(writer);
                // Document is the main object
                Document document = new Document(pdf);
                PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                // add the image to the document
                PageSize pg = PageSize.A4;
                Image img = new Image(ImageDataFactory.create(imageUrl))
                                .setMarginBottom(100)
                                .scaleAbsolute(120, 90)
                                .setFixedPosition(pg.getWidth() / 2 - 60, 750);
                document.add(img);
                // now let's add a big heading
                document.add(new Paragraph("\n\n"));
                Locale locale = Locale.of("en", "US");
                NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);

                try {
                        // Get the purchaseOrder data
                        Optional<PurchaseOrder> opt = purchaseOrderRepository.findById(Long.parseLong(porderid));
                        if (opt.isPresent()) {
                                purchaseOrder = opt.get();
                        }

                        document.add(new Paragraph("\n\n"));
                        document.add(new Paragraph("Purchase Order# " + porderid)
                                        .setFont(font)
                                        .setFontSize(18)
                                        .setBold()
                                        .setMarginRight(pg.getWidth() / 2 - 60).setMarginTop(-10)
                                        .setTextAlignment(TextAlignment.RIGHT));

                        document.add(new Paragraph("\n\n"));
                        Table vendorTable = new Table(2).setWidth(new UnitValue(UnitValue.PERCENT, 30))
                                        .setHorizontalAlignment(HorizontalAlignment.LEFT);

                        Optional<Vendor> vendorOpt = vendorRepository.findById(purchaseOrder.getVendorid());
                        if (vendorOpt.isPresent()) {
                                Vendor vendor = vendorOpt.get();
                                Cell cell = new Cell().add(new Paragraph("Vendor:")
                                                .setBold())
                                                .setBorder(Border.NO_BORDER);
                                vendorTable.addCell(cell);

                                cell = new Cell(1, 2).add(new Paragraph(vendor.getName())
                                                .setBold()
                                                .setTextAlignment(TextAlignment.RIGHT)
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY))
                                                .setBorder(Border.NO_BORDER);
                                vendorTable.addCell(cell);

                                cell = new Cell(1, 2).add(new Paragraph(vendor.getAddress1())
                                                .setBold()
                                                .setTextAlignment(TextAlignment.RIGHT)
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY))
                                                .setBorder(Border.NO_BORDER);
                                vendorTable.addCell(cell);

                                cell = new Cell(1, 2).add(new Paragraph(vendor.getCity())
                                                .setBold()
                                                .setTextAlignment(TextAlignment.RIGHT)
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY))
                                                .setBorder(Border.NO_BORDER);
                                vendorTable.addCell(cell);

                                cell = new Cell(1, 2).add(new Paragraph(vendor.getProvince())
                                                .setBold()
                                                .setTextAlignment(TextAlignment.RIGHT)
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY))
                                                .setBorder(Border.NO_BORDER);
                                vendorTable.addCell(cell);

                                cell = new Cell(1, 2).add(new Paragraph(vendor.getEmail())
                                                .setBold()
                                                .setTextAlignment(TextAlignment.RIGHT)
                                                .setBackgroundColor(ColorConstants.LIGHT_GRAY))
                                                .setBorder(Border.NO_BORDER);
                                vendorTable.addCell(cell);
                        }

                        // Product details table
                        Table productTable = new Table(5);
                        productTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));
                        BigDecimal total = new BigDecimal(0.0);
                        BigDecimal subtotal = new BigDecimal(0.0);
                        BigDecimal tax = new BigDecimal(0.0);
                        BigDecimal extPrice = new BigDecimal(0.0);

                        Cell headerCell = new Cell().add(new Paragraph("Product Code")
                                        .setBold()
                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                        .setTextAlignment(TextAlignment.CENTER));
                        productTable.addCell(headerCell);

                        headerCell = new Cell().add(new Paragraph("Description")
                                        .setBold()
                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                        .setTextAlignment(TextAlignment.CENTER));
                        productTable.addCell(headerCell);

                        headerCell = new Cell().add(new Paragraph("Qty Sold")
                                        .setBold()
                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                        .setTextAlignment(TextAlignment.CENTER));
                        productTable.addCell(headerCell);

                        headerCell = new Cell().add(new Paragraph("Price")
                                        .setBold()
                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                        .setTextAlignment(TextAlignment.CENTER));
                        productTable.addCell(headerCell);

                        headerCell = new Cell().add(new Paragraph("Ext. Price")
                                        .setBold()
                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                        .setTextAlignment(TextAlignment.CENTER));
                        productTable.addCell(headerCell);

                        for (PurchaseOrderLineitem line : purchaseOrder.getItems()) {
                                Optional<Product> optx = productRepository.findById(line.getProductid());
                                if (optx.isPresent()) {
                                        Product product = optx.get();

                                        // Dump Id, Date Incurred, Description, and Amount columns
                                        Cell cell = new Cell().add(new Paragraph(String.valueOf(product.getId()))
                                                        .setTextAlignment(TextAlignment.CENTER));
                                        productTable.addCell(cell);

                                        cell = new Cell()
                                                        .add(new Paragraph(product.getName())
                                                                        .setTextAlignment(TextAlignment.CENTER));
                                        productTable.addCell(cell);

                                        cell = new Cell()
                                                        .add(new Paragraph(String.valueOf(line.getQty()))
                                                                        .setTextAlignment(TextAlignment.RIGHT));
                                        productTable.addCell(cell);

                                        cell = new Cell()
                                                        .add(new Paragraph(formatter.format(product.getCostprice()))
                                                                        .setTextAlignment(TextAlignment.RIGHT));
                                        cell.setTextAlignment(TextAlignment.RIGHT);
                                        productTable.addCell(cell);

                                        extPrice = product.getCostprice().multiply(new BigDecimal(line.getQty()),
                                                        new MathContext(8, RoundingMode.UP));

                                        cell = new Cell()
                                                        .add(new Paragraph(formatter.format(extPrice))
                                                                        .setTextAlignment(TextAlignment.RIGHT));
                                        cell.setTextAlignment(TextAlignment.RIGHT);
                                        productTable.addCell(cell);

                                        subtotal = subtotal.add(extPrice, new MathContext(8, RoundingMode.UP));
                                        BigDecimal taxRate = new BigDecimal("0.13");
                                        tax = subtotal.multiply(taxRate, new MathContext(8, RoundingMode.UP));
                                        total = subtotal.add(tax, new MathContext(8, RoundingMode.UP));
                                        // total = total.add(subtotal + tax, new MathContext(8, RoundingMode.UP));

                                }
                        }

                        Cell cell = new Cell(1, 4).add(new Paragraph("Sub Total:")
                                        .setBorder(Border.NO_BORDER)
                                        .setTextAlignment(TextAlignment.RIGHT))
                                        .setBorder(Border.NO_BORDER);
                        productTable.addCell(cell);

                        cell = new Cell().add(new Paragraph(formatter.format(subtotal))
                                        .setTextAlignment(TextAlignment.RIGHT));
                        productTable.addCell(cell);

                        cell = new Cell(1, 4).add(new Paragraph("Tax: ")
                                        .setBorder(Border.NO_BORDER)
                                        .setTextAlignment(TextAlignment.RIGHT))
                                        .setBorder(Border.NO_BORDER);
                        productTable.addCell(cell);

                        cell = new Cell().add(new Paragraph(formatter.format(tax))
                                        .setTextAlignment(TextAlignment.RIGHT));
                        productTable.addCell(cell);

                        cell = new Cell(1, 4).add(new Paragraph("PO Total:")
                                        .setBorder(Border.NO_BORDER)
                                        .setTextAlignment(TextAlignment.RIGHT))
                                        .setBorder(Border.NO_BORDER);
                        productTable.addCell(cell);

                        cell = new Cell().add(new Paragraph(formatter.format(total))
                                        .setTextAlignment(TextAlignment.RIGHT)
                                        .setBackgroundColor(ColorConstants.YELLOW));
                        productTable.addCell(cell);

                        // Add the tables to the document
                        document.add(vendorTable);
                        document.add(new Paragraph("\n\n"));
                        document.add(productTable);

                        document.add(new Paragraph("\n\n"));

                        LocalDateTime podate;

                        podate = purchaseOrder.getPodate();

                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm:ss a");
                        document.add(new Paragraph(dateFormatter.format(podate))
                                        .setTextAlignment(TextAlignment.CENTER));

                        Image qrcode = addSummaryQRCode(vendorOpt, purchaseOrder);
                        document.add(qrcode);

                        document.close();

                } catch (Exception ex) {
                        Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
                }
                // finally send stream back to the controller
                return new ByteArrayInputStream(baos.toByteArray());

        }

        private static Image addSummaryQRCode(Optional<Vendor> vendorOpt, PurchaseOrder po) {

                Locale locale = Locale.of("en", "US");
                NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
                String summaryText = "Summary for Purchase Order:" + po.getId() + "\nDate:"
                                + po.getPodate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm:ss a"))
                                + "\nVendor:"
                                + vendorOpt.map(Vendor::getName).orElse("") + "\nTotal:"
                                + formatter.format(po.getAmount());

                QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();
                byte[] qrcodebin = qrCodeGenerator.generateQRCode(summaryText);

                Image qrcode = new Image(ImageDataFactory.create(qrcodebin)).scaleAbsolute(100, 100)
                                .setFixedPosition(460, 60);

                return qrcode;
        }
}
