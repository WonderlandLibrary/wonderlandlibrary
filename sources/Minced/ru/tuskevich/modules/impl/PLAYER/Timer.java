// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import net.minecraft.util.math.MathHelper;
import ru.tuskevich.util.movement.MoveUtility;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Timer", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd", type = Type.PLAYER)
public class Timer extends Module
{
    public static SliderSetting timerAmount;
    public static BooleanSetting smart;
    public static SliderSetting movingUp;
    public static long lastUpdateTime;
    public static double value;
    public static float animWidth;
    public double x;
    public double y;
    public double z;
    public double yaw;
    public double pitch;
    public boolean onGround;
    
    public Timer() {
        this.add(Timer.timerAmount, Timer.smart, Timer.movingUp);
    }
    
    public static int getMin() {
        return -15;
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (!Timer.smart.get() || canEnableTimer(Timer.timerAmount.getFloatValue() + 0.2f)) {
            final net.minecraft.util.Timer timer = Timer.mc.timer;
            final float floatValue = Timer.timerAmount.getFloatValue();
            final Minecraft mc = Timer.mc;
            timer.timerSpeed = Math.max(floatValue + ((Minecraft.player.ticksExisted % 2 == 0) ? -0.2f : 0.2f), 0.1f);
        }
        else {
            Timer.mc.timer.timerSpeed = 1.0f;
            this.toggle();
        }
    }
    
    public static double getProgress() {
        return (10.0 - Timer.value) / (Math.abs(getMin()) + 10);
    }
    
    public static boolean canEnableTimer(final float speed) {
        final double predictVl = (50.0 - 50.0 / speed) / 50.0;
        return predictVl + Timer.value < 10.0 - Timer.timerAmount.getDoubleValue();
    }
    
    public boolean canEnableTimerIgnoreSettings(final float speed) {
        final double predictVl = (50.0 - 50.0 / speed) / 50.0;
        return predictVl + Timer.value < 10.0;
    }
    
    @Override
    public void onDisable() {
        Timer.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
    
    public static void m() {
        final long now = System.currentTimeMillis();
        final long timeElapsed = now - Timer.lastUpdateTime;
        Timer.lastUpdateTime = now;
        Timer.value += (50.0 - timeElapsed) / 50.0;
        if (MoveUtility.isMoving()) {
            Timer.value -= Timer.movingUp.getFloatValue();
        }
        else {
            Timer.value -= 0.001;
        }
        Timer.value = MathHelper.clamp(Timer.value, getMin(), 25.0);
    }
    
    static {
        Timer.timerAmount = new SliderSetting("Timer Value", 2.0f, 1.0f, 10.0f, 0.01f);
        Timer.smart = new BooleanSetting("Smart", true);
        Timer.movingUp = new SliderSetting("Moving Up", 0.0f, 0.0f, 0.1f, 0.01f, () -> Timer.smart.get());
    }
}
