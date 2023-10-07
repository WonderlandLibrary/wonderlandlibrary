// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import java.util.HashMap;
import exhibition.event.impl.EventTick;
import exhibition.event.RegisterEvent;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Timer extends Module
{
    private String GS;
    
    public Timer(final ModuleData data) {
        super(data);
        this.GS = "GAMESPEED";
        ((HashMap<String, Setting<Double>>)this.settings).put(this.GS, new Setting<Double>(this.GS, 0.3, "The value the mc timer will be set to.", 0.05, 0.1, 1.0));
    }
    
    @Override
    public void onEnable() {
        Timer.mc.timer.timerSpeed = 1.0f;
    }
    
    @Override
    public void onDisable() {
        Timer.mc.timer.timerSpeed = 1.0f;
    }
    
    @RegisterEvent(events = { EventTick.class })
    @Override
    public void onEvent(final Event event) {
        Timer.mc.timer.timerSpeed = ((HashMap<K, Setting<Number>>)this.settings).get(this.GS).getValue().floatValue();
    }
}
