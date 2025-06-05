package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    Dotenv dotenv = Dotenv.load();
    String omdbApiKey = dotenv.get("OMDB_API_KEY");
    private final String API_KEY = "&apikey=" + omdbApiKey;
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repository;
    private List<Serie> series;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {

        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    \n############################
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listas de séries
                    4 - Buscar séries por Titulo
                    5 - buscar séries por ator
                    6 - Top 5 séries
                    7 - Buscar série por categoria
                    8 - Listar séries por temporadas e avaliação
                    
                    0 - Sair   
                    ############################                              
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSerieporAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriePorCategoria();
                    break;
                case 8:
                    listarPorTemporadaEAvalicao();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("\nEscolha uma serie para busca");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void listarSeriesBuscadas() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("\nEscolha uma serie para busca");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);
        if (serieBuscada.isPresent()) {
            System.out.println("\nDados da série: " + serieBuscada.get());
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void buscarSerieporAtor() {
        System.out.println("Qual o nome do ator para busca");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avalições minimas do ator: " + nomeAtor);
        var avaliacao = leitura.nextLine();
        List<Serie> serieEncontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, Double.valueOf(avaliacao));
        System.out.println("\nSéries em que " + nomeAtor + " trabalhous!");
        serieEncontradas.forEach(s -> System.out.println("Série: " + s.getTitulo() + ", avaliação: " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> serieTop = repository.findTop5ByOrderByAvaliacaoDesc();
        serieTop.stream()
                .sorted(Comparator.comparingDouble(Serie::getAvaliacao).reversed())
                .forEach(e -> System.out.println("Serie: " + e.getTitulo() + ", avaliacao: " + e.getAvaliacao()));
    }

    private void buscarSeriePorCategoria() {
        System.out.println("\nDeseja buscar serie por categoria/genero: ");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPotugues(nomeGenero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        System.out.println("Séries da categoria " + categoria);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void listarPorTemporadaEAvalicao() {
        System.out.println("\nQual a quantidade máxima de temporadas: ");
        var numTemporada = leitura.nextLine();
        System.out.println("Qual é a avaliação minima da séria:");
        var avaliacao = leitura.nextLine();
        List<Serie> seriesPorTemporada = repository.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer.valueOf(numTemporada), Double.valueOf(avaliacao));
        List<Serie> seriesPorTemporada2 = repository.findByTotalTemporadasLessThanEqualAndAvaliacaoLessThan(Integer.valueOf(numTemporada), Double.valueOf(avaliacao));
        if (!seriesPorTemporada.isEmpty()) {
            System.out.println("\nEstas séries atende os parâmetros de avaliação:");
            seriesPorTemporada.stream().forEach(e -> System.out.println("Temporadas: " + e.getTotalTemporadas() + ", avaliacao: " + e.getAvaliacao() + ", serie: " + e.getTitulo()));
        } else {
            System.out.println("\nNão encontramos série que atenda os parâmetros!");
        }
        if (!seriesPorTemporada2.isEmpty()) {
            System.out.println("\nEsta séris tem a avaliação baixa:");
            seriesPorTemporada2.stream().forEach(e -> System.out.println("Temporadas: " + e.getTotalTemporadas() + ", avaliacao: " + e.getAvaliacao() + ", serie: " + e.getTitulo()));
        }

    }
}