package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleInfo;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SetNameCommand extends Command {
    public SetNameCommand() {
        super("name");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            String arg = args[1].toLowerCase();

            switch (arg) {
                case "load":
                    LiquidSense.fileManager.loadConfig(LiquidSense.fileManager.setNameConfig);
                    break;
                case "list":
                    for (Module module : LiquidSense.moduleManager.getModules()) {
                        if (module.getArrayListName().equals(module.getClass().getAnnotation(ModuleInfo.class).name())) continue;
                        chat("Module <" + module.getArrayListName() + "> is §7" + module.getClass().getAnnotation(ModuleInfo.class).name());
                    }
                    break;
                case "cleaner":
                    for (Module module : LiquidSense.moduleManager.getModules()) {
                        if (args.length > 2) {
                            String newValue = getName(args, 2);

                            Module oldModule = LiquidSense.moduleManager.getModule(newValue);
                            if (oldModule == null) return;

                            if (module.getName().equalsIgnoreCase(oldModule.getName())) {
                                module.setArrayListName(module.getClass().getAnnotation(ModuleInfo.class).name());

                                LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.setNameConfig);
                                chat("reset Module <" + newValue + '>');
                                return;
                            }
                        }
                        module.setArrayListName(module.getClass().getAnnotation(ModuleInfo.class).name());
                        LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.setNameConfig);
                    }
                    chat("reset Module name ");
                    break;
                default:
                    if (args.length > 2) {
                        String newValue = getName(args, 2);
                        String oldVaule = getName(args, 1);
                        Module module = LiquidSense.moduleManager.getModule(oldVaule);
                        if (module == null) {
                            chat("Module §a§l" + args[1] + "§3 not found.");
                            return;
                        }
                        module.setNameCommand(newValue);
                        LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.setNameConfig);
                        chat("Module §a§l" + args[1] + "§l§a to " + newValue);
                        return;
                    }
                    break;
            }
            return;
        }
        chatSyntax(new String[]{"<module> <name>", "<cleaner/load/list>"});
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

    public String getName(final String[] args, int size) {
        String string = args[size];
        switch (string) {
            case "#":
                string = string.replace("#", "");
                break;
            case "#l":
                string = string.replace("#l", EnumChatFormatting.BOLD.toString());
                break;
            case "#o":
                string = string.replace("#o", EnumChatFormatting.ITALIC.toString());
                break;
            case "#m":
                string = string.replace("#m", EnumChatFormatting.STRIKETHROUGH.toString());
                break;
            case "#n":
                string = string.replace("#n", EnumChatFormatting.UNDERLINE.toString());
                break;
        }
        return string;
    }
}
