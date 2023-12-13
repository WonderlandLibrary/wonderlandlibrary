// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.movement;

import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class FastSwim extends Module
{
    private Value<Boolean> up;
    private Value<Boolean> forward;
    private Value<Boolean> sprint;
    private Value<Boolean> only2b;
    private Value<Boolean> down;
    int divider;
    
    public FastSwim() {
        super("FastSwim", "Allows The Player To Swim Faster Horizontally and Vertically", 0, Category.MOVEMENT, true);
        this.divider = 5;
    }
    
    @Override
    public void setup() {
        this.up = this.register(new Value<Boolean>("FastSwimUp", this, true));
        this.down = this.register(new Value<Boolean>("FastSwimDown", this, true));
        this.forward = this.register(new Value<Boolean>("FastSwimForward", this, true));
        this.sprint = this.register(new Value<Boolean>("AutoSprintInLiquid", this, true));
        this.only2b = this.register(new Value<Boolean>("Only2b", this, true));
    }
    
    @Override
    public void onUpdate() {
        if (this.only2b.getValue() && !FastSwim.mc.isSingleplayer() && FastSwim.mc.getCurrentServerData() != null && FastSwim.mc.getCurrentServerData().serverIP.equalsIgnoreCase("2b2t.org")) {
            if (this.sprint.getValue() && (FastSwim.mc.player.isInLava() || FastSwim.mc.player.isInWater())) {
                FastSwim.mc.player.setSprinting(true);
            }
            if ((FastSwim.mc.player.isInWater() || FastSwim.mc.player.isInLava()) && FastSwim.mc.gameSettings.keyBindJump.isKeyDown() && this.up.getValue()) {
                FastSwim.mc.player.motionY = 0.725 / this.divider;
            }
            if (FastSwim.mc.player.isInWater() || FastSwim.mc.player.isInLava()) {
                if ((this.forward.getValue() && FastSwim.mc.gameSettings.keyBindForward.isKeyDown()) || FastSwim.mc.gameSettings.keyBindLeft.isKeyDown() || FastSwim.mc.gameSettings.keyBindRight.isKeyDown() || FastSwim.mc.gameSettings.keyBindBack.isKeyDown()) {
                    FastSwim.mc.player.jumpMovementFactor = 0.34f / this.divider;
                }
                else {
                    FastSwim.mc.player.jumpMovementFactor = 0.0f;
                }
            }
            if (FastSwim.mc.player.isInWater() && this.down.getValue() && FastSwim.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final int divider2 = this.divider * -1;
                FastSwim.mc.player.motionY = 2.2 / divider2;
            }
            if (FastSwim.mc.player.isInLava() && this.down.getValue() && FastSwim.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final int divider2 = this.divider * -1;
                FastSwim.mc.player.motionY = 0.91 / divider2;
            }
        }
        if (!this.only2b.getValue()) {
            if (this.sprint.getValue() && (FastSwim.mc.player.isInLava() || FastSwim.mc.player.isInWater())) {
                FastSwim.mc.player.setSprinting(true);
            }
            if ((FastSwim.mc.player.isInWater() || FastSwim.mc.player.isInLava()) && FastSwim.mc.gameSettings.keyBindJump.isKeyDown() && this.up.getValue()) {
                FastSwim.mc.player.motionY = 0.725 / this.divider;
            }
            if (FastSwim.mc.player.isInWater() || FastSwim.mc.player.isInLava()) {
                if ((this.forward.getValue() && FastSwim.mc.gameSettings.keyBindForward.isKeyDown()) || FastSwim.mc.gameSettings.keyBindLeft.isKeyDown() || FastSwim.mc.gameSettings.keyBindRight.isKeyDown() || FastSwim.mc.gameSettings.keyBindBack.isKeyDown()) {
                    FastSwim.mc.player.jumpMovementFactor = 0.34f / this.divider;
                }
                else {
                    FastSwim.mc.player.jumpMovementFactor = 0.0f;
                }
            }
            if (FastSwim.mc.player.isInWater() && this.down.getValue() && FastSwim.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final int divider2 = this.divider * -1;
                FastSwim.mc.player.motionY = 2.2 / divider2;
            }
            if (FastSwim.mc.player.isInLava() && this.down.getValue() && FastSwim.mc.gameSettings.keyBindSneak.isKeyDown()) {
                final int divider2 = this.divider * -1;
                FastSwim.mc.player.motionY = 0.91 / divider2;
            }
        }
    }
}
