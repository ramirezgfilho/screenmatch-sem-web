package br.com.alura.screenmatchsemweb.model;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name="series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;
    private Integer totalTemporadas;
    private Double avaliacao;
    private String actors;
    private String plot;
    private String poster;
    @Enumerated(EnumType.STRING)
    private Categoria genre;

    @OneToMany(mappedBy = "serie" ,cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    public Serie(DadosSerie dadosSerie){
        this.title = dadosSerie.title();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        this.actors = dadosSerie.actors();
        this.plot = dadosSerie.plot(); //ConsultaChatGPT.obterTraducao(dadosSerie.plot()) ;
        this.poster = dadosSerie.poster();
        this.genre = Categoria.fromString(dadosSerie.genre().split(",")[0].trim());
    }

    public Serie() {}


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Categoria getGenre() {
        return genre;
    }

    public void setGenre(Categoria genre) {
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "\n Dados da série:" +
                "\n Nome: " + title +
                "\n Genêro: " + genre +
                "\n Total de temporadas: " + totalTemporadas +
                "\n Avaliação: " + avaliacao +
                "\n Atores principais: " + actors +
                "\n Sinopse: " + plot +
                "\n Episódios: " + episodios +
                "\n Poster: " + poster;
    }

}
