/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.network.play.server.S12PacketEntityVelocity
 *  net.minecraft.util.MathHelper
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Velocity", category=ModuleCategory.COMBAT, description="Allows you to modify the amount of knockback you take.")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010#\u001a\u00020$H\u0016J\u0010\u0010%\u001a\u00020$2\u0006\u0010&\u001a\u00020'H\u0007J\u0010\u0010(\u001a\u00020$2\u0006\u0010&\u001a\u00020)H\u0007J\u0010\u0010*\u001a\u00020$2\u0006\u0010&\u001a\u00020+H\u0007J\u0010\u0010,\u001a\u00020$2\u0006\u0010&\u001a\u00020-H\u0007J\u0010\u0010.\u001a\u00020$2\u0006\u0010&\u001a\u00020/H\u0007J\u0010\u00100\u001a\u00020$2\u0006\u0010&\u001a\u00020/H\u0007J\u0010\u00101\u001a\u00020$2\u0006\u0010&\u001a\u00020+H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\u00020\u00178VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019R\u000e\u0010\u001a\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00062"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/Velocity;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aac5KillAuraValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "aacPushXZReducerValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "aacPushYReducerValue", "damaged", "", "horizontalValue", "isMatrixOnGround", "jump", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "redeCount", "", "reverse2StrengthValue", "reverseHurt", "reverseStrengthValue", "rspAlwaysValue", "rspDengerValue", "tag", "", "getTag", "()Ljava/lang/String;", "templateX", "templateY", "templateZ", "var0", "velocityCalcTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "velocityInput", "velocityTimer", "verticalValue", "onDisable", "", "onJump", "event", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onStrafe", "Lnet/ccbluex/liquidbounce/event/StrafeEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onVelocity", "onVelocityPacket", "KyinoClient"})
public final class Velocity
extends Module {
    private final FloatValue horizontalValue = new FloatValue("Horizontal", 0.0f, -1.0f, 1.0f);
    private final FloatValue verticalValue = new FloatValue("Vertical", 0.0f, -1.0f, 1.0f);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Simple", "AAC", "AACPush", "AACZero", "Reverse", "SmoothReverse", "Jump", "Glitch", "Vanilla", "AAC4", "AAC4.1.0", "AAC4.4.0", "AAC4.4.0semiblyat", "AAC4.4.2", "AAC5.2.0", "AAC5.2.0Semi", "AAC5Semi", "AAC5", "AAC5.2.0Lastest", "AAC5Reduce", "MatrixMeme", "MatrixSemi", "MatrixGround", "MatrixSpoof", "MatrixSimple", "MatrixReverse", "MatrixReduce", "Redesky1", "Redesky2", "Intave"}, "Simple");
    private final BoolValue rspAlwaysValue = new BoolValue("AAC5-AlwaysReduce", true);
    private final BoolValue rspDengerValue = new BoolValue("AAC5-OnlyDanger", false);
    private final FloatValue reverseStrengthValue = new FloatValue("Reverse-Strength", 1.0f, 0.0f, 1.0f);
    private final FloatValue reverse2StrengthValue = new FloatValue("SmoothReverse-Strength", 0.05f, 0.02f, 0.1f);
    private final FloatValue aacPushXZReducerValue = new FloatValue("AACPush-XZReducer", 2.0f, 1.0f, 3.0f);
    private final BoolValue aacPushYReducerValue = new BoolValue("AACPush-YReducer", true);
    private final BoolValue aac5KillAuraValue = new BoolValue("AAC5.2.0Semi-AttackOnly", true);
    private MSTimer velocityTimer = new MSTimer();
    private MSTimer velocityCalcTimer = new MSTimer();
    private boolean velocityInput;
    private boolean reverseHurt;
    private int redeCount = 24;
    private boolean jump;
    private int templateX;
    private int templateY;
    private int templateZ;
    private boolean damaged;
    private boolean isMatrixOnGround;
    private boolean var0;

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @Override
    public void onDisable() {
        block0: {
            if (Velocity.access$getMc$p$s1046033730().field_71439_g == null) break block0;
            Velocity.access$getMc$p$s1046033730().field_71439_g.field_71102_ce = 0.02f;
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block58: {
            block57: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                if (this.redeCount < 24) {
                    int n = this.redeCount;
                    this.redeCount = n + 1;
                }
                EntityPlayerSP entityPlayerSP = Velocity.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                if (entityPlayerSP.func_70090_H()) break block57;
                EntityPlayerSP entityPlayerSP2 = Velocity.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                if (!entityPlayerSP2.func_180799_ab() && !Velocity.access$getMc$p$s1046033730().field_71439_g.field_70134_J) break block58;
            }
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "jump": {
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 0 || !Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.42;
                float yaw = Velocity.access$getMc$p$s1046033730().field_71439_g.field_70177_z * ((float)Math.PI / 180);
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w -= (double)MathHelper.func_76126_a((float)yaw) * 0.2;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y += (double)MathHelper.func_76134_b((float)yaw) * 0.2;
                break;
            }
            case "aac4": {
                if (!Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN == 0 || !this.var0) break;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.6;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.6;
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.var0 = false;
                break;
            }
            case "matrixmeme": {
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 0) break;
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 6) {
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.7;
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.7;
                    }
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN > 5) break;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.8;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.8;
                    break;
                }
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN > 10) break;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.6;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.6;
                break;
            }
            case "glitch": {
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70145_X = this.velocityInput;
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN == 7) {
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.4;
                }
                this.velocityInput = false;
                break;
            }
            case "reverse": {
                if (!this.velocityInput) {
                    return;
                }
                if (!Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    MovementUtils.strafe(MovementUtils.getSpeed() * ((Number)this.reverseStrengthValue.get()).floatValue());
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                break;
            }
            case "smoothreverse": {
                if (!this.velocityInput) {
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_71102_ce = 0.02f;
                    return;
                }
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN > 0) {
                    this.reverseHurt = true;
                }
                if (!Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    if (!this.reverseHurt) break;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_71102_ce = ((Number)this.reverse2StrengthValue.get()).floatValue();
                    break;
                }
                if (!this.velocityTimer.hasTimePassed(80L)) break;
                this.velocityInput = false;
                this.reverseHurt = false;
                break;
            }
            case "aac": {
                if (!this.velocityInput || !this.velocityTimer.hasTimePassed(80L)) break;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= ((Number)this.horizontalValue.get()).doubleValue();
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= ((Number)this.horizontalValue.get()).doubleValue();
                this.velocityInput = false;
                break;
            }
            case "aacpush": {
                if (this.jump) {
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                        this.jump = false;
                    }
                } else {
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN > 0 && Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w != 0.0 && Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y != 0.0) {
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E = true;
                    }
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70172_ad > 0 && ((Boolean)this.aacPushYReducerValue.get()).booleanValue()) {
                        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Speed.class);
                        if (module == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!module.getState()) {
                            Velocity.access$getMc$p$s1046033730().field_71439_g.field_70181_x -= 0.014999993;
                        }
                    }
                }
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70172_ad < 19) break;
                float reduce = ((Number)this.aacPushXZReducerValue.get()).floatValue();
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w /= (double)reduce;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y /= (double)reduce;
                break;
            }
            case "aac5semi": {
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 0) {
                    return;
                }
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 6) {
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.600151164;
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.600151164;
                        break;
                    }
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN > 4) break;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.800151164;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.800151164;
                    break;
                }
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN > 9) break;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.8001421204;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.8001421204;
                break;
            }
            case "aac5reduce": {
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN > 1 && this.velocityInput) {
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.81;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.81;
                }
                if (!this.velocityInput || Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN >= 5 && !Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E || !this.velocityTimer.hasTimePassed(120L)) break;
                this.velocityInput = false;
                break;
            }
            case "aac5.2.0lastest": {
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN > 0 && this.velocityInput) {
                    this.velocityInput = false;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = -0.002f;
                    Minecraft minecraft = Velocity.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Velocity.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Double.MAX_VALUE, Velocity.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                }
                if (!this.velocityTimer.hasTimePassed(80L) || !this.velocityInput) break;
                this.velocityInput = false;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w = (double)this.templateX / 8000.0;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y = (double)this.templateZ / 8000.0;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70181_x = (double)this.templateY / 8000.0;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = -0.002f;
                break;
            }
            case "matrixreduce": {
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 0) break;
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 6) {
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.7;
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.7;
                    }
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN > 5) break;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.8;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.8;
                    break;
                }
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN > 10) break;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.6;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.6;
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onVelocity(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        var2_2 = (String)this.modeValue.get();
        var3_3 = false;
        v0 = var2_2;
        if (v0 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        v1 = v0.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(v1, "(this as java.lang.String).toLowerCase()");
        var2_2 = v1;
        tmp = -1;
        switch (var2_2.hashCode()) {
            case -2037326712: {
                if (!var2_2.equals("matrixground")) break;
                tmp = 1;
                break;
            }
            case 1808414887: {
                if (!var2_2.equals("MatrixReduce")) break;
                tmp = 2;
                break;
            }
        }
        switch (tmp) {
            case 1: {
                if (!Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) ** GOTO lbl-1000
                v2 = Velocity.access$getMc$p$s1046033730().field_71474_y.field_74314_A;
                Intrinsics.checkExpressionValueIsNotNull(v2, "mc.gameSettings.keyBindJump");
                if (!v2.func_151470_d()) {
                    v3 = true;
                } else lbl-1000:
                // 2 sources

                {
                    v3 = false;
                }
                this.isMatrixOnGround = v3;
                break;
            }
            case 2: {
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 0) break;
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 6) {
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.7;
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.7;
                    }
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN > 5) break;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.8;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.8;
                    break;
                }
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN > 10) break;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.6;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.6;
            }
        }
    }

    @EventTarget
    public final void onVelocityPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "matrixground": {
                Packet<?> packet = event.getPacket();
                if (!(packet instanceof S12PacketEntityVelocity)) break;
                ((S12PacketEntityVelocity)packet).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet).func_149411_d() * 0.36);
                ((S12PacketEntityVelocity)packet).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet).func_149409_f() * 0.36);
                if (!this.isMatrixOnGround) break;
                ((S12PacketEntityVelocity)packet).field_149416_c = (int)-628.7;
                ((S12PacketEntityVelocity)packet).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet).func_149411_d() * 0.6);
                ((S12PacketEntityVelocity)packet).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet).func_149409_f() * 0.6);
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E = false;
                break;
            }
            case "matrixspoof": {
                Packet<?> packet = event.getPacket();
                if (!(packet instanceof S12PacketEntityVelocity)) break;
                event.cancelEvent();
                Minecraft minecraft = Velocity.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Velocity.access$getMc$p$s1046033730().field_71439_g.field_70165_t + (double)((S12PacketEntityVelocity)packet).field_149415_b / -24000.0, Velocity.access$getMc$p$s1046033730().field_71439_g.field_70163_u + (double)((S12PacketEntityVelocity)packet).field_149416_c / -24000.0, Velocity.access$getMc$p$s1046033730().field_71439_g.field_70161_v + (double)((S12PacketEntityVelocity)packet).field_149414_d / 8000.0, false));
                break;
            }
            case "matrixsimple": {
                Packet<?> packet = event.getPacket();
                if (!(packet instanceof S12PacketEntityVelocity)) break;
                ((S12PacketEntityVelocity)packet).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet).func_149411_d() * 0.36);
                ((S12PacketEntityVelocity)packet).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet).func_149409_f() * 0.36);
                if (!Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                ((S12PacketEntityVelocity)packet).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet).func_149411_d() * 0.9);
                ((S12PacketEntityVelocity)packet).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet).func_149409_f() * 0.9);
                break;
            }
            case "matrixreverse": {
                Packet<?> packet = event.getPacket();
                if (!(packet instanceof S12PacketEntityVelocity)) break;
                ((S12PacketEntityVelocity)packet).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet).func_149411_d() * -0.3);
                ((S12PacketEntityVelocity)packet).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet).func_149409_f() * -0.3);
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        block77: {
            KillAura Aura;
            Packet<?> packet;
            block79: {
                block78: {
                    Intrinsics.checkParameterIsNotNull(event, "event");
                    boolean damaged = true;
                    packet = event.getPacket();
                    Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
                    if (module == null) {
                        throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
                    }
                    Aura = (KillAura)module;
                    if (!(packet instanceof S12PacketEntityVelocity)) break block77;
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g == null) break block78;
                    WorldClient worldClient = Velocity.access$getMc$p$s1046033730().field_71441_e;
                    if (worldClient == null || (worldClient = worldClient.func_73045_a(((S12PacketEntityVelocity)packet).func_149412_c())) == null) {
                        return;
                    }
                    if (!(Intrinsics.areEqual(worldClient, Velocity.access$getMc$p$s1046033730().field_71439_g) ^ true)) break block79;
                }
                return;
            }
            this.velocityTimer.reset();
            String string = (String)this.modeValue.get();
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "simple": {
                    float horizontal = ((Number)this.horizontalValue.get()).floatValue();
                    float vertical = ((Number)this.verticalValue.get()).floatValue();
                    if (horizontal == 0.0f && vertical == 0.0f) {
                        event.cancelEvent();
                    }
                    ((S12PacketEntityVelocity)packet).field_149415_b = (int)((float)((S12PacketEntityVelocity)packet).func_149411_d() * horizontal);
                    ((S12PacketEntityVelocity)packet).field_149416_c = (int)((float)((S12PacketEntityVelocity)packet).func_149410_e() * vertical);
                    ((S12PacketEntityVelocity)packet).field_149414_d = (int)((float)((S12PacketEntityVelocity)packet).func_149409_f() * horizontal);
                    break;
                }
                case "aac": 
                case "aaczero": 
                case "aac4.4.0": 
                case "reverse": 
                case "aac5reduce": 
                case "smoothreverse": {
                    this.velocityInput = true;
                    break;
                }
                case "glitch": {
                    if (!Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                        return;
                    }
                    this.velocityInput = true;
                    event.cancelEvent();
                    break;
                }
                case "aac5.2.0semi": {
                    event.cancelEvent();
                    if (Velocity.access$getMc$p$s1046033730().func_71387_A() || ((Boolean)this.aac5KillAuraValue.get()).booleanValue() && Aura.getTarget() == null) break;
                    Minecraft minecraft = Velocity.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Velocity.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Double.MAX_VALUE, Velocity.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                    break;
                }
                case "redesky2": {
                    void i;
                    if (((S12PacketEntityVelocity)packet).func_149411_d() == 0 && ((S12PacketEntityVelocity)packet).func_149409_f() == 0) {
                        return;
                    }
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                    ((S12PacketEntityVelocity)packet).field_149415_b = 0;
                    ((S12PacketEntityVelocity)packet).field_149414_d = 0;
                    int horizontal = 0;
                    int vertical = this.redeCount;
                    if (horizontal <= vertical) {
                        while (true) {
                            Minecraft minecraft = Velocity.access$getMc$p$s1046033730();
                            boolean bl2 = false;
                            boolean bl3 = false;
                            Minecraft $this$with = minecraft;
                            boolean bl4 = false;
                            $this$with.field_71439_g.field_71174_a.func_147297_a((Packet)new C02PacketUseEntity((Entity)Velocity.access$getMc$p$s1046033730().field_71439_g, C02PacketUseEntity.Action.ATTACK));
                            $this$with.field_71439_g.field_71174_a.func_147297_a((Packet)new C0APacketAnimation());
                            if (i == vertical) break;
                            ++i;
                        }
                    }
                    if (this.redeCount <= 12) break;
                    this.redeCount -= 5;
                    break;
                }
                case "redesky1": {
                    int count;
                    FallingPlayer.CollisionResult pos;
                    void i;
                    if (((S12PacketEntityVelocity)packet).func_149411_d() == 0 && ((S12PacketEntityVelocity)packet).func_149409_f() == 0) {
                        return;
                    }
                    if (((Boolean)this.rspDengerValue.get()).booleanValue() && (pos = new FallingPlayer(Velocity.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Velocity.access$getMc$p$s1046033730().field_71439_g.field_70163_u, Velocity.access$getMc$p$s1046033730().field_71439_g.field_70161_v, (double)((S12PacketEntityVelocity)packet).field_149415_b / 8000.0, (double)((S12PacketEntityVelocity)packet).field_149416_c / 8000.0, (double)((S12PacketEntityVelocity)packet).field_149414_d / 8000.0, 0.0f, 0.0f, 0.0f).findCollision(60)) != null && Velocity.access$getMc$p$s1046033730().field_71439_g.field_70163_u > Velocity.access$getMc$p$s1046033730().field_71439_g.field_70163_u - (double)7) {
                        return;
                    }
                    if (((Boolean)this.rspAlwaysValue.get()).booleanValue()) {
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70181_x = (double)((float)((S12PacketEntityVelocity)packet).field_149416_c / 8000.0f) * 1.0;
                        ((S12PacketEntityVelocity)packet).field_149415_b = 0;
                        ((S12PacketEntityVelocity)packet).field_149414_d = 0;
                        event.cancelEvent();
                    }
                    if (this.velocityCalcTimer.hasTimePassed(500L)) {
                        int n;
                        int vertical;
                        if (!((Boolean)this.rspAlwaysValue.get()).booleanValue()) {
                            Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                            Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                            Velocity.access$getMc$p$s1046033730().field_71439_g.field_70181_x = (double)((float)((S12PacketEntityVelocity)packet).field_149416_c / 8000.0f) * 1.0;
                            ((S12PacketEntityVelocity)packet).field_149415_b = 0;
                            ((S12PacketEntityVelocity)packet).field_149414_d = 0;
                        }
                        if ((vertical = 0) <= (n = (count = !this.velocityCalcTimer.hasTimePassed(800L) ? 8 : (!this.velocityCalcTimer.hasTimePassed(1200L) ? 12 : 25)))) {
                            while (true) {
                                void i2;
                                Velocity.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C02PacketUseEntity((Entity)Velocity.access$getMc$p$s1046033730().field_71439_g, C02PacketUseEntity.Action.ATTACK));
                                Velocity.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C0APacketAnimation());
                                if (i2 == n) break;
                                ++i2;
                            }
                        }
                        this.velocityCalcTimer.reset();
                        break;
                    }
                    ((S12PacketEntityVelocity)packet).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet).field_149415_b * 0.6);
                    ((S12PacketEntityVelocity)packet).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet).field_149414_d * 0.6);
                    count = 0;
                    int i2 = 4;
                    while (count <= i2) {
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C02PacketUseEntity((Entity)Velocity.access$getMc$p$s1046033730().field_71439_g, C02PacketUseEntity.Action.ATTACK));
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C0APacketAnimation());
                        ++i;
                    }
                    break;
                }
                case "matrixsemi": {
                    ((S12PacketEntityVelocity)packet).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet).func_149411_d() * 0.36);
                    ((S12PacketEntityVelocity)packet).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet).func_149409_f() * 0.36);
                    if (!Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                    ((S12PacketEntityVelocity)packet).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet).func_149411_d() * 0.9);
                    ((S12PacketEntityVelocity)packet).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet).func_149409_f() * 0.9);
                    break;
                }
                case "aac5": {
                    FallingPlayer.CollisionResult pos;
                    int count;
                    void i;
                    if (((S12PacketEntityVelocity)packet).func_149411_d() == 0 && ((S12PacketEntityVelocity)packet).func_149409_f() == 0) {
                        return;
                    }
                    if (((Boolean)this.rspDengerValue.get()).booleanValue() && (pos = new FallingPlayer(Velocity.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Velocity.access$getMc$p$s1046033730().field_71439_g.field_70163_u, Velocity.access$getMc$p$s1046033730().field_71439_g.field_70161_v, (double)((S12PacketEntityVelocity)packet).field_149415_b / 8000.0, (double)((S12PacketEntityVelocity)packet).field_149416_c / 8000.0, (double)((S12PacketEntityVelocity)packet).field_149414_d / 8000.0, 0.0f, 0.0f, 0.0f).findCollision(60)) != null && Velocity.access$getMc$p$s1046033730().field_71439_g.field_70163_u > Velocity.access$getMc$p$s1046033730().field_71439_g.field_70163_u - (double)7) {
                        return;
                    }
                    if (((Boolean)this.rspAlwaysValue.get()).booleanValue()) {
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70181_x = (double)((float)((S12PacketEntityVelocity)packet).field_149416_c / 8000.0f) * 1.0;
                        ((S12PacketEntityVelocity)packet).field_149415_b = 0;
                        ((S12PacketEntityVelocity)packet).field_149414_d = 0;
                        event.cancelEvent();
                    }
                    if (this.velocityCalcTimer.hasTimePassed(500L)) {
                        int n;
                        int i2;
                        if (!((Boolean)this.rspAlwaysValue.get()).booleanValue()) {
                            Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                            Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                            Velocity.access$getMc$p$s1046033730().field_71439_g.field_70181_x = (double)((float)((S12PacketEntityVelocity)packet).field_149416_c / 8000.0f) * 1.0;
                            ((S12PacketEntityVelocity)packet).field_149415_b = 0;
                            ((S12PacketEntityVelocity)packet).field_149414_d = 0;
                        }
                        if ((i2 = 0) <= (n = (count = !this.velocityCalcTimer.hasTimePassed(800L) ? 8 : (!this.velocityCalcTimer.hasTimePassed(1200L) ? 12 : 25)))) {
                            while (true) {
                                Velocity.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C02PacketUseEntity((Entity)Velocity.access$getMc$p$s1046033730().field_71439_g, C02PacketUseEntity.Action.ATTACK));
                                Velocity.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C0APacketAnimation());
                                if (i2 == n) break;
                                ++i2;
                            }
                        }
                        this.velocityCalcTimer.reset();
                        break;
                    }
                    ((S12PacketEntityVelocity)packet).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet).field_149415_b * 0.6);
                    ((S12PacketEntityVelocity)packet).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet).field_149414_d * 0.6);
                    count = 0;
                    int n = 4;
                    while (count <= n) {
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C02PacketUseEntity((Entity)Velocity.access$getMc$p$s1046033730().field_71439_g, C02PacketUseEntity.Action.ATTACK));
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C0APacketAnimation());
                        ++i;
                    }
                    break;
                }
                case "aac4.4.2": {
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN == 0) break;
                    EntityPlayerSP entityPlayerSP = Velocity.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                    if (entityPlayerSP.func_70027_ad()) break;
                    EntityPlayerSP entityPlayerSP2 = Velocity.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                    entityPlayerSP2.func_70031_b(false);
                    Velocity.access$getMc$p$s1046033730().field_71474_y.field_74351_w.field_74513_e = true;
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= (double)0.6f;
                        Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= (double)0.6f;
                        break;
                    }
                    if (this.velocityTimer.hasTimePassed(80L)) {
                        if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 3 == 0) {
                            Velocity.access$getMc$p$s1046033730().field_71439_g.field_70181_x -= 0.0169 * (double)Velocity.access$getMc$p$s1046033730().field_71439_g.field_70143_R;
                        }
                        boolean damaged = false;
                        this.velocityTimer.reset();
                    }
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= (double)0.6f;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= (double)0.6f;
                    break;
                }
                case "aac4.4.0semiblyat": {
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 0 || Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                    float reduce = 0.62f;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= (double)reduce;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= (double)reduce;
                    break;
                }
                case "aac4.4.0": {
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 0 || !(Velocity.access$getMc$p$s1046033730().field_71439_g.field_70163_u < 0.0)) break;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.6;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.6;
                    break;
                }
                case "aac4.1.0": {
                    if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 0) break;
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E = true;
                    this.velocityInput = false;
                    break;
                }
                case "vanilla": {
                    event.cancelEvent();
                    break;
                }
                case "aac5.2.0": {
                    event.cancelEvent();
                    Minecraft minecraft = Velocity.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Velocity.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Double.MAX_VALUE, Velocity.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                    break;
                }
                case "aac5.2.0lastest": {
                    event.cancelEvent();
                    this.velocityInput = true;
                    this.templateX = ((S12PacketEntityVelocity)packet).field_149415_b;
                    this.templateZ = ((S12PacketEntityVelocity)packet).field_149414_d;
                    this.templateY = ((S12PacketEntityVelocity)packet).field_149416_c;
                    break;
                }
                case "matrixground": {
                    Packet<?> packet2 = event.getPacket();
                    if (!(packet2 instanceof S12PacketEntityVelocity)) break;
                    ((S12PacketEntityVelocity)packet2).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet2).func_149411_d() * 0.36);
                    ((S12PacketEntityVelocity)packet2).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet2).func_149409_f() * 0.36);
                    if (!this.isMatrixOnGround) break;
                    ((S12PacketEntityVelocity)packet2).field_149416_c = (int)-628.7;
                    ((S12PacketEntityVelocity)packet2).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet2).func_149411_d() * 0.6);
                    ((S12PacketEntityVelocity)packet2).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet2).func_149409_f() * 0.6);
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E = false;
                    break;
                }
                case "matrixspoof": {
                    Packet<?> packet3 = event.getPacket();
                    if (!(packet3 instanceof S12PacketEntityVelocity)) break;
                    event.cancelEvent();
                    Minecraft minecraft = Velocity.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Velocity.access$getMc$p$s1046033730().field_71439_g.field_70165_t + (double)((S12PacketEntityVelocity)packet3).field_149415_b / -24000.0, Velocity.access$getMc$p$s1046033730().field_71439_g.field_70163_u + (double)((S12PacketEntityVelocity)packet3).field_149416_c / -24000.0, Velocity.access$getMc$p$s1046033730().field_71439_g.field_70161_v + (double)((S12PacketEntityVelocity)packet3).field_149414_d / 8000.0, false));
                    break;
                }
                case "matrixsimple": {
                    Packet<?> packet4 = event.getPacket();
                    if (!(packet4 instanceof S12PacketEntityVelocity)) break;
                    ((S12PacketEntityVelocity)packet4).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet4).func_149411_d() * 0.36);
                    ((S12PacketEntityVelocity)packet4).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet4).func_149409_f() * 0.36);
                    if (!Velocity.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                    ((S12PacketEntityVelocity)packet4).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet4).func_149411_d() * 0.9);
                    ((S12PacketEntityVelocity)packet4).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet4).func_149409_f() * 0.9);
                    break;
                }
                case "matrixreverse": {
                    Packet<?> packet5 = event.getPacket();
                    if (!(packet5 instanceof S12PacketEntityVelocity)) break;
                    ((S12PacketEntityVelocity)packet5).field_149415_b = (int)((double)((S12PacketEntityVelocity)packet5).func_149411_d() * -0.3);
                    ((S12PacketEntityVelocity)packet5).field_149414_d = (int)((double)((S12PacketEntityVelocity)packet5).func_149409_f() * -0.3);
                }
            }
        }
    }

    @EventTarget
    public final void onStrafe(@NotNull StrafeEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onJump(@NotNull JumpEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        EntityPlayerSP entityPlayerSP = Velocity.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (entityPlayerSP.func_70090_H()) return;
        EntityPlayerSP entityPlayerSP2 = Velocity.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
        if (entityPlayerSP2.func_180799_ab() || Velocity.access$getMc$p$s1046033730().field_71439_g.field_70134_J) {
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case -1234264725: {
                if (!string.equals("aaczero")) return;
                break;
            }
            case -1234547235: {
                if (!string.equals("aacpush")) return;
                this.jump = true;
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70124_G) return;
                event.cancelEvent();
                return;
            }
        }
        if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 0) return;
        event.cancelEvent();
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case -1183766399: {
                if (!string.equals("intave") || Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN <= 1 || Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN >= 10) break;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.75;
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.75;
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70737_aN >= 4) break;
                if (Velocity.access$getMc$p$s1046033730().field_71439_g.field_70181_x > 0.0) {
                    Velocity.access$getMc$p$s1046033730().field_71439_g.field_70181_x *= 0.9;
                    break;
                }
                Velocity.access$getMc$p$s1046033730().field_71439_g.field_70181_x *= 1.1;
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

