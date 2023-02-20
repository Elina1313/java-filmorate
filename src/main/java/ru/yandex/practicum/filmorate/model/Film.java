package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Data
public class Film {

    private int id;

    @NotNull
    private String name;

    @NotBlank
    private String description;

    private LocalDate releaseDate;

    private long duration;

}
