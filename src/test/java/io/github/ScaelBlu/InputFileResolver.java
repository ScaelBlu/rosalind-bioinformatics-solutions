package io.github.ScaelBlu;
import org.junit.jupiter.api.extension.*;

import java.io.*;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class InputFileResolver implements ParameterResolver, AfterEachCallback {

/*  MEGJEGYZÉS:
    A Namespace egy egyedi névteret jelent, amivel Extension-ök, vagy az Extension különböző részei elkülöníthetik
    a saját adataikat a Store-ban. A Store objektumok egyszerű tárolók (Map<Object, Object> szerű), amik kulcs-érték párokat
    tartalmaznak. A Store-t az ExtensionContext biztosítja, ami több szintes hierarchiában reprezentálja a tesztmetódus, a
    tesztosztály, a teljes tesztfuttatás kontextusát (a teljesség igénye nélkül), így a Store életciklusa ehhez kötött.
    A Namespace-t érdemes statikusan létrehozni egy Extension-höz. A Namespace.create() paramétere egyedi azonosítót jelent
    (Namespace + key -> érték), így egy Store-ban nem ütköznek az Extension-ök kulcsai. A névtér az a keret, amelyen belül
    a kulcsok/azonosítók/nevek/stb. egyediek. A többszálúságot figyelembe kell venni! A Store és metódusai szinkronizáltak,
    DE a benne lévő értékekre figyelni kell ( pl. értékadáshoz a getOrComputeIfAbsent() )!
*/
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(InputFileResolver.class);
    private static final String CLOSEABLE_KEY = "CLOSEABLE_KEY";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(InputFile.class);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final Parameter parameter = parameterContext.getParameter();
        final String file = parameter.getAnnotation(InputFile.class).value();
        try {
            final InputStream inputStream = getClass().getResourceAsStream(file);
            if (inputStream != null) {
                if (parameter.getType().equals(String.class)) {
                    return new String(inputStream.readAllBytes());
                }
                if (parameter.getType().equals(BufferedReader.class)) {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    final List<Closeable> toClose = extensionContext.getStore(NAMESPACE)
                            .getOrComputeIfAbsent(CLOSEABLE_KEY, _ -> new ArrayList<>(), List.class);
                    toClose.add(reader);
                return reader;
                }
            }
            throw new ParameterResolutionException("No such file in the classpath: ".concat(file));
        } catch (IOException ioe) {
            throw new IllegalStateException("Can not read file: ".concat(file), ioe);
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void afterEach(ExtensionContext context) throws IOException {
        final List<Closeable> toClose = context.getStore(NAMESPACE).remove(CLOSEABLE_KEY, List.class);
        if (toClose != null) {
            for (Closeable closeable : toClose) {
                closeable.close();
            }
        }
    }
}
