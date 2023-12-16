// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.GlStateManager;
import java.awt.Dimension;
import java.io.InputStream;
import java.util.Set;
import java.util.Collection;
import java.util.TreeSet;
import java.util.HashMap;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.client.resources.IResource;
import java.util.Iterator;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import optifine.ReflectorForge;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.MathHelper;
import net.minecraft.client.resources.data.TextureMetadataSection;
import optifine.TextureUtils;
import java.awt.image.BufferedImage;
import optifine.Reflector;
import net.minecraft.client.Minecraft;
import optifine.CustomItems;
import optifine.ConnectedTextures;
import optifine.Config;
import java.io.IOException;
import shadersmod.client.ShadersTex;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class TextureMap extends AbstractTexture implements ITickableTextureObject
{
    private static final Logger logger;
    public static final ResourceLocation field_174945_f;
    public static final ResourceLocation locationBlocksTexture;
    private final List listAnimatedSprites;
    private final Map mapRegisteredSprites;
    private final Map mapUploadedSprites;
    private final String basePath;
    private final IIconCreator field_174946_m;
    private int mipmapLevels;
    private final TextureAtlasSprite missingImage;
    private static final String __OBFID = "CL_00001058";
    private TextureAtlasSprite[] iconGrid;
    private int iconGridSize;
    private int iconGridCountX;
    private int iconGridCountY;
    private double iconGridSizeU;
    private double iconGridSizeV;
    private static final boolean ENABLE_SKIP;
    private boolean skipFirst;
    public int atlasWidth;
    public int atlasHeight;
    
    static {
        logger = LogManager.getLogger();
        field_174945_f = new ResourceLocation("missingno");
        locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
        ENABLE_SKIP = Boolean.parseBoolean(System.getProperty("fml.skipFirstTextureLoad", "true"));
    }
    
    public TextureMap(final String p_i46099_1_) {
        this(p_i46099_1_, null);
    }
    
    public TextureMap(final String p_i46099_1_, final boolean skipFirst) {
        this(p_i46099_1_, null, skipFirst);
    }
    
    public TextureMap(final String p_i46100_1_, final IIconCreator p_i46100_2_) {
        this(p_i46100_1_, p_i46100_2_, false);
    }
    
    public TextureMap(final String p_i46100_1_, final IIconCreator p_i46100_2_, final boolean skipFirst) {
        this.iconGrid = null;
        this.iconGridSize = -1;
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGridSizeU = -1.0;
        this.iconGridSizeV = -1.0;
        this.skipFirst = false;
        this.atlasWidth = 0;
        this.atlasHeight = 0;
        this.listAnimatedSprites = Lists.newArrayList();
        this.mapRegisteredSprites = Maps.newHashMap();
        this.mapUploadedSprites = Maps.newHashMap();
        this.missingImage = new TextureAtlasSprite("missingno");
        this.basePath = p_i46100_1_;
        this.field_174946_m = p_i46100_2_;
        this.skipFirst = (skipFirst && TextureMap.ENABLE_SKIP);
    }
    
    private void initMissingImage() {
        final int size = this.getMinSpriteSize();
        final int[] var1 = this.getMissingImageData(size);
        this.missingImage.setIconWidth(size);
        this.missingImage.setIconHeight(size);
        final int[][] var2 = new int[this.mipmapLevels + 1][];
        var2[0] = var1;
        this.missingImage.setFramesTextureData(Lists.newArrayList((Object[])new int[][][] { var2 }));
        this.missingImage.setIndexInMap(0);
    }
    
    @Override
    public void loadTexture(final IResourceManager p_110551_1_) throws IOException {
        ShadersTex.resManager = p_110551_1_;
        if (this.field_174946_m != null) {
            this.func_174943_a(p_110551_1_, this.field_174946_m);
        }
    }
    
    public void func_174943_a(final IResourceManager p_174943_1_, final IIconCreator p_174943_2_) {
        this.mapRegisteredSprites.clear();
        p_174943_2_.func_177059_a(this);
        if (this.mipmapLevels >= 4) {
            this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, p_174943_1_);
            Config.log("Mipmap levels: " + this.mipmapLevels);
        }
        this.initMissingImage();
        this.deleteGlTexture();
        this.loadTextureAtlas(p_174943_1_);
    }
    
    public void loadTextureAtlas(final IResourceManager p_110571_1_) {
        ShadersTex.resManager = p_110571_1_;
        Config.dbg("Multitexture: " + Config.isMultiTexture());
        if (Config.isMultiTexture()) {
            for (final TextureAtlasSprite var3 : this.mapUploadedSprites.values()) {
                var3.deleteSpriteTexture();
            }
        }
        ConnectedTextures.updateIcons(this);
        CustomItems.updateIcons(this);
        final int var4 = Minecraft.getGLMaximumTextureSize();
        final Stitcher var5 = new Stitcher(var4, var4, true, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int var6 = Integer.MAX_VALUE;
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, this);
        final int minSpriteSize = this.getMinSpriteSize();
        this.iconGridSize = minSpriteSize;
        int var7 = 1 << this.mipmapLevels;
        final Iterator var8 = this.mapRegisteredSprites.entrySet().iterator();
        while (var8.hasNext() && !this.skipFirst) {
            final Map.Entry var9 = var8.next();
            final TextureAtlasSprite var10 = var9.getValue();
            final ResourceLocation var11 = new ResourceLocation(var10.getIconName());
            final ResourceLocation var12 = this.completeResourceLocation(var11, 0);
            if (var10.hasCustomLoader(p_110571_1_, var11)) {
                if (!var10.load(p_110571_1_, var11)) {
                    var6 = Math.min(var6, Math.min(var10.getIconWidth(), var10.getIconHeight()));
                    var5.addSprite(var10);
                }
                Config.dbg("Custom loader: " + var10);
            }
            else {
                try {
                    final IResource var13 = ShadersTex.loadResource(p_110571_1_, var12);
                    final BufferedImage[] var14 = new BufferedImage[1 + this.mipmapLevels];
                    var14[0] = TextureUtil.func_177053_a(var13.getInputStream());
                    if (var14 != null) {
                        final int sheetHeight = var14[0].getWidth();
                        if (sheetHeight < minSpriteSize || this.mipmapLevels > 0) {
                            var14[0] = ((this.mipmapLevels > 0) ? TextureUtils.scaleToPowerOfTwo(var14[0], minSpriteSize) : TextureUtils.scaleMinTo(var14[0], minSpriteSize));
                            final int listSprites = var14[0].getWidth();
                            if (listSprites != sheetHeight) {
                                if (!TextureUtils.isPowerOfTwo(sheetHeight)) {
                                    Config.log("Scaled non power of 2: " + var10.getIconName() + ", " + sheetHeight + " -> " + listSprites);
                                }
                                else {
                                    Config.log("Scaled too small texture: " + var10.getIconName() + ", " + sheetHeight + " -> " + listSprites);
                                }
                            }
                        }
                    }
                    final TextureMetadataSection sheetHeight2 = (TextureMetadataSection)var13.getMetadata("texture");
                    if (sheetHeight2 != null) {
                        final List listSprites2 = sheetHeight2.getListMipmaps();
                        if (!listSprites2.isEmpty()) {
                            final int tas = var14[0].getWidth();
                            final int it = var14[0].getHeight();
                            if (MathHelper.roundUpToPowerOfTwo(tas) != tas || MathHelper.roundUpToPowerOfTwo(it) != it) {
                                throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                            }
                        }
                        final Iterator tas2 = listSprites2.iterator();
                        while (tas2.hasNext()) {
                            final int it = tas2.next();
                            if (it > 0 && it < var14.length - 1 && var14[it] == null) {
                                final ResourceLocation ss = this.completeResourceLocation(var11, it);
                                try {
                                    var14[it] = TextureUtil.func_177053_a(ShadersTex.loadResource(p_110571_1_, ss).getInputStream());
                                }
                                catch (IOException var15) {
                                    TextureMap.logger.error("Unable to load miplevel {} from: {}", new Object[] { it, ss, var15 });
                                }
                            }
                        }
                    }
                    final AnimationMetadataSection listSprites3 = (AnimationMetadataSection)var13.getMetadata("animation");
                    var10.func_180598_a(var14, listSprites3);
                }
                catch (RuntimeException var16) {
                    TextureMap.logger.error("Unable to parse metadata from " + var12, (Throwable)var16);
                    ReflectorForge.FMLClientHandler_trackBrokenTexture(var12, var16.getMessage());
                    continue;
                }
                catch (IOException var17) {
                    TextureMap.logger.error("Using missing texture, unable to load " + var12 + ", " + var17.getClass().getName());
                    ReflectorForge.FMLClientHandler_trackMissingTexture(var12);
                    continue;
                }
                var6 = Math.min(var6, Math.min(var10.getIconWidth(), var10.getIconHeight()));
                final int var18 = Math.min(Integer.lowestOneBit(var10.getIconWidth()), Integer.lowestOneBit(var10.getIconHeight()));
                if (var18 < var7) {
                    TextureMap.logger.warn("Texture {} with size {}x{} limits mip level from {} to {}", new Object[] { var12, var10.getIconWidth(), var10.getIconHeight(), MathHelper.calculateLogBaseTwo(var7), MathHelper.calculateLogBaseTwo(var18) });
                    var7 = var18;
                }
                var5.addSprite(var10);
            }
        }
        final int var19 = Math.min(var6, var7);
        int var20 = MathHelper.calculateLogBaseTwo(var19);
        if (var20 < 0) {
            var20 = 0;
        }
        if (var20 < this.mipmapLevels) {
            TextureMap.logger.info("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", new Object[] { this.basePath, this.mipmapLevels, var20, var19 });
            this.mipmapLevels = var20;
        }
        final Iterator var21 = this.mapRegisteredSprites.values().iterator();
        while (var21.hasNext() && !this.skipFirst) {
            final TextureAtlasSprite var22 = var21.next();
            try {
                var22.generateMipmaps(this.mipmapLevels);
            }
            catch (Throwable var24) {
                final CrashReport var23 = CrashReport.makeCrashReport(var24, "Applying mipmap");
                final CrashReportCategory sheetWidth = var23.makeCategory("Sprite being mipmapped");
                sheetWidth.addCrashSectionCallable("Sprite name", new Callable() {
                    private static final String __OBFID = "CL_00001059";
                    
                    @Override
                    public String call() {
                        return var22.getIconName();
                    }
                });
                sheetWidth.addCrashSectionCallable("Sprite size", new Callable() {
                    private static final String __OBFID = "CL_00001060";
                    
                    @Override
                    public String call() {
                        return String.valueOf(var22.getIconWidth()) + " x " + var22.getIconHeight();
                    }
                });
                sheetWidth.addCrashSectionCallable("Sprite frames", new Callable() {
                    private static final String __OBFID = "CL_00001061";
                    
                    @Override
                    public String call() {
                        return String.valueOf(var22.getFrameCount()) + " frames";
                    }
                });
                sheetWidth.addCrashSection("Mipmap levels", this.mipmapLevels);
                throw new ReportedException(var23);
            }
        }
        this.missingImage.generateMipmaps(this.mipmapLevels);
        var5.addSprite(this.missingImage);
        this.skipFirst = false;
        try {
            var5.doStitch();
        }
        catch (StitcherException var25) {
            throw var25;
        }
        TextureMap.logger.info("Created: {}x{} {}-atlas", new Object[] { var5.getCurrentWidth(), var5.getCurrentHeight(), this.basePath });
        if (Config.isShaders()) {
            ShadersTex.allocateTextureMap(this.getGlTextureId(), this.mipmapLevels, var5.getCurrentWidth(), var5.getCurrentHeight(), var5, this);
        }
        else {
            TextureUtil.func_180600_a(this.getGlTextureId(), this.mipmapLevels, var5.getCurrentWidth(), var5.getCurrentHeight());
        }
        final HashMap var26 = Maps.newHashMap(this.mapRegisteredSprites);
        for (final TextureAtlasSprite var28 : var5.getStichSlots()) {
            if (Config.isShaders()) {
                ShadersTex.setIconName(ShadersTex.setSprite(var28).getIconName());
            }
            final String sheetWidth2 = var28.getIconName();
            var26.remove(sheetWidth2);
            this.mapUploadedSprites.put(sheetWidth2, var28);
            try {
                if (Config.isShaders()) {
                    ShadersTex.uploadTexSubForLoadAtlas(var28.getFrameTextureData(0), var28.getIconWidth(), var28.getIconHeight(), var28.getOriginX(), var28.getOriginY(), false, false);
                }
                else {
                    TextureUtil.uploadTextureMipmap(var28.getFrameTextureData(0), var28.getIconWidth(), var28.getIconHeight(), var28.getOriginX(), var28.getOriginY(), false, false);
                }
            }
            catch (Throwable var29) {
                final CrashReport listSprites4 = CrashReport.makeCrashReport(var29, "Stitching texture atlas");
                final CrashReportCategory it2 = listSprites4.makeCategory("Texture being stitched together");
                it2.addCrashSection("Atlas path", this.basePath);
                it2.addCrashSection("Sprite", var28);
                throw new ReportedException(listSprites4);
            }
            if (var28.hasAnimationMetadata()) {
                this.listAnimatedSprites.add(var28);
            }
        }
        for (final TextureAtlasSprite var28 : var26.values()) {
            var28.copyFrom(this.missingImage);
        }
        if (Config.isMultiTexture()) {
            final int sheetWidth3 = var5.getCurrentWidth();
            final int sheetHeight = var5.getCurrentHeight();
            final List listSprites2 = var5.getStichSlots();
            for (final TextureAtlasSprite tas3 : listSprites2) {
                tas3.sheetWidth = sheetWidth3;
                tas3.sheetHeight = sheetHeight;
                tas3.mipmapLevels = this.mipmapLevels;
                final TextureAtlasSprite ss2 = tas3.spriteSingle;
                if (ss2 != null) {
                    ss2.sheetWidth = sheetWidth3;
                    ss2.sheetHeight = sheetHeight;
                    ss2.mipmapLevels = this.mipmapLevels;
                    tas3.bindSpriteTexture();
                    final boolean texBlur = false;
                    final boolean texClamp = true;
                    TextureUtil.uploadTextureMipmap(ss2.getFrameTextureData(0), ss2.getIconWidth(), ss2.getIconHeight(), ss2.getOriginX(), ss2.getOriginY(), texBlur, texClamp);
                }
            }
            Config.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        }
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, this);
        this.updateIconGrid(var5.getCurrentWidth(), var5.getCurrentHeight());
        if (Config.equals(System.getProperty("saveTextureMap"), "true")) {
            Config.dbg("Exporting texture map to: " + this.basePath + "_x.png");
            TextureUtil.func_177055_a(this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, var5.getCurrentWidth(), var5.getCurrentHeight());
        }
    }
    
    public ResourceLocation completeResourceLocation(final ResourceLocation p_147634_1_, final int p_147634_2_) {
        return this.isAbsoluteLocation(p_147634_1_) ? ((p_147634_2_ == 0) ? new ResourceLocation(p_147634_1_.getResourceDomain(), String.valueOf(p_147634_1_.getResourcePath()) + ".png") : new ResourceLocation(p_147634_1_.getResourceDomain(), String.valueOf(p_147634_1_.getResourcePath()) + "mipmap" + p_147634_2_ + ".png")) : ((p_147634_2_ == 0) ? new ResourceLocation(p_147634_1_.getResourceDomain(), String.format("%s/%s%s", this.basePath, p_147634_1_.getResourcePath(), ".png")) : new ResourceLocation(p_147634_1_.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", this.basePath, p_147634_1_.getResourcePath(), p_147634_2_, ".png")));
    }
    
    public TextureAtlasSprite getAtlasSprite(final String p_110572_1_) {
        TextureAtlasSprite var2 = this.mapUploadedSprites.get(p_110572_1_);
        if (var2 == null) {
            var2 = this.missingImage;
        }
        return var2;
    }
    
    public void updateAnimations() {
        if (Config.isShaders()) {
            ShadersTex.updatingTex = this.getMultiTexID();
        }
        TextureUtil.bindTexture(this.getGlTextureId());
        for (final TextureAtlasSprite it : this.listAnimatedSprites) {
            if (this.isTerrainAnimationActive(it)) {
                it.updateAnimation();
            }
        }
        if (Config.isMultiTexture()) {
            for (final TextureAtlasSprite ts : this.listAnimatedSprites) {
                if (this.isTerrainAnimationActive(ts)) {
                    final TextureAtlasSprite spriteSingle = ts.spriteSingle;
                    if (spriteSingle == null) {
                        continue;
                    }
                    if (ts == TextureUtils.iconClock || ts == TextureUtils.iconCompass) {
                        spriteSingle.frameCounter = ts.frameCounter;
                    }
                    ts.bindSpriteTexture();
                    spriteSingle.updateAnimation();
                }
            }
            TextureUtil.bindTexture(this.getGlTextureId());
        }
        if (Config.isShaders()) {
            ShadersTex.updatingTex = null;
        }
    }
    
    public TextureAtlasSprite func_174942_a(final ResourceLocation p_174942_1_) {
        if (p_174942_1_ == null) {
            throw new IllegalArgumentException("Location cannot be null!");
        }
        TextureAtlasSprite var2 = this.mapRegisteredSprites.get(p_174942_1_.toString());
        if (var2 == null) {
            var2 = TextureAtlasSprite.func_176604_a(p_174942_1_);
            this.mapRegisteredSprites.put(p_174942_1_.toString(), var2);
            if (var2 instanceof TextureAtlasSprite && var2.getIndexInMap() < 0) {
                var2.setIndexInMap(this.mapRegisteredSprites.size());
            }
        }
        return var2;
    }
    
    @Override
    public void tick() {
        this.updateAnimations();
    }
    
    public void setMipmapLevels(final int p_147633_1_) {
        this.mipmapLevels = p_147633_1_;
    }
    
    public TextureAtlasSprite func_174944_f() {
        return this.missingImage;
    }
    
    public TextureAtlasSprite getTextureExtry(final String name) {
        final ResourceLocation loc = new ResourceLocation(name);
        return this.mapRegisteredSprites.get(loc.toString());
    }
    
    public boolean setTextureEntry(final String name, final TextureAtlasSprite entry) {
        if (!this.mapRegisteredSprites.containsKey(name)) {
            this.mapRegisteredSprites.put(name, entry);
            if (entry.getIndexInMap() < 0) {
                entry.setIndexInMap(this.mapRegisteredSprites.size());
            }
            return true;
        }
        return false;
    }
    
    private boolean isAbsoluteLocation(final ResourceLocation loc) {
        final String path = loc.getResourcePath();
        return this.isAbsoluteLocationPath(path);
    }
    
    private boolean isAbsoluteLocationPath(final String resPath) {
        final String path = resPath.toLowerCase();
        return path.startsWith("mcpatcher/") || path.startsWith("optifine/");
    }
    
    public TextureAtlasSprite getSpriteSafe(final String name) {
        final ResourceLocation loc = new ResourceLocation(name);
        return this.mapRegisteredSprites.get(loc.toString());
    }
    
    private boolean isTerrainAnimationActive(final TextureAtlasSprite ts) {
        return (ts != TextureUtils.iconWaterStill && ts != TextureUtils.iconWaterFlow) ? ((ts != TextureUtils.iconLavaStill && ts != TextureUtils.iconLavaFlow) ? ((ts != TextureUtils.iconFireLayer0 && ts != TextureUtils.iconFireLayer1) ? ((ts == TextureUtils.iconPortal) ? Config.isAnimatedPortal() : (ts == TextureUtils.iconClock || ts == TextureUtils.iconCompass || Config.isAnimatedTerrain())) : Config.isAnimatedFire()) : Config.isAnimatedLava()) : Config.isAnimatedWater();
    }
    
    public int getCountRegisteredSprites() {
        return this.mapRegisteredSprites.size();
    }
    
    private int detectMaxMipmapLevel(final Map mapSprites, final IResourceManager rm) {
        int minSize = this.detectMinimumSpriteSize(mapSprites, rm, 20);
        if (minSize < 16) {
            minSize = 16;
        }
        minSize = MathHelper.roundUpToPowerOfTwo(minSize);
        if (minSize > 16) {
            Config.log("Sprite size: " + minSize);
        }
        int minLevel = MathHelper.calculateLogBaseTwo(minSize);
        if (minLevel < 4) {
            minLevel = 4;
        }
        return minLevel;
    }
    
    private int detectMinimumSpriteSize(final Map mapSprites, final IResourceManager rm, final int percentScale) {
        final HashMap mapSizeCounts = new HashMap();
        final Set entrySetSprites = mapSprites.entrySet();
        for (final Map.Entry setSizes : entrySetSprites) {
            final TextureAtlasSprite setSizesSorted = setSizes.getValue();
            final ResourceLocation minSize = new ResourceLocation(setSizesSorted.getIconName());
            final ResourceLocation countScale = this.completeResourceLocation(minSize, 0);
            if (!setSizesSorted.hasCustomLoader(rm, minSize)) {
                try {
                    final IResource countScaleMax = rm.getResource(countScale);
                    if (countScaleMax == null) {
                        continue;
                    }
                    final InputStream it = countScaleMax.getInputStream();
                    if (it == null) {
                        continue;
                    }
                    final Dimension size = TextureUtils.getImageSize(it, "png");
                    if (size == null) {
                        continue;
                    }
                    final int count = size.width;
                    final int width2 = MathHelper.roundUpToPowerOfTwo(count);
                    if (!mapSizeCounts.containsKey(width2)) {
                        mapSizeCounts.put(width2, 1);
                    }
                    else {
                        final int count2 = mapSizeCounts.get(width2);
                        mapSizeCounts.put(width2, count2 + 1);
                    }
                }
                catch (Exception ex) {}
            }
        }
        int countSprites2 = 0;
        final Set setSizes2 = mapSizeCounts.keySet();
        final TreeSet setSizesSorted2 = new TreeSet(setSizes2);
        for (final int countScale2 : setSizesSorted2) {
            final int countScaleMax2 = mapSizeCounts.get(countScale2);
            countSprites2 += countScaleMax2;
        }
        int minSize3 = 16;
        int countScale2 = 0;
        final int countScaleMax2 = countSprites2 * percentScale / 100;
        for (final int size2 : setSizesSorted2) {
            final int count = mapSizeCounts.get(size2);
            countScale2 += count;
            if (size2 > minSize3) {
                minSize3 = size2;
            }
            if (countScale2 > countScaleMax2) {
                return minSize3;
            }
        }
        return minSize3;
    }
    
    private int getMinSpriteSize() {
        int minSize = 1 << this.mipmapLevels;
        if (minSize < 8) {
            minSize = 8;
        }
        return minSize;
    }
    
    private int[] getMissingImageData(final int size) {
        final BufferedImage bi = new BufferedImage(16, 16, 2);
        bi.setRGB(0, 0, 16, 16, TextureUtil.missingTextureData, 0, 16);
        final BufferedImage bi2 = TextureUtils.scaleToPowerOfTwo(bi, size);
        final int[] data = new int[size * size];
        bi2.getRGB(0, 0, size, size, data, 0, size);
        return data;
    }
    
    public boolean isTextureBound() {
        final int boundTexId = GlStateManager.getBoundTexture();
        final int texId = this.getGlTextureId();
        return boundTexId == texId;
    }
    
    private void updateIconGrid(final int sheetWidth, final int sheetHeight) {
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGrid = null;
        if (this.iconGridSize > 0) {
            this.iconGridCountX = sheetWidth / this.iconGridSize;
            this.iconGridCountY = sheetHeight / this.iconGridSize;
            this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
            this.iconGridSizeU = 1.0 / this.iconGridCountX;
            this.iconGridSizeV = 1.0 / this.iconGridCountY;
            for (final TextureAtlasSprite ts : this.mapUploadedSprites.values()) {
                final double deltaU = 0.5 / sheetWidth;
                final double deltaV = 0.5 / sheetHeight;
                final double uMin = Math.min(ts.getMinU(), ts.getMaxU()) + deltaU;
                final double vMin = Math.min(ts.getMinV(), ts.getMaxV()) + deltaV;
                final double uMax = Math.max(ts.getMinU(), ts.getMaxU()) - deltaU;
                final double vMax = Math.max(ts.getMinV(), ts.getMaxV()) - deltaV;
                final int iuMin = (int)(uMin / this.iconGridSizeU);
                final int ivMin = (int)(vMin / this.iconGridSizeV);
                final int iuMax = (int)(uMax / this.iconGridSizeU);
                final int ivMax = (int)(vMax / this.iconGridSizeV);
                for (int iu = iuMin; iu <= iuMax; ++iu) {
                    if (iu >= 0 && iu < this.iconGridCountX) {
                        for (int iv = ivMin; iv <= ivMax; ++iv) {
                            if (iv >= 0 && iv < this.iconGridCountX) {
                                final int index = iv * this.iconGridCountX + iu;
                                this.iconGrid[index] = ts;
                            }
                            else {
                                Config.warn("Invalid grid V: " + iv + ", icon: " + ts.getIconName());
                            }
                        }
                    }
                    else {
                        Config.warn("Invalid grid U: " + iu + ", icon: " + ts.getIconName());
                    }
                }
            }
        }
    }
    
    public TextureAtlasSprite getIconByUV(final double u, final double v) {
        if (this.iconGrid == null) {
            return null;
        }
        final int iu = (int)(u / this.iconGridSizeU);
        final int iv = (int)(v / this.iconGridSizeV);
        final int index = iv * this.iconGridCountX + iu;
        return (index >= 0 && index <= this.iconGrid.length) ? this.iconGrid[index] : null;
    }
}
