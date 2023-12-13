package best.azura.client.impl.module.impl.combat.autoheal;

import best.azura.eventbus.core.Event;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.api.value.Value;
import net.minecraft.client.Minecraft;

import java.util.List;

public abstract class HealSub {
    protected final Minecraft mc = Minecraft.getMinecraft();
    public abstract String getName();
    public abstract List<Value<?>> getValues();
    public abstract void handle(Event event);
    public BooleanValue enabledValue;
    public HealSub() {
        enabledValue = new BooleanValue(getName(), "Check if " + getName() + " is enabled", true);
    }
}