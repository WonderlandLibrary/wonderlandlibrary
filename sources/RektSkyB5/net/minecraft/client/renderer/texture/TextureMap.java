/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.IIconCreator;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import optfine.Config;
import optfine.ConnectedTextures;
import optfine.Reflector;
import optfine.TextureUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureMap
extends AbstractTexture
implements ITickableTextureObject {
    private static final Logger logger = LogManager.getLogger();
    public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
    public static final ResourceLocation locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
    private final List listAnimatedSprites = Lists.newArrayList();
    private final Map mapRegisteredSprites = Maps.newHashMap();
    private final Map mapUploadedSprites = Maps.newHashMap();
    private final String basePath;
    private final IIconCreator iconCreator;
    private int mipmapLevels;
    private final TextureAtlasSprite missingImage = new TextureAtlasSprite("missingno");
    private static final String __OBFID = "CL_00001058";

    public TextureMap(String p_i46099_1_) {
        this(p_i46099_1_, null);
    }

    public TextureMap(String p_i46100_1_, IIconCreator iconCreatorIn) {
        this.basePath = p_i46100_1_;
        this.iconCreator = iconCreatorIn;
    }

    private void initMissingImage() {
        int i2 = this.getMinSpriteSize();
        int[] aint = this.getMissingImageData(i2);
        this.missingImage.setIconWidth(i2);
        this.missingImage.setIconHeight(i2);
        int[][] aint1 = new int[this.mipmapLevels + 1][];
        aint1[0] = aint;
        this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][]{aint1}));
        this.missingImage.setIndexInMap(0);
    }

    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        if (this.iconCreator != null) {
            this.loadSprites(resourceManager, this.iconCreator);
        }
    }

    public void loadSprites(IResourceManager resourceManager, IIconCreator p_174943_2_) {
        this.mapRegisteredSprites.clear();
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
                ((TextureAtlasSprite)textureatlassprite).deleteSpriteTexture();
            }
        }
        ConnectedTextures.updateIcons(this);
        int l1 = Minecraft.getGLMaximumTextureSize();
        Stitcher stitcher = new Stitcher(l1, l1, true, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int i2 = Integer.MAX_VALUE;
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, this);
        int j2 = this.getMinSpriteSize();
        int k2 = 1 << this.mipmapLevels;
        for (Map.Entry entry : this.mapRegisteredSprites.entrySet()) {
            TextureAtlasSprite textureatlassprite1 = (TextureAtlasSprite)entry.getValue();
            ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite1.getIconName());
            ResourceLocation resourcelocation1 = this.completeResourceLocation(resourcelocation, 0);
            if (!textureatlassprite1.hasCustomLoader(resourceManager, resourcelocation)) {
                try {
                    TextureMetadataSection texturemetadatasection;
                    IResource iresource = resourceManager.getResource(resourcelocation1);
                    BufferedImage[] abufferedimage = new BufferedImage[1 + this.mipmapLevels];
                    abufferedimage[0] = TextureUtil.readBufferedImage(iresource.getInputStream());
                    if (this.mipmapLevels > 0 && abufferedimage != null) {
                        int l2 = abufferedimage[0].getWidth();
                        abufferedimage[0] = TextureUtils.scaleToPowerOfTwo(abufferedimage[0], j2);
                        int i1 = abufferedimage[0].getWidth();
                        if (!TextureUtils.isPowerOfTwo(l2)) {
                            Config.log("Scaled non power of 2: " + textureatlassprite1.getIconName() + ", " + l2 + " -> " + i1);
                        }
                    }
                    if ((texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture")) != null) {
                        List<Integer> list = texturemetadatasection.getListMipmaps();
                        if (!list.isEmpty()) {
                            int k1 = abufferedimage[0].getWidth();
                            int j1 = abufferedimage[0].getHeight();
                            if (MathHelper.roundUpToPowerOfTwo(k1) != k1 || MathHelper.roundUpToPowerOfTwo(j1) != j1) {
                                throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                            }
                        }
                        for (int j3 : list) {
                            if (j3 <= 0 || j3 >= abufferedimage.length - 1 || abufferedimage[j3] != null) continue;
                            ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation, j3);
                            try {
                                abufferedimage[j3] = TextureUtil.readBufferedImage(resourceManager.getResource(resourcelocation2).getInputStream());
                            }
                            catch (IOException ioexception) {
                                logger.error("Unable to load miplevel {} from: {}", j3, resourcelocation2, ioexception);
                            }
                        }
                    }
                    AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)iresource.getMetadata("animation");
                    textureatlassprite1.loadSprite(abufferedimage, animationmetadatasection);
                }
                catch (RuntimeException runtimeexception) {
                    logger.error("Unable to parse metadata from " + resourcelocation1, (Throwable)runtimeexception);
                    continue;
                }
                catch (IOException ioexception1) {
                    logger.error("Using missing texture, unable to load " + resourcelocation1 + ", " + ioexception1.getClass().getName());
                    continue;
                }
                i2 = Math.min(i2, Math.min(textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight()));
                int k22 = Math.min(Integer.lowestOneBit(textureatlassprite1.getIconWidth()), Integer.lowestOneBit(textureatlassprite1.getIconHeight()));
                if (k22 < k2) {
                    logger.warn("Texture {} with size {}x{} limits mip level from {} to {}", resourcelocation1, textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight(), MathHelper.calculateLogBaseTwo(k2), MathHelper.calculateLogBaseTwo(k22));
                    k2 = k22;
                }
                stitcher.addSprite(textureatlassprite1);
                continue;
            }
            if (textureatlassprite1.load(resourceManager, resourcelocation)) continue;
            i2 = Math.min(i2, Math.min(textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight()));
            stitcher.addSprite(textureatlassprite1);
        }
        int i22 = Math.min(i2, k2);
        int j22 = MathHelper.calculateLogBaseTwo(i22);
        if (j22 < 0) {
            j22 = 0;
        }
        if (j22 < this.mipmapLevels) {
            logger.info("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.basePath, this.mipmapLevels, j22, i22);
            this.mipmapLevels = j22;
        }
        for (Object textureatlassprite20 : this.mapRegisteredSprites.values()) {
            final TextureAtlasSprite textureatlassprite2 = (TextureAtlasSprite)textureatlassprite20;
            try {
                textureatlassprite2.generateMipmaps(this.mipmapLevels);
            }
            catch (Throwable throwable1) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
                crashreportcategory.addCrashSectionCallable("Sprite name", new Callable(){
                    private static final String __OBFID = "CL_00001059";

                    public String call() throws Exception {
                        return textureatlassprite2.getIconName();
                    }
                });
                crashreportcategory.addCrashSectionCallable("Sprite size", new Callable(){
                    private static final String __OBFID = "CL_00001060";

                    public String call() throws Exception {
                        return textureatlassprite2.getIconWidth() + " x " + textureatlassprite2.getIconHeight();
                    }
                });
                crashreportcategory.addCrashSectionCallable("Sprite frames", new Callable(){
                    private static final String __OBFID = "CL_00001061";

                    public String call() throws Exception {
                        return textureatlassprite2.getFrameCount() + " frames";
                    }
                });
                crashreportcategory.addCrashSection("Mipmap levels", this.mipmapLevels);
                throw new ReportedException(crashreport);
            }
        }
        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);
        stitcher.doStitch();
        logger.info("Created: {}x{} {}-atlas", stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), this.basePath);
        TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        HashMap hashmap = Maps.newHashMap(this.mapRegisteredSprites);
        for (Object textureatlassprite30 : stitcher.getStichSlots()) {
            TextureAtlasSprite textureatlassprite3 = (TextureAtlasSprite)textureatlassprite30;
            String s2 = textureatlassprite3.getIconName();
            hashmap.remove(s2);
            this.mapUploadedSprites.put(s2, textureatlassprite3);
            try {
                TextureUtil.uploadTextureMipmap(textureatlassprite3.getFrameTextureData(0), textureatlassprite3.getIconWidth(), textureatlassprite3.getIconHeight(), textureatlassprite3.getOriginX(), textureatlassprite3.getOriginY(), false, false);
            }
            catch (Throwable throwable) {
                CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
                CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Texture being stitched together");
                crashreportcategory1.addCrashSection("Atlas path", this.basePath);
                crashreportcategory1.addCrashSection("Sprite", textureatlassprite3);
                throw new ReportedException(crashreport1);
            }
            if (!textureatlassprite3.hasAnimationMetadata()) continue;
            this.listAnimatedSprites.add(textureatlassprite3);
        }
        for (Object textureatlassprite4 : hashmap.values()) {
            ((TextureAtlasSprite)textureatlassprite4).copyFrom(this.missingImage);
        }
        if (Config.isMultiTexture()) {
            int l2 = stitcher.getCurrentWidth();
            int i3 = stitcher.getCurrentHeight();
            for (Object textureatlassprite50 : stitcher.getStichSlots()) {
                TextureAtlasSprite textureatlassprite5 = (TextureAtlasSprite)textureatlassprite50;
                textureatlassprite5.sheetWidth = l2;
                textureatlassprite5.sheetHeight = i3;
                textureatlassprite5.mipmapLevels = this.mipmapLevels;
                TextureAtlasSprite textureatlassprite6 = textureatlassprite5.spriteSingle;
                if (textureatlassprite6 == null) continue;
                textureatlassprite6.sheetWidth = l2;
                textureatlassprite6.sheetHeight = i3;
                textureatlassprite6.mipmapLevels = this.mipmapLevels;
                textureatlassprite5.bindSpriteTexture();
                boolean flag = false;
                boolean flag1 = true;
                TextureUtil.uploadTextureMipmap(textureatlassprite6.getFrameTextureData(0), textureatlassprite6.getIconWidth(), textureatlassprite6.getIconHeight(), textureatlassprite6.getOriginX(), textureatlassprite6.getOriginY(), flag, flag1);
            }
            Config.getMinecraft().getTextureManager().bindTexture(locationBlocksTexture);
        }
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, this);
        if (Config.equals(System.getProperty("saveTextureMap"), "true")) {
            TextureUtil.saveGlTexture(this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }
    }

    private ResourceLocation completeResourceLocation(ResourceLocation location, int p_147634_2_) {
        return this.isAbsoluteLocation(location) ? (p_147634_2_ == 0 ? new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".png") : new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + "mipmap" + p_147634_2_ + ".png")) : (p_147634_2_ == 0 ? new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", this.basePath, location.getResourcePath(), ".png")) : new ResourceLocation(location.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", this.basePath, location.getResourcePath(), p_147634_2_, ".png")));
    }

    public TextureAtlasSprite getAtlasSprite(String iconName) {
        TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.mapUploadedSprites.get(iconName);
        if (textureatlassprite == null) {
            textureatlassprite = this.missingImage;
        }
        return textureatlassprite;
    }

    public void updateAnimations() {
        TextureUtil.bindTexture(this.getGlTextureId());
        for (Object textureatlassprite0 : this.listAnimatedSprites) {
            TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)textureatlassprite0;
            if (!this.isTerrainAnimationActive(textureatlassprite)) continue;
            textureatlassprite.updateAnimation();
        }
        if (Config.isMultiTexture()) {
            for (Object textureatlassprite10 : this.listAnimatedSprites) {
                TextureAtlasSprite textureatlassprite2;
                TextureAtlasSprite textureatlassprite1 = (TextureAtlasSprite)textureatlassprite10;
                if (!this.isTerrainAnimationActive(textureatlassprite1) || (textureatlassprite2 = textureatlassprite1.spriteSingle) == null) continue;
                textureatlassprite1.bindSpriteTexture();
                textureatlassprite2.updateAnimation();
            }
            TextureUtil.bindTexture(this.getGlTextureId());
        }
    }

    public TextureAtlasSprite registerSprite(ResourceLocation location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null!");
        }
        TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.mapRegisteredSprites.get(location.toString());
        if (textureatlassprite == null && Reflector.ModLoader_getCustomAnimationLogic.exists()) {
            textureatlassprite = (TextureAtlasSprite)Reflector.call(Reflector.ModLoader_getCustomAnimationLogic, location);
        }
        if (textureatlassprite == null) {
            textureatlassprite = TextureAtlasSprite.makeAtlasSprite(location);
            this.mapRegisteredSprites.put(location.toString(), textureatlassprite);
            if (textureatlassprite instanceof TextureAtlasSprite && textureatlassprite.getIndexInMap() < 0) {
                textureatlassprite.setIndexInMap(this.mapRegisteredSprites.size());
            }
        }
        return textureatlassprite;
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

    public TextureAtlasSprite getTextureExtry(String p_getTextureExtry_1_) {
        ResourceLocation resourcelocation = new ResourceLocation(p_getTextureExtry_1_);
        return (TextureAtlasSprite)this.mapRegisteredSprites.get(resourcelocation.toString());
    }

    public boolean setTextureEntry(String p_setTextureEntry_1_, TextureAtlasSprite p_setTextureEntry_2_) {
        if (!this.mapRegisteredSprites.containsKey(p_setTextureEntry_1_)) {
            this.mapRegisteredSprites.put(p_setTextureEntry_1_, p_setTextureEntry_2_);
            if (p_setTextureEntry_2_.getIndexInMap() < 0) {
                p_setTextureEntry_2_.setIndexInMap(this.mapRegisteredSprites.size());
            }
            return true;
        }
        return false;
    }

    private boolean isAbsoluteLocation(ResourceLocation p_isAbsoluteLocation_1_) {
        String s2 = p_isAbsoluteLocation_1_.getResourcePath();
        return this.isAbsoluteLocationPath(s2);
    }

    private boolean isAbsoluteLocationPath(String p_isAbsoluteLocationPath_1_) {
        String s2 = p_isAbsoluteLocationPath_1_.toLowerCase();
        return s2.startsWith("mcpatcher/") || s2.startsWith("optifine/");
    }

    public TextureAtlasSprite getSpriteSafe(String p_getSpriteSafe_1_) {
        ResourceLocation resourcelocation = new ResourceLocation(p_getSpriteSafe_1_);
        return (TextureAtlasSprite)this.mapRegisteredSprites.get(resourcelocation.toString());
    }

    private boolean isTerrainAnimationActive(TextureAtlasSprite p_isTerrainAnimationActive_1_) {
        return p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterFlow ? (p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaFlow ? (p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer0 && p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer1 ? (p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal ? Config.isAnimatedPortal() : Config.isAnimatedTerrain()) : Config.isAnimatedFire()) : Config.isAnimatedLava()) : Config.isAnimatedWater();
    }

    public int getCountRegisteredSprites() {
        return this.mapRegisteredSprites.size();
    }

    private int detectMaxMipmapLevel(Map p_detectMaxMipmapLevel_1_, IResourceManager p_detectMaxMipmapLevel_2_) {
        int j2;
        int i2 = this.detectMinimumSpriteSize(p_detectMaxMipmapLevel_1_, p_detectMaxMipmapLevel_2_, 20);
        if (i2 < 16) {
            i2 = 16;
        }
        if ((i2 = MathHelper.roundUpToPowerOfTwo(i2)) > 16) {
            Config.log("Sprite size: " + i2);
        }
        if ((j2 = MathHelper.calculateLogBaseTwo(i2)) < 4) {
            j2 = 4;
        }
        return j2;
    }

    private int detectMinimumSpriteSize(Map p_detectMinimumSpriteSize_1_, IResourceManager p_detectMinimumSpriteSize_2_, int p_detectMinimumSpriteSize_3_) {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (Map.Entry entry : p_detectMinimumSpriteSize_1_.entrySet()) {
            TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)entry.getValue();
            ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite.getIconName());
            ResourceLocation resourcelocation1 = this.completeResourceLocation(resourcelocation, 0);
            if (textureatlassprite.hasCustomLoader(p_detectMinimumSpriteSize_2_, resourcelocation)) continue;
            try {
                Dimension dimension;
                InputStream inputstream;
                IResource iresource = p_detectMinimumSpriteSize_2_.getResource(resourcelocation1);
                if (iresource == null || (inputstream = iresource.getInputStream()) == null || (dimension = TextureUtils.getImageSize(inputstream, "png")) == null) continue;
                int i2 = dimension.width;
                int j2 = MathHelper.roundUpToPowerOfTwo(i2);
                if (!map.containsKey(j2)) {
                    map.put(j2, 1);
                    continue;
                }
                int k2 = (Integer)map.get(j2);
                map.put(j2, k2 + 1);
            }
            catch (Exception iresource) {}
        }
        int l2 = 0;
        Set set = map.keySet();
        TreeSet set1 = new TreeSet(set);
        Iterator iterator = set1.iterator();
        while (iterator.hasNext()) {
            int j1 = (Integer)iterator.next();
            int l1 = (Integer)map.get(j1);
            l2 += l1;
        }
        int i1 = 16;
        int k1 = 0;
        int l1 = l2 * p_detectMinimumSpriteSize_3_ / 100;
        Iterator iterator1 = set1.iterator();
        while (iterator1.hasNext()) {
            int i2 = (Integer)iterator1.next();
            int j2 = (Integer)map.get(i2);
            k1 += j2;
            if (i2 > i1) {
                i1 = i2;
            }
            if (k1 <= l1) continue;
            return i1;
        }
        return i1;
    }

    private int getMinSpriteSize() {
        int i2 = 1 << this.mipmapLevels;
        if (i2 < 16) {
            i2 = 16;
        }
        return i2;
    }

    private int[] getMissingImageData(int p_getMissingImageData_1_) {
        BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
        bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.missingTextureData, 0, 16);
        BufferedImage bufferedimage1 = TextureUtils.scaleToPowerOfTwo(bufferedimage, p_getMissingImageData_1_);
        int[] aint = new int[p_getMissingImageData_1_ * p_getMissingImageData_1_];
        bufferedimage1.getRGB(0, 0, p_getMissingImageData_1_, p_getMissingImageData_1_, aint, 0, p_getMissingImageData_1_);
        return aint;
    }
}

