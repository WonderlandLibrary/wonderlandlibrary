package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.optifine.Config;
import net.optifine.ConnectedTextures;
import net.optifine.Reflector;
import net.optifine.TextureUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.shadersmod.client.ShadersTex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;

public class TextureMap extends AbstractTexture implements ITickableTextureObject {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final boolean ENABLE_SKIP = Boolean.parseBoolean(System.getProperty("fml.skipFirstTextureLoad", "true"));
    public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
    public static final ResourceLocation locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
    private final List<TextureAtlasSprite> listAnimatedSprites;
    private final Map<String, TextureAtlasSprite> mapRegisteredSprites;
    private final Map<String, TextureAtlasSprite> mapUploadedSprites;
    private final String basePath;
    private final IIconCreator iconCreator;
    private int mipmapLevels;
    private final TextureAtlasSprite missingImage;
    private TextureAtlasSprite[] iconGrid;
    private final int iconGridSize;
    private int iconGridCountX;
    private int iconGridCountY;
    private double iconGridSizeU;
    private double iconGridSizeV;
    private int counterIndexInMap;
    public int atlasWidth;
    public int atlasHeight;

    public TextureMap(String p_i46099_1_) {
        this(p_i46099_1_, null);
    }

    public TextureMap(String p_i10_1_, boolean p_i10_2_) {
        this(p_i10_1_, null, p_i10_2_);
    }

    public TextureMap(String p_i46100_1_, IIconCreator iconCreatorIn) {
        this(p_i46100_1_, iconCreatorIn, false);
    }

    public TextureMap(String p_i11_1_, IIconCreator p_i11_2_, boolean p_i11_3_) {
        this.iconGrid = null;
        this.iconGridSize = -1;
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGridSizeU = -1.0D;
        this.iconGridSizeV = -1.0D;
        this.counterIndexInMap = 0;
        this.atlasWidth = 0;
        this.atlasHeight = 0;
        this.listAnimatedSprites = Lists.newArrayList();
        this.mapRegisteredSprites = Maps.newHashMap();
        this.mapUploadedSprites = Maps.newHashMap();
        this.missingImage = new TextureAtlasSprite("missingno");
        this.basePath = p_i11_1_;
        this.iconCreator = p_i11_2_;
    }

