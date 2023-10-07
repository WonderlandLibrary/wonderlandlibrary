package cc.novoline.utils.messages;


import static net.minecraft.util.EnumChatFormatting.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author xDelsy
 */
public class HelpMessage extends TextMessage {

    /* constructors */
    private HelpMessage(@NotNull String name, @NotNull String command, @NotNull UsageMessage... subCommands) {
        super(name);
        append("\n\n");

        for (int i = 0, length = subCommands.length; i < length; i++) {
            final UsageMessage subCommand = subCommands[i];
            append(subCommand.command(command + " " + subCommand.command));

            if (i + 1 != length) newLine();
        }
    }

    public static @NotNull HelpMessage of(@NotNull String name, @NotNull String command, @NotNull UsageMessage... subCommands) {
        return new HelpMessage(name, command, subCommands);
    }

    /**
     * @author xDelsy
     */
    public static final class UsageMessage extends TextMessage {

        /* fields */
        @NotNull
        private final String command, description;

        /* constructors */
        private UsageMessage(@NotNull String command, @NotNull String description) {
            super("> ", GRAY);
            this.command = command;
            this.description = description;

            append(this.command, GREEN);
            append(" - ", GRAY);
            append(this.description + (this.description.endsWith(".") ? "" : "."), RESET);
        }

        @NotNull
        public static UsageMessage of(@NotNull String command, @NotNull String description) {
            return new UsageMessage(command, description);
        }

        /* methods */
        @NotNull
        public UsageMessage command(@NotNull String command) {
            return new UsageMessage(command, description);
        }
    }
}
