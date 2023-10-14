package com.info5059.casestudy.pdfexample;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generator - a class for testing how to create dynamic output in PDF
 * format using the iText 7 library
 */
public abstract class Generator extends AbstractPdfView {
    public static ByteArrayInputStream generateReport() {
        URL imageUrl = Generator.class.getResource("/static/images/SomeLogo.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(baos);
            // Initialize PDF document to be written to a stream not a file
            PdfDocument pdf = new PdfDocument(writer);
            // Document is the main object
            Document document = new Document(pdf);
            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            // add the image to the document
            Image img = new Image(ImageDataFactory.create(imageUrl))
                    .scaleAbsolute(125, 125)
                    .setFixedPosition(50, 710);
            document.add(img);
            // now let's add a big heading
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph(String.format("Some Heading"))
                    .setFont(font)
                    .setFontSize(24)
                    .setMarginRight(75)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBold());
            // then a smaller heading
            document.add(new Paragraph("sub heading ")
                    .setFont(font)
                    .setFontSize(16)
                    .setBold()
                    .setMarginRight(110)
                    .setMarginTop(-10)
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("\n\n"));
            // now a 3 column table
            Table table = new Table(3);
            table.setWidth(new UnitValue(UnitValue.PERCENT, 100));
            // Unfortunately we must format each cell individually :(
            // table headings
            Cell cell = new Cell().add(new Paragraph("table hd 1")
                    .setFont(font)
                    .setFontSize(12)
                    .setBold())
                    .setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);
            cell = new Cell().add(new Paragraph("table hd 2")
                    .setFont(font)
                    .setFontSize(12)
                    .setBold())
                    .setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);
            cell = new Cell().add(new Paragraph("table hd 3")
                    .setFont(font)
                    .setFontSize(12)
                    .setBold())
                    .setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);
            // table details
            cell = new Cell().add(new Paragraph("some")
                    .setFont(font)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.LEFT));
            table.addCell(cell);
            cell = new Cell().add(new Paragraph("important")
                    .setFont(font)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.LEFT));
            table.addCell(cell);
            cell = new Cell().add(new Paragraph("data")
                    .setFont(font)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.LEFT));
            table.addCell(cell);
            // table total
            cell = new Cell(1, 2).add(new Paragraph("Total:")
                    .setFont(font)
                    .setFontSize(12)
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT));
            table.addCell(cell);
            cell = new Cell().add(new Paragraph("$9,999.99")
                    .setFont(font)
                    .setFontSize(12)
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(ColorConstants.YELLOW));
            table.addCell(cell);
            document.add(table);
            document.add(new Paragraph("\n\n"));
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
            document.add(new Paragraph(dateFormatter.format(LocalDateTime.now()))
                    .setTextAlignment(TextAlignment.CENTER));
            document.close();
        } catch (Exception ex) {
            Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
        }
        // finally send stream back to the controller
        return new ByteArrayInputStream(baos.toByteArray());

    }
}
