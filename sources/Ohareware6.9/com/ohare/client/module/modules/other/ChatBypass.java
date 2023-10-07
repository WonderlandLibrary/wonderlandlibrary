package com.ohare.client.module.modules.other;

import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.network.play.client.C01PacketChatMessage;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Xen for OhareWare
 * @since 8/7/2019
 **/
public class ChatBypass extends Module {

    public ChatBypass() {
        super("ChatBypass", Category.OTHER,  new Color(255, 255, 0, 255).getRGB());
        setRenderlabel("Chat Bypass");
        setDescription("nigger bypass");
    }


    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.isSending() && event.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage p = (C01PacketChatMessage) event.getPacket();
            String finalmsg = "";
            ArrayList<String> splitshit = new ArrayList();
            String[] msg = p.getMessage().split(" ");
            for (int i = 0; i < msg.length; i++) {
                char[] characters = msg[i].toCharArray();
                for (int i2 = 0; i2 < characters.length; i2++) {
                    splitshit.add(characters[i2] + "\u061C");
                }
                splitshit.add(" ");
            }
            for (int i = 0; i < splitshit.size(); i++) {
                finalmsg += splitshit.get(i);
            }
            if (p.getMessage().startsWith("%")) {
                p.setMessage(finalmsg.replaceFirst("%", ""));
                splitshit.clear();
            }
        }
    }
}
