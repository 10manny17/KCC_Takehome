package com.kcctakehome.backend.controller;

import com.kcctakehome.backend.model.HurricaneEventModel;
import com.kcctakehome.backend.model.HurricaneRecordModel;
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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/fetchData")
    public List<HurricaneEventModel> fetchData() {
        return hurricaneService.fetchData();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/fetchfilterdData")
    public List<HurricaneEventModel> fetchfilterdData() {
        return hurricaneService.fetchFilteredByYearByLandfallByLocation();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/displayData")
    public List<HurricaneEventModel> displayData() {
        return hurricaneService.fetchFilteredByYearByLandfallByLocation();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/downloadReport")
    public ResponseEntity<byte[]> downloadReport() throws IOException {
        byte[] reportPdf = generateReportService.buildReport(hurricaneService.fetchFilteredByYearByLandfallByLocation());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=HurricaneReport.pdf").contentType(MediaType.APPLICATION_PDF).body(reportPdf);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/viewReport")
    public ResponseEntity<byte[]> viewReport() throws IOException {
        byte[] reportPdf = generateReportService.buildReport(hurricaneService.fetchFilteredByYearByLandfallByLocation());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=HurricaneReport.pdf").contentType(MediaType.APPLICATION_PDF).body(reportPdf);
    }

}