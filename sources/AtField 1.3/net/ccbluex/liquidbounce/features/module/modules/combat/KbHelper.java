/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

@ModuleInfo(name="HytAutoVelocity", description="AutoVelocity", category=ModuleCategory.HYT)
public final class KbHelper
extends Module {
    @Override
    public void onEnable() {
        Velocity velocity = null;
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Velocity.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.Velocity");
        }
        velocity = (Velocity)module;
    }

    @EventTarget
    public final void UpdateEvent(UpdateEvent updateEvent) {
        Velocity velocity = null;
        Intrinsics.throwNpe();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        velocity.setState(iEntityPlayerSP.getOnGround());
    }
}

