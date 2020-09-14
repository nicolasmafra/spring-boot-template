package com.nickmafra.demo.controller;

import com.nickmafra.demo.dto.ArquivoDto;
import com.nickmafra.demo.service.RelatorioService;
import com.nickmafra.demo.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/usuarios")
    public ResponseEntity<byte[]> getUsuarios(String nomeLike) {
        ArquivoDto arquivo = relatorioService.gerarRelatorioUsuarios(nomeLike);
        return ControllerUtils.toResponse(arquivo);
    }

}
