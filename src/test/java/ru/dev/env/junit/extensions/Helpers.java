package ru.dev.env.junit.extensions;

import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import lombok.experimental.UtilityClass;
import net.datafaker.Faker;
import ru.dev.env.junit.extensions.end.Order;

@UtilityClass
public class Helpers {

    private static final Faker FAKER = new Faker();

    public static void assertNullParameter(
            ThrowingCallable shouldRaiseThrowable, String parameterName) {
        AssertionsForClassTypes.assertThatThrownBy(shouldRaiseThrowable)
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessageStartingWith(parameterName);
    }

    public static Order createOrder() {
        long randomNumber = FAKER.number().randomNumber();
        String fullName = FAKER.name().fullName();
        String product = FAKER.food().dish();

        return Order.from(randomNumber, fullName, product);
    }

}
