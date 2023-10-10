package tech.atani.client.feature.module.impl.movement;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import tech.atani.client.listener.event.minecraft.world.CollisionBoxesEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.feature.value.impl.StringBoxValue;

@Native
@ModuleData(name = "Jesus", description = "Walk on water like jesus", category = Category.MOVEMENT)
public class Jesus extends Module {
    private final StringBoxValue mode = new StringBoxValue("Mode", "What mode should this module use?", this, new String[] {"Solid", "Matrix"});

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Listen
    public void onMotion(UpdateMotionEvent updateMotionEvent) {
        if(updateMotionEvent.getType() == UpdateMotionEvent.Type.MID) {
            switch(mode.getValue()) {
            case "Matrix":
                if(Methods.mc.thePlayer.isInWater()) {
                    if(Methods.mc.thePlayer.isCollidedHorizontally) {
                        Methods.mc.thePlayer.motionY = 0.22D;
                    } else {
                        Methods.mc.thePlayer.motionY = 0.13D;
                    }
                    Methods.mc.gameSettings.keyBindJump.pressed = false;
                }
                break;
            }
        }
    }

    @Listen
    public void onCollisionBoxes(CollisionBoxesEvent collisionBoxesEvent) {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
            return;
        
        switch(mode.getValue()) {
        case "Solid":
            BlockPos blockPos = collisionBoxesEvent.getBlockPos();

            if(Methods.mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.water) {
                collisionBoxesEvent.setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            }
            break;
        }
    }

}
