package dev.zprestige.prestige.client.event.impl;

import dev.zprestige.prestige.client.event.Event;
import net.minecraft.client.util.math.MatrixStack;

public class Render3DEvent extends Event {
    public MatrixStack matrixStack;
    public float tickDelta;

    public Render3DEvent(MatrixStack matrixStack, float tickDelta) {
        this.matrixStack = matrixStack;
        this.tickDelta = tickDelta;
    }

    public MatrixStack getMatrixStack() {
        return this.matrixStack;
    }

    public float getTickDelta() {
        return this.tickDelta;
    }
}
