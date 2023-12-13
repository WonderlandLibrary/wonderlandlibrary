/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public final class Paralyze
extends Module {
    public Paralyze() {
        super("Paralyze", 0, Category.EXPLOITS, 7829368, true, "Paralyzes other players", new String[]{"para"});
    }

    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        if (!Wrapper.mc.theWorld.getEntitiesWithinAABBExcludingEntity(Wrapper.mc.thePlayer, Wrapper.mc.thePlayer.boundingBox).isEmpty()) {
            int index = 0;
            while (index < 400) {
                Wrapper.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(Wrapper.mc.thePlayer.onGround));
                ++index;
            }
        }
    }
}

