// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.combat;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import exhibition.event.RegisterEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.client.gui.inventory.GuiInventory;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import exhibition.module.Module;

public class AutoSword extends Module
{
    private Timer timer;
    
    public AutoSword(final ModuleData data) {
        super(data);
        this.timer = new Timer();
    }
    
    @RegisterEvent(events = { EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        final EventMotion em = (EventMotion)event;
        if (em.isPre() && (AutoSword.mc.currentScreen instanceof GuiInventory || AutoSword.mc.currentScreen == null) && AutoSword.mc.thePlayer.getCurrentEquippedItem() != null && AutoSword.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && this.timer.delay(100.0f)) {
            for (int i = 0; i < 45; ++i) {
                if (AutoSword.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    final Item item = AutoSword.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                    if (item instanceof ItemSword) {
                        final float itemDamage = this.getAttackDamage(AutoSword.mc.thePlayer.inventoryContainer.getSlot(i).getStack());
                        final float currentDamage = this.getAttackDamage(AutoSword.mc.thePlayer.getCurrentEquippedItem());
                        if (itemDamage > currentDamage) {
                            this.swap(i, AutoSword.mc.thePlayer.inventory.currentItem);
                            this.timer.reset();
                            break;
                        }
                    }
                }
            }
        }
    }
    
    protected void swap(final int slot, final int hotbarNum) {
        AutoSword.mc.playerController.windowClick(AutoSword.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, AutoSword.mc.thePlayer);
    }
    
    private float getAttackDamage(final ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSword)) {
            return 0.0f;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + ((ItemSword)stack.getItem()).getDamageGiven();
    }
}
