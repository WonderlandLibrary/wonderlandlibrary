/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.server.S12PacketEntityVelocity
 *  net.minecraft.network.play.server.S27PacketExplosion
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.JumpEvent;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.StrafeEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.modules.module.modules.movement.Speed;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.MovementUtils;
import net.dev.important.utils.Rotation;
import net.dev.important.utils.RotationUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Info(name="Velocity", spacedName="Velocity", description="Allows you to modify the amount of knockback you take.", category=Category.COMBAT, cnName="\u53cd\u51fb\u9000")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\"\u001a\u00020#H\u0016J\u0010\u0010$\u001a\u00020#2\u0006\u0010%\u001a\u00020&H\u0007J\u0010\u0010'\u001a\u00020#2\u0006\u0010%\u001a\u00020(H\u0007J\u0010\u0010)\u001a\u00020#2\u0006\u0010%\u001a\u00020*H\u0007J\u0010\u0010+\u001a\u00020#2\u0006\u0010%\u001a\u00020,H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0019\u001a\u0004\u0018\u00010\u001a8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u001cR\u000e\u0010\u001d\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006-"}, d2={"Lnet/dev/important/modules/module/modules/combat/Velocity;", "Lnet/dev/important/modules/module/Module;", "()V", "aac5KillAuraValue", "Lnet/dev/important/value/BoolValue;", "aacPushXZReducerValue", "Lnet/dev/important/value/FloatValue;", "aacPushYReducerValue", "aacStrafeValue", "horizontalExplosionValue", "horizontalValue", "jump", "", "legitFaceValue", "legitStrafeValue", "modeValue", "Lnet/dev/important/value/ListValue;", "phaseOffsetValue", "pos", "Lnet/minecraft/util/BlockPos;", "reduceChance", "reverse2StrengthValue", "reverseHurt", "reverseStrengthValue", "shouldAffect", "tag", "", "getTag", "()Ljava/lang/String;", "velocityInput", "velocityTimer", "Lnet/dev/important/utils/timer/MSTimer;", "verticalExplosionValue", "verticalValue", "onDisable", "", "onJump", "event", "Lnet/dev/important/event/JumpEvent;", "onPacket", "Lnet/dev/important/event/PacketEvent;", "onStrafe", "Lnet/dev/important/event/StrafeEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class Velocity
extends Module {
    @NotNull
    private final FloatValue horizontalValue = new FloatValue("Horizontal", 0.0f, -1.0f, 1.0f, "x");
    @NotNull
    private final FloatValue verticalValue = new FloatValue("Vertical", 0.0f, -1.0f, 1.0f, "x");
    @NotNull
    private final FloatValue horizontalExplosionValue = new FloatValue("HorizontalExplosion", 0.0f, 0.0f, 1.0f, "x");
    @NotNull
    private final FloatValue verticalExplosionValue = new FloatValue("VerticalExplosion", 0.0f, 0.0f, 1.0f, "x");
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final BoolValue aac5KillAuraValue;
    @NotNull
    private final FloatValue reduceChance;
    private boolean shouldAffect;
    @NotNull
    private final FloatValue reverseStrengthValue;
    @NotNull
    private final FloatValue reverse2StrengthValue;
    @NotNull
    private final FloatValue aacPushXZReducerValue;
    @NotNull
    private final BoolValue aacPushYReducerValue;
    @NotNull
    private final BoolValue legitStrafeValue;
    @NotNull
    private final BoolValue legitFaceValue;
    @NotNull
    private final BoolValue aacStrafeValue;
    @NotNull
    private final FloatValue phaseOffsetValue;
    @NotNull
    private MSTimer velocityTimer;
    private boolean velocityInput;
    @Nullable
    private BlockPos pos;
    private boolean reverseHurt;
    private boolean jump;

    public Velocity() {
        String[] stringArray = new String[]{"Cancel", "Spoof", "Simple", "AACv4", "AAC4Reduce", "AAC5Reduce", "AAC5.2.0", "AAC", "AACPush", "AACZero", "Reverse", "SmoothReverse", "Jump", "Glitch", "Phase", "Matrix", "Legit", "AEMine"};
        this.modeValue = new ListValue("Mode", stringArray, "Simple");
        this.aac5KillAuraValue = new BoolValue("AAC5.2.0-Attack-Only", true, new Function0<Boolean>(this){
            final /* synthetic */ Velocity this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Velocity.access$getModeValue$p(this.this$0).get(), "aac5.2.0", true);
            }
        });
        this.reduceChance = new FloatValue("Reduce-Chance", 100.0f, 0.0f, 100.0f, "%");
        this.shouldAffect = true;
        this.reverseStrengthValue = new FloatValue("ReverseStrength", 1.0f, 0.1f, 1.0f, "x", new Function0<Boolean>(this){
            final /* synthetic */ Velocity this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Velocity.access$getModeValue$p(this.this$0).get(), "reverse", true);
            }
        });
        this.reverse2StrengthValue = new FloatValue("SmoothReverseStrength", 0.05f, 0.02f, 0.1f, "x", new Function0<Boolean>(this){
            final /* synthetic */ Velocity this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Velocity.access$getModeValue$p(this.this$0).get(), "smoothreverse", true);
            }
        });
        this.aacPushXZReducerValue = new FloatValue("AACPushXZReducer", 2.0f, 1.0f, 3.0f, "x", new Function0<Boolean>(this){
            final /* synthetic */ Velocity this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Velocity.access$getModeValue$p(this.this$0).get(), "aacpush", true);
            }
        });
        this.aacPushYReducerValue = new BoolValue("AACPushYReducer", true, new Function0<Boolean>(this){
            final /* synthetic */ Velocity this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Velocity.access$getModeValue$p(this.this$0).get(), "aacpush", true);
            }
        });
        this.legitStrafeValue = new BoolValue("LegitStrafe", false, new Function0<Boolean>(this){
            final /* synthetic */ Velocity this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Velocity.access$getModeValue$p(this.this$0).get(), "legit", true);
            }
        });
        this.legitFaceValue = new BoolValue("LegitFace", true, new Function0<Boolean>(this){
            final /* synthetic */ Velocity this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Velocity.access$getModeValue$p(this.this$0).get(), "legit", true);
            }
        });
        this.aacStrafeValue = new BoolValue("AACStrafeValue", false, new Function0<Boolean>(this){
            final /* synthetic */ Velocity this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Velocity.access$getModeValue$p(this.this$0).get(), "aac", true);
            }
        });
        this.phaseOffsetValue = new FloatValue("Phase-Offset", 0.05f, -10.0f, 10.0f, "m", new Function0<Boolean>(this){
            final /* synthetic */ Velocity this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)Velocity.access$getModeValue$p(this.this$0).get(), "phase", true);
            }
        });
        this.velocityTimer = new MSTimer();
    }

    @Override
    public void onDisable() {
        if (MinecraftInstance.mc.field_71439_g != null) {
            MinecraftInstance.mc.field_71439_g.field_71102_ce = 0.02f;
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MinecraftInstance.mc.field_71439_g.field_70737_aN <= 0) {
            boolean bl = this.shouldAffect = (float)Math.random() < ((Number)this.reduceChance.get()).floatValue() / 100.0f;
        }
        if (MinecraftInstance.mc.field_71439_g.func_70090_H() || MinecraftInstance.mc.field_71439_g.func_180799_ab() || MinecraftInstance.mc.field_71439_g.field_70134_J || !this.shouldAffect) {
            return;
        }
        String string = ((String)this.modeValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        switch (string) {
            case "jump": {
                if (MinecraftInstance.mc.field_71439_g.field_70737_aN <= 0 || !MinecraftInstance.mc.field_71439_g.field_70122_E) break;
                MinecraftInstance.mc.field_71439_g.field_70181_x = 0.42;
                float yaw = MinecraftInstance.mc.field_71439_g.field_70177_z * ((float)Math.PI / 180);
                string = MinecraftInstance.mc.field_71439_g;
                ((EntityPlayerSP)string).field_70159_w -= (double)MathHelper.func_76126_a((float)yaw) * 0.2;
                string = MinecraftInstance.mc.field_71439_g;
                ((EntityPlayerSP)string).field_70179_y += (double)MathHelper.func_76134_b((float)yaw) * 0.2;
                break;
            }
            case "glitch": {
                MinecraftInstance.mc.field_71439_g.field_70145_X = this.velocityInput;
                if (MinecraftInstance.mc.field_71439_g.field_70737_aN == 7) {
                    MinecraftInstance.mc.field_71439_g.field_70181_x = 0.4;
                }
                this.velocityInput = false;
                break;
            }
            case "reverse": {
                if (!this.velocityInput) {
                    return;
                }
                if (!MinecraftInstance.mc.field_71439_g.field_70122_E) {
                    MovementUtils.strafe(MovementUtils.getSpeed() * ((Number)this.reverseStrengthValue.get()).floatValue());
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                break;
            }
            case "aacv4": {
                if (!MinecraftInstance.mc.field_71439_g.field_70122_E) {
                    if (!this.velocityInput) break;
                    MinecraftInstance.mc.field_71439_g.field_71102_ce = 0.02f;
                    EntityPlayerSP yaw = MinecraftInstance.mc.field_71439_g;
                    yaw.field_70159_w *= 0.6;
                    yaw = MinecraftInstance.mc.field_71439_g;
                    yaw.field_70179_y *= 0.6;
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                MinecraftInstance.mc.field_71439_g.field_71102_ce = 0.02f;
                break;
            }
            case "aac4reduce": {
                if (MinecraftInstance.mc.field_71439_g.field_70737_aN > 0 && !MinecraftInstance.mc.field_71439_g.field_70122_E && this.velocityInput && this.velocityTimer.hasTimePassed(80L)) {
                    EntityPlayerSP yaw = MinecraftInstance.mc.field_71439_g;
                    yaw.field_70159_w *= 0.62;
                    yaw = MinecraftInstance.mc.field_71439_g;
                    yaw.field_70179_y *= 0.62;
                }
                if (!this.velocityInput || MinecraftInstance.mc.field_71439_g.field_70737_aN >= 4 && !MinecraftInstance.mc.field_71439_g.field_70122_E || !this.velocityTimer.hasTimePassed(120L)) break;
                this.velocityInput = false;
                break;
            }
            case "aac5reduce": {
                if (MinecraftInstance.mc.field_71439_g.field_70737_aN > 1 && this.velocityInput) {
                    EntityPlayerSP yaw = MinecraftInstance.mc.field_71439_g;
                    yaw.field_70159_w *= 0.81;
                    yaw = MinecraftInstance.mc.field_71439_g;
                    yaw.field_70179_y *= 0.81;
                }
                if (!this.velocityInput || MinecraftInstance.mc.field_71439_g.field_70737_aN >= 5 && !MinecraftInstance.mc.field_71439_g.field_70122_E || !this.velocityTimer.hasTimePassed(120L)) break;
                this.velocityInput = false;
                break;
            }
            case "smoothreverse": {
                if (!this.velocityInput) {
                    MinecraftInstance.mc.field_71439_g.field_71102_ce = 0.02f;
                    return;
                }
                if (MinecraftInstance.mc.field_71439_g.field_70737_aN > 0) {
                    this.reverseHurt = true;
                }
                if (!MinecraftInstance.mc.field_71439_g.field_70122_E) {
                    if (!this.reverseHurt) break;
                    MinecraftInstance.mc.field_71439_g.field_71102_ce = ((Number)this.reverse2StrengthValue.get()).floatValue();
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                this.reverseHurt = false;
                break;
            }
            case "aac": {
                if (!this.velocityInput || !this.velocityTimer.hasTimePassed(50L)) break;
                EntityPlayerSP yaw = MinecraftInstance.mc.field_71439_g;
                yaw.field_70159_w *= ((Number)this.horizontalValue.get()).doubleValue();
                yaw = MinecraftInstance.mc.field_71439_g;
                yaw.field_70179_y *= ((Number)this.horizontalValue.get()).doubleValue();
                yaw = MinecraftInstance.mc.field_71439_g;
                yaw.field_70181_x *= ((Number)this.verticalValue.get()).doubleValue();
                if (((Boolean)this.aacStrafeValue.get()).booleanValue()) {
                    MovementUtils.strafe();
                }
                this.velocityInput = false;
                break;
            }
            case "aacpush": {
                if (this.jump) {
                    if (MinecraftInstance.mc.field_71439_g.field_70122_E) {
                        this.jump = false;
                    }
                } else {
                    if (MinecraftInstance.mc.field_71439_g.field_70737_aN > 0 && !(MinecraftInstance.mc.field_71439_g.field_70159_w == 0.0) && !(MinecraftInstance.mc.field_71439_g.field_70179_y == 0.0)) {
                        MinecraftInstance.mc.field_71439_g.field_70122_E = true;
                    }
                    if (MinecraftInstance.mc.field_71439_g.field_70172_ad > 0 && ((Boolean)this.aacPushYReducerValue.get()).booleanValue()) {
                        Module module2 = Client.INSTANCE.getModuleManager().get(Speed.class);
                        Intrinsics.checkNotNull(module2);
                        if (!module2.getState()) {
                            EntityPlayerSP yaw = MinecraftInstance.mc.field_71439_g;
                            yaw.field_70181_x -= 0.014999993;
                        }
                    }
                }
                if (MinecraftInstance.mc.field_71439_g.field_70172_ad < 19) break;
                float reduce = ((Number)this.aacPushXZReducerValue.get()).floatValue();
                string = MinecraftInstance.mc.field_71439_g;
                ((EntityPlayerSP)string).field_70159_w /= (double)reduce;
                string = MinecraftInstance.mc.field_71439_g;
                ((EntityPlayerSP)string).field_70179_y /= (double)reduce;
                break;
            }
            case "aaczero": {
                if (MinecraftInstance.mc.field_71439_g.field_70737_aN > 0) {
                    if (!this.velocityInput || MinecraftInstance.mc.field_71439_g.field_70122_E || MinecraftInstance.mc.field_71439_g.field_70143_R > 2.0f) {
                        return;
                    }
                    MinecraftInstance.mc.field_71439_g.func_70024_g(0.0, -1.0, 0.0);
                    MinecraftInstance.mc.field_71439_g.field_70122_E = true;
                    break;
                }
                this.velocityInput = false;
                break;
            }
            case "matrix": {
                if (MinecraftInstance.mc.field_71439_g.field_70737_aN <= 0) {
                    return;
                }
                if (MinecraftInstance.mc.field_71439_g.field_70122_E) {
                    EntityPlayerSP entityPlayerSP;
                    if (MinecraftInstance.mc.field_71439_g.field_70737_aN <= 6) {
                        entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                        entityPlayerSP.field_70159_w *= 0.700054132;
                        entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                        entityPlayerSP.field_70179_y *= 0.700054132;
                    }
                    if (MinecraftInstance.mc.field_71439_g.field_70737_aN > 5) break;
                    entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70159_w *= 0.803150645;
                    entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70179_y *= 0.803150645;
                    break;
                }
                if (MinecraftInstance.mc.field_71439_g.field_70737_aN > 10) break;
                EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                entityPlayerSP.field_70159_w *= 0.605001;
                entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                entityPlayerSP.field_70179_y *= 0.605001;
                break;
            }
            case "aemine": {
                if (MinecraftInstance.mc.field_71439_g.field_70737_aN <= 0) {
                    return;
                }
                if (MinecraftInstance.mc.field_71439_g.field_70737_aN >= 6) {
                    EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70159_w *= 0.605001;
                    entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70179_y *= 0.605001;
                    entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70181_x *= 0.727;
                    break;
                }
                if (MinecraftInstance.mc.field_71439_g.field_70122_E) break;
                EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                entityPlayerSP.field_70159_w *= 0.305001;
                entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                entityPlayerSP.field_70179_y *= 0.305001;
                entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                entityPlayerSP.field_70181_x -= 0.095;
            }
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Packet<?> packet;
        block49: {
            KillAura killAura;
            block51: {
                block50: {
                    Intrinsics.checkNotNullParameter(event, "event");
                    packet = event.getPacket();
                    Module module2 = Client.INSTANCE.getModuleManager().get(KillAura.class);
                    if (module2 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.combat.KillAura");
                    }
                    killAura = (KillAura)module2;
                    if (!(packet instanceof S12PacketEntityVelocity)) break block49;
                    if (MinecraftInstance.mc.field_71439_g == null) break block50;
                    WorldClient worldClient = MinecraftInstance.mc.field_71441_e;
                    Entity entity = worldClient == null ? null : worldClient.func_73045_a(((S12PacketEntityVelocity)packet).func_149412_c());
                    if (entity == null) {
                        return;
                    }
                    if (Intrinsics.areEqual(entity, MinecraftInstance.mc.field_71439_g) && this.shouldAffect) break block51;
                }
                return;
            }
            this.velocityTimer.reset();
            String string = ((String)this.modeValue.get()).toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
            switch (string) {
                case "cancel": {
                    event.cancelEvent();
                    break;
                }
                case "simple": {
                    float horizontal = ((Number)this.horizontalValue.get()).floatValue();
                    float vertical = ((Number)this.verticalValue.get()).floatValue();
                    ((S12PacketEntityVelocity)packet).field_149415_b = (int)((float)((S12PacketEntityVelocity)packet).func_149411_d() * horizontal);
                    ((S12PacketEntityVelocity)packet).field_149416_c = (int)((float)((S12PacketEntityVelocity)packet).func_149410_e() * vertical);
                    ((S12PacketEntityVelocity)packet).field_149414_d = (int)((float)((S12PacketEntityVelocity)packet).func_149409_f() * horizontal);
                    break;
                }
                case "spoof": {
                    float horizontal = ((Number)this.horizontalValue.get()).floatValue();
                    float vertical = ((Number)this.verticalValue.get()).floatValue();
                    ((S12PacketEntityVelocity)packet).field_149415_b = (int)((float)((S12PacketEntityVelocity)packet).func_149411_d() * horizontal);
                    ((S12PacketEntityVelocity)packet).field_149416_c = (int)((float)((S12PacketEntityVelocity)packet).func_149410_e() * vertical);
                    ((S12PacketEntityVelocity)packet).field_149414_d = (int)((float)((S12PacketEntityVelocity)packet).func_149409_f() * horizontal);
                    break;
                }
                case "aac4reduce": {
                    this.velocityInput = true;
                    ((S12PacketEntityVelocity)packet).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet).func_149411_d() * 0.6);
                    ((S12PacketEntityVelocity)packet).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet).func_149409_f() * 0.6);
                    break;
                }
                case "aac": 
                case "aacv4": 
                case "aaczero": 
                case "reverse": 
                case "aac5reduce": 
                case "smoothreverse": {
                    this.velocityInput = true;
                    break;
                }
                case "aac5.2.0": {
                    event.cancelEvent();
                    if (MinecraftInstance.mc.func_71387_A() || ((Boolean)this.aac5KillAuraValue.get()).booleanValue() && killAura.getTarget() == null) break;
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MinecraftInstance.mc.field_71439_g.field_70165_t, Double.MAX_VALUE, MinecraftInstance.mc.field_71439_g.field_70161_v, true));
                    break;
                }
                case "glitch": {
                    if (!MinecraftInstance.mc.field_71439_g.field_70122_E) {
                        return;
                    }
                    this.velocityInput = true;
                    event.cancelEvent();
                    break;
                }
                case "phase": {
                    MinecraftInstance.mc.field_71439_g.func_70634_a(MinecraftInstance.mc.field_71439_g.field_70165_t, MinecraftInstance.mc.field_71439_g.field_70163_u + (double)((Number)this.phaseOffsetValue.get()).floatValue(), MinecraftInstance.mc.field_71439_g.field_70161_v);
                    break;
                }
                case "legit": {
                    this.pos = new BlockPos(MinecraftInstance.mc.field_71439_g.field_70165_t, MinecraftInstance.mc.field_71439_g.field_70163_u, MinecraftInstance.mc.field_71439_g.field_70161_v);
                }
            }
        }
        if (packet instanceof S27PacketExplosion) {
            MinecraftInstance.mc.field_71439_g.field_70159_w += (double)(((S27PacketExplosion)packet).func_149149_c() * ((Number)this.horizontalExplosionValue.get()).floatValue());
            MinecraftInstance.mc.field_71439_g.field_70181_x += (double)(((S27PacketExplosion)packet).func_149144_d() * ((Number)this.verticalExplosionValue.get()).floatValue());
            MinecraftInstance.mc.field_71439_g.field_70179_y += (double)(((S27PacketExplosion)packet).func_149147_e() * ((Number)this.horizontalExplosionValue.get()).floatValue());
            event.cancelEvent();
        }
    }

    @EventTarget
    public final void onStrafe(@NotNull StrafeEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        String string = ((String)this.modeValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        if (Intrinsics.areEqual(string, "legit")) {
            if (this.pos == null || MinecraftInstance.mc.field_71439_g.field_70737_aN <= 0) {
                return;
            }
            BlockPos blockPos = this.pos;
            Intrinsics.checkNotNull(blockPos);
            double d = blockPos.func_177958_n();
            BlockPos blockPos2 = this.pos;
            Intrinsics.checkNotNull(blockPos2);
            double d2 = blockPos2.func_177956_o();
            BlockPos blockPos3 = this.pos;
            Intrinsics.checkNotNull(blockPos3);
            Rotation rot = RotationUtils.getRotations(d, d2, blockPos3.func_177952_p());
            if (((Boolean)this.legitFaceValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation(rot);
            }
            float yaw = rot.getYaw();
            if (((Boolean)this.legitStrafeValue.get()).booleanValue()) {
                float speed = MovementUtils.getSpeed();
                double yaw1 = Math.toRadians(yaw);
                MinecraftInstance.mc.field_71439_g.field_70159_w = -Math.sin(yaw1) * (double)speed;
                MinecraftInstance.mc.field_71439_g.field_70179_y = Math.cos(yaw1) * (double)speed;
            } else {
                float strafe = event.getStrafe();
                float forward = event.getForward();
                float friction = event.getFriction();
                float f = strafe * strafe + forward * forward;
                if (f >= 1.0E-4f) {
                    if ((f = MathHelper.func_76129_c((float)f)) < 1.0f) {
                        f = 1.0f;
                    }
                    f = friction / f;
                    float yawSin = MathHelper.func_76126_a((float)((float)((double)yaw * Math.PI / (double)180.0f)));
                    float yawCos = MathHelper.func_76134_b((float)((float)((double)yaw * Math.PI / (double)180.0f)));
                    EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70159_w += (double)((strafe *= f) * yawCos - (forward *= f) * yawSin);
                    entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70179_y += (double)(forward * yawCos + strafe * yawSin);
                }
            }
        }
    }

    @EventTarget
    public final void onJump(@NotNull JumpEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MinecraftInstance.mc.field_71439_g == null || MinecraftInstance.mc.field_71439_g.func_70090_H() || MinecraftInstance.mc.field_71439_g.func_180799_ab() || MinecraftInstance.mc.field_71439_g.field_70134_J || !this.shouldAffect) {
            return;
        }
        String string = ((String)this.modeValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        switch (string) {
            case "aacpush": {
                this.jump = true;
                if (MinecraftInstance.mc.field_71439_g.field_70124_G) break;
                event.cancelEvent();
                break;
            }
            case "aacv4": {
                if (MinecraftInstance.mc.field_71439_g.field_70737_aN <= 0) break;
                event.cancelEvent();
                break;
            }
            case "aaczero": {
                if (MinecraftInstance.mc.field_71439_g.field_70737_aN <= 0) break;
                event.cancelEvent();
            }
        }
    }

    @Override
    @Nullable
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public static final /* synthetic */ ListValue access$getModeValue$p(Velocity $this) {
        return $this.modeValue;
    }
}

