/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.combat.AntiBot;
import org.celestial.client.friend.Friend;
import org.celestial.client.helpers.math.GCDCalcHelper;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.world.EntityHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class AutoBow
extends Feature {
    private final ListSetting sort = new ListSetting("Target Sort Mode", "Distance", () -> true, "Distance", "Higher Armor", "Lowest Armor", "Health", "Angle", "HurtTime");
    private final BooleanSetting players = new BooleanSetting("Players", true, () -> true);
    private final BooleanSetting mobs = new BooleanSetting("Mobs", false, () -> true);
    private final BooleanSetting animals = new BooleanSetting("Animals", false, () -> true);
    private final NumberSetting distance = new NumberSetting("Distance", 20.0f, 5.0f, 100.0f, 1.0f);
    private final NumberSetting predictRotations = new NumberSetting("Rotation Predict", 1.5f, 0.0f, 5.0f, 0.1f);
    private final BooleanSetting autoShot = new BooleanSetting("Auto Shot", true, () -> true);
    private final NumberSetting shotDelay = new NumberSetting("Shot Delay", 20.0f, 1.0f, 30.0f, 1.0f, this.autoShot::getCurrentValue);
    private final BooleanSetting ignoreNakedPlayers = new BooleanSetting("Ignore Naked Players", false, () -> true);

    public AutoBow() {
        super("AutoBow", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0430\u0438\u043c\u0438\u0442\u0441\u044f \u0438 \u0441\u0442\u0440\u0435\u043b\u044f\u0435\u0442 \u0432 \u0438\u0433\u0440\u043e\u043a\u043e\u0432", Type.Combat);
        this.addSettings(this.sort, this.players, this.mobs, this.animals, this.distance, this.predictRotations, this.autoShot, this.shotDelay, this.ignoreNakedPlayers);
    }

    private EntityLivingBase getSortEntities() {
        ArrayList<EntityLivingBase> entity = new ArrayList<EntityLivingBase>();
        for (Entity e : AutoBow.mc.world.loadedEntityList) {
            EntityLivingBase player;
            if (!(e instanceof EntityLivingBase) || !(AutoBow.mc.player.getDistanceToEntity(player = (EntityLivingBase)e) < this.distance.getCurrentValue()) || !this.canBowing(player)) continue;
            if (player.getHealth() > 0.0f) {
                entity.add(player);
                continue;
            }
            entity.remove(player);
        }
        String sortMode = this.sort.getOptions();
        if (sortMode.equalsIgnoreCase("Angle")) {
            entity.sort((o1, o2) -> (int)(Math.abs(RotationHelper.getAngleEntity(o1) - AutoBow.mc.player.rotationYaw) - Math.abs(RotationHelper.getAngleEntity(o2) - AutoBow.mc.player.rotationYaw)));
        } else if (sortMode.equalsIgnoreCase("Higher Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue).reversed());
        } else if (sortMode.equalsIgnoreCase("Lowest Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue));
        } else if (sortMode.equalsIgnoreCase("Health")) {
            entity.sort((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
        } else if (sortMode.equalsIgnoreCase("Distance")) {
            entity.sort(Comparator.comparingDouble(AutoBow.mc.player::getDistanceToEntity));
        } else if (sortMode.equalsIgnoreCase("HurtTime")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getHurtTime).reversed());
        }
        if (entity.isEmpty()) {
            return null;
        }
        return (EntityLivingBase)entity.get(0);
    }

    private boolean canBowing(EntityLivingBase player) {
        for (Friend friend : Celestial.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) continue;
            return false;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(AntiBot.class).getState() && !AntiBot.isRealPlayer.contains(player) && AntiBot.mode.currentMode.equals("Need Hit")) {
            return false;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(AntiBot.class).getState() && AntiBot.isBotPlayer.contains(player) && (AntiBot.mode.currentMode.equals("Matrix New") || AntiBot.mode.currentMode.equals("Matrix"))) {
            return false;
        }
        if (this.ignoreNakedPlayers.getCurrentValue() && player instanceof EntityPlayer && EntityHelper.checkArmor(player)) {
            return false;
        }
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityWaterMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !this.players.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !this.animals.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityWaterMob && !this.mobs.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityMob && !this.mobs.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !this.mobs.getCurrentValue()) {
                return false;
            }
        }
        if (!AutoBow.mc.player.canEntityBeSeen(player)) {
            return false;
        }
        if (player instanceof EntityArmorStand) {
            return false;
        }
        return player != AutoBow.mc.player;
    }

    private float[] getBowRotations(Entity target, boolean predict, float predictSize) {
        EntityPlayerSP player = AutoBow.mc.player;
        double posX = target.posX + (predict ? (target.posX - target.prevPosX) * (double)predictSize : 0.0) - (player.posX + (predict ? player.posX - player.prevPosX : 0.0));
        double posY = target.getEntityBoundingBox().minY + (predict ? (target.getEntityBoundingBox().minY - target.prevPosY) * (double)predictSize : 0.0) + (double)target.getEyeHeight() - 0.15 - (player.getEntityBoundingBox().minY + (predict ? player.posY - player.prevPosY : 0.0)) - (double)player.getEyeHeight();
        double posZ = target.posZ + (predict ? (target.posZ - target.prevPosZ) * (double)predictSize : 0.0) - (player.posZ + (predict ? player.posZ - player.prevPosZ : 0.0));
        double posSqrt = Math.sqrt(posX * posX + posZ * posZ);
        float velocity = (float)player.getItemInUseDuration() / 20.0f;
        if ((velocity = (velocity * velocity + velocity * 2.0f) / 3.0f) > 1.0f) {
            velocity = 1.0f;
        }
        float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt((double)(velocity * velocity * velocity * velocity) - (double)0.006f * ((double)0.006f * (posSqrt * posSqrt) + 2.0 * posY * (double)(velocity * velocity)))) / ((double)0.006f * posSqrt))));
        yaw = AutoBow.mc.player.rotationYaw + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(yaw - AutoBow.mc.player.rotationYaw));
        pitch = AutoBow.mc.player.rotationPitch + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(pitch - AutoBow.mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        return new float[]{yaw, pitch};
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        EntityLivingBase entity = this.getSortEntities();
        if (entity == null) {
            return;
        }
        if (AutoBow.mc.player.isBowing()) {
            float[] rots = this.getBowRotations(entity, true, this.predictRotations.getCurrentValue());
            RotationHelper.setRotations(event, rots[0], rots[1], rots[0], rots[1]);
            if ((float)AutoBow.mc.player.getItemInUseDuration() >= this.shotDelay.getCurrentValue() && this.autoShot.getCurrentValue()) {
                AutoBow.mc.playerController.onStoppedUsingItem(AutoBow.mc.player);
                AutoBow.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
    }
}

