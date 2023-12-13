/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.EnumWorldBlockLayer;
import optifine.Config;
import org.lwjgl.opengl.GL11;
import shadersmod.client.ShadersRender;

public class VboRenderList
extends ChunkRenderContainer {
    @Override
    public void func_178001_a(EnumWorldBlockLayer p_178001_1_) {
        if (this.field_178007_b) {
            for (RenderChunk var3 : this.field_178009_a) {
                VertexBuffer var4 = var3.func_178565_b(p_178001_1_.ordinal());
                GlStateManager.pushMatrix();
                this.func_178003_a(var3);
                var3.func_178572_f();
                var4.func_177359_a();
                this.func_178010_a();
                var4.func_177358_a(7);
                GlStateManager.popMatrix();
            }
            OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, 0);
            GlStateManager.func_179117_G();
            this.field_178009_a.clear();
        }
    }

    private void func_178010_a() {
        if (Config.isShaders()) {
            ShadersRender.setupArrayPointersVbo();
        } else {
            GL11.glVertexPointer((int)3, (int)5126, (int)28, (long)0L);
            GL11.glColorPointer((int)4, (int)5121, (int)28, (long)12L);
            GL11.glTexCoordPointer((int)2, (int)5126, (int)28, (long)16L);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glTexCoordPointer((int)2, (int)5122, (int)28, (long)24L);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
        }
    }
}

