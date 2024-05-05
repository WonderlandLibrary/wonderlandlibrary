package io.github.raze.commands.collection;

import io.github.raze.Raze;
import io.github.raze.commands.system.Command;
import io.github.raze.utilities.collection.configs.PrefixUtil;

import java.util.Arrays;

public class PrefixCommand extends Command {

    public PrefixCommand() {
        super("Prefix", "Set the chat prefix", "prefix <Character>", "p");
    }

    public String onCommand(String[] arguments, String command) {
        if (arguments.length == 1) {
            return "Usage: " + getSyntax();
        }

        String prefix = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));

        if (!prefix.matches("[^A-Za-z0-9]+")) {
            return "Invalid prefix. Only non-alphanumeric characters are allowed.";
        }

        Raze.INSTANCE.prefix = prefix;
        PrefixUtil.savePrefix(prefix);

        return String.format("Set the prefix to %s", Raze.INSTANCE.getPrefix());
    }


}
