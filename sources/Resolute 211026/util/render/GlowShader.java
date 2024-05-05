// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

import org.lwjgl.opengl.GL20;

public final class GlowShader extends FramebufferShader
{
    public static final GlowShader GLOW_SHADER;
    
    public GlowShader() {
        super("glow.frag");
    }
    
    @Override
    public void setupUniforms() {
        this.setupUniform("texture");
        this.setupUniform("texelSize");
        this.setupUniform("color");
        this.setupUniform("divider");
        this.setupUniform("radius");
        this.setupUniform("maxSample");
    }
    
    @Override
    public void updateUniforms() {
        GL20.glUniform1i(this.getUniform("texture"), 0);
        GL20.glUniform2f(this.getUniform("texelSize"), 1.0f / this.mc.displayWidth * this.radius * this.quality, 1.0f / this.mc.displayHeight * this.radius * this.quality);
        GL20.glUniform3f(this.getUniform("color"), this.red, this.green, this.blue);
        GL20.glUniform1f(this.getUniform("divider"), 140.0f);
        GL20.glUniform1f(this.getUniform("radius"), this.radius);
        GL20.glUniform1f(this.getUniform("maxSample"), 10.0f);
    }
    
    static {
        GLOW_SHADER = new GlowShader();
    }
}
