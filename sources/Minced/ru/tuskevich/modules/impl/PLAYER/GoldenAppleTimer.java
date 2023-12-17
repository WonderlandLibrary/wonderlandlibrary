// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import net.minecraft.init.Items;
import ru.tuskevich.event.events.impl.EventCalculateCooldown;
import ru.tuskevich.event.EventTarget;
import ru.tuskevich.event.events.impl.EventRightClick;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "GoldenAppleTimer", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.PLAYER)
public class GoldenAppleTimer extends Module
{
    public static long lastConsumeTime;
    SliderSetting appleCooldown;
    
    public GoldenAppleTimer() {
        this.appleCooldown = new SliderSetting("Delay", 2300.0f, 2300.0f, 3000.0f, 1.0f);
        this.add(this.appleCooldown);
    }
    
    @EventTarget
    public void onRightClick(final EventRightClick eventRightClick) {
        if (GoldenAppleTimer.mc.getCurrentServerData() != null && GoldenAppleTimer.mc.getCurrentServerData().serverIP != null && GoldenAppleTimer.mc.getCurrentServerData().serverIP.contains("reallyworld")) {
            final long time = System.currentTimeMillis() - GoldenAppleTimer.lastConsumeTime;
            if (time < this.appleCooldown.getIntValue()) {
                eventRightClick.cancel();
            }
        }
    }
    
    @EventTarget
    public void onCalculateCooldown(final EventCalculateCooldown eventCalculateCooldown) {
        if (GoldenAppleTimer.mc.getCurrentServerData() != null && GoldenAppleTimer.mc.getCurrentServerData().serverIP != null && GoldenAppleTimer.mc.getCurrentServerData().serverIP.contains("reallyworld") && eventCalculateCooldown.getStack() == Items.GOLDEN_APPLE) {
            final long time = System.currentTimeMillis() - GoldenAppleTimer.lastConsumeTime;
            if (time < this.appleCooldown.getFloatValue()) {
                eventCalculateCooldown.setCooldown(time / this.appleCooldown.getFloatValue());
            }
        }
    }
}
