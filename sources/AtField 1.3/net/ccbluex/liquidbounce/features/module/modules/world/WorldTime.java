/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.text.StringsKt
 *  net.minecraft.client.Minecraft
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import kotlin.TypeCastException;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;

@ModuleInfo(name="WorldTime", description="\u6539\u53d8\u4f60\u7684\u65f6\u95f4\u548c\u5929\u6c14", category=ModuleCategory.AUTUMN)
public final class WorldTime
extends Module {
    private long timeCycle;
    private final FloatValue rainStrengthValue;
    private final ListValue tagValue;
    private final ListValue timeModeValue = new ListValue("Time", new String[]{"Static", "Cycle"}, "Static");
    private final IntegerValue cycleSpeedValue = new IntegerValue("CycleSpeed", 1, -24, 24);
    private final IntegerValue worldTimeValue = new IntegerValue("Time", 18000, 0, 24000);
    private final ListValue weatherModeValue = new ListValue("Weather", new String[]{"Clear", "Rain", "NoModification"}, "Clear");

    public WorldTime() {
        this.rainStrengthValue = new FloatValue("RainStrength", 0.1f, 0.01f, 1.0f);
        this.tagValue = new ListValue("Tag", new String[]{"TimeOnly", "Simplified", "Detailed", "None"}, "TimeOnly");
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        if (StringsKt.equals((String)((String)this.timeModeValue.get()), (String)"static", (boolean)true)) {
            Minecraft.func_71410_x().field_71441_e.func_72877_b((long)((Number)this.worldTimeValue.get()).intValue());
        } else {
            MinecraftInstance.mc2.field_71441_e.func_72877_b(this.timeCycle);
            this.timeCycle += (long)(((Number)this.cycleSpeedValue.get()).intValue() * 10);
            if (this.timeCycle > 24000L) {
                this.timeCycle = 0L;
            }
            if (this.timeCycle < 0L) {
                this.timeCycle = 24000L;
            }
        }
        if (!StringsKt.equals((String)((String)this.weatherModeValue.get()), (String)"nomodification", (boolean)true)) {
            MinecraftInstance.mc2.field_71441_e.func_72894_k(StringsKt.equals((String)((String)this.weatherModeValue.get()), (String)"clear", (boolean)true) ? 0.0f : ((Number)this.rainStrengthValue.get()).floatValue());
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        if (MinecraftInstance.classProvider.isSPacketTimeUpdate(packetEvent.getPacket())) {
            packetEvent.cancelEvent();
        }
    }

    @Override
    public String getTag() {
        String string;
        String string2 = (String)this.tagValue.get();
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string3.toLowerCase()) {
            case "timeonly": {
                if (StringsKt.equals((String)((String)this.timeModeValue.get()), (String)"static", (boolean)true)) {
                    string = String.valueOf(((Number)this.worldTimeValue.get()).intValue());
                    break;
                }
                string = String.valueOf(this.timeCycle);
                break;
            }
            case "simplified": {
                string = (StringsKt.equals((String)((String)this.timeModeValue.get()), (String)"static", (boolean)true) ? String.valueOf(((Number)this.worldTimeValue.get()).intValue()) : String.valueOf(this.timeCycle)) + ", " + (String)this.weatherModeValue.get();
                break;
            }
            case "detailed": {
                string = "Time: " + (StringsKt.equals((String)((String)this.timeModeValue.get()), (String)"static", (boolean)true) ? String.valueOf(((Number)this.worldTimeValue.get()).intValue()) : "Cycle, " + String.valueOf(this.timeCycle)) + ", Weather: " + (String)this.weatherModeValue.get();
                break;
            }
            default: {
                string = null;
            }
        }
        return string;
    }

    @Override
    public void onEnable() {
        this.timeCycle = 0L;
    }
}

