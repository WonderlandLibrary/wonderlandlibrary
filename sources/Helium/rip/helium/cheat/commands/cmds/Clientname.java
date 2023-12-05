package rip.helium.cheat.commands.cmds;

import rip.helium.ChatUtil;
import rip.helium.Helium;
import rip.helium.cheat.commands.Command;
import rip.helium.cheat.impl.visual.Console;

import java.util.StringJoiner;

public class Clientname extends Command {

    public Clientname() {
        super("clientname", "name");
    }

    @Override
    public void run(String[] args) {
        String fixedName;
        StringJoiner joiner = new StringJoiner(" ");
        for (int i = 1; i < args.length; i++) {
            joiner.add(args[i]);
        }
        fixedName = joiner.toString();
        fixedName = fixedName.replaceAll("&", "§");
        fixedName = fixedName.replaceAll("%name%", mc.thePlayer.getName());
        fixedName = fixedName.replaceAll("%clientuser%", Helium.clientUser);
        Helium.getClient_name = fixedName;
        ChatUtil.chat("§aSuccessfully set the client name to '" + Helium.getClient_name + ".");
    }
}
