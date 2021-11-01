package com.mom.billdesk.getbill;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import static com.mom.billdesk.getbill.GlobalConstants.*;

import java.io.ByteArrayOutputStream;

public class MakeTablePdf {



    //    public static ByteArrayOutputStream getPdfFile(FileInputParams fileParams) {
//
//        Document document = new Document();
//        ByteArrayOutputStream bout = new ByteArrayOutputStream();
//        try{
//            Paragraph p = new Paragraph(fileParams.date);
//
//            PdfWriter.getInstance(document, bout);
//            document.open();
//            document.add(p);
//            document.close();
//            System.out.println("File created");
//        }
//        catch (Exception e){
//            System.out.println("File Failed"+e);
//        }
//        return bout;
//    }
    public static ByteArrayOutputStream getPdfFile(FileInputParams fileParams) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(doc, bout);
            doc.setMargins(36, 36, 55, 36);
            doc.open();
            Paragraph breakLine = new Paragraph("\n\n");


            doc = addHeading(doc);
            doc.add(breakLine);

            doc = addLogo(doc);
            doc.add(breakLine);

            doc = addDateBillNo(doc, fileParams.date, fileParams.billNo);
            doc.add(breakLine);

            doc = addInfo(doc, fileParams.custDetails);
            doc.add(breakLine);

            doc = addTable(doc, fileParams.billItems, fileParams.billDetails);
            doc.add(breakLine);

            doc = addDisclaimer(doc);
            doc.add(breakLine);

