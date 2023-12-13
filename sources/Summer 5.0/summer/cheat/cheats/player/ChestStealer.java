package summer.cheat.cheats.player;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.base.utilities.ItemUtil;
import summer.base.utilities.TimerUtils;
import summer.base.manager.config.Cheats;

public class ChestStealer extends Cheats {
    TimerUtils timer;
    double delay;
    private boolean checkNameOption;
    public Minecraft mc = Minecraft.getMinecraft();

    private Setting minDelay;
    private Setting maxDelay;
    private Setting checkName;

    public ChestStealer() {
        super("ChestStealer", "Steals the best out of the chest", Selection.PLAYER, false);
        this.timer = new TimerUtils();
    }

    @Override
    public void onSetup() {
        Summer.INSTANCE.settingsManager.Property(minDelay = new Setting("MinDelay", this, 130.0, 0.0, 500.0, true));
        Summer.INSTANCE.settingsManager.Property(maxDelay = new Setting("MaxDelay", this, 180.0, 0.0, 500.0, true));
        Summer.INSTANCE.settingsManager.Property(checkName = new Setting("CheckName", this, true));

    }

    @Override
    public void onEnable() {
        this.setDelay();
        super.onEnable();
    }

    public void setDelay() {
        final double min = minDelay.getValDouble();
        double max = maxDelay.getValDouble();
        if (min == max) {
            max = min * 1.1;
        }
        this.delay = ThreadLocalRandom.current().nextDouble(Math.min(min, max), Math.max(min, max));
    }

    @EventTarget
    public void onUpdate(final EventUpdate e) {
        this.checkNameOption = checkName.getValBoolean();
        final int min = minDelay.getValInt();
        final int max = maxDelay.getValInt();
        if (e.isPre() && mc.currentScreen instanceof GuiChest) {
            final GuiChest chest = (GuiChest) mc.currentScreen;
            if ((chest.getLowerChestInventory().getName().toLowerCase().contains("menu")
                    || chest.getLowerChestInventory().getName().toLowerCase().contains("play")) && this.checkNameOption) {
                return;
            }
            if (this.isChestEmpty(chest) || this.isInventoryFull()) {
                Minecraft.thePlayer.closeScreen();
                return;
            }
            for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
                final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                if (stack != null && this.timer.hasReached(this.delay) && this.isValidItem(stack)) {
                    mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, Minecraft.thePlayer);
                    this.setDelay();
                    this.timer.reset();
                    break;
                }
            }
        }
    }

    private boolean isValidItem(final ItemStack stack) {
        return stack != null && ((ItemUtil.compareDamage(stack, ItemUtil.bestSword()) != null
                && ItemUtil.compareDamage(stack, ItemUtil.bestSword()) == stack) || stack.getItem() instanceof ItemBlock
                || (stack.getItem() instanceof ItemPotion && !ItemUtil.isBadPotion(stack))
                || stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemAppleGold
                || stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemSword);
    }

    private boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
            final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (stack != null && this.isValidItem(stack)) {
                return false;
            }
        }
        return true;
    }

    private boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }
}
