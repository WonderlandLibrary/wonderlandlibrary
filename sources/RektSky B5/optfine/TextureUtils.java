/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import optfine.BetterGrass;
import optfine.BetterSnow;
import optfine.Config;
import optfine.CustomColorizer;
import optfine.CustomSky;
import optfine.NaturalTextures;
import optfine.RandomMobs;
import optfine.TextureAnimations;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class TextureUtils {
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
        TextureMap texturemap = TextureUtils.getTextureMapBlocks();
        if (texturemap != null) {
            String s2 = SPRITE_LOCATION_PREFIX;
            iconGrassTop = texturemap.getSpriteSafe(s2 + texGrassTop);
            iconGrassSide = texturemap.getSpriteSafe(s2 + texGrassSide);
            iconGrassSideOverlay = texturemap.getSpriteSafe(s2 + texGrassSideOverlay);
            iconSnow = texturemap.getSpriteSafe(s2 + texSnow);
            iconGrassSideSnowed = texturemap.getSpriteSafe(s2 + texGrassSideSnowed);
            iconMyceliumSide = texturemap.getSpriteSafe(s2 + texMyceliumSide);
            iconMyceliumTop = texturemap.getSpriteSafe(s2 + texMyceliumTop);
            iconWaterStill = texturemap.getSpriteSafe(s2 + texWaterStill);
            iconWaterFlow = texturemap.getSpriteSafe(s2 + texWaterFlow);
            iconLavaStill = texturemap.getSpriteSafe(s2 + texLavaStill);
            iconLavaFlow = texturemap.getSpriteSafe(s2 + texLavaFlow);
            iconFireLayer0 = texturemap.getSpriteSafe(s2 + texFireLayer0);
            iconFireLayer1 = texturemap.getSpriteSafe(s2 + texFireLayer1);
            iconPortal = texturemap.getSpriteSafe(s2 + texPortal);
            iconGlass = texturemap.getSpriteSafe(s2 + texGlass);
            iconGlassPaneTop = texturemap.getSpriteSafe(s2 + texGlassPaneTop);
        }
    }

    public static BufferedImage fixTextureDimensions(String p_fixTextureDimensions_0_, BufferedImage p_fixTextureDimensions_1_) {
        int j2;
        int i2;
        if ((p_fixTextureDimensions_0_.startsWith("/mob/zombie") || p_fixTextureDimensions_0_.startsWith("/mob/pigzombie")) && (i2 = p_fixTextureDimensions_1_.getWidth()) == (j2 = p_fixTextureDimensions_1_.getHeight()) * 2) {
            BufferedImage bufferedimage = new BufferedImage(i2, j2 * 2, 2);
            Graphics2D graphics2d = bufferedimage.createGraphics();
            graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2d.drawImage(p_fixTextureDimensions_1_, 0, 0, i2, j2, null);
            return bufferedimage;
        }
        return p_fixTextureDimensions_1_;
    }

    public static int ceilPowerOfTwo(int p_ceilPowerOfTwo_0_) {
        int i2;
        for (i2 = 1; i2 < p_ceilPowerOfTwo_0_; i2 *= 2) {
        }
        return i2;
    }

    public static int getPowerOfTwo(int p_getPowerOfTwo_0_) {
        int i2 = 1;
        int j2 = 0;
        while (i2 < p_getPowerOfTwo_0_) {
            i2 *= 2;
            ++j2;
        }
        return j2;
    }

    public static int twoToPower(int p_twoToPower_0_) {
        int i2 = 1;
        for (int j2 = 0; j2 < p_twoToPower_0_; ++j2) {
            i2 *= 2;
        }
        return i2;
    }

    public static void refreshBlockTextures() {
    }

    public static ITextureObject getTexture(String p_getTexture_0_) {
        return TextureUtils.getTexture(new ResourceLocation(p_getTexture_0_));
    }

    public static ITextureObject getTexture(ResourceLocation p_getTexture_0_) {
        ITextureObject itextureobject = Config.getTextureManager().getTexture(p_getTexture_0_);
        if (itextureobject != null) {
            return itextureobject;
        }
        if (!Config.hasResource(p_getTexture_0_)) {
            return null;
        }
        SimpleTexture simpletexture = new SimpleTexture(p_getTexture_0_);
        Config.getTextureManager().loadTexture(p_getTexture_0_, simpletexture);
        return simpletexture;
    }

    public static void resourcesReloaded(IResourceManager p_resourcesReloaded_0_) {
        if (TextureUtils.getTextureMapBlocks() != null) {
            Config.dbg("*** Reloading custom textures ***");
            CustomSky.reset();
            TextureAnimations.reset();
            TextureUtils.update();
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
        IResourceManager iresourcemanager = Config.getResourceManager();
        if (iresourcemanager instanceof IReloadableResourceManager) {
            IReloadableResourceManager ireloadableresourcemanager = (IReloadableResourceManager)iresourcemanager;
            IResourceManagerReloadListener iresourcemanagerreloadlistener = new IResourceManagerReloadListener(){

                @Override
                public void onResourceManagerReload(IResourceManager resourceManager) {
                    TextureUtils.resourcesReloaded(resourceManager);
                }
            };
            ireloadableresourcemanager.registerReloadListener(iresourcemanagerreloadlistener);
        }
        ITickableTextureObject itickabletextureobject = new ITickableTextureObject(){

            @Override
            public void tick() {
                TextureAnimations.updateCustomAnimations();
            }

            @Override
            public void loadTexture(IResourceManager resourceManager) throws IOException {
            }

            @Override
            public int getGlTextureId() {
                return 0;
            }

            @Override
            public void setBlurMipmap(boolean p_174936_1_, boolean p_174936_2_) {
            }

            @Override
            public void restoreLastBlurMipmap() {
            }
        };
        ResourceLocation resourcelocation = new ResourceLocation("optifine/TickableTextures");
        Config.getTextureManager().loadTickableTexture(resourcelocation, itickabletextureobject);
    }

    public static String fixResourcePath(String p_fixResourcePath_0_, String p_fixResourcePath_1_) {
        String s2 = "assets/minecraft/";
        if (p_fixResourcePath_0_.startsWith(s2)) {
            p_fixResourcePath_0_ = p_fixResourcePath_0_.substring(s2.length());
            return p_fixResourcePath_0_;
        }
        if (p_fixResourcePath_0_.startsWith("./")) {
            p_fixResourcePath_0_ = p_fixResourcePath_0_.substring(2);
            if (!p_fixResourcePath_1_.endsWith("/")) {
                p_fixResourcePath_1_ = p_fixResourcePath_1_ + "/";
            }
            p_fixResourcePath_0_ = p_fixResourcePath_1_ + p_fixResourcePath_0_;
            return p_fixResourcePath_0_;
        }
        String s1 = "mcpatcher/";
        if (p_fixResourcePath_0_.startsWith("~/")) {
            p_fixResourcePath_0_ = p_fixResourcePath_0_.substring(2);
            p_fixResourcePath_0_ = s1 + p_fixResourcePath_0_;
            return p_fixResourcePath_0_;
        }
        if (p_fixResourcePath_0_.startsWith("/")) {
            p_fixResourcePath_0_ = s1 + p_fixResourcePath_0_.substring(1);
            return p_fixResourcePath_0_;
        }
        return p_fixResourcePath_0_;
    }

    public static String getBasePath(String p_getBasePath_0_) {
        int i2 = p_getBasePath_0_.lastIndexOf(47);
        return i2 < 0 ? "" : p_getBasePath_0_.substring(0, i2);
    }

    public static void applyAnisotropicLevel() {
        if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
            float f2 = GL11.glGetFloat(34047);
            float f1 = Config.getAnisotropicFilterLevel();
            f1 = Math.min(f1, f2);
            GL11.glTexParameterf(3553, 34046, f1);
        }
    }

    public static void bindTexture(int p_bindTexture_0_) {
        GlStateManager.bindTexture(p_bindTexture_0_);
    }

    public static boolean isPowerOfTwo(int p_isPowerOfTwo_0_) {
        int i2 = MathHelper.roundUpToPowerOfTwo(p_isPowerOfTwo_0_);
        return i2 == p_isPowerOfTwo_0_;
    }

    public static BufferedImage scaleToPowerOfTwo(BufferedImage p_scaleToPowerOfTwo_0_, int p_scaleToPowerOfTwo_1_) {
        if (p_scaleToPowerOfTwo_0_ == null) {
            return p_scaleToPowerOfTwo_0_;
        }
        int i2 = p_scaleToPowerOfTwo_0_.getWidth();
        int j2 = p_scaleToPowerOfTwo_0_.getHeight();
        int k2 = Math.max(i2, p_scaleToPowerOfTwo_1_);
        if ((k2 = MathHelper.roundUpToPowerOfTwo(k2)) == i2) {
            return p_scaleToPowerOfTwo_0_;
        }
        int l2 = j2 * k2 / i2;
        BufferedImage bufferedimage = new BufferedImage(k2, l2, 2);
        Graphics2D graphics2d = bufferedimage.createGraphics();
        Object object = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        if (k2 % i2 != 0) {
            object = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        }
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, object);
        graphics2d.drawImage(p_scaleToPowerOfTwo_0_, 0, 0, k2, l2, null);
        return bufferedimage;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Dimension getImageSize(InputStream p_getImageSize_0_, String p_getImageSize_1_) {
        Iterator<ImageReader> iterator = ImageIO.getImageReadersBySuffix(p_getImageSize_1_);
        while (iterator.hasNext()) {
            Dimension dimension;
            ImageReader imagereader = iterator.next();
            try {
                ImageInputStream imageinputstream = ImageIO.createImageInputStream(p_getImageSize_0_);
                imagereader.setInput(imageinputstream);
                int i2 = imagereader.getWidth(imagereader.getMinIndex());
                int j2 = imagereader.getHeight(imagereader.getMinIndex());
                dimension = new Dimension(i2, j2);
            }
            catch (IOException var11) {}
            continue;
            finally {
                imagereader.dispose();
                continue;
            }
            return dimension;
        }
        return null;
    }

    static {
        staticBuffer = GLAllocation.createDirectIntBuffer(256);
    }
}

