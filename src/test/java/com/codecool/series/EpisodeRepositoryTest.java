package com.codecool.series;

import com.codecool.series.entity.Episode;
import com.codecool.series.entity.Season;
import com.codecool.series.entity.Series;
import com.codecool.series.repository.EpisodeRepository;
import com.codecool.series.repository.SeasonRepository;
import com.codecool.series.repository.SeriesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class EpisodeRepositoryTest {

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Test
    public void episodesArePersistedAndDeletedWithNewSeasons(){
        Set<Episode> episodes = IntStream.range(1, 10)
                .boxed()
                .map(integer -> Episode.builder().title("EP" + integer).build())
                .collect(Collectors.toSet());

        Season season1 = Season.builder()
                .season_number(1L)
                .episodes(episodes)
                .build();

        seasonRepository.save(season1);

        assertThat(episodeRepository.findAll())
                .hasSize(9)
                .anyMatch(episode -> episode.getTitle().equals("EP2"));

        seasonRepository.deleteAll();

        assertThat(episodeRepository.findAll())
                .hasSize(0);
    }

}
