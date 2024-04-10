package info.sigmaclient.sigma.modules.combat;

import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.MouseClickEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class InteractRange extends Module {
    public NumberValue range = new NumberValue("Range", 5, 0, 8, NumberValue.NUMBER_TYPE.FLOAT);
    public InteractRange() {
        super("InteractRange", Category.Combat, "Boost your reach (interact)");
        registerValue(range);
    }

    @Override
    public void onMouseClickEvent(MouseClickEvent event) {
        if(!event.isAttack()){
            RayTraceResult result = mc.player.pick(range.getValue().floatValue(), 1.0F);
            if(result.getType() == RayTraceResult.Type.BLOCK
                    && result.getHitVec().distanceTo(mc.player.getEyePosition(1.0f)) > 3.0){
                BlockRayTraceResult resultb = (BlockRayTraceResult) result;
                if(mc.playerController.processRightClickBlock(
                        mc.player,
                        mc.world,
                        resultb.getPos(),
                        resultb.getFace(),
                        resultb.getHitVec(),
                        Hand.MAIN_HAND
                ) == ActionResultType.SUCCESS){
                    mc.player.swingArm(Hand.MAIN_HAND);
                }
            }
        }
        super.onMouseClickEvent(event);
    }
}
