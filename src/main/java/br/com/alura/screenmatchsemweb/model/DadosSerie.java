package br.com.alura.screenmatchsemweb.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(
        @JsonAlias("Title") String title,
        @JsonAlias("totalSeasons") Integer totalTemporadas,
        @JsonAlias("imdbRating") String avaliacao,
        @JsonAlias("Actors") String actors,
        @JsonAlias("Plot") String plot,
        @JsonAlias("Poster") String poster,
        @JsonAlias("Genre") String genre
)

{
    @Override
    public String toString() {
        return "Dados da série:" +
                "\n Nome: " + title +
                "\n Genêro: " + genre +
                "\n Total de temporadas: " + totalTemporadas +
                "\n Avaliação: " + avaliacao +
                "\n Atores principais: " + actors +
                "\n Sinopse: " + plot +
                "\n Poster: " + poster;
    }
}
