package br.com.alura.screenmatchsemweb.dto;

import br.com.alura.screenmatchsemweb.model.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record SerieDTO(
        Long id,
        String title,
        Integer totalTemporadas,
        Double avaliacao,
        String actors,
        String plot,
        String poster,
        Categoria genre){
}
