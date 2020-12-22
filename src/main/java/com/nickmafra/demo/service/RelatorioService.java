package com.nickmafra.demo.service;

import com.nickmafra.demo.dto.DownloadArquivo;
import com.nickmafra.demo.dto.FiltroRelatorioUsuarioDto;
import com.nickmafra.demo.dto.UsuarioConsultaDto;
import com.nickmafra.demo.dto.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private JasperService jasperService;

    @Autowired
    private UsuarioService usuarioService;

    public void gerarRelatorioUsuarios(DownloadArquivo downloadArquivo, FiltroRelatorioUsuarioDto filtro) {
        UsuarioConsultaDto consulta = new UsuarioConsultaDto();
        consulta.setToFullResult();
        consulta.setNome(filtro.getNomeLike());

        List<UsuarioDto> dados = usuarioService.listarDto(consulta).getConteudo();

        String jasperName = "teste_" + filtro.getFormato().getExtensao();
        downloadArquivo.setFileName(filtro.getFormato().comExtensao("Relatório teste"));
        downloadArquivo.setHeaders();

        jasperService.export(jasperName, dados, filtro.getFormato(), downloadArquivo.getOut());
    }

}
