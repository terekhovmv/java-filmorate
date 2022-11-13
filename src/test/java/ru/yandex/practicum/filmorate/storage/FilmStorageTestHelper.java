package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FilmStorageTestHelper {
    private final FilmStorage storage;

    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public FilmStorageTestHelper(
            FilmStorage storage,
            MpaStorage mpaStorage,
            GenreStorage genreStorage
    ) {
        this.storage = storage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public FilmStorageTestHelper(
            FilmStorage storage
    ) {
        this.storage = storage;
        this.mpaStorage = null;
        this.genreStorage = null;
    }

    public void addFilm(int idx, short mpaId, List<Short> genreIds) {
        storage.create(Film.builder()
                .name(createFilmName(idx))
                .description(createFilmDescription(idx))
                .releaseDate(createFilmReleaseDate(idx))
                .duration(createFilmDuration(idx))
                .mpa(createFilmMpaLight(mpaId))
                .genres(createFilmGenresLight(genreIds))
                .build()
        );
    }

    public void addFilm(int idx) {
        addFilm(idx, mpaIdForIdx(idx), genreIdsForIdx(idx));
    }

    public Film getExpectedFilmNoRate(int idx, short mpaId, List<Short> genreIds, Integer rate) {
        Objects.requireNonNull(mpaStorage);
        Objects.requireNonNull(genreStorage);

        return Film.builder()
                .id(idx)
                .name(createFilmName(idx))
                .description(createFilmDescription(idx))
                .releaseDate(createFilmReleaseDate(idx))
                .duration(createFilmDuration(idx))
                .mpa(getFilmMpa(mpaId))
                .genres(getFilmGenres(genreIds))
                .rate(rate)
                .build();
    }

    public Film getExpectedFilmNoRate(int idx) {
        return getExpectedFilmNoRate(idx, mpaIdForIdx(idx), genreIdsForIdx(idx), null);
    }

    public Film getExpectedFilm(int idx, int rate) {
        return getExpectedFilmNoRate(idx, mpaIdForIdx(idx), genreIdsForIdx(idx), rate);
    }

    private String createFilmName(int idx) {
        return idx + "name";
    }

    private String createFilmDescription(int idx) {
        return idx + "description";
    }

    private LocalDate createFilmReleaseDate(int idx) {
        return LocalDate.of(1970, 1, idx);
    }

    private int createFilmDuration(int idx) {
        return 100 + idx;
    }

    private Mpa createFilmMpaLight(short mpaId) {
        return new Mpa(mpaId, null);
    }

    private Genre createFilmGenreLight(short genreId) {
        return new Genre(genreId, null);
    }

    private List<Genre> createFilmGenresLight(List<Short> genreIds) {
        return genreIds.stream().map(this::createFilmGenreLight).collect(Collectors.toList());
    }

    private Mpa getFilmMpa(short mpaId) {
        return mpaStorage.require(mpaId);
    }

    private Genre getFilmGenre(short genreId) {
        return genreStorage.require(genreId);
    }

    private List<Genre> getFilmGenres(List<Short> genreIds) {
        return genreIds.stream().map(this::getFilmGenre).collect(Collectors.toList());
    }

    private short mpaIdForIdx(int idx) {
        return (short)(idx % 5 + 1);
    }

    private List<Short> genreIdsForIdx(int idx) {
        return List.of((short)(idx % 5 + 1), (short)(idx % 5 + 2));
    }
}
