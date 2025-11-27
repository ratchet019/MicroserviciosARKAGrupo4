package com.arka.ecommerce.mssales.service;

import com.arka.ecommerce.mssales.dto.AbandonedCartDto;
import com.arka.ecommerce.mssales.dto.SaleReportDto;
import com.arka.ecommerce.mssales.entity.SaleHeader;
import com.arka.ecommerce.mssales.repository.SaleHeaderRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SaleHeaderRepository repo;
    private final SaleService saleService;

    public Mono<SaleReportDto> weeklyReport() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(7);
        return repo.findBySaleDateBetween(start.atStartOfDay(), today.atTime(23, 59))
                .collectList()
                .map(list -> {
                    BigDecimal total = list.stream()
                            .map(SaleHeader::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new SaleReportDto(start, today, total, list.size());
                });
    }

    public Mono<byte[]> weeklyReportPdf() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(7);

        return repo.findBySaleDateBetween(start.atStartOfDay(), today.atTime(23, 59))
                .collectList()
                .map(list -> {
                    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                        Document document = new Document();
                        PdfWriter.getInstance(document, baos);
                        document.open();

                        // Título
                        document.add(new Paragraph("Reporte Semanal de Ventas"));
                        document.add(new Paragraph("Desde: " + start + "  Hasta: " + today));
                        document.add(new Paragraph("Total de ventas: " + list.size()));
                        document.add(new Paragraph(" "));

                        // Tabla con datos
                        PdfPTable table = new PdfPTable(4);
                        table.setWidthPercentage(100);
                        table.addCell("ID Venta");
                        table.addCell("Cliente");
                        table.addCell("Fecha");
                        table.addCell("Total");

                        BigDecimal total = BigDecimal.ZERO;
                        for (SaleHeader sale : list) {
                            table.addCell(String.valueOf(sale.getId()));
                            table.addCell(sale.getCustomerName());
                            table.addCell(sale.getSaleDate().toString());
                            table.addCell("$" + sale.getTotalAmount());
                            total = total.add(sale.getTotalAmount());
                        }

                        // Total general
                        PdfPCell cell = new PdfPCell(new Phrase("TOTAL GENERAL: $" + total));
                        cell.setColspan(4);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);

                        document.add(table);
                        document.close();

                        return baos.toByteArray();

                    } catch (Exception e) {
                        throw new RuntimeException("Error generando PDF", e);
                    }
                });
    }


    public Mono<byte[]> weeklyReportCsv() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(7);

        return repo.findBySaleDateBetween(start.atStartOfDay(), today.atTime(23,59))
                .collectList()
                .map(list -> {
                    StringBuilder csv = new StringBuilder();
                    csv.append("Fecha,Producto,Total\n");

                    list.forEach(sale -> csv.append(
                            sale.getSaleDate().toLocalDate() + "," +
                                    sale.getCustomerName() + "," +
                                    sale.getTotalAmount() + "\n"
                    ));

                    BigDecimal total = list.stream()
                            .map(SaleHeader::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    csv.append("\nResumen desde,").append(start)
                            .append(",hasta,").append(today)
                            .append("\nTotal de ventas:,").append(total)
                            .append("\nCantidad de ventas:,").append(list.size());

                    return csv.toString().getBytes(StandardCharsets.UTF_8);
                });
    }

    public Mono<byte[]> abandonedCartsCsv() {
        return saleService.findAbandonedCarts()
                .map(carts -> {
                    StringBuilder csv = new StringBuilder("OrderId,CustomerName,CreatedAt,Total\n");
                    for (AbandonedCartDto cart : carts) {
                        csv.append(cart.getOrderId()).append(",")
                                .append(cart.getCustomerName()).append(",")
                                .append(cart.getOrderDate()).append(",")
                                .append(cart.getTotalAmount()).append("\n");
                    }
                    return csv.toString().getBytes(StandardCharsets.UTF_8);
                });
    }
}
