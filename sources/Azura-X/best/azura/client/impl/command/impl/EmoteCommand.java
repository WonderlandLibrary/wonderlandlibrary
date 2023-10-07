package best.azura.client.impl.command.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.command.ACommand;
import best.azura.client.impl.module.impl.render.Emotes;
import best.azura.client.util.other.ChatUtil;
import best.azura.client.util.other.EmoteRequest;
import net.minecraft.client.Minecraft;

public class EmoteCommand extends ACommand {

    @Override
    public String getName() {
        return "emote";
    }

    @Override
    public String getDescription() {
        return "Invoke an emote";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void handleCommand(String[] args) {
        if (args.length != 1) {
            ChatUtil.sendError("Please use " + Client.INSTANCE.getCommandManager().prefix + getName() + " <emote>");
        } else {
            if (args[0].equalsIgnoreCase("list")) {
                ChatUtil.sendChat("dab, hug, facepalm, yes, no, creepy, jerk, tpose, wave, point, idiot");
            } else if (args[0].equalsIgnoreCase("stop")) {
                Emotes.clearEmotes();
            } else {
                Emotes.addToQueue(new EmoteRequest(Minecraft.getMinecraft().getSession().getUsername(), args[0]), true);
                ChatUtil.sendChat("Queued emote " + args[0] + ".");
            }
        }
    }
}