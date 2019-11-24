package com.codecool.series;

import com.codecool.series.entity.Season;
import com.codecool.series.entity.Series;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class SeasonRepositoryTest {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void seasonIsPersistedWithSeries(){
        Season season = Season.builder()
                .season_number(1L)
                .year_released(2019L)
                .build();

        Series series1 = Series.builder()
                .name("test")
                .release_date(LocalDate.of(2019, 01, 01))
                .season(season)
                .build();

        seriesRepository.save(series1);

        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons).hasSize(1).allMatch( season1 -> season1.getId() > 0L);

    }
}
