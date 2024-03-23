package xyz.northclient.commands;

public abstract class Command {
    private String aliases;
    private String usage;

    public abstract void execute(final String command, final String[] args);

    public Command(String aliases,String usage) {
        this.aliases = aliases;
        this.usage = usage;
    }

    public String getAliases() {
        return aliases;
    }

    public String getUsage() {
        return usage;
    }
}