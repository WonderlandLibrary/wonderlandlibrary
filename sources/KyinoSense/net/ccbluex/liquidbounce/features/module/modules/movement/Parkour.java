/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
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
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Parkour", description="Automatically jumps when reaching the edge of a block.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/Parkour;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class Parkour
extends Module {
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (MovementUtils.isMoving() && Parkour.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
            EntityPlayerSP entityPlayerSP = Parkour.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (!entityPlayerSP.func_70093_af()) {
                KeyBinding keyBinding = Parkour.access$getMc$p$s1046033730().field_71474_y.field_74311_E;
                Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindSneak");
                if (!keyBinding.func_151470_d()) {
                    KeyBinding keyBinding2 = Parkour.access$getMc$p$s1046033730().field_71474_y.field_74314_A;
                    Intrinsics.checkExpressionValueIsNotNull(keyBinding2, "mc.gameSettings.keyBindJump");
                    if (!keyBinding2.func_151470_d()) {
                        WorldClient worldClient = Parkour.access$getMc$p$s1046033730().field_71441_e;
                        Entity entity = (Entity)Parkour.access$getMc$p$s1046033730().field_71439_g;
                        EntityPlayerSP entityPlayerSP2 = Parkour.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                        if (worldClient.func_72945_a(entity, entityPlayerSP2.func_174813_aQ().func_72317_d(0.0, -0.5, 0.0).func_72314_b(-0.001, 0.0, -0.001)).isEmpty()) {
                            Parkour.access$getMc$p$s1046033730().field_71439_g.func_70664_aZ();
                        }
                    }
                }
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

