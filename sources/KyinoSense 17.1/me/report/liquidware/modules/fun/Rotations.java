/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.report.liquidware.modules.fun;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.BowAimbot;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.features.module.modules.movement.Sprint;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestAura;
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Rotations", description="Ni kan ni ma ne?", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u001f\u001a\u00020\u00132\u000e\u0010 \u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00010!H\u0002J\u0010\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0007J\u0010\u0010&\u001a\u00020#2\u0006\u0010$\u001a\u00020'H\u0007J\u0006\u0010(\u001a\u00020\u0013R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR\u001a\u0010\u000f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\bR\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u001aR\u0016\u0010\u001b\u001a\u0004\u0018\u00010\u001c8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u001e\u00a8\u0006)"}, d2={"Lme/report/liquidware/modules/fun/Rotations;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "Alpha", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getAlpha", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "setAlpha", "(Lnet/ccbluex/liquidbounce/value/FloatValue;)V", "B", "getB", "setB", "G", "getG", "setG", "R", "getR", "setR", "bodyValue", "", "displayTag", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "playerYaw", "", "Ljava/lang/Float;", "tag", "", "getTag", "()Ljava/lang/String;", "getState", "module", "Ljava/lang/Class;", "onPacket", "", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "shouldRotate", "KyinoClient"})
public final class Rotations
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Chams", "LiquidBounce", "Other", "Ghost"}, "LiquidBounce");
    private final BoolValue displayTag = new BoolValue("ArrayListTag", true);
    @NotNull
    private FloatValue R = new FloatValue("R", 255.0f, 0.0f, 255.0f);
    @NotNull
    private FloatValue G = new FloatValue("G", 255.0f, 0.0f, 255.0f);
    @NotNull
    private FloatValue B = new FloatValue("B", 255.0f, 0.0f, 255.0f);
    @NotNull
    private FloatValue Alpha = new FloatValue("Alpha", 100.0f, 0.0f, 255.0f);
    private Float playerYaw;
    private boolean bodyValue = true;

    @NotNull
    public final FloatValue getR() {
        return this.R;
    }

    public final void setR(@NotNull FloatValue floatValue) {
        Intrinsics.checkParameterIsNotNull(floatValue, "<set-?>");
        this.R = floatValue;
    }

    @NotNull
    public final FloatValue getG() {
        return this.G;
    }

    public final void setG(@NotNull FloatValue floatValue) {
        Intrinsics.checkParameterIsNotNull(floatValue, "<set-?>");
        this.G = floatValue;
    }

    @NotNull
    public final FloatValue getB() {
        return this.B;
    }

    public final void setB(@NotNull FloatValue floatValue) {
        Intrinsics.checkParameterIsNotNull(floatValue, "<set-?>");
        this.B = floatValue;
    }

    @NotNull
    public final FloatValue getAlpha() {
        return this.Alpha;
    }

    public final void setAlpha(@NotNull FloatValue floatValue) {
        Intrinsics.checkParameterIsNotNull(floatValue, "<set-?>");
        this.Alpha = floatValue;
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        switch ((String)this.modeValue.get()) {
            case "Ghost": {
                if (RotationUtils.serverRotation == null || this.bodyValue) break;
                Rotations.access$getMc$p$s1046033730().field_71439_g.field_70759_as = RotationUtils.serverRotation.getYaw();
                break;
            }
            case "Other": {
                if (RotationUtils.serverRotation == null || this.bodyValue) break;
                Rotations.access$getMc$p$s1046033730().field_71439_g.field_70759_as = RotationUtils.serverRotation.getPitch();
                break;
            }
            case "LiquidBounce": {
                if (RotationUtils.serverRotation == null || this.bodyValue) break;
                Rotations.access$getMc$p$s1046033730().field_71439_g.field_70759_as = RotationUtils.serverRotation.getYaw();
            }
        }
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        String string = (String)this.modeValue.get();
        switch (string.hashCode()) {
            case -1367768316: {
                if (!string.equals("LiquidBounce")) break;
                if (!this.bodyValue || !this.shouldRotate() || Rotations.access$getMc$p$s1046033730().field_71439_g == null) {
                    return;
                }
                Packet<?> packet = event.getPacket();
                if (packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C03PacketPlayer.C05PacketPlayerLook) {
                    this.playerYaw = Float.valueOf(((C03PacketPlayer)packet).field_149476_e);
                    Rotations.access$getMc$p$s1046033730().field_71439_g.field_70761_aq = ((C03PacketPlayer)packet).func_149462_g();
                    Rotations.access$getMc$p$s1046033730().field_71439_g.field_70759_as = ((C03PacketPlayer)packet).func_149462_g();
                    break;
                }
                if (this.playerYaw != null) {
                    EntityPlayerSP entityPlayerSP = Rotations.access$getMc$p$s1046033730().field_71439_g;
                    Float f = this.playerYaw;
                    if (f == null) {
                        Intrinsics.throwNpe();
                    }
                    entityPlayerSP.field_70761_aq = f.floatValue();
                }
                Rotations.access$getMc$p$s1046033730().field_71439_g.field_70759_as = Rotations.access$getMc$p$s1046033730().field_71439_g.field_70761_aq;
            }
        }
    }

    private final boolean getState(Class<? extends Module> module) {
        Module module2 = LiquidBounce.INSTANCE.getModuleManager().get(module);
        if (module2 == null) {
            Intrinsics.throwNpe();
        }
        return module2.getState();
    }

    public final boolean shouldRotate() {
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        KillAura killAura = (KillAura)module;
        Module module2 = LiquidBounce.INSTANCE.getModuleManager().getModule(Sprint.class);
        if (module2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.movement.Sprint");
        }
        Sprint sprint = (Sprint)module2;
        return this.getState(Scaffold.class) || this.getState(Sprint.class) && (Boolean)sprint.allDirectionsValue.get() != false || this.getState(KillAura.class) && killAura.getTarget() != null || this.getState(BowAimbot.class) || this.getState(Fucker.class) || this.getState(ChestAura.class) || this.getState(Fly.class);
    }

    @Override
    @Nullable
    public String getTag() {
        return (Boolean)this.displayTag.get() != false ? (String)this.modeValue.get() : null;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

