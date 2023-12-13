package summer.base.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import summer.Summer;
import summer.base.manager.config.Cheats;
import summer.base.manager.Command;
import summer.base.utilities.ChatUtils;

public class BindCommand implements Command {

    @Override
    public boolean run(String[] args) {

        if (args.length == 4) {
            Cheats m = Summer.INSTANCE.cheatManager.getModuleByName(args[2]);
            if (args[1].equalsIgnoreCase("add") && m != null) {
                m.setKey(Keyboard.getKeyIndex(args[3].toUpperCase()));
                ChatUtils.sendMessage(m.getName() + " has been bound to " + Keyboard.getKeyName(m.getKey()) + ".");
                return true;
            }
        } else if (args.length == 3) {
            Cheats m = Summer.INSTANCE.cheatManager.getModuleByName(args[2]);
            if (args[1].equalsIgnoreCase("del") && m != null) {
                m.setKey(Keyboard.KEY_NONE);
                ChatUtils.sendMessage("Unbound " + m.getName() + ".");
                return true;
            }
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("clear")) {
                Summer.INSTANCE.cheatManager.getModuleList().forEach(module -> module.setKey(Keyboard.KEY_NONE));
                ChatUtils.sendMessage("All binds have been cleared.");
                return true;
            }
        }


        return false;
    }

    @Override
    public String usage() {
        return ChatFormatting.WHITE + "b | bind <add/del/clear> <module> <key>";
    }
}