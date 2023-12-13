/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.properties.Property;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.FoodStats;

public class TickRegen
extends Module {
    private static Property<Float> health;
    private static Property<Integer> packets;

    public TickRegen() {
        super("TickRegen", 0, Category.COMBAT, -1837002221, true, "Regens fast as fuck", new String[]{"rgn"});
        health = new Property<Float>(this, "regen_health", Float.valueOf(18.0f));
        packets = new Property<Integer>(this, "regen_packet_amount", 100);
    }

    @EventTarget
    public void onPre(EventPreMotionUpdates event) {
        int index = 0;
        while (index < packets.getValue()) {
            if (Wrapper.mc.thePlayer.onGround && Wrapper.mc.thePlayer.getHealth() <= health.getValue().floatValue() && Wrapper.mc.thePlayer.getFoodStats().getFoodLevel() > 17) {
                Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(Wrapper.mc.thePlayer.onGround));
            }
            ++index;
        }
    }
}

