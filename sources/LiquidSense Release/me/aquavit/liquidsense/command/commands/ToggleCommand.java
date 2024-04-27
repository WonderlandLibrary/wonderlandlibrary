package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.module.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("toggle", "t");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            Module module = LiquidSense.moduleManager.getModule(args[1]);

            if (module == null) {
                chat("Module '" + args[1] + "' not found.");
                return;
            }

            if (args.length > 2) {
                String newState = args[2].toLowerCase();

                if (newState.equals("on") || newState.equals("off")) {
                    module.setState(newState.equals("on"));
                } else {
                    chatSyntax("toggle <module> [on/off]");
                    return;
                }

            } else {
                module.toggle();
            }

            chat((module.getState() ? "Enabled" : "Disabled") + " module §8" + module.getName() + "§3.");
            return;
        }

        chatSyntax("toggle <module> [on/off]");
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 0) return new ArrayList<>();

        String moduleName = args[0];

        if (args.length == 1) {
            return LiquidSense.moduleManager.getModules().stream()
                    .map(Module::getName)
                    .filter(module -> module.toLowerCase().startsWith(moduleName.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
