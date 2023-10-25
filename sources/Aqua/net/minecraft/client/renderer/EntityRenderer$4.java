package net.minecraft.client.renderer;

import java.util.concurrent.Callable;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;

/*
 * Exception performing whole class analysis ignored.
 */
class EntityRenderer.4
implements Callable<String> {
    final /* synthetic */ ScaledResolution val$scaledresolution;

    EntityRenderer.4(ScaledResolution scaledResolution) {
        this.val$scaledresolution = scaledResolution;
    }

    public String call() throws Exception {
        return String.format((String)"Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", (Object[])new Object[]{this.val$scaledresolution.getScaledWidth(), this.val$scaledresolution.getScaledHeight(), EntityRenderer.access$000((EntityRenderer)EntityRenderer.this).displayWidth, EntityRenderer.access$000((EntityRenderer)EntityRenderer.this).displayHeight, this.val$scaledresolution.getScaleFactor()});
    }
}
