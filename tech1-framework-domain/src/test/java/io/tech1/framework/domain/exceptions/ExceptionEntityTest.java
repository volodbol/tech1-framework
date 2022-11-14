package io.tech1.framework.domain.exceptions;

import com.fasterxml.jackson.core.type.TypeReference;
import io.tech1.framework.domain.tests.runners.AbstractObjectMapperRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.tech1.framework.domain.constants.ExceptionEntityConstants.ATTRIBUTE_FULL_MESSAGE;
import static io.tech1.framework.domain.constants.ExceptionEntityConstants.ATTRIBUTE_SHORT_MESSAGE;
import static io.tech1.framework.domain.exceptions.ExceptionEntityType.ERROR;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionEntityTest extends AbstractObjectMapperRunner {

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Test
    public void serializeDeserializeTest() {
        // Arrange
        var exceptionMessage = randomString();
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};

        // Act
        var json = this.writeValueAsString(ExceptionEntity.of(new NullPointerException(exceptionMessage)));
        HashMap<String, Object> exceptionEntity = OBJECT_MAPPER.readValue(json, typeRef);

        // Assert
        assertThat(exceptionEntity).hasSize(3);
        assertThat(exceptionEntity).containsKeys("exceptionEntityType", "attributes", "timestamp");
        assertThat(exceptionEntity.get("exceptionEntityType")).isEqualTo(ERROR.toString());
        assertThat(exceptionEntity.get("timestamp")).isNotNull();
        var attributes = (Map<String, Object>) exceptionEntity.get("attributes");
        assertThat(attributes.get(ATTRIBUTE_SHORT_MESSAGE)).isEqualTo(exceptionMessage);
        assertThat(attributes.get(ATTRIBUTE_FULL_MESSAGE)).isEqualTo(exceptionMessage);
    }
}
