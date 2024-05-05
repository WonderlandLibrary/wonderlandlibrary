/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.MathHelper
 *  org.lwjgl.util.vector.Vector3f
 */
package net.ccbluex.liquidbounce.utils.render;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Vector3f;

public class PlayerUtil {
    private static Minecraft mc = Minecraft.func_71410_x();
    public boolean shouldslow = false;
    public boolean collided = false;

    public static float getDirection() {
        float yaw = PlayerUtil.mc.field_71439_g.field_70177_z;
        if (PlayerUtil.mc.field_71439_g.field_70701_bs < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (PlayerUtil.mc.field_71439_g.field_70701_bs < 0.0f) {
            forward = -0.5f;
        } else if (PlayerUtil.mc.field_71439_g.field_70701_bs > 0.0f) {
            forward = 0.5f;
        }
        if (PlayerUtil.mc.field_71439_g.field_70702_br > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (PlayerUtil.mc.field_71439_g.field_70702_br < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= (float)Math.PI / 180;
    }

    public static boolean isInWater() {
        return PlayerUtil.mc.field_71441_e.func_180495_p(new BlockPos(PlayerUtil.mc.field_71439_g.field_70165_t, PlayerUtil.mc.field_71439_g.field_70163_u, PlayerUtil.mc.field_71439_g.field_70161_v)).func_177230_c().func_149688_o() == Material.field_151586_h;
    }

    public static boolean isOnWater() {
        double y = PlayerUtil.mc.field_71439_g.field_70163_u - 0.03;
        for (int x = MathHelper.func_76128_c((double)PlayerUtil.mc.field_71439_g.field_70165_t); x < MathHelper.func_76143_f((double)PlayerUtil.mc.field_71439_g.field_70165_t); ++x) {
            for (int z = MathHelper.func_76128_c((double)PlayerUtil.mc.field_71439_g.field_70161_v); z < MathHelper.func_76143_f((double)PlayerUtil.mc.field_71439_g.field_70161_v); ++z) {
                BlockPos pos = new BlockPos(x, MathHelper.func_76128_c((double)y), z);
                if (!(PlayerUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid) || PlayerUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c().func_149688_o() != Material.field_151586_h) continue;
                return true;
            }
        }
        return false;
    }

    public static void toFwd(double speed) {
        float yaw = PlayerUtil.mc.field_71439_g.field_70177_z * ((float)Math.PI / 180);
        PlayerUtil.mc.field_71439_g.field_70159_w -= (double)MathHelper.func_76126_a((float)yaw) * speed;
        PlayerUtil.mc.field_71439_g.field_70179_y += (double)MathHelper.func_76134_b((float)yaw) * speed;
    }

    public static void setSpeed(double speed) {
        PlayerUtil.mc.field_71439_g.field_70159_w = -Math.sin(PlayerUtil.getDirection()) * speed;
        PlayerUtil.mc.field_71439_g.field_70179_y = Math.cos(PlayerUtil.getDirection()) * speed;
    }

    public static void tellPlayer(String string) {
        PlayerUtil.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(string));
    }

    public static double getSpeed() {
        return Math.sqrt(PlayerUtil.mc.field_71439_g.field_70159_w * PlayerUtil.mc.field_71439_g.field_70159_w + PlayerUtil.mc.field_71439_g.field_70179_y * PlayerUtil.mc.field_71439_g.field_70179_y);
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer) {
        return PlayerUtil.getBlock(new BlockPos(inPlayer.field_70165_t, inPlayer.field_70163_u - 1.0, inPlayer.field_70161_v));
    }

    public static Block getBlock(BlockPos pos) {
        Minecraft.func_71410_x();
        return PlayerUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
    }

    public static Block getBlockAtPosC(EntityPlayer inPlayer, double x2, double y2, double z2) {
        return PlayerUtil.getBlock(new BlockPos(inPlayer.field_70165_t - x2, inPlayer.field_70163_u - y2, inPlayer.field_70161_v - z2));
    }

    public static ArrayList<Vector3f> vanillaTeleportPositions(double tpX, double tpY, double tpZ, double speed) {
        double d2;
        ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
        Minecraft mc2 = Minecraft.func_71410_x();
        double posX = tpX - PlayerUtil.mc.field_71439_g.field_70165_t;
        double posY = tpY - (PlayerUtil.mc.field_71439_g.field_70163_u + (double)PlayerUtil.mc.field_71439_g.func_70047_e() + 1.1);
        double posZ = tpZ - PlayerUtil.mc.field_71439_g.field_70161_v;
        float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / Math.PI - 90.0);
        float pitch = (float)(-Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ)) * 180.0 / Math.PI);
        double tmpX = PlayerUtil.mc.field_71439_g.field_70165_t;
        double tmpY = PlayerUtil.mc.field_71439_g.field_70163_u;
        double tmpZ = PlayerUtil.mc.field_71439_g.field_70161_v;
        double steps = 1.0;
        for (d2 = speed; d2 < PlayerUtil.getDistance(PlayerUtil.mc.field_71439_g.field_70165_t, PlayerUtil.mc.field_71439_g.field_70163_u, PlayerUtil.mc.field_71439_g.field_70161_v, tpX, tpY, tpZ); d2 += speed) {
            steps += 1.0;
        }
        for (d2 = speed; d2 < PlayerUtil.getDistance(PlayerUtil.mc.field_71439_g.field_70165_t, PlayerUtil.mc.field_71439_g.field_70163_u, PlayerUtil.mc.field_71439_g.field_70161_v, tpX, tpY, tpZ); d2 += speed) {
            tmpX = PlayerUtil.mc.field_71439_g.field_70165_t - Math.sin(PlayerUtil.getDirection(yaw)) * d2;
            tmpZ = PlayerUtil.mc.field_71439_g.field_70161_v + Math.cos(PlayerUtil.getDirection(yaw)) * d2;
            positions.add(new Vector3f((float)tmpX, (float)(tmpY -= (PlayerUtil.mc.field_71439_g.field_70163_u - tpY) / steps), (float)tmpZ));
        }
        positions.add(new Vector3f((float)tpX, (float)tpY, (float)tpZ));
        return positions;
    }

    public static float getDirection(float yaw) {
        Minecraft.func_71410_x();
        if (PlayerUtil.mc.field_71439_g.field_70701_bs < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        Minecraft.func_71410_x();
        if (PlayerUtil.mc.field_71439_g.field_70701_bs < 0.0f) {
            forward = -0.5f;
        } else {
            Minecraft.func_71410_x();
            if (PlayerUtil.mc.field_71439_g.field_70701_bs > 0.0f) {
                forward = 0.5f;
            }
        }
        Minecraft.func_71410_x();
        if (PlayerUtil.mc.field_71439_g.field_70702_br > 0.0f) {
            yaw -= 90.0f * forward;
        }
        Minecraft.func_71410_x();
        if (PlayerUtil.mc.field_71439_g.field_70702_br < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= (float)Math.PI / 180;
    }

    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d1 = y1 - y2;
        double d2 = z1 - z2;
        return MathHelper.func_76133_a((double)(d0 * d0 + d1 * d1 + d2 * d2));
    }

    public static boolean MovementInput() {
        return PlayerUtil.mc.field_71474_y.field_74351_w.field_74513_e || PlayerUtil.mc.field_71474_y.field_74370_x.field_74513_e || PlayerUtil.mc.field_71474_y.field_74366_z.field_74513_e || PlayerUtil.mc.field_71474_y.field_74368_y.field_74513_e;
    }

    public static boolean isMoving() {
        if (!PlayerUtil.mc.field_71439_g.field_70123_F && !PlayerUtil.mc.field_71439_g.func_70093_af()) {
            return PlayerUtil.mc.field_71439_g.field_71158_b.field_78900_b != 0.0f || PlayerUtil.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f;
        }
        return false;
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0 / inc;
        return (double)Math.round(val * one) / one;
    }

    public static boolean isMoving2() {
        return PlayerUtil.mc.field_71439_g.field_70701_bs != 0.0f || PlayerUtil.mc.field_71439_g.field_70702_br != 0.0f;
    }

    public static boolean isInLiquid() {
        if (PlayerUtil.mc.field_71439_g == null) {
            return false;
        }
        for (int x = MathHelper.func_76128_c((double)PlayerUtil.mc.field_71439_g.func_174813_aQ().field_72340_a); x < MathHelper.func_76128_c((double)PlayerUtil.mc.field_71439_g.func_174813_aQ().field_72336_d) + 1; ++x) {
            for (int z = MathHelper.func_76128_c((double)PlayerUtil.mc.field_71439_g.func_174813_aQ().field_72339_c); z < MathHelper.func_76128_c((double)PlayerUtil.mc.field_71439_g.func_174813_aQ().field_72334_f) + 1; ++z) {
                BlockPos pos = new BlockPos(x, (int)PlayerUtil.mc.field_71439_g.func_174813_aQ().field_72338_b, z);
                Block block = PlayerUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
                if (block == null || block instanceof BlockAir) continue;
                return block instanceof BlockLiquid;
            }
        }
        return false;
    }

    public static boolean isOnGround1(double height) {
        return !PlayerUtil.mc.field_71441_e.func_72945_a((Entity)PlayerUtil.mc.field_71439_g, PlayerUtil.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, -height, 0.0)).isEmpty();
    }

    public static void setMotion(double speed) {
        double forward = PlayerUtil.mc.field_71439_g.field_71158_b.field_78900_b;
        double strafe = PlayerUtil.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = PlayerUtil.mc.field_71439_g.field_70177_z;
        if (forward == 0.0 && strafe == 0.0) {
            PlayerUtil.mc.field_71439_g.field_70159_w = 0.0;
            PlayerUtil.mc.field_71439_g.field_70179_y = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            PlayerUtil.mc.field_71439_g.field_70159_w = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            PlayerUtil.mc.field_71439_g.field_70179_y = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (PlayerUtil.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
            int amplifier = PlayerUtil.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static BlockPos getHypixelBlockpos(String str) {
        int val = 89;
        if (str != null && str.length() > 1) {
            char[] chs = str.toCharArray();
            int lenght = chs.length;
            for (int i = 0; i < lenght; ++i) {
                val += chs[i] * str.length() * str.length() + str.charAt(0) + str.charAt(1);
            }
            val /= str.length();
        }
        return new BlockPos(val, -val % 255, val);
    }

    public static void blockHit(Entity en, boolean value) {
        Minecraft mc = Minecraft.func_71410_x();
        ItemStack stack = mc.field_71439_g.func_71045_bC();
        if (mc.field_71439_g.func_71045_bC() != null && en != null && value && stack.func_77973_b() instanceof ItemSword && (double)mc.field_71439_g.field_70733_aJ > 0.2) {
            KeyBinding.func_74507_a((int)mc.field_71474_y.field_74313_G.func_151463_i());
        }
    }

    public static boolean isOnLiquid() {
        AxisAlignedBB boundingBox = PlayerUtil.mc.field_71439_g.func_174813_aQ();
        if (boundingBox == null) {
            return false;
        }
        boundingBox = boundingBox.func_72331_e(0.01, 0.0, 0.01).func_72317_d(0.0, -0.01, 0.0);
        boolean onLiquid = false;
        int y = (int)boundingBox.field_72338_b;
        for (int x = MathHelper.func_76128_c((double)boundingBox.field_72340_a); x < MathHelper.func_76128_c((double)(boundingBox.field_72336_d + 1.0)); ++x) {
            for (int z = MathHelper.func_76128_c((double)boundingBox.field_72339_c); z < MathHelper.func_76128_c((double)(boundingBox.field_72334_f + 1.0)); ++z) {
                Block block = PlayerUtil.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
                if (block == Blocks.field_150350_a) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                onLiquid = true;
            }
        }
        return onLiquid;
    }

    public static float getMaxFallDist() {
        PotionEffect potioneffect = PlayerUtil.mc.field_71439_g.func_70660_b(Potion.field_76430_j);
        int f = potioneffect != null ? potioneffect.func_76458_c() + 1 : 0;
        return PlayerUtil.mc.field_71439_g.func_82143_as() + f;
    }

    public static boolean isHoldingSword() {
        return PlayerUtil.mc.field_71439_g.func_71045_bC() != null && PlayerUtil.mc.field_71439_g.func_71045_bC().func_77973_b() instanceof ItemSword;
    }

    public static boolean isBlockUnder() {
        if (PlayerUtil.mc.field_71439_g.field_70163_u < 0.0) {
            return false;
        }
        for (int off = 0; off < (int)PlayerUtil.mc.field_71439_g.field_70163_u + 2; off += 2) {
            AxisAlignedBB bb = PlayerUtil.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, (double)(-off), 0.0);
            if (PlayerUtil.mc.field_71441_e.func_72945_a((Entity)PlayerUtil.mc.field_71439_g, bb).isEmpty()) continue;
            return true;
        }
        return false;
    }

    public static boolean isOnHypixel() {
        return !mc.func_71356_B() && PlayerUtil.mc.func_147104_D().field_78845_b.contains("hypixel");
    }

    public static boolean checkServer(String serverIP) {
        return !mc.func_71356_B() && PlayerUtil.mc.func_147104_D().field_78845_b.contains(serverIP);
    }

    public static String getIP() {
        return mc.func_71356_B() ? "127.0.0.1" : PlayerUtil.mc.func_147104_D().field_78845_b;
    }

    public static double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (PlayerUtil.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
            int amplifier = PlayerUtil.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static int getSpeedEffect() {
        if (PlayerUtil.mc.field_71439_g.func_70644_a(Potion.field_76424_c)) {
            return PlayerUtil.mc.field_71439_g.func_70660_b(Potion.field_76424_c).func_76458_c() + 1;
        }
        return 0;
    }
}

