// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.realms;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;

public class Tezzelator
{
    public static Tessellator t;
    public static final Tezzelator instance;
    private static final String __OBFID = "CL_00001855";
    
    public int end() {
        return Tezzelator.t.draw();
    }
    
    public void vertex(final double p_vertex_1_, final double p_vertex_3_, final double p_vertex_5_) {
        Tezzelator.t.getWorldRenderer().addVertex(p_vertex_1_, p_vertex_3_, p_vertex_5_);
    }
    
    public void color(final float p_color_1_, final float p_color_2_, final float p_color_3_, final float p_color_4_) {
        Tezzelator.t.getWorldRenderer().setColorRGBA(p_color_1_, p_color_2_, p_color_3_, p_color_4_);
    }
    
    public void color(final int p_color_1_, final int p_color_2_, final int p_color_3_) {
        Tezzelator.t.getWorldRenderer().setPosition(p_color_1_, p_color_2_, p_color_3_);
    }
    
    public void tex2(final int p_tex2_1_) {
        Tezzelator.t.getWorldRenderer().func_178963_b(p_tex2_1_);
    }
    
    public void normal(final float p_normal_1_, final float p_normal_2_, final float p_normal_3_) {
        Tezzelator.t.getWorldRenderer().func_178980_d(p_normal_1_, p_normal_2_, p_normal_3_);
    }
    
    public void noColor() {
        Tezzelator.t.getWorldRenderer().markDirty();
    }
    
    public void color(final int p_color_1_) {
        Tezzelator.t.getWorldRenderer().func_178991_c(p_color_1_);
    }
    
    public void color(final float p_color_1_, final float p_color_2_, final float p_color_3_) {
        Tezzelator.t.getWorldRenderer().func_178986_b(p_color_1_, p_color_2_, p_color_3_);
    }
    
    public WorldRenderer.State sortQuads(final float p_sortQuads_1_, final float p_sortQuads_2_, final float p_sortQuads_3_) {
        return Tezzelator.t.getWorldRenderer().getVertexState(p_sortQuads_1_, p_sortQuads_2_, p_sortQuads_3_);
    }
    
    public void restoreState(final WorldRenderer.State p_restoreState_1_) {
        Tezzelator.t.getWorldRenderer().setVertexState(p_restoreState_1_);
    }
    
    public void begin(final int p_begin_1_) {
        Tezzelator.t.getWorldRenderer().startDrawing(p_begin_1_);
    }
    
    public void begin() {
        Tezzelator.t.getWorldRenderer().startDrawingQuads();
    }
    
    public void vertexUV(final double p_vertexUV_1_, final double p_vertexUV_3_, final double p_vertexUV_5_, final double p_vertexUV_7_, final double p_vertexUV_9_) {
        Tezzelator.t.getWorldRenderer().addVertexWithUV(p_vertexUV_1_, p_vertexUV_3_, p_vertexUV_5_, p_vertexUV_7_, p_vertexUV_9_);
    }
    
    public void color(final int p_color_1_, final int p_color_2_) {
        Tezzelator.t.getWorldRenderer().drawColor(p_color_1_, p_color_2_);
    }
    
    public void offset(final double p_offset_1_, final double p_offset_3_, final double p_offset_5_) {
        Tezzelator.t.getWorldRenderer().setTranslation(p_offset_1_, p_offset_3_, p_offset_5_);
    }
    
    public void color(final int p_color_1_, final int p_color_2_, final int p_color_3_, final int p_color_4_) {
        Tezzelator.t.getWorldRenderer().func_178961_b(p_color_1_, p_color_2_, p_color_3_, p_color_4_);
    }
    
    public void tex(final double p_tex_1_, final double p_tex_3_) {
        Tezzelator.t.getWorldRenderer().setTextureUV(p_tex_1_, p_tex_3_);
    }
    
    public void color(final byte p_color_1_, final byte p_color_2_, final byte p_color_3_) {
        Tezzelator.t.getWorldRenderer().func_178982_a(p_color_1_, p_color_2_, p_color_3_);
    }
    
    static {
        Tezzelator.t = Tessellator.getInstance();
        instance = new Tezzelator();
    }
}
