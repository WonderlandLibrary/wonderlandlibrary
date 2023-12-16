package wtf.expensive.util.world;

import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CEntityActionPacket;
import wtf.expensive.events.impl.player.EventWindowClick;
import wtf.expensive.util.IMinecraft;

public class InventoryUtil implements IMinecraft {

    public static int getHotBarSlot(Item input) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == input) {
                return i;
            }
        }
        return -1;
    }
    public static int getFireWorks() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof FireworkRocketItem) {
                return i;
            }
        }
        return -1;
    }

    public static int getTrident() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof TridentItem) {
                return i;
            }
        }
        return -1;
    }

    public static int getItem(Item item, boolean hotbar) {
        for (int i = 0; i < (hotbar ? 9 : 45); ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static int getSlotInHotBar(Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static int getItemSlot(Item input) {
        for (ItemStack stack : mc.player.getArmorInventoryList()) {
            if (stack.getItem() == input) {
                return -2;
            }
        }
        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack s = mc.player.inventory.getStackInSlot(i);
            if (s.getItem() == input) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot = slot + 36;
        }
        return slot;
    }

    public static int getItemSlot(ItemStack input) {
        for (ItemStack stack : mc.player.getArmorInventoryList()) {
            if (stack == input) {
                return -2;
            }
        }
        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack s = mc.player.inventory.getStackInSlot(i);
            if (s == input) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot = slot + 36;
        }
        return slot;
    }
    public static void handleItemTransfer() {
        int emptySlot;

        if (mc.player.inventory.getItemStack().getItem() != Items.AIR && (emptySlot = findEmptySlot(false)) != -1) {
            mc.playerController.windowClick(0, emptySlot, 0, ClickType.PICKUP, mc.player);
        }

        mc.player.closeScreen();
    }
    public static void handleClick(EventWindowClick windowClick) {

        boolean isSneaking = mc.player.isSneaking();
        if (windowClick.getClickStage() == EventWindowClick.ClickStage.PRE) {
            mc.player.setSprinting(false);
            if (!isSneaking) return;
            mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.RELEASE_SHIFT_KEY));
        }
        if (windowClick.getClickStage() == EventWindowClick.ClickStage.POST) {
            if (!isSneaking) return;
            mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.PRESS_SHIFT_KEY));
        }
    }
    public static int findEmptySlot(boolean isStartingFromZero) {
        int start = isStartingFromZero ? 0 : 9;
        int end = isStartingFromZero ? 9 : 45;

        for (int i = start; i < end; ++i) {
            if (!mc.player.inventory.getStackInSlot(i).isEmpty()) {
                continue;
            }

            return i;
        }

        return -1;
    }
    public static void moveItem(int from, int to, boolean air) {

        if (from == to) return;
        pickupItem(from, 0);
        pickupItem(to, 0);
        if (air)
            pickupItem(from, 0);
    }

    public static void pickupItem(int slot, int button) {
        mc.playerController.windowClick(0, slot, button, ClickType.PICKUP, mc.player);
    }

    public static void dropItem(int slot) {
        mc.playerController.windowClick(0, slot, 0, ClickType.THROW, mc.player);
    }

    public static int getAxe(boolean hotBar) {
        int startSlot = hotBar ? 0 : 9;
        int endSlot = hotBar ? 9 : 36;

        for (int i = startSlot; i < endSlot; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof AxeItem) {
                return i;
            }
        }

        return -1;
    }

    public static int getPearls() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof EnderPearlItem) {
                return i;
            }
        }
        return -1;
    }
}
