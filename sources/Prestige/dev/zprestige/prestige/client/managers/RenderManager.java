/*
when sex mod for prestige clarinet? - cloovey
*/
package dev.zprestige.prestige.client.managers;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.Priority;
import dev.zprestige.prestige.client.event.impl.Render2DEvent;

public class RenderManager {
    public long ms;
    public long lastMs;

    public RenderManager() {
        Prestige.Companion.getEventBus().registerListener(this);
    }

    @EventListener(getPriority=Priority.HIGHEST)
    public void event(Render2DEvent event) {
        ms = System.currentTimeMillis() - lastMs;
        lastMs = System.currentTimeMillis();
        if (ms > 30) {
            ms = 0L;
        }
    }

    public float getMs() {
        return (float)this.ms * 0.005f;
    }
}
