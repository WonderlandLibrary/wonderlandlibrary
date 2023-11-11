package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.other.ChatUtils;

public class HClip extends Command {
    public HClip() {
        super("HClip", "Teleports you horizontally <offset> blocks away.", "hclip <offset>");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length == 1) {
            try {
                double offset = Double.parseDouble(args[0]);
                MovementUtils.hClip(offset);
                ChatUtils.println("§9HClipped " + offset + " blocks.");
            } catch (NumberFormatException e) {
                ChatUtils.println("§4Invalid offset.");
            }
            return;
        }

        sendSyntaxHelpMessage();
    }
}
