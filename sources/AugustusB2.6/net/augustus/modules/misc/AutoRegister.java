// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.misc;

import net.augustus.events.EventTick;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.network.Packet;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.network.play.server.S02PacketChat;
import net.augustus.events.EventReadPacket;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class AutoRegister extends Module
{
    private final TimeHelper timeHelper;
    private String text;
    
    public AutoRegister() {
        super("AutoRegister", new Color(189, 32, 110), Categorys.MISC);
        this.timeHelper = new TimeHelper();
    }
    
    @EventTarget
    public void onEventReadPacket(final EventReadPacket eventReadPacket) {
        final Packet packet = eventReadPacket.getPacket();
        if (packet instanceof S02PacketChat) {
            final S02PacketChat s02PacketChat = (S02PacketChat)packet;
            final String text = s02PacketChat.getChatComponent().getUnformattedText();
            if (StringUtils.containsIgnoreCase(text, "/register") || StringUtils.containsIgnoreCase(text, "/register password password") || text.equalsIgnoreCase("/register <password> <password>")) {
                AutoRegister.mc.thePlayer.sendChatMessage("/register Hallo1337 Hallo1337");
                this.text = "/register Hallo1337 Hallo1337";
                this.timeHelper.reset();
            }
            else if (StringUtils.containsIgnoreCase(text, "/login password") || StringUtils.containsIgnoreCase(text, "/login") || text.equalsIgnoreCase("/login <password>")) {
                AutoRegister.mc.thePlayer.sendChatMessage("/login Hallo1337");
                this.text = "/login Hallo1337";
                this.timeHelper.reset();
            }
        }
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        if (this.timeHelper.reached(1500L) && this.text != null && !this.text.equals("")) {
            AutoRegister.mc.thePlayer.sendChatMessage(this.text);
            System.out.println(this.text);
            this.text = "";
        }
    }
}
