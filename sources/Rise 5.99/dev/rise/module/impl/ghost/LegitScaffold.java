/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.ghost;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "LegitScaffold", description = "Sneaks on the edge of blocks", category = Category.LEGIT)
public final class LegitScaffold extends Module {

    private final NumberSetting delay = new NumberSetting("Delay", this, 50, 0, 200, 10);
    private final BooleanSetting blockCheck = new BooleanSetting("Blocks Only", this, true);
    private final BooleanSetting directionCheck = new BooleanSetting("Directional Check", this, true);
    private final TimeUtil timer = new TimeUtil();
    private boolean wasOverBlock = false;

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (!blockCheck.isEnabled() || (mc.thePlayer.getHeldItem() != null
                && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) && !directionCheck.isEnabled() || mc.thePlayer.moveForward < 0) {

            if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() instanceof BlockAir && mc.thePlayer.onGround) {
                mc.gameSettings.keyBindSneak.setKeyPressed(true);
                wasOverBlock = true;
            } else if (mc.thePlayer.onGround) {
                if (wasOverBlock) timer.reset();

                if (timer.hasReached((long) (delay.getValue() * (Math.random() * 0.1 + 0.95)))) {
                    mc.gameSettings.keyBindSneak.setKeyPressed(Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()));
                }

                wasOverBlock = false;
            }
        } else {
            mc.gameSettings.keyBindSneak.setKeyPressed(Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()));
        }
    }

    @Override
    protected void onDisable() {
        mc.gameSettings.keyBindSneak.setKeyPressed(Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()));
    }
}
