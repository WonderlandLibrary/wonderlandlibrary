// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.client.renderer.GLAllocation;
import java.io.IOException;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.ITextureObject;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.TextureMap;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class TextureUtils
{
    public static final String texGrassTop = "grass_top";
    public static final String texStone = "stone";
    public static final String texDirt = "dirt";
    public static final String texGrassSide = "grass_side";
    public static final String texStoneslabSide = "stone_slab_side";
    public static final String texStoneslabTop = "stone_slab_top";
    public static final String texBedrock = "bedrock";
    public static final String texSand = "sand";
    public static final String texGravel = "gravel";
    public static final String texLogOak = "log_oak";
    public static final String texLogOakTop = "log_oak_top";
    public static final String texGoldOre = "gold_ore";
    public static final String texIronOre = "iron_ore";
    public static final String texCoalOre = "coal_ore";
    public static final String texObsidian = "obsidian";
    public static final String texGrassSideOverlay = "grass_side_overlay";
    public static final String texSnow = "snow";
    public static final String texGrassSideSnowed = "grass_side_snowed";
    public static final String texMyceliumSide = "mycelium_side";
    public static final String texMyceliumTop = "mycelium_top";
    public static final String texDiamondOre = "diamond_ore";
    public static final String texRedstoneOre = "redstone_ore";
    public static final String texLapisOre = "lapis_ore";
    public static final String texLeavesOak = "leaves_oak";
    public static final String texLeavesOakOpaque = "leaves_oak_opaque";
    public static final String texLeavesJungle = "leaves_jungle";
    public static final String texLeavesJungleOpaque = "leaves_jungle_opaque";
    public static final String texCactusSide = "cactus_side";
    public static final String texClay = "clay";
    public static final String texFarmlandWet = "farmland_wet";
    public static final String texFarmlandDry = "farmland_dry";
    public static final String texNetherrack = "netherrack";
    public static final String texSoulSand = "soul_sand";
    public static final String texGlowstone = "glowstone";
    public static final String texLogSpruce = "log_spruce";
    public static final String texLogBirch = "log_birch";
    public static final String texLeavesSpruce = "leaves_spruce";
    public static final String texLeavesSpruceOpaque = "leaves_spruce_opaque";
    public static final String texLogJungle = "log_jungle";
    public static final String texEndStone = "end_stone";
    public static final String texSandstoneTop = "sandstone_top";
    public static final String texSandstoneBottom = "sandstone_bottom";
    public static final String texRedstoneLampOff = "redstone_lamp_off";
    public static final String texRedstoneLampOn = "redstone_lamp_on";
    public static final String texWaterStill = "water_still";
    public static final String texWaterFlow = "water_flow";
    public static final String texLavaStill = "lava_still";
    public static final String texLavaFlow = "lava_flow";
    public static final String texFireLayer0 = "fire_layer_0";
    public static final String texFireLayer1 = "fire_layer_1";
    public static final String texPortal = "portal";
    public static final String texGlass = "glass";
    public static final String texGlassPaneTop = "glass_pane_top";
    public static TextureAtlasSprite iconGrassTop;
    public static TextureAtlasSprite iconGrassSide;
    public static TextureAtlasSprite iconGrassSideOverlay;
    public static TextureAtlasSprite iconSnow;
    public static TextureAtlasSprite iconGrassSideSnowed;
    public static TextureAtlasSprite iconMyceliumSide;
    public static TextureAtlasSprite iconMyceliumTop;
    public static TextureAtlasSprite iconWaterStill;
    public static TextureAtlasSprite iconWaterFlow;
    public static TextureAtlasSprite iconLavaStill;
    public static TextureAtlasSprite iconLavaFlow;
    public static TextureAtlasSprite iconPortal;
    public static TextureAtlasSprite iconFireLayer0;
    public static TextureAtlasSprite iconFireLayer1;
    public static TextureAtlasSprite iconGlass;
    public static TextureAtlasSprite iconGlassPaneTop;
    public static final String SPRITE_LOCATION_PREFIX = "minecraft:blocks/";
    private static IntBuffer staticBuffer;
    
    public static void update() {
        final TextureMap mapBlocks = getTextureMapBlocks();
        if (mapBlocks != null) {
            final String prefix = "minecraft:blocks/";
            TextureUtils.iconGrassTop = mapBlocks.getSpriteSafe(prefix + "grass_top");
            TextureUtils.iconGrassSide = mapBlocks.getSpriteSafe(prefix + "grass_side");
            TextureUtils.iconGrassSideOverlay = mapBlocks.getSpriteSafe(prefix + "grass_side_overlay");
            TextureUtils.iconSnow = mapBlocks.getSpriteSafe(prefix + "snow");
            TextureUtils.iconGrassSideSnowed = mapBlocks.getSpriteSafe(prefix + "grass_side_snowed");
            TextureUtils.iconMyceliumSide = mapBlocks.getSpriteSafe(prefix + "mycelium_side");
            TextureUtils.iconMyceliumTop = mapBlocks.getSpriteSafe(prefix + "mycelium_top");
            TextureUtils.iconWaterStill = mapBlocks.getSpriteSafe(prefix + "water_still");
            TextureUtils.iconWaterFlow = mapBlocks.getSpriteSafe(prefix + "water_flow");
            TextureUtils.iconLavaStill = mapBlocks.getSpriteSafe(prefix + "lava_still");
            TextureUtils.iconLavaFlow = mapBlocks.getSpriteSafe(prefix + "lava_flow");
            TextureUtils.iconFireLayer0 = mapBlocks.getSpriteSafe(prefix + "fire_layer_0");
            TextureUtils.iconFireLayer1 = mapBlocks.getSpriteSafe(prefix + "fire_layer_1");
            TextureUtils.iconPortal = mapBlocks.getSpriteSafe(prefix + "portal");
            TextureUtils.iconGlass = mapBlocks.getSpriteSafe(prefix + "glass");
            TextureUtils.iconGlassPaneTop = mapBlocks.getSpriteSafe(prefix + "glass_pane_top");
        }
    }
    
    public static BufferedImage fixTextureDimensions(final String name, final BufferedImage bi) {
        if (name.startsWith("/mob/zombie") || name.startsWith("/mob/pigzombie")) {
            final int width = bi.getWidth();
            final int height = bi.getHeight();
            if (width == height * 2) {
                final BufferedImage scaledImage = new BufferedImage(width, height * 2, 2);
                final Graphics2D gr = scaledImage.createGraphics();
                gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                gr.drawImage(bi, 0, 0, width, height, null);
                return scaledImage;
            }
        }
        return bi;
    }
    
    public static int ceilPowerOfTwo(final int val) {
        int i;
        for (i = 1; i < val; i *= 2) {}
        return i;
    }
    
    public static int getPowerOfTwo(final int val) {
        int i;
        int po2;
        for (i = 1, po2 = 0; i < val; i *= 2, ++po2) {}
        return po2;
    }
    
    public static int twoToPower(final int power) {
        int val = 1;
        for (int i = 0; i < power; ++i) {
            val *= 2;
        }
        return val;
    }
    
    public static void refreshBlockTextures() {
    }
    
    public static ITextureObject getTexture(final String path) {
        return getTexture(new ResourceLocation(path));
    }
    
    public static ITextureObject getTexture(final ResourceLocation loc) {
        final ITextureObject tex = Config.getTextureManager().getTexture(loc);
        if (tex != null) {
            return tex;
        }
        if (!Config.hasResource(loc)) {
            return null;
        }
        final SimpleTexture tex2 = new SimpleTexture(loc);
        Config.getTextureManager().loadTexture(loc, tex2);
        return tex2;
    }
    
    public static void resourcesReloaded(final IResourceManager rm) {
        if (getTextureMapBlocks() != null) {
            Config.dbg("*** Reloading custom textures ***");
            CustomSky.reset();
            TextureAnimations.reset();
            update();
            NaturalTextures.update();
            BetterGrass.update();
            BetterSnow.update();
            TextureAnimations.update();
            CustomColorizer.update();
            CustomSky.update();
            RandomMobs.resetTextures();
            Config.updateTexturePackClouds();
            Config.getTextureManager().tick();
        }
    }
    
    public static TextureMap getTextureMapBlocks() {
        return Minecraft.getMinecraft().getTextureMapBlocks();
    }
    
    public static void registerResourceListener() {
        final IResourceManager rm = Config.getResourceManager();
        if (rm instanceof IReloadableResourceManager) {
            final IReloadableResourceManager tto = (IReloadableResourceManager)rm;
            final IResourceManagerReloadListener ttol = new IResourceManagerReloadListener() {
                @Override
                public void onResourceManagerReload(final IResourceManager var1) {
                    TextureUtils.resourcesReloaded(var1);
                }
            };
            tto.registerReloadListener(ttol);
        }
        final ITickableTextureObject tto2 = new ITickableTextureObject() {
            @Override
            public void tick() {
                TextureAnimations.updateCustomAnimations();
            }
            
            @Override
            public void loadTexture(final IResourceManager var1) throws IOException {
            }
            
            @Override
            public int getGlTextureId() {
                return 0;
            }
            
            @Override
            public void func_174936_b(final boolean p_174936_1, final boolean p_174936_2) {
            }
            
            @Override
            public void func_174935_a() {
            }
        };
        final ResourceLocation ttol2 = new ResourceLocation("optifine/TickableTextures");
        Config.getTextureManager().loadTickableTexture(ttol2, tto2);
    }
    
    public static String fixResourcePath(String path, String basePath) {
        final String strAssMc = "assets/minecraft/";
        if (path.startsWith(strAssMc)) {
            path = path.substring(strAssMc.length());
            return path;
        }
        if (path.startsWith("./")) {
            path = path.substring(2);
            if (!basePath.endsWith("/")) {
                basePath += "/";
            }
            path = basePath + path;
            return path;
        }
        final String strMcpatcher = "mcpatcher/";
        if (path.startsWith("~/")) {
            path = path.substring(2);
            path = strMcpatcher + path;
            return path;
        }
        if (path.startsWith("/")) {
            path = strMcpatcher + path.substring(1);
            return path;
        }
        return path;
    }
    
    public static String getBasePath(final String path) {
        final int pos = path.lastIndexOf(47);
        return (pos < 0) ? "" : path.substring(0, pos);
    }
    
    static {
        TextureUtils.staticBuffer = GLAllocation.createDirectIntBuffer(256);
    }
}
