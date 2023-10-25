package net.minecraft.client.renderer;

import java.util.concurrent.Callable;
import net.minecraft.client.renderer.EntityRenderer;

/*
 * Exception performing whole class analysis ignored.
 */
class EntityRenderer.2
implements Callable<String> {
    EntityRenderer.2() {
    }

    public String call() throws Exception {
        return EntityRenderer.access$000((EntityRenderer)EntityRenderer.this).currentScreen.getClass().getCanonicalName();
    }
}
