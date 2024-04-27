/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.player.Reach;

@ModuleInfo(name="AutoBlockHelper", description="NMSLGRIMAC SBVL ", category=ModuleCategory.COMBAT)
public final class AutoBlockHelper
extends Module {
    @Override
    @EventTarget
    public void onEnable() {
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        KillAura ka = (KillAura)module;
        Module module2 = LiquidBounce.INSTANCE.getModuleManager().getModule(Reach.class);
        if (module2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.Reach");
        }
        Reach r = (Reach)module2;
        r.getCombatReachValue().set(0);
        r.getBuildReachValue().set(0);
        ka.setState(true);
        r.setState(true);
    }

    @Override
    public void onDisable() {
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        KillAura ka = (KillAura)module;
        Module module2 = LiquidBounce.INSTANCE.getModuleManager().getModule(Reach.class);
        if (module2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.Reach");
        }
        Reach r = (Reach)module2;
        ka.setState(false);
        r.setState(false);
    }
}

