/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import me.AveReborn.Value;
import me.AveReborn.mod.mods.COMBAT.Killaura;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;

public class PlayerUtil {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static float[] getRotations(Entity ent) {
        double x2 = ent.posX;
        double z2 = ent.posZ;
        double y2 = ent.posY + (double)(ent.getEyeHeight() / 4.0f);
        return PlayerUtil.getRotationFromPosition(x2, z2, y2);
    }

    private static float[] getRotationFromPosition(double x2, double z2, double y2) {
        Minecraft.getMinecraft();
        double xDiff = x2 - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double zDiff = z2 - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        double yDiff = y2 - Minecraft.thePlayer.posY - 0.6;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            Minecraft.getMinecraft();
            int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static float getDirection() {
        Minecraft.getMinecraft();
        float yaw = Minecraft.thePlayer.rotationYawHead;
        Minecraft.getMinecraft();
        float forward = Minecraft.thePlayer.moveForward;
        Minecraft.getMinecraft();
        float strafe = Minecraft.thePlayer.moveStrafing;
        yaw += (float)(forward < 0.0f ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += (float)(forward < 0.0f ? -45 : (forward == 0.0f ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= (float)(forward < 0.0f ? -45 : (forward == 0.0f ? 90 : 45));
        }
        return yaw * 0.017453292f;
    }

    public static boolean isInWater() {
        if (PlayerUtil.mc.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ)).getBlock().getMaterial() == Material.water) {
            return true;
        }
        return false;
    }

    public static void toFwd(double speed) {
        float yaw = Minecraft.thePlayer.rotationYaw * 0.017453292f;
        EntityPlayerSP thePlayer = Minecraft.thePlayer;
        thePlayer.motionX -= (double)MathHelper.sin(yaw) * speed;
        EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
        thePlayer2.motionZ += (double)MathHelper.cos(yaw) * speed;
    }

    public static void setSpeed(double speed) {
        Minecraft.thePlayer.motionX = (- Math.sin(PlayerUtil.getDirection())) * speed;
        Minecraft.thePlayer.motionZ = Math.cos(PlayerUtil.getDirection()) * speed;
    }

    public static double getSpeed() {
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        return Math.sqrt(Minecraft.thePlayer.motionX * Minecraft.thePlayer.motionX + Minecraft.thePlayer.motionZ * Minecraft.thePlayer.motionZ);
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer) {
        return PlayerUtil.getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - 1.0, inPlayer.posZ));
    }

    public static Block getBlock(BlockPos pos) {
        return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
    }

    public static Block getBlockAtPosC(EntityPlayer inPlayer, double x2, double y2, double z2) {
        return PlayerUtil.getBlock(new BlockPos(inPlayer.posX - x2, inPlayer.posY - y2, inPlayer.posZ - z2));
    }

    public static ArrayList<Vector3f> vanillaTeleportPositions(double tpX, double tpY, double tpZ, double speed) {
        ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
        Minecraft mc2 = Minecraft.getMinecraft();
        double posX = tpX - Minecraft.thePlayer.posX;
        double posY = tpY - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight() + 1.1);
        double posZ = tpZ - Minecraft.thePlayer.posZ;
        float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / 3.141592653589793 - 90.0);
        float pitch = (float)((- Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ))) * 180.0 / 3.141592653589793);
        double tmpX = Minecraft.thePlayer.posX;
        double tmpY = Minecraft.thePlayer.posY;
        double tmpZ = Minecraft.thePlayer.posZ;
        double steps = 1.0;
        double d2 = speed;
        while (d2 < PlayerUtil.getDistance(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, tpX, tpY, tpZ)) {
            steps += 1.0;
            d2 += speed;
        }
        d2 = speed;
        while (d2 < PlayerUtil.getDistance(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, tpX, tpY, tpZ)) {
            tmpX = Minecraft.thePlayer.posX - Math.sin(PlayerUtil.getDirection(yaw)) * d2;
            tmpZ = Minecraft.thePlayer.posZ + Math.cos(PlayerUtil.getDirection(yaw)) * d2;
            positions.add(new Vector3f((float)tmpX, (float)(tmpY -= (Minecraft.thePlayer.posY - tpY) / steps), (float)tmpZ));
            d2 += speed;
        }
        positions.add(new Vector3f((float)tpX, (float)tpY, (float)tpZ));
        return positions;
    }

    public static float getDirection(float yaw) {
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else {
            Minecraft.getMinecraft();
            if (Minecraft.thePlayer.moveForward > 0.0f) {
                forward = 0.5f;
            }
        }
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= 0.017453292f;
    }

    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d2 = y1 - y2;
        double d3 = z1 - z2;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
    }

    public static boolean MovementInput() {
        if (!(PlayerUtil.mc.gameSettings.keyBindForward.isKeyDown() || PlayerUtil.mc.gameSettings.keyBindLeft.isKeyDown() || PlayerUtil.mc.gameSettings.keyBindRight.isKeyDown() || PlayerUtil.mc.gameSettings.keyBindBack.isKeyDown())) {
            return false;
        }
        return true;
    }

    public static void blockHit(Entity en2, boolean value) {
        Minecraft mc2 = Minecraft.getMinecraft();
        ItemStack stack = Minecraft.thePlayer.getCurrentEquippedItem();
        if (Minecraft.thePlayer.getCurrentEquippedItem() != null && en2 != null && value && stack.getItem() instanceof ItemSword && (double)Minecraft.thePlayer.swingProgress > 0.2) {
            Minecraft.thePlayer.getCurrentEquippedItem().useItemRightClick(mc2.theWorld, Minecraft.thePlayer);
        }
    }

    public static float getItemAtkDamage(ItemStack itemStack) {
        Iterator<Map.Entry<String, AttributeModifier>> iterator;
        Multimap<String, AttributeModifier> multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty() && (iterator = multimap.entries().iterator()).hasNext()) {
            Map.Entry<String, AttributeModifier> entry = iterator.next();
            AttributeModifier attributeModifier = entry.getValue();
            double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2 ? attributeModifier.getAmount() : attributeModifier.getAmount() * 100.0;
            double d2 = damage;
            if (attributeModifier.getAmount() > 1.0) {
                return 1.0f + (float)damage;
            }
            return 1.0f;
        }
        return 1.0f;
    }

    public static int bestWeapon(Entity target) {
        Minecraft mc2 = Minecraft.getMinecraft();
        Minecraft.thePlayer.inventory.currentItem = 0;
        int firstSlot = 0;
        int bestWeapon = -1;
        int j2 = 1;
        int i2 = 0;
        while (i2 < 9) {
            Minecraft.thePlayer.inventory.currentItem = i2;
            ItemStack itemStack = Minecraft.thePlayer.getHeldItem();
            if (itemStack != null) {
                int itemAtkDamage = (int)PlayerUtil.getItemAtkDamage(itemStack);
                if ((itemAtkDamage = (int)((float)itemAtkDamage + EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED))) > j2) {
                    j2 = itemAtkDamage;
                    bestWeapon = i2;
                }
            }
            i2 = (byte)(i2 + 1);
        }
        if (bestWeapon != -1) {
            return bestWeapon;
        }
        return firstSlot;
    }

    public static void shiftClick(Item i2) {
        int i1 = 9;
        while (i1 < 37) {
            ItemStack itemstack = Minecraft.thePlayer.inventoryContainer.getSlot(i1).getStack();
            if (itemstack != null && itemstack.getItem() == i2) {
                PlayerUtil.mc.playerController.windowClick(0, i1, 0, 1, Minecraft.thePlayer);
                break;
            }
            ++i1;
        }
    }

    public static boolean hotbarIsFull() {
        int i2 = 0;
        while (i2 <= 36) {
            ItemStack itemstack = Minecraft.thePlayer.inventory.getStackInSlot(i2);
            if (itemstack == null) {
                return false;
            }
            ++i2;
        }
        return true;
    }

    public static Entity raycast(Entity entiy) {
        EntityPlayerSP var2 = Minecraft.thePlayer;
        Vec3 var9 = entiy.getPositionVector().add(new Vec3(0.0, entiy.getEyeHeight(), 0.0));
        Vec3 var7 = Minecraft.thePlayer.getPositionVector().add(new Vec3(0.0, Minecraft.thePlayer.getEyeHeight(), 0.0));
        Vec3 var10 = null;
        float var11 = 1.0f;
        AxisAlignedBB a2 = Minecraft.thePlayer.getEntityBoundingBox().addCoord(var9.xCoord - var7.xCoord, var9.yCoord - var7.yCoord, var9.zCoord - var7.zCoord).expand(var11, var11, var11);
        List<Entity> var12 = PlayerUtil.mc.theWorld.getEntitiesWithinAABBExcludingEntity(var2, a2);
        double var13 = Killaura.range.getValueState() + 0.5;
        Entity b2 = null;
        int var15 = 0;
        while (var15 < var12.size()) {
            Entity var16 = var12.get(var15);
            if (var16.canBeCollidedWith()) {
                double var20;
                float var17 = var16.getCollisionBorderSize();
                AxisAlignedBB var18 = var16.getEntityBoundingBox().expand(var17, var17, var17);
                MovingObjectPosition var19 = var18.calculateIntercept(var7, var9);
                if (var18.isVecInside(var7)) {
                    if (0.0 < var13 || var13 == 0.0) {
                        b2 = var16;
                        var10 = var19 == null ? var7 : var19.hitVec;
                        var13 = 0.0;
                    }
                } else if (var19 != null && ((var20 = var7.distanceTo(var19.hitVec)) < var13 || var13 == 0.0)) {
                    b2 = var16;
                    var10 = var19.hitVec;
                    var13 = var20;
                }
            }
            ++var15;
        }
        return b2;
    }

    public static Vec3 getLook(float p_174806_1_, float p_174806_2_) {
        float var3 = MathHelper.cos((- p_174806_2_) * 0.017453292f - 3.1415927f);
        float var4 = MathHelper.sin((- p_174806_2_) * 0.017453292f - 3.1415927f);
        float var5 = - MathHelper.cos((- p_174806_1_) * 0.017453292f);
        float var6 = MathHelper.sin((- p_174806_1_) * 0.017453292f);
        return new Vec3(var4 * var5, var6, var3 * var5);
    }
}

