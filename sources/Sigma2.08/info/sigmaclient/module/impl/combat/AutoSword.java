package info.sigmaclient.module.impl.combat;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.impl.player.InventoryCleaner;
import info.sigmaclient.util.Timer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

/**
 * Created by Arithmo on 5/1/2017 at 3:37 PM.
 */
public class AutoSword extends Module {

    private Timer timer = new Timer();

    public AutoSword(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        EventUpdate em = (EventUpdate) event;
        if(Client.getModuleManager().get(InventoryCleaner.class).isEnabled() && InventoryCleaner.isCleaning())
            return;
        if (em.isPre() && (mc.currentScreen instanceof GuiInventory || mc.currentScreen == null)) {
            if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && timer.delay(100)) {
                for (int i = 0; i < 45; i++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                        if (item instanceof ItemSword) {
                            float itemDamage = getAttackDamage(mc.thePlayer.inventoryContainer.getSlot(i).getStack());
                            float currentDamage = getAttackDamage(mc.thePlayer.getCurrentEquippedItem());
                            if (itemDamage > currentDamage) {
                                swap(i, mc.thePlayer.inventory.currentItem);
                                timer.reset();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    protected void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }

    private float getAttackDamage(ItemStack stack) {
        if(!(stack.getItem() instanceof ItemSword)) {
            return 0;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + ((ItemSword)stack.getItem()).getAttackDamage()
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
    }

}
