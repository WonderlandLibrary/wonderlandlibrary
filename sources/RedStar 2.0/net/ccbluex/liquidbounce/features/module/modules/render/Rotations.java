package net.ccbluex.liquidbounce.features.module.modules.render;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.BowAimbot;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura2;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura3;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestAura;
import net.ccbluex.liquidbounce.features.module.modules.world.CivBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker;
import net.ccbluex.liquidbounce.features.module.modules.world.Nuker;
import net.ccbluex.liquidbounce.features.module.modules.world.OldScaffold;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.features.module.modules.world.Tower;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Rotations", description="Allows you to see server-sided head rotations.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000<\n\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\b\u000020B¢J\f0\r2\n\b0HJ020HJ020HJ\b0\rHR0¢\b\n\u0000\bR0¢\b\n\u0000\b\bR\t0\nX¢\n¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/Rotations;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "bodyValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getBodyValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "headValue", "getHeadValue", "playerYaw", "", "Ljava/lang/Float;", "getState", "", "module", "Ljava/lang/Class;", "onPacket", "", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "shouldRotate", "Pride"})
public final class Rotations
extends Module {
    @NotNull
    private final BoolValue headValue = new BoolValue("Head", true);
    @NotNull
    private final BoolValue bodyValue = new BoolValue("Body", true);
    private Float playerYaw;

    @NotNull
    public final BoolValue getHeadValue() {
        return this.headValue;
    }

    @NotNull
    public final BoolValue getBodyValue() {
        return this.bodyValue;
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        block1: {
            Intrinsics.checkParameterIsNotNull(event, "event");
            if (RotationUtils.serverRotation == null || ((Boolean)this.bodyValue.get()).booleanValue() || !((Boolean)this.headValue.get()).booleanValue()) break block1;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP != null) {
                iEntityPlayerSP.setRotationYawHead(RotationUtils.serverRotation.getYaw());
            }
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (!((Boolean)this.bodyValue.get()).booleanValue() || !this.shouldRotate() || MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayerPosLook(packet) || MinecraftInstance.classProvider.isCPacketPlayerLook(packet)) {
            ICPacketPlayer packetPlayer = packet.asCPacketPlayer();
            this.playerYaw = Float.valueOf(packetPlayer.getYaw());
            if (((Boolean)this.bodyValue.get()).booleanValue()) {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP.setRenderYawOffset(packetPlayer.getYaw());
            }
            if (((Boolean)this.headValue.get()).booleanValue()) {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP.setRotationYawHead(packetPlayer.getYaw());
            }
        } else {
            if (this.playerYaw != null && ((Boolean)this.bodyValue.get()).booleanValue()) {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                Float f = this.playerYaw;
                if (f == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP.setRenderYawOffset(f.floatValue());
            }
            if (((Boolean)this.headValue.get()).booleanValue()) {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP.setRotationYawHead(iEntityPlayerSP2.getRenderYawOffset());
            }
        }
    }

    private final boolean getState(Class<?> module) {
        return LiquidBounce.INSTANCE.getModuleManager().get(module).getState();
    }

    private final boolean shouldRotate() {
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        KillAura killAura = (KillAura)module;
        Module module2 = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura2.class);
        if (module2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura2");
        }
        KillAura2 killAura2 = (KillAura2)module2;
        Module module3 = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura3.class);
        if (module3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura3");
        }
        KillAura3 KillAura32 = (KillAura3)module3;
        return this.getState(Scaffold.class) || this.getState(Tower.class) || this.getState(OldScaffold.class) || this.getState(KillAura32.getClass()) || this.getState(KillAura.class) && killAura.getTarget() != null || this.getState(BowAimbot.class) || this.getState(KillAura2.class) && killAura2.getTarget() != null || this.getState(Fucker.class) || this.getState(CivBreak.class) || this.getState(Nuker.class) || this.getState(ChestAura.class);
    }
}
