/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.combat;

import markgg.events.Event;
import markgg.events.EventGetPacket;
import markgg.modules.Module;
import markgg.settings.NumberSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity
extends Module {
    public NumberSetting amount = new NumberSetting("Amount", 25.0, 1.0, 100.0, 1.0);

    public Velocity() {
        super("Velocity", 0, Module.Category.COMBAT);
        this.settings.add(this.amount);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventGetPacket) {
            double velocity;
            Packet packet;
            EventGetPacket pe = (EventGetPacket)e;
            if (pe.getPacket() instanceof S12PacketEntityVelocity) {
                packet = (S12PacketEntityVelocity)pe.getPacket();
                velocity = this.amount.getValue() / 100.0;
                ((S12PacketEntityVelocity)packet).motionX = (int)((double)((S12PacketEntityVelocity)packet).getMotionX() * velocity);
                ((S12PacketEntityVelocity)packet).motionY = (int)((double)((S12PacketEntityVelocity)packet).getMotionY() * velocity);
                ((S12PacketEntityVelocity)packet).motionZ = (int)((double)((S12PacketEntityVelocity)packet).getMotionZ() * velocity);
            }
            if (pe.getPacket() instanceof S27PacketExplosion) {
                packet = (S27PacketExplosion)pe.getPacket();
                velocity = this.amount.getValue() / 100.0;
                ((S27PacketExplosion)packet).motionX = (int)((double)((S27PacketExplosion)packet).getMotionX() * velocity);
                ((S27PacketExplosion)packet).motionY = (int)((double)((S27PacketExplosion)packet).getMotionY() * velocity);
                ((S27PacketExplosion)packet).motionZ = (int)((double)((S27PacketExplosion)packet).getMotionZ() * velocity);
            }
        }
    }
}

