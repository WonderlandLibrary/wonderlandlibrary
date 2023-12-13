// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer;

public class Tessellator
{
    private WorldRenderer worldRenderer;
    private WorldVertexBufferUploader field_178182_b;
    private static final Tessellator instance;
    private static final String __OBFID = "CL_00000960";
    
    public static Tessellator getInstance() {
        return Tessellator.instance;
    }
    
    public Tessellator(final int p_i1250_1_) {
        this.field_178182_b = new WorldVertexBufferUploader();
        this.worldRenderer = new WorldRenderer(p_i1250_1_);
    }
    
    public int draw() {
        return this.field_178182_b.draw(this.worldRenderer, this.worldRenderer.draw());
    }
    
    public WorldRenderer getWorldRenderer() {
        return this.worldRenderer;
    }
    
    static {
        instance = new Tessellator(2097152);
    }
}
