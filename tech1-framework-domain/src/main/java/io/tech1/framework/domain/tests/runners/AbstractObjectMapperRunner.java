package io.tech1.framework.domain.tests.runners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

public abstract class AbstractObjectMapperRunner {
    protected static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            .build();

    @SneakyThrows
    protected final String writeValueAsString(Object object) {
        return OBJECT_MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(object);
    }
}
