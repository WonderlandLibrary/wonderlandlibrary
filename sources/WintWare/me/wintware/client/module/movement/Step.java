/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.movement;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventStep;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.other.TimerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;

public class Step
extends Module {
    boolean isStep = false;
    boolean resetTimer;
    double jumpGround = 0.0;
    double stepY = 0.0;
    double stepX = 0.0;
    double stepZ = 0.0;
    public static TimerHelper lastStep = new TimerHelper();
    public static TimerHelper time = new TimerHelper();
    private final Setting delay;
    public Setting height = new Setting("Height", this, 1.5, 1.0, 10.0, false);

    public Step() {
        super("Step", Category.Movement);
        Main.instance.setmgr.rSetting(this.height);
        this.delay = new Setting("Delay", this, 0.1, 0.0, 1.0, false);
        Main.instance.setmgr.rSetting(this.delay);
    }

    @EventTarget
    public void onStepConfirm(EventStep step) {
        float timer = 0.37f;
        float delay1 = this.delay.getValFloat() * 1000.0f;
        double stepValue = this.height.getValDouble();
        if (this.resetTimer) {
            this.resetTimer = !this.resetTimer;
            Step.mc.timer.timerSpeed = 1.0f;
        }
        if (!Minecraft.player.isInLiquid2()) {
            if (step.isPre()) {
                if (Minecraft.player.isCollidedVertically && !Step.mc.gameSettings.keyBindJump.isPressed() && time.check(delay1)) {
                    step.setStepHeight(stepValue);
                    step.setActive(true);
                }
            } else {
                boolean canStep;
                double rheight = Minecraft.player.getEntityBoundingBox().minY - Minecraft.player.posY;
                boolean bl = canStep = rheight >= 0.625;
                if (canStep) {
                    lastStep.reset();
                    time.reset();
                }
                if (canStep) {
                    this.matrixStep(rheight);
                    this.resetTimer = true;
                    Step.mc.timer.timerSpeed = rheight < 2.0 ? 0.6f : 0.3f;
                }
            }
        }
    }

    void matrixStep(double height) {
        double posX = Minecraft.player.posX;
        double posZ = Minecraft.player.posZ;
        double y = Minecraft.player.posY;
        double first = 0.42;
        double second = 0.75;
        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + first, posZ, false));
    }

    @Override
    public void onDisable() {
        Minecraft.player.stepHeight = 0.625f;
        Step.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}

