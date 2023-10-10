package net.minecraft.client.renderer;

import java.util.Iterator;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumWorldBlockLayer;
import optifine.Config;
import org.lwjgl.opengl.GL11;

public class RenderList extends ChunkRenderContainer {
   private static final String __OBFID = "CL_00000957";

   public void renderChunkLayer(EnumWorldBlockLayer var1) {
      if (this.initialized) {
         if (this.renderChunks.size() == 0) {
            return;
         }

         Iterator var3 = this.renderChunks.iterator();

         while(var3.hasNext()) {
            RenderChunk var2 = (RenderChunk)var3.next();
            ListedRenderChunk var4 = (ListedRenderChunk)var2;
            GlStateManager.pushMatrix();
            this.preRenderChunk(var2);
            GL11.glCallList(var4.getDisplayList(var1, var4.getCompiledChunk()));
            GlStateManager.popMatrix();
         }

         if (Config.isMultiTexture()) {
            GlStateManager.bindCurrentTexture();
         }

         GlStateManager.resetColor();
         this.renderChunks.clear();
      }

   }
}
