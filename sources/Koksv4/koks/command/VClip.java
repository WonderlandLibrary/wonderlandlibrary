package koks.command;

import koks.api.registry.command.Command;

@Command.Info(name = "vclip", description = "You can clip through walls")
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
