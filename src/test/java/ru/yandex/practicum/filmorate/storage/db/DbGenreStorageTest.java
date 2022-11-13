package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.BaseGenreStorageTest;
import ru.yandex.practicum.filmorate.storage.GenreStorage;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbGenreStorageTest extends BaseGenreStorageTest {

    private final DbGenreStorage testee;

    @Override
    protected GenreStorage getTestee() {
        return testee;
    }
}
