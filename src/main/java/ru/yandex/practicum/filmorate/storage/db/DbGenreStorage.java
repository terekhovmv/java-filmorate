package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier(DbStorageConsts.QUALIFIER)
public class DbGenreStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> rowMapper;

    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = new DbGenreRowMapper();
    }

    @Override
    public Optional<Genre> getById(short id) {
        List<Genre> found = jdbcTemplate.query(
                "SELECT * FROM genres WHERE id=?;",
                this.rowMapper,
                id
        );
        if (found.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(found.get(0));
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM genres;",
                this.rowMapper
        );
    }
}
