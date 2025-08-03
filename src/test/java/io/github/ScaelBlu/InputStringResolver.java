package io.github.ScaelBlu;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.IOException;
import java.io.InputStream;

public class InputStringResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(String.class) &&
                parameterContext.isAnnotated(InputFile.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String file = parameterContext.getParameter().getAnnotation(InputFile.class).value();
        try (InputStream is = getClass().getResourceAsStream(file)) {
            if (is != null) {
                return new String(is.readAllBytes());
            }
            throw new ParameterResolutionException("No such file in the classpath: ".concat(file));
        } catch (IOException ioe) {
            throw new IllegalStateException("Can not read file: ".concat(file), ioe);
        }
    }
}
