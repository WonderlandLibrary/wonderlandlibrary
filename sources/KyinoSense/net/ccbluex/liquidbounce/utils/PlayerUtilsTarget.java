/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Multimap
 *  javax.vecmath.Vector3f
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockBarrier
 *  net.minecraft.block.BlockGlass
 *  net.minecraft.block.BlockSlab
 *  net.minecraft.block.BlockStainedGlass
 *  net.minecraft.block.BlockStairs
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.attributes.AttributeModifier
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 */
package net.ccbluex.liquidbounce.utils;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.vecmath.Vector3f;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PlayerUtilsTarget {
    private static Minecraft mc = Minecraft.func_71410_x();

    public static float[] getRotations(Entity ent) {
        double x = ent.field_70165_t;
        double z = ent.field_70161_v;
        double y = ent.field_70163_u + (double)(ent.func_70047_e() / 4.0f);
        return PlayerUtilsTarget.getRotationFromPosition(x, z, y);
    }

    public static void damagePlayer(int damage) {
        if (damage < 1) {
            damage = 1;
        }
        if (damage > MathHelper.func_76128_c((double)PlayerUtilsTarget.mc.field_71439_g.func_110138_aP())) {
            damage = MathHelper.func_76128_c((double)PlayerUtilsTarget.mc.field_71439_g.func_110138_aP());
        }
        double offset = 0.0625;
        if (PlayerUtilsTarget.mc.field_71439_g != null && mc.func_147114_u() != null && PlayerUtilsTarget.mc.field_71439_g.field_70122_E) {
            int i = 0;
            while ((double)i <= (double)(3 + damage) / offset) {
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtilsTarget.mc.field_71439_g.field_70165_t, PlayerUtilsTarget.mc.field_71439_g.field_70163_u + offset, PlayerUtilsTarget.mc.field_71439_g.field_70161_v, false));
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtilsTarget.mc.field_71439_g.field_70165_t, PlayerUtilsTarget.mc.field_71439_g.field_70163_u, PlayerUtilsTarget.mc.field_71439_g.field_70161_v, (double)i == (double)(3 + damage) / offset));
                ++i;
            }
        }
    }

    public static boolean isOnGround(double height) {
        return !PlayerUtilsTarget.mc.field_71441_e.func_72945_a((Entity)PlayerUtilsTarget.mc.field_71439_g, PlayerUtilsTarget.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, -height, 0.0)).isEmpty();
    }

    public static boolean MovementInput() {
        return PlayerUtilsTarget.mc.field_71474_y.field_74351_w.field_74513_e || PlayerUtilsTarget.mc.field_71474_y.field_74370_x.field_74513_e || PlayerUtilsTarget.mc.field_71474_y.field_74366_z.field_74513_e || PlayerUtilsTarget.mc.field_71474_y.field_74368_y.field_74513_e;
    }

    private static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - PlayerUtilsTarget.mc.field_71439_g.field_70165_t;
        double zDiff = z - PlayerUtilsTarget.mc.field_71439_g.field_70161_v;
        double yDiff = y - PlayerUtilsTarget.mc.field_71439_g.field_70163_u - 0.6;
        double dist = MathHelper.func_76133_a((double)(xDiff * xDiff + zDiff * zDiff));
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static boolean isMoving() {
        if (!PlayerUtilsTarget.mc.field_71439_g.field_70123_F && !PlayerUtilsTarget.mc.field_71439_g.func_70093_af()) {
            return PlayerUtilsTarget.mc.field_71439_g.field_71158_b.field_78900_b != 0.0f || PlayerUtilsTarget.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f;
        }
        return false;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (PlayerUtilsTarget.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
            int amplifier = PlayerUtilsTarget.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static float getMaxFallDist() {
        PotionEffect potioneffect = PlayerUtilsTarget.mc.field_71439_g.func_70660_b(Potion.field_76430_j);
        int f2 = potioneffect != null ? potioneffect.func_76458_c() + 1 : 0;
        return PlayerUtilsTarget.mc.field_71439_g.func_82143_as() + f2;
    }

    public static float getDirection() {
        float yaw = PlayerUtilsTarget.mc.field_71439_g.field_70759_as;
        float forward = PlayerUtilsTarget.mc.field_71439_g.field_70701_bs;
        float strafe = PlayerUtilsTarget.mc.field_71439_g.field_70702_br;
        yaw += (float)(forward < 0.0f ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += (float)(forward < 0.0f ? -45 : (forward == 0.0f ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= (float)(forward < 0.0f ? -45 : (forward == 0.0f ? 90 : 45));
        }
        return yaw * ((float)Math.PI / 180);
    }

    public static boolean isInWater() {
        return PlayerUtilsTarget.mc.field_71441_e.func_180495_p(new BlockPos(PlayerUtilsTarget.mc.field_71439_g.field_70165_t, PlayerUtilsTarget.mc.field_71439_g.field_70163_u, PlayerUtilsTarget.mc.field_71439_g.field_70161_v)).func_177230_c().func_149688_o() == Material.field_151586_h;
    }

    public static boolean isMoving2() {
        return PlayerUtilsTarget.mc.field_71439_g.field_70701_bs != 0.0f || PlayerUtilsTarget.mc.field_71439_g.field_70702_br != 0.0f;
    }

    public static Block getBlock(BlockPos pos) {
        return PlayerUtilsTarget.mc.field_71441_e.func_180495_p(pos).func_177230_c();
    }

    public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
        return PlayerUtilsTarget.getBlock(new BlockPos(inPlayer.field_70165_t - x, inPlayer.field_70163_u - y, inPlayer.field_70161_v - z));
    }

    public static ArrayList<Vector3f> vanillaTeleportPositions(double tpX, double tpY, double tpZ, double speed) {
        double d;
        ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
        double posX = tpX - PlayerUtilsTarget.mc.field_71439_g.field_70165_t;
        double posY = tpY - (PlayerUtilsTarget.mc.field_71439_g.field_70163_u + (double)PlayerUtilsTarget.mc.field_71439_g.func_70047_e() + 1.1);
        double posZ = tpZ - PlayerUtilsTarget.mc.field_71439_g.field_70161_v;
        float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / Math.PI - 90.0);
        float pitch = (float)(-Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ)) * 180.0 / Math.PI);
        double tmpX = PlayerUtilsTarget.mc.field_71439_g.field_70165_t;
        double tmpY = PlayerUtilsTarget.mc.field_71439_g.field_70163_u;
        double tmpZ = PlayerUtilsTarget.mc.field_71439_g.field_70161_v;
        double steps = 1.0;
        for (d = speed; d < PlayerUtilsTarget.getDistance(PlayerUtilsTarget.mc.field_71439_g.field_70165_t, PlayerUtilsTarget.mc.field_71439_g.field_70163_u, PlayerUtilsTarget.mc.field_71439_g.field_70161_v, tpX, tpY, tpZ); d += speed) {
            steps += 1.0;
        }
        for (d = speed; d < PlayerUtilsTarget.getDistance(PlayerUtilsTarget.mc.field_71439_g.field_70165_t, PlayerUtilsTarget.mc.field_71439_g.field_70163_u, PlayerUtilsTarget.mc.field_71439_g.field_70161_v, tpX, tpY, tpZ); d += speed) {
            tmpX = PlayerUtilsTarget.mc.field_71439_g.field_70165_t - Math.sin(PlayerUtilsTarget.getDirection(yaw)) * d;
            tmpZ = PlayerUtilsTarget.mc.field_71439_g.field_70161_v + Math.cos(PlayerUtilsTarget.getDirection(yaw)) * d;
            positions.add(new Vector3f((float)tmpX, (float)(tmpY -= (PlayerUtilsTarget.mc.field_71439_g.field_70163_u - tpY) / steps), (float)tmpZ));
        }
        positions.add(new Vector3f((float)tpX, (float)tpY, (float)tpZ));
        return positions;
    }

    public static float getDirection(float yaw) {
        if (PlayerUtilsTarget.mc.field_71439_g.field_70701_bs < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (PlayerUtilsTarget.mc.field_71439_g.field_70701_bs < 0.0f) {
            forward = -0.5f;
        } else if (PlayerUtilsTarget.mc.field_71439_g.field_70701_bs > 0.0f) {
            forward = 0.5f;
        }
        if (PlayerUtilsTarget.mc.field_71439_g.field_70702_br > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (PlayerUtilsTarget.mc.field_71439_g.field_70702_br < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= (float)Math.PI / 180;
    }

    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d2 = y1 - y2;
        double d3 = z1 - z2;
        return MathHelper.func_76133_a((double)(d0 * d0 + d2 * d2 + d3 * d3));
    }

    public static void blockHit(Entity en, boolean value) {
        ItemStack stack = PlayerUtilsTarget.mc.field_71439_g.func_71045_bC();
        if (PlayerUtilsTarget.mc.field_71439_g.func_71045_bC() != null && en != null && value && stack.func_77973_b() instanceof ItemSword && (double)PlayerUtilsTarget.mc.field_71439_g.field_70733_aJ > 0.2) {
            PlayerUtilsTarget.mc.field_71439_g.func_71045_bC().func_77957_a((World)PlayerUtilsTarget.mc.field_71441_e, (EntityPlayer)PlayerUtilsTarget.mc.field_71439_g);
        }
    }

    public static float getItemAtkDamage(ItemStack itemStack) {
        Iterator iterator2;
        Multimap multimap = itemStack.func_111283_C();
        if (!multimap.isEmpty() && (iterator2 = multimap.entries().iterator()).hasNext()) {
            Map.Entry entry = (Map.Entry)iterator2.next();
            AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
            double damage = attributeModifier.func_111169_c() != 1 && attributeModifier.func_111169_c() != 2 ? attributeModifier.func_111164_d() : attributeModifier.func_111164_d() * 100.0;
            double d = damage;
            if (attributeModifier.func_111164_d() > 1.0) {
                return 1.0f + (float)damage;
            }
            return 1.0f;
        }
        return 1.0f;
    }

    public static int bestWeapon(Entity target) {
        PlayerUtilsTarget.mc.field_71439_g.field_71071_by.field_70461_c = 0;
        int firstSlot = 0;
        int bestWeapon = -1;
        int j = 1;
        for (int i = 0; i < 9; i = (int)((byte)(i + 1))) {
            int itemAtkDamage;
            PlayerUtilsTarget.mc.field_71439_g.field_71071_by.field_70461_c = i;
            ItemStack itemStack = PlayerUtilsTarget.mc.field_71439_g.func_70694_bm();
            if (itemStack == null) continue;
            j = itemAtkDamage = (int)PlayerUtilsTarget.getItemAtkDamage(itemStack);
            bestWeapon = i;
        }
        if (bestWeapon != -1) {
            return bestWeapon;
        }
        return firstSlot;
    }

    public static void shiftClick(Item i) {
        for (int i1 = 9; i1 < 37; ++i1) {
            ItemStack itemstack = PlayerUtilsTarget.mc.field_71439_g.field_71069_bz.func_75139_a(i1).func_75211_c();
            if (itemstack == null || itemstack.func_77973_b() != i) continue;
            PlayerUtilsTarget.mc.field_71442_b.func_78753_a(0, i1, 0, 1, (EntityPlayer)PlayerUtilsTarget.mc.field_71439_g);
            break;
        }
    }

    public static boolean hotbarIsFull() {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemstack = PlayerUtilsTarget.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (itemstack != null) continue;
            return false;
        }
        return true;
    }

    public static Vec3 getLook(float p_174806_1_, float p_174806_2_) {
        float var3 = MathHelper.func_76134_b((float)(-p_174806_2_ * ((float)Math.PI / 180) - (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(-p_174806_2_ * ((float)Math.PI / 180) - (float)Math.PI));
        float var5 = -MathHelper.func_76134_b((float)(-p_174806_1_ * ((float)Math.PI / 180)));
        float var6 = MathHelper.func_76126_a((float)(-p_174806_1_ * ((float)Math.PI / 180)));
        return new Vec3((double)(var4 * var5), (double)var6, (double)(var3 * var5));
    }

    public static void tellPlayer(String string) {
        PlayerUtilsTarget.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(string));
    }

    public EntityLivingBase getEntity() {
        return null;
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0 / inc;
        return (double)Math.round(val * one) / one;
    }

    public static double getDistanceToFall() {
        double distance = 0.0;
        for (double i2 = PlayerUtilsTarget.mc.field_71439_g.field_70163_u; i2 > 0.0 && !(i2 < 0.0); i2 -= 0.1) {
            Block block = BlockUtils.getBlock(new BlockPos(PlayerUtilsTarget.mc.field_71439_g.field_70165_t, i2, PlayerUtilsTarget.mc.field_71439_g.field_70161_v));
            if (block.func_149688_o() == Material.field_151579_a || !block.func_149703_v() || !block.func_149730_j() && !(block instanceof BlockSlab) && !(block instanceof BlockBarrier) && !(block instanceof BlockStairs) && !(block instanceof BlockGlass) && !(block instanceof BlockStainedGlass)) continue;
            if (block instanceof BlockSlab) {
                i2 -= 0.5;
            }
            distance = i2;
            break;
        }
        return PlayerUtilsTarget.mc.field_71439_g.field_70163_u - distance;
    }
}

