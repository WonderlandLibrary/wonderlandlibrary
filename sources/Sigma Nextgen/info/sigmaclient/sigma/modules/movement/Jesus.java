package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.event.player.BlockColEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.block.AirBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.util.math.BlockPos;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Jesus extends Module {
    public ModeValue type = new ModeValue("Type", "Vanilla", new String[]{"Vanilla", "Dolphin", "Hycraft", "Vulcan", "Funtime"});
    public BooleanValue up = new BooleanValue("Up in water", false);
    public Jesus() {
        super("Jesus", Category.Movement, "Walk on the liquid");
     registerValue(type);
     registerValue(up);
    }
    boolean onWater = false;
    boolean lastWater = false;
    int jumpTimes = 0;
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
            switch (type.getValue()) {
                case "Dolphin":
                    if(mc.player.isInWater())
                        mc.player.getMotion().y = 0.1;
                    break;
            }
            if(!mc.player.isInWater()){
                if(lastWater){
                    if(type.is("Funtime")){
                        MovementUtils.strafing(0.21f);
                        jumpTimes ++;
                    }
                }
            }
            if(type.is("Funtime")){
                if(mc.player.isInWater()) {
                    if(jumpTimes >= 8) {
                        mc.player.getMotion().y = 0.04;
                        jumpTimes = 0;
                    }
                        else
                    mc.player.getMotion().y = mc.world.getBlockState(new BlockPos(mc.player.getPositionVector().add(0, 0.3, 0))).getBlock() instanceof FlowingFluidBlock ? 0.2 : 0.15;
                }
            }
            lastWater = mc.player.isInWater();
            onWater = false;
            this.suffix = type.getValue();
           if(mc.world.getBlockState(new BlockPos(mc.player.getPositionVector().add(0, -0.1, 0))).getBlock() instanceof FlowingFluidBlock && mc.world.getBlockState(new BlockPos(mc.player.getPositionVector())).getBlock() instanceof AirBlock){
               onWater = true;
               switch (type.getValue()){
                   case "Vanilla":
                       mc.player.getMotion().y = 0;
                       break;
                   case "Polar":
                       mc.player.getMotion().y = 0.01999999910593033;
                       break;
                   case "Vulcan":
                       mc.player.getMotion().y = 0.1;
                       break;
               }
           }else if(up.isEnable() && !type.is("Funtime")){
               if(mc.player.isInWater())
                   mc.player.getMotion().y = 0.15;
            }
        }
        super.onUpdateEvent(event);
    }
}
