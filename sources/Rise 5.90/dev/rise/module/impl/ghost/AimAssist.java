/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.ghost;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.combat.AntiBot;
import dev.rise.module.impl.combat.Aura;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.PlayerUtil;
import dev.rise.util.player.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.RandomUtils;

@ModuleInfo(name = "AimAssist", description = "Helps you aim at people", category = Category.GHOST)
public final class AimAssist extends Module {

    public static EntityLivingBase target;

    private final NumberSetting strength = new NumberSetting("Strength", this, 40, 1, 50, 0.1);
    private final NumberSetting range = new NumberSetting("Range", this, 6, 0.1, 10, 0.1);

    private final BooleanSetting players = new BooleanSetting("Players", this, true);
    private final BooleanSetting nonPlayers = new BooleanSetting("Non Players", this, true);
    private final BooleanSetting teams = new BooleanSetting("Teams", this, true);
    private final BooleanSetting invisibles = new BooleanSetting("Invisibles", this, false);
    private final BooleanSetting dead = new BooleanSetting("Dead", this, false);
    private final BooleanSetting onlySword = new BooleanSetting("Only Sword", this, false);
    private final BooleanSetting vertical = new BooleanSetting("Vertical", this, true);

    public static int deltaX, deltaY;

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        target = getClosest(range.getValue());

        final float s = (float) (strength.getMaximum() - strength.getValue()) + 1;

        if (target == null || onlySword.isEnabled() && !PlayerUtil.isHoldingSword() || !mc.thePlayer.canEntityBeSeen(target)) {
            deltaX = deltaY = 0;
            return;
        }

        final float[] rotations = getRotations();

        final float targetYaw = (float) (rotations[0] + Math.random());
        final float targetPitch = (float) (rotations[1] + Math.random());

        final float niggaYaw = (targetYaw - mc.thePlayer.rotationYaw) / Math.max(2, s);
        final float niggaPitch = (targetPitch - mc.thePlayer.rotationPitch) / Math.max(2, s);

        final float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        final float gcd = f * f * f * 8.0F;

        deltaX = Math.round(niggaYaw / gcd);

        if (vertical.isEnabled()) deltaY = Math.round(niggaPitch / gcd);
        else deltaY = 0;
    }

    @Override
    protected void onDisable() {
        deltaX = 0;
        deltaY = 0;
    }

    /**
     * Gets the closest target in a range for the aura to decide which entity it will attack.
     *
     * @param range The maximum range the closest entity will be searched for.
     * @return The closest entity in a range.
     */
    private EntityLivingBase getClosest(final double range) {
        if (mc.theWorld == null) return null;

        double dist = range;
        EntityLivingBase target = null;

        for (final Entity entity : mc.theWorld.loadedEntityList) {

            if (entity instanceof EntityLivingBase && !AntiBot.bots.contains(entity)) {
                final EntityLivingBase livingBase = (EntityLivingBase) entity;

                if (canAttack(livingBase)) {
                    final double currentDist = mc.thePlayer.getDistanceToEntity(livingBase);

                    if (currentDist <= dist) {
                        dist = currentDist;
                        target = livingBase;
                    }
                }
            }
        }

        return target;
    }

    private boolean canAttack(final EntityLivingBase player) {
        if (player instanceof EntityPlayer && !players.isEnabled()) {
            return false;
        }

        if (player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (!nonPlayers.isEnabled())
                return false;
        }

        if (player.isInvisible() && !invisibles.isEnabled())
            return false;

        if (player.isDead && !dead.isEnabled())
            return false;

        if (AntiBot.bots.contains(player))
            return false;

        if (player.isOnSameTeam(mc.thePlayer) && teams.isEnabled())
            return false;

        if (player.ticksExisted < 2)
            return false;

        for (final String name : Rise.INSTANCE.getFriends()) {
            if (name.equals(player.getCommandSenderName())) {
                return false;
            }
        }

        return mc.thePlayer != player;
    }

    private float[] getRotations() {
        /*
         * Using some basic math this method gets the rotations needed to look inside an entities BoundingBox.
         *
         * Credits to Alan
         */
        final double var4 = (target.posX - (target.lastTickPosX - target.posX)) + 0.01 - mc.thePlayer.posX;
        final double var6 = (target.posZ - (target.lastTickPosZ - target.posZ)) - mc.thePlayer.posZ;
        final double var8 = (target.posY - (target.lastTickPosY - target.posY)) + 0.4 + target.getEyeHeight() / 1.3 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());

        final double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);

        float yaw = (float) (Math.atan2(var6, var4) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(var8, var14) * 180.0D / Math.PI);

        yaw = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
        pitch = mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);


        /*
         * Gets the current and last rotations and smooths them for aura to be harder to flag.
         */
        final float[] rotations = new float[]{yaw, pitch};
        final float[] lastRotations = new float[]{Aura.yaw, Aura.pitch};

        final float[] fixedRotations = RotationUtil.getFixedRotation(rotations, lastRotations);

        yaw = fixedRotations[0];
        pitch = fixedRotations[1];


        // Clamps the pitch so that aura doesn't flag everything with an illegal rotation.
        pitch = MathHelper.clamp_float(pitch, -90.0F, 90.0F);

        return new float[]{yaw, pitch};
    }

    public double randomBetween(final double min, final double max) {
        return ((Math.random() * ((RandomUtils.nextDouble(max, max + 1)) - RandomUtils.nextDouble(min, min + 1)) + 1) + min);
    }

}
