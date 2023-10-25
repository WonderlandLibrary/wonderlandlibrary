package wtf.evolution.module.impl.Movement;

import net.minecraft.block.BlockWeb;
import net.minecraft.util.math.BlockPos;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(name = "WebLeave", type = Category.Movement)
public class WebLeave extends Module {

    public SliderSetting motion = new SliderSetting("Motion", 1, 0, 10, 0.1f).call(this);

    @EventTarget
    public void onMotion(EventMotion e) {
        if (mc.player.isInWeb) {
            mc.player.motionY = 1;
        }
        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ)).getBlock() instanceof BlockWeb) {
            mc.player.motionY = motion.get();
        }
    }

}
