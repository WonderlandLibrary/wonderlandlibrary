package net.minecraft.command;

public class PlayerNotFoundException extends CommandException {
    public PlayerNotFoundException() {
        this("impl.generic.player.notFound", new Object[0]);
    }

    public PlayerNotFoundException(String message, Object... replacements) {
        super(message, replacements);
    }
}
