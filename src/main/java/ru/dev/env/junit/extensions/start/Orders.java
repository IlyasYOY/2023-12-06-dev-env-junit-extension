package ru.dev.env.junit.extensions.start;

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

    public static Orders parse(Path path) {
        // TODO: Реализовать чтение из
        // - файла,
        // - директории,
        // - рекурсивный обход всех директорий.
        
        // TODO: Добавить поддержку JSON, полагаясь на расширение файлов.
        throw new UnsupportedOperationException();
    }

}
