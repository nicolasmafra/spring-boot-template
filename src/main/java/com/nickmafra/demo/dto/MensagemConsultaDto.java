package com.nickmafra.demo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.function.Predicate;

import static com.nickmafra.demo.specification.GenericSpec.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class MensagemConsultaDto extends ConsultaDto {

    private Long id;
    private String titulo;
    private LocalDateTime dataEnvioMin;
    private LocalDateTime dataEnvioMax;

    public Predicate<MensagemDto> toPredicate() {
        return and(
                isEqualTo(MensagemDto::getId, id),
                like(MensagemDto::getTitulo, titulo),
                between(MensagemDto::getDataEnvio, dataEnvioMin, dataEnvioMax)
        );
    }
}
