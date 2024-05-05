/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.item.ItemFishingRod
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoFish", description="Automatically catches fish when using a rod.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/AutoFish;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "rodOutTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class AutoFish
extends Module {
    private final MSTimer rodOutTimer = new MSTimer();

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block5: {
            block4: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                EntityPlayerSP entityPlayerSP = AutoFish.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                if (entityPlayerSP.func_70694_bm() == null) break block4;
                EntityPlayerSP entityPlayerSP2 = AutoFish.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                ItemStack itemStack = entityPlayerSP2.func_70694_bm();
                Intrinsics.checkExpressionValueIsNotNull(itemStack, "mc.thePlayer.heldItem");
                if (itemStack.func_77973_b() instanceof ItemFishingRod) break block5;
            }
            return;
        }
        if (this.rodOutTimer.hasTimePassed(500L) && AutoFish.access$getMc$p$s1046033730().field_71439_g.field_71104_cf == null || AutoFish.access$getMc$p$s1046033730().field_71439_g.field_71104_cf != null && AutoFish.access$getMc$p$s1046033730().field_71439_g.field_71104_cf.field_70159_w == 0.0 && AutoFish.access$getMc$p$s1046033730().field_71439_g.field_71104_cf.field_70179_y == 0.0 && AutoFish.access$getMc$p$s1046033730().field_71439_g.field_71104_cf.field_70181_x != 0.0) {
            AutoFish.access$getMc$p$s1046033730().func_147121_ag();
            this.rodOutTimer.reset();
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

