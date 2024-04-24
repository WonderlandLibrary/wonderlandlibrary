package Method.Client.command;

import Method.Client.managers.FileManager;
import Method.Client.managers.Setting;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.utils.visual.ChatUtils;

import static Method.Client.Main.setmgr;
import static Method.Client.module.ModuleManager.getModuleByName;

@Command.CommandFeatures(description = "Resets a module to factory defaults", Syntax = "Reset <module>")

public class Reset extends Command {

    @Override
    public void runCommand(String s, String[] args) {

        Module m = getModuleByName(args[0]);
        if (m != null) {
            if (m.isToggled())
                m.toggle();

            if (setmgr.getSettingsByMod(m) != null) {
                for (Setting SET : setmgr.getSettingsByMod(m)) {
                    setmgr.getSettings().remove(SET);
                }
                m.setup();
                FileManager.saveData(FileManager.files[4]);
                FileManager.saveData(FileManager.files[8]);
            }
            ModuleManager.addModule(m);
            ChatUtils.message(m + " Returned to Factory");
        }

    }

}