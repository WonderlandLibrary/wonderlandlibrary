package io.github.raze.commands.collection;

import io.github.raze.commands.system.Command;
import io.github.raze.utilities.collection.configs.NameUtil;

import java.util.Arrays;

public class NameProtectCommand extends Command {

    public NameProtectCommand() {
        super("NameProtect", "Set your custom name when using NameProtect module", "nameprotect <Name>", "np");
    }

    public String onCommand(String[] arguments, String command) {
        if (arguments.length == 1) {
            return "Usage: " + getSyntax();
        }

        NameUtil.saveCustomName(String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length)));

        return String.format("Set your custom name to %s", NameUtil.getCustomNameFromFile());
    }
}
