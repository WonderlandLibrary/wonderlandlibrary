package rip.helium.cheat.commands.cmds;

import rip.helium.ChatUtil;
import rip.helium.Helium;
import rip.helium.cheat.commands.Command;

public class Focus extends Command {

    public Focus() {
        super("focus", "focusplayer");
    }

    @Override
    public void run(String[] args) {
        String name = args[1];
        if (!Helium.instance.focusManager.isFocused(name)) {
            Helium.instance.focusManager.addFocus(name);
            ChatUtil.chat("§aAdded " + name + " to focused players!");
        } else {
            Helium.instance.focusManager.removeFocus(name);
            ChatUtil.chat("§cRemoved " + name + " from focused players!");
        }
    }
}
