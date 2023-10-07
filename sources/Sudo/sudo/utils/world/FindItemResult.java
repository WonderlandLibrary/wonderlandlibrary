package sudo.utils.world;


import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;

public class FindItemResult {
	public static MinecraftClient mc = MinecraftClient.getInstance();

    private final int slot, count;

    public FindItemResult(int slot, int count) {
        this.slot = slot;
        this.count = count;
    }

    public int getSlot() {
        return slot;
    }

    public int getCount() {
        return count;
    }

    public boolean found() {
        return slot != -1;
    }

    public Hand getHand() {
        if (slot == InventoryUtils.OFFHAND) return Hand.OFF_HAND;
        else if (slot == mc.player.getInventory().selectedSlot) return Hand.MAIN_HAND;
        return null;
    }

    public boolean isMainHand() {
        return getHand() == Hand.MAIN_HAND;
    }

    public boolean isOffhand() {
        return getHand() == Hand.OFF_HAND;
    }

    public boolean isHotbar() {
        return slot >= InventoryUtils.HOTBAR_START && slot <= InventoryUtils.HOTBAR_END;
    }

    public boolean isMain() {
        return slot >= InventoryUtils.MAIN_START && slot <= InventoryUtils.MAIN_END;
    }

    public boolean isArmor() {
        return slot >= InventoryUtils.ARMOR_START && slot <= InventoryUtils.ARMOR_END;
    }
}