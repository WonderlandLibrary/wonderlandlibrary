/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.player;

import de.Hero.settings.Setting;
import java.util.concurrent.ThreadLocalRandom;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.ArmorUtils;
import me.Tengoku.Terror.util.Timer;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class InventoryCleaner
extends Module {
    private int delay;
    private int[] helmet;
    private boolean best;
    private Timer timer2;
    private TimerUtils timer = new TimerUtils();
    private int[] leggings;
    private int[] boots;
    private Timer timer3;
    private int[] chestplate;

    private boolean isBad(Item item) {
        return item.getUnlocalizedName().contains("tnt") || item.getUnlocalizedName().contains("stick") || item.getUnlocalizedName().contains("egg") || item.getUnlocalizedName().contains("string") || item.getUnlocalizedName().contains("flint") || item.getUnlocalizedName().contains("bow") || item.getUnlocalizedName().contains("arrow") || item.getUnlocalizedName().contains("bucket") || item.getUnlocalizedName().contains("feather") || item.getUnlocalizedName().contains("snow") || item.getUnlocalizedName().contains("piston") || item instanceof ItemGlassBottle || item.getUnlocalizedName().contains("web") || item.getUnlocalizedName().contains("slime") || item.getUnlocalizedName().contains("trip") || item.getUnlocalizedName().contains("wire") || item.getUnlocalizedName().contains("sugar") || item.getUnlocalizedName().contains("note") || item.getUnlocalizedName().contains("record") || item.getUnlocalizedName().contains("flower") || item.getUnlocalizedName().contains("wheat") || item.getUnlocalizedName().contains("fishing") || item.getUnlocalizedName().contains("boat") || item.getUnlocalizedName().contains("leather") || item.getUnlocalizedName().contains("seeds") || item.getUnlocalizedName().contains("skull") || item.getUnlocalizedName().contains("torch") || item.getUnlocalizedName().contains("anvil") || item.getUnlocalizedName().contains("enchant") || item.getUnlocalizedName().contains("exp") || item.getUnlocalizedName().contains("shears");
    }

    /*
     * Enabled aggressive block sorting
     */
    private int getBestShovel() {
        int n = -1;
        float f = 1.0f;
        int n2 = 0;
        while (n2 < Minecraft.thePlayer.inventory.mainInventory.length) {
            ItemTool itemTool;
            float f2;
            ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[n2];
            if (itemStack != null && this.isShovel(itemStack.getItem()) && (f2 = (itemTool = (ItemTool)itemStack.getItem()).getStrVsBlock(itemStack, Block.getBlockById(3))) > f) {
                f = f2;
                n = n2;
            }
            ++n2;
        }
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (eventUpdate.isPre()) {
            double d = Math.max(20.0, Exodus.INSTANCE.settingsManager.getSettingByModule("Delay", this).getValDouble() + ThreadLocalRandom.current().nextDouble(-20.0, 20.0));
            if (InventoryCleaner.mc.currentScreen instanceof GuiInventory && this.timer2.hasTimeElapsed((long)d / 10L, true)) {
                int n = this.getBestSword();
                int n2 = this.getBestPickaxe();
                int n3 = this.getBestAxe();
                int n4 = this.getBestShovel();
                int n5 = 0;
                while (n5 < Minecraft.thePlayer.inventory.mainInventory.length) {
                    ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[n5];
                    if (itemStack != null) {
                        boolean bl = Exodus.INSTANCE.settingsManager.getSettingByModule("Clean", this).getValBoolean();
                        if (bl) {
                            if (itemStack.getItem() instanceof ItemArmor) {
                                this.drop(n5, itemStack);
                                this.timer.reset();
                                return;
                            }
                            if (itemStack.getItem() instanceof ItemSword && n != -1 && n != n5) {
                                this.drop(n5, itemStack);
                                this.timer.reset();
                                return;
                            }
                            if (itemStack.getItem() instanceof ItemPickaxe && n2 != -1 && n2 != n5) {
                                this.drop(n5, itemStack);
                                this.timer.reset();
                                return;
                            }
                            if (itemStack.getItem() instanceof ItemAxe && n3 != -1 && n3 != n5) {
                                this.drop(n5, itemStack);
                                this.timer.reset();
                                return;
                            }
                            if (this.isShovel(itemStack.getItem()) && n4 != -1 && n4 != n5) {
                                this.drop(n5, itemStack);
                                this.timer.reset();
                                return;
                            }
                        }
                        int n6 = (int)(Exodus.INSTANCE.settingsManager.getSettingByModule("SwordSlot", this).getValDouble() - 1.0);
                        if (n != -1 && n != n6) {
                            int n7 = 0;
                            while (n7 < Minecraft.thePlayer.inventoryContainer.inventorySlots.size()) {
                                Slot slot = Minecraft.thePlayer.inventoryContainer.inventorySlots.get(n7);
                                if (slot.getHasStack()) {
                                    if (slot.getStack() == Minecraft.thePlayer.inventory.mainInventory[n]) {
                                        Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot.slotNumber, n6, 2, Minecraft.thePlayer);
                                        this.timer.reset();
                                        return;
                                    }
                                }
                                ++n7;
                            }
                        }
                        if (Exodus.INSTANCE.settingsManager.getSettingByModule("CleanBadItems", this).getValBoolean() && this.isBad(itemStack.getItem())) {
                            this.drop(n5, itemStack);
                            this.timer.reset();
                            return;
                        }
                    }
                    this.timer.reset();
                    ++n5;
                }
            }
        }
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

    public InventoryCleaner() {
        super("InvManager", 0, Category.PLAYER, "Cleans your inventory.");
        this.timer2 = new Timer();
        this.timer3 = new Timer();
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Delay", this, 2500.0, 1000.0, 10000.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("SwordSlot", this, 1.0, 1.0, 9.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Clean", this, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("CleanBadItems", this, true));
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

    /*
     * Enabled aggressive block sorting
     */
    private int getBestPickaxe() {
        int n = -1;
        float f = 1.0f;
        int n2 = 0;
        while (n2 < Minecraft.thePlayer.inventory.mainInventory.length) {
            ItemPickaxe itemPickaxe;
            float f2;
            ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[n2];
            if (itemStack != null && itemStack.getItem() instanceof ItemPickaxe && (f2 = (itemPickaxe = (ItemPickaxe)itemStack.getItem()).getStrVsBlock(itemStack, Block.getBlockById(4))) > f) {
                f = f2;
                n = n2;
            }
            ++n2;
        }
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int getBestSword() {
        int n = -1;
        float f = 1.0f;
        int n2 = 0;
        while (n2 < Minecraft.thePlayer.inventory.mainInventory.length) {
            ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[n2];
            if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
                ItemSword itemSword = (ItemSword)itemStack.getItem();
                float f2 = itemSword.getDamageVsEntity();
                if ((f2 += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack)) > f) {
                    f = f2;
                    n = n2;
                }
            }
            ++n2;
        }
        return n;
    }

    private void drop(int n, ItemStack itemStack) {
        boolean bl = false;
        int n2 = 0;
        while (n2 < 9) {
            ItemStack itemStack2 = Minecraft.thePlayer.inventory.getStackInSlot(n2);
            if (itemStack2 != null && itemStack2 == itemStack) {
                bl = true;
            }
            ++n2;
        }
        if (bl) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
            C07PacketPlayerDigging.Action action = C07PacketPlayerDigging.Action.DROP_ALL_ITEMS;
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(action, BlockPos.ORIGIN, EnumFacing.DOWN));
        } else {
            Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, n, 0, 0, Minecraft.thePlayer);
            Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, -999, 0, 0, Minecraft.thePlayer);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private int getBestAxe() {
        int n = -1;
        float f = 1.0f;
        int n2 = 0;
        while (n2 < Minecraft.thePlayer.inventory.mainInventory.length) {
            ItemAxe itemAxe;
            float f2;
            ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[n2];
            if (itemStack != null && itemStack.getItem() instanceof ItemAxe && (f2 = (itemAxe = (ItemAxe)itemStack.getItem()).getStrVsBlock(itemStack, Block.getBlockById(17))) > f) {
                f = f2;
                n = n2;
            }
            ++n2;
        }
        return n;
    }

    private boolean isShovel(Item item) {
        return Item.getItemById(256) == item || Item.getItemById(269) == item || Item.getItemById(273) == item || Item.getItemById(277) == item || Item.getItemById(284) == item;
    }

    public void betterArmor() {
        block24: {
            int n;
            int n2;
            int n3;
            int[] nArray;
            double d;
            block23: {
                if (!this.best) {
                    return;
                }
                d = Math.max(20.0, Exodus.INSTANCE.settingsManager.getSettingByModule("Delay", this).getValDouble() + ThreadLocalRandom.current().nextDouble(-20.0, 20.0));
                if (Minecraft.thePlayer.openContainer == null) break block23;
                if (Minecraft.thePlayer.openContainer.windowId != 0) break block24;
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
            if (n4 != -1 && InventoryCleaner.mc.currentScreen instanceof GuiContainer && this.timer3.hasTimeElapsed((long)(d / 10.0), true)) {
                Minecraft.playerController.windowClick(0, n4, 0, bl ? 4 : 1, Minecraft.thePlayer);
                this.timer3.reset();
            }
        }
    }
}

