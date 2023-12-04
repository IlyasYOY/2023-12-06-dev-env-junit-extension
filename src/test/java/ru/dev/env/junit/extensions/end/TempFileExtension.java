package ru.dev.env.junit.extensions.end;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class TempFileExtension
        implements BeforeEachCallback, BeforeAllCallback, ParameterResolver {

    private static final Namespace NAMESPACE =
            Namespace.create(TempFileExtension.class);

    private Path file;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        createFile(context);
        log.info("File received at all phase: {}", file);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        createFile(context);
        log.info("File received at each phase: {}", file);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
            ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Path.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
            ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return file;
    }


    private void createFile(ExtensionContext context) throws IOException {
        var store = context.getStore(NAMESPACE.append(this));
        store.getOrComputeIfAbsent("file", key -> new ClosablePath(),
                ClosablePath.class);
    }

    private class ClosablePath implements Store.CloseableResource {
        private Path file;

        public ClosablePath() {
            log.debug("Create temp file...");
            try {
                Path tempFile = Files.createTempFile("temp-file", "orders");
                log.info("Temp file created: {}", tempFile);
                this.file = tempFile;
                TempFileExtension.this.file = tempFile;
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public void close() throws Throwable {
            Files.delete(file);
            log.info("File {} was removed", file);
            this.file = null;
            TempFileExtension.this.file = null;
        }
    }

}
