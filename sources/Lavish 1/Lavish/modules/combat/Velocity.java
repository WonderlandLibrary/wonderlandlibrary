// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.combat;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import Lavish.event.events.EventPacket;
import Lavish.event.Event;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import java.util.ArrayList;
import Lavish.modules.Category;
import Lavish.utils.misc.Timer;
import Lavish.modules.Module;

public class Velocity extends Module
{
    public int randomnumber;
    Timer timer;
    
    public Velocity() {
        super("Velocity", 0, true, Category.Combat, "Makes you take no KB!");
        this.randomnumber = 0;
        this.timer = new Timer();
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Normal");
        options.add("Stack");
        Client.instance.setmgr.rSetting(new Setting("Velocity Modes", this, "Normal", options));
    }
    
    @Override
    public void onEnable() {
    }
    
    @Override
    public void onDisable() {
        this.randomnumber = 0;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (Client.instance.moduleManager.getModuleByName("Fly").isEnabled()) {
            return;
        }
        if (Client.instance.setmgr.getSettingByName("Velocity Modes").getValString().equalsIgnoreCase("Stack") && e instanceof EventPacket && ((EventPacket)e).isRecieving()) {
            final EventPacket event = (EventPacket)e;
            final Packet packet = event.getPacket();
            if (packet instanceof S12PacketEntityVelocity) {
                if (Velocity.mc.thePlayer.isDead) {
                    this.randomnumber = 0;
                }
                final S12PacketEntityVelocity s12 = (S12PacketEntityVelocity)packet;
                if (s12.getEntityID() == Velocity.mc.thePlayer.getEntityId() && this.randomnumber != 2) {
                    ++this.randomnumber;
                }
                if (s12.getEntityID() == Velocity.mc.thePlayer.getEntityId() && this.randomnumber == 2) {
                    e.setCancelled(true);
                    this.randomnumber = 0;
                }
            }
        }
        if (Client.instance.setmgr.getSettingByName("Velocity Modes").getValString().equalsIgnoreCase("Normal") && e instanceof EventPacket && ((EventPacket)e).isRecieving()) {
            final EventPacket event = (EventPacket)e;
            final Packet packet = event.getPacket();
            if (packet instanceof S12PacketEntityVelocity) {
                final S12PacketEntityVelocity s12 = (S12PacketEntityVelocity)packet;
                if (s12.getEntityID() == Velocity.mc.thePlayer.getEntityId()) {
                    e.setCancelled(true);
                }
            }
            if (packet instanceof S27PacketExplosion) {
                e.setCancelled(true);
            }
        }
    }
}
