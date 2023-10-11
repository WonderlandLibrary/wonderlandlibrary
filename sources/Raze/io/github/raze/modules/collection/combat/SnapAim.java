package io.github.raze.modules.collection.combat;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SnapAim extends AbstractModule {

    private boolean doAim;
    private float speed = 6;
    private float ySpeed = 6;

    public SnapAim() {
        super("SnapAim", "Aims for the closest entity.", ModuleCategory.COMBAT);
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if(eventMotion.getState() == Event.State.PRE) {

            List<EntityLivingBase> targets = mc.theWorld.loadedEntityList.stream()
                    .filter(entity -> entity instanceof EntityLivingBase)
                    .map(entity -> (EntityLivingBase) entity)
                    .filter(entity -> entity.getDistanceToEntity(mc.thePlayer) <= 6
                            && entity != mc.thePlayer
                            && !entity.isDead
                            && entity.getHealth() > 0
                            && !entity.isInvisible()
                            && !entity.getName().contains(" "))
                    .sorted(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)))
                    .collect(Collectors.toList());

            if(Mouse.isButtonDown(0) && !targets.isEmpty()) {
                doAim = mc.pointedEntity == null;
            } else if(doAim && mc.pointedEntity != null) {
                speed *= 0.6;
                ySpeed *= 0.6;
                if(mc.thePlayer.ticksExisted % 6 == 0)
                    doAim = false;
            }

            if(doAim && !targets.isEmpty()) {
                aim(targets.get(0));
            } else {
                speed = 2;
                ySpeed = 2;
            }
        }
    }

    public void aim(EntityLivingBase e) {
        if (mc.currentScreen == null) {
                float[] rotations = getRotations(e);
                mc.thePlayer.rotationYaw = rotations[0];
                mc.thePlayer.rotationPitch = rotations[1];
        }
    }

    private float[] getRotations(Entity e) {
            if(ySpeed < 12) {
                ySpeed *= 1.1 + Math.random() / 20;
            }

            if(speed < 12) {
                speed *= 1.1 + Math.random() / 20;
            }
            double deltaX = e.posX - mc.thePlayer.posX;
            double deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
            double deltaZ = e.posZ - mc.thePlayer.posZ;

            float yaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0F;
            float pitch = (float) -Math.toDegrees(Math.atan2(deltaY, Math.sqrt(deltaX * deltaX + deltaZ * deltaZ)));

            float deltaYaw = MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
            float deltaPitch = MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);

            deltaYaw = Math.min(speed, Math.max(-speed, deltaYaw));
            deltaPitch = Math.min(ySpeed, Math.max(-ySpeed, deltaPitch));
            yaw = mc.thePlayer.rotationYaw + deltaYaw;
            pitch = (float) (mc.thePlayer.rotationPitch + deltaPitch + Math.random() * 1.2 - Math.random() * 1.2);

            return new float[]{yaw, pitch};
        }
    }
