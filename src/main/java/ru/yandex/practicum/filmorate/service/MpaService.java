package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.ExistChecker;
import ru.yandex.practicum.filmorate.storage.filmMpa.MpaDbStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage ratingDbStorage;
    private final ExistChecker existChecker;

    public List<Mpa> getMpas() {
        log.debug("GenreService - service.getMpas()");
        return ratingDbStorage.getAllMpa();
    }

    public Mpa getMpaById(int mpaId) {
        log.debug("GenreService - service.getMpaById()");
        existChecker.mpaIsExist(mpaId);
        return ratingDbStorage.getMpa(mpaId);
    }
}