// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.math;

import java.text.DecimalFormat;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Random;

public class MathUtils
{
    private static Random rng;
    
    static {
        MathUtils.rng = new Random();
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer, final double height) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
    }
    
    public static double distance2D(final double startX, final double startZ, final double endX, final double endZ) {
        return Math.hypot(startX - endX, startZ - endZ);
    }
    
    public static double distance3D(final Entity Player, final Entity target) {
        final float f = (float)(Player.posX - target.posX);
        final float f2 = (float)(Player.posY - target.posY);
        final float f3 = (float)(Player.posZ - target.posZ);
        return MathHelper.sqrt_float(f * f + f2 * f2 + f3 * f3);
    }
    
    public static double distance3D(final double startX, final double startY, final double startZ, final double endX, final double endY, final double endZ) {
        final float f = (float)(startX - endX);
        final float f2 = (float)(startY - endY);
        final float f3 = (float)(startZ - endZ);
        return MathHelper.sqrt_float(f * f + f2 * f2 + f3 * f3);
    }
    
    public static double getRandomInRange(final double min, final double max) {
        final Random random = new Random();
        final double range = max - min;
        double scaled = random.nextDouble() * range;
        if (scaled > max) {
            scaled = max;
        }
        double shifted = scaled + min;
        if (shifted > max) {
            shifted = max;
        }
        return shifted;
    }
    
    public static double getHighestOffset(final double max) {
        for (double i = 0.0; i < max; i += 0.01) {
            int[] array;
            for (int length = (array = new int[] { -2, -1, 0, 1, 2 }).length, j = 0; j < length; ++j) {
                final int offset = array[j];
                if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(Minecraft.getMinecraft().thePlayer.motionX * offset, i, Minecraft.getMinecraft().thePlayer.motionZ * offset)).size() > 0) {
                    return i - 0.01;
                }
            }
        }
        return max;
    }
    
    public static float[] constrainAngle(final float[] vector) {
        vector[0] %= 360.0f;
        vector[1] %= 360.0f;
        while (vector[0] <= -180.0f) {
            vector[0] += 360.0f;
        }
        while (vector[1] <= -180.0f) {
            vector[1] += 360.0f;
        }
        while (vector[0] > 180.0f) {
            vector[0] -= 360.0f;
        }
        while (vector[1] > 180.0f) {
            vector[1] -= 360.0f;
        }
        return vector;
    }
    
    public static float getRandomInRange(final float min, final float max) {
        final Random random = new Random();
        final float range = max - min;
        final float scaled = random.nextFloat() * range;
        final float shifted = scaled + min;
        return shifted;
    }
    
    public static int getRandomInRange(final int min, final int max) {
        final Random rand = new Random();
        final int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }
    
    public double doRound(final double d, final int r) {
        String round = "#";
        for (int i = 0; i < r; ++i) {
            round = String.valueOf(String.valueOf(round)) + ".#";
        }
        final DecimalFormat twoDForm = new DecimalFormat(round);
        return Double.valueOf(twoDForm.format(d));
    }
    
    public static int getMiddle(final int i, final int i2) {
        return (i + i2) / 2;
    }
    
    public static Random getRng() {
        return MathUtils.rng;
    }
    
    public static int getNumberFor(final int start, final int end) {
        if (end >= start) {
            return 0;
        }
        if (end - start < 0) {
            return 0;
        }
        return end - start;
    }
    
    public static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static float getRandom() {
        return MathUtils.rng.nextFloat();
    }
    
    public static int getRandom(final int cap) {
        return MathUtils.rng.nextInt(cap);
    }
    
    public static int getRandom(final int floor, final int cap) {
        return floor + MathUtils.rng.nextInt(cap - floor + 1);
    }
    
    public static double getRandomf(final double min, final double max) {
        return min + MathUtils.rng.nextDouble() * (max - min + 1.0);
    }
    
    public static double getMiddleDouble(final int i, final int i2) {
        return (i + i2) / 2.0;
    }
    
    public static double clamp(final double value, final double minimum, final double maximum) {
        return (value > maximum) ? maximum : ((value < minimum) ? minimum : value);
    }
    
    public static double normalizeAngle(final double angle) {
        return (angle + 360.0) % 360.0;
    }
    
    public static float normalizeAngle(final float angle) {
        return (angle + 360.0f) % 360.0f;
    }
    
    public static float clamp(final float input, final float max) {
        return clamp(input, max, -max);
    }
    
    public static float clamp(float input, final float max, final float min) {
        if (input > max) {
            input = max;
        }
        if (input < min) {
            input = min;
        }
        return input;
    }
    
    public static double getSpeed() {
        double speed = Math.sqrt(Math.pow(Minecraft.getMinecraft().thePlayer.posX - Minecraft.getMinecraft().thePlayer.lastTickPosX, 2.0) + Math.pow(Minecraft.getMinecraft().thePlayer.posZ - Minecraft.getMinecraft().thePlayer.lastTickPosZ, 2.0));
        speed = (double)Math.round(speed * 100.0);
        speed /= 10.0;
        speed *= 2.0;
        speed *= Minecraft.getMinecraft().timer.timerSpeed;
        final DecimalFormat df = new DecimalFormat("###.#");
        speed = Double.parseDouble(df.format(speed));
        return speed;
    }
}
