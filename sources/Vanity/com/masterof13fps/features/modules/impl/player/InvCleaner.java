package com.masterof13fps.features.modules.impl.player;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.features.modules.Category;
import net.minecraft.item.*;

import java.util.ArrayList;

@ModuleInfo(name = "InvCleaner", category = Category.PLAYER, description = "Automatically cleans your inventory")
public class InvCleaner extends Module {

    private final ArrayList<Integer> uselessItems = new ArrayList<>();

    private void clean(final int i) {
        mc.playerController.windowClick(0, i, 0, 0, mc.thePlayer);
        mc.playerController.windowClick(0, -999, 0, 0, mc.thePlayer);
    }

    /**
     * Werte für i:
     * 0 - 1.Slot in der Hotbar, links
     * 1-7 - Hotbar Slots
     * 8 - letzter Slot in der Hotbar, rechts
     * 9-35 Inventar
     * 36-39 RüstungsSlots
     * 40 zweite Hand
     */
    private void findUselessItems() {
        for (int i = 0; i < 41; i++) {
            if (i >= 0 && i <= 8) continue;
            if (i >= 36 && i <= 39) continue;
            if (i == 40) continue;
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack != null && !stackIsUseful(itemStack)) {
                uselessItems.add(i);
            }
        }
    }

    private boolean stackIsUseful(ItemStack itemStack) {
        if (Item.getIdFromItem(itemStack.getItem()) == 0) return true; //air um leere slots zu skippen
        if (Item.getIdFromItem(itemStack.getItem()) == 30) return true; //cobweb
        if (Item.getIdFromItem(itemStack.getItem()) == 258) return true; //iron axe
        if (Item.getIdFromItem(itemStack.getItem()) == 259) return true; //flint & steel
        if (Item.getIdFromItem(itemStack.getItem()) == 260) return true; //apple
        if (Item.getIdFromItem(itemStack.getItem()) == 261) return true; //bow
        if (Item.getIdFromItem(itemStack.getItem()) == 262) return true; //arrow
        if (Item.getIdFromItem(itemStack.getItem()) == 264) return true; //diamond
        if (Item.getIdFromItem(itemStack.getItem()) == 265) return true; //iron
        if (Item.getIdFromItem(itemStack.getItem()) == 279) return true; //diamond axe
        if (Item.getIdFromItem(itemStack.getItem()) == 282) return true; //mushroom stew
        if (Item.getIdFromItem(itemStack.getItem()) == 297) return true; //bread
        if (Item.getIdFromItem(itemStack.getItem()) == 320) return true; //cooked porkchop
        if (Item.getIdFromItem(itemStack.getItem()) == 322) return true; //golden apple
        if (Item.getIdFromItem(itemStack.getItem()) == 346) return true; //fishing rod
        if (Item.getIdFromItem(itemStack.getItem()) == 364) return true; //steak
        if (Item.getIdFromItem(itemStack.getItem()) == 366) return true; //cooked chicken
        if (Item.getIdFromItem(itemStack.getItem()) == 384) return true; //bottle o' enchanting
        if (Item.getIdFromItem(itemStack.getItem()) == 412) return true; //cooked rabbit
        if (Item.getIdFromItem(itemStack.getItem()) == 424) return true; //cooked mutton
        if (Item.getIdFromItem(itemStack.getItem()) == 442) return true; //shield
        if (Item.getIdFromItem(itemStack.getItem()) == 443) return true; //elytra
        if (itemStack.getItem() instanceof ItemArmor) return true;
        if (itemStack.getItem() instanceof ItemSword) return true;
        if (itemStack.getItem() instanceof ItemPotion) return true;
        if (itemStack.getItem() instanceof ItemFlintAndSteel) return true;
        return itemStack.getItem() instanceof ItemEnderPearl;
    }

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            uselessItems.clear();

            findUselessItems();
            if (!uselessItems.isEmpty()) {
                for (int i : uselessItems) {
                    clean(i);
                }
            }
        }
    }
}
