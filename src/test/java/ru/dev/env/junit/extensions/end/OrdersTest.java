package ru.dev.env.junit.extensions.end;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.dev.env.junit.extensions.Helpers;

@SuppressWarnings("PMD.TooManyMethods")
class OrdersTest {

    private final Order firstOrder = Helpers.createOrder();
    private final Order secondOrder = Helpers.createOrder();
    private final Order thirdOrder = Helpers.createOrder();

    private Path tempFile;

    @RegisterExtension
    private TempFileExtension instanceExtension = new TempFileExtension();

    @RegisterExtension
    private static TempFileExtension staticExtension = new TempFileExtension();

    @BeforeEach
    void createTempFile() throws IOException {
        tempFile = Files.createTempFile("orders", "temp-file");
    }

    @AfterEach
    void deleteTempFile() throws IOException {
        Files.delete(tempFile);
    }

    @RepeatedTest(10)
    void parseFile() throws IOException {
        String string = """
                %s
                %s
                %s
                """.formatted(firstOrder, secondOrder, thirdOrder);
        Files.writeString(tempFile, string);

        Orders orders = Orders.parse(tempFile);

        AssertionsForInterfaceTypes.assertThat(orders).isNotNull().hasSize(3)
                .containsExactly(firstOrder, secondOrder, thirdOrder);
    }

    @RepeatedTest(10)
    void parseFileFromExtension() throws IOException {
        String string = """
                %s
                %s
                %s
                """.formatted(firstOrder, secondOrder, thirdOrder);
        Files.writeString(instanceExtension.getFile(), string);

        Orders orders = Orders.parse(instanceExtension.getFile());

        AssertionsForInterfaceTypes.assertThat(orders).isNotNull().hasSize(3)
                .containsExactly(firstOrder, secondOrder, thirdOrder);
    }

    @RepeatedTest(10)
    void parseFileFromStaticExtension() throws IOException {
        String string = """
                %s
                %s
                %s
                """.formatted(firstOrder, secondOrder, thirdOrder);
        Files.writeString(staticExtension.getFile(), string);

        Orders orders = Orders.parse(staticExtension.getFile());

        AssertionsForInterfaceTypes.assertThat(orders).isNotNull().hasSize(3)
                .containsExactly(firstOrder, secondOrder, thirdOrder);
    }

    @Test
    void parseNullFile() {
        ThrowingCallable throwingCallable = () -> Orders.parse((Path) null);

        Helpers.assertNullParameter(throwingCallable, "path");
    }

    @Test
    void create() {
        Orders orders = Orders.from(firstOrder, secondOrder, thirdOrder);

        AssertionsForInterfaceTypes.assertThat(orders).isNotNull().hasSize(3)
                .containsExactly(firstOrder, secondOrder, thirdOrder);
    }

    @Test
    void parse() {
        String string = """
                %s
                %s
                %s
                """.formatted(firstOrder, secondOrder, thirdOrder);

        Orders orders = Orders.parse(string);

        AssertionsForInterfaceTypes.assertThat(orders).isNotNull().hasSize(3)
                .containsExactly(firstOrder, secondOrder, thirdOrder);
    }

    @Test
    void errorParseNull() {
        ThrowingCallable throwingCallable = () -> Orders.parse((String) null);

        Helpers.assertNullParameter(throwingCallable, "input");
    }

}
