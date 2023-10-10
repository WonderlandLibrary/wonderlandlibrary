package vestige.handler.client;

import lombok.Getter;
import net.minecraft.item.ItemStack;
import vestige.util.IMinecraft;

public class SlotSpoofHandler implements IMinecraft {

    private int spoofedSlot;

    @Getter
    private boolean spoofing;

    public void startSpoofing(int slot) {
        this.spoofing = true;
        this.spoofedSlot = slot;
    }

    public void stopSpoofing() {
        this.spoofing = false;
    }

    public int getSpoofedSlot() {
        return spoofing ? spoofedSlot : mc.thePlayer.inventory.currentItem;
    }

    public ItemStack getSpoofedStack() {
        return spoofing ? mc.thePlayer.inventory.getStackInSlot(spoofedSlot) : mc.thePlayer.inventory.getCurrentItem();
    }

}
