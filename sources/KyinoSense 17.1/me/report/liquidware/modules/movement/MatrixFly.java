/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.report.liquidware.modules.movement.MatrixFly;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="MatrixFly", category=ModuleCategory.MOVEMENT, description="Matrix Fly")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0007J\b\u0010\u000e\u001a\u00020\u000bH\u0016J\b\u0010\u000f\u001a\u00020\u000bH\u0016J\u0010\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0011H\u0007J\u0010\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0013H\u0007J\u0010\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0015H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lme/report/liquidware/modules/movement/MatrixFly;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "airCount", "", "dontPlace", "", "hasEmptySlot", "resetFallDist", "yChanged", "onBlockBB", "", "event", "Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "onDisable", "onEnable", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class MatrixFly
extends Module {
    private final boolean resetFallDist;
    private boolean dontPlace;
    private int airCount;
    private boolean yChanged;
    private boolean hasEmptySlot;

    /*
     * WARNING - void declaration
     */
    @Override
    public void onEnable() {
        this.dontPlace = true;
        this.airCount = 0;
        this.yChanged = false;
        this.hasEmptySlot = false;
        int n = 0;
        int n2 = 8;
        while (n <= n2) {
            void i;
            if (MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[i] == null) {
                this.hasEmptySlot = true;
            }
            ++i;
        }
        if (!this.hasEmptySlot) {
            ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lKyinoClient\u00a7a\u00a7lFly\u00a78] \u00a7aYou need to have an empty slot to fly.");
        }
    }

    @Override
    public void onDisable() {
        MatrixFly.access$getMc$p$s1046033730().func_152344_a((Runnable)onDisable.1.INSTANCE);
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getEventState() == EventState.PRE && this.resetFallDist && MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70163_u < MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 0.15 && MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70163_u > MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 0.05) {
            int n = this.airCount;
            this.airCount = n + 1;
            if (this.airCount >= 3) {
                PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70165_t, MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70163_u, MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70161_v, MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70177_z, MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70125_A, true));
                Minecraft minecraft = MatrixFly.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1, null, 0.0f, 0.0f, 0.0f));
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.yChanged) {
            MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
            MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
            MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = 0.0f;
        }
        int n = 0;
        int n2 = 8;
        while (n <= n2) {
            void i;
            if (MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[i] == null) {
                Minecraft minecraft = MatrixFly.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange((int)i));
                break;
            }
            ++i;
        }
        if (!this.dontPlace || MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 1.0 > MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70163_u) {
            this.dontPlace = true;
            Minecraft minecraft = MatrixFly.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1, null, 0.0f, 0.0f, 0.0f));
        }
        if (MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
            if (!this.yChanged) {
                if (MatrixFly.access$getMc$p$s1046033730().field_71474_y.field_74314_A.field_74513_e) {
                    this.yChanged = true;
                    MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70163_u += 1.0;
                } else if (MatrixFly.access$getMc$p$s1046033730().field_71474_y.field_74311_E.field_74513_e) {
                    this.yChanged = true;
                    MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70163_u -= 1.0;
                }
            } else {
                this.yChanged = false;
            }
            MatrixFly.access$getMc$p$s1046033730().field_71439_g.func_70664_aZ();
            if (this.yChanged) {
                MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
            }
            this.dontPlace = true;
            Minecraft minecraft = MatrixFly.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), -1, null, 0.0f, 0.0f, 0.0f));
        }
        MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70122_E = false;
        if (MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70181_x < 0.0) {
            MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.7;
            MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.7;
        }
        MatrixFly.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.7f;
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            ((C03PacketPlayer)packet).field_149474_g = false;
        }
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onBlockBB(@NotNull BlockBBEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getBlock() instanceof BlockAir && (double)event.getY() <= MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70163_u) {
            event.setBoundingBox(AxisAlignedBB.func_178781_a((double)event.getX(), (double)event.getY(), (double)event.getZ(), (double)((double)event.getX() + 1.0), (double)MatrixFly.access$getMc$p$s1046033730().field_71439_g.field_70163_u, (double)((double)event.getZ() + 1.0)));
        }
    }

    public MatrixFly() {
        this.resetFallDist = true;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

