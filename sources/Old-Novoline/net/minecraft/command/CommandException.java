package net.minecraft.command;

public class CommandException extends Exception {

    private final Object[] errorObjects;

    public CommandException(String message, Object... objects) {
        super(message);
        this.errorObjects = objects;
    }

    public CommandException(String message, Throwable t, Object... objects) {
        super(message, t);
        this.errorObjects = objects;
    }

    public Object[] getErrorObjects() {
        return this.errorObjects;
    }

}
