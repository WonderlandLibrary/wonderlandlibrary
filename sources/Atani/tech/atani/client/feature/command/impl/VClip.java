package tech.atani.client.feature.command.impl;

import tech.atani.client.feature.command.Command;
import tech.atani.client.feature.command.data.CommandInfo;

@CommandInfo(name = "vclip", description = "clip vertically")
public class VClip extends Command {
    @Override
    public boolean execute(String[] args) {
        if (args.length == 1) {
            try {
                setPosition(getX(), getY() + Double.parseDouble(args[0]), getZ());
            } catch (NumberFormatException e) {
                sendMessage("§cPlease write a number!");
            }
            return true;
        } else
            return false;
    }
}