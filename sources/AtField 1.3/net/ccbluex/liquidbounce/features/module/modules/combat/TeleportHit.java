/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import javax.vecmath.Vector3d;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.PathUtils;
import net.ccbluex.liquidbounce.utils.RaycastUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;

@ModuleInfo(name="TeleportHit", description="Allows to hit entities from far away.", category=ModuleCategory.COMBAT)
public class TeleportHit
extends Module {
    private boolean shouldHit;
    private IEntityLivingBase targetEntity;

    private static void lambda$onMotion$0(Vector3d vector3d) {
        mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerPosition(vector3d.getX(), vector3d.getY(), vector3d.getZ(), false));
    }

    @EventTarget
    public void onMotion(MotionEvent motionEvent) {
        if (motionEvent.getEventState() != EventState.PRE) {
            return;
        }
        IEntity iEntity = RaycastUtils.raycastEntity(100.0, classProvider::isEntityLivingBase);
        IEntityPlayerSP iEntityPlayerSP = mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        if (mc.getGameSettings().getKeyBindAttack().isKeyDown() && EntityUtils.isSelected(iEntity, true) && iEntity.getDistanceSqToEntity(iEntityPlayerSP) >= 1.0) {
            this.targetEntity = iEntity.asEntityLivingBase();
        }
        if (this.targetEntity != null) {
            if (!this.shouldHit) {
                this.shouldHit = true;
                return;
            }
            if (iEntityPlayerSP.getFallDistance() > 0.0f) {
                WVec3 wVec3 = RotationUtils.getVectorForRotation(new Rotation(iEntityPlayerSP.getRotationYaw(), 0.0f));
                double d = iEntityPlayerSP.getPosX() + wVec3.getXCoord() * (double)(iEntityPlayerSP.getDistanceToEntity(this.targetEntity) - 1.0f);
                double d2 = iEntityPlayerSP.getPosZ() + wVec3.getZCoord() * (double)(iEntityPlayerSP.getDistanceToEntity(this.targetEntity) - 1.0f);
                double d3 = (double)this.targetEntity.getPosition().getY() + 0.25;
                PathUtils.findPath(d, d3 + 1.0, d2, 4.0).forEach(TeleportHit::lambda$onMotion$0);
                iEntityPlayerSP.swingItem();
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketUseEntity((IEntity)this.targetEntity, ICPacketUseEntity.WAction.ATTACK));
                iEntityPlayerSP.onCriticalHit(this.targetEntity);
                this.shouldHit = false;
                this.targetEntity = null;
            } else if (iEntityPlayerSP.getOnGround()) {
                iEntityPlayerSP.jump();
            }
        } else {
            this.shouldHit = false;
        }
    }
}

