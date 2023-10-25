package ru.smertnix.celestial.feature.impl.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventLiquidSolid;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.math.TimerHelper;
import ru.smertnix.celestial.utils.movement.MovementUtils;

public class    Jesus extends Feature {
    public TimerHelper timerHelper = new TimerHelper();
    public static ListSetting mode = new ListSetting("Jesus Mode", "Really World", () -> true, "Really World", "Matrix Zoom");
    public static NumberSetting speed = new NumberSetting("Speed", 6, 2, 10.0F, 1, () -> mode.currentMode.equals("Matrix Zoom"));

    private int waterTicks = 0;

    public Jesus() {
        super("Jesus", "Позволяет парить на воде", FeatureCategory.Movement);
        this.addSettings(mode, speed);
    }

    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        this.waterTicks = 0;
        super.onDisable();
    }


    private boolean isWater() {
        BlockPos bp1 = new BlockPos(mc.player.posX - 0.5D, mc.player.posY - 0.5D, mc.player.posZ - 0.5D);
        BlockPos bp2 = new BlockPos(mc.player.posX - 0.5D, mc.player.posY - 0.5D, mc.player.posZ + 0.5D);
        BlockPos bp3 = new BlockPos(mc.player.posX + 0.5D, mc.player.posY - 0.5D, mc.player.posZ + 0.5D);
        BlockPos bp4 = new BlockPos(mc.player.posX + 0.5D, mc.player.posY - 0.5D, mc.player.posZ - 0.5D);
        return mc.player.world.getBlockState(bp1).getBlock() == Blocks.WATER && mc.player.world.getBlockState(bp2).getBlock() == Blocks.WATER && mc.player.world.getBlockState(bp3).getBlock() == Blocks.WATER && mc.player.world.getBlockState(bp4).getBlock() == Blocks.WATER || mc.player.world.getBlockState(bp1).getBlock() == Blocks.LAVA && mc.player.world.getBlockState(bp2).getBlock() == Blocks.LAVA && mc.player.world.getBlockState(bp3).getBlock() == Blocks.LAVA && mc.player.world.getBlockState(bp4).getBlock() == Blocks.LAVA;
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        this.setSuffix(mode.getCurrentMode());
            BlockPos blockPos = new BlockPos(Minecraft.getMinecraft().player.posX, mc.player.posY - 0.1D, mc.player.posZ);
            Block block = mc.world.getBlockState(blockPos).getBlock();
            if (mode.currentMode.equalsIgnoreCase("Matrix Zoom")) {
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 9.999999747378752E-2D, mc.player.posZ)).getBlock() instanceof BlockLiquid) {
                    mc.player.motionY = 0.07000000074505806D;
                }
                if (block instanceof BlockLiquid && !Minecraft.getMinecraft().player.onGround) {
                    if (Minecraft.getMinecraft().world.getBlockState(new BlockPos(Minecraft.getMinecraft().player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.WATER) {
                        mc.player.motionX = 0.0F;
                        mc.player.motionY = 0.036F;
                        mc.player.motionZ = 0.0F;
                    } else {
                        MovementUtils.setSpeed(speed.getNumberValue());
                    }
                    if (Minecraft.getMinecraft().player.isCollided) {
                        mc.player.motionY = 0.2;
                    }
                }
            }
            if (mode.currentMode.equalsIgnoreCase("Really World")) {
            	if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) &&(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.WATER ||  mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.LAVA)
                        && !mc.player.onGround && mc.player.isCollidedHorizontally) {
                 	mc.player.jump();
                 	mc.player.motionX *= 0.1f;
                 	mc.player.motionZ *= 0.1f;
                 	mc.player.fallDistance = -99999.0F;
                 }
            	
                	if ((!mc.player.isCollidedHorizontally || !Keyboard.isKeyDown(Keyboard.KEY_SPACE)) &&(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.WATER || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.LAVA)
                            && !mc.player.onGround) {
            	 BlockPos blockPos2;
                 Block block2;
            	blockPos2 = new BlockPos(mc.player.posX, mc.player.posY - 0.02, mc.player.posZ);
                block2 = mc.world.getBlockState(blockPos2).getBlock();

                boolean isUp = mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.0311, mc.player.posZ)).getBlock() == Blocks.WATER || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.0311, mc.player.posZ)).getBlock() == Blocks.LAVA;
                mc.player.jumpMovementFactor = 0;
                mc.player.fallDistance = -99999.0F;
                float yport = MovementUtils.getSpeed() > 0.1 ? 0.02f : 0.032f;
                mc.player.setVelocity(0,mc.player.fallDistance < 3.5 ? (isUp ? yport : -yport) : -0.114, 0);
                
                if (mc.player.posY > (int)mc.player.posY + 0.89 && mc.player.posY <= (int)mc.player.posY + 1.0f || mc.player.fallDistance > 3.5) {
                	mc.player.posY = (int)mc.player.posY + 1.0f + 1E-45;
                    	MovementUtils.setSpeed(1.1f);
                }
                    
                    

                }
            	if ((mc.player.isInWater() || mc.player.isInLava())) {
                    	if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock() == Blocks.LAVA || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                        	mc.player.motionY = 0.18;
                        } else {
                        	mc.player.jump();
                        }
                }
        }
    }
    
    @EventTarget
    public void onLiquidBB(EventLiquidSolid event) {
        if (Jesus.mode.currentMode.equals("Really World")) {
        }
    }
}