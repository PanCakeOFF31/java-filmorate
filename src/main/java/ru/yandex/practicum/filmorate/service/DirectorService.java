package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.director.DirectorNotFounException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmDirector.DirectorStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorService {
    private final JdbcTemplate jdbcTemplate;
    private final DirectorStorage directorStorage;

    public List<Director> receiveDirectors() {
        log.debug("DirectorService - service.receiveDirectors()");

        List<Director> directors = directorStorage.getDirectors();

        int directorQuantity = directorStorage.getDirectorQuantity();
        log.info("Возвращен список режиссеров в количестве: {}", directorQuantity);

        return directors;
    }

    public Director receiveDirectorById(final int directorId) {
        log.debug("DirectorService - service.receiveDirectorById({})", directorId);

        directorIsExist(directorId);

        return directorStorage.getDirectorById(directorId);
    }

    public Director createDirector(final Director director) {
        log.debug("DirectorService - service.createDirector()");
        log.info("Количество режиссеров до добавления: " + directorStorage.getDirectorQuantity());

        Integer id = directorStorage.createDirector(director);
        log.info("Добавлен режиссер с id: " + id);

        Director addedDirector = directorStorage.getDirectorById(id);

        log.info(director.toString());
        log.info("Количество режиссеров теперь: " + directorStorage.getDirectorQuantity());

        return addedDirector;
    }

    public Director changeDirector(final Director director) {
        log.debug("DirectorService - service.changeDirector()");

        int directorId = director.getId();
        directorIsExist(directorId);

        log.info("Режиссер до обновления: {}", directorStorage.getDirectorById(directorId));

        Director updateDirector = directorStorage.changeDirector(director);
        log.info("Режиссер после обновления: {}", updateDirector);

        return updateDirector;
    }

    public Director deleteDirectorById(final int directorId) {
        log.debug("DirectorService - service.deleteDirectorById()");

        directorIsExist(directorId);

        log.info("Количество режиссеров до удаления: " + directorStorage.getDirectorQuantity());

        Director delletedDirector = directorStorage.deleteDirectorById(directorId);

        log.info("Количество режиссеров теперь: " + directorStorage.getDirectorQuantity());

        return delletedDirector;
    }

    public List<Film> receiveSortedDirectorFilmsBy(final int directorId, final String sortBy) {
        log.debug("DirectorService - service.receiveSortedDirectorFilmsBy()");
        return null;
    }


    private boolean directorIsExist(int directorId) {
        String message = "Режиссера нет с id :" + directorId;

        if (!directorStorage.containsById(directorId)) {
            log.warn(message);
            throw new DirectorNotFounException(message);
        }

        return true;
    }

}
