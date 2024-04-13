package dev.tenacity.command;

public abstract class AbstractCommand {

    private final String name, description, usage;
    private final int necessaryArguments;

    public AbstractCommand(final String name, final String description, final String usage, final int necessaryArguments) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.necessaryArguments = necessaryArguments;
    }

    public final String getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    public final String getUsage() {
        return usage;
    }

    public final int getNecessaryArguments() {
        return necessaryArguments;
    }

    public abstract void onCommand(final String[] arguments);

}
