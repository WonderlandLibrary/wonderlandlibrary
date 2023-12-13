package dev.tenacity.event.impl.render;

import dev.tenacity.event.Event;
import lombok.AllArgsConstructor;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.util.function.BiConsumer;

@AllArgsConstructor
public class CustomBlockRenderEvent extends Event {

    private final BiConsumer<Float, Float> transformFirstPersonItem;
    private final Runnable doBlockTransformations;
    private final float swingProgress, equipProgress;

    @Exclude(Strategy.NAME_REMAPPING)
    public float getSwingProgress() {
        return swingProgress;
    }

    @Exclude(Strategy.NAME_REMAPPING)
    public float getEquipProgress() {
        return equipProgress;
    }

    @Exclude(Strategy.NAME_REMAPPING)
    public void transformFirstPersonItem(float equipProgress, float swingProgress) {
        this.transformFirstPersonItem.accept(equipProgress, swingProgress);
    }

    @Exclude(Strategy.NAME_REMAPPING)
    public void doBlockTransformations() {
        this.doBlockTransformations.run();
    }

}
