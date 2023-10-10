package Reality.Realii.commands.commands;

import Reality.Realii.Client;
import Reality.Realii.commands.Command;
import Reality.Realii.utils.cheats.player.Helper;

public class Config extends Command {
    public Config() {
        super("config", new String[]{}, "", "save client config.");
    }

    @Override
    public String execute(String[] var1) {
        if (var1.length < 1){
            Helper.sendMessage(".config <load/save>");
        }

        if(var1[0].equals("load")){
            Client.instance.getModuleManager().readSettings();
            Helper.sendMessage("Loaded");

        }else {
            Client.instance.getModuleManager().saveSettings();
            Helper.sendMessage("Saved");
        }
        return null;
    }
}
