package me.crimp.claudius.mod.command.commands;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.modules.misc.McfSync;

public class delsyncCommand extends Command {
    public delsyncCommand() {
        super("del", new String[]{"<name>"});
    }
    public static String Uno = McfSync.INSTANCE.syncdel1.getPlannedValue() + " del";
    public static String dos = McfSync.INSTANCE.syncdel2.getPlannedValue() + " del";
    public static String tres = McfSync.INSTANCE.syncdel3.getPlannedValue() + " del";;

    @Override
    public void execute(String[] commands) {
        claudius.friendManager.removeFriend(commands[0]);
        if (McfSync.INSTANCE.one.getValue().equals(true)) {
            mc.player.sendChatMessage(Uno + " " + commands[0]);
        }
        if (McfSync.INSTANCE.two.getValue().equals(true)) {
            mc.player.sendChatMessage(dos + " " + commands[0]);
        }
        if (McfSync.INSTANCE.three.getValue().equals(true)) {
            mc.player.sendChatMessage(tres + " " + commands[0]);
        }

    }
}

