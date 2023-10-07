// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util;

import net.minecraft.entity.player.EntityPlayer;

public class InventoryUtil implements MinecraftUtil
{
    public static void clickSlot(final int slot, final int mouseButton, final boolean shiftClick) {
        InventoryUtil.mc.playerController.windowClick((InventoryUtil.mc.currentScreen == null) ? 0 : InventoryUtil.mc.thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, InventoryUtil.mc.thePlayer);
    }
    
    public static void clickSlot(final int slot, final boolean shiftClick) {
        InventoryUtil.mc.playerController.windowClick((InventoryUtil.mc.currentScreen == null) ? 0 : InventoryUtil.mc.thePlayer.inventoryContainer.windowId, slot, 0, shiftClick ? 1 : 0, InventoryUtil.mc.thePlayer);
    }
    
    public static void doubleClickSlot(final int slot) {
        InventoryUtil.mc.playerController.windowClick((InventoryUtil.mc.currentScreen == null) ? 0 : InventoryUtil.mc.thePlayer.inventoryContainer.windowId, slot, 0, 0, InventoryUtil.mc.thePlayer);
        InventoryUtil.mc.playerController.windowClick((InventoryUtil.mc.currentScreen == null) ? 0 : InventoryUtil.mc.thePlayer.inventoryContainer.windowId, slot, 0, 6, InventoryUtil.mc.thePlayer);
        InventoryUtil.mc.playerController.windowClick((InventoryUtil.mc.currentScreen == null) ? 0 : InventoryUtil.mc.thePlayer.inventoryContainer.windowId, slot, 0, 0, InventoryUtil.mc.thePlayer);
    }
}
