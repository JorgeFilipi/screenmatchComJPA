package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe principal da aplicação, responsável por interagir com o usuário
 * através de um menu no console, processar as opções e interagir com a API
 * do OMDB e o banco de dados.
 */
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
    private Optional<Serie> serieBuscada;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {

        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    \n############################
                    1 - Buscar séries
                    2 - Buscar episódios (JPA)
                    3 - Listas de séries
                    4 - Buscar séries por Titulo
                    5 - buscar séries por ator
                    6 - Top 5 séries
                    7 - Buscar série por categoria
                    8 - Filtrar séries 
                    9 - buscar episódios pos trecho 
                    10 - top episódios por série 
                    11 - Bucar episódios por data 
                    12 - Excluir episódios de uma serie 
                    13 - Excluir serie com episódios
                    
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
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarepisodiosPorData();
                    break;
                case 12:
                    excluirEpisodio();
                    break;
                case 13:
                    excluirSerie();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    /**
     * Busca os dados de uma série na API do OMDB e salva no banco de dados.
     */
    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repository.save(serie);
        System.out.println(dados);
    }

    /**
     * Método auxiliar para obter os dados de uma série da API.
     * Pede o nome da série ao usuário, faz a requisição e converte o JSON.
     * return um objeto DadosSerie com as informações da série.
     */
    private DadosSerie getDadosSerie() {
        System.out.println("\nDigite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        return conversor.obterDados(json, DadosSerie.class);
    }

    /**
     * Busca uma série no banco de dados pelo título (ignorando maiúsculas/minúsculas)
     * e armazena o resultado no campo 'serieBuscada'.
     */
    private void buscarSeriePorNome() {
        System.out.println("\nEscolha uma serie para busca");
        var nomeSerie = leitura.nextLine();
        serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);
    }

    /**
     * Lista os títulos das séries que já possuem episódios cadastrados no banco de dados.
     */
    private void listarSeriesComEpisodios() {
        System.out.println("\nEsta são series com uma lista de episódios salvos\n");
        List<String> serieComEpisodios = repository.seriesComEpisodios();
        serieComEpisodios.forEach(System.out::println);
    }

    private void listarSeriesSemEpisodios() {
        System.out.println("\nEsta são series sem uma lista de episódios salvos\n");
        series = repository.seriesSemEpisodios();
        series.forEach(s -> System.out.println(s.getTitulo()));
    }

    /**
     * Busca na API todos os episódios de uma série já salva no banco de dados
     * e os associa à série, salvando-os.
     */
    private void buscarEpisodioPorSerie() {
        listarSeriesSemEpisodios();
        System.out.println("\nEscolha uma serie para busca");
        String nomeSerie = leitura.nextLine();
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
                .sorted(Comparator.comparing(Serie::getId))
                .forEach(s -> System.out.printf("%s - Titulo: %s, nota: %s, temporadas %s, elenco (%s), sinopse - '%s' - poster:%s\n",
                        s.getId(),
                        s.getTitulo().toUpperCase(),
                        s.getAvaliacao(),
                        s.getTotalTemporadas(),
                        s.getAtores(),
                        s.getSinopse(),
                        s.getPoster()));
    }

    private void buscarSeriePorTitulo() {
        buscarSeriePorNome();
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
        System.out.println("\nSéries em que " + nomeAtor + " trabalhou!");
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
        var totalTemporadas = leitura.nextLine();
        System.out.println("Qual é a avaliação minima da séria:");
        var avaliacao = leitura.nextLine();
        List<Serie> seriesPorTemporada = repository.seriesPorTemporadasEAvaliação(Integer.valueOf(totalTemporadas), Double.valueOf(avaliacao));
        List<Serie> seriesPorTemporada2 = repository.seriesPorTemporadasEAvaliaçãoB(Integer.valueOf(totalTemporadas), Double.valueOf(avaliacao));
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

    private void buscarEpisodioPorTrecho() {
        System.out.println("\nNome do episódio para busca: ");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repository.episodioPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: '%s', temporada: %s, avaliação: %s, episódio: %s - %s\n",
                        e.getSerie().getTitulo().toUpperCase(),
                        e.getTemporada(),
                        e.getAvaliacao(),
                        e.getNumeroEpisodio(),
                        e.getTitulo()));
    }

    private void topEpisodiosPorSerie() {
        listarSeriesComEpisodios();
        buscarSeriePorNome();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série: '%s', temporada: %s, avaliação: %s, episódio: %s - %s\n",
                            e.getSerie().getTitulo().toUpperCase(),
                            e.getTemporada(),
                            e.getAvaliacao(),
                            e.getNumeroEpisodio(),
                            e.getTitulo()));
        }
    }

    private void buscarepisodiosPorData() {
        listarSeriesComEpisodios();
        buscarSeriePorNome();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            System.out.println("Digite o ano de lançamento");
            var ano = leitura.nextInt();

            List<Episodio> episodiosAno = repository.episodioPorSerieEAno(serie, ano);
            episodiosAno.forEach(System.out::println);
        }
    }

    private void excluirEpisodio() {
        listarSeriesComEpisodios();
        System.out.println("\nDigite o nome da série para excluir todos os episódios:");
        String nomeSerie = leitura.nextLine();

        Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            repository.deletarEpisodiosPorSerie(serie.getId());
            System.out.println("Todos os episódios da série '" + serie.getTitulo() + "' foram excluídos com sucesso!");
        } else {
            System.out.println("Série não encontrada.");
        }
    }

    @Transactional
    private void excluirSerie() {
        listarSeriesBuscadas();

        System.out.println("Digite o nome da série para excluir:");
        String nomeSerie = leitura.nextLine();

        Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();

            repository.deletarEpisodiosPorSerie(serie.getId());
            repository.deletarEpisodiosPorSerie(serie.getId());
            repository.deletarSerie(serie.getId());

            System.out.println("Série '" + serie.getTitulo() + "' e todas as temporadas/episódios associados foram excluídos!");
        } else {
            System.out.println("Série não encontrada.");
        }


    }
}