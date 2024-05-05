package club.async.module.impl.movement;

import club.async.event.impl.EventPostUpdate;
import club.async.event.impl.EventPreUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.ModeSetting;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "NoSlowDown", description = "Dont Slow Down", category = Category.MOVEMENT)
public class NoSlowDown extends Module {

    public ModeSetting mode = new ModeSetting("Mode", this, new String[] {"Vanilla", "NCP", "AAC"});

    @Handler
    public void preUpdate(EventPreUpdate event) {
        if (mc.thePlayer.isUsingItem()) {
            switch (mode.getCurrMode()) {
                case "NCP":
                case "AAC":
                    mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    break;
            }
        }
    }

    @Handler
    public void postUpdate(EventPostUpdate event) {
        if (mc.thePlayer.isUsingItem()) {
            switch (mode.getCurrMode()) {
                case "NCP":
                    mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f));
                    break;
            }
        }
    }

}
