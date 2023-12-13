package me.earth.phobos.features.command.commands;

import me.earth.phobos.Phobos;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.render.XRay;
import me.earth.phobos.features.setting.Setting;

public class XrayCommand
        extends Command {
    public XrayCommand() {
        super("xray", new String[]{"<add/del>", "<block>"});
    }

    @Override
    public void execute(String[] commands) {
        XRay module = Phobos.moduleManager.getModuleByClass(XRay.class);
        if (module != null) {
            if (commands.length == 1) {
                StringBuilder blocks = new StringBuilder();
                for (Setting setting : module.getSettings()) {
                    if (setting.equals(module.enabled) || setting.equals(module.drawn) || setting.equals(module.bind) || setting.equals(module.newBlock) || setting.equals(module.showBlocks))
                        continue;
                    blocks.append(setting.getName()).append(", ");
                }
                Command.sendMessage(blocks.toString());
                return;
            }
            if (commands.length == 2) {
                XrayCommand.sendMessage("Please specify a block.");
                return;
            }
            String addRemove = commands[0];
            String blockName = commands[1];
            if (addRemove.equalsIgnoreCase("del") || addRemove.equalsIgnoreCase("remove")) {
                Setting setting = module.getSettingByName(blockName);
                if (setting != null) {
                    if (setting.equals(module.enabled) || setting.equals(module.drawn) || setting.equals(module.bind) || setting.equals(module.newBlock) || setting.equals(module.showBlocks)) {
                        return;
                    }
                    module.unregister(setting);
                }
                XrayCommand.sendMessage("<XRay>\u00a7c Removed: " + blockName);
            } else if (addRemove.equalsIgnoreCase("add")) {
                if (!module.shouldRender(blockName)) {
                    module.register(new Setting<Object>(blockName, Boolean.valueOf(true), v -> module.showBlocks.getValue()));
                    XrayCommand.sendMessage("<Xray> Added new Block: " + blockName);
                }
            } else {
                XrayCommand.sendMessage("\u00a7cAn error occured, block either exists or wrong use of command: .xray <add/del(remove)> <block>");
            }
        }
    }
}

