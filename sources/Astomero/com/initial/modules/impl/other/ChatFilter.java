package com.initial.modules.impl.other;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import net.minecraft.network.play.client.*;
import java.util.*;
import com.initial.events.*;

public class ChatFilter extends Module
{
    public ModeSet mode;
    
    public ChatFilter() {
        super("ChatFilter", 0, Category.OTHER);
        this.mode = new ModeSet("Mode", "Bypass", new String[] { "Bypass", "Chinese" });
        this.addSettings(this.mode);
    }
    
    @EventTarget
    public void onSend(final EventSendPacket event) {
        this.setDisplayName("Chat Filter §7" + this.mode.getMode());
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Bypass": {
                if (event.getPacket() instanceof C01PacketChatMessage) {
                    final C01PacketChatMessage p = (C01PacketChatMessage)event.getPacket();
                    String finalmsg = "";
                    final ArrayList<String> splitshit = new ArrayList<String>();
                    final String[] msg = p.getMessage().split(" ");
                    for (int i = 0; i < msg.length; ++i) {
                        final char[] characters = msg[i].toCharArray();
                        for (int i2 = 0; i2 < characters.length; ++i2) {
                            splitshit.add(characters[i2] + "\u061c");
                        }
                        splitshit.add(" ");
                    }
                    for (int i = 0; i < splitshit.size(); ++i) {
                        finalmsg += splitshit.get(i);
                    }
                    p.setMessage(finalmsg.replaceFirst("%", ""));
                    splitshit.clear();
                    break;
                }
                break;
            }
        }
    }
}
