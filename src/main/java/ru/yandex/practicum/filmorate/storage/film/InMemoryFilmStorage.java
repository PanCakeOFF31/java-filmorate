package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generateId = 1;

    @Override
    public int getFilmsSize() {
        return films.size();
    }

    private int generateId() {
        while (films.containsKey(generateId))
            ++generateId;

        return this.generateId;
    }
}
