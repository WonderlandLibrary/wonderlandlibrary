/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.combat;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.ArmorUtils;
import me.Tengoku.Terror.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public class AutoArmor
extends Module {
    private int[] chestplate;
    private int[] boots;
    private int delay;
    Timer timer = new Timer();
    private int[] helmet;
    private boolean best;
    private int[] leggings;

    public AutoArmor() {
        super("AutoArmor", 0, Category.COMBAT, "Automatically equips armor.");
    }

    public void autoArmor() {
        if (this.best) {
            return;
        }
        int n = -1;
        ++this.delay;
        if (this.delay >= 10) {
            int n2;
            int n3;
            int n4;
            int[] nArray;
            if (Minecraft.thePlayer.inventory.armorInventory[0] == null) {
                nArray = this.boots;
                n4 = this.boots.length;
                n3 = 0;
                while (n3 < n4) {
                    n2 = nArray[n3];
                    if (ArmorUtils.getItem(n2) != -1) {
                        n = ArmorUtils.getItem(n2);
                        break;
                    }
                    ++n3;
                }
            }
            if (Minecraft.thePlayer.inventory.armorInventory[1] == null) {
                nArray = this.leggings;
                n4 = this.leggings.length;
                n3 = 0;
                while (n3 < n4) {
                    n2 = nArray[n3];
                    if (ArmorUtils.getItem(n2) != -1) {
                        n = ArmorUtils.getItem(n2);
                        break;
                    }
                    ++n3;
                }
            }
            if (Minecraft.thePlayer.inventory.armorInventory[2] == null) {
                nArray = this.chestplate;
                n4 = this.chestplate.length;
                n3 = 0;
                while (n3 < n4) {
                    n2 = nArray[n3];
                    if (ArmorUtils.getItem(n2) != -1) {
                        n = ArmorUtils.getItem(n2);
                        break;
                    }
                    ++n3;
                }
            }
            if (Minecraft.thePlayer.inventory.armorInventory[3] == null) {
                nArray = this.helmet;
                n4 = this.helmet.length;
                n3 = 0;
                while (n3 < n4) {
                    n2 = nArray[n3];
                    if (ArmorUtils.getItem(n2) != -1) {
                        n = ArmorUtils.getItem(n2);
                        break;
                    }
                    ++n3;
                }
            }
            if (n != -1) {
                Minecraft.playerController.windowClick(0, n, 0, 1, Minecraft.thePlayer);
                this.delay = 0;
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        this.autoArmor();
        this.betterArmor();
    }

    @Override
    public void setup() {
        this.chestplate = new int[]{311, 307, 315, 303, 299};
        this.leggings = new int[]{312, 308, 316, 304, 300};
        this.boots = new int[]{313, 309, 317, 305, 301};
        this.helmet = new int[]{310, 306, 314, 302, 298};
        this.delay = 0;
        this.best = true;
    }

    public void betterArmor() {
        block23: {
            int n;
            int n2;
            int n3;
            int[] nArray;
            block24: {
                if (!this.best) {
                    return;
                }
                ++this.delay;
                if (this.delay < 10) break block23;
                if (Minecraft.thePlayer.openContainer == null) break block24;
                if (Minecraft.thePlayer.openContainer.windowId != 0) break block23;
            }
            boolean bl = false;
            int n4 = -1;
            if (Minecraft.thePlayer.inventory.armorInventory[0] == null) {
                nArray = this.boots;
                n3 = this.boots.length;
                n2 = 0;
                while (n2 < n3) {
                    n = nArray[n2];
                    if (ArmorUtils.getItem(n) != -1) {
                        n4 = ArmorUtils.getItem(n);
                        break;
                    }
                    ++n2;
                }
            }
            if (ArmorUtils.isBetterArmor(0, this.boots)) {
                n4 = 8;
                bl = true;
            }
            if (Minecraft.thePlayer.inventory.armorInventory[3] == null) {
                nArray = this.helmet;
                n3 = this.helmet.length;
                n2 = 0;
                while (n2 < n3) {
                    n = nArray[n2];
                    if (ArmorUtils.getItem(n) != -1) {
                        n4 = ArmorUtils.getItem(n);
                        break;
                    }
                    ++n2;
                }
            }
            if (ArmorUtils.isBetterArmor(3, this.helmet)) {
                n4 = 5;
                bl = true;
            }
            if (Minecraft.thePlayer.inventory.armorInventory[1] == null) {
                nArray = this.leggings;
                n3 = this.leggings.length;
                n2 = 0;
                while (n2 < n3) {
                    n = nArray[n2];
                    if (ArmorUtils.getItem(n) != -1) {
                        n4 = ArmorUtils.getItem(n);
                        break;
                    }
                    ++n2;
                }
            }
            if (ArmorUtils.isBetterArmor(1, this.leggings)) {
                n4 = 7;
                bl = true;
            }
            if (Minecraft.thePlayer.inventory.armorInventory[2] == null) {
                nArray = this.chestplate;
                n3 = this.chestplate.length;
                n2 = 0;
                while (n2 < n3) {
                    n = nArray[n2];
                    if (ArmorUtils.getItem(n) != -1) {
                        n4 = ArmorUtils.getItem(n);
                        break;
                    }
                    ++n2;
                }
            }
            if (ArmorUtils.isBetterArmor(2, this.chestplate)) {
                n4 = 6;
                bl = true;
            }
            n3 = 0;
            ItemStack[] itemStackArray = Minecraft.thePlayer.inventory.mainInventory;
            int n5 = Minecraft.thePlayer.inventory.mainInventory.length;
            int n6 = 0;
            while (n6 < n5) {
                ItemStack itemStack = itemStackArray[n6];
                if (itemStack == null) {
                    n3 = 1;
                    break;
                }
                ++n6;
            }
            boolean bl2 = bl = bl && n3 == 0;
            if (n4 != -1 && AutoArmor.mc.currentScreen instanceof GuiContainer) {
                Minecraft.playerController.windowClick(0, n4, 0, bl ? 4 : 1, Minecraft.thePlayer);
                this.delay = 0;
            }
        }
    }
}

