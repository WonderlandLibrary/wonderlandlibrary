package me.earth.phobos.features.command.commands;

import com.google.gson.JsonParser;
import me.earth.phobos.Phobos;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.manager.ConfigManager;

public class ModuleCommand
        extends Command {
    public ModuleCommand() {
        super("module", new String[]{"<module>", "<set/reset>", "<setting>", "<value>"});
    }

    @Override
    public void execute(String[] commands) {
        Setting setting;
        if (commands.length == 1) {
            ModuleCommand.sendMessage("Modules: ");
            for (Module.Category category : Phobos.moduleManager.getCategories()) {
                String modules = category.getName() + ": ";
                for (Module module : Phobos.moduleManager.getModulesByCategory(category)) {
                    modules = modules + (module.isEnabled() ? "\u00a7a" : "\u00a7c") + module.getName() + "\u00a7r" + ", ";
                }
                ModuleCommand.sendMessage(modules);
            }
            return;
        }
        Module module = Phobos.moduleManager.getModuleByDisplayName(commands[0]);
        if (module == null) {
            module = Phobos.moduleManager.getModuleByName(commands[0]);
            if (module == null) {
                ModuleCommand.sendMessage("\u00a7cThis module doesnt exist.");
                return;
            }
            ModuleCommand.sendMessage("\u00a7c This is the original name of the module. Its current name is: " + module.getDisplayName());
            return;
        }
        if (commands.length == 2) {
            ModuleCommand.sendMessage(module.getDisplayName() + " : " + module.getDescription());
            for (Setting setting2 : module.getSettings()) {
                ModuleCommand.sendMessage(setting2.getName() + " : " + setting2.getValue() + ", " + setting2.getDescription());
            }
            return;
        }
        if (commands.length == 3) {
            if (commands[1].equalsIgnoreCase("set")) {
                ModuleCommand.sendMessage("\u00a7cPlease specify a setting.");
            } else if (commands[1].equalsIgnoreCase("reset")) {
                for (Setting setting3 : module.getSettings()) {
                    setting3.setValue(setting3.getDefaultValue());
                }
            } else {
                ModuleCommand.sendMessage("\u00a7cThis command doesnt exist.");
            }
            return;
        }
        if (commands.length == 4) {
            ModuleCommand.sendMessage("\u00a7cPlease specify a value.");
            return;
        }
        if (commands.length == 5 && (setting = module.getSettingByName(commands[2])) != null) {
            JsonParser jp = new JsonParser();
            if (setting.getType().equalsIgnoreCase("String")) {
                setting.setValue(commands[3]);
                ModuleCommand.sendMessage("\u00a7a" + module.getName() + " " + setting.getName() + " has been set to " + commands[3] + ".");
                return;
            }
            try {
                if (setting.getName().equalsIgnoreCase("Enabled")) {
                    if (commands[3].equalsIgnoreCase("true")) {
                        module.enable();
                    }
                    if (commands[3].equalsIgnoreCase("false")) {
                        module.disable();
                    }
                }
                ConfigManager.setValueFromJson(module, setting, jp.parse(commands[3]));
            } catch (Exception e) {
                ModuleCommand.sendMessage("\u00a7cBad Value! This setting requires a: " + setting.getType() + " value.");
                return;
            }
            ModuleCommand.sendMessage("\u00a7a" + module.getName() + " " + setting.getName() + " has been set to " + commands[3] + ".");
        }
    }
}

