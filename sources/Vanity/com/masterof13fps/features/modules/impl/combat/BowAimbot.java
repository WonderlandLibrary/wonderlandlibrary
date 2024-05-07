package com.masterof13fps.features.modules.impl.combat;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventMotion;
import com.masterof13fps.features.modules.Category;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;

import java.util.Iterator;

@ModuleInfo(name = "BowAimbot", category = Category.COMBAT, description = "You automatically aim at other targets with your bow")
public class BowAimbot extends Module {

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMotion) {
            Entity targetEntity = getCursorEntity();
            if ((mc.thePlayer.getCurrentEquippedItem() != null) &&
                    ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) &&
                    (targetEntity != null)) {
                int bowCurrentCharge = mc.thePlayer.getItemInUseDuration();
                float bowVelocity = bowCurrentCharge / 20.0F;
                bowVelocity = (bowVelocity * bowVelocity + bowVelocity * 2.0F) / 3.0F;
                if (bowVelocity < 0.1D) {
                    return;
                }
                if (bowVelocity > 1.0F) {
                    bowVelocity = 1.0F;
                }
                double xDistance = targetEntity.posX - mc.thePlayer.posX;
                double zDistance = targetEntity.posZ - mc.thePlayer.posZ;
                double eyeDistance = targetEntity.posY + targetEntity.getEyeHeight() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
                double trajectoryXZ = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
                float trajectoryTheta90 = (float) (Math.atan2(zDistance, xDistance) * 180.0D / 3.141592653589793D) - 90.0F;
                float bowTrajectory = -getTrajectoryAngleSolutionLow((float) trajectoryXZ, (float) eyeDistance, bowVelocity);
                ((EventMotion) event).setYaw(trajectoryTheta90);
                ((EventMotion) event).setPitch(bowTrajectory);
            }
        }
    }

    public Entity getCursorEntity() {
        Entity poorEntity = null;
        double distanceToEntity = 1000.0D;
        for (Iterator entityIterator = mc.theWorld.loadedEntityList.iterator(); entityIterator.hasNext(); ) {
            Object currentObject = ((Iterator<?>) entityIterator).next();
            if ((currentObject instanceof Entity)) {
                Entity targetEntity = (Entity) currentObject;
                if (((targetEntity instanceof EntityPlayer)) && (targetEntity != mc.thePlayer) &&
                        (targetEntity.getDistanceToEntity(mc.thePlayer) <= 140.0F) && (mc.thePlayer.canEntityBeSeen(targetEntity)) && (((EntityLivingBase) targetEntity).deathTime <= 0)) {
                    if (poorEntity == null) {
                        poorEntity = targetEntity;
                    }
                    double xDistance = targetEntity.posX - mc.thePlayer.posX;
                    double zDistance = targetEntity.posZ - mc.thePlayer.posZ;
                    double eyeDistance = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - targetEntity.posY;
                    double trajectoryXZ = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
                    float trajectoryTheta90 = (float) (Math.atan2(zDistance, xDistance) * 180.0D / 3.141592653589793D) - 90.0F;
                    float trajectoryTheta = (float) (Math.atan2(eyeDistance, trajectoryXZ) * 180.0D / 3.141592653589793D);

                    double xAngleDistance = getDistanceBetweenAngles(trajectoryTheta90, mc.thePlayer.rotationYaw % 360.0F);
                    double yAngleDistance = getDistanceBetweenAngles(trajectoryTheta, mc.thePlayer.rotationPitch % 360.0F);

                    double entityDistance = Math.sqrt(xAngleDistance * xAngleDistance + yAngleDistance * yAngleDistance);
                    if (entityDistance < distanceToEntity) {
                        poorEntity = targetEntity;
                        distanceToEntity = entityDistance;
                    }
                }
            }
        }
        return poorEntity;
    }

    private float getTrajectoryAngleSolutionLow(float angleX, float angleY, float bowVelocity) {
        float velocityIncrement = 0.006F;
        float squareRootBow = bowVelocity * bowVelocity * bowVelocity * bowVelocity - velocityIncrement * (velocityIncrement * (angleX * angleX) + 2.0F * angleY * (bowVelocity * bowVelocity));
        return (float) Math.toDegrees(Math.atan((bowVelocity * bowVelocity - Math.sqrt(squareRootBow)) / (velocityIncrement * angleX)));
    }

    private float getDistanceBetweenAngles(float angle1, float angle2) {
        float angleToEntity = Math.abs(angle1 - angle2) % 360.0F;
        if (angleToEntity > 180.0F) {
            angleToEntity = 360.0F - angleToEntity;
        }
        return angleToEntity;
    }
}
