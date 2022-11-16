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

    public void addFilm(int userFriendlyIdx, short mpaId, List<Short> genreIds) {
        storage.create(Film.builder()
                .name(createFilmName(userFriendlyIdx))
                .description(createFilmDescription(userFriendlyIdx))
                .releaseDate(createFilmReleaseDate(userFriendlyIdx))
                .duration(createFilmDuration(userFriendlyIdx))
                .mpa(createFilmMpaLight(mpaId))
                .genres(createFilmGenresLight(genreIds))
                .build()
        );
    }

    public void addFilm(int userFriendlyIdx) {
        addFilm(userFriendlyIdx, createFilmMpaId(userFriendlyIdx), createFilmGenreIds(userFriendlyIdx));
    }

    public Film getExpectedFilm(int id, int userFriendlyIdx, short mpaId, List<Short> genreIds, Integer rate) {
        Objects.requireNonNull(mpaStorage);
        Objects.requireNonNull(genreStorage);

        return Film.builder()
                .id(id)
                .name(createFilmName(userFriendlyIdx))
                .description(createFilmDescription(userFriendlyIdx))
                .releaseDate(createFilmReleaseDate(userFriendlyIdx))
                .duration(createFilmDuration(userFriendlyIdx))
                .mpa(getFilmMpa(mpaId))
                .genres(getFilmGenres(genreIds))
                .rate(rate)
                .build();
    }

    public Film getExpectedFilmNoRate(int id) {
        return getExpectedFilmNoRate(id, id);
    }

    public Film getExpectedFilmNoRate(int id, int userFriendlyIdx) {
        return getExpectedFilm(id, userFriendlyIdx, createFilmMpaId(userFriendlyIdx), createFilmGenreIds(userFriendlyIdx), null);
    }

    public Film getExpectedFilmWithRate(int id, int rate) {
        return getExpectedFilmWithRate(id, id, rate);
    }

    public Film getExpectedFilmWithRate(int id, int userFriendlyIdx, int rate) {
        return getExpectedFilm(id, userFriendlyIdx, createFilmMpaId(userFriendlyIdx), createFilmGenreIds(userFriendlyIdx), rate);
    }


    private String createFilmName(int userFriendlyIdx) {
        return userFriendlyIdx + "name";
    }

    private String createFilmDescription(int userFriendlyIdx) {
        return userFriendlyIdx + "description";
    }

    private LocalDate createFilmReleaseDate(int userFriendlyIdx) {
        return LocalDate.of(1970, 1, userFriendlyIdx);
    }

    private int createFilmDuration(int userFriendlyIdx) {
        return 100 + userFriendlyIdx;
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

    private short createFilmMpaId(int userFriendlyIdx) {
        return (short)(userFriendlyIdx % 5 + 1);
    }

    private List<Short> createFilmGenreIds(int userFriendlyIdx) {
        return List.of((short)(userFriendlyIdx % 5 + 1), (short)(userFriendlyIdx % 5 + 2));
    }
}
