package ru.dev.env.junit.extensions;

import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

public class Helpers {

    public static void assertNullParameter(ThrowingCallable shouldRaiseThrowable,
            String parameterName) {
        AssertionsForClassTypes.assertThatThrownBy(shouldRaiseThrowable)
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessageStartingWith(parameterName);
    }
    
}
