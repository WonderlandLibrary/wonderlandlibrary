/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

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
import net.ccbluex.liquidbounce.features.module.modules.exploit.Derp;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestAura;
import net.ccbluex.liquidbounce.features.module.modules.world.CivBreak;
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker;
import net.ccbluex.liquidbounce.features.module.modules.world.Nuker;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.features.module.modules.world.Tower;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="Rotations", description="Allows you to see server-sided head and body rotations.", category=ModuleCategory.RENDER)
public final class Rotations
extends Module {
    private final BoolValue bodyValue = new BoolValue("Body", true);
    private Float playerYaw;

    @EventTarget
    public final void onRender3D(Render3DEvent event) {
        block1: {
            if (RotationUtils.serverRotation == null || ((Boolean)this.bodyValue.get()).booleanValue()) break block1;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP != null) {
                iEntityPlayerSP.setRotationYawHead(RotationUtils.serverRotation.getYaw());
            }
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (!((Boolean)this.bodyValue.get()).booleanValue() || !this.shouldRotate() || thePlayer == null) {
            return;
        }
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayerPosLook(packet) || MinecraftInstance.classProvider.isCPacketPlayerLook(packet)) {
            ICPacketPlayer packetPlayer = packet.asCPacketPlayer();
            this.playerYaw = Float.valueOf(packetPlayer.getYaw());
            thePlayer.setRenderYawOffset(packetPlayer.getYaw());
            thePlayer.setRotationYawHead(packetPlayer.getYaw());
        } else {
            if (this.playerYaw != null) {
                Float f = this.playerYaw;
                if (f == null) {
                    Intrinsics.throwNpe();
                }
                thePlayer.setRenderYawOffset(f.floatValue());
            }
            thePlayer.setRotationYawHead(thePlayer.getRenderYawOffset());
        }
    }

    private final boolean getState(Class<?> module) {
        Module module2 = LiquidBounce.INSTANCE.getModuleManager().get(module);
        if (module2 == null) {
            Intrinsics.throwNpe();
        }
        return module2.getState();
    }

    private final boolean shouldRotate() {
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        KillAura killAura = (KillAura)module;
        return this.getState(Scaffold.class) || this.getState(Tower.class) || this.getState(KillAura.class) && killAura.getTarget() != null || this.getState(Derp.class) || this.getState(BowAimbot.class) || this.getState(Fucker.class) || this.getState(CivBreak.class) || this.getState(Nuker.class) || this.getState(ChestAura.class);
    }
}

