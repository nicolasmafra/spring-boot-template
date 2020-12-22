package com.nickmafra.demo.service;

import com.nickmafra.demo.dto.DownloadArquivo;
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

    public void gerarRelatorioUsuarios(DownloadArquivo downloadArquivo, String nomeLike) {
        UsuarioConsultaDto consulta = new UsuarioConsultaDto();
        consulta.setToFullResult();
        consulta.setNome(nomeLike);

        List<UsuarioDto> dados = usuarioService.listarDto(consulta).getConteudo();

        downloadArquivo.setFileName("RelatorioTeste.pdf");
        downloadArquivo.setHeaders();
        jasperService.exportToPdf("teste", dados, downloadArquivo.getOut());

    }

}
