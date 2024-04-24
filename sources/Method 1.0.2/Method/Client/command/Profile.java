package Method.Client.command;

import Method.Client.Main;
import Method.Client.clickgui.component.Frame;
import Method.Client.module.Category;
import Method.Client.module.ModuleManager;
import Method.Client.module.Profiles.Profiletem;
import Method.Client.utils.visual.ChatUtils;

import static Method.Client.module.ModuleManager.addModule;
import static Method.Client.module.ModuleManager.getModuleByName;

@Command.CommandFeatures(description = "Modify Profile Add/Remove", Syntax = "profile <Add/Remove> [Name]")

public class Profile extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        if (args[0].equalsIgnoreCase("Remove")) {
            if (ModuleManager.getModuleByName(args[1]) != null && ModuleManager.getModuleByName(args[1]).getCategory() == Category.PROFILES) {
                ModuleManager.toggledModules.remove(getModuleByName(args[1]));
                ModuleManager.modules.remove(getModuleByName(args[1]));
                ChatUtils.message("Removed Profile" + args[1]);
            }
        }
        if (args[0].equalsIgnoreCase("Add")) {
            if (ModuleManager.getModuleByName(args[1]) == null) {
                addModule(new Profiletem(args[1]));
                ChatUtils.message("Added Profile: " + args[1]);
            } else ChatUtils.error("Profile name taken");
        }
        for (Frame frame : Main.ClickGui.frames) {
            if (frame.getCategory().equals(Category.PROFILES)) {
                frame.updateRefresh();
            }
        }

    }

}