package net.smoothboot.client.events.impl;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class RenderWorldEvent {
    public MatrixStack matrices;
    public float tickDelta;
    public long limitTime;
    public boolean renderBlockOutline;
    public Camera camera;
    public GameRenderer gameRenderer;
    public LightmapTextureManager arg4;
    public Matrix4f arg5;
    public CallbackInfo ci;

    public RenderWorldEvent(MatrixStack mStack, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager arg4, Matrix4f arg5, CallbackInfo ci) {
        this.matrices = mStack;
        this.tickDelta = tickDelta;
        this.renderBlockOutline = renderBlockOutline;
        this.camera = camera;
        this.gameRenderer = gameRenderer;

        this.arg4 = arg4;
        this.arg5 = arg5;

        this.ci = ci;
    }
}
