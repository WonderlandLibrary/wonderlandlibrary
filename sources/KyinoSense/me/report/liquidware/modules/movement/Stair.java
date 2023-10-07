/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockStairs
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Stair", description=":/", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2={"Lme/report/liquidware/modules/movement/Stair;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "canJump", "", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "tag", "", "getTag", "()Ljava/lang/String;", "walkingDown", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class Stair
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"NCP", "Blocksmc"}, "Blocksmc");
    private boolean canJump;
    private boolean walkingDown;

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onUpdate(@NotNull UpdateEvent event) {
        double motion;
        block17: {
            block16: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                if (!MovementUtils.isMoving()) break block16;
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(Speed.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                if (!module.getState()) break block17;
            }
            return;
        }
        if (Stair.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 0.0f && !this.walkingDown) {
            this.walkingDown = true;
        } else if (Stair.access$getMc$p$s1046033730().field_71439_g.field_70163_u > Stair.access$getMc$p$s1046033730().field_71439_g.field_71096_bN) {
            this.walkingDown = false;
        }
        String mode = (String)this.modeValue.get();
        if (!Stair.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
            return;
        }
        double d = Stair.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
        EntityPlayerSP entityPlayerSP = Stair.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        BlockPos blockPos = new BlockPos(d, entityPlayerSP.func_174813_aQ().field_72338_b, Stair.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
        if (BlockUtils.getBlock(blockPos) instanceof BlockStairs && !this.walkingDown) {
            Stair.access$getMc$p$s1046033730().field_71439_g.func_70107_b(Stair.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Stair.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 0.5, Stair.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
            motion = StringsKt.equals(mode, "NCP", true) ? 1.4 : (StringsKt.equals(mode, "Blocksmc", true) ? 1.4 : 1.0);
            Stair.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= motion;
            Stair.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= motion;
        }
        if (BlockUtils.getBlock(blockPos.func_177977_b()) instanceof BlockStairs) {
            if (this.walkingDown) {
                if (StringsKt.equals(mode, "NCP", true)) {
                    Stair.access$getMc$p$s1046033730().field_71439_g.field_70181_x = -1.0;
                } else if (StringsKt.equals(mode, "Blocksmc", true)) {
                    Stair.access$getMc$p$s1046033730().field_71439_g.field_70181_x = -1.0;
                }
                return;
            }
            motion = StringsKt.equals(mode, "NCP", true) ? 1.3 : (StringsKt.equals(mode, "Blocksmc", true) ? 1.3 : 1.3);
            Stair.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= motion;
            Stair.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= motion;
            this.canJump = true;
        } else if (StringsKt.startsWith(mode, "AAC", true) && this.canJump) {
            this.canJump = false;
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

