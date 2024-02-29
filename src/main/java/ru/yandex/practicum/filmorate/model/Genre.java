package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class Genre {
    private final int id;
    private final String name;
}
