package ru.dev.env.junit.extensions.end;

import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import net.datafaker.Faker;
import ru.dev.env.junit.extensions.Helpers;

class OrderTest {

    private final Faker faker = new Faker();

    private void assertOrder(long id, String cookName, String product,
            Order order) {
        AssertionsForClassTypes.assertThat(order).isNotNull()
                .returns(id, Order::getId).returns(cookName, Order::getCookName)
                .returns(product, Order::getProduct);
    }

    @Nested
    class FromFactoryMethod {
        private final long id = faker.number().randomNumber();
        private final String cookName = faker.name().firstName();
        private final String product = faker.food().sushi();

        @Test
        void createSimpleOrder() {
            var order = Order.from(id, cookName, product);

            assertOrder(id, cookName, product, order);
        }

        @Test
        void createSimpleOrderErrorCookNameIsNull() {
            String nullCookName = null;

            ThrowingCallable throwable =
                    () -> Order.from(id, nullCookName, product);

            Helpers.assertNullParameter(throwable, "cookName");
        }

        @Test
        void createSimpleOrderErrorProductIsNull() {
            String nullProduct = null;

            ThrowingCallable throwable =
                    () -> Order.from(id, cookName, nullProduct);

            Helpers.assertNullParameter(throwable, "product");
        }
    }

    @Nested
    class ParseLineMethod {
        private final long id = faker.number().randomNumber();
        private final String cookName = faker.name().firstName();
        private final String product = faker.food().dish();

        @Test
        void success() {
            String input = "%d,%s,%s".formatted(id, cookName, product);

            var order = Order.parse(input);

            assertOrder(id, cookName, product, order);
        }

        @Test
        void errorInputInNull() {
            String nullInput = null;

            ThrowingCallable throwable = () -> Order.parse(nullInput);

            Helpers.assertNullParameter(throwable, "input");
        }

        @Test
        void errorStringWithIllegalId() {
            String illegalId = faker.name().fullName();
            String input = "%s,%s,%s".formatted(illegalId, cookName, product);

            ThrowingCallable throwable = () -> Order.parse(input);

            AssertionsForClassTypes.assertThatThrownBy(throwable)
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("'%s' must be a number", illegalId)
                    .hasCauseExactlyInstanceOf(NumberFormatException.class);
        }

        @Test
        void errorNotEnoughData() {
            String input = "%s,%s".formatted(id, cookName);

            ThrowingCallable throwable = () -> Order.parse(input);

            AssertionsForClassTypes.assertThatThrownBy(throwable)
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("'%s' not enough data to create order", input)
                    .hasNoCause();
        }

        @Test
        void errorTooMuchData() {
            String extraInput = faker.app().name();
            String input =
                    "%s,%s,%s,%s".formatted(id, cookName, product, extraInput);

            ThrowingCallable throwable = () -> Order.parse(input);

            AssertionsForClassTypes.assertThatThrownBy(throwable)
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("'%s' too much data to create order", input)
                    .hasNoCause();
        }

    }

    @Nested
    class ToStringToCsv {
        private final long id = faker.number().randomNumber();
        private final String cookName = faker.name().firstName();
        private final String product = faker.food().dish();

        private final Order order = Order.from(id, cookName, product);

        @Test
        void success() {
            var toString = order.toString();

            String input = "%d,%s,%s".formatted(id, cookName, product);
            AssertionsForClassTypes.assertThat(toString).isEqualTo(input);
        }

        @Test
        void parseAndToStringCycle() {
            var orderAfterToString = Order.parse(order.toString());

            AssertionsForClassTypes.assertThat(orderAfterToString)
                    .isEqualTo(order);
        }
    }


}
