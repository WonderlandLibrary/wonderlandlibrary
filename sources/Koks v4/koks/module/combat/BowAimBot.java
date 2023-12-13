package koks.module.combat;

import koks.Koks;
import koks.api.FormulaHelper;
import koks.api.PlayerHandler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.RotationUtil;
import koks.api.manager.value.annotation.Value;
import koks.api.utils.TimeHelper;
import koks.event.*;
import koks.module.player.FastUse;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.*;

import java.util.Comparator;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
@Module.Info(name = "BowAimBot", description = "You aim automatically at players", category = Module.Category.COMBAT)
public class BowAimBot extends Module {

    @Value(name = "OnlyPlayers")
    boolean onlyPlayers = true;

    @Value(name = "AutoShoot")
    boolean autoShoot = true;

    @Value(name = "ThroughWalls")
    boolean throughWalls = true;

    @Value(name = "MoveFix")
    boolean moveFix = false;

    @Value(name = "Clamp Yaw")
    boolean clampYaw = false;

    @Value(name = "LockView")
    boolean lockView = false;

    private Entity curEntity;

    final TimeHelper timeHelper = new TimeHelper();

    //TODO: FOV

    @Override
    @Event.Info
    public void onEvent(Event event) {
        final RotationUtil rotationUtil = RotationUtil.getInstance();

        if (event instanceof UpdateEvent) {
            if (curEntity != null) {
                if (!isValid(curEntity))
                    curEntity = null;
            }
        }

        if (event instanceof final UpdatePlayerMovementState updatePlayerMovementState) {
            if (moveFix && allowAiming(getPlayer())) {
                updatePlayerMovementState.setYaw(PlayerHandler.yaw);
                updatePlayerMovementState.setSilentMoveFix(true);
            }
        }

        if (event instanceof final MoveEvent moveEvent) {
            if (moveFix && allowAiming(getPlayer()))
                moveEvent.setYaw(PlayerHandler.yaw);
        }

        if (event instanceof final JumpEvent jumpEvent) {
            if (moveFix && allowAiming(getPlayer())) {
                jumpEvent.setYaw(PlayerHandler.yaw);
            }
        }

        if (event instanceof final RotationEvent rotationEvent) {

            if (lockView && allowAiming(getPlayer())) {
                getPlayer().rotationYaw = PlayerHandler.yaw;
                getPlayer().rotationPitch = PlayerHandler.pitch;
            }

            if (getGameSettings().keyBindUseItem.pressed && autoShoot && allowAiming(getPlayer())) {
                rotationEvent.setYaw(PlayerHandler.yaw);
                rotationEvent.setPitch(PlayerHandler.pitch);
            }

            Entity entity = getClosestEntity();
            curEntity = entity;
            if (isUsing(getPlayer())) {
                if (entity != null && entity != getPlayer()) {
                    final double deltaX = entity.posX - getX();
                    double deltaY = (entity.posY + entity.getEyeHeight()) - (getY() + getPlayer().getEyeHeight());
                    final double deltaZ = entity.posZ - getZ();

                    if (!(entity instanceof EntityPlayer))
                        deltaY = (entity.posY + entity.getEyeHeight()) - (getY() + getPlayer().getEyeHeight());

                    final double x = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ); //distance
                    final double v = getVelocity();
                    final double g = getGravity();

                    float pitch = FormulaHelper.getProjectileMotion(v, g, x, deltaY);
                    float[] rotations = rotationUtil.facePlayer(entity, false, false, false, true, true, 5, false, 0, clampYaw, 180);
                    pitch = MathHelper.clamp_float(pitch, -90, 90);

                    if (!Float.isNaN(pitch)) {
                        rotationEvent.setYaw(rotations[0]);
                        rotationEvent.setPitch(pitch);
                    }

                    if (v == 1F && autoShoot) {
                        if (timeHelper.hasReached(200))
                            getPlayerController().onStoppedUsingItem(getPlayer());
                    } else if (autoShoot) {
                        timeHelper.reset();
                    }

                        /*final float ticksInAir = simulateArrow(entity);
                        deltaX = ((entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ticksInAir) - getX());
                        deltaY = ((entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ticksInAir) - getY());
                        deltaZ = ((entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ticksInAir) - getZ());

                        rotations = rotationUtil.faceVector(new Vec3(deltaX, deltaY, deltaZ));
                        pitch = FormulaHelper.getProjectileMotion(v,g,x,deltaY);

                        pitch = MathHelper.clamp_float(pitch, -90, 90);

                        if (!Float.isNaN(pitch)) {
                            updateMotionEvent.setYaw(rotations[0]);
                            updateMotionEvent.setPitch(pitch);
                            curYaw = rotations[0];
                            curPitch = pitch;
                        }*/
                }
            }
        }
    }

    public boolean allowAiming(EntityPlayer player) {
        return isUsing(player) && curEntity != null;
    }

    public boolean isUsing(EntityPlayer player) {
        return player.isUsingItem() && getPlayer().getCurrentEquippedItem() != null && getPlayer().getCurrentEquippedItem().getItem() instanceof ItemBow;
    }

    public float simulateArrow(Entity entity) {
        float ticksInAir = 0;
        final Vec3 eyePosition = getPlayer().getPositionEyes(1F);
        float motionX, motionY, motionZ, posX = (float) eyePosition.xCoord, posY = (float) eyePosition.yCoord, posZ = (float) eyePosition.zCoord;
        posX -= (double) (MathHelper.cos(getYaw() / 180.0F * (float) Math.PI) * 0.16F);
        posY -= 0.10000000149011612D;
        posZ -= (double) (MathHelper.sin(getYaw() / 180.0F * (float) Math.PI) * 0.16F);
        motionX = (-MathHelper.sin(getYaw() / 180.0F * (float) Math.PI) * MathHelper.cos(getPitch() / 180.0F * (float) Math.PI));
        motionZ = (MathHelper.cos(getYaw() / 180.0F * (float) Math.PI) * MathHelper.cos(getPitch() / 180.0F * (float) Math.PI));
        motionY = -MathHelper.sin(getPitch() / 180.0F * (float) Math.PI);

        do {
            ticksInAir++;
            float f4 = 0.99F;
            final float f6 = 0.05F;
            final Block block = getWorld().getBlockState(new BlockPos(posX, posY, posZ)).getBlock();
            if (block instanceof BlockLiquid) {
                f4 = 0.6F;
            }
            posX += motionX;
            posY += motionY;
            posZ += motionZ;

            motionX *= (double) f4;
            motionY *= (double) f4;
            motionZ *= (double) f4;
            motionY -= (double) f6;

            if (entity.getDistance(posX, posY, posZ) <= 1)
                break;

        } while (getPlayer().getDistance(posX, posY, posZ) <= getPlayer().getDistanceToEntity(entity));
        return ticksInAir;
    }

    public EntityLivingBase getClosestEntity() {
        return (EntityLivingBase) getWorld().loadedEntityList.stream().filter(this::isValid).sorted(Comparator.comparingDouble(entity -> getPlayer().getDistanceToEntity(entity))).findFirst().orElse(null);
    }

    public boolean isValid(Entity entity) {
        if (!(entity instanceof EntityLivingBase))
            return false;
        if (entity == getPlayer())
            return false;
        if (!(entity instanceof EntityPlayer) && onlyPlayers)
            return false;
        if (!throughWalls && !getPlayer().canEntityBeSeen(entity))
            return false;
        if (entity.isInvisible())
            return false;
        final AntiBot antiBot = ModuleRegistry.getModule(AntiBot.class);
        if (antiBot.isToggled() && antiBot.isBot((EntityLivingBase) entity))
            return false;
        if (entity instanceof final EntityPlayer player) {

            final Teams teams = ModuleRegistry.getModule(Teams.class);
            final Friends friends = ModuleRegistry.getModule(Friends.class);

            if (teams.isToggled() && isTeam(getPlayer(), player)) return false;
            if (friends.isToggled() && Koks.getKoks().friendManager.isFriend(getName(player))) return false;
        }
        return true;
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    public double getGravity() {
        return 0.006;
    }

    public double getVelocity() {
        int i = getPlayer().getCurrentEquippedItem().getMaxItemUseDuration() - getPlayer().getItemInUseCount();
        final FastUse fastUse = ModuleRegistry.getModule(FastUse.class);
        final FastBow fastBow = ModuleRegistry.getModule(FastBow.class);
        if (fastUse.isToggled() || fastBow.isToggled())
            i = getPlayer().getCurrentEquippedItem().getMaxItemUseDuration();
        float f = (float) i / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }
}
