// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils;

import net.lenni0451.eventapi.events.IEvent;
import net.augustus.events.EventAttackSlowdown;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import net.augustus.Augustus;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.potion.Potion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.utils.interfaces.MM;
import net.augustus.utils.interfaces.MC;

public class PlayerUtil implements MC, MM
{
    public static void verusdmg() {
        PlayerUtil.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtil.mc.thePlayer.posX, PlayerUtil.mc.thePlayer.posY + 3.001, PlayerUtil.mc.thePlayer.posZ, false));
        PlayerUtil.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtil.mc.thePlayer.posX, PlayerUtil.mc.thePlayer.posY, PlayerUtil.mc.thePlayer.posZ, false));
        PlayerUtil.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtil.mc.thePlayer.posX, PlayerUtil.mc.thePlayer.posY, PlayerUtil.mc.thePlayer.posZ, true));
    }
    
    public static void cubedmg() {
        for (int i = 0; i < 51; ++i) {
            PlayerUtil.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtil.mc.thePlayer.posX, PlayerUtil.mc.thePlayer.posY + 0.06, PlayerUtil.mc.thePlayer.posZ, false));
            PlayerUtil.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtil.mc.thePlayer.posX, PlayerUtil.mc.thePlayer.posY, PlayerUtil.mc.thePlayer.posZ, false));
        }
        PlayerUtil.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtil.mc.thePlayer.posX, PlayerUtil.mc.thePlayer.posY, PlayerUtil.mc.thePlayer.posZ, false));
        PlayerUtil.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
    }
    
    public static double[] predictPosition(final EntityPlayer entity, final int predictTicks) {
        final double diffX = entity.prevPosX - entity.posX;
        final double diffZ = entity.prevPosZ - entity.posZ;
        double posX = entity.posX;
        double posZ = entity.posZ;
        for (int i = 0; i <= predictTicks; ++i) {
            posX -= diffX * i;
            posZ -= diffZ * i;
        }
        return new double[] { posX, posZ };
    }
    
    public static double[] getPredictedPos(float forward, float strafe, double motionX, double motionY, double motionZ, double posX, double posY, double posZ, final boolean isJumping) {
        strafe *= 0.98f;
        forward *= 0.98f;
        float f4 = 0.91f;
        final boolean isSprinting = PlayerUtil.mc.thePlayer.isSprinting();
        if (isJumping && PlayerUtil.mc.thePlayer.onGround && PlayerUtil.mc.thePlayer.getJumpTicks() == 0) {
            motionY = PlayerUtil.mc.thePlayer.getJumpUpwardsMotion();
            if (PlayerUtil.mc.thePlayer.isPotionActive(Potion.jump)) {
                motionY += (PlayerUtil.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
            }
            if (isSprinting) {
                final float f5 = PlayerUtil.mc.thePlayer.rotationYaw * 0.017453292f;
                motionX -= MathHelper.sin(f5) * 0.2f;
                motionZ += MathHelper.cos(f5) * 0.2f;
            }
        }
        if (PlayerUtil.mc.thePlayer.onGround) {
            f4 = PlayerUtil.mc.thePlayer.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(posY) - 1, MathHelper.floor_double(posZ))).getBlock().slipperiness * 0.91f;
        }
        final float f6 = 0.16277136f / (f4 * f4 * f4);
        float friction;
        if (PlayerUtil.mc.thePlayer.onGround) {
            friction = PlayerUtil.mc.thePlayer.getAIMoveSpeed() * f6;
            if (PlayerUtil.mc.thePlayer == Minecraft.getMinecraft().thePlayer && Augustus.getInstance().getModuleManager().sprint.isToggled() && Augustus.getInstance().getModuleManager().sprint.allDirection.getBoolean() && PlayerUtil.mm.sprint.allSprint) {
                friction = 0.12999998f;
            }
        }
        else {
            friction = PlayerUtil.mc.thePlayer.jumpMovementFactor;
        }
        float f7 = strafe * strafe + forward * forward;
        if (f7 >= 1.0E-4f) {
            f7 = MathHelper.sqrt_float(f7);
            if (f7 < 1.0f) {
                f7 = 1.0f;
            }
            f7 = friction / f7;
            strafe *= f7;
            forward *= f7;
            final float f8 = MathHelper.sin(PlayerUtil.mc.thePlayer.rotationYaw * 3.1415927f / 180.0f);
            final float f9 = MathHelper.cos(PlayerUtil.mc.thePlayer.rotationYaw * 3.1415927f / 180.0f);
            motionX += strafe * f9 - forward * f8;
            motionZ += forward * f9 + strafe * f8;
        }
        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        f4 = 0.91f;
        if (PlayerUtil.mc.thePlayer.onGround) {
            f4 = PlayerUtil.mc.thePlayer.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(PlayerUtil.mc.thePlayer.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(posZ))).getBlock().slipperiness * 0.91f;
        }
        if (PlayerUtil.mc.thePlayer.worldObj.isRemote && (!PlayerUtil.mc.thePlayer.worldObj.isBlockLoaded(new BlockPos((int)posX, 0, (int)posZ)) || !PlayerUtil.mc.thePlayer.worldObj.getChunkFromBlockCoords(new BlockPos((int)posX, 0, (int)posZ)).isLoaded())) {
            if (posY > 0.0) {
                motionY = -0.1;
            }
            else {
                motionY = 0.0;
            }
        }
        else {
            motionY -= 0.08;
        }
        motionY *= 0.9800000190734863;
        motionX *= f4;
        motionZ *= f4;
        return new double[] { posX, posY, posZ, motionX, motionY, motionZ };
    }
    
    public static Vec3 getPredictedPos(final boolean isHitting, final Entity targetEntity, float forward, float strafe) {
        strafe *= 0.98f;
        forward *= 0.98f;
        float f4 = 0.91f;
        double motionX = PlayerUtil.mc.thePlayer.motionX;
        double motionZ = PlayerUtil.mc.thePlayer.motionZ;
        double motionY = PlayerUtil.mc.thePlayer.motionY;
        boolean isSprinting = PlayerUtil.mc.thePlayer.isSprinting();
        if (isHitting) {
            final float f5 = (float)PlayerUtil.mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
            float f6 = 0.0f;
            if (targetEntity instanceof EntityLivingBase) {
                f6 = EnchantmentHelper.func_152377_a(PlayerUtil.mc.thePlayer.getHeldItem(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
            }
            else {
                f6 = EnchantmentHelper.func_152377_a(PlayerUtil.mc.thePlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
            }
            if (f5 > 0.0f || f6 > 0.0f) {
                int i = EnchantmentHelper.getKnockbackModifier(PlayerUtil.mc.thePlayer);
                if (PlayerUtil.mc.thePlayer.isSprinting()) {
                    ++i;
                }
                final boolean flag2 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(PlayerUtil.mc.thePlayer), f5);
                if (flag2) {
                    if (i > 0) {
                        final EventAttackSlowdown eventAttackSlowdown = new EventAttackSlowdown(false, 0.6);
                        EventHandler.call(eventAttackSlowdown);
                        motionX *= eventAttackSlowdown.getSlowDown();
                        motionZ *= eventAttackSlowdown.getSlowDown();
                        isSprinting = eventAttackSlowdown.isSprint();
                    }
                    else if (Augustus.getInstance().getModuleManager().velocity.isToggled() && Augustus.getInstance().getModuleManager().velocity.mode.getSelected().equals("Intave") && Minecraft.getMinecraft().thePlayer.hurtTime != 0) {
                        motionX *= Augustus.getInstance().getModuleManager().velocity.XZValueIntave.getValue();
                        motionZ *= Augustus.getInstance().getModuleManager().velocity.XZValueIntave.getValue();
                        isSprinting = false;
                    }
                }
            }
        }
        if (PlayerUtil.mc.thePlayer.isJumping && PlayerUtil.mc.thePlayer.onGround && PlayerUtil.mc.thePlayer.getJumpTicks() == 0) {
            motionY = PlayerUtil.mc.thePlayer.getJumpUpwardsMotion();
            if (PlayerUtil.mc.thePlayer.isPotionActive(Potion.jump)) {
                motionY += (PlayerUtil.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
            }
            if (isSprinting) {
                final float f5 = PlayerUtil.mc.thePlayer.rotationYaw * 0.017453292f;
                motionX -= MathHelper.sin(f5) * 0.2f;
                motionZ += MathHelper.cos(f5) * 0.2f;
            }
        }
        if (PlayerUtil.mc.thePlayer.onGround) {
            f4 = PlayerUtil.mc.thePlayer.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(PlayerUtil.mc.thePlayer.posX), MathHelper.floor_double(PlayerUtil.mc.thePlayer.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(PlayerUtil.mc.thePlayer.posZ))).getBlock().slipperiness * 0.91f;
        }
        final float f7 = 0.16277136f / (f4 * f4 * f4);
        float friction;
        if (PlayerUtil.mc.thePlayer.onGround) {
            friction = PlayerUtil.mc.thePlayer.getAIMoveSpeed() * f7;
            if (PlayerUtil.mc.thePlayer == Minecraft.getMinecraft().thePlayer && Augustus.getInstance().getModuleManager().sprint.isToggled() && Augustus.getInstance().getModuleManager().sprint.allDirection.getBoolean() && PlayerUtil.mm.sprint.allSprint) {
                friction = 0.12999998f;
            }
        }
        else {
            friction = PlayerUtil.mc.thePlayer.jumpMovementFactor;
        }
        float f8 = strafe * strafe + forward * forward;
        if (f8 >= 1.0E-4f) {
            f8 = MathHelper.sqrt_float(f8);
            if (f8 < 1.0f) {
                f8 = 1.0f;
            }
            f8 = friction / f8;
            strafe *= f8;
            forward *= f8;
            final float f9 = MathHelper.sin(PlayerUtil.mc.thePlayer.rotationYaw * 3.1415927f / 180.0f);
            final float f10 = MathHelper.cos(PlayerUtil.mc.thePlayer.rotationYaw * 3.1415927f / 180.0f);
            motionX += strafe * f10 - forward * f9;
            motionZ += forward * f10 + strafe * f9;
        }
        f4 = 0.91f;
        if (PlayerUtil.mc.thePlayer.onGround) {
            f4 = PlayerUtil.mc.thePlayer.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(PlayerUtil.mc.thePlayer.posX), MathHelper.floor_double(PlayerUtil.mc.thePlayer.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(PlayerUtil.mc.thePlayer.posZ))).getBlock().slipperiness * 0.91f;
        }
        motionY *= 0.9800000190734863;
        motionX *= f4;
        motionZ *= f4;
        return new Vec3(motionX, motionY, motionZ);
    }
}