            doc.close();

        } catch (Exception ex) {
            System.out.println("Error while printing " + ex);
        }

        return bout;
    }



    private static Document addLogo(Document doc) {
        String logo = "https://misattestation.com/wp-content/uploads/logo-momentum-international-services.png";
        try {
            Image img = Image.getInstance(logo);
            img.scaleToFit(50f, 50f);
            img.setAlignment(Element.ALIGN_MIDDLE);
            doc.add(img);
        } catch (Exception ex) {
            System.out.println("Error adding logo"+ex);
        }
        return doc;
    }

    private static Document addHeading(Document doc){
        Paragraph para = new Paragraph(PDFHEADING);
        para.setAlignment(Element.ALIGN_CENTER);
        try {
            doc.add(para);
        } catch (Exception ex) {
            //Logger.getLogger(MakeTableBill.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error adding heading"+ex);
        }
        return doc;
    }

    private static Document addDateBillNo(Document doc, String billDate, String billNo) {

        Paragraph para = new Paragraph("Date: " + billDate + "\nBillNo:" + billNo);
        para.setAlignment(Element.ALIGN_RIGHT);
        try {
            doc.add(para);
        } catch (Exception ex) {
            //Logger.getLogger(MakeTableBill.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error adding date"+ex);
        }

        return doc;
    }


    private static Document addInfo(Document doc, CustomerInfo custDetails) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        String custInfo = custDetails.custName +",\n"+custDetails.custAdd1+",\n"+custDetails.custAdd2+",\n"+custDetails.custAdd3;
        table.addCell(getCell(custInfo, Element.ALIGN_LEFT));
        table.addCell(getCell(COMPANYDETAILS, Element.ALIGN_RIGHT));

        try {
            doc.add(table);
        } catch (Exception ex) {
            //Logger.getLogger(MakeTableBill.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error adding customer info"+ex);
        }
        return doc;
    }

    private static PdfPCell getCell(String txt, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(txt));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private static Document addTable(Document doc, BillItems[] billItems, BillInfo billInfo) throws DocumentException {
        Font whiteFont = new Font();
        whiteFont.setColor(BaseColor.WHITE);


        PdfPTable pdfTable = new PdfPTable(ITEMTABLEHEADERS.length);

        for (int i = 0; i < ITEMTABLEHEADERS.length; i++) {
            PdfPCell headerCell = new PdfPCell(new Phrase(ITEMTABLEHEADERS[i], whiteFont));
            headerCell.setBorder(2);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.enableBorderSide(Rectangle.LEFT);
            headerCell.enableBorderSide(Rectangle.RIGHT);
            headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
            pdfTable.addCell(headerCell);
        }


        for(BillItems item:billItems){
            PdfPCell cell1 = new PdfPCell(new Phrase(item.docHolder));
            cell1.setBorder(2);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.enableBorderSide(Rectangle.LEFT);
            cell1.enableBorderSide(Rectangle.RIGHT);
            pdfTable.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase(item.process));
            cell2.setBorder(2);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.enableBorderSide(Rectangle.LEFT);
            cell2.enableBorderSide(Rectangle.RIGHT);
            pdfTable.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Phrase(item.amount));
            cell3.setBorder(2);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.enableBorderSide(Rectangle.LEFT);
            cell3.enableBorderSide(Rectangle.RIGHT);
            pdfTable.addCell(cell3);
        }


        //adding sum
        String labelForTotal = (billInfo.isTaxEnabled || billInfo.isDiscountEnabled) ? "Sub Total" : "Gross Total";
        PdfPCell totCell = new PdfPCell(new Phrase(labelForTotal));
        totCell.setBorder(2);
        totCell.setColspan(2);
        totCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totCell.enableBorderSide(Rectangle.LEFT);
        totCell.enableBorderSide(Rectangle.RIGHT);
        pdfTable.addCell(totCell);

        PdfPCell amtCell = new PdfPCell(new Phrase(""+ billInfo.amount));
        amtCell.setBorder(2);
        amtCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        amtCell.enableBorderSide(Rectangle.LEFT);
        amtCell.enableBorderSide(Rectangle.RIGHT);
        pdfTable.addCell(amtCell);


        //adding taxes
        if(billInfo.isTaxEnabled){
            PdfPCell taxCell = new PdfPCell(new Phrase("Taxes"));
            taxCell.setBorder(2);
            taxCell.setColspan(2);
            taxCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            taxCell.enableBorderSide(Rectangle.LEFT);
            taxCell.enableBorderSide(Rectangle.RIGHT);
            pdfTable.addCell(taxCell);

            PdfPCell taxPerCell = new PdfPCell(new Phrase(billInfo.taxes+"%"));
            taxPerCell.setBorder(2);
            taxPerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            taxPerCell.enableBorderSide(Rectangle.LEFT);
            taxPerCell.enableBorderSide(Rectangle.RIGHT);
            pdfTable.addCell(taxPerCell);
        }


        //adding discount
        if(billInfo.isDiscountEnabled){
            PdfPCell discCell = new PdfPCell(new Phrase("Discount"));
            discCell.setBorder(2);
            discCell.setColspan(2);
            discCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            discCell.enableBorderSide(Rectangle.LEFT);
            discCell.enableBorderSide(Rectangle.RIGHT);
            pdfTable.addCell(discCell);

            PdfPCell discPerCell = new PdfPCell(new Phrase(billInfo.discount+"%"));
            discPerCell.setBorder(2);
            discPerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            discPerCell.enableBorderSide(Rectangle.LEFT);
            discPerCell.enableBorderSide(Rectangle.RIGHT);
            pdfTable.addCell(discPerCell);
        }

        //adding calculated total
        if(billInfo.isTaxEnabled || billInfo.isDiscountEnabled){
            PdfPCell totCalCell = new PdfPCell(new Phrase("Gross Total"));
            totCalCell.setBorder(2);
            totCalCell.setColspan(2);
            totCalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totCalCell.enableBorderSide(Rectangle.LEFT);
            totCalCell.enableBorderSide(Rectangle.RIGHT);
            pdfTable.addCell(totCalCell);

            PdfPCell amtCalCell = new PdfPCell(new Phrase(billInfo.total));
            amtCalCell.setBorder(2);
            amtCalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            amtCalCell.enableBorderSide(Rectangle.LEFT);
            amtCalCell.enableBorderSide(Rectangle.RIGHT);
            pdfTable.addCell(amtCalCell);
        }


        doc.add(pdfTable);
        return doc;
    }

    private static Document addDisclaimer(Document doc) {
        Paragraph para = new Paragraph(DISCLAIMER);
        para.setAlignment(Element.ALIGN_JUSTIFIED);
        Font font = new Font(Font.FontFamily.TIMES_ROMAN,10.0f, Font.NORMAL, BaseColor.BLACK);
        para.setFont(font);
        try{
            doc.add(para);
        }
        catch(Exception ex){
            System.out.println("Error adding disclaimer"+ex);
        }
        return doc;
    }

}
