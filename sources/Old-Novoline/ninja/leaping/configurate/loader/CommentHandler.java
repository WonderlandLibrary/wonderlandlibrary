package ninja.leaping.configurate.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Interface specifying methods for handling abstract comments
 */
public interface CommentHandler {

    /**
     * Defines the handlers behaviour for reading comments.
     *
     * @param reader The reader
     * @return The comment
     * @throws IOException If any IO error occurs in the process
     */
    @NotNull Optional<String> extractHeader(@NotNull BufferedReader reader) throws IOException;

    /**
     * Converts the given lines into a comment
     *
     * @param lines The lines to make a comment
     * @return The transformed lines
     */
    @NotNull Collection<String> toComment(@NotNull Collection<String> lines);
}
