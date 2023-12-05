package rip.helium.cheat.commands.cmds;

import rip.helium.ChatUtil;
import rip.helium.Helium;
import rip.helium.cheat.commands.Command;

public class Help extends Command {

    public Help() {
        super("help", "?");
    }

    @Override
    public void run(String[] args) {
        ChatUtil.chat("§7§m----------------------------------------");
        ChatUtil.chat("§c§lHelium §7- §f" + Helium.client_build);
        ChatUtil.chat("§7§oDeveloped by Shotbowxd & Jinthium!");
        ChatUtil.chat(" ");
        ChatUtil.chat("§c§lCommands:");
        ChatUtil.chat("§c-friend §7- Friends a player so killaura doesnt attack them");
        ChatUtil.chat("§c-vclip §7- Clips you down or up");
        ChatUtil.chat("§c-tp §7- Teleports you to a certain place");
        ChatUtil.chat("§c-bind §7- Binds a module");
        ChatUtil.chat("§c-clientname §7- Changes the client name");
        ChatUtil.chat("§c-toggle or -t §7- Toggles a module");
        ChatUtil.chat("§c-focus §7- Focuses a player more than others");
        ChatUtil.chat("§7§m----------------------------------------");
    }
}
