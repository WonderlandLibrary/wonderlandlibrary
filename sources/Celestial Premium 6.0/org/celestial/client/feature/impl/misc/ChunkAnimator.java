/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.render.EventRenderChunk;
import org.celestial.client.event.events.impl.render.EventRenderChunkContainer;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.NumberSetting;

public class ChunkAnimator
extends Feature {
    private final WeakHashMap<RenderChunk, AtomicLong> lifespans = new WeakHashMap();
    private final NumberSetting animateDelay = new NumberSetting("Animate Delay", 1500.0f, 100.0f, 5000.0f, 100.0f, () -> true);

    public ChunkAnimator() {
        super("ChunkAnimator", "\u0410\u043d\u0438\u043c\u0438\u0440\u0443\u0435\u0442 \u0447\u0430\u043d\u043a\u0438", Type.Misc);
        this.addSettings(this.animateDelay);
    }

    private double easeOutCubic(double t) {
        return (t -= 1.0) * t * t + 1.0;
    }

    @EventTarget
    public void onChunkRender(EventRenderChunk event) {
        if (Minecraft.getMinecraft().player != null && !this.lifespans.containsKey(event.RenderChunk)) {
            this.lifespans.put(event.RenderChunk, new AtomicLong(-1L));
        }
    }

    @EventTarget
    public void onChunkContainer(EventRenderChunkContainer event) {
        if (this.lifespans.containsKey(event.RenderChunk)) {
            long timeDifference;
            AtomicLong timeAlive = this.lifespans.get(event.RenderChunk);
            long timeClone = timeAlive.get();
            if (timeClone == -1L) {
                timeClone = System.currentTimeMillis();
                timeAlive.set(timeClone);
            }
            if ((float)(timeDifference = System.currentTimeMillis() - timeClone) <= this.animateDelay.getCurrentValue()) {
                double chunkY = event.RenderChunk.getPosition().getY();
                double offsetY = chunkY * this.easeOutCubic((float)timeDifference / this.animateDelay.getCurrentValue());
                GlStateManager.translate(0.0, -chunkY + offsetY, 0.0);
            }
        }
    }
}

