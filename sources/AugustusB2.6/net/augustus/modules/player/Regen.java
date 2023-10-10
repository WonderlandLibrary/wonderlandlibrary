// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.player;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class Regen extends Module
{
    public final DoubleValue health;
    public final DoubleValue hunger;
    public final DoubleValue packets;
    public final BooleanValue groundCheck;
    
    public Regen() {
        super("Regen", new Color(224, 111, 49), Categorys.PLAYER);
        this.health = new DoubleValue(2, "Health", this, 20.0, 0.0, 20.0, 0);
        this.hunger = new DoubleValue(1, "MinHunger", this, 5.0, 0.0, 20.0, 0);
        this.packets = new DoubleValue(3, "Packets", this, 100.0, 0.0, 200.0, 0);
        this.groundCheck = new BooleanValue(4, "GroundCheck", this, false);
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        if (Regen.mc.thePlayer.getFoodStats().getFoodLevel() > this.hunger.getValue() && Regen.mc.thePlayer.getHealth() < this.health.getValue()) {
            for (int i = 0; i < this.packets.getValue(); ++i) {
                if (this.groundCheck.getBoolean()) {
                    Regen.mc.thePlayer.sendQueue.addToSendQueueDirect(new C03PacketPlayer(Regen.mc.thePlayer.onGround));
                }
                else {
                    Regen.mc.thePlayer.sendQueue.addToSendQueueDirect(new C03PacketPlayer(true));
                }
            }
        }
    }
}
