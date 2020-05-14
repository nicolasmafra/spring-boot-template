package com.nickmafra.demo.controller;

import com.nickmafra.demo.dto.ConsultaDto;
import com.nickmafra.demo.dto.MensagemDto;
import com.nickmafra.demo.dto.PaginaDto;
import com.nickmafra.demo.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/mensagens")
public class MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @GetMapping
    public PaginaDto<MensagemDto> getAll(ConsultaDto consultaDto) {
        return mensagemService.listar(consultaDto);
    }

    @PostMapping
    public void post(@Valid @RequestBody MensagemDto mensagemDto) {
        mensagemService.enviar(mensagemDto);
    }
}
