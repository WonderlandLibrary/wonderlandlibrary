/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="AntiFireBall", category=ModuleCategory.COMBAT, description="\u81ea\u52a8\u5c06\u706b\u7130\u5f39\u6253\u98de")
public final class AntiFireBall
extends Module {
    private final ListValue swingValue = new ListValue("Swing", new String[]{"Normal", "Packet", "None"}, "Normal");
    private final BoolValue rotationValue = new BoolValue("Rotation", true);
    private final MSTimer timer = new MSTimer();

    @EventTarget
    private final void onUpdate(UpdateEvent updateEvent) {
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        for (IEntity iEntity : iWorldClient.getLoadedEntityList()) {
            if (!MinecraftInstance.classProvider.isEntityFireball(updateEvent)) continue;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!((double)iEntityPlayerSP.getDistanceToEntity(iEntity) < 5.5) || !this.timer.hasTimePassed(300L)) continue;
            if (((Boolean)this.rotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation(RotationUtils.getRotationsNonLivingEntity(iEntity));
            }
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP2.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(iEntity, ICPacketUseEntity.WAction.ATTACK));
            if (this.swingValue.equals("Normal")) {
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP3.swingItem();
            } else if (this.swingValue.equals("Packet")) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketAnimation());
            }
            this.timer.reset();
            break;
        }
    }
}

