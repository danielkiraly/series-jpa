package com.codecool.series;

import com.codecool.series.entity.Series;
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
public class SeriesRepositoryTest {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void saveOneSimple(){
        Series testSeries = Series.builder()
                .name("test")
                .release_date(LocalDate.of(2019, 01, 01))
                .build();

        seriesRepository.save(testSeries);

        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).hasSize(1);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveUniqueFieldTwice(){
        Series series1 = Series.builder()
                .name("test")
                .release_date(LocalDate.of(2019, 01, 01))
                .build();

        seriesRepository.save(series1);

        Series series2 = Series.builder()
                .name("test")
                .release_date(LocalDate.of(2019, 01, 01))
                .build();

        seriesRepository.saveAndFlush(series2);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void nameShouldNotBeNull(){
        Series series1 = Series.builder()
                .release_date(LocalDate.of(2019, 01, 01))
                .build();

        seriesRepository.save(series1);
    }

    @Test
    public void transientIsNotSaved(){
        Series series1 = Series.builder()
                .name("test")
                .release_date(LocalDate.of(2017, 01, 01))
                .build();
        series1.calculateYearsSinceRunning();
        assertThat(series1.getYears_since_running()).isGreaterThanOrEqualTo(1);

        seriesRepository.save(series1);
        entityManager.clear();

        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).allMatch( series -> series.getYears_since_running() == 0L);
    }

}
