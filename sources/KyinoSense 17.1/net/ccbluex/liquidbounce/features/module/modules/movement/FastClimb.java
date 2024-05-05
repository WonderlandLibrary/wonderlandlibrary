/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockLadder
 *  net.minecraft.block.BlockVine
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.FastClimb$WhenMappings;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="FastClimb", description="Allows you to climb up ladders and vines faster.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007J\u0010\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0012H\u0007R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0013"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/FastClimb;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "speedValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "tag", "", "getTag", "()Ljava/lang/String;", "onBlockBB", "", "event", "Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "KyinoClient"})
public final class FastClimb
extends Module {
    @NotNull
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Clip", "AAC3.0.0", "AAC3.0.5", "SAAC3.1.2", "AAC3.1.2"}, "Vanilla");
    private final FloatValue speedValue = new FloatValue("Speed", 0.2872f, 0.01f, 5.0f);

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     */
    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals(mode, "Vanilla", true) && FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70123_F) {
            EntityPlayerSP entityPlayerSP = FastClimb.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_70617_f_()) {
                event.setY(((Number)this.speedValue.get()).floatValue());
                FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                return;
            }
        }
        if (StringsKt.equals(mode, "AAC3.0.0", true) && FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70123_F) {
            double x = 0.0;
            double z = 0.0;
            EntityPlayerSP entityPlayerSP = FastClimb.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            EnumFacing enumFacing = entityPlayerSP.func_174811_aO();
            if (enumFacing != null) {
                switch (FastClimb$WhenMappings.$EnumSwitchMapping$0[enumFacing.ordinal()]) {
                    case 1: {
                        z = -0.99;
                        break;
                    }
                    case 2: {
                        x = 0.99;
                        break;
                    }
                    case 3: {
                        z = 0.99;
                        break;
                    }
                    case 4: {
                        x = -0.99;
                        break;
                    }
                }
            }
            Block block = BlockUtils.getBlock(new BlockPos(FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70165_t + x, FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70163_u, FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70161_v + z));
            if (!(block instanceof BlockLadder)) {
                if (!(block instanceof BlockVine)) return;
            }
            event.setY(0.5);
            FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
            return;
        }
        if (StringsKt.equals(mode, "AAC3.0.5", true)) {
            KeyBinding keyBinding = FastClimb.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
            Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindForward");
            if (keyBinding.func_151470_d()) {
                EntityPlayerSP entityPlayerSP = FastClimb.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                AxisAlignedBB axisAlignedBB = entityPlayerSP.func_174813_aQ();
                Intrinsics.checkExpressionValueIsNotNull(axisAlignedBB, "mc.thePlayer.entityBoundingBox");
                if (BlockUtils.collideBlockIntersects(axisAlignedBB, new BlockUtils.Collidable(){

                    public boolean collideBlock(@Nullable Block block) {
                        return block instanceof BlockLadder || block instanceof BlockVine;
                    }
                })) {
                    event.setX(0.0);
                    event.setY(0.5);
                    event.setZ(0.0);
                    FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                    FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                    FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                    return;
                }
            }
        }
        if (StringsKt.equals(mode, "SAAC3.1.2", true) && FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70123_F) {
            EntityPlayerSP entityPlayerSP = FastClimb.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_70617_f_()) {
                event.setY(0.1649);
                FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                return;
            }
        }
        if (StringsKt.equals(mode, "AAC3.1.2", true) && FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70123_F) {
            EntityPlayerSP entityPlayerSP = FastClimb.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_70617_f_()) {
                event.setY(0.1699);
                FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                return;
            }
        }
        if (!StringsKt.equals(mode, "Clip", true)) return;
        EntityPlayerSP entityPlayerSP = FastClimb.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (!entityPlayerSP.func_70617_f_()) return;
        KeyBinding keyBinding = FastClimb.access$getMc$p$s1046033730().field_71474_y.field_74351_w;
        Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindForward");
        if (!keyBinding.func_151470_d()) return;
        int x = (int)FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
        int n = (int)FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 8;
        if (x > n) return;
        while (true) {
            void i;
            Block block;
            if (!((block = BlockUtils.getBlock(new BlockPos(FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70165_t, (double)i, FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70161_v))) instanceof BlockLadder)) {
                double x2 = 0.0;
                double z = 0.0;
                EntityPlayerSP entityPlayerSP2 = FastClimb.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                EnumFacing enumFacing = entityPlayerSP2.func_174811_aO();
                if (enumFacing != null) {
                    switch (FastClimb$WhenMappings.$EnumSwitchMapping$1[enumFacing.ordinal()]) {
                        case 1: {
                            z = -1.0;
                            break;
                        }
                        case 2: {
                            x2 = 1.0;
                            break;
                        }
                        case 3: {
                            z = 1.0;
                            break;
                        }
                        case 4: {
                            x2 = -1.0;
                            break;
                        }
                    }
                }
                FastClimb.access$getMc$p$s1046033730().field_71439_g.func_70107_b(FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70165_t + x2, (double)i, FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70161_v + z);
                return;
            }
            FastClimb.access$getMc$p$s1046033730().field_71439_g.func_70107_b(FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70165_t, (double)i, FastClimb.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
            if (i == n) return;
            ++i;
        }
    }

    @EventTarget
    public final void onBlockBB(@NotNull BlockBBEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (FastClimb.access$getMc$p$s1046033730().field_71439_g != null && (event.getBlock() instanceof BlockLadder || event.getBlock() instanceof BlockVine) && StringsKt.equals((String)this.modeValue.get(), "AAC3.0.5", true)) {
            EntityPlayerSP entityPlayerSP = FastClimb.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_70617_f_()) {
                event.setBoundingBox(null);
            }
        }
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

