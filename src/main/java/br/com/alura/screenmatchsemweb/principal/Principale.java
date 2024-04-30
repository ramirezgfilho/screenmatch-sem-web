package br.com.alura.screenmatchsemweb.principal;

import br.com.alura.screenmatchsemweb.model.*;
import br.com.alura.screenmatchsemweb.repository.SerieRepository;
import br.com.alura.screenmatchsemweb.service.ConsumoApi;
import br.com.alura.screenmatchsemweb.service.ConverteDados;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

public class Principale {

    private final String ENDERECO = "https://www.omdbapi.com/?apikey=6f082e45&t=";
    private Scanner leitura = new Scanner(System.in);
    ConsumoApi consumoApi = new ConsumoApi();
    ConverteDados converteDados = new ConverteDados();
    private List<DadosSerie> listaDeSeries = new ArrayList<>();
    private SerieRepository repository;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca;


    public Principale(SerieRepository repository) {
        this.repository = repository;
    }


    public void exibemenu(){

        var opcao = -1;
        while (opcao != 0){
            var menu = """
                    \n
                    1 - Buscar séries.
                    2 - Buscar episódios de uma série.
                    3 - Listar séries buscadas.
                    4 - Buscar série por nome.
                    5 - Buscar série por nome do ator.
                    6 - Buscar 5 séries melhores avaliadas. 
                    7 - Buscar séries por categoria. 
                    8 - Buscar por número de séries e nota.
                    9 - Buscar por trecho do episódio. 
                    10 - Buscar top episódios por série.
                    11 - Buscar episódios a partir de uma data.
                    
                    0 - Sair
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao){
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscaEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorNomeAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscar3SeriesEavaliacao();
                    break;
                case 9:
                    buscaEpisodioPorTrecho();
                    break;
                case 10:
                    buscarTopEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpDepoisDeUmaData();
                    break;

                case 0:
                    break;
                default:
                    System.out.println("Opção inválida");
            }

        }

    }

    private void buscarEpDepoisDeUmaData() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite a partir de qual ano deseja buscar os episódios: ");
            var anoEps = leitura.nextInt();
            leitura.nextLine();
            List<Episodio> episodiosAno = repository.episodiosDepoisDeUmaData(serie, anoEps);
            episodiosAno.forEach(System.out::println);
        }
    }

    private void buscarTopEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(System.out::println);
        }
    }

    private void buscaEpisodioPorTrecho() {
        System.out.println("\nDigite o trecho no nome do episódio: ");
        var trechoEpisodio = leitura.nextLine();

        List<Episodio> episodios = repository.episodiosPorTrecho(trechoEpisodio);
        episodios.forEach(System.out::println);
    }

    private void buscar3SeriesEavaliacao() {
        System.out.println("Digite o numero de temporadas: ");
        var numeroTemp = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Digite a nota desejada: ");
        var notaDesejada = leitura.nextDouble();
        leitura.nextLine();
        List<Serie> top3Series = repository.seriesPorTemporadaEAvaliacao(numeroTemp,notaDesejada);
        top3Series.forEach(s -> System.out.println(s.getTitle() + "Avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("\nDigite a categoria para buscar: ");
        var categoria = leitura.nextLine();
        Categoria genre = Categoria.fromPortugues(categoria);
        List<Serie> seriesPorCategoria = repository.findByGenre(genre);
        System.out.println("\nSéries por categoria: ");
        seriesPorCategoria.forEach(s -> System.out.println(s.getTitle()));
    }

    private void buscarTop5Series() {
        List<Serie> seriesTop5 = repository.findTop5ByOrderByAvaliacaoDesc();
        seriesTop5.forEach(s -> System.out.println(s.getTitle() + " avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorNomeAtor() {
        System.out.println("\nDigite o nome do ator: ");
        var nomeDoAtor = leitura.nextLine();
        List<Serie> seriesEncontradas = repository.findByActorsContainingIgnoreCase(nomeDoAtor);
        System.out.println("Séries em que o " + nomeDoAtor + " trabalhou: ");
        seriesEncontradas.forEach(s -> System.out.println(s.getTitle() + " ,Avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorTitulo() {
        System.out.println("\nDigite o nome da série para buscar: ");
        var nome = leitura.nextLine();
        serieBusca = repository.findByTitleContainingIgnoreCase(nome);

        if (serieBusca.isPresent()){
            System.out.println(serieBusca);
        } else {
            System.out.println("Série não encontrada! ");
        }

    }

    private void listarSeriesBuscadas() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repository.save(serie);
        System.out.println(serie);
    }

    private DadosSerie getDadosSerie(){
        System.out.println("** Digite o nome da série para a busca: **");
        var nomeSerie = leitura.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+"));
        DadosSerie dados = converteDados.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscaEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha a série pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repository.findByTitleContainingIgnoreCase(nomeSerie);

        if(serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadaList = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obterDados(ENDERECO + serieEncontrada.getTitle().replace(" ", "+") + "&season=" + i);
                DadosTemporada dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);
                temporadaList.add(dadosTemporada);
            }
            temporadaList.forEach(System.out::println);

            List<Episodio> episodios = temporadaList.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada! ");
        }



    }
}




//        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadaList.get(i).episodios();
//            for (int j = 0; j < temporadaList.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).title());
//            }
//        }

//temporadaList.forEach(t -> t.episodios().forEach(e -> System.out.println(e.title())));



//FILTRANDO OS 10 EPISODIOS MAIS BEM AVALIADOS
//        System.out.println("\n EPS MAIS BEM AVALIADOS! \n");
//        List<DadosEpisodio> dadosEpisodios = temporadaList.stream().flatMap(t -> t.episodios().stream()).collect(Collectors.toList());
//        dadosEpisodios.stream()
//                .filter(e -> !e.imdbRating().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodio::imdbRating).reversed())
//                .limit(10)
//                .map(e -> e.title().toUpperCase())
//                .forEach(System.out::println);

//TRANSFORMANDO A LISTA EM UMA LISTA DA CLASSE EPISODIO
//List<Episodio> episodios = temporadaList.stream().flatMap(t -> t.episodios().stream().map(d -> new Episodio(t.numero(), d))).collect(Collectors.toList());

//episodios.forEach(System.out::println);


//ENCONTRAR O PRIMEIRO COM O VALOR QUE O USUARIO DIGITOU:
//        System.out.println("Digite um trecho do nome do episódio");
//        var trechoTitulo = leitura.nextLine();
//        Optional<Episodio> epBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//        if (epBuscado.isPresent()){
//            System.out.println("Ep encontrado: ");
//            System.out.println("Temporada: " + epBuscado.get().getTemporada());
//        } else {
//            System.out.println("Ep não encontrado");
//        }


//IMPRIMINDO A LISTA DE EPISODIO A PARTIR DE UM ANO INDICADO PELO USUÁRIO
//        System.out.println("\n A partir de que ano você deseja ver os episodios? \n ");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//        LocalDate dataBusca = LocalDate.of(ano, 1,1);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream().filter(e -> e.getReleased() != null && e.getReleased().isAfter(dataBusca)).forEach(e -> System.out.println(
//                "Temporada: " + e.getTemporada() + " - " +
//                "Episódio: " + e.getTitulo() + " - " +
//                "Data de lançamento: " + e.getReleased().format(formatter)));


//FAZ MEDIA DE AVALIACAO DAS TEMPORADAS DA SERIE
//        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
//                .filter(e -> e.getImdbRating() > 0.0)
//                .collect(Collectors.groupingBy(Episodio::getTemporada,
//                        Collectors.averagingDouble(Episodio::getImdbRating)));
//        System.out.println(avaliacaoPorTemporada);

//        DoubleSummaryStatistics est = episodios.stream()
//                .filter(e -> e.getImdbRating() > 0.0)
//                .collect(Collectors.summarizingDouble(Episodio::getImdbRating));
//        System.out.println("\nMédia: " + est.getAverage());
//        System.out.println("Melhor ep: " + est.getMax());
//        System.out.println("Pior ep: " + est.getMin());
//        System.out.println("Count: " + est.getCount());



