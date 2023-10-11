package com.initial.modules.impl.player;

import com.initial.modules.*;
import com.initial.events.impl.*;
import com.initial.events.*;
import com.initial.utils.player.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;

public class AutoArmor extends Module
{
    private int[] chestplate;
    private int[] leggings;
    private int[] boots;
    private int[] helmet;
    private int delay;
    private boolean best;
    
    public AutoArmor() {
        super("AutoArmor", 0, Category.COMBAT);
    }
    
    @Override
    public void setup() {
        this.chestplate = new int[] { 311, 307, 315, 303, 299 };
        this.leggings = new int[] { 312, 308, 316, 304, 300 };
        this.boots = new int[] { 313, 309, 317, 305, 301 };
        this.helmet = new int[] { 310, 306, 314, 302, 298 };
        this.delay = 0;
        this.best = true;
    }
    
    @EventTarget
    public void onEvent(final EventUpdate event) {
        this.setDisplayName("Auto Armor");
        this.autoArmor();
        this.betterArmor();
    }
    
    public void autoArmor() {
        if (this.best) {
            return;
        }
        int item = -1;
        ++this.delay;
        if (this.delay >= 10) {
            if (this.mc.thePlayer.inventory.armorInventory[0] == null) {
                int[] boots;
                for (int length = (boots = this.boots).length, i = 0; i < length; ++i) {
                    final int id = boots[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (this.mc.thePlayer.inventory.armorInventory[1] == null) {
                int[] leggings;
                for (int length = (leggings = this.leggings).length, i = 0; i < length; ++i) {
                    final int id = leggings[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (this.mc.thePlayer.inventory.armorInventory[2] == null) {
                int[] chestplate;
                for (int length = (chestplate = this.chestplate).length, i = 0; i < length; ++i) {
                    final int id = chestplate[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (this.mc.thePlayer.inventory.armorInventory[3] == null) {
                int[] helmet;
                for (int length = (helmet = this.helmet).length, i = 0; i < length; ++i) {
                    final int id = helmet[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (item != -1) {
                this.mc.playerController.windowClick(0, item, 0, 1, this.mc.thePlayer);
                this.delay = 0;
            }
        }
    }
    
    public void betterArmor() {
        if (!this.best) {
            return;
        }
        ++this.delay;
        if (this.delay >= 10 && (this.mc.thePlayer.openContainer == null || this.mc.thePlayer.openContainer.windowId == 0)) {
            boolean switchArmor = false;
            int item = -1;
            if (this.mc.thePlayer.inventory.armorInventory[0] == null) {
                int[] array;
                for (int j = (array = this.boots).length, i = 0; i < j; ++i) {
                    final int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(0, this.boots)) {
                item = 8;
                switchArmor = true;
            }
            if (this.mc.thePlayer.inventory.armorInventory[3] == null) {
                int[] array;
                for (int j = (array = this.helmet).length, i = 0; i < j; ++i) {
                    final int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(3, this.helmet)) {
                item = 5;
                switchArmor = true;
            }
            if (this.mc.thePlayer.inventory.armorInventory[1] == null) {
                int[] array;
                for (int j = (array = this.leggings).length, i = 0; i < j; ++i) {
                    final int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(1, this.leggings)) {
                item = 7;
                switchArmor = true;
            }
            if (this.mc.thePlayer.inventory.armorInventory[2] == null) {
                int[] array;
                for (int j = (array = this.chestplate).length, i = 0; i < j; ++i) {
                    final int id = array[i];
                    if (ArmorUtils.getItem(id) != -1) {
                        item = ArmorUtils.getItem(id);
                        break;
                    }
                }
            }
            if (ArmorUtils.isBetterArmor(2, this.chestplate)) {
                item = 6;
                switchArmor = true;
            }
            boolean b = false;
            ItemStack[] stackArray;
            for (int k = (stackArray = this.mc.thePlayer.inventory.mainInventory).length, l = 0; l < k; ++l) {
                final ItemStack stack = stackArray[l];
                if (stack == null) {
                    b = true;
                    break;
                }
            }
            switchArmor = (switchArmor && !b);
            if (item != -1) {
                this.mc.playerController.windowClick(0, item, 0, switchArmor ? 4 : 1, this.mc.thePlayer);
                this.delay = 0;
            }
        }
    }
}
