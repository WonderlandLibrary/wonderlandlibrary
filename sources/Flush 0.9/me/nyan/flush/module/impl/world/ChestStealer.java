package me.nyan.flush.module.impl.world;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event2D;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.other.Timer;
import me.nyan.flush.utils.player.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class ChestStealer extends Module {
    private final me.nyan.flush.utils.other.Timer timer = new Timer();

    private final NumberSetting delay = new NumberSetting("Delay", this, 80, 0, 300);
    private final BooleanSetting titleCheck = new BooleanSetting("Chest Title Check", this, true),
            autoClose = new BooleanSetting("Auto Close", this, true),
            ignoreJunk = new BooleanSetting("Ignore Junk", this, true);

    public ChestStealer() {
        super("ChestStealer", Category.WORLD);
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        int delay = this.delay.getValueInt() + (this.delay.getValueInt() == 0 ? 0 : MathUtils.getRandomNumber(10, -10));

        if (mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            boolean empty = true;
            if (!isValid(chest)) {
                return;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                mc.thePlayer.closeScreen();
            }

            for (Slot slot : chest.inventorySlots) {
                ItemStack stack = chest.lowerChestInventory.getStackInSlot(slot.slotNumber);
                if (PlayerUtils.isInventoryFull() && autoClose.getValue()) {
                    mc.thePlayer.closeScreen();
                    break;
                }
                if (stack == null || (isBadItem(stack) && ignoreJunk.getValue())) {
                    continue;
                }
                empty = false;
                if (timer.hasTimeElapsed(delay, true)) {
                    mc.playerController.windowClick(chest.windowId, slot.slotNumber, 0, 1, mc.thePlayer);
                }
            }

            if (empty && autoClose.getValue()) {
                mc.thePlayer.closeScreen();
            }
        }
    }

    private boolean isBadItem(ItemStack stack) {
        for (String baditem : PlayerUtils.getBadItems()) {
            if (stack.getItem().getUnlocalizedName().contains(baditem) && !(stack.getItem() instanceof ItemArmor)) {
                return true;
            }
        }

        if (stack.getItem() instanceof ItemPotion) {
            for (PotionEffect effect : ((ItemPotion) stack.getItem()).getEffects(stack)) {
                if (Potion.potionTypes[effect.getPotionID()].isBadEffect()) {
                    return true;
                }
            }
        }

        return Block.getBlockFromItem(stack.getItem()) != null && Block.getBlockFromItem(stack.getItem()) instanceof BlockFlower;
    }

    public boolean isValid(ContainerChest chest) {
        String chestname = EnumChatFormatting.getTextWithoutFormattingCodes(chest.lowerChestInventory.getName()).toLowerCase();
        return !titleCheck.getValue() || (chestname.contains("chest") || chestname.contains("coffre"));
    }

    @Override
    public String getSuffix() {
        return delay.toString();
    }
}