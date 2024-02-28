package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generateId = 1;

    @Override
    public int getFilmsQuantity() {
        log.debug("InMemoryFilmStorage - films.getFilmsQuantity().");
        return films.size();
    }

    @Override
    public List<Film> getFilms() {
        log.debug("InMemoryFilmStorage - films.getFilms().");
        return new ArrayList<>(films.values());
    }

    @Override
    public Set<Integer> getAllRowId() {
        log.debug("InMemoryFilmStorage - films.getKeys().");
        return new HashSet<>(films.keySet());
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

    @Override
    public Integer addFilm(Film film) {
        log.debug("InMemoryFilmStorage - films.addFilm().");
        Integer generatedId = generateId();

        film.setId(generatedId);
        films.put(generatedId, film);

        log.info("Фильм добавлен: " + film);
        return generatedId;
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("InMemoryFilmStorage - films.updateFilm().");
        Integer key = film.getId();
        return films.put(key, film);
    }

    public boolean containsFilm(Film film) {
        log.debug("InMemoryFilmStorage - films.containsFilm().");
        return films.containsKey(film.getId());
    }

    @Override
    public boolean containsById(int id) {
        log.debug("InMemoryFilmStorage - films.containsById().");
        return films.containsKey(id);
    }

    private int generateId() {
        log.debug("InMemoryFilmStorage - films.generatedId().");
        while (films.containsKey(generateId))
            ++generateId;

        return this.generateId;
    }
}
