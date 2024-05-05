// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class Regen extends Module
{
    private NumberSetting health;
    private NumberSetting packets;
    
    public Regen() {
        super("Regen", 0, "Regenerates your health", Category.PLAYER);
        this.health = new NumberSetting("Health", 5.0, 1.0, 20.0, 1.0);
        this.packets = new NumberSetting("Packets", 100.0, 5.0, 500.0, 5.0);
        this.addSettings(this.health, this.packets);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion && Regen.mc.thePlayer.getHealth() < this.health.getValue()) {
            for (int i = 0; i < this.packets.getValue(); ++i) {
                Regen.mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Regen.mc.thePlayer.posX, Regen.mc.thePlayer.posY, Regen.mc.thePlayer.posZ, Regen.mc.thePlayer.onGround));
            }
        }
    }
}
