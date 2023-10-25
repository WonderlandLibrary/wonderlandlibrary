package intent.AquaDev.aqua.modules.misc;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventPacket;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.combat.Killaura;
import intent.AquaDev.aqua.utils.TimeUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

public class AutoHypixel
extends Module {
    TimeUtil timeUtil = new TimeUtil();

    public AutoHypixel() {
        super("AutoHypixel", "AutoHypixel", 0, Category.Misc);
        Aqua.setmgr.register(new Setting("Mode", (Module)this, "Normal", new String[]{"Normal", "Insane"}));
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event event) {
        Packet e;
        if (event instanceof EventPacket && (e = EventPacket.getPacket()) instanceof S02PacketChat) {
            S02PacketChat s02PacketChat = (S02PacketChat)e;
            String cp21 = s02PacketChat.getChatComponent().getUnformattedText();
            if (cp21.contains((CharSequence)"You won! Want to play again? Click here!")) {
                if (Aqua.setmgr.getSetting("AutoHypixelMode").getCurrentMode().equalsIgnoreCase("Insane")) {
                    if (this.timeUtil.hasReached(2000L)) {
                        AutoHypixel.mc.thePlayer.sendChatMessage("/play solo_insane");
                        this.timeUtil.reset();
                    }
                    Killaura.bots.clear();
                }
                if (Aqua.setmgr.getSetting("AutoHypixelMode").getCurrentMode().equalsIgnoreCase("Normal")) {
                    if (this.timeUtil.hasReached(2000L)) {
                        AutoHypixel.mc.thePlayer.sendChatMessage("/play solo_normal");
                        this.timeUtil.reset();
                    }
                    Killaura.bots.clear();
                }
            }
            if (Aqua.setmgr.getSetting("AutoHypixelMode").getCurrentMode().equalsIgnoreCase("Insane") && cp21.contains((CharSequence)"You died!")) {
                if (this.timeUtil.hasReached(2000L)) {
                    AutoHypixel.mc.thePlayer.sendChatMessage("/play solo_insane");
                    this.timeUtil.reset();
                }
                Killaura.bots.clear();
            }
            if (Aqua.setmgr.getSetting("AutoHypixelMode").getCurrentMode().equalsIgnoreCase("Normal") && cp21.contains((CharSequence)"You died!")) {
                if (this.timeUtil.hasReached(2000L)) {
                    AutoHypixel.mc.thePlayer.sendChatMessage("/play solo_normal");
                    this.timeUtil.reset();
                }
                Killaura.bots.clear();
            }
        }
    }
}
