package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;
import net.minecraft.item.ItemStack;

public class EventUpdateRenderItem implements NamedEvent {
    public ItemStack stack;
    public float prevEquippedProcess, equippedProcess;
    public EventUpdateRenderItem(ItemStack stack, float prevEquippedProcess, float equippedProcess) {
        this.stack = stack;
        this.prevEquippedProcess = prevEquippedProcess;
        this.equippedProcess = equippedProcess;
    }

    @Override
    public String name() {
        return "updateRenderItem";
    }
}