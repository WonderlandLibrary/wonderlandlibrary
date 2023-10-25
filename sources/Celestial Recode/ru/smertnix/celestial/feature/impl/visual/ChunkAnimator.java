package ru.smertnix.celestial.feature.impl.visual;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.EventRenderChunk;
import ru.smertnix.celestial.event.events.EventRenderChunkContainer;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;

public class ChunkAnimator extends Feature {

    private final HashMap<RenderChunk, AtomicLong> renderChunkMap = new HashMap<>();

    public ChunkAnimator() {
        super("ChunkAnimator", "Анимирует все чанки в радиусе полезрения ", FeatureCategory.Render);
    }

    private double easeOutCubic(double t) {
        return (--t) * t * t + 1;
    }

    @EventTarget
    private void onRenderChunk(EventRenderChunk event) {
        if (mc.player != null) {
            if (!renderChunkMap.containsKey(event.getRenderChunk())) {
                renderChunkMap.put(event.getRenderChunk(), new AtomicLong(-1L));
            }
        }
    }

    @EventTarget
    private void onChunkRender(EventRenderChunkContainer event) {
        if (renderChunkMap.containsKey(event.getRenderChunk())) {
            AtomicLong timeAlive = renderChunkMap.get(event.getRenderChunk());
            long timeClone = timeAlive.get();
            if (timeClone == -1L) {
                timeClone = System.currentTimeMillis();
                timeAlive.set(timeClone);
            }

            long timeDifference = System.currentTimeMillis() - timeClone;
            if (timeDifference <= 250) {
                double chunkY = event.getRenderChunk().getPosition().getY();
                double offsetY;
                offsetY = chunkY * easeOutCubic(timeDifference / 250F);
                GlStateManager.translate(0.0, -chunkY + offsetY, 0.0);
            }
        }
    }
}
