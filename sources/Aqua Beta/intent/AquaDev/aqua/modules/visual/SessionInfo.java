package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventPacket;
import events.listeners.EventPostRender2D;
import events.listeners.EventRender2D;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.visual.Arraylist;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.modules.visual.Shadow;
import intent.AquaDev.aqua.utils.RenderUtil;
import java.awt.Color;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.StringJoiner;
import java.util.TimeZone;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

public class SessionInfo
extends Module {
    private int kills = 0;
    private int deaths = 0;

    public SessionInfo() {
        super("SessionInfo", "SessionInfo", 0, Category.Visual);
        Aqua.setmgr.register(new Setting("PosY", (Module)this, 25.0, 0.0, 440.0, true));
        Aqua.setmgr.register(new Setting("PosX", (Module)this, 4.0, 0.0, 840.0, true));
        Aqua.setmgr.register(new Setting("ConnectedPosX", (Module)this, true));
        Aqua.setmgr.register(new Setting("Mode", (Module)this, "Glow", new String[]{"Glow", "Shadow"}));
        Aqua.setmgr.register(new Setting("InfoMode", (Module)this, "Classic", new String[]{"Classic", "Modern"}));
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public static LocalTime getLocalTime(long time) {
        return LocalDateTime.ofInstant((Instant)Instant.ofEpochMilli((long)time), (ZoneId)TimeZone.getDefault().toZoneId()).toLocalTime();
    }

    public void onEvent(Event e) {
        S02PacketChat chat;
        String text;
        Packet packet;
        if (e instanceof EventPacket && (packet = EventPacket.getPacket()) instanceof S02PacketChat && (text = (chat = (S02PacketChat)packet).getChatComponent().getUnformattedText()).contains((CharSequence)SessionInfo.mc.thePlayer.getName())) {
            for (Entity entity : SessionInfo.mc.theWorld.loadedEntityList) {
                if (!(entity instanceof EntityPlayer) || entity instanceof EntityPlayerSP || !text.contains((CharSequence)entity.getName())) continue;
                boolean kill = false;
                String[] s = text.split(" ");
                for (int i = 0; i < s.length - 1; ++i) {
                    if (s[i].equals((Object)SessionInfo.mc.thePlayer.getName())) {
                        kill = false;
                        break;
                    }
                    if (!s[i].equals((Object)entity.getName())) continue;
                    kill = true;
                    break;
                }
                if (kill) {
                    ++this.kills;
                }
                boolean death = false;
                for (int i = 0; i < s.length - 1; ++i) {
                    if (s[i].equals((Object)SessionInfo.mc.thePlayer.getName())) {
                        death = true;
                        break;
                    }
                    if (!s[i].equals((Object)entity.getName())) continue;
                    death = false;
                    break;
                }
                if (!death) continue;
                ++this.deaths;
            }
        }
        if (e instanceof EventRender2D) {
            if (Aqua.setmgr.getSetting("SessionInfoInfoMode").getCurrentMode().equalsIgnoreCase("Classic")) {
                this.drawRectShaders();
            }
            if (Aqua.setmgr.getSetting("SessionInfoInfoMode").getCurrentMode().equalsIgnoreCase("Modern")) {
                this.drawRectShadersModern();
            }
        }
        if (e instanceof EventPostRender2D) {
            if (Aqua.setmgr.getSetting("SessionInfoInfoMode").getCurrentMode().equalsIgnoreCase("Classic")) {
                this.drawRects();
                this.drawStrings();
            }
            if (Aqua.setmgr.getSetting("SessionInfoInfoMode").getCurrentMode().equalsIgnoreCase("Modern")) {
                this.drawRectsModern();
                this.drawStringsModern();
            }
        }
    }

    public void drawStrings() {
        float posX = Aqua.setmgr.getSetting("SessionInfoConnectedPosX").isState() ? 4.0f : (float)Aqua.setmgr.getSetting("SessionInfoPosX").getCurrentNumber();
        float posY = (float)Aqua.setmgr.getSetting("SessionInfoPosY").getCurrentNumber();
        StringJoiner joiner = new StringJoiner((CharSequence)", ");
        Duration duration = Duration.between((Temporal)SessionInfo.getLocalTime(Aqua.INSTANCE.lastConnection), (Temporal)SessionInfo.getLocalTime(System.currentTimeMillis()));
        long seconds = duration.getSeconds();
        long nano = 0L;
        if (seconds <= 0L) {
            nano = duration.getNano();
        }
        if (nano <= 0L) {
            long minutes = seconds / 60L;
            long hours = minutes / 60L;
            if (hours > 0L) {
                joiner.add((CharSequence)(hours + "h"));
            }
            while (minutes > 60L) {
                minutes -= 60L;
            }
            if (minutes > 0L) {
                joiner.add((CharSequence)(minutes + "min"));
            }
            while (seconds > 60L) {
                seconds -= 60L;
            }
            if (seconds > 0L) {
                joiner.add((CharSequence)(seconds + "s"));
            }
        } else if (nano > 1000000L) {
            joiner.add((CharSequence)(nano / 1000000L + "ms"));
        } else {
            joiner.add((CharSequence)(nano + "ns"));
        }
        Aqua.INSTANCE.comfortaa3.drawString("Session Info", posX + 28.0f, posY + 2.0f, Aqua.setmgr.getSetting("HUDColor").getColor());
        Aqua.INSTANCE.comfortaa4.drawString("Kills : " + this.kills, posX + 1.0f, posY + 15.0f, -1);
        Aqua.INSTANCE.comfortaa4.drawString("Deaths : " + this.deaths, posX + 1.0f, posY + 25.0f, -1);
        Aqua.INSTANCE.comfortaa4.drawString("Playtime : " + joiner, posX + 1.0f, posY + 35.0f, -1);
        Aqua.INSTANCE.comfortaa4.drawString("KD : " + this.kills / (this.deaths == 0 ? 1 : this.deaths), posX + 1.0f, posY + 45.0f, -1);
        Arraylist.drawGlowArray(() -> Aqua.INSTANCE.comfortaa3.drawString("Session Info", posX + 27.0f, posY + 2.0f, Aqua.setmgr.getSetting("HUDColor").getColor()), (boolean)false);
    }

    public void drawRectShaders() {
        float posX = Aqua.setmgr.getSetting("SessionInfoConnectedPosX").isState() ? 4.0f : (float)Aqua.setmgr.getSetting("SessionInfoPosX").getCurrentNumber();
        float posY = (float)Aqua.setmgr.getSetting("SessionInfoPosY").getCurrentNumber();
        int width = 120;
        int height = 57;
        int cornerRadios = 4;
        if (Aqua.moduleManager.getModuleByName("Blur").isToggled()) {
            Blur.drawBlurred(() -> RenderUtil.drawRoundedRect2Alpha((double)posX, (double)posY, (double)width, (double)height, (double)cornerRadios, (Color)new Color(0, 0, 0, 80)), (boolean)false);
        }
        if (Aqua.moduleManager.getModuleByName("Arraylist").isToggled() && Aqua.setmgr.getSetting("SessionInfoMode").getCurrentMode().equalsIgnoreCase("Glow")) {
            Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect((double)posX, (double)posY, (double)width, (double)height, (double)cornerRadios, (int)Aqua.setmgr.getSetting("HUDColor").getColor()), (boolean)false);
        }
        if (Aqua.moduleManager.getModuleByName("Shadow").isToggled() && Aqua.setmgr.getSetting("SessionInfoMode").getCurrentMode().equalsIgnoreCase("Shadow")) {
            Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)posX, (double)posY, (double)width, (double)height, (double)cornerRadios, (int)Color.black.getRGB()), (boolean)false);
        }
    }

    public void drawRects() {
        float posX = Aqua.setmgr.getSetting("SessionInfoConnectedPosX").isState() ? 4.0f : (float)Aqua.setmgr.getSetting("SessionInfoPosX").getCurrentNumber();
        float posY = (float)Aqua.setmgr.getSetting("SessionInfoPosY").getCurrentNumber();
        int width = 120;
        int height = 57;
        int cornerRadios = 4;
        RenderUtil.drawRoundedRect2Alpha((double)posX, (double)posY, (double)width, (double)height, (double)cornerRadios, (Color)new Color(0, 0, 0, 60));
    }

    public void drawStringsModern() {
        float posX = Aqua.setmgr.getSetting("SessionInfoConnectedPosX").isState() ? 4.0f : (float)Aqua.setmgr.getSetting("SessionInfoPosX").getCurrentNumber();
        float posY = (float)Aqua.setmgr.getSetting("SessionInfoPosY").getCurrentNumber();
        StringJoiner joiner = new StringJoiner((CharSequence)", ");
        Duration duration = Duration.between((Temporal)SessionInfo.getLocalTime(Aqua.INSTANCE.lastConnection), (Temporal)SessionInfo.getLocalTime(System.currentTimeMillis()));
        long seconds = duration.getSeconds();
        long nano = 0L;
        if (seconds <= 0L) {
            nano = duration.getNano();
        }
        if (nano <= 0L) {
            long minutes = seconds / 60L;
            long hours = minutes / 60L;
            if (hours > 0L) {
                joiner.add((CharSequence)(hours + "h"));
            }
            while (minutes > 60L) {
                minutes -= 60L;
            }
            if (minutes > 0L) {
                joiner.add((CharSequence)(minutes + "min"));
            }
            while (seconds > 60L) {
                seconds -= 60L;
            }
            if (seconds > 0L) {
                joiner.add((CharSequence)(seconds + "s"));
            }
        } else if (nano > 1000000L) {
            joiner.add((CharSequence)(nano / 1000000L + "ms"));
        } else {
            joiner.add((CharSequence)(nano + "ns"));
        }
        Aqua.INSTANCE.comfortaa3.drawString("Session Info", posX + 2.0f, posY + 2.0f, Arraylist.getGradientOffset((Color)new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (Color)new Color(Aqua.setmgr.getSetting("ArraylistColor").getColor()), (double)15.0).getRGB());
        Aqua.INSTANCE.comfortaa4.drawString("Kills : " + this.kills, posX + 2.0f, posY + 15.0f, -1);
        Aqua.INSTANCE.comfortaa4.drawString("Deaths : " + this.deaths, (float)(Aqua.INSTANCE.comfortaa3.getStringWidth("Kills : " + this.kills) + 5), posY + 15.0f, -1);
        Aqua.INSTANCE.comfortaa4.drawString("Playtime : " + joiner, posX + 2.0f, posY + 25.0f, -1);
        Aqua.INSTANCE.comfortaa4.drawString("KD : " + this.kills / (this.deaths == 0 ? 1 : this.deaths), posX + 2.0f, posY + 35.0f, -1);
        Arraylist.drawGlowArray(() -> Aqua.INSTANCE.comfortaa3.drawString("Session Info", posX + 2.0f, posY + 2.0f, Arraylist.getGradientOffset((Color)new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (Color)new Color(Aqua.setmgr.getSetting("ArraylistColor").getColor()), (double)15.0).getRGB()), (boolean)false);
    }

    public void drawRectShadersModern() {
        float posX = Aqua.setmgr.getSetting("SessionInfoConnectedPosX").isState() ? -4.0f : (float)Aqua.setmgr.getSetting("SessionInfoPosX").getCurrentNumber();
        float posY = (float)Aqua.setmgr.getSetting("SessionInfoPosY").getCurrentNumber();
        int width = 120;
        int height = 47;
        int cornerRadios = 4;
        if (Aqua.moduleManager.getModuleByName("Blur").isToggled()) {
            Blur.drawBlurred(() -> RenderUtil.drawRoundedRect2Alpha((double)posX, (double)posY, (double)width, (double)height, (double)cornerRadios, (Color)new Color(0, 0, 0, 80)), (boolean)false);
        }
        if (Aqua.moduleManager.getModuleByName("Arraylist").isToggled() && Aqua.setmgr.getSetting("SessionInfoMode").getCurrentMode().equalsIgnoreCase("Glow")) {
            Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect((double)posX, (double)posY, (double)width, (double)height, (double)cornerRadios, (int)Aqua.setmgr.getSetting("HUDColor").getColor()), (boolean)false);
        }
        if (Aqua.moduleManager.getModuleByName("Shadow").isToggled() && Aqua.setmgr.getSetting("SessionInfoMode").getCurrentMode().equalsIgnoreCase("Shadow")) {
            Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)posX, (double)posY, (double)width, (double)height, (double)cornerRadios, (int)Color.black.getRGB()), (boolean)false);
        }
    }

    public void drawRectsModern() {
        float posX = Aqua.setmgr.getSetting("SessionInfoConnectedPosX").isState() ? -4.0f : (float)Aqua.setmgr.getSetting("SessionInfoPosX").getCurrentNumber();
        float posY = (float)Aqua.setmgr.getSetting("SessionInfoPosY").getCurrentNumber();
        int width = 120;
        int height = 47;
        int cornerRadios = 4;
        RenderUtil.drawRoundedRect2Alpha((double)posX, (double)posY, (double)width, (double)height, (double)cornerRadios, (Color)new Color(0, 0, 0, 90));
        RenderUtil.drawRoundedRect2Alpha((double)posX, (double)posY, (double)6.0, (double)height, (double)0.0, (Color)Arraylist.getGradientOffset((Color)new Color(Aqua.setmgr.getSetting("HUDColor").getColor()), (Color)new Color(Aqua.setmgr.getSetting("ArraylistColor").getColor()), (double)15.0));
    }
}
