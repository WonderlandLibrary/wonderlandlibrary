package net.caffeinemc.phosphor.api.event.events;

import net.caffeinemc.phosphor.api.event.Cancellable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;

@SuppressWarnings("all")
public class SlotCheckEvent extends Cancellable {
    private static final SlotCheckEvent INSTANCE = new SlotCheckEvent();

    public boolean cancelOutput = false;

    public void setCancelOutput(boolean cancelOutput) {
        this.cancelOutput = cancelOutput;
        cancel();
    }

    public HandledScreen instance;
    public Slot slot;

    public static SlotCheckEvent get(HandledScreen instance, Slot slot) {
        INSTANCE.setCancelled(false);
        INSTANCE.instance = instance;
        INSTANCE.slot = slot;
        return INSTANCE;
    }
}
