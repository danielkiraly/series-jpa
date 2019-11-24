package com.codecool.series.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Series {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private LocalDate release_date;

    @Singular
    @OneToMany(mappedBy = "series", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @EqualsAndHashCode.Exclude
    private Set<Season> seasons;

    @Transient
    private long years_since_running;

    public void calculateYearsSinceRunning(){
        if( release_date != null){
            years_since_running = ChronoUnit.YEARS.between(release_date, LocalDate.now());
        }
    }

    @Singular
    @ElementCollection
    private List<String> characters;

}
