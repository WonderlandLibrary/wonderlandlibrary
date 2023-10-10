package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.gui.MapItemRenderer.1;
import net.minecraft.client.gui.MapItemRenderer.Instance;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;

public class MapItemRenderer {
   private static final ResourceLocation mapIcons = new ResourceLocation("textures/map/map_icons.png");
   private final TextureManager textureManager;
   private final Map loadedMaps = Maps.newHashMap();

   public MapItemRenderer(TextureManager textureManagerIn) {
      this.textureManager = textureManagerIn;
   }

   // $FF: synthetic method
   static TextureManager access$400(MapItemRenderer x0) {
      return x0.textureManager;
   }

   // $FF: synthetic method
   static ResourceLocation access$500() {
      return mapIcons;
   }

   public void clearLoadedMaps() {
      for(Instance mapitemrenderer$instance : this.loadedMaps.values()) {
         this.textureManager.deleteTexture(Instance.access$300(mapitemrenderer$instance));
      }

      this.loadedMaps.clear();
   }

   private Instance getMapRendererInstance(MapData mapdataIn) {
      Instance mapitemrenderer$instance = (Instance)this.loadedMaps.get(mapdataIn.mapName);
      if(mapitemrenderer$instance == null) {
         mapitemrenderer$instance = new Instance(this, mapdataIn, (1)null);
         this.loadedMaps.put(mapdataIn.mapName, mapitemrenderer$instance);
      }

      return mapitemrenderer$instance;
   }

   public void renderMap(MapData mapdataIn, boolean p_148250_2_) {
      Instance.access$100(this.getMapRendererInstance(mapdataIn), p_148250_2_);
   }

   public void updateMapTexture(MapData mapdataIn) {
      Instance.access$000(this.getMapRendererInstance(mapdataIn));
   }
}
