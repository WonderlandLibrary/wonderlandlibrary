/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPacketReceive;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate
extends Module {
    public NoRotate() {
        super("NoRotate", 0, Category.PLAYER, 26367, true, "Cheesegod fortune rains down on you", new String[]{"nora"});
    }

    @EventTarget
    private void onPacketReceive(EventPacketReceive event) {
        if (!this.getState()) {
            return;
        }
        if (event.packet instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.packet;
            packet.field_148936_d = Wrapper.mc.thePlayer.rotationYaw;
            packet.field_148937_e = Wrapper.mc.thePlayer.rotationPitch;
        }
    }
}

