package de.tired.base.module.implementation.misc;

import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.util.hook.PlayerHook;
import de.tired.util.math.TimerUtil;
import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.EventPreMotion;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

@ModuleAnnotation(name = "AutoArmor", category = ModuleCategory.PLAYER, clickG = "automatically equips armor")

public class AutoArmor extends Module {

    public TimerUtil timerUtil = new TimerUtil();

    private BooleanSetting onlyWhileNotMoving = new BooleanSetting("onlyWhileNotMoving", this, true);
    private BooleanSetting onlyInv = new BooleanSetting("onlyInv", this, true);

    public NumberSetting delay = new NumberSetting("Delay", this, 60, 0, 500, 1);

    public AutoArmor() {

    }

    @EventTarget
    public void onUpdate(EventPreMotion e) {
        if ((onlyInv.getValue() && !(MC.currentScreen instanceof GuiInventory)) || (onlyWhileNotMoving.getValue() && PlayerHook.isMoving())) {
            return;
        }
        if (MC.thePlayer.openContainer instanceof ContainerChest) {
            timerUtil.doReset();
        }
        if (timerUtil.reachedTime(delay.getValue().longValue())) {
            for (int armorSlot = 5; armorSlot < 9; armorSlot++) {
                if (equipBest(armorSlot)) {
                    timerUtil.doReset();
                    break;
                }
            }
        }

    }

    public static float getProtection(ItemStack stack){
        float prot = 0;
        if ((stack.getItem() instanceof ItemArmor)) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            prot += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075D;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)/100d;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)/100d;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack)/100d;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)/50d;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack)/100d;
        }
        return prot;
    }

    public static boolean isBestArmor(ItemStack stack, int type){
        float prot = getProtection(stack);
        String strType = "";
        if(type == 1){
            strType = "helmet";
        }else if(type == 2){
            strType = "chestplate";
        }else if(type == 3){
            strType = "leggings";
        }else if(type == 4){
            strType = "boots";
        }
        if(!stack.getUnlocalizedName().contains(strType)){
            return false;
        }
        for (int i = 5; i < 45; i++) {
            if (MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(getProtection(is) > prot && is.getUnlocalizedName().contains(strType))
                    return false;
            }
        }
        return true;
    }

    private boolean equipBest(int armorSlot) {
        int equipSlot = -1, currProt = -1;
        ItemArmor currItem = null;
        ItemStack slotStack = MC.thePlayer.inventoryContainer.getSlot(armorSlot).getStack();
        if (slotStack != null && slotStack.getItem() instanceof ItemArmor) {
            currItem = (ItemArmor) slotStack.getItem();
            currProt = currItem.damageReduceAmount
                    + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, MC.thePlayer.inventoryContainer.getSlot(armorSlot).getStack());
        }
        for (int i = 9; i < 45; i++) {
            ItemStack is = MC.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (is != null && is.getItem() instanceof ItemArmor) {
                int prot = ((ItemArmor) is.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, is);
                if ((currItem == null || currProt < prot) && isValidPiece(armorSlot, (ItemArmor) is.getItem())) {
                    currItem = (ItemArmor) is.getItem();
                    equipSlot = i;
                    currProt = prot;
                }
            }
        }
        if (equipSlot != -1) {
            if (slotStack != null) {
                drop(armorSlot);
            } else {
                click(equipSlot, 0, true);
            }
            return true;
        }
        return false;
    }

    private boolean isValidPiece(int armorSlot, ItemArmor item) {
        String unlocalizedName = item.getUnlocalizedName();
        return armorSlot == 5 && unlocalizedName.startsWith("item.helmet")
                || armorSlot == 6 && unlocalizedName.startsWith("item.chestplate")
                || armorSlot == 7 && unlocalizedName.startsWith("item.leggings")
                || armorSlot == 8 && unlocalizedName.startsWith("item.boots");
    }

    public static void click(int slot, int mouseButton, boolean shiftClick) {
        MC.playerController.windowClick(MC.thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, MC.thePlayer);
    }

    public static void drop(int slot) {
        MC.playerController.windowClick(0, slot, 1, 4, MC.thePlayer);
    }


    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
