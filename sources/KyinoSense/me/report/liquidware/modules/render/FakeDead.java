/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="FakeDead", category=ModuleCategory.RENDER, description="dwd")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\bH\u0016J\u0010\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lme/report/liquidware/modules/render/FakeDead;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "deathtime", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "hurt", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "onDisable", "", "onEnable", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class FakeDead
extends Module {
    private final IntegerValue deathtime = new IntegerValue("FakeDeathTime", 4, 0, 14);
    private final BoolValue hurt = new BoolValue("FakeGetHurt", false);

    @Override
    public void onEnable() {
        if (FakeDead.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        FakeDead.access$getMc$p$s1046033730().field_71439_g.field_70725_aQ = ((Number)this.deathtime.get()).intValue();
        if (((Boolean)this.hurt.get()).booleanValue()) {
            FakeDead.access$getMc$p$s1046033730().field_71439_g.field_70737_aN = 2;
        }
    }

    @Override
    public void onDisable() {
        if (FakeDead.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        FakeDead.access$getMc$p$s1046033730().field_71439_g.field_70725_aQ = 0;
        if (!((Boolean)this.hurt.get()).booleanValue()) {
            return;
        }
        FakeDead.access$getMc$p$s1046033730().field_71439_g.field_70737_aN = 0;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (FakeDead.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        if (!this.getState()) {
            return;
        }
        FakeDead.access$getMc$p$s1046033730().field_71439_g.field_70725_aQ = ((Number)this.deathtime.get()).intValue();
        if (((Boolean)this.hurt.get()).booleanValue()) {
            FakeDead.access$getMc$p$s1046033730().field_71439_g.field_70737_aN = 2;
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

