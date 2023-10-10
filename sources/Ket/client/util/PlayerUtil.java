package client.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@UtilityClass
public final class PlayerUtil implements MinecraftInstance {
    public boolean isInLiquid() {
        return mc.thePlayer.isInWater() || mc.thePlayer.isInLava();
    }
    public double getDistance(final Entity entity) {
        final double diffX = mc.thePlayer.posX - entity.posX, diffY = mc.thePlayer.posY - entity.posY, diffZ = mc.thePlayer.posZ - entity.posZ;
        return Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
    }
    public static int findItem(int startSlot, int endSlot, Item item, Minecraft mc) {
        for (int i = startSlot; i < endSlot; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (stack != null && stack.getItem() == item)
                return i;
        }
        return -1;
    }

    public static EntityPlayerSP getLocalPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
}
