package io.tech1.framework.domain.properties.utilities;

import io.tech1.framework.domain.asserts.Asserts;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import io.tech1.framework.domain.reflections.ClassProperty;
import io.tech1.framework.domain.utilities.reflections.ReflectionUtility;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullNotEmptyOrThrow;
import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static java.util.Collections.emptyList;
import static org.springframework.util.StringUtils.uncapitalize;

@Slf4j
@UtilityClass
public class PropertiesAsserter {

    private static final Map<Function<Class<?>, Boolean>, Consumer<ClassProperty>> ACTIONS = new HashMap<>() {{
        put(Date.class::equals, Asserts::assertNonNullClassPropertyOrThrow);
        put(LocalDate.class::equals, Asserts::assertNonNullClassPropertyOrThrow);
        put(LocalDateTime.class::equals, Asserts::assertNonNullClassPropertyOrThrow);
        put(ChronoUnit.class::equals, Asserts::assertNonNullClassPropertyOrThrow);
        put(Boolean.class::equals, Asserts::assertNonNullClassPropertyOrThrow);
        put(Short.class::equals, Asserts::assertNonNullClassPropertyOrThrow);
        put(Integer.class::equals, Asserts::assertNonNullClassPropertyOrThrow);
        put(Long.class::equals, Asserts::assertNonNullClassPropertyOrThrow);
        put(BigInteger.class::equals, Asserts::assertNonNullClassPropertyOrThrow);
        put(BigDecimal.class::equals, Asserts::assertNonNullClassPropertyOrThrow);
        put(String.class::equals, Asserts::assertNonNullClassPropertyOrThrow);
        put(Collection.class::isAssignableFrom, cp -> assertNonNullNotEmptyOrThrow((Collection<?>) cp.getValue(), invalidAttribute(cp.getName())));
    }};

    public static void assertProperties(AbstractPropertiesConfigs abstractConfigs, String parentName) {
        assertNonNullOrThrow(abstractConfigs, invalidAttribute(parentName));
        var getters = getGetters(abstractConfigs, parentName, emptyList());
        verifyProperties(getters, abstractConfigs, parentName, emptyList());
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private static void verifyProperties(List<Method> getters, Object property, String parentName, List<String> projection) {
        getters.forEach(getter -> {
            var attribute = uncapitalize(getter.getName()
                    .replace("get", "")
                    .replace("is", "")
            );
            var attributeName = parentName + "." + attribute;
            try {
                var getterValue = getter.invoke(property);
                innerClass(getterValue, attributeName, projection);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException("Unexpected. Attribute: " + attributeName);
            }
        });
    }

    private static void innerClass(Object property, String attributeName, List<String> projection) {
        assertNonNullOrThrow(property, invalidAttribute(attributeName));
        var propertyClass = property.getClass();
        var consumerOpt = ACTIONS.entrySet().stream()
                .filter(entry -> entry.getKey().apply(propertyClass))
                .map(Map.Entry::getValue)
                .findFirst();
        if (consumerOpt.isPresent()) {
            var assertFieldConsumer = consumerOpt.get();
            var assertField = ClassProperty.of(propertyClass, attributeName, property);
            assertFieldConsumer.accept(assertField);
        } else {
            var getters = getGetters(property, attributeName, projection);
            verifyProperties(getters, property, attributeName, projection);
        }
    }

    private static List<Method> getGetters(Object property, String attributeName, List<String> skipProjection) {
        assertNonNullOrThrow(property, invalidAttribute(attributeName));
        return ReflectionUtility.getGetters(property).stream()
                .filter(Objects::nonNull)
                .filter(method -> !method.getName().equals("getOrder"))
                .filter(method -> {
                    try {
                        var fieldName = uncapitalize(method.getName()
                                .replace("get", "")
                                .replace("is", "")
                        );
                        var declaredField = property.getClass().getDeclaredField(fieldName);
                        return declaredField.isAnnotationPresent(MandatoryProperty.class) && !declaredField.isAnnotationPresent(NonMandatoryProperty.class);
                    } catch (NoSuchFieldException e) {
                        return true;
                    }
                })
                .filter(method -> {
                    var lowerCaseAttribute = method.getName().toLowerCase().replace("get", "");
                    return !skipProjection.contains(lowerCaseAttribute);
                })
                .collect(Collectors.toList());
    }
}
