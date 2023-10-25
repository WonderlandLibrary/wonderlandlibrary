package fun.rich.client.command;


import fun.rich.client.utils.Helper;
import fun.rich.client.utils.other.ChatUtils;


public abstract class CommandAbstract implements Command, Helper {


    private final String name;
    private final String description;
    private final String usage;
    private final String[] aliases;

    public CommandAbstract(String name, String description, String usage, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.usage = usage;
    }

    public void usage() {
        ChatUtils.addChatMessage("§cInvalid usage, try: " + usage + " or .help");
    }

    public String getUsage() {
        return this.usage;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String[] getAliases() {
        return this.aliases;
    }
}
