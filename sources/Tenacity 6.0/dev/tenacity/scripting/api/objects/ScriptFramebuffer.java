// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api.objects;

import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.shader.Framebuffer;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;

@Exclude({ Strategy.NAME_REMAPPING })
public class ScriptFramebuffer
{
    private Framebuffer framebuffer;
    
    public ScriptFramebuffer() {
        this.framebuffer = new Framebuffer(1, 1, true);
    }
    
    public void resize() {
        this.framebuffer = RenderUtil.createFrameBuffer(this.framebuffer, true);
    }
    
    public void bind() {
        this.framebuffer.bindFramebuffer(false);
    }
    
    public void clear() {
        this.framebuffer.framebufferClear();
    }
    
    public void unbind() {
        this.framebuffer.unbindFramebuffer();
    }
    
    public int getTextureID() {
        return this.framebuffer.framebufferTexture;
    }
    
    public int getWidth() {
        return this.framebuffer.framebufferWidth;
    }
    
    public int getHeight() {
        return this.framebuffer.framebufferHeight;
    }
}
