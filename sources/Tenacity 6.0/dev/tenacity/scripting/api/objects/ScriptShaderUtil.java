// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api.objects;

import dev.tenacity.utils.render.ShaderUtil;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;

@Exclude({ Strategy.NAME_REMAPPING })
public class ScriptShaderUtil
{
    private final ShaderUtil shaderUtil;
    
    public ScriptShaderUtil(final String fragSource) {
        this.shaderUtil = new ShaderUtil(fragSource, false);
    }
    
    public void init() {
        this.shaderUtil.init();
    }
    
    public void unload() {
        this.shaderUtil.unload();
    }
    
    public void setUniformf(final String name, final float... args) {
        this.shaderUtil.setUniformf(name, args);
    }
    
    public void setUniformi(final String name, final int... args) {
        this.shaderUtil.setUniformi(name, args);
    }
    
    public void drawQuads() {
        ShaderUtil.drawQuads();
    }
    
    public void drawQuads(final float x, final float y, final float width, final float height) {
        ShaderUtil.drawQuads(x, y, width, height);
    }
}
