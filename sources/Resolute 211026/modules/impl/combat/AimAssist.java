// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import vip.Resolute.ui.click.skeet.SkeetUI;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import vip.Resolute.util.player.RotationUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import net.minecraft.entity.EntityLivingBase;
import java.util.Random;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class AimAssist extends Module
{
    public ModeSetting mode;
    private NumberSetting range;
    private NumberSetting fov;
    private NumberSetting aimspeed;
    private NumberSetting horizontal;
    private NumberSetting vertical;
    private NumberSetting ticksExisted;
    private NumberSetting horizontalSpeed;
    private NumberSetting verticalSpeed;
    private BooleanSetting onSword;
    protected Random rand;
    public static EntityLivingBase target;
    float oldSens;
    
    public boolean isModeSelected() {
        return this.mode.is("Rotate");
    }
    
    public AimAssist() {
        super("AimAssist", 0, "Rotates the player to assist with aim", Category.COMBAT);
        this.mode = new ModeSetting("Mode", "Rotate", new String[] { "Sensitivity", "Rotate" });
        this.range = new NumberSetting("Range", 4.0, this::isModeSelected, 1.0, 8.0, 0.1);
        this.fov = new NumberSetting("FOV", 250.0, this::isModeSelected, 1.0, 360.0, 1.0);
        this.aimspeed = new NumberSetting("Aim Speed", 5.0, this::isModeSelected, 0.1, 10.0, 0.1);
        this.horizontal = new NumberSetting("Horizontal", 9.0, this::isModeSelected, 0.1, 10.0, 0.1);
        this.vertical = new NumberSetting("Vertical", 9.0, this::isModeSelected, 0.1, 10.0, 0.1);
        this.ticksExisted = new NumberSetting("Ticks Existed", 100.0, this::isModeSelected, 0.0, 1000.0, 10.0);
        this.horizontalSpeed = new NumberSetting("Horizontal Speed", 80.0, this::isModeSelected, 1.0, 100.0, 1.0);
        this.verticalSpeed = new NumberSetting("Vertical Speed", 80.0, this::isModeSelected, 1.0, 100.0, 1.0);
        this.onSword = new BooleanSetting("On Sword", true, this::isModeSelected);
        this.rand = new Random();
        this.addSettings(this.mode, this.range, this.fov, this.aimspeed, this.horizontal, this.vertical, this.ticksExisted, this.onSword);
        this.oldSens = AimAssist.mc.gameSettings.mouseSensitivity;
    }
    
    @Override
    public void onEnable() {
        this.oldSens = AimAssist.mc.gameSettings.mouseSensitivity;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(String.valueOf(this.range.getValue()));
        if (e instanceof EventMotion && e.isPre()) {
            if (this.mode.is("Sensitivity")) {
                AimAssist.mc.gameSettings.mouseSensitivity = ((AimAssist.mc.objectMouseOver.entityHit != null) ? 0.1f : this.oldSens);
            }
            if (this.mode.is("Rotate")) {
                if (this.onSword.isEnabled() && !this.isHoldingSword()) {
                    return;
                }
                AimAssist.target = this.getClosest(this.range.getValue());
                if (AimAssist.target != null) {
                    double horizontalSpeed = this.horizontal.getValue() * 3.0 + ((this.horizontal.getValue() > 0.0) ? this.rand.nextDouble() : 0.0);
                    double verticalSpeed = this.vertical.getValue() * 3.0 + ((this.vertical.getValue() > 0.0) ? this.rand.nextDouble() : 0.0);
                    horizontalSpeed *= this.aimspeed.getValue();
                    verticalSpeed *= this.aimspeed.getValue();
                    this.faceTarget(AimAssist.target, 0.0f, (float)verticalSpeed);
                    this.faceTarget(AimAssist.target, (float)horizontalSpeed, 0.0f);
                }
            }
        }
    }
    
    private void faceTarget(final EntityLivingBase targets, final float yawspeed, final float pitchspeed) {
        final EntityPlayerSP player = AimAssist.mc.thePlayer;
        final float yaw = this.getRotations(targets)[0];
        final float pitch = this.getRotations(targets)[1];
        player.rotationYaw = this.getRotation(player.rotationYaw, yaw, yawspeed);
        player.rotationPitch = this.getRotation(player.rotationPitch, pitch, pitchspeed);
    }
    
    public float[] getRotations(final EntityLivingBase e) {
        return RotationUtils.getRotations2(e);
    }
    
    protected float getRotation(final float currentRotation, final float targetRotation, final float maxIncrement) {
        float deltaAngle = MathHelper.wrapAngleTo180_float(targetRotation - currentRotation);
        if (deltaAngle > maxIncrement) {
            deltaAngle = maxIncrement;
        }
        if (deltaAngle < -maxIncrement) {
            deltaAngle = -maxIncrement;
        }
        return currentRotation + deltaAngle / 2.0f;
    }
    
    private boolean isHoldingSword() {
        return AimAssist.mc.thePlayer.getCurrentEquippedItem() != null && AimAssist.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }
    
    private EntityLivingBase getClosest(final double range) {
        double dist = range;
        EntityLivingBase target = null;
        for (final Object object : AimAssist.mc.theWorld.loadedEntityList) {
            final Entity entity = (Entity)object;
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase player = (EntityLivingBase)entity;
                if (!this.canAttack(player)) {
                    continue;
                }
                final double currentDist = AimAssist.mc.thePlayer.getDistanceToEntity(player);
                if (currentDist > dist) {
                    continue;
                }
                dist = currentDist;
                target = player;
            }
        }
        return target;
    }
    
    public boolean canAttack(final EntityLivingBase player) {
        if (player == AimAssist.mc.thePlayer) {
            return false;
        }
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !SkeetUI.isPlayers()) {
                return false;
            }
            if (player instanceof EntityAnimal && !SkeetUI.isAnimals()) {
                return false;
            }
            if (player instanceof EntityMob && !SkeetUI.isMobs()) {
                return false;
            }
            if (!player.isEntityAlive()) {
                return false;
            }
            if (player instanceof EntityVillager && !SkeetUI.isVillagers()) {
                return false;
            }
        }
        return this.isInFOV(player, this.fov.getValue()) && (!(player instanceof EntityPlayer) || !isTeam(AimAssist.mc.thePlayer, (EntityPlayer)player) || !SkeetUI.isTeams()) && (!player.isInvisible() || SkeetUI.isInvisibles()) && player.ticksExisted > this.ticksExisted.getValue();
    }
    
    private boolean isInFOV(final EntityLivingBase entity, double angle) {
        angle *= 0.5;
        final double angleDiff = this.getAngleDifference(AimAssist.mc.thePlayer.rotationYaw, this.getRotations(entity.posX, entity.posY, entity.posZ)[0]);
        return (angleDiff > 0.0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0.0);
    }
    
    private float getAngleDifference(final float dir, final float yaw) {
        final float f = Math.abs(yaw - dir) % 360.0f;
        final float dist = (f > 180.0f) ? (360.0f - f) : f;
        return dist;
    }
    
    private float[] getRotations(final double x, final double y, final double z) {
        final double diffX = x + 0.5 - AimAssist.mc.thePlayer.posX;
        final double diffY = (y + 0.5) / 2.0 - (AimAssist.mc.thePlayer.posY + AimAssist.mc.thePlayer.getEyeHeight());
        final double diffZ = z + 0.5 - AimAssist.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static boolean isTeam(final EntityPlayer e, final EntityPlayer e2) {
        if (e2.getTeam() != null && e.getTeam() != null) {
            final Character target = e2.getDisplayName().getFormattedText().charAt(1);
            final Character player = e.getDisplayName().getFormattedText().charAt(1);
            return target.equals(player);
        }
        return true;
    }
}
