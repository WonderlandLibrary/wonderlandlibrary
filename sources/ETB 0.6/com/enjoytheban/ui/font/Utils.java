package com.enjoytheban.ui.font;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class Utils {
    public static boolean fuck = true;
    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isContainerEmpty(final Container container) {
        for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 27; i < slotAmount; ++i) {
            if (container.getSlot(i).getHasStack()) {
                return false;
            }
        }
        return true;
    }

    public static Minecraft getMinecraft() {
        return mc;
    }

    public static boolean canBlock() {
        if (mc == null)
            mc = Minecraft.getMinecraft();

        if (mc.thePlayer.getHeldItem() == null)
            return false;
        if (mc.thePlayer.isBlocking() || (mc.thePlayer.isUsingItem() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))
            return true;
        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && Minecraft.getMinecraft().gameSettings.keyBindUseItem.isPressed())
            return true;

        return false;
    }

    public static String getMD5(final String input) {
        final StringBuilder res = new StringBuilder();
        try {
            final MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(input.getBytes());
            final byte[] md5 = algorithm.digest();
            String tmp;
            for (final byte aMd5 : md5) {
                tmp = Integer.toHexString(0xFF & aMd5);
                if (tmp.length() == 1) {
                    res.append("0").append(tmp);
                } else {
                    res.append(tmp);
                }
            }
        } catch (final NoSuchAlgorithmException ignored) {
        }
        return res.toString();
    }

    public static void breakAnticheats() {
    	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + mc.thePlayer.motionX, mc.thePlayer.posY - 110, mc.thePlayer.posZ + mc.thePlayer.motionZ, true));
    }

    public static int add(final int number, final int add, final int max) {
        return (number + add > max ? max : number + add);
    }

    public static int remove(final int number, final int remove, final int min) {
        return (number - remove < min ? min : number - remove);
    }

    public static int check(final int number) {
        return (number <= 0 ? 1 : (number > 255 ? 255 : number));
    }

    public static double getDist() {
        double distance = 0;
        for (double i = mc.thePlayer.posY; i > 0; i -= 0.1) {
            if (i < 0)
                break;

            final Block block = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ)).getBlock();
            if (block.getMaterial() != Material.air && (block.isCollidable()) && (block.isFullBlock() || block instanceof BlockSlab || block instanceof BlockBarrier || block instanceof BlockStairs ||
                    block instanceof BlockGlass || block instanceof BlockStainedGlass)) {

                if (block instanceof BlockSlab)
                    i -= 0.5;

                distance = i;
                break;
            }
        }
        return (mc.thePlayer.posY - distance);
    }
}