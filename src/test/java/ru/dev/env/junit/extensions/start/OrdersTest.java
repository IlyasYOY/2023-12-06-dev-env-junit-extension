package ru.dev.env.junit.extensions.start;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import net.datafaker.Faker;
import ru.dev.env.junit.extensions.Helpers;

class OrdersTest {

    private final Faker faker = new Faker();

    private final Order firstOrder = createOrder();
    private final Order secondOrder = createOrder();
    private final Order thirdOrder = createOrder();

    @Test
    void create() {
        Orders orders = Orders.from(firstOrder, secondOrder, thirdOrder);

        assertOrders(orders);
    }

    @Test
    void parse() {
        Orders orders = Orders.parse("""
                %s
                %s
                %s
                """.formatted(firstOrder, secondOrder, thirdOrder));

        assertOrders(orders);
    }

    @Test
    void errorParseNull() {
        ThrowingCallable throwingCallable = () -> Orders.parse((String) null);

        Helpers.assertNullParameter(throwingCallable, "input");
    }

    private void assertOrders(Orders orders) {
        AssertionsForInterfaceTypes.assertThat(orders).isNotNull().hasSize(3)
                .containsExactly(firstOrder, secondOrder, thirdOrder);
    }

    private Order createOrder() {
        long randomNumber = faker.number().randomNumber();
        String fullName = faker.name().fullName();
        String product = faker.food().dish();

        return Order.from(randomNumber, fullName, product);
    }

}
