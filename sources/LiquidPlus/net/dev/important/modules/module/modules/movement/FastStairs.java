/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockStairs
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.movement.Speed;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.MovementUtils;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.ListValue;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

@Info(name="FastStairs", spacedName="Fast Stairs", description="Allows you to climb up stairs faster.", category=Category.MOVEMENT)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/dev/important/modules/module/modules/movement/FastStairs;", "Lnet/dev/important/modules/module/Module;", "()V", "canJump", "", "longJumpValue", "Lnet/dev/important/value/BoolValue;", "modeValue", "Lnet/dev/important/value/ListValue;", "tag", "", "getTag", "()Ljava/lang/String;", "walkingDown", "onUpdate", "", "event", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class FastStairs
extends Module {
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final BoolValue longJumpValue;
    private boolean canJump;
    private boolean walkingDown;

    public FastStairs() {
        String[] stringArray = new String[]{"Step", "NCP", "AAC3.1.0", "AAC3.3.6", "AAC3.3.13"};
        this.modeValue = new ListValue("Mode", stringArray, "NCP");
        this.longJumpValue = new BoolValue("LongJump", false);
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        EntityPlayerSP entityPlayerSP;
        double motion;
        block17: {
            block16: {
                Intrinsics.checkNotNullParameter(event, "event");
                if (!MovementUtils.isMoving()) break block16;
                Module module2 = Client.INSTANCE.getModuleManager().get(Speed.class);
                Intrinsics.checkNotNull(module2);
                if (!module2.getState()) break block17;
            }
            return;
        }
        if (MinecraftInstance.mc.field_71439_g.field_70143_R > 0.0f && !this.walkingDown) {
            this.walkingDown = true;
        } else if (MinecraftInstance.mc.field_71439_g.field_70163_u > MinecraftInstance.mc.field_71439_g.field_71096_bN) {
            this.walkingDown = false;
        }
        String mode = (String)this.modeValue.get();
        if (!MinecraftInstance.mc.field_71439_g.field_70122_E) {
            return;
        }
        BlockPos blockPos = new BlockPos(MinecraftInstance.mc.field_71439_g.field_70165_t, MinecraftInstance.mc.field_71439_g.func_174813_aQ().field_72338_b, MinecraftInstance.mc.field_71439_g.field_70161_v);
        if (BlockUtils.getBlock(blockPos) instanceof BlockStairs && !this.walkingDown) {
            MinecraftInstance.mc.field_71439_g.func_70107_b(MinecraftInstance.mc.field_71439_g.field_70165_t, MinecraftInstance.mc.field_71439_g.field_70163_u + 0.5, MinecraftInstance.mc.field_71439_g.field_70161_v);
            motion = StringsKt.equals(mode, "NCP", true) ? 1.4 : (StringsKt.equals(mode, "AAC3.1.0", true) ? 1.5 : (StringsKt.equals(mode, "AAC3.3.13", true) ? 1.2 : 1.0));
            entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            entityPlayerSP.field_70159_w *= motion;
            entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            entityPlayerSP.field_70179_y *= motion;
        }
        if (BlockUtils.getBlock(blockPos.func_177977_b()) instanceof BlockStairs) {
            if (this.walkingDown) {
                if (StringsKt.equals(mode, "NCP", true)) {
                    MinecraftInstance.mc.field_71439_g.field_70181_x = -1.0;
                } else if (StringsKt.equals(mode, "AAC3.3.13", true)) {
                    EntityPlayerSP motion2 = MinecraftInstance.mc.field_71439_g;
                    motion2.field_70181_x -= 0.014;
                }
                return;
            }
            motion = StringsKt.equals(mode, "NCP", true) ? 1.3 : (StringsKt.equals(mode, "AAC3.1.0", true) ? 1.3 : (StringsKt.equals(mode, "AAC3.3.6", true) ? 1.48 : (StringsKt.equals(mode, "AAC3.3.13", true) ? 1.52 : 1.3)));
            entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            entityPlayerSP.field_70159_w *= motion;
            entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            entityPlayerSP.field_70179_y *= motion;
            this.canJump = true;
        } else if (StringsKt.startsWith(mode, "AAC", true) && this.canJump) {
            if (((Boolean)this.longJumpValue.get()).booleanValue()) {
                MinecraftInstance.mc.field_71439_g.func_70664_aZ();
                EntityPlayerSP entityPlayerSP2 = MinecraftInstance.mc.field_71439_g;
                entityPlayerSP2.field_70159_w *= 1.35;
                entityPlayerSP2 = MinecraftInstance.mc.field_71439_g;
                entityPlayerSP2.field_70179_y *= 1.35;
            }
            this.canJump = false;
        }
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

