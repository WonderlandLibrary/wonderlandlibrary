/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S03PacketTimeUpdate
 */
package net.dev.important.modules.module.modules.world;

import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

@Info(name="Ambience", description="Change your world time and weather client-side.", category=Category.WORLD, cnName="\u81ea\u5b9a\u4e49\u65f6\u95f4")
public class Ambience
extends Module {
    private final ListValue mode = new ListValue("Time-Mode", new String[]{"Static", "Cycle"}, "Static");
    private final ListValue weathermode = new ListValue("Weather-Mode", new String[]{"Clear", "Rain"}, "Clear");
    private final IntegerValue cycleSpeed = new IntegerValue("Cycle-Speed", 24, 1, 24);
    private final BoolValue reverseCycle = new BoolValue("Reverse-Cycle", false);
    private final IntegerValue time = new IntegerValue("Static-Time", 24000, 0, 24000);
    private final FloatValue rainstrength = new FloatValue("Rain-Strength", 0.1f, 0.1f, 0.5f);
    private final BoolValue displayTag = new BoolValue("Display-Tag", false);
    private int timeCycle = 0;

    @Override
    public void onEnable() {
        this.timeCycle = 0;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (((String)this.mode.get()).equalsIgnoreCase("static")) {
            Ambience.mc.field_71441_e.func_72877_b((long)((Integer)this.time.get()).intValue());
        } else {
            Ambience.mc.field_71441_e.func_72877_b((long)this.timeCycle);
            this.timeCycle += ((Boolean)this.reverseCycle.get() != false ? -((Integer)this.cycleSpeed.get()).intValue() : (Integer)this.cycleSpeed.get()) * 10;
            if (this.timeCycle > 24000) {
                this.timeCycle = 0;
            } else if (this.timeCycle < 0) {
                this.timeCycle = 24000;
            }
        }
        if (((String)this.weathermode.get()).equalsIgnoreCase("clear")) {
            Ambience.mc.field_71441_e.func_72894_k(0.0f);
        } else {
            Ambience.mc.field_71441_e.func_72894_k(((Float)this.rainstrength.get()).floatValue());
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S03PacketTimeUpdate) {
            event.cancelEvent();
        }
    }

    @Override
    public String getTag() {
        return (Boolean)this.displayTag.get() != false ? "Time: " + (((String)this.mode.get()).equalsIgnoreCase("cycle") ? "Cycle" + ((Boolean)this.reverseCycle.get() != false ? ", Reverse" : "") : "Static, " + ((Integer)this.time.get()).toString()) + " | Weather: " + (String)this.weathermode.get() : null;
    }
}

