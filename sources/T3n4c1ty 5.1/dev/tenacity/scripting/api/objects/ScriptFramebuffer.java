package dev.tenacity.scripting.api.objects;

import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.shader.Framebuffer;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude(Strategy.NAME_REMAPPING)
public class ScriptFramebuffer {

    private Framebuffer framebuffer;

    public ScriptFramebuffer() {
        this.framebuffer = new Framebuffer(1, 1, true);
    }


    public void resize() {
        framebuffer = RenderUtil.createFrameBuffer(framebuffer, true);
    }

    public void bind() {
        framebuffer.bindFramebuffer(false);
    }

    public void clear() {
        framebuffer.framebufferClear();
    }

    public void unbind() {
        framebuffer.unbindFramebuffer();
    }

    public int getTextureID() {
        return framebuffer.framebufferTexture;
    }

    public int getWidth() {
        return framebuffer.framebufferWidth;
    }

    public int getHeight() {
        return framebuffer.framebufferHeight;
    }


}
