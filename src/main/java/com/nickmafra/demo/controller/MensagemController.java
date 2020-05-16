package com.nickmafra.demo.controller;

import com.nickmafra.demo.dto.ConsultaDto;
import com.nickmafra.demo.dto.MensagemDto;
import com.nickmafra.demo.dto.PaginaDto;
import com.nickmafra.demo.model.Mensagem;
import com.nickmafra.demo.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/mensagens")
public class MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @GetMapping
    public PaginaDto<MensagemDto> getAll(ConsultaDto consultaDto) {
        Pageable pageable = consultaDto.toPageable();
        Page<Mensagem> page = mensagemService.listar(pageable);
        return new PaginaDto<>(page).map(MensagemDto::new);
    }

    @PostMapping
    public void post(@Valid @RequestBody MensagemDto mensagemDto) {
        Mensagem mensagem = mensagemDto.toMensagem();
        mensagemService.enviar(mensagem);
    }
}
