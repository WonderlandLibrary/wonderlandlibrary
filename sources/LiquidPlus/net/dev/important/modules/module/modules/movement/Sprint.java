/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.client.C0BPacketEntityAction$Action
 *  net.minecraft.potion.Potion
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.JumpEvent;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.MovementUtils;
import net.dev.important.utils.Rotation;
import net.dev.important.utils.RotationUtils;
import net.dev.important.value.BoolValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import org.jetbrains.annotations.NotNull;

@Info(name="AutoSprint", description="Automatically sprints all the time.", category=Category.MOVEMENT, cnName="\u81ea\u52a8\u75be\u8dd1")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0007J\u0010\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001cH\u0007J\u0010\u0010\u001d\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001eH\u0007R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0011\u0010\u000b\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006R\u0011\u0010\r\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006R\u0011\u0010\u000f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0006R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0013\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0006R\u0011\u0010\u0015\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0006\u00a8\u0006\u001f"}, d2={"Lnet/dev/important/modules/module/modules/movement/Sprint;", "Lnet/dev/important/modules/module/Module;", "()V", "allDirectionsValue", "Lnet/dev/important/value/BoolValue;", "getAllDirectionsValue", "()Lnet/dev/important/value/BoolValue;", "blindnessValue", "getBlindnessValue", "checkServerSide", "getCheckServerSide", "checkServerSideGround", "getCheckServerSideGround", "foodValue", "getFoodValue", "jumpDirPatchValue", "getJumpDirPatchValue", "modified", "", "moveDirPatchValue", "getMoveDirPatchValue", "noPacketPatchValue", "getNoPacketPatchValue", "onJump", "", "event", "Lnet/dev/important/event/JumpEvent;", "onPacket", "Lnet/dev/important/event/PacketEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class Sprint
extends Module {
    @NotNull
    private final BoolValue allDirectionsValue = new BoolValue("AllDirections", true);
    @NotNull
    private final BoolValue noPacketPatchValue = new BoolValue("AllDirections-NoPacketsPatch", true, new Function0<Boolean>(this){
        final /* synthetic */ Sprint this$0;
        {
            this.this$0 = $receiver;
            super(0);
        }

        @NotNull
        public final Boolean invoke() {
            return (Boolean)this.this$0.getAllDirectionsValue().get();
        }
    });
    @NotNull
    private final BoolValue moveDirPatchValue = new BoolValue("AllDirections-MoveDirPatch", false, new Function0<Boolean>(this){
        final /* synthetic */ Sprint this$0;
        {
            this.this$0 = $receiver;
            super(0);
        }

        @NotNull
        public final Boolean invoke() {
            return (Boolean)this.this$0.getAllDirectionsValue().get();
        }
    });
    @NotNull
    private final BoolValue jumpDirPatchValue = new BoolValue("MoveDirPatch-JumpOnly", true, new Function0<Boolean>(this){
        final /* synthetic */ Sprint this$0;
        {
            this.this$0 = $receiver;
            super(0);
        }

        @NotNull
        public final Boolean invoke() {
            return (Boolean)this.this$0.getAllDirectionsValue().get() != false && (Boolean)this.this$0.getMoveDirPatchValue().get() != false;
        }
    });
    @NotNull
    private final BoolValue blindnessValue = new BoolValue("Blindness", true);
    @NotNull
    private final BoolValue foodValue = new BoolValue("Food", true);
    @NotNull
    private final BoolValue checkServerSide = new BoolValue("CheckServerSide", false);
    @NotNull
    private final BoolValue checkServerSideGround = new BoolValue("CheckServerSideOnlyGround", false);
    private boolean modified;

    @NotNull
    public final BoolValue getAllDirectionsValue() {
        return this.allDirectionsValue;
    }

    @NotNull
    public final BoolValue getNoPacketPatchValue() {
        return this.noPacketPatchValue;
    }

    @NotNull
    public final BoolValue getMoveDirPatchValue() {
        return this.moveDirPatchValue;
    }

    @NotNull
    public final BoolValue getJumpDirPatchValue() {
        return this.jumpDirPatchValue;
    }

    @NotNull
    public final BoolValue getBlindnessValue() {
        return this.blindnessValue;
    }

    @NotNull
    public final BoolValue getFoodValue() {
        return this.foodValue;
    }

    @NotNull
    public final BoolValue getCheckServerSide() {
        return this.checkServerSide;
    }

    @NotNull
    public final BoolValue getCheckServerSideGround() {
        return this.checkServerSideGround;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Packet<?> packet = event.getPacket();
        if (((Boolean)this.allDirectionsValue.get()).booleanValue() && ((Boolean)this.noPacketPatchValue.get()).booleanValue() && packet instanceof C0BPacketEntityAction && (((C0BPacketEntityAction)packet).func_180764_b() == C0BPacketEntityAction.Action.STOP_SPRINTING || ((C0BPacketEntityAction)packet).func_180764_b() == C0BPacketEntityAction.Action.START_SPRINTING)) {
            event.cancelEvent();
        }
    }

    @EventTarget
    public final void onJump(@NotNull JumpEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (((Boolean)this.allDirectionsValue.get()).booleanValue() && ((Boolean)this.moveDirPatchValue.get()).booleanValue() && ((Boolean)this.jumpDirPatchValue.get()).booleanValue() && !this.modified) {
            event.cancelEvent();
            float prevYaw = MinecraftInstance.mc.field_71439_g.field_70177_z;
            MinecraftInstance.mc.field_71439_g.field_70177_z = MovementUtils.getRawDirection();
            this.modified = true;
            MinecraftInstance.mc.field_71439_g.func_70664_aZ();
            MinecraftInstance.mc.field_71439_g.field_70177_z = prevYaw;
            this.modified = false;
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        KillAura killAura;
        block6: {
            block5: {
                Intrinsics.checkNotNullParameter(event, "event");
                Module module2 = Client.INSTANCE.getModuleManager().getModule(KillAura.class);
                Intrinsics.checkNotNull(module2);
                killAura = (KillAura)module2;
                if (!MovementUtils.isMoving() || MinecraftInstance.mc.field_71439_g.func_70093_af() || ((Boolean)this.blindnessValue.get()).booleanValue() && MinecraftInstance.mc.field_71439_g.func_70644_a(Potion.field_76440_q) || ((Boolean)this.foodValue.get()).booleanValue() && !((float)MinecraftInstance.mc.field_71439_g.func_71024_bL().func_75116_a() > 6.0f) && !MinecraftInstance.mc.field_71439_g.field_71075_bZ.field_75101_c) break block5;
                if (!((Boolean)this.checkServerSide.get()).booleanValue() || !MinecraftInstance.mc.field_71439_g.field_70122_E && ((Boolean)this.checkServerSideGround.get()).booleanValue() || ((Boolean)this.allDirectionsValue.get()).booleanValue() || RotationUtils.targetRotation == null) break block6;
                Rotation rotation = new Rotation(MinecraftInstance.mc.field_71439_g.field_70177_z, MinecraftInstance.mc.field_71439_g.field_70125_A);
                if (!(RotationUtils.getRotationDifference(rotation) > 30.0)) break block6;
            }
            MinecraftInstance.mc.field_71439_g.func_70031_b(false);
            return;
        }
        if (((Boolean)this.allDirectionsValue.get()).booleanValue() || MinecraftInstance.mc.field_71439_g.field_71158_b.field_78900_b >= 0.8f) {
            MinecraftInstance.mc.field_71439_g.func_70031_b(true);
        }
        if (((Boolean)this.allDirectionsValue.get()).booleanValue() && ((Boolean)this.moveDirPatchValue.get()).booleanValue() && !((Boolean)this.jumpDirPatchValue.get()).booleanValue() && killAura.getTarget() == null) {
            RotationUtils.setTargetRotation(new Rotation(MovementUtils.getRawDirection(), MinecraftInstance.mc.field_71439_g.field_70125_A));
        }
    }
}

