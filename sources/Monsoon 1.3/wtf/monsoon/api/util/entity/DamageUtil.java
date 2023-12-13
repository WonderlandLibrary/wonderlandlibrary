package wtf.monsoon.api.util.entity;


import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C03PacketPlayer;
import wtf.monsoon.api.util.misc.PacketUtil;

import java.util.Objects;

public class DamageUtil
{
    public static Minecraft mc = Minecraft.getMinecraft();


    public static void damageMethodOne() {
        double damageOffset = 0.06011F,damageY = 0.000495765F, damageYTwo = 0.0049575F;
        double offset = damageOffset;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        for (int i = 0; i < (getMaxFallDist() / (offset - 0.005F)) + 1; i++) {
            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + offset, z, false));
            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + damageY, z, false));
            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + damageYTwo + offset * 0.000001, z, false));
        }
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
    }

    public static void damageVerus() {
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4.1001, mc.thePlayer.posZ, false));
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        mc.thePlayer.jump();
    }

    public static void damageMethod2(double damage) {

        Minecraft mc = Minecraft.getMinecraft();

        if (damage > floor_double(mc.thePlayer.getMaxHealth()))
            damage = floor_double(mc.thePlayer.getMaxHealth());

        double offset = 0.0625;
        //offset = 0.015625;
        if (mc.thePlayer != null) {
            for (short i = 0; i <= ((3 + damage) / offset); i++) {
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY + ((offset / 2) * 1), mc.thePlayer.posZ, false));
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY + ((offset / 2) * 2), mc.thePlayer.posZ, false));
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((3 + damage) / offset))));
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX,
                        mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, (i == ((3 + damage) / offset))));
            }
        }
    }
    public static int floor_double(double p_76128_0_)
    {
        int var2 = (int)p_76128_0_;
        return p_76128_0_ < (double)var2 ? var2 - 1 : var2;
    }

    public static float getMaxFallDist() {
        //PotionEffect potioneffect = mc.player.getActivePotionEffect(Potion.jump);
        //int f = potioneffect != null ? (potioneffect.getAmplifier() + 1) : 0;
        return Minecraft.getMinecraft().thePlayer.getMaxFallHeight();
    }

    public static void damageMethodThree() {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;
        float minValue = 3.1F;
        for (int i = 0; i < (int) ((minValue / (randomNumber(0.0890D, 0.0849D) - 1.0E-3D - Math.random() * 0.0002F - Math.random() * 0.0002F)) + 18); i++) {
            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + randomNumber(0.0655D, 0.0625D) - randomNumber(1.0E-3D, 1.0E-2D) - Math.random() * 0.0002F, z, false));
            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + Math.random() * 0.0002F, z, false));
        }
        PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
    }

    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }
}