    private void initMissingImage() {
        final int i = this.getMinSpriteSize();
        final int[] aint = this.getMissingImageData(i);
        this.missingImage.setIconWidth(i);
        this.missingImage.setIconHeight(i);
        final int[][] aint1 = new int[this.mipmapLevels + 1][];
        aint1[0] = aint;
        this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][]{aint1}));
        this.missingImage.setIndexInMap(this.counterIndexInMap++);
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException {
        ShadersTex.resManager = resourceManager;

        if (this.iconCreator != null) {
            this.loadSprites(resourceManager, this.iconCreator);
        }
    }

    public void loadSprites(IResourceManager resourceManager, IIconCreator p_174943_2_) {
        this.mapRegisteredSprites.clear();
        this.counterIndexInMap = 0;
        p_174943_2_.registerSprites(this);

        if (this.mipmapLevels >= 4) {
            this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, resourceManager);
            Config.log("Mipmap levels: " + this.mipmapLevels);
        }

        this.initMissingImage();
        this.deleteGlTexture();
        this.loadTextureAtlas(resourceManager);
    }

    public void loadTextureAtlas(IResourceManager resourceManager) {
        Config.dbg("Multitexture: " + Config.isMultiTexture());

        if (Config.isMultiTexture()) {
            for (Object textureatlassprite : this.mapUploadedSprites.values()) {
                ((TextureAtlasSprite) textureatlassprite).deleteSpriteTexture();
            }
        }

        ConnectedTextures.updateIcons(this);
        final int l1 = Minecraft.getGLMaximumTextureSize();
        final Stitcher stitcher = new Stitcher(l1, l1, true, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int i = Integer.MAX_VALUE;
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, this);
        final int j = this.getMinSpriteSize();
        int k = 1 << this.mipmapLevels;

        for (Object entry : this.mapRegisteredSprites.entrySet()) {
            final TextureAtlasSprite textureatlassprite1 = (TextureAtlasSprite) ((Entry) entry).getValue();
            final ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite1.getIconName());
            final ResourceLocation resourcelocation1 = this.completeResourceLocation(resourcelocation, 0);

            if (!textureatlassprite1.hasCustomLoader(resourceManager, resourcelocation)) {
                try {
                    final IResource iresource = resourceManager.getResource(resourcelocation1);
                    final BufferedImage[] abufferedimage = new BufferedImage[1 + this.mipmapLevels];
                    abufferedimage[0] = TextureUtil.readBufferedImage(iresource.getInputStream());

                    if (this.mipmapLevels > 0 && abufferedimage != null) {
                        final int l = abufferedimage[0].getWidth();
                        abufferedimage[0] = TextureUtils.scaleToPowerOfTwo(abufferedimage[0], j);
                        final int i1 = abufferedimage[0].getWidth();

                        if (!TextureUtils.isPowerOfTwo(l)) {
                            Config.log("Scaled non power of 2: " + textureatlassprite1.getIconName() + ", " + l + " -> " + i1);
                        }
                    }

                    final TextureMetadataSection texturemetadatasection = iresource.getMetadata("texture");

                    if (texturemetadatasection != null) {
                        final List<Integer> list = texturemetadatasection.getListMipmaps();

                        if (!list.isEmpty()) {
                            final int k1 = abufferedimage[0].getWidth();
                            final int j1 = abufferedimage[0].getHeight();

                            if (MathHelper.roundUpToPowerOfTwo(k1) != k1 || MathHelper.roundUpToPowerOfTwo(j1) != j1) {
                                throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                            }
                        }

                        for (Object o : list) {
                            final int j3 = (Integer) o;

                            if (j3 > 0 && j3 < abufferedimage.length - 1 && abufferedimage[j3] == null) {
                                final ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation, j3);

                                try {
                                    abufferedimage[j3] = TextureUtil.readBufferedImage(resourceManager.getResource(resourcelocation2).getInputStream());
                                } catch (IOException ioexception) {
                                    LOGGER.error("Unable to load mip-level {} from: {}", j3, resourcelocation2, ioexception);
                                }
                            }
                        }
                    }

                    final AnimationMetadataSection animationmetadatasection = iresource.getMetadata("animation");
                    textureatlassprite1.loadSprite(abufferedimage, animationmetadatasection);
                } catch (RuntimeException runtimeexception) {
                    LOGGER.error("Unable to parse metadata from " + resourcelocation1, runtimeexception);
                    continue;
                } catch (IOException ioexception1) {
                    LOGGER.error("Using missing texture, unable to load " + resourcelocation1 + ", " + ioexception1.getClass().getName());
                    continue;
                }

                i = Math.min(i, Math.min(textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight()));
                final int k2 = Math.min(Integer.lowestOneBit(textureatlassprite1.getIconWidth()), Integer.lowestOneBit(textureatlassprite1.getIconHeight()));

                if (k2 < k) {
                    LOGGER.warn("Texture {} with size {}x{} limits mip level from {} to {}", resourcelocation1, textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight(), MathHelper.calculateLogBaseTwo(k), MathHelper.calculateLogBaseTwo(k2));
                    k = k2;
                }

                stitcher.addSprite(textureatlassprite1);
            } else if (!textureatlassprite1.load(resourceManager, resourcelocation)) {
                i = Math.min(i, Math.min(textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight()));
                stitcher.addSprite(textureatlassprite1);
            }
        }

        final int i2 = Math.min(i, k);
        int j2 = MathHelper.calculateLogBaseTwo(i2);

        if (j2 < 0) {
            j2 = 0;
        }

        if (j2 < this.mipmapLevels) {
            LOGGER.info("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.basePath, this.mipmapLevels, j2, i2);
            this.mipmapLevels = j2;
        }

        for (final Object textureatlassprite20 : this.mapRegisteredSprites.values()) {
            final TextureAtlasSprite textureatlassprite2 = (TextureAtlasSprite) textureatlassprite20;

            try {
                textureatlassprite2.generateMipmaps(this.mipmapLevels);
            } catch (Throwable throwable1) {
                final CrashReport crashReport = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
                final CrashReportCategory crashReportCategory = crashReport.makeCategory("Sprite being mipmapped");

                crashReportCategory.addCrashSectionCallable("Sprite name", textureatlassprite2::getIconName);
                crashReportCategory.addCrashSectionCallable("Sprite size", () -> textureatlassprite2.getIconWidth() + " x " + textureatlassprite2.getIconHeight());
                crashReportCategory.addCrashSectionCallable("Sprite frames", () -> textureatlassprite2.getFrameCount() + " frames");
                crashReportCategory.addCrashSection("Mipmap levels", this.mipmapLevels);

                throw new ReportedException(crashReport);
            }
        }

        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);

        try {
            stitcher.doStitch();
        } catch (StitcherException e) {
            throw e;
        }

        LOGGER.info("Created: {}x{} {}-atlas", stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), this.basePath);
        TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        final HashMap<String, TextureAtlasSprite> hashMap = Maps.newHashMap(this.mapRegisteredSprites);

        for (Object o : stitcher.getStichSlots()) {
            final TextureAtlasSprite textureAtlasSprite = (TextureAtlasSprite) o;
            final String s = textureAtlasSprite.getIconName();
            hashMap.remove(s);
            this.mapUploadedSprites.put(s, textureAtlasSprite);

            try {
                TextureUtil.uploadTextureMipmap(textureAtlasSprite.getFrameTextureData(0), textureAtlasSprite.getIconWidth(), textureAtlasSprite.getIconHeight(), textureAtlasSprite.getOriginX(), textureAtlasSprite.getOriginY(), false, false);
            } catch (Throwable throwable) {
                final CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
                final CrashReportCategory crashReportCategory = crashReport.makeCategory("Texture being stitched together");

                crashReportCategory.addCrashSection("Atlas path", this.basePath);
                crashReportCategory.addCrashSection("Sprite", textureAtlasSprite);

                throw new ReportedException(crashReport);
            }

            if (textureAtlasSprite.hasAnimationMetadata()) {
                this.listAnimatedSprites.add(textureAtlasSprite);
            }
        }

        for (Object textureatlassprite4 : hashMap.values()) {
            ((TextureAtlasSprite) textureatlassprite4).copyFrom(this.missingImage);
        }

        if (Config.isMultiTexture()) {
            final int l2 = stitcher.getCurrentWidth();
            final int i3 = stitcher.getCurrentHeight();

            for (Object o : stitcher.getStichSlots()) {
                final TextureAtlasSprite textureatlassprite5 = (TextureAtlasSprite) o;
                textureatlassprite5.sheetWidth = l2;
                textureatlassprite5.sheetHeight = i3;
                textureatlassprite5.mipmapLevels = this.mipmapLevels;
                final TextureAtlasSprite textureatlassprite6 = textureatlassprite5.spriteSingle;

                if (textureatlassprite6 != null) {
                    textureatlassprite6.sheetWidth = l2;
                    textureatlassprite6.sheetHeight = i3;
                    textureatlassprite6.mipmapLevels = this.mipmapLevels;
                    textureatlassprite5.bindSpriteTexture();
                    final boolean flag = false;
                    final boolean flag1 = true;
                    TextureUtil.uploadTextureMipmap(textureatlassprite6.getFrameTextureData(0), textureatlassprite6.getIconWidth(), textureatlassprite6.getIconHeight(), textureatlassprite6.getOriginX(), textureatlassprite6.getOriginY(), flag, flag1);
                }
            }

            Config.getMinecraft().getTextureManager().bindTexture(locationBlocksTexture);
        }

        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, this);

        if (Config.equals(System.getProperty("saveTextureMap"), "true")) {
            TextureUtil.saveGlTexture(this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }
    }

    public ResourceLocation completeResourceLocation(ResourceLocation location, int p_147634_2_) {
        return this.isAbsoluteLocation(location) ? p_147634_2_ == 0 ? new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".png") : new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + "mipmap" + p_147634_2_ + ".png") : p_147634_2_ == 0 ? new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", this.basePath, location.getResourcePath(), ".png")) : new ResourceLocation(location.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", this.basePath, location.getResourcePath(), p_147634_2_, ".png"));
    }

    public TextureAtlasSprite getAtlasSprite(String iconName) {
        TextureAtlasSprite textureatlassprite = this.mapUploadedSprites.get(iconName);

        if (textureatlassprite == null) {
            textureatlassprite = this.missingImage;
        }

        return textureatlassprite;
    }

    public void updateAnimations() {
        if (Config.isShaders()) {
            ShadersTex.updatingTex = this.getMultiTexID();
        }

        boolean flag = false;
        boolean flag1 = false;
        TextureUtil.bindTexture(this.getGlTextureId());

        for (Object textureatlassprite0 : this.listAnimatedSprites) {
            final TextureAtlasSprite textureatlassprite = (TextureAtlasSprite) textureatlassprite0;

            if (this.isTerrainAnimationActive(textureatlassprite)) {
                textureatlassprite.updateAnimation();

                if (textureatlassprite.spriteNormal != null) {
                    flag = true;
                }

                if (textureatlassprite.spriteSpecular != null) {
                    flag1 = true;
                }
            }
        }

        if (Config.isMultiTexture()) {
            for (Object textureatlassprite10 : this.listAnimatedSprites) {
                final TextureAtlasSprite textureatlassprite1 = (TextureAtlasSprite) textureatlassprite10;

                if (this.isTerrainAnimationActive(textureatlassprite1)) {
                    final TextureAtlasSprite textureatlassprite2 = textureatlassprite1.spriteSingle;

                    if (textureatlassprite2 != null) {
                        if (textureatlassprite1 == TextureUtils.iconClock || textureatlassprite1 == TextureUtils.iconCompass) {
                            textureatlassprite2.frameCounter = textureatlassprite1.frameCounter;
                        }

                        textureatlassprite1.bindSpriteTexture();
                        textureatlassprite2.updateAnimation();
                    }
                }
            }

            TextureUtil.bindTexture(this.getGlTextureId());
        }

        if (Config.isShaders()) {
            if (flag) {
                TextureUtil.bindTexture(this.getMultiTexID().norm);

                for (Object textureatlassprite30 : this.listAnimatedSprites) {
                    final TextureAtlasSprite textureatlassprite3 = (TextureAtlasSprite) textureatlassprite30;

                    if (textureatlassprite3.spriteNormal != null && this.isTerrainAnimationActive(textureatlassprite3)) {
                        if (textureatlassprite3 == TextureUtils.iconClock || textureatlassprite3 == TextureUtils.iconCompass) {
                            textureatlassprite3.spriteNormal.frameCounter = textureatlassprite3.frameCounter;
                        }

                        textureatlassprite3.spriteNormal.updateAnimation();
                    }
                }
            }

            if (flag1) {
                TextureUtil.bindTexture(this.getMultiTexID().spec);

                for (Object textureatlassprite40 : this.listAnimatedSprites) {
                    final TextureAtlasSprite textureatlassprite4 = (TextureAtlasSprite) textureatlassprite40;

                    if (textureatlassprite4.spriteSpecular != null && this.isTerrainAnimationActive(textureatlassprite4)) {
                        if (textureatlassprite4 == TextureUtils.iconClock || textureatlassprite4 == TextureUtils.iconCompass) {
                            textureatlassprite4.spriteNormal.frameCounter = textureatlassprite4.frameCounter;
                        }

                        textureatlassprite4.spriteSpecular.updateAnimation();
                    }
                }
            }

            if (flag || flag1) {
                TextureUtil.bindTexture(this.getGlTextureId());
            }
        }

        if (Config.isShaders()) {
            ShadersTex.updatingTex = null;
        }
    }

    public TextureAtlasSprite registerSprite(ResourceLocation location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null!");
        } else {
            TextureAtlasSprite textureatlassprite = this.mapRegisteredSprites.get(location.toString());

            if (textureatlassprite == null) {
                textureatlassprite = TextureAtlasSprite.makeAtlasSprite(location);
                this.mapRegisteredSprites.put(location.toString(), textureatlassprite);

                if (textureatlassprite.getIndexInMap() < 0) {
                    textureatlassprite.setIndexInMap(this.counterIndexInMap++);
                }
            }

            return textureatlassprite;
        }
    }

    public void tick() {
        this.updateAnimations();
    }

    public TextureAtlasSprite getMissingSprite() {
        return this.missingImage;
    }

    public TextureAtlasSprite getTextureExtry(String p_getTextureExtry_1_) {
        final ResourceLocation resourcelocation = new ResourceLocation(p_getTextureExtry_1_);
        return this.mapRegisteredSprites.get(resourcelocation.toString());
    }

    public boolean setTextureEntry(String p_setTextureEntry_1_, TextureAtlasSprite p_setTextureEntry_2_) {
        if (!this.mapRegisteredSprites.containsKey(p_setTextureEntry_1_)) {
            this.mapRegisteredSprites.put(p_setTextureEntry_1_, p_setTextureEntry_2_);

            if (p_setTextureEntry_2_.getIndexInMap() < 0) {
                p_setTextureEntry_2_.setIndexInMap(this.counterIndexInMap++);
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean setTextureEntry(TextureAtlasSprite p_setTextureEntry_1_) {
        return this.setTextureEntry(p_setTextureEntry_1_.getIconName(), p_setTextureEntry_1_);
    }

    public String getBasePath() {
        return this.basePath;
    }

    public int getMipmapLevels() {
        return this.mipmapLevels;
    }

    public void setMipmapLevels(int mipmapLevelsIn) {
        this.mipmapLevels = mipmapLevelsIn;
    }

    private boolean isAbsoluteLocation(ResourceLocation p_isAbsoluteLocation_1_) {
        final String s = p_isAbsoluteLocation_1_.getResourcePath();
        return this.isAbsoluteLocationPath(s);
    }

    private boolean isAbsoluteLocationPath(String p_isAbsoluteLocationPath_1_) {
        final String s = p_isAbsoluteLocationPath_1_.toLowerCase();
        return s.startsWith("mcpatcher/") || s.startsWith("optifine/");
    }

    public TextureAtlasSprite getSpriteSafe(String p_getSpriteSafe_1_) {
        final ResourceLocation resourcelocation = new ResourceLocation(p_getSpriteSafe_1_);
        return this.mapRegisteredSprites.get(resourcelocation.toString());
    }

    private boolean isTerrainAnimationActive(TextureAtlasSprite p_isTerrainAnimationActive_1_) {
        return p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterFlow ? p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaFlow ? p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer0 && p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer1 ? p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal ? Config.isAnimatedPortal() : p_isTerrainAnimationActive_1_ == TextureUtils.iconClock || p_isTerrainAnimationActive_1_ == TextureUtils.iconCompass || Config.isAnimatedTerrain() : Config.isAnimatedFire() : Config.isAnimatedLava() : Config.isAnimatedWater();
    }

    public int getCountRegisteredSprites() {
        return this.counterIndexInMap;
    }

    private int detectMaxMipmapLevel(Map p_detectMaxMipmapLevel_1_, IResourceManager p_detectMaxMipmapLevel_2_) {
        int i = this.detectMinimumSpriteSize(p_detectMaxMipmapLevel_1_, p_detectMaxMipmapLevel_2_, 20);

        if (i < 16) {
            i = 16;
        }

        i = MathHelper.roundUpToPowerOfTwo(i);

        if (i > 16) {
            Config.log("Sprite size: " + i);
        }

        int j = MathHelper.calculateLogBaseTwo(i);

        if (j < 4) {
            j = 4;
        }

        return j;
    }

    private int detectMinimumSpriteSize(Map p_detectMinimumSpriteSize_1_, IResourceManager p_detectMinimumSpriteSize_2_, int p_detectMinimumSpriteSize_3_) {
        final Map<Integer, Integer> map = new HashMap<>();

        for (Object entry : p_detectMinimumSpriteSize_1_.entrySet()) {
            final TextureAtlasSprite textureatlassprite = (TextureAtlasSprite) ((Entry) entry).getValue();
            final ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite.getIconName());
            final ResourceLocation resourcelocation1 = this.completeResourceLocation(resourcelocation, 0);

            if (!textureatlassprite.hasCustomLoader(p_detectMinimumSpriteSize_2_, resourcelocation)) {
                try {
                    final IResource iresource = p_detectMinimumSpriteSize_2_.getResource(resourcelocation1);

                    if (iresource != null) {
                        final InputStream inputstream = iresource.getInputStream();

                        if (inputstream != null) {
                            final Dimension dimension = TextureUtils.getImageSize(inputstream, "png");

                            if (dimension != null) {
                                final int i = dimension.width;
                                final int j = MathHelper.roundUpToPowerOfTwo(i);

                                if (!map.containsKey(j)) {
                                    map.put(j, 1);
                                } else {
                                    final int k = map.get(j);
                                    map.put(j, k + 1);
                                }
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }

        int l = 0;
        final Set<Integer> set = map.keySet();
        final Set<Integer> set1 = new TreeSet<>(set);
        int l1;

        for (final Iterator<Integer> iterator = set1.iterator(); iterator.hasNext(); l += l1) {
            final int j1 = iterator.next();
            l1 = map.get(j1);
        }

        int i1 = 16;
        int k1 = 0;
        l1 = l * p_detectMinimumSpriteSize_3_ / 100;

        for (Object o : set1) {
            final int i2 = (Integer) o;
            final int j2 = map.get(i2);
            k1 += j2;

            if (i2 > i1) {
                i1 = i2;
            }

            if (k1 > l1) {
                return i1;
            }
        }

        return i1;
    }

    private int getMinSpriteSize() {
        int i = 1 << this.mipmapLevels;

        if (i < 8) {
            i = 8;
        }

        return i;
    }

    private int[] getMissingImageData(int p_getMissingImageData_1_) {
        final BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
        bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.missingTextureData, 0, 16);
        final BufferedImage bufferedimage1 = TextureUtils.scaleToPowerOfTwo(bufferedimage, p_getMissingImageData_1_);
        final int[] aint = new int[p_getMissingImageData_1_ * p_getMissingImageData_1_];
        bufferedimage1.getRGB(0, 0, p_getMissingImageData_1_, p_getMissingImageData_1_, aint, 0, p_getMissingImageData_1_);
        return aint;
    }

    public boolean isTextureBound() {
        final int i = GlStateManager.getBoundTexture();
        final int j = this.getGlTextureId();
        return i == j;
    }

    private void updateIconGrid(int p_updateIconGrid_1_, int p_updateIconGrid_2_) {
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGrid = null;

        if (this.iconGridSize > 0) {
            this.iconGridCountX = p_updateIconGrid_1_ / this.iconGridSize;
            this.iconGridCountY = p_updateIconGrid_2_ / this.iconGridSize;
            this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
            this.iconGridSizeU = 1.0D / (double) this.iconGridCountX;
            this.iconGridSizeV = 1.0D / (double) this.iconGridCountY;

            for (Object textureatlassprite0 : this.mapUploadedSprites.values()) {
                final TextureAtlasSprite textureatlassprite = (TextureAtlasSprite) textureatlassprite0;
                final double d0 = 0.5D / (double) p_updateIconGrid_1_;
                final double d1 = 0.5D / (double) p_updateIconGrid_2_;
                final double d2 = (double) Math.min(textureatlassprite.getMinU(), textureatlassprite.getMaxU()) + d0;
                final double d3 = (double) Math.min(textureatlassprite.getMinV(), textureatlassprite.getMaxV()) + d1;
                final double d4 = (double) Math.max(textureatlassprite.getMinU(), textureatlassprite.getMaxU()) - d0;
                final double d5 = (double) Math.max(textureatlassprite.getMinV(), textureatlassprite.getMaxV()) - d1;
                final int i = (int) (d2 / this.iconGridSizeU);
                final int j = (int) (d3 / this.iconGridSizeV);
                final int k = (int) (d4 / this.iconGridSizeU);
                final int l = (int) (d5 / this.iconGridSizeV);

                for (int i1 = i; i1 <= k; ++i1) {
                    if (i1 >= 0 && i1 < this.iconGridCountX) {
                        for (int j1 = j; j1 <= l; ++j1) {
                            if (j1 >= 0 && j1 < this.iconGridCountX) {
                                final int k1 = j1 * this.iconGridCountX + i1;
                                this.iconGrid[k1] = textureatlassprite;
                            } else {
                                Config.warn("Invalid grid V: " + j1 + ", icon: " + textureatlassprite.getIconName());
                            }
                        }
                    } else {
                        Config.warn("Invalid grid U: " + i1 + ", icon: " + textureatlassprite.getIconName());
                    }
                }
            }
        }
    }

    public TextureAtlasSprite getIconByUV(double p_getIconByUV_1_, double p_getIconByUV_3_) {
        if (this.iconGrid == null) {
            return null;
        } else {
            final int i = (int) (p_getIconByUV_1_ / this.iconGridSizeU);
            final int j = (int) (p_getIconByUV_3_ / this.iconGridSizeV);
            final int k = j * this.iconGridCountX + i;
            return k >= 0 && k <= this.iconGrid.length ? this.iconGrid[k] : null;
        }
    }

}
