// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.movement;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import dev.tenacity.utils.player.MovementUtils;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Module;

public class AutoHeadHitter extends Module
{
    private final BooleanSetting onlyWhileJumping;
    private final BooleanSetting ignoreIfSneaking;
    private final NumberSetting jps;
    private final TimerUtil timer;
    
    public AutoHeadHitter() {
        super("AutoHeadHitter", "Auto Head Hitter", Category.MOVEMENT, "Automatically jumps when there is a block above you");
        this.onlyWhileJumping = new BooleanSetting("Only while jumping", true);
        this.ignoreIfSneaking = new BooleanSetting("Ignore if sneaking", true);
        this.jps = new NumberSetting("Jumps per second", 10.0, 1.0, 20.0, 1.0);
        this.timer = new TimerUtil();
        this.addSettings(this.onlyWhileJumping, this.ignoreIfSneaking, this.jps);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        if (e.isPre()) {
            if ((this.onlyWhileJumping.isEnabled() && !AutoHeadHitter.mc.gameSettings.keyBindJump.isKeyDown()) || (this.ignoreIfSneaking.isEnabled() && AutoHeadHitter.mc.thePlayer.isSneaking()) || AutoHeadHitter.mc.currentScreen != null || !AutoHeadHitter.mc.thePlayer.onGround || !MovementUtils.isMoving() || !this.isUnderBlock()) {
                return;
            }
            if (this.timer.hasTimeElapsed(1000.0 / this.jps.getValue())) {
                AutoHeadHitter.mc.thePlayer.jump();
                this.timer.reset();
            }
        }
    }
    
    private boolean isUnderBlock() {
        final BlockPos pos = new BlockPos(Math.floor(AutoHeadHitter.mc.thePlayer.posX), (int)AutoHeadHitter.mc.thePlayer.posY + 2, Math.floor(AutoHeadHitter.mc.thePlayer.posZ));
        return (AutoHeadHitter.mc.theWorld.isBlockFullCube(pos) || AutoHeadHitter.mc.theWorld.isBlockNormalCube(pos, false)) && AutoHeadHitter.mc.theWorld.getBlockState(pos).getBlock() != Blocks.air;
    }
}
