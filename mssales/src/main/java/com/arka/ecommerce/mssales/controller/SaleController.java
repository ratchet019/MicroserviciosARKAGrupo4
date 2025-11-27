package com.arka.ecommerce.mssales.controller;

import com.arka.ecommerce.mssales.dto.AbandonedCartDto;
import com.arka.ecommerce.mssales.dto.SaleDto;
import com.arka.ecommerce.mssales.dto.SaleReportDto;
import com.arka.ecommerce.mssales.dto.SaleRequest;
import com.arka.ecommerce.mssales.service.ReportService;
import com.arka.ecommerce.mssales.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;
    private final ReportService reportService;

    @PostMapping
    public Mono<ResponseEntity<SaleDto>> create(@RequestBody SaleRequest request) {
        return saleService.registerSale(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/report/weekly")
    public Mono<ResponseEntity<SaleReportDto>> weeklyReport() {
        return reportService.weeklyReport()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/report/weekly/pdf")
    public Mono<ResponseEntity<byte[]>> weeklyReportPdf() {
        return reportService.weeklyReportPdf()
                .map(bytes -> ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=weekly-report.pdf")
                        .header("Content-Type", "application/pdf")
                        .body(bytes));
    }


    @GetMapping(value = "/report/weekly/csv", produces = "text/csv")
    public Mono<ResponseEntity<byte[]>> weeklyReportCsv() {
        return reportService.weeklyReportCsv()
                .map(csv -> ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=weekly-report.csv")
                        .body(csv)
                );
    }

    @GetMapping("/abandoned-carts")
    public Mono<ResponseEntity<List<AbandonedCartDto>>> getAbandonedCarts() {
        return saleService.findAbandonedCarts()
                .map(ResponseEntity::ok);
    }

    @GetMapping(value = "/report/abandoned-carts/csv", produces = "text/csv")
    public Mono<ResponseEntity<byte[]>> abandonedCartsCsv() {
        return reportService.abandonedCartsCsv()
                .map(csv -> ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=abandoned-carts.csv")
                        .body(csv)
                );
    }

}

