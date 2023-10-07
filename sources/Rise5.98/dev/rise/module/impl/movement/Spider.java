/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.movement;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;

@ModuleInfo(name = "Spider", description = "Lets you climb up walls", category = Category.MOVEMENT)
public final class Spider extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Collide", "Jump");
    private final NumberSetting speed = new NumberSetting("Speed", this, 0.2, 0.0, 5, 0.1);

    private final TimeUtil timer = new TimeUtil();
    private boolean stoppedMotion = true;

    @Override
    public void onUpdateAlwaysInGui() {
        speed.hidden = !mode.is("Normal");
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        switch (mode.getMode()) {
            case "Normal":
                if (mc.thePlayer.isCollidedHorizontally) {
                    mc.thePlayer.motionY = speed.getValue();
                    stoppedMotion = false;
                } else if (!stoppedMotion) {
                    mc.thePlayer.motionY = 0;
                    stoppedMotion = true;
                }
                break;

            case "Collide":
                if (!mc.thePlayer.onGround && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                    if (mc.thePlayer.posY + mc.thePlayer.motionY < Math.floor(mc.thePlayer.posY))
                        if (!(PlayerUtil.getBlockRelativeToPlayer(-1, -1, 0) instanceof BlockAir) || !(PlayerUtil.getBlockRelativeToPlayer(1, -1, 0) instanceof BlockAir) || !(PlayerUtil.getBlockRelativeToPlayer(0, -1, -1) instanceof BlockAir) || !(PlayerUtil.getBlockRelativeToPlayer(0, -1, 1) instanceof BlockAir))
                            mc.thePlayer.motionY = (Math.floor(mc.thePlayer.posY) - (mc.thePlayer.posY));

                    if (mc.thePlayer.motionY == 0) {
                        mc.thePlayer.onGround = true;
                        event.setGround(true);
                    }
                }
                break;

            case "Jump":
                if (mc.thePlayer.isCollidedHorizontally && timer.hasReached(500L)) {
                    mc.thePlayer.motionY = MoveUtil.getJumpMotion(0.42F);
                    event.setGround(true);
                    timer.reset();
                }
                break;
        }
    }

    @Override
    protected void onDisable() {
        stoppedMotion = true;
        timer.reset();
    }
}
