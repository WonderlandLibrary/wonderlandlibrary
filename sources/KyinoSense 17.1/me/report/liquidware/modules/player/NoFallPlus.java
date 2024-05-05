/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.player;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.player.NoFall;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NoFallPlus", category=ModuleCategory.PLAYER, description="Nofall + plus = nofall plus")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lme/report/liquidware/modules/player/NoFallPlus;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class NoFallPlus
extends Module {
    /*
     * Enabled aggressive block sorting
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(NoFall.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.NoFall");
        }
        NoFall nofall = (NoFall)module;
        WorldClient worldClient = NoFallPlus.access$getMc$p$s1046033730().field_71441_e;
        Entity entity = (Entity)NoFallPlus.access$getMc$p$s1046033730().field_71439_g;
        EntityPlayerSP entityPlayerSP = NoFallPlus.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (worldClient.func_72945_a(entity, entityPlayerSP.func_174813_aQ().func_72317_d(0.0, 0.0, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
            WorldClient worldClient2 = NoFallPlus.access$getMc$p$s1046033730().field_71441_e;
            Entity entity2 = (Entity)NoFallPlus.access$getMc$p$s1046033730().field_71439_g;
            EntityPlayerSP entityPlayerSP2 = NoFallPlus.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
            if (worldClient2.func_72945_a(entity2, entityPlayerSP2.func_174813_aQ().func_72317_d(0.0, -10002.25, 0.0).func_72314_b(0.0, -10003.75, 0.0)).isEmpty()) {
                Module module2 = LiquidBounce.INSTANCE.getModuleManager().getModule(NoFall.class);
                if (module2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.NoFall");
                }
                ((NoFall)module2).setState(false);
                return;
            }
        }
        Module module3 = LiquidBounce.INSTANCE.getModuleManager().getModule(NoFall.class);
        if (module3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.NoFall");
        }
        ((NoFall)module3).setState(true);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

