package com.nickmafra.demo.service;

import com.nickmafra.demo.infra.exception.AppRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    private JasperPrint loadAndFill(String jasperName, Collection<?> dados) {
        return fill(load(jasperName), new HashMap<>(), dados);
    }

    private byte[] exportToPdf(JasperPrint jasperPrint) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
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

        return out.toByteArray();
    }

    public byte[] exportToPdf(String jasperName, Collection<?> dados) {
        return exportToPdf(loadAndFill(jasperName, dados));
    }
}
