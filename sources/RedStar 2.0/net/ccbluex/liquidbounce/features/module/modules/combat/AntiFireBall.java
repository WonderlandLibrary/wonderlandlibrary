package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
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

@ModuleInfo(name="AntiFireBall", category=ModuleCategory.COMBAT, description="Fuck")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J\t0\n20\fHR0X¢\n\u0000R0X¢\n\u0000R0\bX¢\n\u0000¨\r"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/AntiFireBall;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "rotationValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "swingValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "timer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class AntiFireBall
extends Module {
    private final ListValue swingValue = new ListValue("Swing", new String[]{"Normal", "Packet", "None"}, "Normal");
    private final BoolValue rotationValue = new BoolValue("Rotation", true);
    private final MSTimer timer = new MSTimer();

    @EventTarget
    private final void onUpdate(UpdateEvent event) {
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        for (IEntity entity : iWorldClient.getLoadedEntityList()) {
            if (!MinecraftInstance.classProvider.isEntityFireball(event)) continue;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!((double)iEntityPlayerSP.getDistanceToEntity(entity) < 5.5) || !this.timer.hasTimePassed(300L)) continue;
            if (((Boolean)this.rotationValue.get()).booleanValue()) {
                RotationUtils.setTargetRotation(RotationUtils.getRotationsNonLivingEntity(entity));
            }
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP2.getSendQueue().addToSendQueue(MinecraftInstance.classProvider.createCPacketUseEntity(entity, ICPacketUseEntity.WAction.ATTACK));
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
