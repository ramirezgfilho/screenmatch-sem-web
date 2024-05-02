package br.com.alura.screenmatchsemweb.service;

import br.com.alura.screenmatchsemweb.dto.EpisodioDTO;
import br.com.alura.screenmatchsemweb.dto.SerieDTO;
import br.com.alura.screenmatchsemweb.model.Categoria;
import br.com.alura.screenmatchsemweb.model.Serie;
import br.com.alura.screenmatchsemweb.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obterTodasAsSeries(){
        return converteDados(repository.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repository.lancamentosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = repository.findById(id);
        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(),s.getTitle(),s.getTotalTemporadas(),s.getAvaliacao(),s.getActors(),s.getPlot(),s.getPoster(),s.getGenre());
        }
        return null;
    }

    public List<EpisodioDTO> obterTodasAsTemporadas(Long id){
        Optional<Serie> serie = repository.findById(id);
        if (serie.isPresent()){
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumero()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
        return repository.obterEpisodiosPorTemporada(id, numero).stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumero()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterSeriesPorCategoria(String categoria) {
        Categoria categoria1 = Categoria.fromPortugues(categoria);
        return converteDados(repository.findByGenre(categoria1));
    }


    private List<SerieDTO> converteDados(List<Serie> series){
        return  series.stream()
                .map(s -> new SerieDTO(s.getId(),s.getTitle(),s.getTotalTemporadas(),s.getAvaliacao(),s.getActors(),s.getPlot(),s.getPoster(),s.getGenre()))
                .collect(Collectors.toList());
    }



}
