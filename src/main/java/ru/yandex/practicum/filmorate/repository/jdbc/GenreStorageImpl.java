package ru.yandex.practicum.filmorate.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.repository.GenreStorage;

import java.util.*;

@Repository
@Transactional
@RequiredArgsConstructor
public class GenreStorageImpl implements GenreStorage {

    private final JdbcClient jdbcClient;

    @Override
    public Set<Genre> findByIdList(Set<Long> idList) {
        return jdbcClient
                .sql("SELECT ID, NAME FROM GENRES WHERE ID IN (:id)")
                .param("id", idList)
                .query(Genre.class)
                .set();

    }

    @Override
    public Optional<Genre> findById(long id) {
        return jdbcClient
                .sql("SELECT ID, NAME FROM GENRES WHERE ID = ?")
                .param(id)
                .query(Genre.class)
                .optional();
    }

    @Override
    public List<Genre> findAll() {
        return jdbcClient
                .sql("SELECT ID, NAME FROM GENRES")
                .query(Genre.class)
                .list();
    }
}