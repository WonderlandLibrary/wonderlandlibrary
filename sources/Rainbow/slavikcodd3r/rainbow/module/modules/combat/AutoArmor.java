package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.utils.ClientUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
@Mod(displayName = "AutoArmor", suffix = "FakeInv")
public class AutoArmor extends Module
{
	
    @EventTarget
    private void onTick(final TickEvent event) {
        if (ClientUtils.player() != null && (ClientUtils.mc().currentScreen == null || ClientUtils.mc().currentScreen instanceof GuiInventory || !ClientUtils.mc().currentScreen.getClass().getName().contains("inventory"))) {
            int slotID = -1;
            double maxProt = -1.0;
            for (int i = 9; i < 45; ++i) {
                final ItemStack stack = ClientUtils.player().inventoryContainer.getSlot(i).getStack();
                if (stack != null) {
                    if (this.canEquip(stack)) {
                        final double protValue = this.getProtectionValue(stack);
                        if (protValue >= maxProt) {
                            slotID = i;
                            maxProt = protValue;
                        }
                    }
                }
            }
            if (slotID != -1) {
                ClientUtils.playerController().windowClick(ClientUtils.player().inventoryContainer.windowId, slotID, 0, 1, ClientUtils.player());
            }
        }
    }
    
    private boolean canEquip(final ItemStack stack) {
        return (ClientUtils.player().getEquipmentInSlot(1) == null && stack.getUnlocalizedName().contains("boots")) || (ClientUtils.player().getEquipmentInSlot(2) == null && stack.getUnlocalizedName().contains("leggings")) || (ClientUtils.player().getEquipmentInSlot(3) == null && stack.getUnlocalizedName().contains("chestplate")) || (ClientUtils.player().getEquipmentInSlot(4) == null && stack.getUnlocalizedName().contains("helmet"));
    }
    
    private double getProtectionValue(final ItemStack stack) {
        return ((ItemArmor)stack.getItem()).damageReduceAmount + (100 - ((ItemArmor)stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4 * 0.0075;
    }
}
