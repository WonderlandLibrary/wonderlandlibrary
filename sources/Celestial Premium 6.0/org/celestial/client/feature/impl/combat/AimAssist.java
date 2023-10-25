/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.combat.AntiBot;
import org.celestial.client.friend.Friend;
import org.celestial.client.helpers.math.GCDCalcHelper;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.world.EntityHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class AimAssist
extends Feature {
    public static NumberSetting range;
    public static BooleanSetting ignoreNakedPlayers;
    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting team;
    public static BooleanSetting walls;
    public static BooleanSetting invis;
    public static BooleanSetting click;
    public static NumberSetting fov;
    public static NumberSetting predict;
    public static NumberSetting rotYawSpeed;
    public static NumberSetting rotPitchSpeed;
    public static NumberSetting rotYawRandom;
    public static NumberSetting rotPitchRandom;
    public static ListSetting sort;
    public static ListSetting part;

    public AimAssist() {
        super("AimAssist", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0439 \u0430\u0438\u043c \u043d\u0430 \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u0435\u0439 \u0432\u043e\u043a\u0440\u0443\u0433 \u0442\u0435\u0431\u044f", Type.Combat);
        sort = new ListSetting("Assist Sort Mode", "Distance", () -> true, "Distance", "Higher Armor", "Lowest Armor", "Health", "Angle", "HurtTime");
        part = new ListSetting("Aim-Part Mode", "Chest", () -> true, "Chest", "Head", "Leggings", "Boots");
        range = new NumberSetting("Range", 4.0f, 1.0f, 6.0f, 0.01f, () -> true);
        players = new BooleanSetting("Players", true, () -> true);
        mobs = new BooleanSetting("Mobs", false, () -> true);
        team = new BooleanSetting("Team Check", false, () -> true);
        walls = new BooleanSetting("Walls", false, () -> true);
        invis = new BooleanSetting("Invisible", false, () -> true);
        click = new BooleanSetting("Click Only", false, () -> true);
        predict = new NumberSetting("Aim Predict", 0.5f, 0.0f, 5.0f, 0.1f, () -> true);
        fov = new NumberSetting("Assist FOV", 180.0f, 5.0f, 180.0f, 5.0f, () -> true);
        rotYawSpeed = new NumberSetting("Rotation Yaw Speed", 1.0f, 0.1f, 5.0f, 0.1f, () -> true);
        rotPitchSpeed = new NumberSetting("Rotation Pitch Speed", 1.0f, 0.1f, 5.0f, 0.1f, () -> true);
        rotYawRandom = new NumberSetting("Yaw Randomize", 1.0f, 0.0f, 3.0f, 0.1f, () -> true);
        rotPitchRandom = new NumberSetting("Pitch Randomize", 1.0f, 0.0f, 3.0f, 0.1f, () -> true);
        ignoreNakedPlayers = new BooleanSetting("Ignore Naked Players", false, () -> true);
        this.addSettings(players, mobs, walls, invis, team, click, range, predict, fov, rotYawSpeed, rotPitchSpeed, rotPitchRandom, rotYawRandom, sort, part, ignoreNakedPlayers);
    }

    public static boolean canSeeEntityAtFov(Entity entityIn, float scope) {
        double diffZ = entityIn.posZ - AimAssist.mc.player.posZ;
        double diffX = entityIn.posX - AimAssist.mc.player.posX;
        float newYaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        double difference = AimAssist.angleDifference(newYaw, AimAssist.mc.player.rotationYaw);
        return difference <= (double)scope;
    }

    public static double angleDifference(double a, double b) {
        float yaw360 = (float)(Math.abs(a - b) % 360.0);
        if (yaw360 > 180.0f) {
            yaw360 = 360.0f - yaw360;
        }
        return yaw360;
    }

    public static EntityLivingBase getSortEntities() {
        ArrayList<EntityLivingBase> entity = new ArrayList<EntityLivingBase>();
        for (Entity e : AimAssist.mc.world.loadedEntityList) {
            EntityLivingBase player;
            if (!(e instanceof EntityLivingBase) || !(AimAssist.mc.player.getDistanceToEntity(player = (EntityLivingBase)e) < range.getCurrentValue()) || !AimAssist.canAssist(player)) continue;
            if (player.getHealth() > 0.0f) {
                entity.add(player);
                continue;
            }
            entity.remove(player);
        }
        String sortMode = sort.getOptions();
        if (sortMode.equalsIgnoreCase("Angle")) {
            entity.sort((o1, o2) -> (int)(Math.abs(RotationHelper.getAngleEntity(o1) - AimAssist.mc.player.rotationYaw) - Math.abs(RotationHelper.getAngleEntity(o2) - AimAssist.mc.player.rotationYaw)));
        } else if (sortMode.equalsIgnoreCase("Higher Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue).reversed());
        } else if (sortMode.equalsIgnoreCase("Lowest Armor")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getTotalArmorValue));
        } else if (sortMode.equalsIgnoreCase("Health")) {
            entity.sort((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
        } else if (sortMode.equalsIgnoreCase("Distance")) {
            entity.sort(Comparator.comparingDouble(AimAssist.mc.player::getDistanceToEntity));
        } else if (sortMode.equalsIgnoreCase("HurtTime")) {
            entity.sort(Comparator.comparing(EntityLivingBase::getHurtTime).reversed());
        }
        if (entity.isEmpty()) {
            return null;
        }
        return (EntityLivingBase)entity.get(0);
    }

    public static boolean canAssist(EntityLivingBase player) {
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
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityWaterMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !players.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !mobs.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityWaterMob && !mobs.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityMob && !mobs.getCurrentValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !mobs.getCurrentValue()) {
                return false;
            }
        }
        if (ignoreNakedPlayers.getCurrentValue() && player instanceof EntityPlayer && EntityHelper.checkArmor(player)) {
            return false;
        }
        if (player.isInvisible() && !invis.getCurrentValue()) {
            return false;
        }
        if (ignoreNakedPlayers.getCurrentValue() && EntityHelper.checkArmor(player)) {
            return false;
        }
        if (!AimAssist.canSeeEntityAtFov(player, fov.getCurrentValue() * 2.0f)) {
            return false;
        }
        if (team.getCurrentValue() && EntityHelper.isTeamWithYou(player)) {
            return false;
        }
        if (!player.canEntityBeSeen(AimAssist.mc.player)) {
            return walls.getCurrentValue();
        }
        return player != AimAssist.mc.player;
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        EntityLivingBase entity = AimAssist.getSortEntities();
        if (entity != null && AimAssist.mc.player.getDistanceToEntity(entity) <= range.getCurrentValue() && entity != AimAssist.mc.player) {
            float[] rots = this.getRotationsForAssist(entity);
            if (click.getCurrentValue() && !AimAssist.mc.gameSettings.keyBindAttack.isKeyDown()) {
                return;
            }
            if (AimAssist.canAssist(entity) && entity.getHealth() > 0.0f) {
                AimAssist.mc.player.rotationYaw = rots[0];
                AimAssist.mc.player.rotationPitch = rots[1];
            }
        }
    }

    private float[] getRotationsForAssist(EntityLivingBase entityIn) {
        float yaw = RotationHelper.updateRotation(GCDCalcHelper.getFixedRotation(AimAssist.mc.player.rotationYaw + MathematicHelper.randomizeFloat(-rotYawRandom.getCurrentValue(), rotYawRandom.getCurrentValue())), this.getRotation(entityIn, predict.getCurrentValue())[0], rotYawSpeed.getCurrentValue() * 10.0f);
        float pitch = RotationHelper.updateRotation(GCDCalcHelper.getFixedRotation(AimAssist.mc.player.rotationPitch + MathematicHelper.randomizeFloat(-rotPitchRandom.getCurrentValue(), rotPitchRandom.getCurrentValue())), this.getRotation(entityIn, predict.getCurrentValue())[1], rotPitchSpeed.getCurrentValue() * 10.0f);
        return new float[]{yaw, pitch};
    }

    private float[] getRotation(Entity e, float predictValue) {
        String mode = part.getOptions();
        float aimPoint = 0.0f;
        if (mode.equalsIgnoreCase("Head")) {
            aimPoint = 0.0f;
        } else if (mode.equalsIgnoreCase("Chest")) {
            aimPoint = 0.5f;
        } else if (mode.equalsIgnoreCase("Leggings")) {
            aimPoint = 0.9f;
        } else if (mode.equalsIgnoreCase("Boots")) {
            aimPoint = 1.3f;
        }
        double xDelta = e.posX + (e.posX - e.prevPosX) * (double)predictValue - AimAssist.mc.player.posX - AimAssist.mc.player.motionX * (double)predictValue;
        double zDelta = e.posZ + (e.posZ - e.prevPosZ) * (double)predictValue - AimAssist.mc.player.posZ - AimAssist.mc.player.motionZ * (double)predictValue;
        double diffY = e.posY + (double)e.getEyeHeight() - (AimAssist.mc.player.posY + (double)AimAssist.mc.player.getEyeHeight() + (double)aimPoint);
        double distance = MathHelper.sqrt(xDelta * xDelta + zDelta * zDelta);
        float yaw = (float)(MathHelper.atan2(zDelta, xDelta) * 180.0 / Math.PI - 90.0) + MathematicHelper.randomizeFloat(-rotYawRandom.getCurrentValue(), rotYawRandom.getCurrentValue());
        float pitch = (float)(-(MathHelper.atan2(diffY, distance) * 180.0 / Math.PI)) + MathematicHelper.randomizeFloat(-rotPitchRandom.getCurrentValue(), rotPitchRandom.getCurrentValue());
        yaw = AimAssist.mc.player.rotationYaw + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(yaw - AimAssist.mc.player.rotationYaw));
        pitch = AimAssist.mc.player.rotationPitch + GCDCalcHelper.getFixedRotation(MathHelper.wrapDegrees(pitch - AimAssist.mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        return new float[]{yaw, pitch};
    }
}

