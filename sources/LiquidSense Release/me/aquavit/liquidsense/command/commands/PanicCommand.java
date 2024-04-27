package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PanicCommand extends Command {
    public PanicCommand(){
        super("panic");
    }

    @Override
    public void execute(String[] args) {
        List<Module> modules = LiquidSense.moduleManager.getModules().stream()
                .filter(Module::getState)
                .collect(Collectors.toList());
        String msg;

        if (args.length > 1 && !args[1].isEmpty()) {
            switch (args[1].toLowerCase()) {
                case "all":
                    msg = "all";
                    break;

                case "nonrender":
                    modules = modules.stream()
                            .filter(module -> module.getCategory() != ModuleCategory.RENDER)
                            .collect(Collectors.toList());
                    msg = "all non-render";
                    break;

                default:
                    List<ModuleCategory> categories = Arrays.stream(ModuleCategory.values())
                            .filter(category -> category.getDisplayName().equalsIgnoreCase(args[1]))
                            .collect(Collectors.toList());

                    if (categories.isEmpty()) {
                        chat("Category " + args[1] + " not found");
                        return;
                    }

                    ModuleCategory category = categories.get(0);
                    modules = modules.stream()
                            .filter(module -> module.getCategory() == category)
                            .collect(Collectors.toList());
                    msg = "all " + category.getDisplayName();
                    break;
            }
        } else {
            chatSyntax("panic <all/nonrender/blatant/client/exploit/ghost/hud/misc/movement/player/render/world>");
            return;
        }

        for (Module module : modules) {
            module.setState(false);
        }

        chat("Disabled " + msg + " modules.");
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 0) return new ArrayList<>();

        if (args.length == 1) {
            return Arrays.stream(new String[]{"all", "nonrender", "blatant", "client", "exploit", "ghost", "hud", "misc", "movement", "player", "render", "world"})
                    .filter(it -> it.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}
