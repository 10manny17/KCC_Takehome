package com.kcctakehome.backend.controller;

import com.kcctakehome.backend.model.HurricaneEventModel;
import com.kcctakehome.backend.service.GenerateReportService;
import com.kcctakehome.backend.service.HurricaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * HurricaneController.java
 * <p>
 * This class handles HTTP requests related to Hurricane operations.(Including generating a report)
 * It provides RESTful endpoints for fetching hurricane data.
 *
 * @author Emmanuel Chalumeau
 * @version 1.0
 * @since 2025-02-05
 */

@RestController
@RequestMapping("/api")
public class HurricaneController {

    private final HurricaneService hurricaneService;

    private final GenerateReportService generateReportService;

    @Autowired
    public HurricaneController(HurricaneService hurricaneService, GenerateReportService generateReportService) {
        this.hurricaneService = hurricaneService;
        this.generateReportService = generateReportService;
    }

    /**
     * Gets hurricane events
     *
     * @return List of Hurricanes
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/fetchfilterdData")
    public List<HurricaneEventModel> fetchfilterdData() {
        return hurricaneService.fetchFilteredByYearByLandfallByLocation();
    }

    /**
     * Generates and returns a PDF report of filtered hurricane data.
     * The response is set to allow user to download the PDF attachment.
     *
     * @return ResponseEntity containing the PDF file as a byte array.
     * @throws IOException if an error occurs during report generation.
     */

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/downloadReport")
    public ResponseEntity<byte[]> downloadReport() throws IOException {
        byte[] reportPdf = generateReportService.buildReport(hurricaneService.fetchFilteredByYearByLandfallByLocation());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=HurricaneReport.pdf").contentType(MediaType.APPLICATION_PDF).body(reportPdf);
    }

    /**
     * Generates and returns a PDF report of filtered hurricane data.
     * The response is set to display the PDF inline in the browser.
     *
     * @return ResponseEntity containing the PDF file as a byte array.
     * @throws IOException if an error occurs during report generation.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/viewReport")
    public ResponseEntity<byte[]> viewReport() throws IOException {
        byte[] reportPdf = generateReportService.buildReport(hurricaneService.fetchFilteredByYearByLandfallByLocation());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=HurricaneReport.pdf").contentType(MediaType.APPLICATION_PDF).body(reportPdf);
    }

}