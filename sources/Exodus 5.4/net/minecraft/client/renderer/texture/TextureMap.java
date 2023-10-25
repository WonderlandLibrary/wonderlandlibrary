/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureMap
extends AbstractTexture
implements ITickableTextureObject {
    private static final Logger logger = LogManager.getLogger();
    public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
    private int mipmapLevels;
    private final Map<String, TextureAtlasSprite> mapRegisteredSprites;
    private final TextureAtlasSprite missingImage;
    private final IIconCreator iconCreator;
    private final String basePath;
    private final Map<String, TextureAtlasSprite> mapUploadedSprites;
    private final List<TextureAtlasSprite> listAnimatedSprites = Lists.newArrayList();
    public static final ResourceLocation locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");

    public void updateAnimations() {
        TextureUtil.bindTexture(this.getGlTextureId());
        for (TextureAtlasSprite textureAtlasSprite : this.listAnimatedSprites) {
            textureAtlasSprite.updateAnimation();
        }
    }

    public TextureAtlasSprite registerSprite(ResourceLocation resourceLocation) {
        if (resourceLocation == null) {
            throw new IllegalArgumentException("Location cannot be null!");
        }
        TextureAtlasSprite textureAtlasSprite = this.mapRegisteredSprites.get(resourceLocation);
        if (textureAtlasSprite == null) {
            textureAtlasSprite = TextureAtlasSprite.makeAtlasSprite(resourceLocation);
            this.mapRegisteredSprites.put(resourceLocation.toString(), textureAtlasSprite);
        }
        return textureAtlasSprite;
    }

    private ResourceLocation completeResourceLocation(ResourceLocation resourceLocation, int n) {
        return n == 0 ? new ResourceLocation(resourceLocation.getResourceDomain(), String.format("%s/%s%s", this.basePath, resourceLocation.getResourcePath(), ".png")) : new ResourceLocation(resourceLocation.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", this.basePath, resourceLocation.getResourcePath(), n, ".png"));
    }

    @Override
    public void loadTexture(IResourceManager iResourceManager) throws IOException {
        if (this.iconCreator != null) {
            this.loadSprites(iResourceManager, this.iconCreator);
        }
    }

    public void loadSprites(IResourceManager iResourceManager, IIconCreator iIconCreator) {
        this.mapRegisteredSprites.clear();
        iIconCreator.registerSprites(this);
        this.initMissingImage();
        this.deleteGlTexture();
        this.loadTextureAtlas(iResourceManager);
    }

    public TextureMap(String string, IIconCreator iIconCreator) {
        this.mapRegisteredSprites = Maps.newHashMap();
        this.mapUploadedSprites = Maps.newHashMap();
        this.missingImage = new TextureAtlasSprite("missingno");
        this.basePath = string;
        this.iconCreator = iIconCreator;
    }

    public TextureAtlasSprite getAtlasSprite(String string) {
        TextureAtlasSprite textureAtlasSprite = this.mapUploadedSprites.get(string);
        if (textureAtlasSprite == null) {
            textureAtlasSprite = this.missingImage;
        }
        return textureAtlasSprite;
    }

    public TextureAtlasSprite getMissingSprite() {
        return this.missingImage;
    }

    public TextureMap(String string) {
        this(string, null);
    }

    public void loadTextureAtlas(IResourceManager iResourceManager) {
        List<Integer> list;
        Object object;
        Object object2;
        int n = Minecraft.getGLMaximumTextureSize();
        Stitcher stitcher = new Stitcher(n, n, true, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int n2 = Integer.MAX_VALUE;
        int n3 = 1 << this.mipmapLevels;
        for (Map.Entry<String, TextureAtlasSprite> entry : this.mapRegisteredSprites.entrySet()) {
            TextureAtlasSprite object32 = entry.getValue();
            ResourceLocation resourceLocation = new ResourceLocation(object32.getIconName());
            Iterator iterator = this.completeResourceLocation(resourceLocation, 0);
            try {
                IResource iResource = iResourceManager.getResource((ResourceLocation)((Object)iterator));
                object2 = new BufferedImage[1 + this.mipmapLevels];
                object2[0] = TextureUtil.readBufferedImage(iResource.getInputStream());
                object = (TextureMetadataSection)iResource.getMetadata("texture");
                if (object != null) {
                    int n4;
                    list = ((TextureMetadataSection)object).getListMipmaps();
                    if (!list.isEmpty()) {
                        int n5 = ((BufferedImage)object2[0]).getWidth();
                        n4 = ((BufferedImage)object2[0]).getHeight();
                        if (MathHelper.roundUpToPowerOfTwo(n5) != n5 || MathHelper.roundUpToPowerOfTwo(n4) != n4) {
                            throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                        }
                    }
                    Iterator iterator2 = list.iterator();
                    while (iterator2.hasNext()) {
                        n4 = (Integer)iterator2.next();
                        if (n4 <= 0 || n4 >= ((Object)object2).length - 1 || object2[n4] != null) continue;
                        ResourceLocation resourceLocation2 = this.completeResourceLocation(resourceLocation, n4);
                        try {
                            object2[n4] = TextureUtil.readBufferedImage(iResourceManager.getResource(resourceLocation2).getInputStream());
                        }
                        catch (IOException iOException) {
                            logger.error("Unable to load miplevel {} from: {}", new Object[]{n4, resourceLocation2, iOException});
                        }
                    }
                }
                list = (AnimationMetadataSection)iResource.getMetadata("animation");
                object32.loadSprite((BufferedImage[])object2, (AnimationMetadataSection)((Object)list));
            }
            catch (RuntimeException runtimeException) {
                logger.error("Unable to parse metadata from " + iterator, (Throwable)runtimeException);
                continue;
            }
            catch (IOException iOException) {
                logger.error("Using missing texture, unable to load " + iterator, (Throwable)iOException);
                continue;
            }
            n2 = Math.min(n2, Math.min(object32.getIconWidth(), object32.getIconHeight()));
            int n6 = Math.min(Integer.lowestOneBit(object32.getIconWidth()), Integer.lowestOneBit(object32.getIconHeight()));
            if (n6 < n3) {
                logger.warn("Texture {} with size {}x{} limits mip level from {} to {}", new Object[]{iterator, object32.getIconWidth(), object32.getIconHeight(), MathHelper.calculateLogBaseTwo(n3), MathHelper.calculateLogBaseTwo(n6)});
                n3 = n6;
            }
            stitcher.addSprite(object32);
        }
        int n7 = Math.min(n2, n3);
        int n8 = MathHelper.calculateLogBaseTwo(n7);
        if (n8 < this.mipmapLevels) {
            logger.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", new Object[]{this.basePath, this.mipmapLevels, n8, n7});
            this.mipmapLevels = n8;
        }
        for (final TextureAtlasSprite textureAtlasSprite : this.mapRegisteredSprites.values()) {
            try {
                textureAtlasSprite.generateMipmaps(this.mipmapLevels);
            }
            catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Applying mipmap");
                object2 = crashReport.makeCategory("Sprite being mipmapped");
                ((CrashReportCategory)object2).addCrashSectionCallable("Sprite name", new Callable<String>(){

                    @Override
                    public String call() throws Exception {
                        return textureAtlasSprite.getIconName();
                    }
                });
                ((CrashReportCategory)object2).addCrashSectionCallable("Sprite size", new Callable<String>(){

                    @Override
                    public String call() throws Exception {
                        return String.valueOf(textureAtlasSprite.getIconWidth()) + " x " + textureAtlasSprite.getIconHeight();
                    }
                });
                ((CrashReportCategory)object2).addCrashSectionCallable("Sprite frames", new Callable<String>(){

                    @Override
                    public String call() throws Exception {
                        return String.valueOf(textureAtlasSprite.getFrameCount()) + " frames";
                    }
                });
                ((CrashReportCategory)object2).addCrashSection("Mipmap levels", this.mipmapLevels);
                throw new ReportedException(crashReport);
            }
        }
        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);
        stitcher.doStitch();
        logger.info("Created: {}x{} {}-atlas", new Object[]{stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), this.basePath});
        TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        HashMap hashMap = Maps.newHashMap(this.mapRegisteredSprites);
        for (TextureAtlasSprite textureAtlasSprite : stitcher.getStichSlots()) {
            String string = textureAtlasSprite.getIconName();
            hashMap.remove(string);
            this.mapUploadedSprites.put(string, textureAtlasSprite);
            try {
                TextureUtil.uploadTextureMipmap(textureAtlasSprite.getFrameTextureData(0), textureAtlasSprite.getIconWidth(), textureAtlasSprite.getIconHeight(), textureAtlasSprite.getOriginX(), textureAtlasSprite.getOriginY(), false, false);
            }
            catch (Throwable throwable) {
                object = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
                list = ((CrashReport)object).makeCategory("Texture being stitched together");
                ((CrashReportCategory)((Object)list)).addCrashSection("Atlas path", this.basePath);
                ((CrashReportCategory)((Object)list)).addCrashSection("Sprite", textureAtlasSprite);
                throw new ReportedException((CrashReport)object);
            }
            if (!textureAtlasSprite.hasAnimationMetadata()) continue;
            this.listAnimatedSprites.add(textureAtlasSprite);
        }
        for (TextureAtlasSprite textureAtlasSprite : hashMap.values()) {
            textureAtlasSprite.copyFrom(this.missingImage);
        }
    }

    @Override
    public void tick() {
        this.updateAnimations();
    }

    private void initMissingImage() {
        int[] nArray = TextureUtil.missingTextureData;
        this.missingImage.setIconWidth(16);
        this.missingImage.setIconHeight(16);
        int[][] nArrayArray = new int[this.mipmapLevels + 1][];
        nArrayArray[0] = nArray;
        this.missingImage.setFramesTextureData(Lists.newArrayList((Object[])new int[][][]{nArrayArray}));
    }

    public void setMipmapLevels(int n) {
        this.mipmapLevels = n;
    }
}

