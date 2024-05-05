package ru.smertnix.celestial.feature.impl.player;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.utils.movement.MovementUtils;

public class NoClip extends Feature {

    public NoClip() {
        super("No Clip", "Позволяет ходить через стены", FeatureCategory.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player != null && !isNoClip(mc.player)) {
        	if (mc.player.isCollidedHorizontally)
        		mc.player.isCollidedHorizontally = false;
        	if (mc.player.isCollidedVertically)
        		mc.player.isCollidedVertically = false;
            mc.player.noClip = true;
            mc.player.onGround = false;
            MovementUtils.setSpeed(MovementUtils.getSpeed());
            if (mc.gameSettings.keyBindJump.isKeyDown() && mc.player.ticksExisted % 10 == 0) {
                mc.player.jump();
            }
            if (!(mc.gameSettings.keyBindSneak.isKeyDown() && mc.player.ticksExisted % 10 < 5)) {
            	if (mc.player.motionY < 0) {
            		mc.player.motionY = 0.00001;
            	}
            }
        } else {
        	mc.player.noClip = false;
        }
    }

    

    public static boolean isNoClip(Entity entity) {
    	boolean a;
    	if ((mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() != Blocks.WEB && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.WATER && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.LAVA && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.AIR)) {
    		a = true;
    	} else {
    		a = false;
    	}
        return a;

    }

    public void onDisable() {
        mc.player.noClip = false;
        super.onDisable();
    }
}
