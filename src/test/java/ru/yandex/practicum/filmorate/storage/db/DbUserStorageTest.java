package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.storage.BaseUserStorageTest;
import ru.yandex.practicum.filmorate.storage.UserStorage;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbUserStorageTest extends BaseUserStorageTest {
    private final DbUserStorage testee;

    @Override
    protected UserStorage getTestee() {
        return testee;
    }

    @BeforeEach
    protected void beforeEach() {
        super.beforeEach();
    }
}
