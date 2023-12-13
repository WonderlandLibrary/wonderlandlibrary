/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPacketSent;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class AntiHunger
extends Module {
    public AntiHunger() {
        super("AntiHunger", 0, Category.PLAYER, 10027212, true, "Stops hunger", new String[]{"ah"});
    }

    @EventTarget
    private void onPacketSend(EventPacketSent event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packet = (C03PacketPlayer)event.getPacket();
            packet.field_149474_g = false;
        }
    }
}

