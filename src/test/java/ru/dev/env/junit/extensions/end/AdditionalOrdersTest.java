package ru.dev.env.junit.extensions.end;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.dev.env.junit.extensions.Helpers;

class AdditionalOrdersTest {

    private final Order firstOrder = Helpers.createOrder();
    private final Order secondOrder = Helpers.createOrder();
    private final Order thirdOrder = Helpers.createOrder();


    @RepeatedTest(10)
    @ExtendWith(TempFileExtension.class)
    void parseFileFromExtensionResolvingParameter(Path tempFile)
            throws IOException {
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

}
