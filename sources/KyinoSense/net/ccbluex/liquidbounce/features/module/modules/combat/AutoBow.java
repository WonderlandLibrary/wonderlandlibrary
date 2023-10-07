/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.BowAimbot;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoBow", description="Automatically shoots an arrow whenever your bow is fully loaded.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/AutoBow;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "waitForBowAimbot", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class AutoBow
extends Module {
    private final BoolValue waitForBowAimbot = new BoolValue("WaitForBowAimbot", true);

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(BowAimbot.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.BowAimbot");
        }
        BowAimbot bowAimbot = (BowAimbot)module;
        EntityPlayerSP entityPlayerSP = AutoBow.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (entityPlayerSP.func_71039_bw()) {
            EntityPlayerSP entityPlayerSP2 = AutoBow.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
            ItemStack itemStack = entityPlayerSP2.func_70694_bm();
            if (Intrinsics.areEqual(itemStack != null ? itemStack.func_77973_b() : null, Items.field_151031_f)) {
                EntityPlayerSP entityPlayerSP3 = AutoBow.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                if (!(entityPlayerSP3.func_71057_bx() <= 20 || ((Boolean)this.waitForBowAimbot.get()).booleanValue() && bowAimbot.getState() && !bowAimbot.hasTarget())) {
                    AutoBow.access$getMc$p$s1046033730().field_71439_g.func_71034_by();
                    Minecraft minecraft = AutoBow.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
                }
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

