/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.events.EventPacketSent;
import cow.milkgod.cheese.events.EventPushOutOfBlock;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Antivelocity
extends Module {
    public Antivelocity() {
        super("AntiVelocity", 38, Category.MOVEMENT, 10066329, true, "Makes you take no velocity.", new String[]{"novel", "velocity", "antivel", "av", "antiv", "avelocity", "antivel"});
    }

    @EventTarget
    public void onPacketSent(EventPacketSent sent) {
        if (sent.getPacket() instanceof S12PacketEntityVelocity || sent.getPacket() instanceof S27PacketExplosion) {
            sent.setCancelled(true);
        }
    }

    @EventTarget
    public void onEvent(EventPushOutOfBlock event) {
        event.setCancelled(true);
    }

    public static boolean shouldAntiVel() {
        Cheese.getInstance();
        return Cheese.moduleManager.getModState("AntiVelocity");
    }
}

