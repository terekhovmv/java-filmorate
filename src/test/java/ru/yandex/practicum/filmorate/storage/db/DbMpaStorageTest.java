package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.BaseMpaStorageTest;
import ru.yandex.practicum.filmorate.storage.MpaStorage;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbMpaStorageTest extends BaseMpaStorageTest {
    private final DbMpaStorage testee;

    @Override
    protected MpaStorage getTestee() {
        return testee;
    }
}
