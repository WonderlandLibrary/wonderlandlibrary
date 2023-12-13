package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;

public class MapItemRenderer {
   private static final ResourceLocation mapIcons = new ResourceLocation("textures/map/map_icons.png");
   private final TextureManager textureManager;
   private final Map loadedMaps = Maps.newHashMap();

   public void clearLoadedMaps() {
      Iterator var2 = this.loadedMaps.values().iterator();

      while(var2.hasNext()) {
         MapItemRenderer.Instance var1 = (MapItemRenderer.Instance)var2.next();
         this.textureManager.deleteTexture(MapItemRenderer.Instance.access$3(var1));
      }

      this.loadedMaps.clear();
   }

   public MapItemRenderer(TextureManager var1) {
      this.textureManager = var1;
   }

   private MapItemRenderer.Instance getMapRendererInstance(MapData var1) {
      MapItemRenderer.Instance var2 = (MapItemRenderer.Instance)this.loadedMaps.get(var1.mapName);
      if (var2 == null) {
         var2 = new MapItemRenderer.Instance(this, var1, (MapItemRenderer.Instance)null);
         this.loadedMaps.put(var1.mapName, var2);
      }

      return var2;
   }

   public void updateMapTexture(MapData var1) {
      MapItemRenderer.Instance.access$0(this.getMapRendererInstance(var1));
   }

   public void renderMap(MapData var1, boolean var2) {
      MapItemRenderer.Instance.access$1(this.getMapRendererInstance(var1), var2);
   }

   static TextureManager access$0(MapItemRenderer var0) {
      return var0.textureManager;
   }

   static ResourceLocation access$1() {
      return mapIcons;
   }

   class Instance {
      private final MapData mapData;
      private final ResourceLocation location;
      final MapItemRenderer this$0;
      private final int[] mapTextureData;
      private final DynamicTexture mapTexture;

      private void updateMapTexture() {
         for(int var1 = 0; var1 < 16384; ++var1) {
            int var2 = this.mapData.colors[var1] & 255;
            if (var2 / 4 == 0) {
               this.mapTextureData[var1] = (var1 + var1 / 128 & 1) * 8 + 16 << 24;
            } else {
               this.mapTextureData[var1] = MapColor.mapColorArray[var2 / 4].func_151643_b(var2 & 3);
            }
         }

         this.mapTexture.updateDynamicTexture();
      }

      static void access$0(MapItemRenderer.Instance var0) {
         var0.updateMapTexture();
      }

      static ResourceLocation access$3(MapItemRenderer.Instance var0) {
         return var0.location;
      }

      Instance(MapItemRenderer var1, MapData var2, MapItemRenderer.Instance var3) {
         this(var1, var2);
      }

      private Instance(MapItemRenderer var1, MapData var2) {
         this.this$0 = var1;
         this.mapData = var2;
         this.mapTexture = new DynamicTexture(128, 128);
         this.mapTextureData = this.mapTexture.getTextureData();
         this.location = MapItemRenderer.access$0(var1).getDynamicTextureLocation("map/" + var2.mapName, this.mapTexture);

         for(int var3 = 0; var3 < this.mapTextureData.length; ++var3) {
            this.mapTextureData[var3] = 0;
         }

      }

      private void render(boolean var1) {
         byte var2 = 0;
         byte var3 = 0;
         Tessellator var4 = Tessellator.getInstance();
         WorldRenderer var5 = var4.getWorldRenderer();
         float var6 = 0.0F;
         MapItemRenderer.access$0(this.this$0).bindTexture(this.location);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(1, 771, 0, 1);
         GlStateManager.disableAlpha();
         var5.begin(7, DefaultVertexFormats.POSITION_TEX);
         var5.pos((double)((float)(var2 + 0) + var6), (double)((float)(var3 + 128) - var6), -0.009999999776482582D).tex(0.0D, 1.0D).endVertex();
         var5.pos((double)((float)(var2 + 128) - var6), (double)((float)(var3 + 128) - var6), -0.009999999776482582D).tex(1.0D, 1.0D).endVertex();
         var5.pos((double)((float)(var2 + 128) - var6), (double)((float)(var3 + 0) + var6), -0.009999999776482582D).tex(1.0D, 0.0D).endVertex();
         var5.pos((double)((float)(var2 + 0) + var6), (double)((float)(var3 + 0) + var6), -0.009999999776482582D).tex(0.0D, 0.0D).endVertex();
         var4.draw();
         GlStateManager.enableAlpha();
         GlStateManager.disableBlend();
         MapItemRenderer.access$0(this.this$0).bindTexture(MapItemRenderer.access$1());
         int var7 = 0;
         Iterator var9 = this.mapData.mapDecorations.values().iterator();

         while(true) {
            Vec4b var8;
            do {
               if (!var9.hasNext()) {
                  GlStateManager.pushMatrix();
                  GlStateManager.translate(0.0F, 0.0F, -0.04F);
                  GlStateManager.scale(1.0F, 1.0F, 1.0F);
                  GlStateManager.popMatrix();
                  return;
               }

               var8 = (Vec4b)var9.next();
            } while(var1 && var8.func_176110_a() != 1);

            GlStateManager.pushMatrix();
            GlStateManager.translate((float)var2 + (float)var8.func_176112_b() / 2.0F + 64.0F, (float)var3 + (float)var8.func_176113_c() / 2.0F + 64.0F, -0.02F);
            GlStateManager.rotate((float)(var8.func_176111_d() * 360) / 16.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.scale(4.0F, 4.0F, 3.0F);
            GlStateManager.translate(-0.125F, 0.125F, 0.0F);
            byte var10 = var8.func_176110_a();
            float var11 = (float)(var10 % 4 + 0) / 4.0F;
            float var12 = (float)(var10 / 4 + 0) / 4.0F;
            float var13 = (float)(var10 % 4 + 1) / 4.0F;
            float var14 = (float)(var10 / 4 + 1) / 4.0F;
            var5.begin(7, DefaultVertexFormats.POSITION_TEX);
            float var15 = -0.001F;
            var5.pos(-1.0D, 1.0D, (double)((float)var7 * -0.001F)).tex((double)var11, (double)var12).endVertex();
            var5.pos(1.0D, 1.0D, (double)((float)var7 * -0.001F)).tex((double)var13, (double)var12).endVertex();
            var5.pos(1.0D, -1.0D, (double)((float)var7 * -0.001F)).tex((double)var13, (double)var14).endVertex();
            var5.pos(-1.0D, -1.0D, (double)((float)var7 * -0.001F)).tex((double)var11, (double)var14).endVertex();
            var4.draw();
            GlStateManager.popMatrix();
            ++var7;
         }
      }

      static void access$1(MapItemRenderer.Instance var0, boolean var1) {
         var0.render(var1);
      }
   }
}
