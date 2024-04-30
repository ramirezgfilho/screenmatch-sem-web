package br.com.alura.screenmatchsemweb.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Entity
@Table(name="episodios")
public class Episodio {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer temporada;
    private String titulo;
    private Integer numero;
    private Double imdbRating;
    private LocalDate released;
    @ManyToOne
    private Serie serie;

    public Episodio(){}

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodio.title();
        this.numero = dadosEpisodio.number();

        try {
            this.imdbRating = Double.valueOf(dadosEpisodio.imdbRating());
        } catch (NumberFormatException ex){
            this.imdbRating = 0.0;
        }

        try {
            this.released = LocalDate.parse(dadosEpisodio.released());

        }catch (DateTimeParseException date){
            this.released = LocalDate.of(2024,01,01);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public LocalDate getReleased() {
        return released;
    }

    public void setReleased(LocalDate released) {
        this.released = released;
    }

    @Override
    public String toString() {
        return  "Título: " + titulo +
                ", Temporada: " + temporada +
                ", Número: " + numero +
                ", Avaliação: " + imdbRating +
                ", Lançamento: " + released;
    }
}
