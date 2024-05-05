package rip.helium.cheat.commands.cmds;

import org.lwjgl.input.Keyboard;
import rip.helium.ChatUtil;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.commands.Command;

import java.util.List;

public class Bind extends Command {

    public Bind() {
        super("bind", "b");
    }

    @Override
    public void run(String[] command) {
        if (command.length == 3) {
            final List<Cheat> cheats = Helium.instance.cheatManager.searchRegistry(command[1]);
            final int keyCode = Keyboard.getKeyIndex(command[2].toUpperCase());
            if (cheats.size() < 1 || cheats.get(0) == null) {
                ChatUtil.chat("Could not find module '" + command[1] + "'");
                return;
            }

            /*/
            We're doing the thing below to prevent waterspeed being toggled instead of speed.
             */

            if (command[1].equalsIgnoreCase("speed")) {
                final Cheat cheat = Helium.instance.cheatManager.getCheatRegistry().get("Speed");
                cheat.setBind(keyCode);
                ChatUtil.chat("§cSuccessfully set keybind for Speed to " + Keyboard.getKeyName(keyCode));
                return;
            }

            if (cheats.size() > 0) {
                final Cheat cheat = cheats.get(0);
                cheat.setBind(keyCode);
                ChatUtil.chat("§cSuccessfully set keybind for " + cheat.getId() + " to " + Keyboard.getKeyName(keyCode));
            }
        } else {
            ChatUtil.chat("Invalid Syntax. Try .bind <module> <key>");
        }
    }
}
