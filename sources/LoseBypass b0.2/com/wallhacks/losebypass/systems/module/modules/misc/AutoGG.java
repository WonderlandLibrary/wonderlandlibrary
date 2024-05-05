/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.misc;

import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.PacketReceiveEvent;
import com.wallhacks.losebypass.systems.module.Module;
import java.util.Arrays;
import java.util.List;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.EnumChatFormatting;

@Module.Registration(name="AutoGG", description="Sends gg every time a game is over")
public class AutoGG
extends Module {
    long last;
    private final List<String> endingStrings = Arrays.asList("1st Killer - ", "1st Place - ", "Winner: ", " - Damage Dealt - ", "Winning Team - ", "1st - ", "Winners: ", "Winner: ", "Winning Team: ", " won the game!", "Top Seeker: ", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - ");

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!(event.getPacket() instanceof S02PacketChat)) return;
        S02PacketChat p = (S02PacketChat)event.getPacket();
        String stripped = EnumChatFormatting.getTextWithoutFormattingCodes(p.getChatComponent().getFormattedText());
        if (!this.isEndOfGame(stripped)) return;
        if (System.currentTimeMillis() - this.last <= 1000L) return;
        this.last = System.currentTimeMillis();
        AutoGG.mc.thePlayer.sendChatMessage("gg");
    }

    private boolean isEndOfGame(String message) {
        return this.endingStrings.stream().anyMatch(message::contains);
    }
}

