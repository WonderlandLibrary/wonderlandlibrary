package tech.atani.client.feature.command;

import tech.atani.client.feature.command.data.CommandInfo;
import tech.atani.client.utility.interfaces.Methods;

public abstract class Command implements Methods {

    final String name, description;
    final String[] aliases;

    public Command() {
        final CommandInfo info = getClass().getAnnotation(CommandInfo.class);
        name = info.name();
        description = info.description();
        aliases = info.aliases().length != 0 ? info.aliases() : new String[] {name};
    }

    public abstract boolean execute(String[] args);

    public String getPrefix() {
        return ".";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }
}
