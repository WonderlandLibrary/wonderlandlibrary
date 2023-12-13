package dev.rise.event.api;

import dev.rise.event.EventHandler;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

@Getter
@Setter
public abstract class Event {
    private boolean cancelled;

    public void call() {
        if (Minecraft.getMinecraft().thePlayer != null) EventHandler.handle(this);
    }
}
