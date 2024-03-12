// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import dev.tenacity.event.impl.player.StepConfirmEvent;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class Step extends Module
{
    private final ModeSetting mode;
    private final NumberSetting height;
    private final NumberSetting timer;
    private boolean hasStepped;
    private final TimerUtil timerUtil;
    public static boolean isStepping;
    
    public Step() {
        super("Step", "Step", Category.MOVEMENT, "step up blocks");
        this.mode = new ModeSetting("Mode", "Vanilla", new String[] { "Vanilla", "NCP", "Full Jump Packets" });
        this.height = new NumberSetting("Height", 1.0, 10.0, 1.0, 0.5);
        this.timer = new NumberSetting("Timer", 1.0, 2.0, 0.1, 0.1);
        this.timerUtil = new TimerUtil();
        this.addSettings(this.mode, this.height, this.timer);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        this.setSuffix(this.mode.getMode());
        if (Step.mc.thePlayer.onGround) {
            if (Step.mc.thePlayer.stepHeight != this.height.getValue().floatValue()) {
                Step.mc.thePlayer.stepHeight = this.height.getValue().floatValue();
            }
        }
        else if (Step.mc.thePlayer.stepHeight != 0.625f) {
            Step.mc.thePlayer.stepHeight = 0.625f;
        }
        if (this.timerUtil.hasTimeElapsed(20L) && this.hasStepped) {
            Step.mc.timer.timerSpeed = 1.0f;
            this.hasStepped = false;
            Step.isStepping = false;
        }
    }
    
    @Override
    public void onStepConfirmEvent(final StepConfirmEvent event) {
        final double diffY = Step.mc.thePlayer.getEntityBoundingBox().minY - Step.mc.thePlayer.posY;
        if (diffY > 0.625 && diffY <= 1.5 && Step.mc.thePlayer.onGround) {
            Step.mc.timer.timerSpeed = this.timer.getValue().floatValue();
            this.timerUtil.reset();
            this.hasStepped = true;
            Step.isStepping = true;
            final String mode = this.mode.getMode();
            switch (mode) {
                case "NCP": {
                    for (final double offset : new double[] { 0.41999998688698, 0.7531999805212 }) {
                        Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY + offset, Step.mc.thePlayer.posZ, false));
                    }
                    break;
                }
                case "Full Jump Packets": {
                    for (final double offset : new double[] { 0.41999998688698, 0.7531999805212, 1.00133597911214, 1.16610926093821, 1.24918707874468, 1.24918707874468, 1.1707870772188, 1.0155550727022 }) {
                        Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY + offset, Step.mc.thePlayer.posZ, false));
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void onDisable() {
        Step.mc.thePlayer.stepHeight = 0.625f;
        super.onDisable();
    }
}
