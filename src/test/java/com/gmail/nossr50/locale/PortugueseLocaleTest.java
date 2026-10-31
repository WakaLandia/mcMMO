package com.gmail.nossr50.locale;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class PortugueseLocaleTest {
    private static final Pattern ARGUMENT = Pattern.compile("\\{\\d+(?:,[^}]*)?\\}");

    @Test
    void portugueseShouldContainEveryOriginalMessage() throws IOException {
        final Properties original = loadLocale("en_US");
        final Properties portuguese = loadLocale("pt_BR");

        assertThat(portuguese.stringPropertyNames()).containsAll(original.stringPropertyNames());
        for (final String key : original.stringPropertyNames()) {
            if (!original.getProperty(key).isBlank()) {
                assertThat(portuguese.getProperty(key)).as(key).isNotBlank();
            }
        }
    }

    @Test
    void portugueseShouldPreserveEveryMessageArgument() throws IOException {
        final Properties original = loadLocale("en_US");
        final Properties portuguese = loadLocale("pt_BR");

        for (final String key : original.stringPropertyNames()) {
            assertThat(arguments(portuguese.getProperty(key, "")))
                    .as(key)
                    .isEqualTo(arguments(original.getProperty(key)));
        }
    }

    private static Properties loadLocale(final String locale) throws IOException {
        final String path = "/com/gmail/nossr50/locale/locale_" + locale + ".properties";
        final var stream = PortugueseLocaleTest.class.getResourceAsStream(path);
        assertThat(stream).as(path).isNotNull();
        try (final var reader = new InputStreamReader(stream, StandardCharsets.UTF_8.newDecoder())) {
            final Properties properties = new Properties();
            properties.load(reader);
            return properties;
        }
    }

    private static Set<String> arguments(final String message) {
        return ARGUMENT.matcher(message).results()
                .map(result -> result.group())
                .collect(java.util.stream.Collectors.toCollection(TreeSet::new));
    }
}
