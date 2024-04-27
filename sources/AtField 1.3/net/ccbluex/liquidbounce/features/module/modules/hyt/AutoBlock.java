/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.FloatValue;

@ModuleInfo(name="HytAutoBlock", description="IDeaX", category=ModuleCategory.HYT)
public final class AutoBlock
extends Module {
    private FloatValue blockrange = new FloatValue("BlockRange", 5.0f, 0.0f, 8.0f);
    private boolean blocking;

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        KillAura killAura = (KillAura)module;
        if (killAura.getTarget() != null) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            IEntityLivingBase iEntityLivingBase = killAura.getTarget();
            if (iEntityLivingBase == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getDistanceToEntity(iEntityLivingBase) <= ((Number)this.blockrange.get()).floatValue()) {
                MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().setPressed(true);
                this.blocking = true;
            }
        } else if (this.blocking) {
            MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().setPressed(false);
            this.blocking = false;
        }
    }
}

