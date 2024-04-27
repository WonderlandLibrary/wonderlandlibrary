/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.combat.BowAimbot;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.value.BoolValue;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

@Info(name="AutoBow", spacedName="Auto Bow", description="Automatically shoots an arrow whenever your bow is fully loaded.", category=Category.COMBAT, cnName="\u81ea\u52a8\u5c04\u7bad")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lnet/dev/important/modules/module/modules/combat/AutoBow;", "Lnet/dev/important/modules/module/Module;", "()V", "waitForBowAimbot", "Lnet/dev/important/value/BoolValue;", "onUpdate", "", "event", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class AutoBow
extends Module {
    @NotNull
    private final BoolValue waitForBowAimbot = new BoolValue("WaitForBowAimbot", true);

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Module module2 = Client.INSTANCE.getModuleManager().get(BowAimbot.class);
        if (module2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.combat.BowAimbot");
        }
        BowAimbot bowAimbot = (BowAimbot)module2;
        if (MinecraftInstance.mc.field_71439_g.func_71039_bw()) {
            ItemStack itemStack = MinecraftInstance.mc.field_71439_g.func_70694_bm();
            if (Intrinsics.areEqual(itemStack == null ? null : itemStack.func_77973_b(), Items.field_151031_f) && MinecraftInstance.mc.field_71439_g.func_71057_bx() > 20 && (!((Boolean)this.waitForBowAimbot.get()).booleanValue() || !bowAimbot.getState() || bowAimbot.hasTarget())) {
                MinecraftInstance.mc.field_71439_g.func_71034_by();
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
            }
        }
    }
}

