// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import net.optifine.EmissiveTextures;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.awt.Dimension;
import java.io.InputStream;
import java.util.TreeSet;
import java.util.HashMap;
import net.optifine.SmartAnimations;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.client.resources.IResource;
import java.util.Iterator;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.optifine.shaders.ShadersTex;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.util.math.MathHelper;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import net.optifine.reflect.ReflectorForge;
import java.util.Collection;
import java.util.ArrayList;
import net.optifine.SpriteDependencies;
import net.optifine.util.TextureUtils;
import net.optifine.BetterGrass;
import net.optifine.CustomItems;
import net.optifine.ConnectedTextures;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.optifine.util.CounterInt;
import java.util.Map;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class TextureMap extends AbstractTexture implements ITickableTextureObject
{
    private static final Logger LOGGER;
    public static final ResourceLocation LOCATION_MISSING_TEXTURE;
    public static final ResourceLocation LOCATION_BLOCKS_TEXTURE;
    private final List<TextureAtlasSprite> listAnimatedSprites;
    private final Map<String, TextureAtlasSprite> mapRegisteredSprites;
    private final Map<String, TextureAtlasSprite> mapUploadedSprites;
    private final String basePath;
    private final ITextureMapPopulator iconCreator;
    private int mipmapLevels;
    private final TextureAtlasSprite missingImage;
    private TextureAtlasSprite[] iconGrid;
    private int iconGridSize;
    private int iconGridCountX;
    private int iconGridCountY;
    private double iconGridSizeU;
    private double iconGridSizeV;
    private CounterInt counterIndexInMap;
    public int atlasWidth;
    public int atlasHeight;
    private int countAnimationsActive;
    private int frameCountAnimations;
    
    public TextureMap(final String basePathIn) {
        this(basePathIn, null);
    }
    
    public TextureMap(final String p_i8_1_, final boolean p_i8_2_) {
        this(p_i8_1_, null, p_i8_2_);
    }
    
    public TextureMap(final String basePathIn, @Nullable final ITextureMapPopulator iconCreatorIn) {
        this(basePathIn, iconCreatorIn, false);
    }
    
    public TextureMap(final String p_i9_1_, final ITextureMapPopulator p_i9_2_, final boolean p_i9_3_) {
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
        this.basePath = p_i9_1_;
        this.iconCreator = p_i9_2_;
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
    
    public void loadSprites(final IResourceManager resourceManager, final ITextureMapPopulator iconCreatorIn) {
        this.mapRegisteredSprites.clear();
        this.counterIndexInMap.reset();
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, this);
        iconCreatorIn.registerSprites(this);
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
        final Stitcher stitcher = new Stitcher(i2, i2, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int j = Integer.MAX_VALUE;
        final int k = this.getMinSpriteSize();
        this.iconGridSize = k;
        int l = 1 << this.mipmapLevels;
        int m = 0;
        int i3 = 0;
        SpriteDependencies.reset();
        final List<TextureAtlasSprite> list = new ArrayList<TextureAtlasSprite>(this.mapRegisteredSprites.values());
        for (int j2 = 0; j2 < list.size(); ++j2) {
            final TextureAtlasSprite textureatlassprite2 = SpriteDependencies.resolveDependencies(list, j2, this);
            final ResourceLocation resourcelocation = this.getResourceLocation(textureatlassprite2);
            IResource iresource = null;
            textureatlassprite2.updateIndexInMap(this.counterIndexInMap);
            if (textureatlassprite2.hasCustomLoader(resourceManager, resourcelocation)) {
                if (textureatlassprite2.load(resourceManager, resourcelocation, p_lambda$loadTextureAtlas$0_1_ -> this.mapRegisteredSprites.get(p_lambda$loadTextureAtlas$0_1_.toString()))) {
                    Config.detail("Custom loader (skipped): " + textureatlassprite2);
                    ++i3;
                    continue;
                }
                Config.detail("Custom loader: " + textureatlassprite2);
                ++m;
            }
            else {
                try {
                    final PngSizeInfo pngsizeinfo = PngSizeInfo.makeFromResource(resourceManager.getResource(resourcelocation));
                    iresource = resourceManager.getResource(resourcelocation);
                    final boolean flag = iresource.getMetadata("animation") != null;
                    textureatlassprite2.loadSprite(pngsizeinfo, flag);
                }
                catch (RuntimeException runtimeexception) {
                    TextureMap.LOGGER.error("Unable to parse metadata from {}", (Object)resourcelocation, (Object)runtimeexception);
                    ReflectorForge.FMLClientHandler_trackBrokenTexture(resourcelocation, runtimeexception.getMessage());
                }
                catch (IOException ioexception) {
                    TextureMap.LOGGER.error("Using missing texture, unable to load " + resourcelocation + ", " + ioexception.getClass().getName());
                    ReflectorForge.FMLClientHandler_trackMissingTexture(resourcelocation);
                }
                finally {
                    IOUtils.closeQuietly((Closeable)iresource);
                }
            }
            final int i4 = textureatlassprite2.getIconWidth();
            final int k2 = textureatlassprite2.getIconHeight();
            if (i4 >= 1 && k2 >= 1) {
                if (i4 < k || this.mipmapLevels > 0) {
                    final int k3 = (this.mipmapLevels > 0) ? TextureUtils.scaleToGrid(i4, k) : TextureUtils.scaleToMin(i4, k);
                    if (k3 != i4) {
                        if (!TextureUtils.isPowerOfTwo(i4)) {
                            Config.log("Scaled non power of 2: " + textureatlassprite2.getIconName() + ", " + i4 + " -> " + k3);
                        }
                        else {
                            Config.log("Scaled too small texture: " + textureatlassprite2.getIconName() + ", " + i4 + " -> " + k3);
                        }
                        final int l2 = k2 * k3 / i4;
                        textureatlassprite2.setIconWidth(k3);
                        textureatlassprite2.setIconHeight(l2);
                    }
                }
                j = Math.min(j, Math.min(textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight()));
                final int l3 = Math.min(Integer.lowestOneBit(textureatlassprite2.getIconWidth()), Integer.lowestOneBit(textureatlassprite2.getIconHeight()));
                if (l3 < l) {
                    TextureMap.LOGGER.warn("Texture {} with size {}x{} limits mip level from {} to {}", (Object)resourcelocation, (Object)textureatlassprite2.getIconWidth(), (Object)textureatlassprite2.getIconHeight(), (Object)MathHelper.log2(l), (Object)MathHelper.log2(l3));
                    l = l3;
                }
                if (this.generateMipmaps(resourceManager, textureatlassprite2)) {
                    stitcher.addSprite(textureatlassprite2);
                }
            }
            else {
                Config.warn("Invalid sprite size: " + textureatlassprite2);
            }
        }
        if (m > 0) {
            Config.dbg("Custom loader sprites: " + m);
        }
        if (i3 > 0) {
            Config.dbg("Custom loader sprites (skipped): " + i3);
        }
        if (SpriteDependencies.getCountDependencies() > 0) {
            Config.dbg("Sprite dependencies: " + SpriteDependencies.getCountDependencies());
        }
        final int j3 = Math.min(j, l);
        int k4 = MathHelper.log2(j3);
        if (k4 < 0) {
            k4 = 0;
        }
        if (k4 < this.mipmapLevels) {
            TextureMap.LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", (Object)this.basePath, (Object)this.mipmapLevels, (Object)k4, (Object)j3);
            this.mipmapLevels = k4;
        }
        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);
        try {
            stitcher.doStitch();
        }
        catch (StitcherException stitcherexception) {
            throw stitcherexception;
        }
        TextureMap.LOGGER.info("Created: {}x{} {}-atlas", (Object)stitcher.getCurrentWidth(), (Object)stitcher.getCurrentHeight(), (Object)this.basePath);
        if (Config.isShaders()) {
            ShadersTex.allocateTextureMap(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), stitcher, this);
        }
        else {
            TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }
        final Map<String, TextureAtlasSprite> map = (Map<String, TextureAtlasSprite>)Maps.newHashMap((Map)this.mapRegisteredSprites);
        for (final TextureAtlasSprite textureatlassprite3 : stitcher.getStichSlots()) {
            final String s = textureatlassprite3.getIconName();
            map.remove(s);
            this.mapUploadedSprites.put(s, textureatlassprite3);
            try {
                if (Config.isShaders()) {
                    ShadersTex.uploadTexSubForLoadAtlas(this, textureatlassprite3.getIconName(), textureatlassprite3.getFrameTextureData(0), textureatlassprite3.getIconWidth(), textureatlassprite3.getIconHeight(), textureatlassprite3.getOriginX(), textureatlassprite3.getOriginY(), false, false);
                }
                else {
                    TextureUtil.uploadTextureMipmap(textureatlassprite3.getFrameTextureData(0), textureatlassprite3.getIconWidth(), textureatlassprite3.getIconHeight(), textureatlassprite3.getOriginX(), textureatlassprite3.getOriginY(), false, false);
                }
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Texture being stitched together");
                crashreportcategory.addCrashSection("Atlas path", this.basePath);
                crashreportcategory.addCrashSection("Sprite", textureatlassprite3);
                throw new ReportedException(crashreport);
            }
            if (textureatlassprite3.hasAnimationMetadata()) {
                textureatlassprite3.setAnimationIndex(this.listAnimatedSprites.size());
                this.listAnimatedSprites.add(textureatlassprite3);
            }
        }
        for (final TextureAtlasSprite textureatlassprite4 : map.values()) {
            textureatlassprite4.copyFrom(this.missingImage);
        }
        Config.log("Animated sprites: " + this.listAnimatedSprites.size());
        if (Config.isMultiTexture()) {
            final int l4 = stitcher.getCurrentWidth();
            final int j4 = stitcher.getCurrentHeight();
            for (final TextureAtlasSprite textureatlassprite5 : stitcher.getStichSlots()) {
                textureatlassprite5.sheetWidth = l4;
                textureatlassprite5.sheetHeight = j4;
                textureatlassprite5.mipmapLevels = this.mipmapLevels;
                final TextureAtlasSprite textureatlassprite6 = textureatlassprite5.spriteSingle;
                if (textureatlassprite6 != null) {
                    if (textureatlassprite6.getIconWidth() <= 0) {
                        textureatlassprite6.setIconWidth(textureatlassprite5.getIconWidth());
                        textureatlassprite6.setIconHeight(textureatlassprite5.getIconHeight());
                        textureatlassprite6.initSprite(textureatlassprite5.getIconWidth(), textureatlassprite5.getIconHeight(), 0, 0, false);
                        textureatlassprite6.clearFramesTextureData();
                        final List<int[][]> list2 = textureatlassprite5.getFramesTextureData();
                        textureatlassprite6.setFramesTextureData(list2);
                        textureatlassprite6.setAnimationMetadata(textureatlassprite5.getAnimationMetadata());
                    }
                    textureatlassprite6.sheetWidth = l4;
                    textureatlassprite6.sheetHeight = j4;
                    textureatlassprite6.mipmapLevels = this.mipmapLevels;
                    textureatlassprite6.setAnimationIndex(textureatlassprite5.getAnimationIndex());
                    textureatlassprite5.bindSpriteTexture();
                    final boolean flag2 = false;
                    final boolean flag3 = true;
                    try {
                        TextureUtil.uploadTextureMipmap(textureatlassprite6.getFrameTextureData(0), textureatlassprite6.getIconWidth(), textureatlassprite6.getIconHeight(), textureatlassprite6.getOriginX(), textureatlassprite6.getOriginY(), flag2, flag3);
                    }
                    catch (Exception exception) {
                        Config.dbg("Error uploading sprite single: " + textureatlassprite6 + ", parent: " + textureatlassprite5);
                        exception.printStackTrace();
                    }
                }
            }
            Config.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        }
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, this);
        this.updateIconGrid(stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        if (Config.equals(System.getProperty("saveTextureMap"), "true")) {
            Config.dbg("Exporting texture map: " + this.basePath);
            TextureUtils.saveGlTexture("debug/" + this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }
    }
    
    public boolean generateMipmaps(final IResourceManager resourceManager, final TextureAtlasSprite texture) {
        final ResourceLocation resourcelocation1 = this.getResourceLocation(texture);
        IResource iresource1 = null;
        if (!texture.hasCustomLoader(resourceManager, resourcelocation1)) {
            boolean flag4 = false;
            try {
                iresource1 = resourceManager.getResource(resourcelocation1);
                texture.loadSpriteFrames(iresource1, this.mipmapLevels + 1);
            }
            catch (RuntimeException runtimeexception1) {
                TextureMap.LOGGER.error("Unable to parse metadata from {}", (Object)resourcelocation1, (Object)runtimeexception1);
                flag4 = false;
            }
            catch (IOException ioexception1) {
                TextureMap.LOGGER.error("Using missing texture, unable to load {}", (Object)resourcelocation1, (Object)ioexception1);
                final boolean crashreportcategory;
                flag4 = (crashreportcategory = false);
                return crashreportcategory;
            }
            finally {
                IOUtils.closeQuietly((Closeable)iresource1);
            }
            return flag4;
        }
        TextureUtils.generateCustomMipmaps(texture, this.mipmapLevels);
        try {
            texture.generateMipmaps(this.mipmapLevels);
            return true;
        }
        catch (Throwable throwable1) {
            final CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
            final CrashReportCategory crashreportcategory2 = crashreport1.makeCategory("Sprite being mipmapped");
            crashreportcategory2.addDetail("Sprite name", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    return texture.getIconName();
                }
            });
            crashreportcategory2.addDetail("Sprite size", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    return texture.getIconWidth() + " x " + texture.getIconHeight();
                }
            });
            crashreportcategory2.addDetail("Sprite frames", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    return texture.getFrameCount() + " frames";
                }
            });
            crashreportcategory2.addCrashSection("Mipmap levels", this.mipmapLevels);
            throw new ReportedException(crashreport1);
        }
    }
    
    public ResourceLocation getResourceLocation(final TextureAtlasSprite p_184396_1_) {
        final ResourceLocation resourcelocation1 = new ResourceLocation(p_184396_1_.getIconName());
        return this.completeResourceLocation(resourcelocation1);
    }
    
    public ResourceLocation completeResourceLocation(final ResourceLocation p_completeResourceLocation_1_) {
        return this.isAbsoluteLocation(p_completeResourceLocation_1_) ? new ResourceLocation(p_completeResourceLocation_1_.getNamespace(), p_completeResourceLocation_1_.getPath() + ".png") : new ResourceLocation(p_completeResourceLocation_1_.getNamespace(), String.format("%s/%s%s", this.basePath, p_completeResourceLocation_1_.getPath(), ".png"));
    }
    
    public TextureAtlasSprite getAtlasSprite(final String iconName) {
        TextureAtlasSprite textureatlassprite6 = this.mapUploadedSprites.get(iconName);
        if (textureatlassprite6 == null) {
            textureatlassprite6 = this.missingImage;
        }
        return textureatlassprite6;
    }
    
    public void updateAnimations() {
        boolean flag3 = false;
        boolean flag4 = false;
        TextureUtil.bindTexture(this.getGlTextureId());
        int i4 = 0;
        for (final TextureAtlasSprite textureatlassprite6 : this.listAnimatedSprites) {
            if (this.isTerrainAnimationActive(textureatlassprite6)) {
                textureatlassprite6.updateAnimation();
                if (textureatlassprite6.isAnimationActive()) {
                    ++i4;
                }
                if (textureatlassprite6.spriteNormal != null) {
                    flag3 = true;
                }
                if (textureatlassprite6.spriteSpecular == null) {
                    continue;
                }
                flag4 = true;
            }
        }
        if (Config.isMultiTexture()) {
            for (final TextureAtlasSprite textureatlassprite7 : this.listAnimatedSprites) {
                if (this.isTerrainAnimationActive(textureatlassprite7)) {
                    final TextureAtlasSprite textureatlassprite8 = textureatlassprite7.spriteSingle;
                    if (textureatlassprite8 == null) {
                        continue;
                    }
                    if (textureatlassprite7 == TextureUtils.iconClock || textureatlassprite7 == TextureUtils.iconCompass) {
                        textureatlassprite8.frameCounter = textureatlassprite7.frameCounter;
                    }
                    textureatlassprite7.bindSpriteTexture();
                    textureatlassprite8.updateAnimation();
                    if (!textureatlassprite8.isAnimationActive()) {
                        continue;
                    }
                    ++i4;
                }
            }
            TextureUtil.bindTexture(this.getGlTextureId());
        }
        if (Config.isShaders()) {
            if (flag3) {
                TextureUtil.bindTexture(this.getMultiTexID().norm);
                for (final TextureAtlasSprite textureatlassprite9 : this.listAnimatedSprites) {
                    if (textureatlassprite9.spriteNormal != null && this.isTerrainAnimationActive(textureatlassprite9)) {
                        if (textureatlassprite9 == TextureUtils.iconClock || textureatlassprite9 == TextureUtils.iconCompass) {
                            textureatlassprite9.spriteNormal.frameCounter = textureatlassprite9.frameCounter;
                        }
                        textureatlassprite9.spriteNormal.updateAnimation();
                        if (!textureatlassprite9.spriteNormal.isAnimationActive()) {
                            continue;
                        }
                        ++i4;
                    }
                }
            }
            if (flag4) {
                TextureUtil.bindTexture(this.getMultiTexID().spec);
                for (final TextureAtlasSprite textureatlassprite10 : this.listAnimatedSprites) {
                    if (textureatlassprite10.spriteSpecular != null && this.isTerrainAnimationActive(textureatlassprite10)) {
                        if (textureatlassprite10 == TextureUtils.iconClock || textureatlassprite10 == TextureUtils.iconCompass) {
                            textureatlassprite10.spriteNormal.frameCounter = textureatlassprite10.frameCounter;
                        }
                        textureatlassprite10.spriteSpecular.updateAnimation();
                        if (!textureatlassprite10.spriteSpecular.isAnimationActive()) {
                            continue;
                        }
                        ++i4;
                    }
                }
            }
            if (flag3 || flag4) {
                TextureUtil.bindTexture(this.getGlTextureId());
            }
        }
        final int j4 = Config.getMinecraft().entityRenderer.frameCount;
        if (j4 != this.frameCountAnimations) {
            this.countAnimationsActive = i4;
            this.frameCountAnimations = j4;
        }
        if (SmartAnimations.isActive()) {
            SmartAnimations.resetSpritesRendered();
        }
    }
    
    public TextureAtlasSprite registerSprite(final ResourceLocation location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null!");
        }
        TextureAtlasSprite textureatlassprite6 = this.mapRegisteredSprites.get(location.toString());
        if (textureatlassprite6 == null) {
            textureatlassprite6 = TextureAtlasSprite.makeAtlasSprite(location);
            this.mapRegisteredSprites.put(location.toString(), textureatlassprite6);
            textureatlassprite6.updateIndexInMap(this.counterIndexInMap);
            if (Config.isEmissiveTextures()) {
                this.checkEmissive(location, textureatlassprite6);
            }
        }
        return textureatlassprite6;
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
    
    @Nullable
    public TextureAtlasSprite getTextureExtry(final String p_getTextureExtry_1_) {
        return this.mapRegisteredSprites.get(p_getTextureExtry_1_);
    }
    
    public boolean setTextureEntry(final TextureAtlasSprite p_setTextureEntry_1_) {
        final String s1 = p_setTextureEntry_1_.getIconName();
        if (!this.mapRegisteredSprites.containsKey(s1)) {
            this.mapRegisteredSprites.put(s1, p_setTextureEntry_1_);
            p_setTextureEntry_1_.updateIndexInMap(this.counterIndexInMap);
            return true;
        }
        return false;
    }
    
    public String getBasePath() {
        return this.basePath;
    }
    
    public int getMipmapLevels() {
        return this.mipmapLevels;
    }
    
    private boolean isAbsoluteLocation(final ResourceLocation p_isAbsoluteLocation_1_) {
        final String s1 = p_isAbsoluteLocation_1_.getPath();
        return this.isAbsoluteLocationPath(s1);
    }
    
    private boolean isAbsoluteLocationPath(final String p_isAbsoluteLocationPath_1_) {
        final String s1 = p_isAbsoluteLocationPath_1_.toLowerCase();
        return s1.startsWith("mcpatcher/") || s1.startsWith("optifine/");
    }
    
    public TextureAtlasSprite getSpriteSafe(final String p_getSpriteSafe_1_) {
        final ResourceLocation resourcelocation1 = new ResourceLocation(p_getSpriteSafe_1_);
        return this.mapRegisteredSprites.get(resourcelocation1.toString());
    }
    
    public TextureAtlasSprite getRegisteredSprite(final ResourceLocation p_getRegisteredSprite_1_) {
        return this.mapRegisteredSprites.get(p_getRegisteredSprite_1_.toString());
    }
    
    private boolean isTerrainAnimationActive(final TextureAtlasSprite p_isTerrainAnimationActive_1_) {
        if (p_isTerrainAnimationActive_1_ == TextureUtils.iconWaterStill || p_isTerrainAnimationActive_1_ == TextureUtils.iconWaterFlow) {
            return Config.isAnimatedWater();
        }
        if (p_isTerrainAnimationActive_1_ == TextureUtils.iconLavaStill || p_isTerrainAnimationActive_1_ == TextureUtils.iconLavaFlow) {
            return Config.isAnimatedLava();
        }
        if (p_isTerrainAnimationActive_1_ == TextureUtils.iconFireLayer0 || p_isTerrainAnimationActive_1_ == TextureUtils.iconFireLayer1) {
            return Config.isAnimatedFire();
        }
        if (p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal) {
            return Config.isAnimatedPortal();
        }
        return p_isTerrainAnimationActive_1_ == TextureUtils.iconClock || p_isTerrainAnimationActive_1_ == TextureUtils.iconCompass || Config.isAnimatedTerrain();
    }
    
    public int getCountRegisteredSprites() {
        return this.counterIndexInMap.getValue();
    }
    
    private int detectMaxMipmapLevel(final Map p_detectMaxMipmapLevel_1_, final IResourceManager p_detectMaxMipmapLevel_2_) {
        int i4 = this.detectMinimumSpriteSize(p_detectMaxMipmapLevel_1_, p_detectMaxMipmapLevel_2_, 20);
        if (i4 < 16) {
            i4 = 16;
        }
        i4 = MathHelper.smallestEncompassingPowerOfTwo(i4);
        if (i4 > 16) {
            Config.log("Sprite size: " + i4);
        }
        int j4 = MathHelper.log2(i4);
        if (j4 < 4) {
            j4 = 4;
        }
        return j4;
    }
    
    private int detectMinimumSpriteSize(final Map p_detectMinimumSpriteSize_1_, final IResourceManager p_detectMinimumSpriteSize_2_, final int p_detectMinimumSpriteSize_3_) {
        final Map map1 = new HashMap();
        for (final Object entry : p_detectMinimumSpriteSize_1_.entrySet()) {
            final TextureAtlasSprite textureatlassprite6 = ((Map.Entry)entry).getValue();
            final ResourceLocation resourcelocation1 = new ResourceLocation(textureatlassprite6.getIconName());
            final ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation1);
            if (!textureatlassprite6.hasCustomLoader(p_detectMinimumSpriteSize_2_, resourcelocation1)) {
                try {
                    final IResource iresource1 = p_detectMinimumSpriteSize_2_.getResource(resourcelocation2);
                    if (iresource1 == null) {
                        continue;
                    }
                    final InputStream inputstream = iresource1.getInputStream();
                    if (inputstream == null) {
                        continue;
                    }
                    final Dimension dimension = TextureUtils.getImageSize(inputstream, "png");
                    if (dimension == null) {
                        continue;
                    }
                    final int k3 = dimension.width;
                    final int l3 = MathHelper.smallestEncompassingPowerOfTwo(k3);
                    if (!map1.containsKey(l3)) {
                        map1.put(l3, 1);
                    }
                    else {
                        final int i4 = map1.get(l3);
                        map1.put(l3, i4 + 1);
                    }
                }
                catch (Exception ex) {}
            }
        }
        int l4 = 0;
        final Set set = map1.keySet();
        final Set set2 = new TreeSet(set);
        for (final int j5 : set2) {
            final int l5 = map1.get(j5);
            l4 += l5;
        }
        int i5 = 16;
        int k4 = 0;
        final int l5 = l4 * p_detectMinimumSpriteSize_3_ / 100;
        for (final int i6 : set2) {
            final int j6 = map1.get(i6);
            k4 += j6;
            if (i6 > i5) {
                i5 = i6;
            }
            if (k4 > l5) {
                return i5;
            }
        }
        return i5;
    }
    
    private int getMinSpriteSize() {
        int i4 = 1 << this.mipmapLevels;
        if (i4 < 8) {
            i4 = 8;
        }
        return i4;
    }
    
    private int[] getMissingImageData(final int p_getMissingImageData_1_) {
        final BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
        bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.MISSING_TEXTURE_DATA, 0, 16);
        final BufferedImage bufferedimage2 = TextureUtils.scaleImage(bufferedimage, p_getMissingImageData_1_);
        final int[] aint = new int[p_getMissingImageData_1_ * p_getMissingImageData_1_];
        bufferedimage2.getRGB(0, 0, p_getMissingImageData_1_, p_getMissingImageData_1_, aint, 0, p_getMissingImageData_1_);
        return aint;
    }
    
    public boolean isTextureBound() {
        final int i4 = GlStateManager.getBoundTexture();
        final int j4 = this.getGlTextureId();
        return i4 == j4;
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
            for (final TextureAtlasSprite textureatlassprite6 : this.mapUploadedSprites.values()) {
                final double d0 = 0.5 / p_updateIconGrid_1_;
                final double d2 = 0.5 / p_updateIconGrid_2_;
                final double d3 = Math.min(textureatlassprite6.getMinU(), textureatlassprite6.getMaxU()) + d0;
                final double d4 = Math.min(textureatlassprite6.getMinV(), textureatlassprite6.getMaxV()) + d2;
                final double d5 = Math.max(textureatlassprite6.getMinU(), textureatlassprite6.getMaxU()) - d0;
                final double d6 = Math.max(textureatlassprite6.getMinV(), textureatlassprite6.getMaxV()) - d2;
                final int i4 = (int)(d3 / this.iconGridSizeU);
                final int j4 = (int)(d4 / this.iconGridSizeV);
                final int k4 = (int)(d5 / this.iconGridSizeU);
                final int l4 = (int)(d6 / this.iconGridSizeV);
                for (int i5 = i4; i5 <= k4; ++i5) {
                    if (i5 >= 0 && i5 < this.iconGridCountX) {
                        for (int j5 = j4; j5 <= l4; ++j5) {
                            if (j5 >= 0 && j5 < this.iconGridCountX) {
                                final int k5 = j5 * this.iconGridCountX + i5;
                                this.iconGrid[k5] = textureatlassprite6;
                            }
                            else {
                                Config.warn("Invalid grid V: " + j5 + ", icon: " + textureatlassprite6.getIconName());
                            }
                        }
                    }
                    else {
                        Config.warn("Invalid grid U: " + i5 + ", icon: " + textureatlassprite6.getIconName());
                    }
                }
            }
        }
    }
    
    public TextureAtlasSprite getIconByUV(final double p_getIconByUV_1_, final double p_getIconByUV_3_) {
        if (this.iconGrid == null) {
            return null;
        }
        final int i4 = (int)(p_getIconByUV_1_ / this.iconGridSizeU);
        final int j4 = (int)(p_getIconByUV_3_ / this.iconGridSizeV);
        final int k4 = j4 * this.iconGridCountX + i4;
        return (k4 >= 0 && k4 <= this.iconGrid.length) ? this.iconGrid[k4] : null;
    }
    
    private void checkEmissive(final ResourceLocation p_checkEmissive_1_, final TextureAtlasSprite p_checkEmissive_2_) {
        final String s1 = EmissiveTextures.getSuffixEmissive();
        if (s1 != null && !p_checkEmissive_1_.getPath().endsWith(s1)) {
            final ResourceLocation resourcelocation1 = new ResourceLocation(p_checkEmissive_1_.getNamespace(), p_checkEmissive_1_.getPath() + s1);
            final ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation1);
            if (Config.hasResource(resourcelocation2)) {
                final TextureAtlasSprite textureatlassprite6 = this.registerSprite(resourcelocation1);
                textureatlassprite6.isEmissive = true;
                p_checkEmissive_2_.spriteEmissive = textureatlassprite6;
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
        LOGGER = LogManager.getLogger();
        LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
        LOCATION_BLOCKS_TEXTURE = new ResourceLocation("textures/atlas/blocks.png");
    }
}
