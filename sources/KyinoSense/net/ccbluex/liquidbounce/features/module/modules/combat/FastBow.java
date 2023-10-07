/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="FastBow", description="Turns your bow into a machine gun.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/FastBow;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "packetsValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class FastBow
extends Module {
    private final IntegerValue packetsValue = new IntegerValue("Packets", 20, 3, 20);

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        EntityPlayerSP entityPlayerSP = FastBow.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (!entityPlayerSP.func_71039_bw()) {
            return;
        }
        if (FastBow.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70448_g() != null) {
            ItemStack itemStack = FastBow.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70448_g();
            Intrinsics.checkExpressionValueIsNotNull(itemStack, "mc.thePlayer.inventory.getCurrentItem()");
            if (itemStack.func_77973_b() instanceof ItemBow) {
                Minecraft minecraft = FastBow.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                NetHandlerPlayClient netHandlerPlayClient = minecraft.func_147114_u();
                EntityPlayerSP entityPlayerSP2 = FastBow.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                netHandlerPlayClient.func_147297_a((Packet)new C08PacketPlayerBlockPlacement(BlockPos.field_177992_a, 255, entityPlayerSP2.func_71045_bC(), 0.0f, 0.0f, 0.0f));
                float yaw = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getYaw() : FastBow.access$getMc$p$s1046033730().field_71439_g.field_70177_z;
                float pitch = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getPitch() : FastBow.access$getMc$p$s1046033730().field_71439_g.field_70125_A;
                int n = 0;
                int n2 = ((Number)this.packetsValue.get()).intValue();
                while (n < n2) {
                    void i;
                    Minecraft minecraft2 = FastBow.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                    minecraft2.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, true));
                    ++i;
                }
                Minecraft minecraft3 = FastBow.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
                minecraft3.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
                EntityPlayerSP entityPlayerSP3 = FastBow.access$getMc$p$s1046033730().field_71439_g;
                ItemStack itemStack2 = FastBow.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70448_g();
                Intrinsics.checkExpressionValueIsNotNull(itemStack2, "mc.thePlayer.inventory.getCurrentItem()");
                entityPlayerSP3.field_71072_f = itemStack2.func_77988_m() - 1;
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

