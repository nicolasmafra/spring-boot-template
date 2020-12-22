package com.nickmafra.demo.controller;

import com.nickmafra.demo.dto.DownloadArquivo;
import com.nickmafra.demo.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/usuarios")
    public void getUsuarios(String nomeLike, HttpServletResponse response) throws IOException {
        DownloadArquivo downloadArquivo = new DownloadArquivo(response);
        relatorioService.gerarRelatorioUsuarios(downloadArquivo, nomeLike);
    }

}
