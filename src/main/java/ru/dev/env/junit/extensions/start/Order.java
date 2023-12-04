package ru.dev.env.junit.extensions.start;

import lombok.NonNull;
import lombok.Value;

@Value
public final class Order {

    private static final int INPUT_STRING_LENGTH = 3;

    private static final String SEPARATOR = ",";
    private static final String TO_STRING_FORMAT = "%d,%s,%s";

    long id;
    String cookName;
    String product;

    private Order(long id, String cookName, String product) {
        this.id = id;
        this.cookName = cookName;
        this.product = product;
    }

    @NonNull
    public static Order from(long id, @NonNull String cookName,
            @NonNull String product) {
        return new Order(id, cookName, product);
    }

    @NonNull
    public static Order parse(@NonNull String input) {
        var parts = input.split(SEPARATOR);

        if (parts.length < INPUT_STRING_LENGTH) {
            throw new IllegalArgumentException("'%s' not enough data to create order".formatted(input));
        }
        if (parts.length > INPUT_STRING_LENGTH) {
            throw new IllegalArgumentException("'%s' too much data to create order".formatted(input));
        }

        var id = parseId(parts[0]);
        var cookName = parts[1];
        var product = parts[2];

        return Order.from(id, cookName, product);
    }

    private static long parseId(String part) {
        try {
            return Long.parseLong(part);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "'%s' must be a number".formatted(part), exception);
        }
    }

    @Override
    public String toString() {
        return TO_STRING_FORMAT.formatted(id, cookName, product);
    }

}
