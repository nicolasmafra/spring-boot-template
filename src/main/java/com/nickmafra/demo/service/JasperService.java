package com.nickmafra.demo.service;

import com.nickmafra.demo.enums.FormatoRelatorio;
import com.nickmafra.demo.infra.exception.AppRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Slf4j
@Service
public class JasperService {

    private static final String AUTHOR = "spring-boot-template";

    @Autowired
    private ResourceLoader resourceLoader;

    private JasperReport load(String jasperName) {
        log.info("Gerando relatório do modelo {}", jasperName);
        InputStream in;
        try {
            in = resourceLoader.getResource("classpath:jasper/" + jasperName + ".jasper").getInputStream();
        } catch (IOException e) {
            throw new AppRuntimeException("Erro ao carregar arquivo modelo de relatório.", e);
        }
        try {
            return (JasperReport) JRLoader.loadObject(in);
        } catch (JRException e) {
            throw new AppRuntimeException("Erro ao ler modelo do jasper.", e);
        }
    }

    private JasperPrint fill(JasperReport report, Map<String, Object> params,  Collection<?> dados) {
        try {
            JRDataSource dataSource = new JRBeanCollectionDataSource(dados);
            return JasperFillManager.fillReport(report, params, dataSource);
        } catch (JRException e) {
            throw new AppRuntimeException("Erro ao preencher dados no relatório.", e);
        }
    }

    public JasperPrint loadAndFill(String jasperName, Collection<?> dados) {
        return fill(load(jasperName), new HashMap<>(), dados);
    }

    public void exportToPdf(List<JasperPrint> jasperPrints, OutputStream out) {
        JRPdfExporter exporter = new JRPdfExporter();

        exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrints));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

        SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor(AUTHOR);

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);
        try {
            exporter.exportReport();
        } catch (JRException e) {
            throw new AppRuntimeException("Erro ao gerar PDF.", e);
        }
    }

    public void exportToXlsx(List<JasperPrint> jasperPrints, OutputStream out) {
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrints));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setWhitePageBackground(false);
        configuration.setShowGridLines(true);
        configuration.setOnePagePerSheet(false);
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        configuration.setRemoveEmptySpaceBetweenRows(true);
        exporter.setConfiguration(configuration);
        try {

            exporter.exportReport();

        } catch (JRException e) {
            throw new AppRuntimeException("Erro ao gerar XLSX.", e);
        }
    }

    public void export(String jasperName, Collection<?> dados, FormatoRelatorio formatoRelatorio, OutputStream out) {
        List<JasperPrint> jasperPrints = Collections.singletonList(loadAndFill(jasperName, dados));

        if (formatoRelatorio == FormatoRelatorio.PDF) {
            exportToPdf(jasperPrints, out);
        } else if (formatoRelatorio == FormatoRelatorio.XLSX) {
            exportToXlsx(jasperPrints, out);
        }
    }
}
