/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockLadder
 *  net.minecraft.block.BlockVine
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.event.BlockBBEvent;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MoveEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Info(name="FastClimb", spacedName="Fast Climb", description="Allows you to climb up ladders and vines faster.", category=Category.MOVEMENT, cnName="\u5feb\u901f\u722c\u68af\u5b50")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0007J\u0010\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0018H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2={"Lnet/dev/important/modules/module/modules/movement/FastClimb;", "Lnet/dev/important/modules/module/Module;", "()V", "downSpeedValue", "Lnet/dev/important/value/FloatValue;", "modeValue", "Lnet/dev/important/value/ListValue;", "getModeValue", "()Lnet/dev/important/value/ListValue;", "spartanTimerBoostValue", "Lnet/dev/important/value/BoolValue;", "tag", "", "getTag", "()Ljava/lang/String;", "timerValue", "upSpeedValue", "usedTimer", "", "onBlockBB", "", "event", "Lnet/dev/important/event/BlockBBEvent;", "onMove", "Lnet/dev/important/event/MoveEvent;", "LiquidBounce"})
public final class FastClimb
extends Module {
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final FloatValue upSpeedValue;
    @NotNull
    private final FloatValue downSpeedValue;
    @NotNull
    private final FloatValue timerValue;
    @NotNull
    private final BoolValue spartanTimerBoostValue;
    private boolean usedTimer;

    public FastClimb() {
        String[] stringArray = new String[]{"Vanilla", "Clip", "AAC3.0.0", "AAC3.0.5", "SAAC3.1.2", "AAC3.1.2", "Spartan", "Negativity", "Horizon1.4.6", "HiveMC"};
        this.modeValue = new ListValue("Mode", stringArray, "Vanilla");
        this.upSpeedValue = new FloatValue("UpSpeed", 0.2872f, 0.01f, 10.0f);
        this.downSpeedValue = new FloatValue("DownSpeed", 0.2872f, 0.01f, 10.0f);
        this.timerValue = new FloatValue("Timer", 2.0f, 0.1f, 10.0f, "x");
        this.spartanTimerBoostValue = new BoolValue("SpartanTimerBoost", false);
    }

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        int i;
        Intrinsics.checkNotNullParameter(event, "event");
        if (this.usedTimer) {
            MinecraftInstance.mc.field_71428_T.field_74278_d = 1.0f;
            this.usedTimer = false;
        }
        String mode = (String)this.modeValue.get();
        if (StringsKt.equals(mode, "Vanilla", true) && MinecraftInstance.mc.field_71439_g.func_70617_f_()) {
            if (MinecraftInstance.mc.field_71439_g.field_70123_F) {
                event.setY(((Number)this.upSpeedValue.get()).floatValue());
            } else if (!MinecraftInstance.mc.field_71474_y.field_74311_E.field_74513_e) {
                event.setY(-((double)((Number)this.downSpeedValue.get()).floatValue()));
            }
            MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
            MinecraftInstance.mc.field_71428_T.field_74278_d = ((Number)this.timerValue.get()).floatValue();
            this.usedTimer = true;
            return;
        }
        if (StringsKt.equals(mode, "AAC3.0.0", true) && MinecraftInstance.mc.field_71439_g.field_70123_F) {
            double x = 0.0;
            double z = 0.0;
            EnumFacing enumFacing = MinecraftInstance.mc.field_71439_g.func_174811_aO();
            switch (enumFacing == null ? -1 : WhenMappings.$EnumSwitchMapping$0[enumFacing.ordinal()]) {
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
            Block block = BlockUtils.getBlock(new BlockPos(MinecraftInstance.mc.field_71439_g.field_70165_t + x, MinecraftInstance.mc.field_71439_g.field_70163_u, MinecraftInstance.mc.field_71439_g.field_70161_v + z));
            if (!(block instanceof BlockLadder)) {
                if (!(block instanceof BlockVine)) return;
            }
            event.setY(0.5);
            MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
            return;
        }
        if (StringsKt.equals(mode, "AAC3.0.5", true) && MinecraftInstance.mc.field_71474_y.field_74351_w.func_151470_d()) {
            AxisAlignedBB axisAlignedBB = MinecraftInstance.mc.field_71439_g.func_174813_aQ();
            Intrinsics.checkNotNullExpressionValue(axisAlignedBB, "mc.thePlayer.entityBoundingBox");
            if (BlockUtils.collideBlockIntersects(axisAlignedBB, new BlockUtils.Collidable(){

                public boolean collideBlock(@Nullable Block block) {
                    return block instanceof BlockLadder || block instanceof BlockVine;
                }
            })) {
                event.setX(0.0);
                event.setY(0.5);
                event.setZ(0.0);
                MinecraftInstance.mc.field_71439_g.field_70159_w = 0.0;
                MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
                MinecraftInstance.mc.field_71439_g.field_70179_y = 0.0;
                return;
            }
        }
        if (StringsKt.equals(mode, "SAAC3.1.2", true) && MinecraftInstance.mc.field_71439_g.field_70123_F && MinecraftInstance.mc.field_71439_g.func_70617_f_()) {
            event.setY(0.1649);
            MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
            return;
        }
        if (StringsKt.equals(mode, "AAC3.1.2", true) && MinecraftInstance.mc.field_71439_g.field_70123_F && MinecraftInstance.mc.field_71439_g.func_70617_f_()) {
            event.setY(0.1699);
            MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
            return;
        }
        if (StringsKt.equals(mode, "Spartan", true) && MinecraftInstance.mc.field_71439_g.func_70617_f_()) {
            if (MinecraftInstance.mc.field_71439_g.field_70123_F) {
                event.setY(0.199);
            } else if (!MinecraftInstance.mc.field_71474_y.field_74311_E.field_74513_e) {
                event.setY(-1.3489);
            }
            MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
            if ((Boolean)this.spartanTimerBoostValue.get() == false) return;
            if (!MinecraftInstance.mc.field_71439_g.func_70617_f_()) return;
            if (MinecraftInstance.mc.field_71439_g.field_70173_aa % 2 == 0) {
                MinecraftInstance.mc.field_71428_T.field_74278_d = 2.5f;
            }
            if (MinecraftInstance.mc.field_71439_g.field_70173_aa % 30 == 0) {
                MinecraftInstance.mc.field_71428_T.field_74278_d = 3.0f;
            }
            this.usedTimer = true;
            return;
        }
        if (StringsKt.equals(mode, "Negativity", true) && MinecraftInstance.mc.field_71439_g.func_70617_f_()) {
            if (MinecraftInstance.mc.field_71439_g.field_70123_F) {
                event.setY(0.2299);
            } else if (!MinecraftInstance.mc.field_71474_y.field_74311_E.field_74513_e) {
                event.setY(-0.226);
            }
            MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
            return;
        }
        if (StringsKt.equals(mode, "Twillight", true) && MinecraftInstance.mc.field_71439_g.func_70617_f_()) {
            if (MinecraftInstance.mc.field_71439_g.field_70123_F) {
                event.setY(0.16);
            } else if (!MinecraftInstance.mc.field_71474_y.field_74311_E.field_74513_e) {
                event.setY(-7.99);
            }
            MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
            return;
        }
        if (StringsKt.equals(mode, "Horizon1.4.6", true) && MinecraftInstance.mc.field_71439_g.func_70617_f_()) {
            if (MinecraftInstance.mc.field_71439_g.field_70123_F) {
                event.setY(0.125);
            } else if (!MinecraftInstance.mc.field_71474_y.field_74311_E.field_74513_e) {
                event.setY(-0.16);
            }
            MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
            return;
        }
        if (StringsKt.equals(mode, "HiveMC", true) && MinecraftInstance.mc.field_71439_g.func_70617_f_()) {
            if (MinecraftInstance.mc.field_71439_g.field_70123_F) {
                event.setY(0.179);
            } else if (!MinecraftInstance.mc.field_71474_y.field_74311_E.field_74513_e) {
                event.setY(-0.225);
            }
            MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
            return;
        }
        if (!StringsKt.equals(mode, "Clip", true)) return;
        if (!MinecraftInstance.mc.field_71439_g.func_70617_f_()) return;
        if (!MinecraftInstance.mc.field_71474_y.field_74351_w.func_151470_d()) return;
        int n = (int)MinecraftInstance.mc.field_71439_g.field_70163_u;
        int n2 = (int)MinecraftInstance.mc.field_71439_g.field_70163_u + 8;
        if (n > n2) return;
        do {
            Block block;
            if (!((block = BlockUtils.getBlock(new BlockPos(MinecraftInstance.mc.field_71439_g.field_70165_t, (double)(i = n++), MinecraftInstance.mc.field_71439_g.field_70161_v))) instanceof BlockLadder)) {
                double x = 0.0;
                double z = 0.0;
                EnumFacing enumFacing = MinecraftInstance.mc.field_71439_g.func_174811_aO();
                switch (enumFacing == null ? -1 : WhenMappings.$EnumSwitchMapping$0[enumFacing.ordinal()]) {
                    case 1: {
                        z = -1.0;
                        break;
                    }
                    case 2: {
                        x = 1.0;
                        break;
                    }
                    case 3: {
                        z = 1.0;
                        break;
                    }
                    case 4: {
                        x = -1.0;
                        break;
                    }
                }
                MinecraftInstance.mc.field_71439_g.func_70107_b(MinecraftInstance.mc.field_71439_g.field_70165_t + x, (double)i, MinecraftInstance.mc.field_71439_g.field_70161_v + z);
                return;
            }
            MinecraftInstance.mc.field_71439_g.func_70107_b(MinecraftInstance.mc.field_71439_g.field_70165_t, (double)i, MinecraftInstance.mc.field_71439_g.field_70161_v);
        } while (i != n2);
    }

    @EventTarget
    public final void onBlockBB(@NotNull BlockBBEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MinecraftInstance.mc.field_71439_g != null && (event.getBlock() instanceof BlockLadder || event.getBlock() instanceof BlockVine) && StringsKt.equals((String)this.modeValue.get(), "AAC3.0.5", true) && MinecraftInstance.mc.field_71439_g.func_70617_f_()) {
            event.setBoundingBox(null);
        }
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @Metadata(mv={1, 6, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[EnumFacing.values().length];
            nArray[EnumFacing.NORTH.ordinal()] = 1;
            nArray[EnumFacing.EAST.ordinal()] = 2;
            nArray[EnumFacing.SOUTH.ordinal()] = 3;
            nArray[EnumFacing.WEST.ordinal()] = 4;
            $EnumSwitchMapping$0 = nArray;
        }
    }
}

