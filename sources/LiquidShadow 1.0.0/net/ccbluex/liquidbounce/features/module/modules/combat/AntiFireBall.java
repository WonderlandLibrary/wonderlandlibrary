package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;

@ModuleInfo(name = "AntiFireBall",description = "Let the fireball cant hurt you.",category = ModuleCategory.COMBAT)
public class AntiFireBall extends Module {
    private final BoolValue swingValue = new BoolValue("Swing",false);
    private final BoolValue keepRotationValue = new BoolValue("KeepRotation",true);
    private final IntegerValue keepRotationTicksValue = new IntegerValue("KeepRotationTicks",5,1,20);

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityFireball && entity.getDistanceToEntity(mc.thePlayer) < 3) {
                VecRotation rotation = RotationUtils.searchCenter(
                        entity.getEntityBoundingBox(),
                        false,
                        true,
                        false,
                        false,
                        entity.getDistanceToEntity(mc.thePlayer));
                RotationUtils.setTargetRotation(rotation.getRotation(),keepRotationValue.get() ? keepRotationTicksValue.get() : 0);
                mc.playerController.attackEntity(mc.thePlayer,entity);
                if (swingValue.get()) {
                    mc.thePlayer.swingItem();
                } else {
                    mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                }
            }
        }
    }
}
