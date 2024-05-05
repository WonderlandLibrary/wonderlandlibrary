package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;

/*
 * Exception performing whole class analysis ignored.
 */
public class MapItemRenderer {
    private static final ResourceLocation mapIcons = new ResourceLocation("textures/map/map_icons.png");
    private final TextureManager textureManager;
    private final Map<String, Instance> loadedMaps = Maps.newHashMap();

    public MapItemRenderer(TextureManager textureManagerIn) {
        this.textureManager = textureManagerIn;
    }

    public void updateMapTexture(MapData mapdataIn) {
        Instance.access$000((Instance)this.getMapRendererInstance(mapdataIn));
    }

    public void renderMap(MapData mapdataIn, boolean p_148250_2_) {
        Instance.access$100((Instance)this.getMapRendererInstance(mapdataIn), (boolean)p_148250_2_);
    }

    private Instance getMapRendererInstance(MapData mapdataIn) {
        Instance mapitemrenderer$instance = (Instance)this.loadedMaps.get((Object)mapdataIn.mapName);
        if (mapitemrenderer$instance == null) {
            mapitemrenderer$instance = new Instance(this, mapdataIn, null);
            this.loadedMaps.put((Object)mapdataIn.mapName, (Object)mapitemrenderer$instance);
        }
        return mapitemrenderer$instance;
    }

    public void clearLoadedMaps() {
        for (Instance mapitemrenderer$instance : this.loadedMaps.values()) {
            this.textureManager.deleteTexture(Instance.access$300((Instance)mapitemrenderer$instance));
        }
        this.loadedMaps.clear();
    }

    static /* synthetic */ TextureManager access$400(MapItemRenderer x0) {
        return x0.textureManager;
    }

    static /* synthetic */ ResourceLocation access$500() {
        return mapIcons;
    }
}
