/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.motion.EventStep;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.combat.KillAura;
import org.celestial.client.feature.impl.combat.TargetStrafe;
import org.celestial.client.feature.impl.movement.NoClip;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class Step
extends Feature {
    public static TimerHelper lastStep = new TimerHelper();
    public static TimerHelper time = new TimerHelper();
    public static NumberSetting delay;
    public static NumberSetting heightStep;
    public static ListSetting stepMode;
    public BooleanSetting reverseStep;
    public boolean jump;
    boolean resetTimer;

    public Step() {
        super("Step", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0432\u0437\u0431\u0435\u0440\u0430\u0435\u0442\u0441\u044f \u043d\u0430 \u0431\u043b\u043e\u043a\u0438", Type.Movement);
        stepMode = new ListSetting("Step Mode", "Matrix", () -> true, "Matrix", "NCP", "Vanilla");
        delay = new NumberSetting("Step Delay", 0.0f, 0.0f, 1.0f, 0.1f, () -> true);
        heightStep = new NumberSetting("Height", 1.0f, 1.0f, 10.0f, 0.5f, () -> true);
        this.reverseStep = new BooleanSetting("Reverse Step", false, () -> true);
        this.addSettings(stepMode, heightStep, delay, this.reverseStep);
    }

    @EventTarget
    public void onStep(EventStep step) {
        String mode = stepMode.getOptions();
        float delayValue = delay.getCurrentValue() * 1000.0f;
        double stepValue = heightStep.getCurrentValue();
        if (Celestial.instance.featureManager.getFeatureByClass(NoClip.class).getState()) {
            return;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(TargetStrafe.class).getState() && KillAura.target != null && Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState()) {
            return;
        }
        if (this.resetTimer) {
            this.resetTimer = false;
            Step.mc.timer.timerSpeed = 1.0f;
        }
        if (step.isPre() && !Step.mc.player.isInLiquid()) {
            if (Step.mc.player.isCollidedVertically && !Step.mc.gameSettings.keyBindJump.isPressed() && time.hasReached(delayValue)) {
                step.setStepHeight(stepValue);
            }
        } else {
            boolean canStep;
            double height = Step.mc.player.getEntityBoundingBox().minY - Step.mc.player.posY;
            boolean bl = canStep = height >= 0.625;
            if (canStep) {
                lastStep.reset();
                time.reset();
            }
            if (mode.equalsIgnoreCase("Matrix")) {
                if (canStep) {
                    Step.mc.timer.timerSpeed = 0.37f;
                    this.resetTimer = true;
                    this.matrixStep();
                }
            } else if (mode.equalsIgnoreCase("NCP")) {
                if (canStep) {
                    Step.mc.timer.timerSpeed = height > 1.0 ? 0.1f : 0.4f;
                    this.resetTimer = true;
                    this.ncpStep(height);
                }
            } else if (mode.equalsIgnoreCase("Vanilla")) {
                Step.mc.player.stepHeight = heightStep.getCurrentValue();
            }
        }
    }

    private void matrixStep() {
        double posX = Step.mc.player.posX;
        double posZ = Step.mc.player.posZ;
        double posY = Step.mc.player.posY;
        Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, posY + 0.42, posZ, false));
    }

    private void ncpStep(double height) {
        block13: {
            double y;
            double posZ;
            double posX;
            double[] offset;
            block12: {
                offset = new double[]{0.42, 0.333, 0.248, 0.083, -0.078};
                posX = Step.mc.player.posX;
                posZ = Step.mc.player.posZ;
                y = Step.mc.player.posY;
                if (!(height < 1.1)) break block12;
                double first = 0.42;
                double second = 0.75;
                if (height != 1.0) {
                    first *= height;
                    second *= height;
                    if (first > 0.425) {
                        first = 0.425;
                    }
                    if (second > 0.78) {
                        second = 0.78;
                    }
                    if (second < 0.49) {
                        second = 0.49;
                    }
                }
                if (first == 0.42) {
                    first = 0.41999998688698;
                }
                Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + first, posZ, false));
                if (!(y + second < y + height)) break block13;
                Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + second, posZ, true));
                break block13;
            }
            if (height < 1.6) {
                for (double off : offset) {
                    Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y += off, posZ, true));
                }
            } else if (height < 2.1) {
                double[] heights;
                for (double off : heights = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869}) {
                    Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + off, posZ, true));
                }
            } else {
                double[] heights;
                for (double off : heights = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907}) {
                    Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, y + off, posZ, true));
                }
            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = stepMode.getOptions();
        this.setSuffix(mode);
        if (Step.mc.player.motionY > 0.0) {
            this.jump = true;
        } else if (Step.mc.player.onGround) {
            this.jump = false;
        }
        if (this.reverseStep.getCurrentValue() && !Step.mc.gameSettings.keyBindJump.isKeyDown() && !Step.mc.player.onGround && Step.mc.player.motionY < 0.0 && Step.mc.player.fallDistance < 1.0f && !this.jump) {
            Step.mc.player.motionY = -1.0;
        }
    }

    @Override
    public void onDisable() {
        Step.mc.player.stepHeight = 0.625f;
        Step.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}

