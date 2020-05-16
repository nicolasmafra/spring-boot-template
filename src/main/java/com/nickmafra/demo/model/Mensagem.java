package com.nickmafra.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Mensagem {

    @GeneratedValue
    @Id
    private Long id;
    private String titulo;
    private String conteudo;
    private LocalDateTime dataEnvio;
    private LocalDateTime dataRecebimento;

}
