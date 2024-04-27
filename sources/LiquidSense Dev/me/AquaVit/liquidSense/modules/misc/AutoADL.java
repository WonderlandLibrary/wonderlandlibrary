package me.AquaVit.liquidSense.modules.misc;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

@ModuleInfo(name = "AutoADL", description = "go fuck yourself", category = ModuleCategory.MISC)
public class AutoADL extends Module {

    @EventTarget
    public void onUpdate(UpdateEvent e) {
        ItemStack[] items = getHotbarItems();
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item != null && item.getItem() == Items.iron_sword && item.getEnchantmentTagList() != null && item.getEnchantmentTagList().tagCount() == 1) {
                NBTTagCompound enchantment = (NBTTagCompound) item.getEnchantmentTagList().get(0);
                if ((enchantment.getInteger("id") == 16 && enchantment.getInteger("lvl") == 2)) {
                    if (item.hasDisplayName()) {
                        if (item.getTagCompound().getCompoundTag("display").getTag("Lore") != null) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i));
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                            System.out.println("send packet" + enchantment);
                        }
                    }
                }
            }
        }
    }

    private ItemStack[] getHotbarItems() {
        ItemStack[] results = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            results[i] = mc.thePlayer.inventory.mainInventory[i];
        }
        return results;
    }
}
