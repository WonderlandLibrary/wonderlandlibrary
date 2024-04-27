package net.ccbluex.liquidbounce.features.module.modules.world;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
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
import net.minecraft.client.multiplayer.WorldClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Ambience", description="Change your world time and weather client-side.", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000B\n\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\t\n\b\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J\b0HJ020HJ020HR0X¢\n\u0000R0X¢\n\u0000R0\b8VX¢\b\t\nR0\fX¢\n\u0000R\r0X¢\n\u0000R0\fX¢\n\u0000R0\fX¢\n\u0000R0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/Ambience;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "cycleSpeedValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "rainStrengthValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "tag", "", "getTag", "()Ljava/lang/String;", "tagValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "timeCycle", "", "timeModeValue", "weatherModeValue", "worldTimeValue", "onEnable", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class Ambience
extends Module {
    private final ListValue timeModeValue = new ListValue("Time", new String[]{"Static", "Cycle"}, "Static");
    private final IntegerValue cycleSpeedValue = new IntegerValue("CycleSpeed", 1, -24, 24);
    private final IntegerValue worldTimeValue = new IntegerValue("Time", 12000, 0, 24000);
    private final ListValue weatherModeValue = new ListValue("Weather", new String[]{"Clear", "Rain", "NoModification"}, "Clear");
    private final FloatValue rainStrengthValue = new FloatValue("RainStrength", 0.1f, 0.01f, 1.0f);
    private final ListValue tagValue = new ListValue("Tag", new String[]{"TimeOnly", "Simplified", "Detailed", "None"}, "TimeOnly");
    private long timeCycle;

    @Override
    public void onEnable() {
        this.timeCycle = 0L;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (MinecraftInstance.classProvider.isSPacketTimeUpdate(event.getPacket())) {
            event.cancelEvent();
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (StringsKt.equals((String)this.timeModeValue.get(), "static", true)) {
            WorldClient worldClient = Minecraft.getMinecraft().world;
            Intrinsics.checkExpressionValueIsNotNull(worldClient, "Minecraft.getMinecraft().world");
            worldClient.setWorldTime((long)((Number)this.worldTimeValue.get()).intValue());
        } else {
            WorldClient worldClient = MinecraftInstance.mc2.world;
            Intrinsics.checkExpressionValueIsNotNull(worldClient, "mc2.world");
            worldClient.setWorldTime(this.timeCycle);
            this.timeCycle += (long)(((Number)this.cycleSpeedValue.get()).intValue() * 10);
            if (this.timeCycle > 24000L) {
                this.timeCycle = 0L;
            }
            if (this.timeCycle < 0L) {
                this.timeCycle = 24000L;
            }
        }
        if (!StringsKt.equals((String)this.weatherModeValue.get(), "nomodification", true)) {
            MinecraftInstance.mc2.world.func_72894_k(StringsKt.equals((String)this.weatherModeValue.get(), "clear", true) ? 0.0f : ((Number)this.rainStrengthValue.get()).floatValue());
        }
    }

    @Override
    @Nullable
    public String getTag() {
        String string;
        String string2 = (String)this.tagValue.get();
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string4 = string3.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).toLowerCase()");
        switch (string4) {
            case "timeonly": {
                if (StringsKt.equals((String)this.timeModeValue.get(), "static", true)) {
                    string = String.valueOf(((Number)this.worldTimeValue.get()).intValue());
                    break;
                }
                string = String.valueOf(this.timeCycle);
                break;
            }
            case "simplified": {
                string = (StringsKt.equals((String)this.timeModeValue.get(), "static", true) ? String.valueOf(((Number)this.worldTimeValue.get()).intValue()) : String.valueOf(this.timeCycle)) + ", " + (String)this.weatherModeValue.get();
                break;
            }
            case "detailed": {
                string = "Time: " + (StringsKt.equals((String)this.timeModeValue.get(), "static", true) ? String.valueOf(((Number)this.worldTimeValue.get()).intValue()) : "Cycle, " + String.valueOf(this.timeCycle)) + ", Weather: " + (String)this.weatherModeValue.get();
                break;
            }
            default: {
                string = null;
            }
        }
        return string;
    }
}
