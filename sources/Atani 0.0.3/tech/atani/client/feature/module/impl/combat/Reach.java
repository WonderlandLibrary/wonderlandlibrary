package tech.atani.client.feature.module.impl.combat;

import net.minecraft.util.EnumFacing;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.listener.event.minecraft.player.rotation.RayTraceRangeEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.value.impl.SliderValue;

@ModuleData(name = "Reach", description = "Allows you to hit further", category = Category.COMBAT)
public class Reach extends Module {

    public SliderValue<Float> attackRange = new SliderValue<>("Attack Range", "What'll be the range for Attacking?", this, 3f, 3f, 6f, 1);
    public CheckBoxValue fixServersSideMisplace = new CheckBoxValue("Fix Server-Side Misplace", "Fix Server-Side Misplace?", this, true);

    double correctedRange;

    @Listen
    public void onRayTrace(RayTraceRangeEvent rayTraceRangeEvent) {
        correctedRange = this.attackRange.getValue() + 0.00256f;
        if (this.fixServersSideMisplace.getValue()) {
            final float n = 0.010625f;
            if (mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH || mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST) {
                correctedRange += n * 2.0f;
            }
        }
        rayTraceRangeEvent.setRange((float) correctedRange);
        rayTraceRangeEvent.setBlockReachDistance((float) Math.max(mc.playerController.getBlockReachDistance(), correctedRange));
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
