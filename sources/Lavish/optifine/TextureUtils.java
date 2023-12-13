// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

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
import shadersmod.client.MultiTexID;
import java.io.IOException;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.Minecraft;
import shadersmod.client.Shaders;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.GLAllocation;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class TextureUtils
{
    public static final String texGrassTop;
    public static final String texStone;
    public static final String texDirt;
    public static final String texCoarseDirt;
    public static final String texGrassSide;
    public static final String texStoneslabSide;
    public static final String texStoneslabTop;
    public static final String texBedrock;
    public static final String texSand;
    public static final String texGravel;
    public static final String texLogOak;
    public static final String texLogBigOak;
    public static final String texLogAcacia;
    public static final String texLogSpruce;
    public static final String texLogBirch;
    public static final String texLogJungle;
    public static final String texLogOakTop;
    public static final String texLogBigOakTop;
    public static final String texLogAcaciaTop;
    public static final String texLogSpruceTop;
    public static final String texLogBirchTop;
    public static final String texLogJungleTop;
    public static final String texLeavesOak;
    public static final String texLeavesBigOak;
    public static final String texLeavesAcacia;
    public static final String texLeavesBirch;
    public static final String texLeavesSpuce;
    public static final String texLeavesJungle;
    public static final String texGoldOre;
    public static final String texIronOre;
    public static final String texCoalOre;
    public static final String texObsidian;
    public static final String texGrassSideOverlay;
    public static final String texSnow;
    public static final String texGrassSideSnowed;
    public static final String texMyceliumSide;
    public static final String texMyceliumTop;
    public static final String texDiamondOre;
    public static final String texRedstoneOre;
    public static final String texLapisOre;
    public static final String texCactusSide;
    public static final String texClay;
    public static final String texFarmlandWet;
    public static final String texFarmlandDry;
    public static final String texNetherrack;
    public static final String texSoulSand;
    public static final String texGlowstone;
    public static final String texLeavesSpruce;
    public static final String texLeavesSpruceOpaque;
    public static final String texEndStone;
    public static final String texSandstoneTop;
    public static final String texSandstoneBottom;
    public static final String texRedstoneLampOff;
    public static final String texRedstoneLampOn;
    public static final String texWaterStill;
    public static final String texWaterFlow;
    public static final String texLavaStill;
    public static final String texLavaFlow;
    public static final String texFireLayer0;
    public static final String texFireLayer1;
    public static final String texPortal;
    public static final String texGlass;
    public static final String texGlassPaneTop;
    public static final String texCompass;
    public static final String texClock;
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
    public static final String SPRITE_PREFIX_BLOCKS;
    public static final String SPRITE_PREFIX_ITEMS;
    private static IntBuffer staticBuffer;
    
    static {
        texStone = "stone";
        texLeavesJungle = "leaves_jungle";
        texLeavesSpruceOpaque = "leaves_spruce_opaque";
        texLogJungle = "log_jungle";
        texLogBirch = "log_birch";
        texSandstoneBottom = "sandstone_bottom";
        texLogOak = "log_oak";
        texIronOre = "iron_ore";
        texMyceliumSide = "mycelium_side";
        texGrassTop = "grass_top";
        texSoulSand = "soul_sand";
        texGlassPaneTop = "glass_pane_top";
        texGrassSideSnowed = "grass_side_snowed";
        texNetherrack = "netherrack";
        texRedstoneOre = "redstone_ore";
        texCoarseDirt = "coarse_dirt";
        texLogSpruce = "log_spruce";
        texLogBigOakTop = "log_big_oak_top";
        texGravel = "gravel";
        texObsidian = "obsidian";
        texLogAcaciaTop = "log_acacia_top";
        texRedstoneLampOff = "redstone_lamp_off";
        texLeavesBigOak = "leaves_big_oak";
        texRedstoneLampOn = "redstone_lamp_on";
        texLeavesSpuce = "leaves_spruce";
        texLogAcacia = "log_acacia";
        texLeavesAcacia = "leaves_acacia";
        texCoalOre = "coal_ore";
        texSand = "sand";
        texWaterStill = "water_still";
        texGrassSideOverlay = "grass_side_overlay";
        texFarmlandWet = "farmland_wet";
        texGrassSide = "grass_side";
        texLogOakTop = "log_oak_top";
        texWaterFlow = "water_flow";
        texLeavesBirch = "leaves_birch";
        texGoldOre = "gold_ore";
        texClay = "clay";
        texMyceliumTop = "mycelium_top";
        texClock = "clock";
        texStoneslabTop = "stone_slab_top";
        texLavaFlow = "lava_flow";
        texFarmlandDry = "farmland_dry";
        texSandstoneTop = "sandstone_top";
        texLogSpruceTop = "log_spruce_top";
        texCompass = "compass";
        texLeavesOak = "leaves_oak";
        texLogBirchTop = "log_birch_top";
        texGlass = "glass";
        texGlowstone = "glowstone";
        texFireLayer0 = "fire_layer_0";
        SPRITE_PREFIX_BLOCKS = "minecraft:blocks/";
        texLavaStill = "lava_still";
        texLeavesSpruce = "leaves_spruce";
        texSnow = "snow";
        texStoneslabSide = "stone_slab_side";
        texEndStone = "end_stone";
        texPortal = "portal";
        texLapisOre = "lapis_ore";
        texDiamondOre = "diamond_ore";
        texLogJungleTop = "log_jungle_top";
        texDirt = "dirt";
        texBedrock = "bedrock";
        texLogBigOak = "log_big_oak";
        SPRITE_PREFIX_ITEMS = "minecraft:items/";
        texFireLayer1 = "fire_layer_1";
        texCactusSide = "cactus_side";
        TextureUtils.staticBuffer = GLAllocation.createDirectIntBuffer(256);
    }
    
    public static void update() {
        final TextureMap texturemap = getTextureMapBlocks();
        if (texturemap != null) {
            final String s = "minecraft:blocks/";
            TextureUtils.iconGrassTop = texturemap.getSpriteSafe(String.valueOf(s) + "grass_top");
            TextureUtils.iconGrassSide = texturemap.getSpriteSafe(String.valueOf(s) + "grass_side");
            TextureUtils.iconGrassSideOverlay = texturemap.getSpriteSafe(String.valueOf(s) + "grass_side_overlay");
            TextureUtils.iconSnow = texturemap.getSpriteSafe(String.valueOf(s) + "snow");
            TextureUtils.iconGrassSideSnowed = texturemap.getSpriteSafe(String.valueOf(s) + "grass_side_snowed");
            TextureUtils.iconMyceliumSide = texturemap.getSpriteSafe(String.valueOf(s) + "mycelium_side");
            TextureUtils.iconMyceliumTop = texturemap.getSpriteSafe(String.valueOf(s) + "mycelium_top");
            TextureUtils.iconWaterStill = texturemap.getSpriteSafe(String.valueOf(s) + "water_still");
            TextureUtils.iconWaterFlow = texturemap.getSpriteSafe(String.valueOf(s) + "water_flow");
            TextureUtils.iconLavaStill = texturemap.getSpriteSafe(String.valueOf(s) + "lava_still");
            TextureUtils.iconLavaFlow = texturemap.getSpriteSafe(String.valueOf(s) + "lava_flow");
            TextureUtils.iconFireLayer0 = texturemap.getSpriteSafe(String.valueOf(s) + "fire_layer_0");
            TextureUtils.iconFireLayer1 = texturemap.getSpriteSafe(String.valueOf(s) + "fire_layer_1");
            TextureUtils.iconPortal = texturemap.getSpriteSafe(String.valueOf(s) + "portal");
            TextureUtils.iconGlass = texturemap.getSpriteSafe(String.valueOf(s) + "glass");
            TextureUtils.iconGlassPaneTop = texturemap.getSpriteSafe(String.valueOf(s) + "glass_pane_top");
            final String s2 = "minecraft:items/";
            TextureUtils.iconCompass = texturemap.getSpriteSafe(String.valueOf(s2) + "compass");
            TextureUtils.iconClock = texturemap.getSpriteSafe(String.valueOf(s2) + "clock");
        }
    }
    
    public static BufferedImage fixTextureDimensions(final String p_fixTextureDimensions_0_, final BufferedImage p_fixTextureDimensions_1_) {
        if (p_fixTextureDimensions_0_.startsWith("/mob/zombie") || p_fixTextureDimensions_0_.startsWith("/mob/pigzombie")) {
            final int i = p_fixTextureDimensions_1_.getWidth();
            final int j = p_fixTextureDimensions_1_.getHeight();
            if (i == j * 2) {
                final BufferedImage bufferedimage = new BufferedImage(i, j * 2, 2);
                final Graphics2D graphics2d = bufferedimage.createGraphics();
                graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics2d.drawImage(p_fixTextureDimensions_1_, 0, 0, i, j, null);
                return bufferedimage;
            }
        }
        return p_fixTextureDimensions_1_;
    }
    
    public static int ceilPowerOfTwo(final int p_ceilPowerOfTwo_0_) {
        int i;
        for (i = 1; i < p_ceilPowerOfTwo_0_; i *= 2) {}
        return i;
    }
    
    public static int getPowerOfTwo(final int p_getPowerOfTwo_0_) {
        int i;
        int j;
        for (i = 1, j = 0; i < p_getPowerOfTwo_0_; i *= 2, ++j) {}
        return j;
    }
    
    public static int twoToPower(final int p_twoToPower_0_) {
        int i = 1;
        for (int j = 0; j < p_twoToPower_0_; ++j) {
            i *= 2;
        }
        return i;
    }
    
    public static ITextureObject getTexture(final ResourceLocation p_getTexture_0_) {
        final ITextureObject itextureobject = Config.getTextureManager().getTexture(p_getTexture_0_);
        if (itextureobject != null) {
            return itextureobject;
        }
        if (!Config.hasResource(p_getTexture_0_)) {
            return null;
        }
        final SimpleTexture simpletexture = new SimpleTexture(p_getTexture_0_);
        Config.getTextureManager().loadTexture(p_getTexture_0_, simpletexture);
        return simpletexture;
    }
    
    public static void resourcesReloaded(final IResourceManager p_resourcesReloaded_0_) {
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
            RandomMobs.resetTextures();
            CustomItems.updateModels();
            Shaders.resourcesReloaded();
            Lang.resourcesReloaded();
            Config.updateTexturePackClouds();
            SmartLeaves.updateLeavesModels();
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
                public void onResourceManagerReload(final IResourceManager resourceManager) {
                    TextureUtils.resourcesReloaded(resourceManager);
                }
            };
            ireloadableresourcemanager.registerReloadListener(iresourcemanagerreloadlistener);
        }
        final ITickableTextureObject itickabletextureobject = new ITickableTextureObject() {
            @Override
            public void tick() {
                TextureAnimations.updateCustomAnimations();
            }
            
            @Override
            public void loadTexture(final IResourceManager resourceManager) throws IOException {
            }
            
            @Override
            public int getGlTextureId() {
                return 0;
            }
            
            @Override
            public void setBlurMipmap(final boolean p_174936_1_, final boolean p_174936_2_) {
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
    
    public static String fixResourcePath(String p_fixResourcePath_0_, String p_fixResourcePath_1_) {
        final String s = "assets/minecraft/";
        if (p_fixResourcePath_0_.startsWith(s)) {
            p_fixResourcePath_0_ = p_fixResourcePath_0_.substring(s.length());
            return p_fixResourcePath_0_;
        }
        if (p_fixResourcePath_0_.startsWith("./")) {
            p_fixResourcePath_0_ = p_fixResourcePath_0_.substring(2);
            if (!p_fixResourcePath_1_.endsWith("/")) {
                p_fixResourcePath_1_ = String.valueOf(p_fixResourcePath_1_) + "/";
            }
            p_fixResourcePath_0_ = String.valueOf(p_fixResourcePath_1_) + p_fixResourcePath_0_;
            return p_fixResourcePath_0_;
        }
        if (p_fixResourcePath_0_.startsWith("/~")) {
            p_fixResourcePath_0_ = p_fixResourcePath_0_.substring(1);
        }
        final String s2 = "mcpatcher/";
        if (p_fixResourcePath_0_.startsWith("~/")) {
            p_fixResourcePath_0_ = p_fixResourcePath_0_.substring(2);
            p_fixResourcePath_0_ = String.valueOf(s2) + p_fixResourcePath_0_;
            return p_fixResourcePath_0_;
        }
        if (p_fixResourcePath_0_.startsWith("/")) {
            p_fixResourcePath_0_ = String.valueOf(s2) + p_fixResourcePath_0_.substring(1);
            return p_fixResourcePath_0_;
        }
        return p_fixResourcePath_0_;
    }
    
    public static String getBasePath(final String p_getBasePath_0_) {
        final int i = p_getBasePath_0_.lastIndexOf(47);
        return (i < 0) ? "" : p_getBasePath_0_.substring(0, i);
    }
    
    public static void applyAnisotropicLevel() {
        if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
            final float f = GL11.glGetFloat(34047);
            float f2 = (float)Config.getAnisotropicFilterLevel();
            f2 = Math.min(f2, f);
            GL11.glTexParameterf(3553, 34046, f2);
        }
    }
    
    public static void bindTexture(final int p_bindTexture_0_) {
        GlStateManager.bindTexture(p_bindTexture_0_);
    }
    
    public static boolean isPowerOfTwo(final int p_isPowerOfTwo_0_) {
        final int i = MathHelper.roundUpToPowerOfTwo(p_isPowerOfTwo_0_);
        return i == p_isPowerOfTwo_0_;
    }
    
    public static BufferedImage scaleImage(final BufferedImage p_scaleImage_0_, final int p_scaleImage_1_) {
        final int i = p_scaleImage_0_.getWidth();
        final int j = p_scaleImage_0_.getHeight();
        final int k = j * p_scaleImage_1_ / i;
        final BufferedImage bufferedimage = new BufferedImage(p_scaleImage_1_, k, 2);
        final Graphics2D graphics2d = bufferedimage.createGraphics();
        Object object = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        if (p_scaleImage_1_ < i || p_scaleImage_1_ % i != 0) {
            object = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        }
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, object);
        graphics2d.drawImage(p_scaleImage_0_, 0, 0, p_scaleImage_1_, k, null);
        return bufferedimage;
    }
    
    public static BufferedImage scaleToPowerOfTwo(final BufferedImage p_scaleToPowerOfTwo_0_, final int p_scaleToPowerOfTwo_1_) {
        if (p_scaleToPowerOfTwo_0_ == null) {
            return p_scaleToPowerOfTwo_0_;
        }
        final int i = p_scaleToPowerOfTwo_0_.getWidth();
        final int j = p_scaleToPowerOfTwo_0_.getHeight();
        int k = Math.max(i, p_scaleToPowerOfTwo_1_);
        k = MathHelper.roundUpToPowerOfTwo(k);
        if (k == i) {
            return p_scaleToPowerOfTwo_0_;
        }
        final int l = j * k / i;
        final BufferedImage bufferedimage = new BufferedImage(k, l, 2);
        final Graphics2D graphics2d = bufferedimage.createGraphics();
        Object object = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        if (k % i != 0) {
            object = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        }
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, object);
        graphics2d.drawImage(p_scaleToPowerOfTwo_0_, 0, 0, k, l, null);
        return bufferedimage;
    }
    
    public static BufferedImage scaleMinTo(final BufferedImage p_scaleMinTo_0_, final int p_scaleMinTo_1_) {
        if (p_scaleMinTo_0_ == null) {
            return p_scaleMinTo_0_;
        }
        final int i = p_scaleMinTo_0_.getWidth();
        final int j = p_scaleMinTo_0_.getHeight();
        if (i >= p_scaleMinTo_1_) {
            return p_scaleMinTo_0_;
        }
        int k;
        for (k = i; k < p_scaleMinTo_1_; k *= 2) {}
        final int l = j * k / i;
        final BufferedImage bufferedimage = new BufferedImage(k, l, 2);
        final Graphics2D graphics2d = bufferedimage.createGraphics();
        final Object object = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, object);
        graphics2d.drawImage(p_scaleMinTo_0_, 0, 0, k, l, null);
        return bufferedimage;
    }
    
    public static Dimension getImageSize(final InputStream p_getImageSize_0_, final String p_getImageSize_1_) {
        final Iterator iterator = ImageIO.getImageReadersBySuffix(p_getImageSize_1_);
        while (iterator.hasNext()) {
            final ImageReader imagereader = iterator.next();
            Dimension dimension;
            try {
                final ImageInputStream imageinputstream = ImageIO.createImageInputStream(p_getImageSize_0_);
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
            imagereader.dispose();
            return dimension;
        }
        return null;
    }
    
    public static void dbgMipmaps(final TextureAtlasSprite p_dbgMipmaps_0_) {
        final int[][] aint = p_dbgMipmaps_0_.getFrameTextureData(0);
        for (int i = 0; i < aint.length; ++i) {
            final int[] aint2 = aint[i];
            if (aint2 == null) {
                Config.dbg(i + ": " + aint2);
            }
            else {
                Config.dbg(i + ": " + aint2.length);
            }
        }
    }
    
    public static void saveGlTexture(final String p_saveGlTexture_0_, final int p_saveGlTexture_1_, final int p_saveGlTexture_2_, final int p_saveGlTexture_3_, final int p_saveGlTexture_4_) {
        bindTexture(p_saveGlTexture_1_);
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        final File file1 = new File(p_saveGlTexture_0_);
        final File file2 = file1.getParentFile();
        if (file2 != null) {
            file2.mkdirs();
        }
        for (int i = 0; i < 16; ++i) {
            final File file3 = new File(String.valueOf(p_saveGlTexture_0_) + "_" + i + ".png");
            file3.delete();
        }
        for (int i2 = 0; i2 <= p_saveGlTexture_2_; ++i2) {
            final File file4 = new File(String.valueOf(p_saveGlTexture_0_) + "_" + i2 + ".png");
            final int j = p_saveGlTexture_3_ >> i2;
            final int k = p_saveGlTexture_4_ >> i2;
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
                Config.warn(exception.getClass().getName() + ": " + exception.getMessage());
            }
        }
    }
    
    public static int getGLMaximumTextureSize() {
        for (int i = 65536; i > 0; i >>= 1) {
            GL11.glTexImage2D(32868, 0, 6408, i, i, 0, 6408, 5121, (IntBuffer)null);
            final int j = GL11.glGetError();
            final int k = GL11.glGetTexLevelParameteri(32868, 0, 4096);
            if (k != 0) {
                return i;
            }
        }
        return -1;
    }
}
