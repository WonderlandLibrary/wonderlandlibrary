package Method.Client.command;

import Method.Client.Main;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.utils.visual.ChatUtils;

@Command.CommandFeatures(description = "Edit Module", Syntax = "Edit <Name>")

public class Edit extends Command {

    @Override
    public void runCommand(String s, String[] args) {

        String name = args[0];
        Module module = ModuleManager.getModuleByName(name);
        module.toggle();
        if (module.getCategory() == Category.MAKER) {
            Main.Maker.setModule(module);
            ChatUtils.message("Trying to edit " + name);
            mc.displayGuiScreen(Main.Maker);
        } else {
            ChatUtils.message("Edit using the maker");
        }


    }


}