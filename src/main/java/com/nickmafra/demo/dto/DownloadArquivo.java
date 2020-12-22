package com.nickmafra.demo.dto;

import lombok.Data;
import org.springframework.http.HttpHeaders;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Data
public class DownloadArquivo {

    public enum Disposition {
        ATTACHMENT, INLINE
    }

    private final HttpServletResponse response;
    private final OutputStream out;
    private Disposition disposition;
    private String fileName;
    private String contentType;

    public DownloadArquivo(HttpServletResponse response) throws IOException {
        this.response = response;
        this.out = response.getOutputStream();
    }

    public void setHeaders() {
        if (disposition == null) {
            disposition = DownloadArquivo.Disposition.ATTACHMENT;
        }
        String contentDisposition = disposition.name().toLowerCase();
        if (fileName != null) {
            contentDisposition += ";filename=\"" + fileName + "\"";
        }

        String exposeHeaders = response.getHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS);
        if (exposeHeaders == null) {
            exposeHeaders = HttpHeaders.CONTENT_DISPOSITION;
        } else {
            exposeHeaders += "," + HttpHeaders.CONTENT_DISPOSITION;
        }

        if (contentType == null) {
            if (fileName != null) {
                contentType = new MimetypesFileTypeMap().getContentType(fileName);
            } else {
                contentType = "application/octet-stream";
            }
        }

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, exposeHeaders);
        response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
    }
}
