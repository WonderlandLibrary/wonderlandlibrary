package dev.rise.util.misc;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class RiseLogger {
    @Getter
    private final EvictingList<String> messages = new EvictingList<>(2000);

    public void println(final String line) {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        final LocalDateTime now = LocalDateTime.now();
        messages.add("[" + dtf.format(now) + "] " + line);
    }
}
