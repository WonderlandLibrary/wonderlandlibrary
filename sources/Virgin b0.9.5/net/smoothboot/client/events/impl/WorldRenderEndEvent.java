package net.smoothboot.client.events.impl;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.smoothboot.client.mixinterface.IDispatch;
import net.smoothboot.client.mixinterface.ISubWorldRenderEnd;
import net.smoothboot.client.mixinterface.Subscriptions;

public class WorldRenderEndEvent {
    public static void init() {
        WorldRenderEvents.END.register(WorldRenderEndEvent::dispatchEnd);
    }

    private static void dispatchEnd(WorldRenderContext ctx) {
        for (ISubWorldRenderEnd iswre : Subscriptions.WORLD_RENDER_END) {
            if (iswre instanceof IDispatch d && !d.shouldDispatch()) continue;
            iswre.onWorldRenderEnd(ctx);
        }
    }
}
