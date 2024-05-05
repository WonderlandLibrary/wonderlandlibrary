// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.network.Packet;
import vip.Resolute.Resolute;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.events.impl.EventRender2D;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import vip.Resolute.util.font.FontUtil;
import vip.Resolute.util.font.MinecraftFontRenderer;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class Statistics extends Module
{
    public NumberSetting yadd;
    MinecraftFontRenderer fontRenderer;
    int wins;
    int lost;
    public static int kills;
    int flags;
    
    public Statistics() {
        super("Statistics", 0, "Displays ingame statistics", Category.RENDER);
        this.yadd = new NumberSetting("Y Pos", -60.0, -60.0, 180.0, 1.0);
        this.fontRenderer = FontUtil.moon;
        this.wins = 0;
        this.lost = 0;
        this.flags = 0;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventPacket) {
            final EventPacket eventPacket = (EventPacket)e;
            final Packet<?> packet = eventPacket.getPacket();
            if (((EventPacket)e).getPacket() instanceof S08PacketPlayerPosLook) {
                ++this.flags;
            }
            if (Statistics.mc.thePlayer != null && Statistics.mc.thePlayer.ticksExisted >= 0 && packet instanceof S45PacketTitle) {
                if (((S45PacketTitle)packet).getMessage() == null) {
                    return;
                }
                final String message = ((S45PacketTitle)packet).getMessage().getUnformattedText();
                if (message.equals("VICTORY!")) {
                    ++this.wins;
                }
                if (message.equals("YOU DIED!") || message.equals("GAME END") || message.equals("You are now a spectator!")) {
                    ++this.lost;
                }
            }
            if (Statistics.mc.thePlayer != null && Statistics.mc.thePlayer.ticksExisted >= 0 && packet instanceof S02PacketChat) {
                final String look = "killed by " + Statistics.mc.thePlayer.getName();
                final String look2 = "slain by " + Statistics.mc.thePlayer.getName();
                final String look3 = "void while escaping " + Statistics.mc.thePlayer.getName();
                final String look4 = "was killed with magic while fighting " + Statistics.mc.thePlayer.getName();
                final String look5 = "couldn't fly while escaping " + Statistics.mc.thePlayer.getName();
                final String look6 = "fell to their death while escaping " + Statistics.mc.thePlayer.getName();
                final String look7 = "foi morto por " + Statistics.mc.thePlayer.getName();
                final String look8 = "fue asesinado por " + Statistics.mc.thePlayer.getName();
                final String look9 = "fue destrozado a manos de " + Statistics.mc.thePlayer.getName();
                final S02PacketChat s02PacketChat = (S02PacketChat)packet;
                final String cp21 = s02PacketChat.getChatComponent().getUnformattedText();
                if (cp21.contains(look) || cp21.contains(look2) || cp21.contains(look3) || cp21.contains(look4) || cp21.contains(look5) || cp21.contains(look6) || cp21.contains(look7) || cp21.contains(look8) || cp21.contains(look9)) {
                    ++Statistics.kills;
                }
                if ((cp21.contains(Statistics.mc.thePlayer.getName() + "killed by ") && cp21.contains("elimination")) || cp21.contains(Statistics.mc.thePlayer.getName() + " morreu sozinho") || cp21.contains(Statistics.mc.thePlayer.getName() + " foi morto por")) {
                    ++this.lost;
                }
                if (cp21.contains(Statistics.mc.thePlayer.getName() + " venceu a partida!")) {
                    ++this.wins;
                }
            }
        }
        if (e instanceof EventRender2D) {
            final ScaledResolution sr = new ScaledResolution(Statistics.mc);
            Gui.drawRect(sr.getScaledWidth() / 20 - 40, sr.getScaledHeight() / 5 + this.yadd.getValue(), sr.getScaledWidth() / 8 + 30, sr.getScaledHeight() / 2.5 + this.yadd.getValue() - 29.0, -1879048192);
            Gui.drawRect(sr.getScaledWidth() / 20 - 40, sr.getScaledHeight() / 2 + this.yadd.getValue() - 140.0, sr.getScaledWidth() / 8 + 30, sr.getScaledHeight() / 2 + this.yadd.getValue() - 138.5, -1871942548);
            Gui.drawRect(sr.getScaledWidth() / 20 - 40, sr.getScaledHeight() / 5 + this.yadd.getValue() - 0.0, sr.getScaledWidth() / 8 + 30, sr.getScaledHeight() / 5 + this.yadd.getValue() - 1.0, -16716033);
            FontUtil.moon.drawString("Session Information", sr.getScaledWidth() / 16.0f - 50.0f, (float)(sr.getScaledHeight() / 5.0f + 1.0f + this.yadd.getValue()), -1);
            FontUtil.moon.drawString("Session Time: " + this.formatTime(Resolute.sessionTime.elapsed()), sr.getScaledWidth() / 16.0f - 50.0f, (float)(sr.getScaledHeight() / 5.0f + 17.0f + this.yadd.getValue()), -1);
            FontUtil.moon.drawString("Bad Config: True", sr.getScaledWidth() / 16.0f - 50.0f, (float)(sr.getScaledHeight() / 5.0f + 28.0f + this.yadd.getValue()), -1);
            FontUtil.moon.drawString("Kills: " + Statistics.kills, sr.getScaledWidth() / 16.0f - 50.0f, (float)(sr.getScaledHeight() / 5.0f + 39.0f + this.yadd.getValue()), -1);
            FontUtil.moon.drawString("Won: " + this.wins, sr.getScaledWidth() / 16.0f - 50.0f, (float)(sr.getScaledHeight() / 5.0f + 50.0f + this.yadd.getValue()), -1);
            FontUtil.moon.drawString("Lost: " + this.lost, sr.getScaledWidth() / 16.0f - 50.0f, (float)(sr.getScaledHeight() / 5.0f + 61.0f + this.yadd.getValue()), -1);
        }
    }
    
    private String formatTime(long time) {
        time /= 1000L;
        return String.format("%d:%02d", time / 60L, time % 60L);
    }
    
    static {
        Statistics.kills = 0;
    }
}
