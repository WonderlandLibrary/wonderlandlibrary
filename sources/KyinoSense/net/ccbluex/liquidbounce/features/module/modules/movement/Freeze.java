/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Freeze", description="Allows you to stay stuck in mid air.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0007\u00a8\u0006\b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/Freeze;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onDisable", "", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class Freeze
extends Module {
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Freeze.access$getMc$p$s1046033730().field_71439_g.field_70128_L = true;
        Freeze.access$getMc$p$s1046033730().field_71439_g.field_70177_z = Freeze.access$getMc$p$s1046033730().field_71439_g.field_71109_bG;
        Freeze.access$getMc$p$s1046033730().field_71439_g.field_70125_A = Freeze.access$getMc$p$s1046033730().field_71439_g.field_70726_aT;
    }

    @Override
    public void onDisable() {
        block0: {
            if (Freeze.access$getMc$p$s1046033730().field_71439_g == null) break block0;
            Freeze.access$getMc$p$s1046033730().field_71439_g.field_70128_L = false;
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

