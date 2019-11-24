package com.codecool.series;

import com.codecool.series.entity.Author;
import com.codecool.series.entity.Episode;
import com.codecool.series.entity.Season;
import com.codecool.series.entity.Series;
import com.codecool.series.repository.SeasonRepository;
import com.codecool.series.repository.SeriesRepository;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.persistence.PostRemove;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
public class SeriesApplication {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    public static void main(String[] args) {
        SpringApplication.run(SeriesApplication.class, args);
    }

    @Bean
    @Profile("production")
    public CommandLineRunner init(){
        return args -> {
            Set<Episode> episodes1 = IntStream.range(1, 10)
                    .boxed()
                    .map(integer -> Episode.builder()
                            .title("S01Episode" + integer)
                            .directed_by("Kirk")
                            .author(Author.BENIOFFWEISS)
                            .build())
                    .collect(Collectors.toSet());

            Set<Episode> episodes2 = IntStream.range(1, 10)
                    .boxed()
                    .map(integer -> Episode.builder()
                            .title("S02Episode" + integer)
                            .directed_by("Taylor")
                            .author(Author.COGMAN)
                            .build())
                    .collect(Collectors.toSet());

            Set<Episode> episodes3 = IntStream.range(1, 10)
                    .boxed()
                    .map(integer -> Episode.builder()
                            .title("S03Episode" + integer)
                            .directed_by("Minahan")
                            .author(Author.RRMARTIN)
                            .build())
                    .collect(Collectors.toSet());

            Season season1 = Season.builder()
                    .season_number(1L)
                    .episodes_count(10L)
                    .episodes(episodes1)
                    .year_released(2011L)
                    .build();

            setSeasonInEpisodes(season1);

            Season season2 = Season.builder()
                    .season_number(2L)
                    .episodes_count(10L)
                    .episodes(episodes2)
                    .year_released(2012L)
                    .build();

            setSeasonInEpisodes(season2);

            Season season3 = Season.builder()
                    .season_number(3L)
                    .episodes_count(10L)
                    .episodes(episodes3)
                    .year_released(2013L)
                    .build();

            setSeasonInEpisodes(season3);

            Series gameOfThrones = Series.builder()
                    .name("Game of Thrones")
                    .release_date(LocalDate.of(2010, 4, 12))
                    .season(season1)
                    .season(season2)
                    .season(season3)
                    .characters(Lists.newArrayList("John Snow", "Daenerys", "Grejoy"))
                    .build();

            season1.setSeries(gameOfThrones);
            season2.setSeries(gameOfThrones);
            season3.setSeries(gameOfThrones);

            seriesRepository.save(gameOfThrones);

        };
    }

    private void setSeasonInEpisodes(Season season){
        Set<Episode> episodes = season.getEpisodes();
        for (Episode episode: episodes){
            episode.setSeason(season);
        }
    }

}
