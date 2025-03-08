package ru.yandex.practicum.filmorate.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.repository.MPARatingStorage;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class MPARatingStorageImpl implements MPARatingStorage {

    private final JdbcClient jdbcClient;

    @Override
    public Optional<MPARating> findById(long id) {
        return jdbcClient
                .sql("SELECT ID, NAME FROM MPA_RATING WHERE ID = ? ")
                .param(id)
                .query(MPARating.class)
                .optional();
    }

    @Override
    public List<MPARating> findAll() {
        return jdbcClient
                .sql("SELECT ID, NAME FROM MPA_RATING")
                .query(MPARating.class)
                .list();
    }
}