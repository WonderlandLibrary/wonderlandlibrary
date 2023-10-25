package club.async.module.impl.player;

import club.async.event.impl.EventPreUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.BooleanSetting;
import club.async.module.setting.impl.NumberSetting;
import club.async.util.RotationUtil;
import club.async.util.ScaffoldUtil;
import club.async.util.TimeUtil;
import club.async.util.WorldUtil;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "BedBreaker", description = "Destroy beds", category = Category.PLAYER)
public class BedBreaker extends Module {

    private final TimeUtil timeUtil = new TimeUtil();

    public BooleanSetting silent = new BooleanSetting("Silent", this, false);
    public NumberSetting radius = new NumberSetting("Radius", this, 3,6,4,1);
    public NumberSetting delay = new NumberSetting("Delay", this, 50,1000,100,50);

    @Handler
    public void preUpdate(EventPreUpdate eventPreUpdate) {
        setExtraTag(radius.getDouble());
        for (double x = mc.thePlayer.posX - radius.getDouble(); x <= mc.thePlayer.posX + radius.getDouble(); x++) {
            for (double y = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - radius.getDouble(); y <= mc.thePlayer.posY + mc.thePlayer.getEyeHeight() + radius.getDouble(); y++) {
                for (double z = mc.thePlayer.posZ - radius.getDouble(); z <= mc.thePlayer.posZ + radius.getDouble(); z++) {
                    final BlockPos blockPos = new BlockPos(x,y,z);
                    if (WorldUtil.getBlock(blockPos) instanceof BlockBed) {
                        if (timeUtil.hasTimePassed(delay.getLong())) {
                            eventPreUpdate.setYaw(RotationUtil.getRotations(blockPos, ScaffoldUtil.getBlockData(blockPos).face)[0]);
                            eventPreUpdate.setPitch(RotationUtil.getRotations(blockPos, ScaffoldUtil.getBlockData(blockPos).face)[1]);
                            mc.thePlayer.rotationYawHead = RotationUtil.getRotations(blockPos, ScaffoldUtil.getBlockData(blockPos).face)[0];
                            mc.thePlayer.renderYawOffset = RotationUtil.getRotations(blockPos, ScaffoldUtil.getBlockData(blockPos).face)[0];

                            if (!silent.get())
                                mc.thePlayer.swingItem();
                            else
                                mc.getNetHandler().addToSendQueue(new C0APacketAnimation());

                            WorldUtil.destroy(blockPos);
                            timeUtil.reset();
                        }
                    }
                }
            }
        }
    }

}
