/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.combat;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class TriggerBot
extends Module {
    public TriggerBot() {
        super("TriggerBot", Category.Combat);
    }

    @EventTarget
    public void onEventUpdate(EventUpdate e) {
        Entity entity;
        block5: {
            block4: {
                entity = TriggerBot.mc.objectMouseOver.entityHit;
                if (entity == null) break block4;
                if (!((double)Minecraft.player.getDistanceToEntity(entity) > 3.5) && !(entity instanceof EntityEnderCrystal) && !entity.isDead && !(((EntityLivingBase)entity).getHealth() <= 0.0f) && entity instanceof EntityPlayer) break block5;
            }
            return;
        }
        if (Minecraft.player.getCooledAttackStrength(0.0f) == 1.0f) {
            TriggerBot.mc.playerController.attackEntity(Minecraft.player, entity);
            Minecraft.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    public static float[] getRotations(EntityPlayer entityPlayer) {
        double d = entityPlayer.posX + (entityPlayer.posX - entityPlayer.lastTickPosX) - Minecraft.player.posX;
        double d2 = entityPlayer.posY + (double)entityPlayer.getEyeHeight() - Minecraft.player.posY + (double)Minecraft.player.getEyeHeight() - 3.5;
        double d3 = entityPlayer.posZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) - Minecraft.player.posZ;
        double d4 = Math.sqrt(Math.pow(d, 2.0) + Math.pow(d3, 2.0));
        float f = (float)Math.toDegrees(-Math.atan(d / d3));
        float f2 = (float)(-Math.toDegrees(Math.atan(d2 / d4)));
        if (d < 0.0 && d3 < 0.0) {
            f = (float)(90.0 + Math.toDegrees(Math.atan(d3 / d)));
        } else if (d > 0.0 && d3 < 0.0) {
            f = (float)(-90.0 + Math.toDegrees(Math.atan(d3 / d)));
        }
        return new float[]{f, f2};
    }
}

