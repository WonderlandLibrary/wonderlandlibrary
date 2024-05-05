package com.initial.modules.impl.player;

import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import net.minecraft.util.*;
import net.minecraft.block.*;
import com.initial.utils.movement.*;
import com.initial.events.*;

public class Jesus extends Module
{
    public ModeSet mode;
    
    public Jesus() {
        super("Jesus", 0, Category.PLAYER);
        this.mode = new ModeSet("Mode", "Verus", new String[] { "Verus", "Jump", "Vanilla" });
        this.addSettings(this.mode);
    }
    
    @EventTarget
    public void onPre(final EventMotion e) {
        this.setDisplayName("Jesus §7" + this.mode.getMode());
        final BlockPos bpos = new BlockPos(Jesus.localPlayer.posX, Jesus.localPlayer.posY - (Jesus.localPlayer.onGround ? 0.1 : 0.41), Jesus.localPlayer.posZ);
        if (this.mc.theWorld.getBlockState(bpos).getBlock() instanceof BlockLiquid && !this.mc.gameSettings.keyBindJump.isKeyDown()) {
            final String mode = this.mode.getMode();
            switch (mode) {
                case "Verus": {
                    MovementUtils.actualSetSpeed(MovementUtils.getSpeed());
                    e.setOnGround(true);
                    Jesus.localPlayer.motionY = 0.0;
                    break;
                }
                case "Jump": {
                    Jesus.localPlayer.motionY = 0.21;
                    break;
                }
                case "Vanilla": {
                    Jesus.localPlayer.motionY = 0.0;
                    break;
                }
            }
        }
    }
}
