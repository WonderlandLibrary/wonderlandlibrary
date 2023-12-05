package rip.helium.cheat.commands.cmds;

import rip.helium.ChatUtil;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.commands.Command;

import java.util.List;

public class Toggle extends Command {

    public Toggle() {
        super("t", "toggle");
    }

    @Override
    public void run(String[] command) {
        if (command.length == 2) {
            final List<Cheat> cheats = Helium.instance.cheatManager.searchRegistry(command[1]);
            if (cheats.size() < 1 || cheats.get(0) == null) {
                ChatUtil.chat("§cNot a cheat!");
                return;
            }
            if (cheats.size() > 0) {
                final Cheat cheat2 = cheats.get(0);
                cheat2.setState(!cheat2.getState(), true);
                ChatUtil.chat(cheat2.getState() ? ("§aYou have enabled §a" + cheat2.getId()) : ("§cYou have disabled §c" + cheat2.getId()));
            }
        } else {
            ChatUtil.chat("Try: -toggle <mod>");
        }
    }
}
