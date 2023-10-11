package io.github.raze.modules.collection.combat;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.settings.collection.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AimAssist extends AbstractModule {

    private final NumberSetting aimSpeedX,aimSpeedY, cameraShake, blockDistance;
    private final BooleanSetting clickAim, mobAim, aimWithSword, instantLock;

    public AimAssist() {
        super("AimAssist", "Aims for the closest entity.", ModuleCategory.COMBAT);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                cameraShake = new NumberSetting(this, "Camera shake amount", 0, 5, 0.2),
                blockDistance = new NumberSetting(this, "Distance", 1, 10, 5),

                clickAim = new BooleanSetting(this, "Click to aim", false),
                mobAim = new BooleanSetting(this, "Aim at monsters", true),
                aimWithSword = new BooleanSetting(this, "Only aim with swords", false),
                instantLock = new BooleanSetting(this, "Instant aim", false),

                aimSpeedX = new NumberSetting(this, "X Aim Speed", 0.1, 7, 0.5)
                        .setHidden(instantLock::get),
                aimSpeedY = new NumberSetting(this, "Y Aim Speed", 0.1, 7, 0.5)
                        .setHidden(instantLock::get)
        );
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if(eventMotion.getState() == Event.State.PRE) {

            List<EntityLivingBase> targets = mc.theWorld.loadedEntityList.stream()
                    .filter(entity -> entity instanceof EntityLivingBase)
                    .map(entity -> (EntityLivingBase) entity)
                    .filter(entity -> entity.getDistanceToEntity(mc.thePlayer) <= blockDistance.get().floatValue()
                            && entity != mc.thePlayer
                            && !entity.isDead
                            && entity.getHealth() > 0
                            && !entity.isInvisible()
                            && !entity.getName().contains(" "))
                    .sorted(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)))
                    .collect(Collectors.toList());

            if (!mobAim.get())
                targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());

            if (!targets.isEmpty()) {
                EntityLivingBase target = targets.get(0);
                aim(target);
            }
        }
    }

    public void aim(EntityLivingBase e) {
        if (mc.currentScreen == null) {
            ItemStack heldItem = mc.thePlayer.getHeldItem();

            if (heldItem != null && (heldItem.getItem() instanceof ItemSword && aimWithSword.get())) {
                setRotations(e);
            } else if (!aimWithSword.get()) {
                setRotations(e);
            }
        }
    }

    public void setRotations(EntityLivingBase e) {
        float[] rotations = getRotations(e);

        if (clickAim.get()) {
            if (Mouse.isButtonDown(0)) {
                mc.thePlayer.rotationYaw = rotations[0];
                mc.thePlayer.rotationPitch = rotations[1];
            }
        } else {
            mc.thePlayer.rotationYaw = rotations[0];
            mc.thePlayer.rotationPitch = rotations[1];
        }
    }

    private float[] getRotations(Entity e) {
        float rotationSpeedX = instantLock.get() ? 10000 : aimSpeedX.get().floatValue();
        float rotationSpeedY = instantLock.get() ? 10000 : aimSpeedY.get().floatValue();
        float cameraShakeSpeed = (float) (Math.random() * cameraShake.get().floatValue());

        double deltaX = e.posX - mc.thePlayer.posX;
        double deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
        double deltaZ = e.posZ - mc.thePlayer.posZ;

        float yaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0F;
        float pitch = (float) -Math.toDegrees(Math.atan2(deltaY, Math.sqrt(deltaX * deltaX + deltaZ * deltaZ)));

        float deltaYaw = MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
        float deltaPitch = MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);

        deltaYaw = Math.min(rotationSpeedX, Math.max(-rotationSpeedX, deltaYaw));
        deltaPitch = Math.min(rotationSpeedY, Math.max(-rotationSpeedY, deltaPitch));
        yaw = mc.thePlayer.rotationYaw + deltaYaw + cameraShakeSpeed;
        pitch = mc.thePlayer.rotationPitch + deltaPitch + cameraShakeSpeed;

        return new float[]{yaw, pitch};
    }
}
