package summer.cheat.cheats.player;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventClientTick;
import summer.base.utilities.TimerUtils;
import summer.base.manager.config.Cheats;

public class AutoArmor extends Cheats {
    public Minecraft mc = Minecraft.getMinecraft();
    private TimerUtils timer;
    private TimerUtils dropTimer;
    private boolean dropping;
    public Setting delay;
    public Setting drop;
    public Setting openInv;

    public AutoArmor() {
        super("AutoArmor", "Automatically puts on the best armor", Selection.PLAYER, false);
        this.timer = new TimerUtils();
        this.dropTimer = new TimerUtils();
    }

    public void onSetup() {
        Summer.INSTANCE.settingsManager.Property(delay = new Setting("Delay", this, 3.0D, 0.0D, 10.0D, true));
        Summer.INSTANCE.settingsManager.Property(drop = new Setting("Drop", this, true));
        Summer.INSTANCE.settingsManager.Property(openInv = new Setting("OpenInv", this, false));
    }

    @EventTarget
    public void onTick(EventClientTick e) {
        String mode;
        if (!isToggled())
            return;
        if (openInv.getValBoolean()) {
            mode = "OpenInv";
        } else {
            mode = "Basic";
        }
        long delayValue = delay.getValLong() * 50L;
        if (mode.equalsIgnoreCase("OpenInv")
                && !(this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory))
            return;
        if (this.mc.currentScreen == null
                || this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory
                || this.mc.currentScreen instanceof net.minecraft.client.gui.GuiChat) {
            if (this.timer.hasReached(delayValue))
                getBestArmor();
            if (!this.dropping) {
                if ((this.mc.currentScreen == null
                        || this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory
                        || this.mc.currentScreen instanceof net.minecraft.client.gui.GuiChat)
                        && this.timer.hasReached(delayValue))
                    getBestArmor();
            } else if (this.dropTimer.hasReached(delayValue)) {
                this.mc.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, -999, 0, 0,
                        (EntityPlayer) Minecraft.thePlayer);
                this.dropTimer.reset();
                this.dropping = false;
            }
        }
    }

    public void getBestArmor() {
        for (int type = 1; type < 5; type++) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                if (isBestArmor(is, type))
                    continue;
                if (drop.getValBoolean()) {
                    drop(4 + type);
                } else {
                    shiftClick(4 + type);
                }
            }
            for (int i = 9; i < 45; i++) {
                if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack is2 = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (isBestArmor(is2, type) && getProtection(is2) > 0.0F) {
                        shiftClick(i);
                        this.timer.reset();
                        if (delay.getValLong() > 0L)
                            return;
                    }
                }
            }
        }
        if (drop.getValBoolean())
            for (int j = 9; j < 45; j++) {
                if (Minecraft.thePlayer.inventoryContainer.getSlot(j).getHasStack()) {
                    ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(j).getStack();
                    if ((getProtection(is) > 0.0F || isDuplicate(is, j)) && !this.dropping
                            && is.getItem() instanceof ItemArmor)
                        drop(j);
                }
            }
    }

    public boolean isDuplicate(ItemStack stack, int slot) {
        for (int i = 0; i < 45; i++) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is != stack && slot != i && getProtection(stack) == getProtection(is)
                        && is.getItem() instanceof ItemArmor && !(is.getItem() instanceof net.minecraft.item.ItemPotion)
                        && !(is.getItem() instanceof net.minecraft.item.ItemBlock))
                    return true;
            }
        }
        return false;
    }

    public static boolean isBestArmor(ItemStack stack, int type) {
        float prot = getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "helmet";
        } else if (type == 2) {
            strType = "chestplate";
        } else if (type == 3) {
            strType = "leggings";
        } else if (type == 4) {
            strType = "boots";
        }
        if (!stack.getUnlocalizedName().contains(strType))
            return false;
        for (int i = 5; i < 45; i++) {
            Minecraft mc = Minecraft.getMinecraft();
            if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType))
                    return false;
            }
        }
        return true;
    }

    public void shiftClick(int slot) {
        this.mc.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, 0, 1,
                (EntityPlayer) Minecraft.thePlayer);
    }

    public void drop(int slot) {
        if (drop.getValBoolean()
                && this.dropTimer.hasReached((delay.getValLong() * 50L))
                && !this.dropping) {
            this.mc.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, 0, 0,
                    (EntityPlayer) Minecraft.thePlayer);
            this.dropping = true;
            this.dropTimer.reset();
        }
    }

    public static float getProtection(ItemStack stack) {
        float prot = 0.0F;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.getItem();
            prot += (float) (armor.damageReduceAmount + ((100 - armor.damageReduceAmount)
                    * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075D);
            prot += (float) (EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)
                    / 100.0D);
            prot += (float) (EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)
                    / 100.0D);
            prot += (float) (EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0D);
            prot += (float) (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0D);
            prot += (float) (EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack)
                    / 100.0D);
        }
        return prot;
    }
}