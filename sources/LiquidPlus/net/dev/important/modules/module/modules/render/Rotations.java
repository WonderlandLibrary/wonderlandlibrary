/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.Render3DEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.combat.BowAimbot;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.modules.module.modules.exploit.Disabler;
import net.dev.important.modules.module.modules.movement.Fly;
import net.dev.important.modules.module.modules.world.ChestAura;
import net.dev.important.modules.module.modules.world.Fucker;
import net.dev.important.modules.module.modules.world.Scaffold;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.RotationUtils;
import net.dev.important.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Info(name="Rotations", description="Allows you to see server-sided head and body rotations.", category=Category.RENDER, cnName="\u8f6c\u5934")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\n\u001a\u00020\u000b2\n\u0010\f\u001a\u0006\u0012\u0002\b\u00030\rH\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0013H\u0007J\b\u0010\u0014\u001a\u00020\u000bH\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0012\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\t\u00a8\u0006\u0015"}, d2={"Lnet/dev/important/modules/module/modules/render/Rotations;", "Lnet/dev/important/modules/module/Module;", "()V", "modeValue", "Lnet/dev/important/value/ListValue;", "getModeValue", "()Lnet/dev/important/value/ListValue;", "playerYaw", "", "Ljava/lang/Float;", "getState", "", "module", "Ljava/lang/Class;", "onPacket", "", "event", "Lnet/dev/important/event/PacketEvent;", "onRender3D", "Lnet/dev/important/event/Render3DEvent;", "shouldRotate", "LiquidBounce"})
public final class Rotations
extends Module {
    @NotNull
    private final ListValue modeValue;
    @Nullable
    private Float playerYaw;

    public Rotations() {
        String[] stringArray = new String[]{"Head", "Body"};
        this.modeValue = new ListValue("Mode", stringArray, "Body");
    }

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (StringsKt.equals((String)this.modeValue.get(), "head", true) && RotationUtils.serverRotation != null) {
            MinecraftInstance.mc.field_71439_g.field_70759_as = RotationUtils.serverRotation.getYaw();
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (StringsKt.equals((String)this.modeValue.get(), "head", true) || !this.shouldRotate() || MinecraftInstance.mc.field_71439_g == null) {
            return;
        }
        Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C03PacketPlayer.C05PacketPlayerLook) {
            this.playerYaw = Float.valueOf(((C03PacketPlayer)packet).field_149476_e);
            MinecraftInstance.mc.field_71439_g.field_70761_aq = ((C03PacketPlayer)packet).func_149462_g();
            MinecraftInstance.mc.field_71439_g.field_70759_as = ((C03PacketPlayer)packet).func_149462_g();
        } else {
            if (this.playerYaw != null) {
                EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                Float f = this.playerYaw;
                Intrinsics.checkNotNull(f);
                entityPlayerSP.field_70761_aq = f.floatValue();
            }
            MinecraftInstance.mc.field_71439_g.field_70759_as = MinecraftInstance.mc.field_71439_g.field_70761_aq;
        }
    }

    private final boolean getState(Class<?> module2) {
        Module module3 = Client.INSTANCE.getModuleManager().get(module2);
        Intrinsics.checkNotNull(module3);
        return module3.getState();
    }

    private final boolean shouldRotate() {
        Module module2 = Client.INSTANCE.getModuleManager().getModule(KillAura.class);
        if (module2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.combat.KillAura");
        }
        KillAura killAura = (KillAura)module2;
        Module module3 = Client.INSTANCE.getModuleManager().getModule(Disabler.class);
        if (module3 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.exploit.Disabler");
        }
        Disabler disabler = (Disabler)module3;
        return this.getState(Scaffold.class) || this.getState(KillAura.class) && killAura.getTarget() != null || this.getState(Disabler.class) && disabler.getCanRenderInto3D() || this.getState(BowAimbot.class) || this.getState(Fucker.class) || this.getState(ChestAura.class) || this.getState(Fly.class);
    }
}

