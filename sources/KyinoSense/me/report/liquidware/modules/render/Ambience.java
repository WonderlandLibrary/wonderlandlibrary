/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S03PacketTimeUpdate
 *  net.minecraft.network.play.server.S2BPacketChangeGameState
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

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
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Ambience", category=ModuleCategory.RENDER, description="Automatically attacks targets around you.")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015H\u0007J\u0010\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0017H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lme/report/liquidware/modules/render/Ambience;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "changeWorldTimeSpeedValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "customWorldTimeValue", "i", "", "getI", "()J", "setI", "(J)V", "timeModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "weatherModeValue", "weatherStrengthValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "onDisable", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class Ambience
extends Module {
    private final ListValue timeModeValue = new ListValue("TimeMode", new String[]{"None", "Normal", "Custom"}, "Normal");
    private final ListValue weatherModeValue = new ListValue("WeatherMode", new String[]{"None", "Sunny", "Rainy", "Thunder"}, "None");
    private final IntegerValue customWorldTimeValue = new IntegerValue("CustomTime", 1000, 0, 24000);
    private final IntegerValue changeWorldTimeSpeedValue = new IntegerValue("ChangeWorldTimeSpeed", 150, 10, 500);
    private final FloatValue weatherStrengthValue = new FloatValue("WeatherStrength", 1.0f, 0.0f, 1.0f);
    private long i;

    public final long getI() {
        return this.i;
    }

    public final void setI(long l) {
        this.i = l;
    }

    @Override
    public void onDisable() {
        this.i = 0L;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block15: {
            Intrinsics.checkParameterIsNotNull(event, "event");
            String string = (String)this.timeModeValue.get();
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "normal": {
                    this.i = this.i < (long)24000 ? (this.i += ((Number)this.changeWorldTimeSpeedValue.get()).longValue()) : 0L;
                    WorldClient worldClient = Ambience.access$getMc$p$s1046033730().field_71441_e;
                    Intrinsics.checkExpressionValueIsNotNull(worldClient, "mc.theWorld");
                    worldClient.func_72877_b(this.i);
                    break;
                }
                case "custom": {
                    WorldClient worldClient = Ambience.access$getMc$p$s1046033730().field_71441_e;
                    Intrinsics.checkExpressionValueIsNotNull(worldClient, "mc.theWorld");
                    worldClient.func_72877_b((long)((Number)this.customWorldTimeValue.get()).intValue());
                }
            }
            string = (String)this.weatherModeValue.get();
            bl = false;
            String string4 = string;
            if (string4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string5 = string4.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string5, "(this as java.lang.String).toLowerCase()");
            string = string5;
            switch (string.hashCode()) {
                case 108275557: {
                    if (!string.equals("rainy")) return;
                    break;
                }
                case -1334895388: {
                    if (!string.equals("thunder")) return;
                    break block15;
                }
                case 109799703: {
                    if (!string.equals("sunny")) return;
                    Ambience.access$getMc$p$s1046033730().field_71441_e.func_72894_k(0.0f);
                    Ambience.access$getMc$p$s1046033730().field_71441_e.func_147442_i(0.0f);
                    return;
                }
            }
            Ambience.access$getMc$p$s1046033730().field_71441_e.func_72894_k(((Number)this.weatherStrengthValue.get()).floatValue());
            Ambience.access$getMc$p$s1046033730().field_71441_e.func_147442_i(0.0f);
            return;
        }
        Ambience.access$getMc$p$s1046033730().field_71441_e.func_72894_k(((Number)this.weatherStrengthValue.get()).floatValue());
        Ambience.access$getMc$p$s1046033730().field_71441_e.func_147442_i(((Number)this.weatherStrengthValue.get()).floatValue());
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        int n;
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        if (!StringsKt.equals((String)this.timeModeValue.get(), "none", true) && packet instanceof S03PacketTimeUpdate) {
            event.cancelEvent();
        }
        if (!StringsKt.equals((String)this.weatherModeValue.get(), "none", true) && packet instanceof S2BPacketChangeGameState && 7 <= (n = ((S2BPacketChangeGameState)packet).func_149138_c()) && 8 >= n) {
            event.cancelEvent();
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

