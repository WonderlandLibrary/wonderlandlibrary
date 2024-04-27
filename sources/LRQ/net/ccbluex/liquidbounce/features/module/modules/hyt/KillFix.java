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
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="HytKillFix", description="KillFix", category=ModuleCategory.HYT)
public final class KillFix
extends Module {
    private final IntegerValue hurttime = new IntegerValue("hurttime", 9, 1, 10);
    private final IntegerValue hurttime2 = new IntegerValue("hurttime2", 10, 1, 10);
    private final FloatValue AirRange = new FloatValue("AirRange", 3.0f, 0.0f, 5.0f);
    private final FloatValue GroundRange = new FloatValue("GroundRange", 3.5f, 0.0f, 5.0f);
    private final BoolValue Debug = new BoolValue("Debug", true);
    private int ticks;

    public final int getTicks() {
        return this.ticks;
    }

    public final void setTicks(int n) {
        this.ticks = n;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        KillAura killAura;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isAirBorne()) {
            Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
            }
            killAura = (KillAura)module;
            killAura.getRangeValue().set(this.AirRange.get());
            if (((Boolean)this.Debug.get()).booleanValue()) {
                ClientUtils.displayChatMessage("Kafix -> set ka range " + String.valueOf(((Number)this.AirRange.getValue()).floatValue()));
            }
        }
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP2.getOnGround()) {
            Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
            }
            killAura = (KillAura)module;
            killAura.getRangeValue().set(this.GroundRange.get());
            if (((Boolean)this.Debug.get()).booleanValue()) {
                ClientUtils.displayChatMessage("Kafix -> set ka range " + String.valueOf(((Number)this.GroundRange.getValue()).floatValue()));
            }
        }
        int killAura2 = this.ticks;
        this.ticks = killAura2 + 1;
        if (this.ticks == 1) {
            Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
            }
            KillAura killAura3 = (KillAura)module;
            killAura3.getHurtTimeValue().set(this.hurttime.get());
            if (((Boolean)this.Debug.get()).booleanValue()) {
                ClientUtils.displayChatMessage("Kafix -> set ka hurttime " + String.valueOf(((Number)this.hurttime.getValue()).intValue()));
            }
        }
        if (this.ticks == 2) {
            Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
            }
            KillAura killAura4 = (KillAura)module;
            killAura4.getHurtTimeValue().set(this.hurttime2.get());
            if (((Boolean)this.Debug.get()).booleanValue()) {
                ClientUtils.displayChatMessage("Kafix -> set ka hurttime " + String.valueOf(((Number)this.hurttime2.getValue()).intValue()));
            }
        }
        if (this.ticks == 3) {
            this.ticks = 0;
        }
    }
}

