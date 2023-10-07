package best.azura.client.impl.command.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.command.ACommand;
import best.azura.client.util.other.ChatUtil;
import best.azura.client.util.player.MovementUtil;

public class VClipCommand extends ACommand {

    @Override
    public String getName() {
        return "vclip";
    }

    @Override
    public String getDescription() {
        return "Clip vertically";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void handleCommand(String[] args) {
        if (args.length != 1) {
            ChatUtil.sendError("Please use " + Client.INSTANCE.getCommandManager().prefix + getName() + " <height>");
        } else {
            double height;
            try {
                height = Double.parseDouble(args[0]);
            } catch (NumberFormatException e) {
                height = 0;
                ChatUtil.sendError("String " + args[0] + " is not a number!");
            } catch (Exception e) {
                height = 0;
                ChatUtil.sendError("An error occurred: " + e.getMessage());
            }
            MovementUtil.vClip(height);
        }
    }
}