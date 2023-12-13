package dev.tenacity.module.impl.movement;

import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.utils.player.MovementUtils;
import dev.tenacity.utils.time.TimerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class AutoHeadHitter extends Module {

    private final BooleanSetting
            onlyWhileJumping = new BooleanSetting("Only while jumping", true),
            ignoreIfSneaking = new BooleanSetting("Ignore if sneaking", true);
    private final NumberSetting jps = new NumberSetting("Jumps per second", 10, 1, 20, 1);
    private final TimerUtil timer = new TimerUtil();

    public AutoHeadHitter() {
        super("AutoHeadHitter", Category.MOVEMENT, "Automatically jumps when there is a block above you");
        addSettings(onlyWhileJumping, ignoreIfSneaking, jps);
    }

    @Override
    public void onMotionEvent(MotionEvent e) {
        if (e.isPre()) {
            if ((onlyWhileJumping.isEnabled() && !mc.gameSettings.keyBindJump.isKeyDown())
                    || (ignoreIfSneaking.isEnabled() && mc.thePlayer.isSneaking())
                    || mc.currentScreen != null
                    || !mc.thePlayer.onGround
                    || !MovementUtils.isMoving()
                    || !isUnderBlock()) {
                return;
            }
            if (timer.hasTimeElapsed(1000 / jps.getValue())) {
                mc.thePlayer.jump();
                timer.reset();
            }
        }
    }

    private boolean isUnderBlock() {
        BlockPos pos = new BlockPos(Math.floor(mc.thePlayer.posX), (int) mc.thePlayer.posY + 2, Math.floor(mc.thePlayer.posZ));
        return (mc.theWorld.isBlockFullCube(pos) || mc.theWorld.isBlockNormalCube(pos, false)) && mc.theWorld.getBlockState(pos).getBlock() != Blocks.air;
    }

}
