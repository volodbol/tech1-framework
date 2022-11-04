package io.tech1.framework.domain.tuples;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Tuple2<A, B> {
    private final A a;
    private final B b;

    public static <A, B> Tuple2<A, B> of(
            A a,
            B b
    ) {
        return new Tuple2<>(
                a,
                b
        );
    }
}
