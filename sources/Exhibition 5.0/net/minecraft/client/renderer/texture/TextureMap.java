// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import net.minecraft.optifine.Config;
import net.minecraft.optifine.TextureUtils;
import java.util.HashMap;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.client.resources.IResource;
import java.util.Iterator;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.MathHelper;
import net.minecraft.client.resources.data.TextureMetadataSection;
import java.awt.image.BufferedImage;
import net.minecraft.optifine.Reflector;
import net.minecraft.client.Minecraft;
import net.minecraft.optifine.ConnectedTextures;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
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
    
    public TextureMap(final String p_i46099_1_) {
        this(p_i46099_1_, null);
    }
    
    public TextureMap(final String p_i46100_1_, final IIconCreator p_i46100_2_) {
        this.listAnimatedSprites = Lists.newArrayList();
        this.mapRegisteredSprites = Maps.newHashMap();
        this.mapUploadedSprites = Maps.newHashMap();
        this.missingImage = new TextureAtlasSprite("missingno");
        this.basePath = p_i46100_1_;
        this.field_174946_m = p_i46100_2_;
    }
    
    private void initMissingImage() {
        final int[] var1 = TextureUtil.missingTextureData;
        this.missingImage.setIconWidth(16);
        this.missingImage.setIconHeight(16);
        final int[][] var2 = new int[this.mipmapLevels + 1][];
        var2[0] = var1;
        this.missingImage.setFramesTextureData(Lists.newArrayList((Object[])new int[][][] { var2 }));
        this.missingImage.setIndexInMap(0);
    }
    
    @Override
    public void loadTexture(final IResourceManager p_110551_1_) throws IOException {
        if (this.field_174946_m != null) {
            this.func_174943_a(p_110551_1_, this.field_174946_m);
        }
    }
    
    public void func_174943_a(final IResourceManager p_174943_1_, final IIconCreator p_174943_2_) {
        this.mapRegisteredSprites.clear();
        p_174943_2_.func_177059_a(this);
        this.initMissingImage();
        this.deleteGlTexture();
        ConnectedTextures.updateIcons(this);
        this.loadTextureAtlas(p_174943_1_);
    }
    
    public void loadTextureAtlas(final IResourceManager p_110571_1_) {
        final int var2 = Minecraft.getGLMaximumTextureSize();
        final Stitcher var3 = new Stitcher(var2, var2, true, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int var4 = Integer.MAX_VALUE;
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, this);
        int var5 = 1 << this.mipmapLevels;
        for (final Map.Entry var7 : this.mapRegisteredSprites.entrySet()) {
            final TextureAtlasSprite var8 = var7.getValue();
            final ResourceLocation var9 = new ResourceLocation(var8.getIconName());
            final ResourceLocation var10 = this.completeResourceLocation(var9, 0);
            if (!var8.hasCustomLoader(p_110571_1_, var9)) {
                try {
                    final IResource var11 = p_110571_1_.getResource(var10);
                    final BufferedImage[] var12 = new BufferedImage[1 + this.mipmapLevels];
                    var12[0] = TextureUtil.func_177053_a(var11.getInputStream());
                    final TextureMetadataSection var13 = (TextureMetadataSection)var11.getMetadata("texture");
                    if (var13 != null) {
                        final List var14 = var13.getListMipmaps();
                        if (!var14.isEmpty()) {
                            final int var15 = var12[0].getWidth();
                            final int var16 = var12[0].getHeight();
                            if (MathHelper.roundUpToPowerOfTwo(var15) != var15 || MathHelper.roundUpToPowerOfTwo(var16) != var16) {
                                throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                            }
                        }
                        final Iterator var17 = var14.iterator();
                        while (var17.hasNext()) {
                            final int var16 = var17.next();
                            if (var16 > 0 && var16 < var12.length - 1 && var12[var16] == null) {
                                final ResourceLocation var18 = this.completeResourceLocation(var9, var16);
                                try {
                                    var12[var16] = TextureUtil.func_177053_a(p_110571_1_.getResource(var18).getInputStream());
                                }
                                catch (IOException var19) {
                                    TextureMap.logger.error("Unable to load miplevel {} from: {}", new Object[] { var16, var18, var19 });
                                }
                            }
                        }
                    }
                    final AnimationMetadataSection var20 = (AnimationMetadataSection)var11.getMetadata("animation");
                    var8.func_180598_a(var12, var20);
                }
                catch (RuntimeException var21) {
                    TextureMap.logger.error("Unable to parse metadata from " + var10, (Throwable)var21);
                    continue;
                }
                catch (IOException var22) {
                    TextureMap.logger.error("Using missing texture, unable to load " + var10 + ", " + var22.getClass().getName());
                    continue;
                }
                var4 = Math.min(var4, Math.min(var8.getIconWidth(), var8.getIconHeight()));
                final int var23 = Math.min(Integer.lowestOneBit(var8.getIconWidth()), Integer.lowestOneBit(var8.getIconHeight()));
                if (var23 < var5) {
                    TextureMap.logger.warn("Texture {} with size {}x{} limits mip level from {} to {}", new Object[] { var10, var8.getIconWidth(), var8.getIconHeight(), MathHelper.calculateLogBaseTwo(var5), MathHelper.calculateLogBaseTwo(var23) });
                    var5 = var23;
                }
                var3.addSprite(var8);
            }
            else {
                if (var8.load(p_110571_1_, var9)) {
                    continue;
                }
                var4 = Math.min(var4, Math.min(var8.getIconWidth(), var8.getIconHeight()));
                var3.addSprite(var8);
            }
        }
        final int var24 = Math.min(var4, var5);
        int var25 = MathHelper.calculateLogBaseTwo(var24);
        if (var25 < 0) {
            var25 = 0;
        }
        if (var25 < this.mipmapLevels) {
            TextureMap.logger.info("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", new Object[] { this.basePath, this.mipmapLevels, var25, var24 });
            this.mipmapLevels = var25;
        }
        for (final TextureAtlasSprite var27 : this.mapRegisteredSprites.values()) {
            try {
                var27.generateMipmaps(this.mipmapLevels);
            }
            catch (Throwable var29) {
                final CrashReport var28 = CrashReport.makeCrashReport(var29, "Applying mipmap");
                final CrashReportCategory var30 = var28.makeCategory("Sprite being mipmapped");
                var30.addCrashSectionCallable("Sprite name", new Callable() {
                    private static final String __OBFID = "CL_00001059";
                    
                    @Override
                    public String call() {
                        return var27.getIconName();
                    }
                });
                var30.addCrashSectionCallable("Sprite size", new Callable() {
                    private static final String __OBFID = "CL_00001060";
                    
                    @Override
                    public String call() {
                        return var27.getIconWidth() + " x " + var27.getIconHeight();
                    }
                });
                var30.addCrashSectionCallable("Sprite frames", new Callable() {
                    private static final String __OBFID = "CL_00001061";
                    
                    @Override
                    public String call() {
                        return var27.getFrameCount() + " frames";
                    }
                });
                var30.addCrashSection("Mipmap levels", this.mipmapLevels);
                throw new ReportedException(var28);
            }
        }
        this.missingImage.generateMipmaps(this.mipmapLevels);
        var3.addSprite(this.missingImage);
        try {
            var3.doStitch();
        }
        catch (StitcherException var31) {
            throw var31;
        }
        TextureMap.logger.info("Created: {}x{} {}-atlas", new Object[] { var3.getCurrentWidth(), var3.getCurrentHeight(), this.basePath });
        TextureUtil.func_180600_a(this.getGlTextureId(), this.mipmapLevels, var3.getCurrentWidth(), var3.getCurrentHeight());
        final HashMap var32 = Maps.newHashMap(this.mapRegisteredSprites);
        for (final TextureAtlasSprite var34 : var3.getStichSlots()) {
            final String var35 = var34.getIconName();
            var32.remove(var35);
            this.mapUploadedSprites.put(var35, var34);
            try {
                TextureUtil.uploadTextureMipmap(var34.getFrameTextureData(0), var34.getIconWidth(), var34.getIconHeight(), var34.getOriginX(), var34.getOriginY(), false, false);
            }
            catch (Throwable var37) {
                final CrashReport var36 = CrashReport.makeCrashReport(var37, "Stitching texture atlas");
                final CrashReportCategory var38 = var36.makeCategory("Texture being stitched together");
                var38.addCrashSection("Atlas path", this.basePath);
                var38.addCrashSection("Sprite", var34);
                throw new ReportedException(var36);
            }
            if (var34.hasAnimationMetadata()) {
                this.listAnimatedSprites.add(var34);
            }
        }
        for (final TextureAtlasSprite var34 : var32.values()) {
            var34.copyFrom(this.missingImage);
        }
        TextureUtil.func_177055_a(this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, var3.getCurrentWidth(), var3.getCurrentHeight());
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, this);
    }
    
    private ResourceLocation completeResourceLocation(final ResourceLocation p_147634_1_, final int p_147634_2_) {
        return this.isAbsoluteLocation(p_147634_1_) ? ((p_147634_2_ == 0) ? new ResourceLocation(p_147634_1_.getResourceDomain(), p_147634_1_.getResourcePath() + ".png") : new ResourceLocation(p_147634_1_.getResourceDomain(), p_147634_1_.getResourcePath() + "mipmap" + p_147634_2_ + ".png")) : ((p_147634_2_ == 0) ? new ResourceLocation(p_147634_1_.getResourceDomain(), String.format("%s/%s%s", this.basePath, p_147634_1_.getResourcePath(), ".png")) : new ResourceLocation(p_147634_1_.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", this.basePath, p_147634_1_.getResourcePath(), p_147634_2_, ".png")));
    }
    
    public TextureAtlasSprite getAtlasSprite(final String p_110572_1_) {
        TextureAtlasSprite var2 = this.mapUploadedSprites.get(p_110572_1_);
        if (var2 == null) {
            var2 = this.missingImage;
        }
        return var2;
    }
    
    public void updateAnimations() {
        TextureUtil.bindTexture(this.getGlTextureId());
        for (final TextureAtlasSprite var2 : this.listAnimatedSprites) {
            if (this.isTerrainAnimationActive(var2)) {
                var2.updateAnimation();
            }
        }
    }
    
    public TextureAtlasSprite func_174942_a(final ResourceLocation p_174942_1_) {
        if (p_174942_1_ == null) {
            throw new IllegalArgumentException("Location cannot be null!");
        }
        TextureAtlasSprite var2 = this.mapRegisteredSprites.get(p_174942_1_.toString());
        if (var2 == null && Reflector.ModLoader_getCustomAnimationLogic.exists()) {
            var2 = (TextureAtlasSprite)Reflector.call(Reflector.ModLoader_getCustomAnimationLogic, p_174942_1_);
        }
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
        return (ts != TextureUtils.iconWaterStill && ts != TextureUtils.iconWaterFlow) ? ((ts != TextureUtils.iconLavaStill && ts != TextureUtils.iconLavaFlow) ? ((ts != TextureUtils.iconFireLayer0 && ts != TextureUtils.iconFireLayer1) ? ((ts == TextureUtils.iconPortal) ? Config.isAnimatedPortal() : Config.isAnimatedTerrain()) : Config.isAnimatedFire()) : Config.isAnimatedLava()) : Config.isAnimatedWater();
    }
    
    public int getCountRegisteredSprites() {
        return this.mapRegisteredSprites.size();
    }
    
    static {
        logger = LogManager.getLogger();
        field_174945_f = new ResourceLocation("missingno");
        locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
    }
}
