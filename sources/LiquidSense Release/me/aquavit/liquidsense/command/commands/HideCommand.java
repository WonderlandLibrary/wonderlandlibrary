package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.client.ClientUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HideCommand extends Command {

    public HideCommand(){
        super("hide");
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            String arg = args[1].toLowerCase();
            switch (arg) {
                case "list":
                    LiquidSense.moduleManager.getModules().stream().filter(Module::getArray).forEach(module -> ClientUtils.displayChatMessage("§6> §c" + module.getName()));
                    chat("§c§lHidden");
                    break;
                case "clear":
                    for (Module module : LiquidSense.moduleManager.getModules()) module.setArray(true);
                    chat("Cleared hidden modules.");
                    break;
                case "reset":
                    for (Module module : LiquidSense.moduleManager.getModules()) module.setArray(module.getClass().getAnnotation(ModuleInfo.class).array());
                    chat("Reset hidden modules.");
                    break;
                default:
                    Module module = LiquidSense.moduleManager.getModule(args[1]);

                    if (module == null) {
                        chat("Module §a§l" + args[1] + "§3 not found.");
                        return;
                    }

                    module.setArray(!module.getArray());

                    chat("Module §a§l" + module.getName() + "§3 is now §a§l" + (module.getArray() ? "visible" : "invisible") + "§3 on the array list.");
                    playEdit();
                    break;
            }
            return;
        }
        chatSyntax("hide <module/list/clear/reset>");
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
