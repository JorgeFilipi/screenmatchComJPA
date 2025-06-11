package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);


    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadasEAvaliação(int totalTemporadas, double avaliacao);

    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao < :avaliacao")
    List<Serie> seriesPorTemporadasEAvaliaçãoB(int totalTemporadas, double avaliacao);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio% ")
    List<Episodio> episodioPorTrecho(String trechoEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE  s = :serie AND YEAR(e.dataLancamento) >= :ano")
    List<Episodio> episodioPorSerieEAno(Serie serie, int ano);

    @Query("SELECT DISTINCT s.titulo FROM Serie s JOIN s.episodios e")
    List<String> seriesComEpisodios();

    @Query("SELECT DISTINCT s FROM Serie s LEFT JOIN s.episodios e WHERE e IS NULL")
    List<Serie> seriesSemEpisodios();

    @Transactional
    @Modifying
    @Query("DELETE FROM Episodio e WHERE e.serie.id = :serieId")
    void deletarEpisodiosPorSerie(@Param("serieId") Long serieId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Serie s WHERE s.id = :id")
    void deletarSerie(@Param("id") Long id);


    @Query("SELECT s FROM Serie s " +
            "JOIN s.episodios e " +
            "GROUP BY s " +
            "ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> lancamentosMaisRecentes();

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
    List<Episodio> obterEpisodiosPorTemporada(Long id, Long numero);

}
