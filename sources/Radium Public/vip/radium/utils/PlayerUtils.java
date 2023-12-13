package vip.radium.utils;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public final class PlayerUtils {

    private PlayerUtils() {}

    public static double getTotalArmorProtection(final EntityPlayer player) {
        double totalArmor = 0;
        for (int i = 0; i < 4; i++) {
            final ItemStack armorStack = player.getCurrentArmor(i);

            if (armorStack != null && armorStack.getItem() instanceof ItemArmor) {
                totalArmor += InventoryUtils.getDamageReduction(armorStack);
            }
        }

        return totalArmor;
    }

    public static boolean hasMoved(final EntityPlayer player) {
        // lastTickPos(X/Z) is only updated for local player
        final boolean localPlayer = player instanceof EntityPlayerSP;
        double xDist = player.posX - (localPlayer ? player.lastTickPosX : player.prevPosX);
        double zDist = player.posZ - (localPlayer ? player.lastTickPosZ : player.prevPosZ);
        return StrictMath.sqrt(xDist * xDist + zDist * zDist) > 9.0E-4D;
    }

    public static float getMovementDirection(final EntityPlayer player) {
        return RotationUtils.getYawBetween(player.rotationYaw,
                player.posX, player.posZ,
                player.prevPosX, player.prevPosZ);
    }

    public static boolean hasInvalidNetInfo(final EntityPlayer entity) {
        final NetworkPlayerInfo info = Wrapper.getNetHandler().getPlayerInfo(entity.getUniqueID());
        return info == null || info.getResponseTime() != 1;
    }

    public static int getBounty(final EntityPlayer player) {
        if (!ServerUtils.isOnHypixel() || HypixelGameUtils.getGameMode() != HypixelGameUtils.GameMode.PIT)
            return -1;
        // TODO: get bounty (will be used for killaura sorting and possibly target list or esp?)
        final String playerName = player.getGameProfile().getName();
        final String tabListName = player.getDisplayName().getFormattedText();
        System.out.println(tabListName);
        return 0;
    }

    public static boolean isTeamMate(final EntityPlayer entity) {
        final String entName = entity.getDisplayName().getFormattedText();
        final String playerName = Wrapper.getPlayer().getDisplayName().getFormattedText();
        if (entName.length() < 2 || playerName.length() < 2) return false;
        if (!entName.startsWith("\247") || !playerName.startsWith("\247")) return false;
        return entName.charAt(1) == playerName.charAt(1);
    }
}
