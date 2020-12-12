package com.nickmafra.demo.controller;

import com.nickmafra.demo.dto.ConsultaDto;
import com.nickmafra.demo.dto.MensagemConsultaDto;
import com.nickmafra.demo.dto.MensagemDto;
import com.nickmafra.demo.dto.PaginaDto;
import com.nickmafra.demo.model.Mensagem;
import com.nickmafra.demo.service.MensagemService;
import com.nickmafra.demo.util.MockUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

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


    // Exemplo de como mockar quando um serviço ainda não está desenvolvido.


    private final Integer[] horasAtras = new Integer[] { 1, 2, 3, 4 };
    private final String[] titulos = new String[] { "Teste A", "Teste B", "Teste C" };
    private final String[] conteudos = new String[] { "Bom dia", "Boa tarde", "Boa noite" };

    private MensagemDto mensagemMockada(int n) {
        return MensagemDto.builder()
                .id((long) n) // para ver se o mock funciona
                .dataEnvio(LocalDateTime.now().minusHours(MockUtils.randomize(n, horasAtras)))
                .titulo(MockUtils.randomize(n, titulos))
                .conteudo(MockUtils.randomize(n, conteudos))
                .build();
    }

    @GetMapping("/mock")
    public PaginaDto<MensagemDto> getAllMock(MensagemConsultaDto consultaDto) {
        List<MensagemDto> itens = MockUtils.gerarItens(30, this::mensagemMockada);
        itens = MockUtils.filtrar(consultaDto.toPredicate(), itens);
        itens = MockUtils.ordenar(consultaDto, itens);
        return MockUtils.pegarPagina(consultaDto, itens);
    }
}
