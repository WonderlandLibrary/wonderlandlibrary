package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.other.ChatUtils;

public class VClip extends Command {
    public VClip() {
        super("VClip", "Teleports you vertically <offset> blocks away.", "vclip <offset>");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length == 1) {
            try {
                double offset = Double.parseDouble(args[0]);
                MovementUtils.vClip(offset);
                ChatUtils.println("§9VClipped " + offset + " blocks.");
            } catch (NumberFormatException e) {
                ChatUtils.println("§4Invalid offset.");
            }
            return;
        }

        sendSyntaxHelpMessage();
    }
}
