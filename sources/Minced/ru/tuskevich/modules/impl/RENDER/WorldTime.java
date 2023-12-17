// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import ru.tuskevich.event.events.impl.EventPacket;
import ru.tuskevich.event.EventTarget;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "WorldTime", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd", type = Type.RENDER)
public class WorldTime extends Module
{
    public SliderSetting time;
    
    public WorldTime() {
        this.time = new SliderSetting("Time", 1000.0f, 0.0f, 24000.0f, 100.0f);
        this.add(this.time);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        WorldTime.mc.world.setWorldTime((long)this.time.getFloatValue());
    }
    
    @EventTarget
    public void onPacket(final EventPacket e) {
        if (e.getPacket() instanceof SPacketTimeUpdate) {
            e.cancel();
        }
    }
}
