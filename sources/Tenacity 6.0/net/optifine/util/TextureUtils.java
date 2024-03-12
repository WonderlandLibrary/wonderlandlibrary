// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import net.minecraft.client.renderer.GLAllocation;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.RenderedImage;
import org.lwjgl.BufferUtils;
import java.io.File;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;
import javax.imageio.ImageReader;
import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.io.InputStream;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import net.optifine.shaders.MultiTexID;
import java.io.IOException;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.Minecraft;
import net.optifine.CustomBlockLayers;
import net.optifine.CustomLoadingScreens;
import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
import net.optifine.CustomGuis;
import net.optifine.CustomPanorama;
import net.optifine.SmartLeaves;
import net.optifine.Lang;
import net.optifine.shaders.Shaders;
import net.optifine.entity.model.CustomEntityModels;
import net.optifine.CustomItems;
import net.optifine.RandomEntities;
import net.optifine.CustomColors;
import net.optifine.BetterSnow;
import net.optifine.BetterGrass;
import net.optifine.NaturalTextures;
import net.optifine.TextureAnimations;
import net.optifine.CustomSky;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
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
    public static final String texCoarseDirt = "coarse_dirt";
    public static final String texGrassSide = "grass_side";
    public static final String texStoneslabSide = "stone_slab_side";
    public static final String texStoneslabTop = "stone_slab_top";
    public static final String texBedrock = "bedrock";
    public static final String texSand = "sand";
    public static final String texGravel = "gravel";
    public static final String texLogOak = "log_oak";
    public static final String texLogBigOak = "log_big_oak";
    public static final String texLogAcacia = "log_acacia";
    public static final String texLogSpruce = "log_spruce";
    public static final String texLogBirch = "log_birch";
    public static final String texLogJungle = "log_jungle";
    public static final String texLogOakTop = "log_oak_top";
    public static final String texLogBigOakTop = "log_big_oak_top";
    public static final String texLogAcaciaTop = "log_acacia_top";
    public static final String texLogSpruceTop = "log_spruce_top";
    public static final String texLogBirchTop = "log_birch_top";
    public static final String texLogJungleTop = "log_jungle_top";
    public static final String texLeavesOak = "leaves_oak";
    public static final String texLeavesBigOak = "leaves_big_oak";
    public static final String texLeavesAcacia = "leaves_acacia";
    public static final String texLeavesBirch = "leaves_birch";
    public static final String texLeavesSpuce = "leaves_spruce";
    public static final String texLeavesJungle = "leaves_jungle";
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
    public static final String texCactusSide = "cactus_side";
    public static final String texClay = "clay";
    public static final String texFarmlandWet = "farmland_wet";
    public static final String texFarmlandDry = "farmland_dry";
    public static final String texNetherrack = "netherrack";
    public static final String texSoulSand = "soul_sand";
    public static final String texGlowstone = "glowstone";
    public static final String texLeavesSpruce = "leaves_spruce";
    public static final String texLeavesSpruceOpaque = "leaves_spruce_opaque";
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
    public static final String texCompass = "compass";
    public static final String texClock = "clock";
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
    public static TextureAtlasSprite iconCompass;
    public static TextureAtlasSprite iconClock;
    public static final String SPRITE_PREFIX_BLOCKS = "minecraft:blocks/";
    public static final String SPRITE_PREFIX_ITEMS = "minecraft:items/";
    private static IntBuffer staticBuffer;
    
    public static void update() {
        final TextureMap texturemap = getTextureMapBlocks();
        if (texturemap != null) {
            final String s = "minecraft:blocks/";
            TextureUtils.iconGrassTop = texturemap.getSpriteSafe(s + "grass_top");
            TextureUtils.iconGrassSide = texturemap.getSpriteSafe(s + "grass_side");
            TextureUtils.iconGrassSideOverlay = texturemap.getSpriteSafe(s + "grass_side_overlay");
            TextureUtils.iconSnow = texturemap.getSpriteSafe(s + "snow");
            TextureUtils.iconGrassSideSnowed = texturemap.getSpriteSafe(s + "grass_side_snowed");
            TextureUtils.iconMyceliumSide = texturemap.getSpriteSafe(s + "mycelium_side");
            TextureUtils.iconMyceliumTop = texturemap.getSpriteSafe(s + "mycelium_top");
            TextureUtils.iconWaterStill = texturemap.getSpriteSafe(s + "water_still");
            TextureUtils.iconWaterFlow = texturemap.getSpriteSafe(s + "water_flow");
            TextureUtils.iconLavaStill = texturemap.getSpriteSafe(s + "lava_still");
            TextureUtils.iconLavaFlow = texturemap.getSpriteSafe(s + "lava_flow");
            TextureUtils.iconFireLayer0 = texturemap.getSpriteSafe(s + "fire_layer_0");
            TextureUtils.iconFireLayer1 = texturemap.getSpriteSafe(s + "fire_layer_1");
            TextureUtils.iconPortal = texturemap.getSpriteSafe(s + "portal");
            TextureUtils.iconGlass = texturemap.getSpriteSafe(s + "glass");
            TextureUtils.iconGlassPaneTop = texturemap.getSpriteSafe(s + "glass_pane_top");
            final String s2 = "minecraft:items/";
            TextureUtils.iconCompass = texturemap.getSpriteSafe(s2 + "compass");
            TextureUtils.iconClock = texturemap.getSpriteSafe(s2 + "clock");
        }
    }
    
    public static BufferedImage fixTextureDimensions(final String name, final BufferedImage bi) {
        if (name.startsWith("/mob/zombie") || name.startsWith("/mob/pigzombie")) {
            final int i = bi.getWidth();
            final int j = bi.getHeight();
            if (i == j * 2) {
                final BufferedImage bufferedimage = new BufferedImage(i, j * 2, 2);
                final Graphics2D graphics2d = bufferedimage.createGraphics();
                graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics2d.drawImage(bi, 0, 0, i, j, null);
                return bufferedimage;
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
        int j;
        for (i = 1, j = 0; i < val; i *= 2, ++j) {}
        return j;
    }
    
    public static int twoToPower(final int power) {
        int i = 1;
        for (int j = 0; j < power; ++j) {
            i *= 2;
        }
        return i;
    }
    
    public static ITextureObject getTexture(final ResourceLocation loc) {
        final ITextureObject itextureobject = Config.getTextureManager().getTexture(loc);
        if (itextureobject != null) {
            return itextureobject;
        }
        if (!Config.hasResource(loc)) {
            return null;
        }
        final SimpleTexture simpletexture = new SimpleTexture(loc);
        Config.getTextureManager().loadTexture(loc, simpletexture);
        return simpletexture;
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
            CustomColors.update();
            CustomSky.update();
            RandomEntities.update();
            CustomItems.updateModels();
            CustomEntityModels.update();
            Shaders.resourcesReloaded();
            Lang.resourcesReloaded();
            Config.updateTexturePackClouds();
            SmartLeaves.updateLeavesModels();
            CustomPanorama.update();
            CustomGuis.update();
            LayerMooshroomMushroom.update();
            CustomLoadingScreens.update();
            CustomBlockLayers.update();
            Config.getTextureManager().tick();
        }
    }
    
    public static TextureMap getTextureMapBlocks() {
        return Minecraft.getMinecraft().getTextureMapBlocks();
    }
    
    public static void registerResourceListener() {
        final IResourceManager iresourcemanager = Config.getResourceManager();
        if (iresourcemanager instanceof IReloadableResourceManager) {
            final IReloadableResourceManager ireloadableresourcemanager = (IReloadableResourceManager)iresourcemanager;
            final IResourceManagerReloadListener iresourcemanagerreloadlistener = new IResourceManagerReloadListener() {
                @Override
                public void onResourceManagerReload(final IResourceManager var1) {
                    TextureUtils.resourcesReloaded(var1);
                }
            };
            ireloadableresourcemanager.registerReloadListener(iresourcemanagerreloadlistener);
        }
        final ITickableTextureObject itickabletextureobject = new ITickableTextureObject() {
            @Override
            public void tick() {
                TextureAnimations.updateAnimations();
            }
            
            @Override
            public void loadTexture(final IResourceManager var1) throws IOException {
            }
            
            @Override
            public int getGlTextureId() {
                return 0;
            }
            
            @Override
            public void setBlurMipmap(final boolean p_174936_1, final boolean p_174936_2) {
            }
            
            @Override
            public void restoreLastBlurMipmap() {
            }
            
            @Override
            public MultiTexID getMultiTexID() {
                return null;
            }
        };
        final ResourceLocation resourcelocation = new ResourceLocation("optifine/TickableTextures");
        Config.getTextureManager().loadTickableTexture(resourcelocation, itickabletextureobject);
    }
    
    public static ResourceLocation fixResourceLocation(ResourceLocation loc, final String basePath) {
        if (!loc.getResourceDomain().equals("minecraft")) {
            return loc;
        }
        final String s = loc.getResourcePath();
        final String s2 = fixResourcePath(s, basePath);
        if (s2 != s) {
            loc = new ResourceLocation(loc.getResourceDomain(), s2);
        }
        return loc;
    }
    
    public static String fixResourcePath(String path, String basePath) {
        final String s = "assets/minecraft/";
        if (path.startsWith(s)) {
            path = path.substring(s.length());
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
        if (path.startsWith("/~")) {
            path = path.substring(1);
        }
        final String s2 = "mcpatcher/";
        if (path.startsWith("~/")) {
            path = path.substring(2);
            path = s2 + path;
            return path;
        }
        if (path.startsWith("/")) {
            path = s2 + path.substring(1);
            return path;
        }
        return path;
    }
    
    public static String getBasePath(final String path) {
        final int i = path.lastIndexOf(47);
        return (i < 0) ? "" : path.substring(0, i);
    }
    
    public static void applyAnisotropicLevel() {
        if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
            final float f = GL11.glGetFloat(34047);
            float f2 = (float)Config.getAnisotropicFilterLevel();
            f2 = Math.min(f2, f);
            GL11.glTexParameterf(3553, 34046, f2);
        }
    }
    
    public static void bindTexture(final int glTexId) {
        GlStateManager.bindTexture(glTexId);
    }
    
    public static boolean isPowerOfTwo(final int x) {
        final int i = MathHelper.roundUpToPowerOfTwo(x);
        return i == x;
    }
    
    public static BufferedImage scaleImage(final BufferedImage bi, final int w2) {
        final int i = bi.getWidth();
        final int j = bi.getHeight();
        final int k = j * w2 / i;
        final BufferedImage bufferedimage = new BufferedImage(w2, k, 2);
        final Graphics2D graphics2d = bufferedimage.createGraphics();
        Object object = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        if (w2 < i || w2 % i != 0) {
            object = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        }
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, object);
        graphics2d.drawImage(bi, 0, 0, w2, k, null);
        return bufferedimage;
    }
    
    public static int scaleToGrid(final int size, final int sizeGrid) {
        if (size == sizeGrid) {
            return size;
        }
        int i;
        for (i = size / sizeGrid * sizeGrid; i < size; i += sizeGrid) {}
        return i;
    }
    
    public static int scaleToMin(final int size, final int sizeMin) {
        if (size >= sizeMin) {
            return size;
        }
        int i;
        for (i = sizeMin / size * size; i < sizeMin; i += size) {}
        return i;
    }
    
    public static Dimension getImageSize(final InputStream in, final String suffix) {
        final Iterator iterator = ImageIO.getImageReadersBySuffix(suffix);
        while (iterator.hasNext()) {
            final ImageReader imagereader = iterator.next();
            Dimension dimension;
            try {
                final ImageInputStream imageinputstream = ImageIO.createImageInputStream(in);
                imagereader.setInput(imageinputstream);
                final int i = imagereader.getWidth(imagereader.getMinIndex());
                final int j = imagereader.getHeight(imagereader.getMinIndex());
                dimension = new Dimension(i, j);
            }
            catch (IOException var11) {
                continue;
            }
            finally {
                imagereader.dispose();
            }
            return dimension;
        }
        return null;
    }
    
    public static void dbgMipmaps(final TextureAtlasSprite textureatlassprite) {
        final int[][] aint = textureatlassprite.getFrameTextureData(0);
        for (int i = 0; i < aint.length; ++i) {
            final int[] aint2 = aint[i];
            if (aint2 == null) {
                Config.dbg("" + i + ": " + aint2);
            }
            else {
                Config.dbg("" + i + ": " + aint2.length);
            }
        }
    }
    
    public static void saveGlTexture(final String name, final int textureId, final int mipmapLevels, final int width, final int height) {
        bindTexture(textureId);
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        final File file1 = new File(name);
        final File file2 = file1.getParentFile();
        if (file2 != null) {
            file2.mkdirs();
        }
        for (int i = 0; i < 16; ++i) {
            final File file3 = new File(name + "_" + i + ".png");
            file3.delete();
        }
        for (int i2 = 0; i2 <= mipmapLevels; ++i2) {
            final File file4 = new File(name + "_" + i2 + ".png");
            final int j = width >> i2;
            final int k = height >> i2;
            final int l = j * k;
            final IntBuffer intbuffer = BufferUtils.createIntBuffer(l);
            final int[] aint = new int[l];
            GL11.glGetTexImage(3553, i2, 32993, 33639, intbuffer);
            intbuffer.get(aint);
            final BufferedImage bufferedimage = new BufferedImage(j, k, 2);
            bufferedimage.setRGB(0, 0, j, k, aint, 0, j);
            try {
                ImageIO.write(bufferedimage, "png", file4);
                Config.dbg("Exported: " + file4);
            }
            catch (Exception exception) {
                Config.warn("Error writing: " + file4);
                Config.warn("" + exception.getClass().getName() + ": " + exception.getMessage());
            }
        }
    }
    
    public static void generateCustomMipmaps(final TextureAtlasSprite tas, final int mipmaps) {
        final int i = tas.getIconWidth();
        final int j = tas.getIconHeight();
        if (tas.getFrameCount() < 1) {
            final List<int[][]> list = new ArrayList<int[][]>();
            final int[][] aint = new int[mipmaps + 1][];
            final int[] aint2 = new int[i * j];
            aint[0] = aint2;
            list.add(aint);
            tas.setFramesTextureData(list);
        }
        final List<int[][]> list2 = new ArrayList<int[][]>();
        for (int l = tas.getFrameCount(), i2 = 0; i2 < l; ++i2) {
            int[] aint3 = getFrameData(tas, i2, 0);
            if (aint3 == null || aint3.length < 1) {
                aint3 = new int[i * j];
            }
            if (aint3.length != i * j) {
                int k = (int)Math.round(Math.sqrt(aint3.length));
                if (k * k != aint3.length) {
                    aint3 = new int[] { 0 };
                    k = 1;
                }
                final BufferedImage bufferedimage = new BufferedImage(k, k, 2);
                bufferedimage.setRGB(0, 0, k, k, aint3, 0, k);
                final BufferedImage bufferedimage2 = scaleImage(bufferedimage, i);
                final int[] aint4 = new int[i * j];
                bufferedimage2.getRGB(0, 0, i, j, aint4, 0, i);
                aint3 = aint4;
            }
            final int[][] aint5 = new int[mipmaps + 1][];
            aint5[0] = aint3;
            list2.add(aint5);
        }
        tas.setFramesTextureData(list2);
        tas.generateMipmaps(mipmaps);
    }
    
    public static int[] getFrameData(final TextureAtlasSprite tas, final int frame, final int level) {
        final List<int[][]> list = tas.getFramesTextureData();
        if (list.size() <= frame) {
            return null;
        }
        final int[][] aint = list.get(frame);
        if (aint != null && aint.length > level) {
            final int[] aint2 = aint[level];
            return aint2;
        }
        return null;
    }
    
    public static int getGLMaximumTextureSize() {
        for (int i = 65536; i > 0; i >>= 1) {
            GlStateManager.glTexImage2D(32868, 0, 6408, i, i, 0, 6408, 5121, null);
            final int j = GL11.glGetError();
            final int k = GlStateManager.glGetTexLevelParameteri(32868, 0, 4096);
            if (k != 0) {
                return i;
            }
        }
        return -1;
    }
    
    static {
        TextureUtils.staticBuffer = GLAllocation.createDirectIntBuffer(256);
    }
}
