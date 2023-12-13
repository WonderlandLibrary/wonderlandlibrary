/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.ghost;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import markgg.events.Event;
import markgg.events.listeners.EventMotion;
import markgg.modules.Module;
import markgg.utilities.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class AimAssist
extends Module {
    public float oldSens;
    public TimerUtil timer = new TimerUtil();

    public AimAssist() {
        super("AimAssist", 0, Module.Category.GHOST);
    }

    @Override
    public void onEnable() {
        this.oldSens = this.mc.gameSettings.mouseSensitivity;
    }

    @Override
    public void onDisable() {
        this.mc.gameSettings.mouseSensitivity = this.oldSens;
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion && e.isPre()) {
            EventMotion event = (EventMotion)e;
            List targets = this.mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
            targets = targets.stream().filter(entity -> entity.getDistanceToEntity(this.mc.thePlayer) < 5.0f && entity != this.mc.thePlayer && !entity.isDead && !entity.isInvisible()).collect(Collectors.toList());
            targets.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(this.mc.thePlayer)));
            targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
            if (!targets.isEmpty()) {
                EntityLivingBase target = (EntityLivingBase)targets.get(0);
                this.mc.thePlayer.rotationYaw = this.getRotations(target)[0];
                this.mc.thePlayer.rotationPitch = this.getRotations(target)[1];
            }
        }
    }

    public float[] getRotations(Entity e) {
        double deltaX = e.posX + (e.posX - e.lastTickPosX) - this.mc.thePlayer.posX;
        double deltaY = e.posY - 3.5 + (double)e.getEyeHeight() - this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight();
        double deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - this.mc.thePlayer.posZ;
        double distance = Math.sqrt(Math.pow(deltaX, 2.0) + Math.pow(deltaZ, 2.0));
        float yaw = (float)Math.toDegrees(-Math.atan(deltaX / deltaZ));
        float pitch = (float)(-Math.toDegrees(Math.atan(deltaY / distance)));
        if (deltaX < 0.0 && deltaZ < 0.0) {
            yaw = (float)(90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        } else if (deltaX > 0.0 && deltaZ < 0.0) {
            yaw = (float)(-90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        }
        return new float[]{yaw, pitch};
    }
}

