package io.github.nevalackin.client.event.render.item;

import io.github.nevalackin.client.event.Event;

public class ApplyBlockTransformationEvent implements Event {

    private final TransformItem originalTransform;
    private TransformItem replacementTransform;

    public ApplyBlockTransformationEvent(TransformItem originalTransform) {
        this.originalTransform = originalTransform;
        this.replacementTransform = originalTransform;
    }

    public TransformItem getOriginalTransform() {
        return originalTransform;
    }

    public TransformItem getReplacementTransform() {
        return replacementTransform;
    }

    public void setReplacementTransform(TransformItem replacementTransform) {
        this.replacementTransform = replacementTransform;
    }

    @FunctionalInterface
    public interface TransformItem {
        void transform(float equipProgress, float swingProgress);
    }
}
