package com.masterof13fps.features.commands.impl;

import com.masterof13fps.Client;
import com.masterof13fps.features.commands.Command;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class Say extends Command {

    String syntax = Client.main().getClientPrefix() + "say <Message>";

    @Override
    public void execute(String[] args) {
        if (args.length != 0) {
            final StringBuilder message = new StringBuilder();
            for (String arg : args) {
                message.append(arg.concat(" "));
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(message.toString()));
        }else{
            notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
        }
    }

    public Say() {
        super("say", "shout");
    }
}
