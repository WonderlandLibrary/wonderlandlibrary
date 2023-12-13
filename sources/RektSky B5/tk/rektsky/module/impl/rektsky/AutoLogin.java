/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.rektsky;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class AutoLogin
extends Module {
    private int ticks = 0;
    private String password = "H@cker";

    public AutoLogin() {
        super("AutoLogin", "Auto login(include Login/Register/Verify) to Redesky", 0, Category.REKTSKY);
        this.toggle();
    }

    @Override
    public void onEvent(Event e2) {
        this.mc.ingameGUI.subtitle = "";
        this.mc.ingameGUI.title = "";
        try {
            Packet<?> p2;
            if (e2 instanceof PacketSentEvent && (p2 = ((PacketSentEvent)e2).getPacket()) instanceof C0EPacketClickWindow) {
                C0EPacketClickWindow c0EPacketClickWindow = (C0EPacketClickWindow)((PacketSentEvent)e2).getPacket();
            }
            if (e2 instanceof PacketReceiveEvent) {
                p2 = ((PacketReceiveEvent)e2).getPacket();
                if (p2 instanceof S45PacketTitle) {
                    S45PacketTitle packet = (S45PacketTitle)p2;
                    if (packet.getType() == S45PacketTitle.Type.TITLE && packet.getMessage().getUnformattedText().equals("\u00a7a\u00a7lAutenticado!")) {
                        ((PacketReceiveEvent)e2).setCanceled(true);
                        return;
                    }
                    if (packet.getType() == S45PacketTitle.Type.SUBTITLE && packet.getMessage().getUnformattedText().equals("\u00a7fRedirecionando...")) {
                        ((PacketReceiveEvent)e2).setCanceled(true);
                        return;
                    }
                }
                if (p2 instanceof S02PacketChat) {
                    // empty if block
                }
            }
            if (e2 instanceof WorldTickEvent) {
                ++this.ticks;
                if (this.ticks % 1 == 0) {
                    if (this.mc.ingameGUI.subtitle.contains("/login")) {
                        this.mc.thePlayer.sendChatMessage("/login " + this.password);
                        this.mc.ingameGUI.subtitle = "\u00a7rfJoining...";
                        this.mc.ingameGUI.title = "\u00a7a\u00a7lRektSky AutoLogin";
                    }
                    if (this.mc.ingameGUI.subtitle.contains("/register")) {
                        this.mc.thePlayer.sendChatMessage("/register " + this.password + " " + this.password);
                        this.mc.ingameGUI.subtitle = "\u00a7rJoining...";
                        this.mc.ingameGUI.title = "\u00a7a\u00a7lRektSky AutoLogin";
                    }
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public void onEnable() {
    }
}

