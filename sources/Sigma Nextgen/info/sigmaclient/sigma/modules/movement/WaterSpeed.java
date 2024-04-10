package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.event.player.BlockColEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.util.math.BlockPos;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class WaterSpeed extends Module {
    public ModeValue type = new ModeValue("Type", "Vanilla", new String[]{"Vanilla", "Matrix"});
    public WaterSpeed() {
        super("WaterSpeed", Category.Movement, "Faster on the liquid");
     registerValue(type);
    }
    boolean onWater = false;
    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
    @Override
    public void onBlockColEvent(BlockColEvent event) {
        super.onBlockColEvent(event);
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            if(mc.player.isInWater()){
                switch (type.getValue()){
                    case "Vanilla"->{
                        MovementUtils.strafing(MovementUtils.getBaseMoveSpeed());
                    }
                    case "Matrix"->{
                        if(mc.player.isSwimming()) {
                            mc.player.setSprinting(true);
                            MovementUtils.strafing(MovementUtils.getSpeed() * 0.8f + MovementUtils.getBaseMoveSpeed() * 0.3f);
                        }
                    }
                }
            }
        }
        super.onUpdateEvent(event);
    }
}
