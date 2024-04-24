package me.crimp.claudius.mod.command.commands;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.modules.misc.McfSync;

public class addsyncCommand extends Command {
    public addsyncCommand() {
        super("Add", new String[]{"<name>"});
    }
    public static String Uno = McfSync.INSTANCE.syncadd1.getPlannedValue() + " add";
    public static String dos = McfSync.INSTANCE.syncadd2.getPlannedValue() + " add";
    public static String tres = McfSync.INSTANCE.syncadd3.getPlannedValue() + " add";

    @Override
    public void execute(String[] commands) {
        claudius.friendManager.addFriend(commands[0]);
        if (McfSync.INSTANCE.one.getValue().equals(true)) {
            mc.player.sendChatMessage(Uno + " " + commands[0]);
        }
        if (McfSync.INSTANCE.two.getValue().equals(true)) {
            mc.player.sendChatMessage(dos + " " + commands[0]);
        }
    }
}

