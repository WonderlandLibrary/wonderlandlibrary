package com.enjoytheban.module.modules.player;

import net.minecraft.network.play.server.S02PacketChat;

import java.awt.*;

import com.enjoytheban.Client;
import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.Type;
import com.enjoytheban.api.events.misc.EventChat;
import com.enjoytheban.api.events.world.EventPacketRecieve;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

/**
 * @author Noil
 * Credits to Latch
 */

public class AutoAccept extends Module {

    public AutoAccept() {
        super("AutoAccept", new String[] {"TPAccept, autotp"}, ModuleType.Player);
        setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
    }

    @EventHandler
    private void onPacket(EventChat e) {
        if (e.getType() == Type.PRE) {
            String message = e.getMessage();
            if (this.gotTpaRequest(message)) {
                this.handleRequest(message, "/tpaccept", "Accepted teleport");
            }
            if (this.gotPartyRequest(message)) {
                this.handleRequest(message, "/party accept", "Accepted party invite");
            }
            if (this.gotFactionRequest(message)) {
                this.handleRequest(message, "/f join", "Accepted faction invitation");
            }
        }
    }

    private void handleRequest(String message, String messageToSend, String notificationMessage) {
        final Object o;
        FriendManager.getFriends().forEach((friends, alias) -> {
            if (FriendManager.isFriend(FriendManager.getAlias(friends)) && message.contains(FriendManager.getAlias(friends))) {
                mc.thePlayer.sendChatMessage(messageToSend + " " + FriendManager.getAlias(friends));
            }
        });
    }

    private boolean gotTpaRequest(String message) {
        message = message.toLowerCase();
            return message.contains("has requested to teleport") || message.contains("to teleport to you") || (message.contains("has requested that you teleport to them"));
    }

    private boolean gotFactionRequest(String message) {
        message = message.toLowerCase();
        return message.contains("has invited you to join") && !message.contains("party");
    }

    private boolean gotPartyRequest(String message) {
        message = message.toLowerCase();
        return message.contains("has invited you to join their party");
    }
}
