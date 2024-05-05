package com.initial.utils.player;

import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class PlayerUtils
{
    public static Minecraft mc;
    
    public static boolean isOnSameTeam(final EntityLivingBase entity) {
        if (entity.getTeam() != null && PlayerUtils.mc.thePlayer.getTeam() != null) {
            final char c1 = entity.getDisplayName().getFormattedText().charAt(1);
            final char c2 = PlayerUtils.mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
            return c1 == c2;
        }
        return false;
    }
    
    public static boolean isBlockUnder(final double x, final double y, final double z) {
        for (int i = (int)y - 1; i > 0; --i) {
            if (!(new BlockPos(x, i, z).getBlock() instanceof BlockAir)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isBlockUnder() {
        return isBlockUnder(PlayerUtils.mc.thePlayer.posX, PlayerUtils.mc.thePlayer.posY, PlayerUtils.mc.thePlayer.posZ);
    }
    
    public static void damage() {
        int damage = 1;
        if (damage > MathHelper.floor_double(PlayerUtils.mc.thePlayer.getMaxHealth())) {
            damage = MathHelper.floor_double(PlayerUtils.mc.thePlayer.getMaxHealth());
        }
        final double offset = 0.0625;
        if (PlayerUtils.mc.thePlayer != null && PlayerUtils.mc.getNetHandler() != null && PlayerUtils.mc.thePlayer.onGround) {
            for (int i = 0; i <= (3 + damage) / offset; ++i) {
                PlayerUtils.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtils.mc.thePlayer.posX, PlayerUtils.mc.thePlayer.posY + offset, PlayerUtils.mc.thePlayer.posZ, false));
                PlayerUtils.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtils.mc.thePlayer.posX, PlayerUtils.mc.thePlayer.posY, PlayerUtils.mc.thePlayer.posZ, i == (3 + damage) / offset));
            }
        }
    }
    
    static {
        PlayerUtils.mc = Minecraft.getMinecraft();
    }
}
