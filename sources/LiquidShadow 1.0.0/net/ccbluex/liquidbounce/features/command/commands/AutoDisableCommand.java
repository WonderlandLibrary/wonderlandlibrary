package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.AutoDisableMode;
import net.ccbluex.liquidbounce.features.module.Module;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AutoDisableCommand extends Command {
    public AutoDisableCommand() {
        super("autodisable");
    }

    @Override
    public void execute(@NotNull String[] args) {
        if (args.length <= 1) {
            chatSyntax("autodisable <module> [none/flag/death]");
            return;
        }

        Module module = LiquidBounce.moduleManager.getModule(args[1]);
        if (module == null) {
            chat("this module is not exist!");
            return;
        }
        if (args.length == 2) {
            chat(args[1] + "'s autoDisableMode is " + module.getAutoDisableMode().name());

        }

        if (args.length == 3) {
            AutoDisableMode autoDisableMode = AutoDisableMode.NONE;
            if (args[2].equalsIgnoreCase("flag")) {
                autoDisableMode = AutoDisableMode.FLAG;
            } else if (args[2].equalsIgnoreCase("death")) {
                autoDisableMode = AutoDisableMode.DEATH;
            }

            module.setAutoDisableMode(autoDisableMode);
            chat(args[1] + "'s autoDisableMode was been set to " + autoDisableMode.name());
        }

    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull String[] args) {
        if (args.length == 1) {
            List<String> s = new ArrayList<>();
            for (Module module : LiquidBounce.moduleManager.getModules()) {
                if (module.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    s.add(module.getName());
                }
            }
            return s;
        } else if (args.length == 2) {
            List<String> autoDisableModes = Arrays.asList("NONE","FLAG","DEATH");
            List<String> s = new ArrayList<>();
            for (String autoDisableMode : autoDisableModes) {
                if (autoDisableMode.toLowerCase().startsWith(args[1].toLowerCase())) {
                    s.add(autoDisableMode);
                }
            }
            return s;
        }
        return Collections.emptyList();
    }
}
