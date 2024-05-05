package com.initial.modules.impl.other;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import net.minecraft.network.play.server.*;
import com.initial.events.*;
import java.util.concurrent.*;

public class Killsults extends Module
{
    public ModeSet mode;
    
    public Killsults() {
        super("KillSults", 0, Category.OTHER);
        this.mode = new ModeSet("Mode", "BlocksMC", new String[] { "BlocksMC", "Redesky", "MC-Central", "Mineplex", "Hypixel" });
        this.addSettings(this.mode);
    }
    
    @EventTarget
    public void onReceive(final EventReceivePacket event) {
        if (event.getPacket() instanceof S02PacketChat) {
            final S02PacketChat packet = (S02PacketChat)event.getPacket();
            final String message = packet.getChatComponent().getUnformattedText();
            final String mode = this.mode.getMode();
            switch (mode) {
                case "MC-Central": {
                    if (message.contains(this.mc.thePlayer.getName()) && message.contains("Has Killed")) {
                        this.sendMsg();
                        break;
                    }
                    break;
                }
                case "BlocksMC": {
                    if (message.contains(this.mc.thePlayer.getName()) && message.contains("was killed")) {
                        this.sendMsg();
                        break;
                    }
                    break;
                }
                case "Mineplex": {
                    if (message.contains(this.mc.thePlayer.getName()) && (message.contains("killed by") || message.contains("thrown into the void"))) {
                        this.sendMsg();
                        break;
                    }
                    break;
                }
                case "Hypixel": {
                    if (message.contains(this.mc.thePlayer.getName()) && message.contains("was Killed by")) {
                        this.sendMsg();
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    public void sendMsg() {
        final String[] niggaSults = { "Your step sis is calling brb", "Go drown in your own salt.", "Kids like you are the inspiration of birth control.", "Your brain is a fucking fax machine", "Wait you guys cant fly?", "I dont care if im cheating in a block game, All i care is that you died in a fucking block game.", "Toxic nigga", "You just got killed by a free client...", "Go back to roblox where you belong, you degenerate 6 year old kid", "jew", "Why the fuck are u using skidma, we are in 2021 not 2019", "This is 2020, Astomero > skidma!", "You rush because youre bad, you want the satisfaction of one kill before dying to a noob.", "Nigga balls go kis", "Yo ass jealous of how much shit is getting out ur mouth man", "Astomero got too much of a nut to fuck fdp rn", "Dont ever underestimate the power of Astomero", "Astomero hijacked ur mama card", "Wow, you died to a fucking free client" };
        final int randomIndex = ThreadLocalRandom.current().nextInt(0, niggaSults.length);
        this.mc.thePlayer.sendChatMessage(niggaSults[randomIndex]);
    }
}
