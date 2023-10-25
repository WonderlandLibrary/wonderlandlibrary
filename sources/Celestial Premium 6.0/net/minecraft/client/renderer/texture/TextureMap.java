/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureMapPopulator;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.PngSizeInfo;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import optifine.BetterGrass;
import optifine.Config;
import optifine.ConnectedTextures;
import optifine.CustomItems;
import optifine.Reflector;
import optifine.ReflectorForge;
import optifine.SpriteDependencies;
import optifine.TextureUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

public class TextureMap
extends AbstractTexture
implements ITickableTextureObject {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
    public static final ResourceLocation LOCATION_BLOCKS_TEXTURE = new ResourceLocation("textures/atlas/blocks.png");
    private final List<TextureAtlasSprite> listAnimatedSprites = Lists.newArrayList();
    private final Map<String, TextureAtlasSprite> mapRegisteredSprites = Maps.newHashMap();
    private final Map<String, TextureAtlasSprite> mapUploadedSprites = Maps.newHashMap();
    private final String basePath;
    private final ITextureMapPopulator iconCreator;
    private int mipmapLevels;
    private final TextureAtlasSprite missingImage = new TextureAtlasSprite("missingno");
    private TextureAtlasSprite[] iconGrid = null;
    private int iconGridSize = -1;
    private int iconGridCountX = -1;
    private int iconGridCountY = -1;
    private double iconGridSizeU = -1.0;
    private double iconGridSizeV = -1.0;
    private int counterIndexInMap = 0;
    public int atlasWidth = 0;
    public int atlasHeight = 0;

    public TextureMap(String basePathIn) {
        this(basePathIn, null);
    }

    public TextureMap(String p_i3_1_, boolean p_i3_2_) {
        this(p_i3_1_, null, p_i3_2_);
    }

    public TextureMap(String basePathIn, @Nullable ITextureMapPopulator iconCreatorIn) {
        this(basePathIn, iconCreatorIn, false);
    }

    public TextureMap(String p_i4_1_, ITextureMapPopulator p_i4_2_, boolean p_i4_3_) {
        this.basePath = p_i4_1_;
        this.iconCreator = p_i4_2_;
    }

    private void initMissingImage() {
        int i = this.getMinSpriteSize();
        int[] aint = this.getMissingImageData(i);
        this.missingImage.setIconWidth(i);
        this.missingImage.setIconHeight(i);
        int[][] aint1 = new int[this.mipmapLevels + 1][];
        aint1[0] = aint;
        this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][]{aint1}));
        this.missingImage.setIndexInMap(this.counterIndexInMap++);
    }

    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        ShadersTex.resManager = resourceManager;
        if (this.iconCreator != null) {
            this.loadSprites(resourceManager, this.iconCreator);
        }
    }

    public void loadSprites(IResourceManager resourceManager, ITextureMapPopulator iconCreatorIn) {
        this.mapRegisteredSprites.clear();
        this.counterIndexInMap = 0;
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void loadTextureAtlas(IResourceManager resourceManager) {
        int j;
        ShadersTex.resManager = resourceManager;
        Config.dbg("Multitexture: " + Config.isMultiTexture());
        if (Config.isMultiTexture()) {
            for (TextureAtlasSprite textureatlassprite : this.mapUploadedSprites.values()) {
                textureatlassprite.deleteSpriteTexture();
            }
        }
        ConnectedTextures.updateIcons(this);
        CustomItems.updateIcons(this);
        BetterGrass.updateIcons(this);
        int k1 = TextureUtils.getGLMaximumTextureSize();
        Stitcher stitcher = new Stitcher(k1, k1, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int i = Integer.MAX_VALUE;
        this.iconGridSize = j = this.getMinSpriteSize();
        int k = 1 << this.mipmapLevels;
        ArrayList<TextureAtlasSprite> list = new ArrayList<TextureAtlasSprite>(this.mapRegisteredSprites.values());
        for (int l = 0; l < list.size(); ++l) {
            ResourceLocation resourcelocation;
            TextureAtlasSprite textureatlassprite1;
            block37: {
                textureatlassprite1 = SpriteDependencies.resolveDependencies(list, l, this);
                resourcelocation = this.getResourceLocation(textureatlassprite1);
                Object iresource = null;
                if (textureatlassprite1.getIndexInMap() < 0) {
                    textureatlassprite1.setIndexInMap(this.counterIndexInMap++);
                }
                if (textureatlassprite1.hasCustomLoader(resourceManager, resourcelocation)) {
                    if (textureatlassprite1.load(resourceManager, resourcelocation, p_lambda$loadTextureAtlas$0_1_ -> this.mapRegisteredSprites.get(p_lambda$loadTextureAtlas$0_1_.toString()))) {
                        Config.dbg("Custom loader (skipped): " + textureatlassprite1);
                        continue;
                    }
                    Config.dbg("Custom loader: " + textureatlassprite1);
                } else {
                    PngSizeInfo pngsizeinfo = PngSizeInfo.makeFromResource(resourceManager.getResource(resourcelocation));
                    iresource = Config.isShaders() ? ShadersTex.loadResource(resourceManager, resourcelocation) : resourceManager.getResource(resourcelocation);
                    boolean flag = iresource.getMetadata("animation") != null;
                    textureatlassprite1.loadSprite(pngsizeinfo, flag);
                    IOUtils.closeQuietly((Closeable)iresource);
                }
                break block37;
                catch (RuntimeException runtimeexception) {
                    LOGGER.error("Unable to parse metadata from {}", resourcelocation, runtimeexception);
                    ReflectorForge.FMLClientHandler_trackBrokenTexture(resourcelocation, runtimeexception.getMessage());
                    {
                        catch (Throwable throwable) {
                            IOUtils.closeQuietly(iresource);
                            throw throwable;
                        }
                    }
                    IOUtils.closeQuietly((Closeable)iresource);
                    continue;
                    catch (IOException ioexception) {
                        LOGGER.error("Using missing texture, unable to load " + resourcelocation + ", " + ioexception.getClass().getName());
                        ReflectorForge.FMLClientHandler_trackMissingTexture(resourcelocation);
                        IOUtils.closeQuietly((Closeable)iresource);
                        continue;
                    }
                }
            }
            int k2 = textureatlassprite1.getIconWidth();
            int i3 = textureatlassprite1.getIconHeight();
            if (k2 >= 1 && i3 >= 1) {
                if (k2 < j || this.mipmapLevels > 0) {
                    int i1;
                    int n = i1 = this.mipmapLevels > 0 ? TextureUtils.scaleToPowerOfTwo(k2, j) : TextureUtils.scaleMinTo(k2, j);
                    if (i1 != k2) {
                        if (!TextureUtils.isPowerOfTwo(k2)) {
                            Config.log("Scaled non power of 2: " + textureatlassprite1.getIconName() + ", " + k2 + " -> " + i1);
                        } else {
                            Config.log("Scaled too small texture: " + textureatlassprite1.getIconName() + ", " + k2 + " -> " + i1);
                        }
                        int j1 = i3 * i1 / k2;
                        textureatlassprite1.setIconWidth(i1);
                        textureatlassprite1.setIconHeight(j1);
                    }
                }
                i = Math.min(i, Math.min(textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight()));
                int j3 = Math.min(Integer.lowestOneBit(textureatlassprite1.getIconWidth()), Integer.lowestOneBit(textureatlassprite1.getIconHeight()));
                if (j3 < k) {
                    LOGGER.warn("Texture {} with size {}x{} limits mip level from {} to {}", resourcelocation, textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight(), MathHelper.log2(k), MathHelper.log2(j3));
                    k = j3;
                }
                if (!this.generateMipmaps(resourceManager, textureatlassprite1)) continue;
                stitcher.addSprite(textureatlassprite1);
                continue;
            }
            Config.warn("Invalid sprite size: " + textureatlassprite1);
        }
        int l1 = Math.min(i, k);
        int i2 = MathHelper.log2(l1);
        if (i2 < 0) {
            i2 = 0;
        }
        if (i2 < this.mipmapLevels) {
            LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.basePath, this.mipmapLevels, i2, l1);
            this.mipmapLevels = i2;
        }
        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);
        stitcher.doStitch();
        LOGGER.info("Created: {}x{} {}-atlas", stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), this.basePath);
        if (Config.isShaders()) {
            ShadersTex.allocateTextureMap(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), stitcher, this);
        } else {
            TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }
        HashMap<String, TextureAtlasSprite> map = Maps.newHashMap(this.mapRegisteredSprites);
        for (TextureAtlasSprite textureatlassprite2 : stitcher.getStichSlots()) {
            if (Config.isShaders()) {
                ShadersTex.setIconName(ShadersTex.setSprite(textureatlassprite2).getIconName());
            }
            String s = textureatlassprite2.getIconName();
            map.remove(s);
            this.mapUploadedSprites.put(s, textureatlassprite2);
            try {
                if (Config.isShaders()) {
                    ShadersTex.uploadTexSubForLoadAtlas(textureatlassprite2.getFrameTextureData(0), textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight(), textureatlassprite2.getOriginX(), textureatlassprite2.getOriginY(), false, false);
                } else {
                    TextureUtil.uploadTextureMipmap(textureatlassprite2.getFrameTextureData(0), textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight(), textureatlassprite2.getOriginX(), textureatlassprite2.getOriginY(), false, false);
                }
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Texture being stitched together");
                crashreportcategory.addCrashSection("Atlas path", this.basePath);
                crashreportcategory.addCrashSection("Sprite", textureatlassprite2);
                throw new ReportedException(crashreport);
            }
            if (!textureatlassprite2.hasAnimationMetadata()) continue;
            this.listAnimatedSprites.add(textureatlassprite2);
        }
        for (TextureAtlasSprite textureatlassprite3 : map.values()) {
            textureatlassprite3.copyFrom(this.missingImage);
        }
        if (Config.isMultiTexture()) {
            int j2 = stitcher.getCurrentWidth();
            int l2 = stitcher.getCurrentHeight();
            for (TextureAtlasSprite textureatlassprite4 : stitcher.getStichSlots()) {
                textureatlassprite4.sheetWidth = j2;
                textureatlassprite4.sheetHeight = l2;
                textureatlassprite4.mipmapLevels = this.mipmapLevels;
                TextureAtlasSprite textureatlassprite5 = textureatlassprite4.spriteSingle;
                if (textureatlassprite5 == null) continue;
                if (textureatlassprite5.getIconWidth() <= 0) {
                    textureatlassprite5.setIconWidth(textureatlassprite4.getIconWidth());
                    textureatlassprite5.setIconHeight(textureatlassprite4.getIconHeight());
                    textureatlassprite5.initSprite(textureatlassprite4.getIconWidth(), textureatlassprite4.getIconHeight(), 0, 0, false);
                    textureatlassprite5.clearFramesTextureData();
                    List<int[][]> list1 = textureatlassprite4.getFramesTextureData();
                    textureatlassprite5.setFramesTextureData(list1);
                    textureatlassprite5.setAnimationMetadata(textureatlassprite4.getAnimationMetadata());
                }
                textureatlassprite5.sheetWidth = j2;
                textureatlassprite5.sheetHeight = l2;
                textureatlassprite5.mipmapLevels = this.mipmapLevels;
                textureatlassprite4.bindSpriteTexture();
                boolean flag2 = false;
                boolean flag1 = true;
                try {
                    TextureUtil.uploadTextureMipmap(textureatlassprite5.getFrameTextureData(0), textureatlassprite5.getIconWidth(), textureatlassprite5.getIconHeight(), textureatlassprite5.getOriginX(), textureatlassprite5.getOriginY(), flag2, flag1);
                }
                catch (Exception exception) {
                    Config.dbg("Error uploading sprite single: " + textureatlassprite5 + ", parent: " + textureatlassprite4);
                    exception.printStackTrace();
                }
            }
            Config.getInstance().getTextureManager().bindTexture(LOCATION_BLOCKS_TEXTURE);
        }
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, this);
        this.updateIconGrid(stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        if (!Config.equals(System.getProperty("saveTextureMap"), "true")) return;
        Config.dbg("Exporting texture map: " + this.basePath);
        TextureUtils.saveGlTexture("debug/" + this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean generateMipmaps(IResourceManager resourceManager, final TextureAtlasSprite texture) {
        block11: {
            ResourceLocation resourcelocation1 = this.getResourceLocation(texture);
            IResource iresource1 = null;
            if (texture.hasCustomLoader(resourceManager, resourcelocation1)) {
                TextureUtils.generateCustomMipmaps(texture, this.mipmapLevels);
            } else {
                boolean flag4;
                try {
                    iresource1 = resourceManager.getResource(resourcelocation1);
                    texture.loadSpriteFrames(iresource1, this.mipmapLevels + 1);
                    break block11;
                }
                catch (RuntimeException runtimeexception1) {
                    LOGGER.error("Unable to parse metadata from {}", resourcelocation1, runtimeexception1);
                    flag4 = false;
                }
                catch (IOException ioexception1) {
                    boolean flag42;
                    boolean crashreportcategory;
                    LOGGER.error("Using missing texture, unable to load {}", resourcelocation1, ioexception1);
                    boolean bl = crashreportcategory = (flag42 = false);
                    return bl;
                }
                finally {
                    IOUtils.closeQuietly((Closeable)iresource1);
                }
                return flag4;
            }
        }
        try {
            texture.generateMipmaps(this.mipmapLevels);
            return true;
        }
        catch (Throwable throwable1) {
            CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
            CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Sprite being mipmapped");
            crashreportcategory1.setDetail("Sprite name", new ICrashReportDetail<String>(){

                @Override
                public String call() throws Exception {
                    return texture.getIconName();
                }
            });
            crashreportcategory1.setDetail("Sprite size", new ICrashReportDetail<String>(){

                @Override
                public String call() throws Exception {
                    return texture.getIconWidth() + " x " + texture.getIconHeight();
                }
            });
            crashreportcategory1.setDetail("Sprite frames", new ICrashReportDetail<String>(){

                @Override
                public String call() throws Exception {
                    return texture.getFrameCount() + " frames";
                }
            });
            crashreportcategory1.addCrashSection("Mipmap levels", this.mipmapLevels);
            throw new ReportedException(crashreport1);
        }
    }

    public ResourceLocation getResourceLocation(TextureAtlasSprite p_184396_1_) {
        ResourceLocation resourcelocation1 = new ResourceLocation(p_184396_1_.getIconName());
        return this.completeResourceLocation(resourcelocation1);
    }

    public ResourceLocation completeResourceLocation(ResourceLocation p_completeResourceLocation_1_) {
        return this.isAbsoluteLocation(p_completeResourceLocation_1_) ? new ResourceLocation(p_completeResourceLocation_1_.getNamespace(), p_completeResourceLocation_1_.getPath() + ".png") : new ResourceLocation(p_completeResourceLocation_1_.getNamespace(), String.format("%s/%s%s", this.basePath, p_completeResourceLocation_1_.getPath(), ".png"));
    }

    public TextureAtlasSprite getAtlasSprite(String iconName) {
        TextureAtlasSprite textureatlassprite6 = this.mapUploadedSprites.get(iconName);
        if (textureatlassprite6 == null) {
            textureatlassprite6 = this.missingImage;
        }
        return textureatlassprite6;
    }

    public void updateAnimations() {
        if (Config.isShaders()) {
            ShadersTex.updatingTex = this.getMultiTexID();
        }
        boolean flag3 = false;
        boolean flag4 = false;
        TextureUtil.bindTexture(this.getGlTextureId());
        for (TextureAtlasSprite textureatlassprite6 : this.listAnimatedSprites) {
            if (!this.isTerrainAnimationActive(textureatlassprite6)) continue;
            textureatlassprite6.updateAnimation();
            if (textureatlassprite6.spriteNormal != null) {
                flag3 = true;
            }
            if (textureatlassprite6.spriteSpecular == null) continue;
            flag4 = true;
        }
        if (Config.isMultiTexture()) {
            for (TextureAtlasSprite textureatlassprite8 : this.listAnimatedSprites) {
                TextureAtlasSprite textureatlassprite7;
                if (!this.isTerrainAnimationActive(textureatlassprite8) || (textureatlassprite7 = textureatlassprite8.spriteSingle) == null) continue;
                if (textureatlassprite8 == TextureUtils.iconClock || textureatlassprite8 == TextureUtils.iconCompass) {
                    textureatlassprite7.frameCounter = textureatlassprite8.frameCounter;
                }
                textureatlassprite8.bindSpriteTexture();
                textureatlassprite7.updateAnimation();
            }
            TextureUtil.bindTexture(this.getGlTextureId());
        }
        if (Config.isShaders()) {
            if (flag3) {
                TextureUtil.bindTexture(this.getMultiTexID().norm);
                for (TextureAtlasSprite textureatlassprite9 : this.listAnimatedSprites) {
                    if (textureatlassprite9.spriteNormal == null || !this.isTerrainAnimationActive(textureatlassprite9)) continue;
                    if (textureatlassprite9 == TextureUtils.iconClock || textureatlassprite9 == TextureUtils.iconCompass) {
                        textureatlassprite9.spriteNormal.frameCounter = textureatlassprite9.frameCounter;
                    }
                    textureatlassprite9.spriteNormal.updateAnimation();
                }
            }
            if (flag4) {
                TextureUtil.bindTexture(this.getMultiTexID().spec);
                for (TextureAtlasSprite textureatlassprite10 : this.listAnimatedSprites) {
                    if (textureatlassprite10.spriteSpecular == null || !this.isTerrainAnimationActive(textureatlassprite10)) continue;
                    if (textureatlassprite10 == TextureUtils.iconClock || textureatlassprite10 == TextureUtils.iconCompass) {
                        textureatlassprite10.spriteNormal.frameCounter = textureatlassprite10.frameCounter;
                    }
                    textureatlassprite10.spriteSpecular.updateAnimation();
                }
            }
            if (flag3 || flag4) {
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
        }
        TextureAtlasSprite textureatlassprite6 = this.mapRegisteredSprites.get(location.toString());
        if (textureatlassprite6 == null) {
            textureatlassprite6 = TextureAtlasSprite.makeAtlasSprite(location);
            this.mapRegisteredSprites.put(location.toString(), textureatlassprite6);
            if (textureatlassprite6.getIndexInMap() < 0) {
                textureatlassprite6.setIndexInMap(this.counterIndexInMap++);
            }
        }
        return textureatlassprite6;
    }

    @Override
    public void tick() {
        this.updateAnimations();
    }

    public void setMipmapLevels(int mipmapLevelsIn) {
        this.mipmapLevels = mipmapLevelsIn;
    }

    public TextureAtlasSprite getMissingSprite() {
        return this.missingImage;
    }

    @Nullable
    public TextureAtlasSprite getTextureExtry(String p_getTextureExtry_1_) {
        return this.mapRegisteredSprites.get(p_getTextureExtry_1_);
    }

    public boolean setTextureEntry(TextureAtlasSprite p_setTextureEntry_1_) {
        String s1 = p_setTextureEntry_1_.getIconName();
        if (!this.mapRegisteredSprites.containsKey(s1)) {
            this.mapRegisteredSprites.put(s1, p_setTextureEntry_1_);
            if (p_setTextureEntry_1_.getIndexInMap() < 0) {
                p_setTextureEntry_1_.setIndexInMap(this.counterIndexInMap++);
            }
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

    private boolean isAbsoluteLocation(ResourceLocation p_isAbsoluteLocation_1_) {
        String s1 = p_isAbsoluteLocation_1_.getPath();
        return this.isAbsoluteLocationPath(s1);
    }

    private boolean isAbsoluteLocationPath(String p_isAbsoluteLocationPath_1_) {
        String s1 = p_isAbsoluteLocationPath_1_.toLowerCase();
        return s1.startsWith("mcpatcher/") || s1.startsWith("optifine/");
    }

    public TextureAtlasSprite getSpriteSafe(String p_getSpriteSafe_1_) {
        ResourceLocation resourcelocation1 = new ResourceLocation(p_getSpriteSafe_1_);
        return this.mapRegisteredSprites.get(resourcelocation1.toString());
    }

    public TextureAtlasSprite getRegisteredSprite(ResourceLocation p_getRegisteredSprite_1_) {
        return this.mapRegisteredSprites.get(p_getRegisteredSprite_1_.toString());
    }

    private boolean isTerrainAnimationActive(TextureAtlasSprite p_isTerrainAnimationActive_1_) {
        if (p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterFlow) {
            if (p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaFlow) {
                if (p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer0 && p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer1) {
                    if (p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal) {
                        return Config.isAnimatedPortal();
                    }
                    return p_isTerrainAnimationActive_1_ != TextureUtils.iconClock && p_isTerrainAnimationActive_1_ != TextureUtils.iconCompass ? Config.isAnimatedTerrain() : true;
                }
                return Config.isAnimatedFire();
            }
            return Config.isAnimatedLava();
        }
        return Config.isAnimatedWater();
    }

    public int getCountRegisteredSprites() {
        return this.counterIndexInMap;
    }

    private int detectMaxMipmapLevel(Map p_detectMaxMipmapLevel_1_, IResourceManager p_detectMaxMipmapLevel_2_) {
        int l3;
        int k3 = this.detectMinimumSpriteSize(p_detectMaxMipmapLevel_1_, p_detectMaxMipmapLevel_2_, 20);
        if (k3 < 16) {
            k3 = 16;
        }
        if ((k3 = MathHelper.smallestEncompassingPowerOfTwo(k3)) > 16) {
            Config.log("Sprite size: " + k3);
        }
        if ((l3 = MathHelper.log2(k3)) < 4) {
            l3 = 4;
        }
        return l3;
    }

    private int detectMinimumSpriteSize(Map p_detectMinimumSpriteSize_1_, IResourceManager p_detectMinimumSpriteSize_2_, int p_detectMinimumSpriteSize_3_) {
        HashMap<Integer, Integer> map1 = new HashMap<Integer, Integer>();
        for (Map.Entry entry : p_detectMinimumSpriteSize_1_.entrySet()) {
            TextureAtlasSprite textureatlassprite6 = (TextureAtlasSprite)entry.getValue();
            ResourceLocation resourcelocation1 = new ResourceLocation(textureatlassprite6.getIconName());
            ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation1);
            if (textureatlassprite6.hasCustomLoader(p_detectMinimumSpriteSize_2_, resourcelocation1)) continue;
            try {
                Dimension dimension;
                InputStream inputstream;
                IResource iresource1 = p_detectMinimumSpriteSize_2_.getResource(resourcelocation2);
                if (iresource1 == null || (inputstream = iresource1.getInputStream()) == null || (dimension = TextureUtils.getImageSize(inputstream, "png")) == null) continue;
                int k3 = dimension.width;
                int l3 = MathHelper.smallestEncompassingPowerOfTwo(k3);
                if (!map1.containsKey(l3)) {
                    map1.put(l3, 1);
                    continue;
                }
                int i4 = (Integer)map1.get(l3);
                map1.put(l3, i4 + 1);
            }
            catch (Exception iresource1) {}
        }
        int j4 = 0;
        Set set = map1.keySet();
        TreeSet set1 = new TreeSet(set);
        Iterator iterator = set1.iterator();
        while (iterator.hasNext()) {
            int l4 = (Integer)iterator.next();
            int j5 = (Integer)map1.get(l4);
            j4 += j5;
        }
        int k4 = 16;
        int i5 = 0;
        int j5 = j4 * p_detectMinimumSpriteSize_3_ / 100;
        Iterator iterator1 = set1.iterator();
        while (iterator1.hasNext()) {
            int k5 = (Integer)iterator1.next();
            int l5 = (Integer)map1.get(k5);
            i5 += l5;
            if (k5 > k4) {
                k4 = k5;
            }
            if (i5 <= j5) continue;
            return k4;
        }
        return k4;
    }

    private int getMinSpriteSize() {
        int k3 = 1 << this.mipmapLevels;
        if (k3 < 8) {
            k3 = 8;
        }
        return k3;
    }

    private int[] getMissingImageData(int p_getMissingImageData_1_) {
        BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
        bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.MISSING_TEXTURE_DATA, 0, 16);
        BufferedImage bufferedimage1 = TextureUtils.scaleToPowerOfTwo(bufferedimage, p_getMissingImageData_1_);
        int[] aint = new int[p_getMissingImageData_1_ * p_getMissingImageData_1_];
        bufferedimage1.getRGB(0, 0, p_getMissingImageData_1_, p_getMissingImageData_1_, aint, 0, p_getMissingImageData_1_);
        return aint;
    }

    public boolean isTextureBound() {
        int l3;
        int k3 = GlStateManager.getBoundTexture();
        return k3 == (l3 = this.getGlTextureId());
    }

    private void updateIconGrid(int p_updateIconGrid_1_, int p_updateIconGrid_2_) {
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGrid = null;
        if (this.iconGridSize > 0) {
            this.iconGridCountX = p_updateIconGrid_1_ / this.iconGridSize;
            this.iconGridCountY = p_updateIconGrid_2_ / this.iconGridSize;
            this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
            this.iconGridSizeU = 1.0 / (double)this.iconGridCountX;
            this.iconGridSizeV = 1.0 / (double)this.iconGridCountY;
            for (TextureAtlasSprite textureatlassprite6 : this.mapUploadedSprites.values()) {
                double d0 = 0.5 / (double)p_updateIconGrid_1_;
                double d1 = 0.5 / (double)p_updateIconGrid_2_;
                double d2 = (double)Math.min(textureatlassprite6.getMinU(), textureatlassprite6.getMaxU()) + d0;
                double d3 = (double)Math.min(textureatlassprite6.getMinV(), textureatlassprite6.getMaxV()) + d1;
                double d4 = (double)Math.max(textureatlassprite6.getMinU(), textureatlassprite6.getMaxU()) - d0;
                double d5 = (double)Math.max(textureatlassprite6.getMinV(), textureatlassprite6.getMaxV()) - d1;
                int k3 = (int)(d2 / this.iconGridSizeU);
                int l3 = (int)(d3 / this.iconGridSizeV);
                int i4 = (int)(d4 / this.iconGridSizeU);
                int j4 = (int)(d5 / this.iconGridSizeV);
                for (int k4 = k3; k4 <= i4; ++k4) {
                    if (k4 >= 0 && k4 < this.iconGridCountX) {
                        for (int l4 = l3; l4 <= j4; ++l4) {
                            if (l4 >= 0 && l4 < this.iconGridCountX) {
                                int i5 = l4 * this.iconGridCountX + k4;
                                this.iconGrid[i5] = textureatlassprite6;
                                continue;
                            }
                            Config.warn("Invalid grid V: " + l4 + ", icon: " + textureatlassprite6.getIconName());
                        }
                        continue;
                    }
                    Config.warn("Invalid grid U: " + k4 + ", icon: " + textureatlassprite6.getIconName());
                }
            }
        }
    }

    public TextureAtlasSprite getIconByUV(double p_getIconByUV_1_, double p_getIconByUV_3_) {
        if (this.iconGrid == null) {
            return null;
        }
        int l3 = (int)(p_getIconByUV_3_ / this.iconGridSizeV);
        int k3 = (int)(p_getIconByUV_1_ / this.iconGridSizeU);
        int i4 = l3 * this.iconGridCountX + k3;
        return i4 >= 0 && i4 <= this.iconGrid.length ? this.iconGrid[i4] : null;
    }
}

