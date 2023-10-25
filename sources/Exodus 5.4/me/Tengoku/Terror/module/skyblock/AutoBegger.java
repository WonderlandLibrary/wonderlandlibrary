/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.skyblock;

import java.util.Random;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventPacket;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;

public class AutoBegger
extends Module {
    Timer timerChat;
    Timer timerPacket;
    Timer switchTimer;
    Timer timerChest = new Timer();
    int count;
    boolean canSwitch = true;
    Timer hubTimer;
    Timer timerSwitchHub;

    @EventTarget
    public void onPre(EventMotion eventMotion) {
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (eventPacket.getPacket() instanceof S02PacketChat) {
            S02PacketChat s02PacketChat = (S02PacketChat)eventPacket.getPacket();
            if (s02PacketChat.getChatComponent().getUnformattedText().contains("Warping you to your SkyBlock island...")) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage("/hub"));
                System.out.println("asgasg");
            }
            if (s02PacketChat.getChatComponent().getUnformattedText().contains("has sent you a trade request.") && this.timerPacket.hasTimeElapsed(100L, true)) {
                String string = s02PacketChat.getChatComponent().getUnformattedText();
                string = string.replace("\ufffdahas sent you a trade request. \ufffdbClick here \ufffdato accept!", "");
                System.out.println(string);
                string = string.replace("\ufffda", "");
                string = string.replace("\ufffdb", "");
                string = string.replace("\ufffd6", "");
                string = string.replace("\ufffd8", "");
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage("/trade " + string));
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.count = 0;
    }

    public String randomString() {
        int n = 97;
        int n2 = 122;
        int n3 = 10;
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(n3);
        int n4 = 0;
        while (n4 < n3) {
            int n5 = n + (int)(random.nextFloat() * (float)(n2 - n + 1));
            stringBuilder.append((char)n5);
            ++n4;
        }
        String string = stringBuilder.toString();
        return string;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        Random random = new Random();
        if (this.canSwitch) {
            Minecraft.gameSettings.keyBindForward.pressed = true;
        }
        int n = (int)(Math.random() * 52.0);
        String string = "abcdefghijklmnopqrstuvwxyz";
        if (Minecraft.thePlayer != null) {
            if (Minecraft.thePlayer.openContainer != null) {
                if (Minecraft.thePlayer.openContainer instanceof ContainerChest) {
                    ContainerChest containerChest = (ContainerChest)Minecraft.thePlayer.openContainer;
                    if (this.timerChest.hasTimeElapsed(300L, true)) {
                        Minecraft.playerController.windowClick(containerChest.windowId, 39, 0, 0, Minecraft.thePlayer);
                    }
                }
            }
        }
        if (this.timerChat.hasTimeElapsed(20000L, true)) {
            this.canSwitch = true;
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage("can anyone give me 100k at map pls i just got scammed " + this.randomString() + this.randomString()));
        }
        if (this.switchTimer.hasTimeElapsed(40000L, true) && this.canSwitch) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage("/is"));
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage("/hub"));
            Minecraft.gameSettings.keyBindForward.pressed = true;
        }
    }

    public AutoBegger() {
        super("AutoBegger", 0, Category.SKYBLOCK, "Free money.");
        this.timerChat = new Timer();
        this.timerPacket = new Timer();
        this.timerSwitchHub = new Timer();
        this.switchTimer = new Timer();
        this.hubTimer = new Timer();
    }
}

