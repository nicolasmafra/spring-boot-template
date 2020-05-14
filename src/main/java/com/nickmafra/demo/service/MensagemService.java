package com.nickmafra.demo.service;

import com.nickmafra.demo.dto.ConsultaDto;
import com.nickmafra.demo.dto.MensagemDto;
import com.nickmafra.demo.dto.PaginaDto;
import com.nickmafra.demo.infra.jms.EnviadorFila;
import com.nickmafra.demo.model.Mensagem;
import com.nickmafra.demo.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;
    @Autowired
    private EnviadorFila enviadorFila;

    public PaginaDto<MensagemDto> listar(ConsultaDto consulta) {
        Page<Mensagem> page = mensagemRepository.findAll(consulta.toPageable());
        return new PaginaDto<>(page).map(MensagemDto::new);
    }

    public void enviar(MensagemDto mensagem) {
        enviadorFila.enviarMensagem(mensagem);
    }

    public void salvar(MensagemDto mensagemDto) {
        mensagemRepository.save(mensagemDto.toMensagem());
    }
}
