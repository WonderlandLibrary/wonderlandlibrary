// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import net.optifine.EmissiveTextures;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Set;
import java.awt.Dimension;
import java.io.InputStream;
import java.util.Collection;
import java.util.TreeSet;
import java.util.HashMap;
import net.optifine.SmartAnimations;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.client.resources.IResource;
import java.util.Iterator;
import net.optifine.shaders.ShadersTex;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.MathHelper;
import net.minecraft.client.resources.data.TextureMetadataSection;
import java.awt.image.BufferedImage;
import net.optifine.util.TextureUtils;
import net.optifine.BetterGrass;
import net.optifine.CustomItems;
import net.optifine.ConnectedTextures;
import net.minecraft.src.Config;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.optifine.util.CounterInt;
import java.util.Map;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class TextureMap extends AbstractTexture implements ITickableTextureObject
{
    private static final boolean ENABLE_SKIP;
    private static final Logger logger;
    public static final ResourceLocation LOCATION_MISSING_TEXTURE;
    public static final ResourceLocation locationBlocksTexture;
    private final List<TextureAtlasSprite> listAnimatedSprites;
    private final Map<String, TextureAtlasSprite> mapRegisteredSprites;
    private final Map<String, TextureAtlasSprite> mapUploadedSprites;
    private final String basePath;
    private final IIconCreator iconCreator;
    private int mipmapLevels;
    private final TextureAtlasSprite missingImage;
    private boolean skipFirst;
    private TextureAtlasSprite[] iconGrid;
    private int iconGridSize;
    private int iconGridCountX;
    private int iconGridCountY;
    private double iconGridSizeU;
    private double iconGridSizeV;
    private final CounterInt counterIndexInMap;
    public int atlasWidth;
    public int atlasHeight;
    private int countAnimationsActive;
    private int frameCountAnimations;
    
    public TextureMap(final String p_i46099_1_) {
        this(p_i46099_1_, null);
    }
    
    public TextureMap(final String p_i5_1_, final boolean p_i5_2_) {
        this(p_i5_1_, null, p_i5_2_);
    }
    
    public TextureMap(final String p_i46100_1_, final IIconCreator iconCreatorIn) {
        this(p_i46100_1_, iconCreatorIn, false);
    }
    
    public TextureMap(final String p_i6_1_, final IIconCreator p_i6_2_, final boolean p_i6_3_) {
        this.skipFirst = false;
        this.iconGrid = null;
        this.iconGridSize = -1;
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGridSizeU = -1.0;
        this.iconGridSizeV = -1.0;
        this.counterIndexInMap = new CounterInt(0);
        this.atlasWidth = 0;
        this.atlasHeight = 0;
        this.listAnimatedSprites = (List<TextureAtlasSprite>)Lists.newArrayList();
        this.mapRegisteredSprites = (Map<String, TextureAtlasSprite>)Maps.newHashMap();
        this.mapUploadedSprites = (Map<String, TextureAtlasSprite>)Maps.newHashMap();
        this.missingImage = new TextureAtlasSprite("missingno");
        this.basePath = p_i6_1_;
        this.iconCreator = p_i6_2_;
        this.skipFirst = (p_i6_3_ && TextureMap.ENABLE_SKIP);
    }
    
    private void initMissingImage() {
        final int i = this.getMinSpriteSize();
        final int[] aint = this.getMissingImageData(i);
        this.missingImage.setIconWidth(i);
        this.missingImage.setIconHeight(i);
        final int[][] aint2 = new int[this.mipmapLevels + 1][];
        aint2[0] = aint;
        this.missingImage.setFramesTextureData(Lists.newArrayList((Object[])new int[][][] { aint2 }));
        this.missingImage.setIndexInMap(this.counterIndexInMap.nextValue());
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        if (this.iconCreator != null) {
            this.loadSprites(resourceManager, this.iconCreator);
        }
    }
    
    public void loadSprites(final IResourceManager resourceManager, final IIconCreator p_174943_2_) {
        this.mapRegisteredSprites.clear();
        this.counterIndexInMap.reset();
        p_174943_2_.registerSprites(this);
        if (this.mipmapLevels >= 4) {
            this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, resourceManager);
            Config.log("Mipmap levels: " + this.mipmapLevels);
        }
        this.initMissingImage();
        this.deleteGlTexture();
        this.loadTextureAtlas(resourceManager);
    }
    
    public void loadTextureAtlas(final IResourceManager resourceManager) {
        Config.dbg("Multitexture: " + Config.isMultiTexture());
        if (Config.isMultiTexture()) {
            for (final TextureAtlasSprite textureatlassprite : this.mapUploadedSprites.values()) {
                textureatlassprite.deleteSpriteTexture();
            }
        }
        ConnectedTextures.updateIcons(this);
        CustomItems.updateIcons(this);
        BetterGrass.updateIcons(this);
        final int i2 = TextureUtils.getGLMaximumTextureSize();
        final Stitcher stitcher = new Stitcher(i2, i2, true, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int j = Integer.MAX_VALUE;
        final int k = this.getMinSpriteSize();
        this.iconGridSize = k;
        int l = 1 << this.mipmapLevels;
        int m = 0;
        int i3 = 0;
        for (final Map.Entry<String, TextureAtlasSprite> entry : this.mapRegisteredSprites.entrySet()) {
            if (this.skipFirst) {
                break;
            }
            final TextureAtlasSprite textureatlassprite2 = entry.getValue();
            final ResourceLocation resourcelocation1 = new ResourceLocation(textureatlassprite2.getIconName());
            final ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation1, 0);
            textureatlassprite2.updateIndexInMap(this.counterIndexInMap);
            if (textureatlassprite2.hasCustomLoader(resourceManager, resourcelocation1)) {
                if (!textureatlassprite2.load(resourceManager, resourcelocation1)) {
                    j = Math.min(j, Math.min(textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight()));
                    stitcher.addSprite(textureatlassprite2);
                    Config.detail("Custom loader (skipped): " + textureatlassprite2);
                    ++i3;
                }
                Config.detail("Custom loader: " + textureatlassprite2);
                ++m;
            }
            else {
                try {
                    final IResource iresource = resourceManager.getResource(resourcelocation2);
                    final BufferedImage[] abufferedimage = new BufferedImage[1 + this.mipmapLevels];
                    abufferedimage[0] = TextureUtil.readBufferedImage(iresource.getInputStream());
                    final int k2 = abufferedimage[0].getWidth();
                    final int l2 = abufferedimage[0].getHeight();
                    if (k2 < 1 || l2 < 1) {
                        Config.warn("Invalid sprite size: " + textureatlassprite2);
                        continue;
                    }
                    if (k2 < k || this.mipmapLevels > 0) {
                        final int i4 = (this.mipmapLevels > 0) ? TextureUtils.scaleToGrid(k2, k) : TextureUtils.scaleToMin(k2, k);
                        if (i4 != k2) {
                            if (!TextureUtils.isPowerOfTwo(k2)) {
                                Config.log("Scaled non power of 2: " + textureatlassprite2.getIconName() + ", " + k2 + " -> " + i4);
                            }
                            else {
                                Config.log("Scaled too small texture: " + textureatlassprite2.getIconName() + ", " + k2 + " -> " + i4);
                            }
                            abufferedimage[0] = TextureUtils.scaleImage(abufferedimage[0], i4);
                        }
                    }
                    final TextureMetadataSection texturemetadatasection = iresource.getMetadata("texture");
                    if (texturemetadatasection != null) {
                        final List<Integer> list1 = texturemetadatasection.getListMipmaps();
                        if (!list1.isEmpty()) {
                            final int k3 = abufferedimage[0].getWidth();
                            final int l3 = abufferedimage[0].getHeight();
                            if (MathHelper.roundUpToPowerOfTwo(k3) != k3 || MathHelper.roundUpToPowerOfTwo(l3) != l3) {
                                throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                            }
                        }
                        for (final Integer integer : list1) {
                            final int j2 = integer;
                            if (j2 > 0 && j2 < abufferedimage.length - 1 && abufferedimage[j2] == null) {
                                final ResourceLocation resourcelocation3 = this.completeResourceLocation(resourcelocation1, j2);
                                try {
                                    abufferedimage[j2] = TextureUtil.readBufferedImage(resourceManager.getResource(resourcelocation3).getInputStream());
                                }
                                catch (IOException ioexception) {
                                    TextureMap.logger.error("Unable to load miplevel {} from: {}", new Object[] { j2, resourcelocation3, ioexception });
                                }
                            }
                        }
                    }
                    final AnimationMetadataSection animationmetadatasection = iresource.getMetadata("animation");
                    textureatlassprite2.loadSprite(abufferedimage, animationmetadatasection);
                }
                catch (RuntimeException runtimeexception) {
                    TextureMap.logger.error("Unable to parse metadata from " + resourcelocation2, (Throwable)runtimeexception);
                    continue;
                }
                catch (IOException ioexception2) {
                    TextureMap.logger.error("Using missing texture, unable to load " + resourcelocation2 + ", " + ioexception2.getClass().getName());
                    continue;
                }
                j = Math.min(j, Math.min(textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight()));
                final int j3 = Math.min(Integer.lowestOneBit(textureatlassprite2.getIconWidth()), Integer.lowestOneBit(textureatlassprite2.getIconHeight()));
                if (j3 < l) {
                    TextureMap.logger.warn("Texture {} with size {}x{} limits mip level from {} to {}", new Object[] { resourcelocation2, textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight(), MathHelper.calculateLogBaseTwo(l), MathHelper.calculateLogBaseTwo(j3) });
                    l = j3;
                }
                stitcher.addSprite(textureatlassprite2);
            }
        }
        if (m > 0) {
            Config.dbg("Custom loader sprites: " + m);
        }
        if (i3 > 0) {
            Config.dbg("Custom loader sprites (skipped): " + i3);
        }
        final int j4 = Math.min(j, l);
        int k4 = MathHelper.calculateLogBaseTwo(j4);
        if (k4 < 0) {
            k4 = 0;
        }
        if (k4 < this.mipmapLevels) {
            TextureMap.logger.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", new Object[] { this.basePath, this.mipmapLevels, k4, j4 });
            this.mipmapLevels = k4;
        }
        for (final TextureAtlasSprite textureatlassprite3 : this.mapRegisteredSprites.values()) {
            if (this.skipFirst) {
                break;
            }
            try {
                textureatlassprite3.generateMipmaps(this.mipmapLevels);
            }
            catch (Throwable throwable1) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
                crashreportcategory.addCrashSectionCallable("Sprite name", textureatlassprite3::getIconName);
                final TextureAtlasSprite textureAtlasSprite;
                crashreportcategory.addCrashSectionCallable("Sprite size", () -> textureAtlasSprite.getIconWidth() + " x " + textureAtlasSprite.getIconHeight());
                crashreportcategory.addCrashSectionCallable("Sprite frames", () -> textureatlassprite3.getFrameCount() + " frames");
                crashreportcategory.addCrashSection("Mipmap levels", this.mipmapLevels);
                throw new ReportedException(crashreport);
            }
        }
        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);
        this.skipFirst = false;
        stitcher.doStitch();
        TextureMap.logger.info("Created: {}x{} {}-atlas", new Object[] { stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), this.basePath });
        if (Config.isShaders()) {
            ShadersTex.allocateTextureMap(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), stitcher, this);
        }
        else {
            TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }
        final Map<String, TextureAtlasSprite> map = (Map<String, TextureAtlasSprite>)Maps.newHashMap((Map)this.mapRegisteredSprites);
        for (final TextureAtlasSprite textureatlassprite4 : stitcher.getStichSlots()) {
            final String s = textureatlassprite4.getIconName();
            map.remove(s);
            this.mapUploadedSprites.put(s, textureatlassprite4);
            try {
                if (Config.isShaders()) {
                    ShadersTex.uploadTexSubForLoadAtlas(this, textureatlassprite4.getIconName(), textureatlassprite4.getFrameTextureData(0), textureatlassprite4.getIconWidth(), textureatlassprite4.getIconHeight(), textureatlassprite4.getOriginX(), textureatlassprite4.getOriginY(), false, false);
                }
                else {
                    TextureUtil.uploadTextureMipmap(textureatlassprite4.getFrameTextureData(0), textureatlassprite4.getIconWidth(), textureatlassprite4.getIconHeight(), textureatlassprite4.getOriginX(), textureatlassprite4.getOriginY(), false, false);
                }
            }
            catch (Throwable throwable2) {
                final CrashReport crashreport2 = CrashReport.makeCrashReport(throwable2, "Stitching texture atlas");
                final CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Texture being stitched together");
                crashreportcategory2.addCrashSection("Atlas path", this.basePath);
                crashreportcategory2.addCrashSection("Sprite", textureatlassprite4);
                throw new ReportedException(crashreport2);
            }
            if (textureatlassprite4.hasAnimationMetadata()) {
                textureatlassprite4.setAnimationIndex(this.listAnimatedSprites.size());
                this.listAnimatedSprites.add(textureatlassprite4);
            }
        }
        for (final TextureAtlasSprite textureatlassprite5 : map.values()) {
            textureatlassprite5.copyFrom(this.missingImage);
        }
        Config.log("Animated sprites: " + this.listAnimatedSprites.size());
        if (Config.isMultiTexture()) {
            final int l4 = stitcher.getCurrentWidth();
            final int i5 = stitcher.getCurrentHeight();
            for (final TextureAtlasSprite textureatlassprite6 : stitcher.getStichSlots()) {
                textureatlassprite6.sheetWidth = l4;
                textureatlassprite6.sheetHeight = i5;
                textureatlassprite6.mipmapLevels = this.mipmapLevels;
                final TextureAtlasSprite textureatlassprite7 = textureatlassprite6.spriteSingle;
                if (textureatlassprite7 != null) {
                    if (textureatlassprite7.getIconWidth() <= 0) {
                        textureatlassprite7.setIconWidth(textureatlassprite6.getIconWidth());
                        textureatlassprite7.setIconHeight(textureatlassprite6.getIconHeight());
                        textureatlassprite7.initSprite(textureatlassprite6.getIconWidth(), textureatlassprite6.getIconHeight(), 0, 0, false);
                        textureatlassprite7.clearFramesTextureData();
                        final List<int[][]> list2 = textureatlassprite6.getFramesTextureData();
                        textureatlassprite7.setFramesTextureData(list2);
                        textureatlassprite7.setAnimationMetadata(textureatlassprite6.getAnimationMetadata());
                    }
                    textureatlassprite7.sheetWidth = l4;
                    textureatlassprite7.sheetHeight = i5;
                    textureatlassprite7.mipmapLevels = this.mipmapLevels;
                    textureatlassprite7.setAnimationIndex(textureatlassprite6.getAnimationIndex());
                    textureatlassprite6.bindSpriteTexture();
                    final boolean flag1 = false;
                    final boolean flag2 = true;
                    try {
                        TextureUtil.uploadTextureMipmap(textureatlassprite7.getFrameTextureData(0), textureatlassprite7.getIconWidth(), textureatlassprite7.getIconHeight(), textureatlassprite7.getOriginX(), textureatlassprite7.getOriginY(), flag1, flag2);
                    }
                    catch (Exception exception) {
                        Config.dbg("Error uploading sprite single: " + textureatlassprite7 + ", parent: " + textureatlassprite6);
                        exception.printStackTrace();
                    }
                }
            }
            Config.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        }
        this.updateIconGrid(stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        if (Config.equals(System.getProperty("saveTextureMap"), "true")) {
            Config.dbg("Exporting texture map: " + this.basePath);
            TextureUtils.saveGlTexture("debug/" + this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }
    }
    
    public ResourceLocation completeResourceLocation(final ResourceLocation p_completeResourceLocation_1_) {
        return this.completeResourceLocation(p_completeResourceLocation_1_, 0);
    }
    
    public ResourceLocation completeResourceLocation(final ResourceLocation location, final int p_147634_2_) {
        return this.isAbsoluteLocation(location) ? new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".png") : ((p_147634_2_ == 0) ? new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", this.basePath, location.getResourcePath(), ".png")) : new ResourceLocation(location.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", this.basePath, location.getResourcePath(), p_147634_2_, ".png")));
    }
    
    public TextureAtlasSprite getAtlasSprite(final String iconName) {
        TextureAtlasSprite textureatlassprite = this.mapUploadedSprites.get(iconName);
        if (textureatlassprite == null) {
            textureatlassprite = this.missingImage;
        }
        return textureatlassprite;
    }
    
    public void updateAnimations() {
        boolean flag = false;
        boolean flag2 = false;
        TextureUtil.bindTexture(this.getGlTextureId());
        int i = 0;
        for (final TextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {
            if (this.isTerrainAnimationActive(textureatlassprite)) {
                textureatlassprite.updateAnimation();
                if (textureatlassprite.isAnimationActive()) {
                    ++i;
                }
                if (textureatlassprite.spriteNormal != null) {
                    flag = true;
                }
                if (textureatlassprite.spriteSpecular == null) {
                    continue;
                }
                flag2 = true;
            }
        }
        if (Config.isMultiTexture()) {
            for (final TextureAtlasSprite textureatlassprite2 : this.listAnimatedSprites) {
                if (this.isTerrainAnimationActive(textureatlassprite2)) {
                    final TextureAtlasSprite textureatlassprite3 = textureatlassprite2.spriteSingle;
                    if (textureatlassprite3 == null) {
                        continue;
                    }
                    if (textureatlassprite2 == TextureUtils.iconClock || textureatlassprite2 == TextureUtils.iconCompass) {
                        textureatlassprite3.frameCounter = textureatlassprite2.frameCounter;
                    }
                    textureatlassprite2.bindSpriteTexture();
                    textureatlassprite3.updateAnimation();
                    if (!textureatlassprite3.isAnimationActive()) {
                        continue;
                    }
                    ++i;
                }
            }
            TextureUtil.bindTexture(this.getGlTextureId());
        }
        if (Config.isShaders()) {
            if (flag) {
                TextureUtil.bindTexture(this.getMultiTexID().norm);
                for (final TextureAtlasSprite textureatlassprite4 : this.listAnimatedSprites) {
                    if (textureatlassprite4.spriteNormal != null && this.isTerrainAnimationActive(textureatlassprite4)) {
                        if (textureatlassprite4 == TextureUtils.iconClock || textureatlassprite4 == TextureUtils.iconCompass) {
                            textureatlassprite4.spriteNormal.frameCounter = textureatlassprite4.frameCounter;
                        }
                        textureatlassprite4.spriteNormal.updateAnimation();
                        if (!textureatlassprite4.spriteNormal.isAnimationActive()) {
                            continue;
                        }
                        ++i;
                    }
                }
            }
            if (flag2) {
                TextureUtil.bindTexture(this.getMultiTexID().spec);
                for (final TextureAtlasSprite textureatlassprite5 : this.listAnimatedSprites) {
                    if (textureatlassprite5.spriteSpecular != null && this.isTerrainAnimationActive(textureatlassprite5)) {
                        if (textureatlassprite5 == TextureUtils.iconClock || textureatlassprite5 == TextureUtils.iconCompass) {
                            textureatlassprite5.spriteNormal.frameCounter = textureatlassprite5.frameCounter;
                        }
                        textureatlassprite5.spriteSpecular.updateAnimation();
                        if (!textureatlassprite5.spriteSpecular.isAnimationActive()) {
                            continue;
                        }
                        ++i;
                    }
                }
            }
            if (flag || flag2) {
                TextureUtil.bindTexture(this.getGlTextureId());
            }
        }
        final int j = Config.getMinecraft().entityRenderer.frameCount;
        if (j != this.frameCountAnimations) {
            this.countAnimationsActive = i;
            this.frameCountAnimations = j;
        }
        if (SmartAnimations.isActive()) {
            SmartAnimations.resetSpritesRendered();
        }
    }
    
    public TextureAtlasSprite registerSprite(final ResourceLocation location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null!");
        }
        TextureAtlasSprite textureatlassprite = this.mapRegisteredSprites.get(location.toString());
        if (textureatlassprite == null) {
            textureatlassprite = TextureAtlasSprite.makeAtlasSprite(location);
            this.mapRegisteredSprites.put(location.toString(), textureatlassprite);
            textureatlassprite.updateIndexInMap(this.counterIndexInMap);
            if (Config.isEmissiveTextures()) {
                this.checkEmissive(location, textureatlassprite);
            }
        }
        return textureatlassprite;
    }
    
    @Override
    public void tick() {
        this.updateAnimations();
    }
    
    public void setMipmapLevels(final int mipmapLevelsIn) {
        this.mipmapLevels = mipmapLevelsIn;
    }
    
    public TextureAtlasSprite getMissingSprite() {
        return this.missingImage;
    }
    
    public TextureAtlasSprite getTextureExtry(final String p_getTextureExtry_1_) {
        return this.mapRegisteredSprites.get(p_getTextureExtry_1_);
    }
    
    public boolean setTextureEntry(final String p_setTextureEntry_1_, final TextureAtlasSprite p_setTextureEntry_2_) {
        if (!this.mapRegisteredSprites.containsKey(p_setTextureEntry_1_)) {
            this.mapRegisteredSprites.put(p_setTextureEntry_1_, p_setTextureEntry_2_);
            p_setTextureEntry_2_.updateIndexInMap(this.counterIndexInMap);
            return true;
        }
        return false;
    }
    
    public boolean setTextureEntry(final TextureAtlasSprite p_setTextureEntry_1_) {
        return this.setTextureEntry(p_setTextureEntry_1_.getIconName(), p_setTextureEntry_1_);
    }
    
    public String getBasePath() {
        return this.basePath;
    }
    
    public int getMipmapLevels() {
        return this.mipmapLevels;
    }
    
    private boolean isAbsoluteLocation(final ResourceLocation p_isAbsoluteLocation_1_) {
        final String s = p_isAbsoluteLocation_1_.getResourcePath();
        return this.isAbsoluteLocationPath(s);
    }
    
    private boolean isAbsoluteLocationPath(final String p_isAbsoluteLocationPath_1_) {
        final String s = p_isAbsoluteLocationPath_1_.toLowerCase();
        return s.startsWith("mcpatcher/") || s.startsWith("optifine/");
    }
    
    public TextureAtlasSprite getSpriteSafe(final String p_getSpriteSafe_1_) {
        final ResourceLocation resourcelocation = new ResourceLocation(p_getSpriteSafe_1_);
        return this.mapRegisteredSprites.get(resourcelocation.toString());
    }
    
    public TextureAtlasSprite getRegisteredSprite(final ResourceLocation p_getRegisteredSprite_1_) {
        return this.mapRegisteredSprites.get(p_getRegisteredSprite_1_.toString());
    }
    
    private boolean isTerrainAnimationActive(final TextureAtlasSprite p_isTerrainAnimationActive_1_) {
        return (p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterFlow) ? ((p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaFlow) ? ((p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer0 && p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer1) ? ((p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal) ? Config.isAnimatedPortal() : (p_isTerrainAnimationActive_1_ == TextureUtils.iconClock || p_isTerrainAnimationActive_1_ == TextureUtils.iconCompass || Config.isAnimatedTerrain())) : Config.isAnimatedFire()) : Config.isAnimatedLava()) : Config.isAnimatedWater();
    }
    
    public int getCountRegisteredSprites() {
        return this.counterIndexInMap.getValue();
    }
    
    private int detectMaxMipmapLevel(final Map p_detectMaxMipmapLevel_1_, final IResourceManager p_detectMaxMipmapLevel_2_) {
        int i = this.detectMinimumSpriteSize(p_detectMaxMipmapLevel_1_, p_detectMaxMipmapLevel_2_);
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
    
    private int detectMinimumSpriteSize(final Map p_detectMinimumSpriteSize_1_, final IResourceManager p_detectMinimumSpriteSize_2_) {
        final Map map = new HashMap();
        for (final Object entry0 : p_detectMinimumSpriteSize_1_.entrySet()) {
            final Map.Entry entry2 = (Map.Entry)entry0;
            final TextureAtlasSprite textureatlassprite = entry2.getValue();
            final ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite.getIconName());
            final ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation);
            if (!textureatlassprite.hasCustomLoader(p_detectMinimumSpriteSize_2_, resourcelocation)) {
                try {
                    final IResource iresource = p_detectMinimumSpriteSize_2_.getResource(resourcelocation2);
                    if (iresource == null) {
                        continue;
                    }
                    final InputStream inputstream = iresource.getInputStream();
                    if (inputstream == null) {
                        continue;
                    }
                    final Dimension dimension = TextureUtils.getImageSize(inputstream, "png");
                    inputstream.close();
                    if (dimension == null) {
                        continue;
                    }
                    final int i = dimension.width;
                    final int j = MathHelper.roundUpToPowerOfTwo(i);
                    if (!map.containsKey(j)) {
                        map.put(j, 1);
                    }
                    else {
                        final int k = map.get(j);
                        map.put(j, k + 1);
                    }
                }
                catch (Exception ex) {}
            }
        }
        int l = 0;
        final Set set = map.keySet();
        final Set set2 = new TreeSet(set);
        for (final int j2 : set2) {
            final int l2 = map.get(j2);
            l += l2;
        }
        int i2 = 16;
        int k2 = 0;
        final int l2 = l * 20 / 100;
        for (final Object o : set2) {
            final int i3 = (int)o;
            final int j3 = map.get(i3);
            k2 += j3;
            if (i3 > i2) {
                i2 = i3;
            }
            if (k2 > l2) {
                return i2;
            }
        }
        return i2;
    }
    
    private int getMinSpriteSize() {
        int i = 1 << this.mipmapLevels;
        if (i < 8) {
            i = 8;
        }
        return i;
    }
    
    private int[] getMissingImageData(final int p_getMissingImageData_1_) {
        final BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
        bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.missingTextureData, 0, 16);
        final BufferedImage bufferedimage2 = TextureUtils.scaleImage(bufferedimage, p_getMissingImageData_1_);
        final int[] aint = new int[p_getMissingImageData_1_ * p_getMissingImageData_1_];
        bufferedimage2.getRGB(0, 0, p_getMissingImageData_1_, p_getMissingImageData_1_, aint, 0, p_getMissingImageData_1_);
        return aint;
    }
    
    public boolean isTextureBound() {
        final int i = GlStateManager.getBoundTexture();
        final int j = this.getGlTextureId();
        return i == j;
    }
    
    private void updateIconGrid(final int p_updateIconGrid_1_, final int p_updateIconGrid_2_) {
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGrid = null;
        if (this.iconGridSize > 0) {
            this.iconGridCountX = p_updateIconGrid_1_ / this.iconGridSize;
            this.iconGridCountY = p_updateIconGrid_2_ / this.iconGridSize;
            this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
            this.iconGridSizeU = 1.0 / this.iconGridCountX;
            this.iconGridSizeV = 1.0 / this.iconGridCountY;
            for (final TextureAtlasSprite textureatlassprite : this.mapUploadedSprites.values()) {
                final double d0 = 0.5 / p_updateIconGrid_1_;
                final double d2 = 0.5 / p_updateIconGrid_2_;
                final double d3 = Math.min(textureatlassprite.getMinU(), textureatlassprite.getMaxU()) + d0;
                final double d4 = Math.min(textureatlassprite.getMinV(), textureatlassprite.getMaxV()) + d2;
                final double d5 = Math.max(textureatlassprite.getMinU(), textureatlassprite.getMaxU()) - d0;
                final double d6 = Math.max(textureatlassprite.getMinV(), textureatlassprite.getMaxV()) - d2;
                final int i = (int)(d3 / this.iconGridSizeU);
                final int j = (int)(d4 / this.iconGridSizeV);
                final int k = (int)(d5 / this.iconGridSizeU);
                final int l = (int)(d6 / this.iconGridSizeV);
                for (int i2 = i; i2 <= k; ++i2) {
                    if (i2 >= 0 && i2 < this.iconGridCountX) {
                        for (int j2 = j; j2 <= l; ++j2) {
                            if (j2 >= 0 && j2 < this.iconGridCountX) {
                                final int k2 = j2 * this.iconGridCountX + i2;
                                this.iconGrid[k2] = textureatlassprite;
                            }
                            else {
                                Config.warn("Invalid grid V: " + j2 + ", icon: " + textureatlassprite.getIconName());
                            }
                        }
                    }
                    else {
                        Config.warn("Invalid grid U: " + i2 + ", icon: " + textureatlassprite.getIconName());
                    }
                }
            }
        }
    }
    
    public TextureAtlasSprite getIconByUV(final double p_getIconByUV_1_, final double p_getIconByUV_3_) {
        if (this.iconGrid == null) {
            return null;
        }
        final int i = (int)(p_getIconByUV_1_ / this.iconGridSizeU);
        final int j = (int)(p_getIconByUV_3_ / this.iconGridSizeV);
        final int k = j * this.iconGridCountX + i;
        return (k >= 0 && k <= this.iconGrid.length) ? this.iconGrid[k] : null;
    }
    
    private void checkEmissive(final ResourceLocation p_checkEmissive_1_, final TextureAtlasSprite p_checkEmissive_2_) {
        final String s = EmissiveTextures.getSuffixEmissive();
        if (s != null && !p_checkEmissive_1_.getResourcePath().endsWith(s)) {
            final ResourceLocation resourcelocation = new ResourceLocation(p_checkEmissive_1_.getResourceDomain(), p_checkEmissive_1_.getResourcePath() + s);
            final ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation);
            if (Config.hasResource(resourcelocation2)) {
                final TextureAtlasSprite textureatlassprite = this.registerSprite(resourcelocation);
                textureatlassprite.isEmissive = true;
                p_checkEmissive_2_.spriteEmissive = textureatlassprite;
            }
        }
    }
    
    public int getCountAnimations() {
        return this.listAnimatedSprites.size();
    }
    
    public int getCountAnimationsActive() {
        return this.countAnimationsActive;
    }
    
    static {
        ENABLE_SKIP = Boolean.parseBoolean(System.getProperty("fml.skipFirstTextureLoad", "true"));
        logger = LogManager.getLogger();
        LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
        locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
    }
}
