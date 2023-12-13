/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.combat;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.combat.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class AimAssist
extends Module {
    Setting range = new Setting("Range", this, 4.0, 3.0, 7.0, false);
    public float[] facing;

    public AimAssist() {
        super("AimAssist", Category.Combat);
        Main.instance.setmgr.rSetting(this.range);
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        for (Object theObject : AimAssist.mc.world.loadedEntityList) {
            EntityLivingBase entity;
            if (!(theObject instanceof EntityLivingBase) || (entity = (EntityLivingBase)theObject) instanceof EntityPlayerSP) continue;
            this.facing = AimAssist.faceTarget(entity, 360.0f, 360.0f, false);
            if (!((double)Minecraft.player.getDistanceToEntity(entity) < this.range.getValDouble())) continue;
            float f = this.facing[0];
            float f2 = this.facing[1];
            event.setYaw(f);
            Minecraft.player.rotationYaw = f;
            Minecraft.player.rotationPitch = f2 - -15.0f;
            if (Minecraft.player.rotationYaw < f) {
                Minecraft.player.rotationYaw += 0.5f;
            }
            if (Minecraft.player.rotationYaw > f) {
                Minecraft.player.rotationYaw -= 0.5f;
            }
            if (Minecraft.player.rotationPitch < f2) {
                Minecraft.player.rotationPitch += 0.5f;
            }
            if (!(Minecraft.player.rotationPitch > f2)) continue;
            Minecraft.player.rotationPitch -= 0.5f;
        }
    }

    public static float[] faceTarget(Entity target, float p_706252, float p_706253, boolean miss) {
        double var7;
        double var4 = target.posX - Minecraft.player.posX;
        double var5 = target.posZ - Minecraft.player.posZ;
        if (target instanceof EntityLivingBase) {
            EntityLivingBase var6 = (EntityLivingBase)target;
            var7 = var6.posY + (double)var6.getEyeHeight() - (Minecraft.player.posY + (double)Minecraft.player.getEyeHeight());
        } else {
            var7 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.player.posY + (double)Minecraft.player.getEyeHeight());
        }
        double var8 = MathHelper.sqrt(var4 * var4 + var5 * var5);
        float var9 = (float)(Math.atan2(var5, var4) * 180.0 / Math.PI) - 90.0f;
        float var10 = (float)(-(Math.atan2(var7 - (target instanceof EntityPlayer ? 0.25 : 0.0), var8) * 180.0 / Math.PI));
        float f = AimAssist.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
        float gcd = f * f * f * 1.2f;
        float pitch = RotationUtil.updateRotation(Minecraft.player.rotationPitch, var10, p_706253);
        float yaw = RotationUtil.updateRotation(Minecraft.player.rotationYaw, var9, p_706252);
        yaw -= yaw % gcd;
        pitch -= pitch % gcd;
        return new float[]{yaw, pitch};
    }
}

