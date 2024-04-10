package info.sigmaclient.sigma.modules.combat;

import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.MouseClickEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.network.play.client.CPlayerTryUseItemOnBlockPacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.network.play.client.CUseEntityPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class NoInteract extends Module {
    public NoInteract() {
        super("NoInteract", Category.Combat, "No interact special blocks");
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if (event.packet instanceof CPlayerTryUseItemOnBlockPacket || event.packet instanceof CUseEntityPacket) {
            if (event.packet instanceof CUseEntityPacket && (((CUseEntityPacket) event.packet).getAction().equals(CUseEntityPacket.Action.INTERACT) || ((CUseEntityPacket) event.packet).getAction().equals(CUseEntityPacket.Action.INTERACT_AT))) {
                mc.getConnection().sendPacketNOEvent(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                event.cancelable = true;
            }
            if (event.packet instanceof CPlayerTryUseItemOnBlockPacket) {
                mc.getConnection().sendPacketNOEvent(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                event.cancelable = true;
            }
        }
        super.onPacketEvent(event);
    }
}
