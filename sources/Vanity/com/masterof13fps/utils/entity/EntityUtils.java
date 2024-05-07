package com.masterof13fps.utils.entity;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Multimap;

import com.masterof13fps.Methods;
import com.masterof13fps.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class EntityUtils implements Methods, Wrapper {
    private static final Minecraft MINECRAFT = Minecraft.mc();
    private static boolean set = false;
    private static EntityPlayer reference;

    public static double[] interpolate(Entity entity) {
        double partialTicks = (double) Minecraft.mc().timer.renderPartialTicks;
        double[] pos = new double[]{entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks};
        return pos;
    }

    public static boolean isReferenceSet() {
        return !set;
    }

    public static void setReference(EntityPlayer ref) {
        reference = ref;

        set = reference == MINECRAFT.thePlayer;
    }

    public static float[] getEntityRotations(Entity target) {
        double var4 = target.posX - MINECRAFT.thePlayer.posX;
        double var6 = target.posZ - MINECRAFT.thePlayer.posZ;
        double var8 = target.posY + target.getEyeHeight() / 1.3D - (MINECRAFT.thePlayer.posY + MINECRAFT.thePlayer.getEyeHeight());
        double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
        float yaw = (float) (Math.atan2(var6, var4) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(var8, var14) * 180.0D / 3.141592653589793D);
        return new float[]{yaw, pitch};
    }

    public static Vec3 getLook(float yaw, float pitch) {
        return Entity.getVectorForRotation(pitch, yaw);
    }

    public static MovingObjectPosition rayCastedBlock(float yaw, float pitch) {
        float range = Methods.mc.playerController.getBlockReachDistance();

        Vec3 vec31 = getLook(yaw, pitch);

        Vec3 vec3 = Methods.mc.thePlayer.getPositionEyes(1.0F);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);


        MovingObjectPosition ray = Methods.mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, false);

        if (ray != null && ray.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            return ray;
        return null;
    }

    public static float getAngle(float[] original, float[] rotations) {
        float curYaw = normalizeAngle(original[0]);
        rotations[0] = normalizeAngle(rotations[0]);
        float curPitch = normalizeAngle(original[1]);
        rotations[1] = normalizeAngle(rotations[1]);
        float fixedYaw = normalizeAngle(curYaw - rotations[0]);
        float fixedPitch = normalizeAngle(curPitch - rotations[1]);
        return Math.abs(normalizeAngle(fixedYaw) + Math.abs(fixedPitch));
    }

    public static float getAngle(float[] rotations) {
        return getAngle(new float[]{MINECRAFT.thePlayer.rotationYaw, MINECRAFT.thePlayer.rotationPitch}, rotations);
    }

    public static float normalizeAngle(float angle) {
        return MathHelper.wrapAngleTo180_float((angle + 180.0F) % 360.0F - 180.0F);
    }

    public static float getPitchChange(EntityLivingBase entity) {
        double deltaX = entity.posX - MINECRAFT.thePlayer.posX;
        double deltaZ = entity.posZ - MINECRAFT.thePlayer.posZ;
        double deltaY = entity.posY - 2.2D + entity.getEyeHeight() - MINECRAFT.thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(MINECRAFT.thePlayer.rotationPitch - (float) pitchToEntity);
    }

    public static float getYawChange(EntityLivingBase entity) {
        double deltaX = entity.posX - MINECRAFT.thePlayer.posX;
        double deltaZ = entity.posZ - MINECRAFT.thePlayer.posZ;
        double yawToEntity;
        double yawToEntity1 = 0;
        if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
            yawToEntity1 = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
            double yawToEntity11;
            if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
                yawToEntity11 = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
            } else {
                yawToEntity11 = Math.toDegrees(-Math.atan(deltaX / deltaZ));
            }
        }
        return MathHelper.wrapAngleTo180_float(-(MINECRAFT.thePlayer.rotationYaw - (float) yawToEntity1));
    }

    public static int getBestWeapon(EntityLivingBase target) {
        int originalSlot = MINECRAFT.thePlayer.inventory.currentItem;
        int weaponSlot = -1;
        float weaponDamage = 1.0F;
        for (byte slot = 0; slot < 9; slot = (byte) (slot + 1)) {
            MINECRAFT.thePlayer.inventory.currentItem = slot;
            ItemStack itemStack = MINECRAFT.thePlayer.getHeldItem();
            if (itemStack != null) {
                float damage = getItemDamage(itemStack);
                damage += EnchantmentHelper.func_152377_a(MINECRAFT.thePlayer.getHeldItem(), target
                        .getCreatureAttribute());
                if (damage > weaponDamage) {
                    weaponDamage = damage;
                    weaponSlot = slot;
                }
            }
        }
        if (weaponSlot != -1) {
            return weaponSlot;
        }
        return originalSlot;
    }

    private static float getItemDamage(ItemStack itemStack) {
        Multimap multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty()) {
            Iterator iterator = multimap.entries().iterator();
            if (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();

                AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
                double damage;
                double damage1;
                if ((attributeModifier.getOperation() != 1) &&
                        (attributeModifier.getOperation() != 2)) {
                    damage1 = attributeModifier.getAmount();
                } else {
                    damage1 = attributeModifier.getAmount() * 100.0D;
                }
                if (attributeModifier.getAmount() > 1.0D) {
                    return 1.0F + (float) damage1;
                }
                return 1.0F;
            }
        }
        return 1.0F;
    }

    public static void damagePlayer(int damage) {
        if (damage < 1) {
            damage = 1;
        }
        if (damage > MathHelper.floor_double(MINECRAFT.thePlayer.getMaxHealth())) {
            damage = MathHelper.floor_double(MINECRAFT.thePlayer.getMaxHealth());
        }
        double offset = 0.0625D;
        if ((MINECRAFT.thePlayer != null) && (MINECRAFT.getNetHandler() != null) && (MINECRAFT.thePlayer.onGround)) {
            for (int i = 0; i <= (3 + damage) / offset; i++) {
                MINECRAFT.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MINECRAFT.thePlayer.posX, MINECRAFT.thePlayer.posY + offset, MINECRAFT.thePlayer.posZ, false));
                MINECRAFT.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MINECRAFT.thePlayer.posX, MINECRAFT.thePlayer.posY, MINECRAFT.thePlayer.posZ, i == (3 + damage) / offset));
            }
        }
    }

    public static double[] teleportToPosition(double[] startPosition, double[] endPosition, double setOffset, double slack, boolean extendOffset, boolean onGround) {
        boolean wasSneaking = false;
        if (MINECRAFT.thePlayer.isSneaking()) {
            wasSneaking = true;
        }
        double startX = startPosition[0];
        double startY = startPosition[1];
        double startZ = startPosition[2];

        double endX = endPosition[0];
        double endY = endPosition[1];
        double endZ = endPosition[2];

        double distance = Math.abs(startX - startY) + Math.abs(startY - endY) + Math.abs(startZ - endZ);

        int count = 0;
        while (distance > slack) {
            distance = Math.abs(startX - endX) + Math.abs(startY - endY) + Math.abs(startZ - endZ);
            if (count > 120) {
                break;
            }
            double offset = (extendOffset) && ((count & 0x1) == 0) ? setOffset + 0.15D : setOffset;

            double diffX = startX - endX;
            double diffY = startY - endY;
            double diffZ = startZ - endZ;
            if (diffX < 0.0D) {
                if (Math.abs(diffX) > offset) {
                    startX += offset;
                } else {
                    startX += Math.abs(diffX);
                }
            }
            if (diffX > 0.0D) {
                if (Math.abs(diffX) > offset) {
                    startX -= offset;
                } else {
                    startX -= Math.abs(diffX);
                }
            }
            if (diffY < 0.0D) {
                if (Math.abs(diffY) > offset) {
                    startY += offset;
                } else {
                    startY += Math.abs(diffY);
                }
            }
            if (diffY > 0.0D) {
                if (Math.abs(diffY) > offset) {
                    startY -= offset;
                } else {
                    startY -= Math.abs(diffY);
                }
            }
            if (diffZ < 0.0D) {
                if (Math.abs(diffZ) > offset) {
                    startZ += offset;
                } else {
                    startZ += Math.abs(diffZ);
                }
            }
            if (diffZ > 0.0D) {
                if (Math.abs(diffZ) > offset) {
                    startZ -= offset;
                } else {
                    startZ -= Math.abs(diffZ);
                }
            }
            if (wasSneaking) {
                MINECRAFT.getNetHandler().addToSendQueue(new C0BPacketEntityAction(MINECRAFT.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
            MINECRAFT.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(startX, startY, startZ, onGround));
            count++;
        }
        if (wasSneaking) {
            MINECRAFT.getNetHandler().addToSendQueue(new C0BPacketEntityAction(MINECRAFT.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        }
        return new double[]{startX, startY, startZ};
    }
}
