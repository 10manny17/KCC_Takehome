package com.kcctakehome.backend.service;

import com.itextpdf.layout.properties.TextAlignment;
import com.kcctakehome.backend.model.HurricaneEventModel;
import com.kcctakehome.backend.model.HurricaneRecordModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@Service
public class GenerateReportService {


    @Autowired
    public GenerateReportService() {
    }

    public byte[] buildReport(List<HurricaneEventModel> events) throws IOException {
        HurricaneService getHurricaneInfo = new HurricaneService();
        try (ByteArrayOutputStream generateReportStream = new ByteArrayOutputStream()) {
            PdfWriter generateReportWriter = new PdfWriter(generateReportStream);
            PdfDocument reportPDF = new PdfDocument(generateReportWriter);
            Document reportDocument = new Document(reportPDF);
            reportDocument.add(new Paragraph("Karen Clark & Company: Hurricane Report").setBold().setFontSize(14));
            reportDocument.add(new Paragraph("Desc: Hurricanes 1990-Now in Florida"));
            reportDocument.add(new Paragraph("Prepared by: Emmanuel C. "));

            Table dataTable = new Table(new float[]{3, 3, 3}).useAllAvailableWidth();
            dataTable.addHeaderCell("Hurricane Name").setBold().setTextAlignment(TextAlignment.CENTER);
            dataTable.addHeaderCell("Landfall Date").setBold().setTextAlignment(TextAlignment.CENTER);
            dataTable.addHeaderCell("MAX Wind Speed").setBold().setTextAlignment(TextAlignment.CENTER);

            for (HurricaneEventModel hurricaneEvent : events) {
                dataTable.addCell(hurricaneEvent.getHurricaneName()).setTextAlignment(TextAlignment.CENTER);
                HurricaneRecordModel maxSpeedEvent = getHurricaneInfo.getMaxSpeed(hurricaneEvent.getHurricaneRecords());
                String eventDate = (maxSpeedEvent != null) ? getHurricaneInfo.parseHurricaneRecordDate(maxSpeedEvent.getHurricaneRecordDate()) : "N/A";
                dataTable.addCell(eventDate).setTextAlignment(TextAlignment.CENTER);
                int maxSpeed = (maxSpeedEvent != null) ? maxSpeedEvent.getHurricaneRecordWindSpeed() : 0;
                dataTable.addCell(Integer.toString(maxSpeed)).setTextAlignment(TextAlignment.CENTER);
            }

            reportDocument.add(dataTable);
            reportDocument.close();
            return generateReportStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating report: " + e.getMessage(), e);
        }
    }

}
