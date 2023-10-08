package net.minecraft.command;

public class NumberInvalidException extends CommandException {
    public NumberInvalidException() {
        this("impl.generic.num.invalid", new Object[0]);
    }

    public NumberInvalidException(String message, Object... replacements) {
        super(message, replacements);
    }
}
