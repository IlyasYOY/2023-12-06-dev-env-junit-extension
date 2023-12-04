package ru.dev.env.junit.extensions.end;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Delegate;

@Value
public final class Orders implements Collection<Order> {

    @Delegate
    Collection<Order> items;

    private Orders(Order... orders) {
        this.items = Arrays.stream(orders).toList();
    }

    public static Orders from(Order... orders) {
        return new Orders(orders);
    }

    public static Orders parse(@NonNull String input) {
        var parts = input.split("\n");

        Stream.Builder<Order> builder = Stream.builder();
        for (String part : parts) {
            Order order = Order.parse(part);
            builder.add(order);
        }

        Order[] array = builder.build().toArray(Order[]::new);
        return Orders.from(array);
    }

    public static Orders parse(@NonNull Path path) {
        String content = readFile(path);
        return Orders.parse(content);
    }

    private static String readFile(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            // TODO: Тут еще надо будет проверить и сделать тест, если уж вы решили скопировать.
            return null;
        }
    }

}
