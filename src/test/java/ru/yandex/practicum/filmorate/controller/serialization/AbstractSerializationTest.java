package ru.yandex.practicum.filmorate.controller.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

/**
 * Test class for the {@link ObjectMapper} <br>
 * Default JsonMapper from IOC Container <br>
 * Tests serialization DTO objects <br>
 */

@JsonTest
public abstract class AbstractSerializationTest {

    @Autowired
    protected ObjectMapper mapper;
}