// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.objects;

import dev.tenacity.utils.render.ShaderUtil;

public class Mask
{
    private final ShaderUtil maskShader;
    
    public Mask() {
        this.maskShader = new ShaderUtil("Tenacity/Shaders/mask.frag");
    }
}
