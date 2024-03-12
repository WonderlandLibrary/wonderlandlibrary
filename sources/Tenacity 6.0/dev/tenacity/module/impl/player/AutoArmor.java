// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.player;

import net.minecraft.item.ItemStack;
import dev.tenacity.utils.player.InventoryUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemArmor;
import net.minecraft.inventory.ContainerChest;
import dev.tenacity.utils.player.MovementUtils;
import net.minecraft.client.gui.inventory.GuiInventory;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class AutoArmor extends Module
{
    private final NumberSetting delay;
    private final BooleanSetting onlyWhileNotMoving;
    private final BooleanSetting invOnly;
    private final TimerUtil timer;
    
    public AutoArmor() {
        super("AutoArmor", "Auto Armor", Category.PLAYER, "Automatically equips armor");
        this.delay = new NumberSetting("Delay", 150.0, 300.0, 0.0, 10.0);
        this.onlyWhileNotMoving = new BooleanSetting("Stop when moving", false);
        this.invOnly = new BooleanSetting("Inventory only", false);
        this.timer = new TimerUtil();
        this.addSettings(this.delay, this.onlyWhileNotMoving, this.invOnly);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        if (e.isPost()) {
            return;
        }
        if ((this.invOnly.isEnabled() && !(AutoArmor.mc.currentScreen instanceof GuiInventory)) || (this.onlyWhileNotMoving.isEnabled() && MovementUtils.isMoving())) {
            return;
        }
        if (AutoArmor.mc.thePlayer.openContainer instanceof ContainerChest) {
            this.timer.reset();
        }
        if (this.timer.hasTimeElapsed(this.delay.getValue().longValue())) {
            for (int armorSlot = 5; armorSlot < 9; ++armorSlot) {
                if (this.equipBest(armorSlot)) {
                    this.timer.reset();
                    break;
                }
            }
        }
    }
    
    private boolean equipBest(final int armorSlot) {
        int equipSlot = -1;
        int currProt = -1;
        ItemArmor currItem = null;
        final ItemStack slotStack = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(armorSlot).getStack();
        if (slotStack != null && slotStack.getItem() instanceof ItemArmor) {
            currItem = (ItemArmor)slotStack.getItem();
            currProt = currItem.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, AutoArmor.mc.thePlayer.inventoryContainer.getSlot(armorSlot).getStack());
        }
        for (int i = 9; i < 45; ++i) {
            final ItemStack is = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (is != null && is.getItem() instanceof ItemArmor) {
                final int prot = ((ItemArmor)is.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, is);
                if ((currItem == null || currProt < prot) && this.isValidPiece(armorSlot, (ItemArmor)is.getItem())) {
                    currItem = (ItemArmor)is.getItem();
                    equipSlot = i;
                    currProt = prot;
                }
            }
        }
        if (equipSlot != -1) {
            if (slotStack != null) {
                InventoryUtils.drop(armorSlot);
            }
            else {
                InventoryUtils.click(equipSlot, 0, true);
            }
            return true;
        }
        return false;
    }
    
    private boolean isValidPiece(final int armorSlot, final ItemArmor item) {
        final String unlocalizedName = item.getUnlocalizedName();
        return (armorSlot == 5 && unlocalizedName.startsWith("item.helmet")) || (armorSlot == 6 && unlocalizedName.startsWith("item.chestplate")) || (armorSlot == 7 && unlocalizedName.startsWith("item.leggings")) || (armorSlot == 8 && unlocalizedName.startsWith("item.boots"));
    }
}
