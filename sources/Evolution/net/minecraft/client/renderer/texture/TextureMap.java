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
import java.util.Map.Entry;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.src.Config;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.optifine.BetterGrass;
import net.optifine.ConnectedTextures;
import net.optifine.CustomItems;
import net.optifine.EmissiveTextures;
import net.optifine.SmartAnimations;
import net.optifine.SpriteDependencies;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.shaders.ShadersTex;
import net.optifine.util.CounterInt;
import net.optifine.util.TextureUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureMap extends AbstractTexture implements ITickableTextureObject
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
    public static final ResourceLocation LOCATION_BLOCKS_TEXTURE = new ResourceLocation("textures/atlas/blocks.png");
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

    public TextureMap(String basePathIn)
    {
        this(basePathIn, (ITextureMapPopulator)null);
    }

    public TextureMap(String p_i8_1_, boolean p_i8_2_)
    {
        this(p_i8_1_, (ITextureMapPopulator)null, p_i8_2_);
    }

    public TextureMap(String basePathIn, @Nullable ITextureMapPopulator iconCreatorIn)
    {
        this(basePathIn, iconCreatorIn, false);
    }

    public TextureMap(String p_i9_1_, ITextureMapPopulator p_i9_2_, boolean p_i9_3_)
    {
        this.iconGrid = null;
        this.iconGridSize = -1;
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGridSizeU = -1.0D;
        this.iconGridSizeV = -1.0D;
        this.counterIndexInMap = new CounterInt(0);
        this.atlasWidth = 0;
        this.atlasHeight = 0;
        this.listAnimatedSprites = Lists.<TextureAtlasSprite>newArrayList();
        this.mapRegisteredSprites = Maps.<String, TextureAtlasSprite>newHashMap();
        this.mapUploadedSprites = Maps.<String, TextureAtlasSprite>newHashMap();
        this.missingImage = new TextureAtlasSprite("missingno");
        this.basePath = p_i9_1_;
        this.iconCreator = p_i9_2_;
    }

    private void initMissingImage()
    {
        int i = this.getMinSpriteSize();
        int[] aint = this.getMissingImageData(i);
        this.missingImage.setIconWidth(i);
        this.missingImage.setIconHeight(i);
        int[][] aint1 = new int[this.mipmapLevels + 1][];
        aint1[0] = aint;
        this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][] {aint1}));
        this.missingImage.setIndexInMap(this.counterIndexInMap.nextValue());
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException
    {
        if (this.iconCreator != null)
        {
            this.loadSprites(resourceManager, this.iconCreator);
        }
    }

    public void loadSprites(IResourceManager resourceManager, ITextureMapPopulator iconCreatorIn)
    {
        this.mapRegisteredSprites.clear();
        this.counterIndexInMap.reset();
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, this);
        iconCreatorIn.registerSprites(this);

        if (this.mipmapLevels >= 4)
        {
            this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, resourceManager);
            Config.log("Mipmap levels: " + this.mipmapLevels);
        }

        this.initMissingImage();
        this.deleteGlTexture();
        this.loadTextureAtlas(resourceManager);
    }

    public void loadTextureAtlas(IResourceManager resourceManager)
    {
        Config.dbg("Multitexture: " + Config.isMultiTexture());

        if (Config.isMultiTexture())
        {
            for (TextureAtlasSprite textureatlassprite : this.mapUploadedSprites.values())
            {
                textureatlassprite.deleteSpriteTexture();
            }
        }

        ConnectedTextures.updateIcons(this);
        CustomItems.updateIcons(this);
        BetterGrass.updateIcons(this);
        int i2 = TextureUtils.getGLMaximumTextureSize();
        Stitcher stitcher = new Stitcher(i2, i2, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int i = Integer.MAX_VALUE;
        int j = this.getMinSpriteSize();
        this.iconGridSize = j;
        int k = 1 << this.mipmapLevels;
        int l = 0;
        int i1 = 0;
        SpriteDependencies.reset();
        List<TextureAtlasSprite> list = new ArrayList<TextureAtlasSprite>(this.mapRegisteredSprites.values());

        for (int j1 = 0; j1 < list.size(); ++j1)
        {
            TextureAtlasSprite textureatlassprite1 = SpriteDependencies.resolveDependencies(list, j1, this);
            ResourceLocation resourcelocation = this.getResourceLocation(textureatlassprite1);
            IResource iresource = null;
            textureatlassprite1.updateIndexInMap(this.counterIndexInMap);

            if (textureatlassprite1.hasCustomLoader(resourceManager, resourcelocation))
            {
                if (textureatlassprite1.load(resourceManager, resourcelocation, (p_lambda$loadTextureAtlas$0_1_) ->
            {
                return this.mapRegisteredSprites.get(p_lambda$loadTextureAtlas$0_1_.toString());
                }))
                {
                    Config.detail("Custom loader (skipped): " + textureatlassprite1);
                    ++i1;
                    continue;
                }
                Config.detail("Custom loader: " + textureatlassprite1);
                ++l;
            }
            else
            {
                try
                {
                    PngSizeInfo pngsizeinfo = PngSizeInfo.makeFromResource(resourceManager.getResource(resourcelocation));
                    iresource = resourceManager.getResource(resourcelocation);
                    boolean flag = iresource.getMetadata("animation") != null;
                    textureatlassprite1.loadSprite(pngsizeinfo, flag);
                }
                catch (RuntimeException runtimeexception)
                {
                    LOGGER.error("Unable to parse metadata from {}", resourcelocation, runtimeexception);
                    ReflectorForge.FMLClientHandler_trackBrokenTexture(resourcelocation, runtimeexception.getMessage());
                    continue;
                }
                catch (IOException ioexception)
                {
                    LOGGER.error("Using missing texture, unable to load " + resourcelocation + ", " + ioexception.getClass().getName());
                    ReflectorForge.FMLClientHandler_trackMissingTexture(resourcelocation);
                    continue;
                }
                finally
                {
                    IOUtils.closeQuietly((Closeable)iresource);
                }
            }

            int i3 = textureatlassprite1.getIconWidth();
            int k3 = textureatlassprite1.getIconHeight();

            if (i3 >= 1 && k3 >= 1)
            {
                if (i3 < j || this.mipmapLevels > 0)
                {
                    int k1 = this.mipmapLevels > 0 ? TextureUtils.scaleToGrid(i3, j) : TextureUtils.scaleToMin(i3, j);

                    if (k1 != i3)
                    {
                        if (!TextureUtils.isPowerOfTwo(i3))
                        {
                            Config.log("Scaled non power of 2: " + textureatlassprite1.getIconName() + ", " + i3 + " -> " + k1);
                        }
                        else
                        {
                            Config.log("Scaled too small texture: " + textureatlassprite1.getIconName() + ", " + i3 + " -> " + k1);
                        }

                        int l1 = k3 * k1 / i3;
                        textureatlassprite1.setIconWidth(k1);
                        textureatlassprite1.setIconHeight(l1);
                    }
                }

                i = Math.min(i, Math.min(textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight()));
                int l3 = Math.min(Integer.lowestOneBit(textureatlassprite1.getIconWidth()), Integer.lowestOneBit(textureatlassprite1.getIconHeight()));

                if (l3 < k)
                {
                    LOGGER.warn("Texture {} with size {}x{} limits mip level from {} to {}", resourcelocation, Integer.valueOf(textureatlassprite1.getIconWidth()), Integer.valueOf(textureatlassprite1.getIconHeight()), Integer.valueOf(MathHelper.log2(k)), Integer.valueOf(MathHelper.log2(l3)));
                    k = l3;
                }

                if (this.generateMipmaps(resourceManager, textureatlassprite1))
                {
                    stitcher.addSprite(textureatlassprite1);
                }
            }
            else
            {
                Config.warn("Invalid sprite size: " + textureatlassprite1);
            }
        }

        if (l > 0)
        {
            Config.dbg("Custom loader sprites: " + l);
        }

        if (i1 > 0)
        {
            Config.dbg("Custom loader sprites (skipped): " + i1);
        }

        if (SpriteDependencies.getCountDependencies() > 0)
        {
            Config.dbg("Sprite dependencies: " + SpriteDependencies.getCountDependencies());
        }

        int j2 = Math.min(i, k);
        int k2 = MathHelper.log2(j2);

        if (k2 < 0)
        {
            k2 = 0;
        }

        if (k2 < this.mipmapLevels)
        {
            LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.basePath, Integer.valueOf(this.mipmapLevels), Integer.valueOf(k2), Integer.valueOf(j2));
            this.mipmapLevels = k2;
        }

        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);

        try
        {
            stitcher.doStitch();
        }
        catch (StitcherException stitcherexception)
        {
            throw stitcherexception;
        }

        LOGGER.info("Created: {}x{} {}-atlas", Integer.valueOf(stitcher.getCurrentWidth()), Integer.valueOf(stitcher.getCurrentHeight()), this.basePath);

        if (Config.isShaders())
        {
            ShadersTex.allocateTextureMap(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), stitcher, this);
        }
        else
        {
            TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }

        Map<String, TextureAtlasSprite> map = Maps.<String, TextureAtlasSprite>newHashMap(this.mapRegisteredSprites);

        for (TextureAtlasSprite textureatlassprite2 : stitcher.getStichSlots())
        {
            String s = textureatlassprite2.getIconName();
            map.remove(s);
            this.mapUploadedSprites.put(s, textureatlassprite2);

            try
            {
                if (Config.isShaders())
                {
                    ShadersTex.uploadTexSubForLoadAtlas(this, textureatlassprite2.getIconName(), textureatlassprite2.getFrameTextureData(0), textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight(), textureatlassprite2.getOriginX(), textureatlassprite2.getOriginY(), false, false);
                }
                else
                {
                    TextureUtil.uploadTextureMipmap(textureatlassprite2.getFrameTextureData(0), textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight(), textureatlassprite2.getOriginX(), textureatlassprite2.getOriginY(), false, false);
                }
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Texture being stitched together");
                crashreportcategory.addCrashSection("Atlas path", this.basePath);
                crashreportcategory.addCrashSection("Sprite", textureatlassprite2);
                throw new ReportedException(crashreport);
            }

            if (textureatlassprite2.hasAnimationMetadata())
            {
                textureatlassprite2.setAnimationIndex(this.listAnimatedSprites.size());
                this.listAnimatedSprites.add(textureatlassprite2);
            }
        }

        for (TextureAtlasSprite textureatlassprite3 : map.values())
        {
            textureatlassprite3.copyFrom(this.missingImage);
        }

        Config.log("Animated sprites: " + this.listAnimatedSprites.size());

        if (Config.isMultiTexture())
        {
            int l2 = stitcher.getCurrentWidth();
            int j3 = stitcher.getCurrentHeight();

            for (TextureAtlasSprite textureatlassprite4 : stitcher.getStichSlots())
            {
                textureatlassprite4.sheetWidth = l2;
                textureatlassprite4.sheetHeight = j3;
                textureatlassprite4.mipmapLevels = this.mipmapLevels;
                TextureAtlasSprite textureatlassprite5 = textureatlassprite4.spriteSingle;

                if (textureatlassprite5 != null)
                {
                    if (textureatlassprite5.getIconWidth() <= 0)
                    {
                        textureatlassprite5.setIconWidth(textureatlassprite4.getIconWidth());
                        textureatlassprite5.setIconHeight(textureatlassprite4.getIconHeight());
                        textureatlassprite5.initSprite(textureatlassprite4.getIconWidth(), textureatlassprite4.getIconHeight(), 0, 0, false);
                        textureatlassprite5.clearFramesTextureData();
                        List<int[][]> list1 = textureatlassprite4.getFramesTextureData();
                        textureatlassprite5.setFramesTextureData(list1);
                        textureatlassprite5.setAnimationMetadata(textureatlassprite4.getAnimationMetadata());
                    }

                    textureatlassprite5.sheetWidth = l2;
                    textureatlassprite5.sheetHeight = j3;
                    textureatlassprite5.mipmapLevels = this.mipmapLevels;
                    textureatlassprite5.setAnimationIndex(textureatlassprite4.getAnimationIndex());
                    textureatlassprite4.bindSpriteTexture();
                    boolean flag2 = false;
                    boolean flag1 = true;

                    try
                    {
                        TextureUtil.uploadTextureMipmap(textureatlassprite5.getFrameTextureData(0), textureatlassprite5.getIconWidth(), textureatlassprite5.getIconHeight(), textureatlassprite5.getOriginX(), textureatlassprite5.getOriginY(), flag2, flag1);
                    }
                    catch (Exception exception)
                    {
                        Config.dbg("Error uploading sprite single: " + textureatlassprite5 + ", parent: " + textureatlassprite4);
                        exception.printStackTrace();
                    }
                }
            }

            Config.getMinecraft().getTextureManager().bindTexture(LOCATION_BLOCKS_TEXTURE);
        }

        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, this);
        this.updateIconGrid(stitcher.getCurrentWidth(), stitcher.getCurrentHeight());

        if (Config.equals(System.getProperty("saveTextureMap"), "true"))
        {
            Config.dbg("Exporting texture map: " + this.basePath);
            TextureUtils.saveGlTexture("debug/" + this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }
    }

    public boolean generateMipmaps(IResourceManager resourceManager, final TextureAtlasSprite texture)
    {
        ResourceLocation resourcelocation1 = this.getResourceLocation(texture);
        IResource iresource1 = null;

        if (texture.hasCustomLoader(resourceManager, resourcelocation1))
        {
            TextureUtils.generateCustomMipmaps(texture, this.mipmapLevels);
        }
        else
        {
            label60:
            {
                boolean flag4;

                try
                {
                    iresource1 = resourceManager.getResource(resourcelocation1);
                    texture.loadSpriteFrames(iresource1, this.mipmapLevels + 1);
                    break label60;
                }
                catch (RuntimeException runtimeexception1)
                {
                    LOGGER.error("Unable to parse metadata from {}", resourcelocation1, runtimeexception1);
                    flag4 = false;
                }
                catch (IOException ioexception1)
                {
                    LOGGER.error("Using missing texture, unable to load {}", resourcelocation1, ioexception1);
                    flag4 = false;
                    boolean crashreportcategory = flag4;
                    return crashreportcategory;
                }
                finally
                {
                    IOUtils.closeQuietly((Closeable)iresource1);
                }

                return flag4;
            }
        }

        try
        {
            texture.generateMipmaps(this.mipmapLevels);
            return true;
        }
        catch (Throwable throwable1)
        {
            CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
            CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Sprite being mipmapped");
            crashreportcategory1.addDetail("Sprite name", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    return texture.getIconName();
                }
            });
            crashreportcategory1.addDetail("Sprite size", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    return texture.getIconWidth() + " x " + texture.getIconHeight();
                }
            });
            crashreportcategory1.addDetail("Sprite frames", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    return texture.getFrameCount() + " frames";
                }
            });
            crashreportcategory1.addCrashSection("Mipmap levels", Integer.valueOf(this.mipmapLevels));
            throw new ReportedException(crashreport1);
        }
    }

    public ResourceLocation getResourceLocation(TextureAtlasSprite p_184396_1_)
    {
        ResourceLocation resourcelocation1 = new ResourceLocation(p_184396_1_.getIconName());
        return this.completeResourceLocation(resourcelocation1);
    }

    public ResourceLocation completeResourceLocation(ResourceLocation p_completeResourceLocation_1_)
    {
        return this.isAbsoluteLocation(p_completeResourceLocation_1_) ? new ResourceLocation(p_completeResourceLocation_1_.getNamespace(), p_completeResourceLocation_1_.getPath() + ".png") : new ResourceLocation(p_completeResourceLocation_1_.getNamespace(), String.format("%s/%s%s", this.basePath, p_completeResourceLocation_1_.getPath(), ".png"));
    }

    public TextureAtlasSprite getAtlasSprite(String iconName)
    {
        TextureAtlasSprite textureatlassprite6 = this.mapUploadedSprites.get(iconName);

        if (textureatlassprite6 == null)
        {
            textureatlassprite6 = this.missingImage;
        }

        return textureatlassprite6;
    }

    public void updateAnimations()
    {
        boolean flag3 = false;
        boolean flag4 = false;
        TextureUtil.bindTexture(this.getGlTextureId());
        int i4 = 0;

        for (TextureAtlasSprite textureatlassprite6 : this.listAnimatedSprites)
        {
            if (this.isTerrainAnimationActive(textureatlassprite6))
            {
                textureatlassprite6.updateAnimation();

                if (textureatlassprite6.isAnimationActive())
                {
                    ++i4;
                }

                if (textureatlassprite6.spriteNormal != null)
                {
                    flag3 = true;
                }

                if (textureatlassprite6.spriteSpecular != null)
                {
                    flag4 = true;
                }
            }
        }

        if (Config.isMultiTexture())
        {
            for (TextureAtlasSprite textureatlassprite8 : this.listAnimatedSprites)
            {
                if (this.isTerrainAnimationActive(textureatlassprite8))
                {
                    TextureAtlasSprite textureatlassprite7 = textureatlassprite8.spriteSingle;

                    if (textureatlassprite7 != null)
                    {
                        if (textureatlassprite8 == TextureUtils.iconClock || textureatlassprite8 == TextureUtils.iconCompass)
                        {
                            textureatlassprite7.frameCounter = textureatlassprite8.frameCounter;
                        }

                        textureatlassprite8.bindSpriteTexture();
                        textureatlassprite7.updateAnimation();

                        if (textureatlassprite7.isAnimationActive())
                        {
                            ++i4;
                        }
                    }
                }
            }

            TextureUtil.bindTexture(this.getGlTextureId());
        }

        if (Config.isShaders())
        {
            if (flag3)
            {
                TextureUtil.bindTexture(this.getMultiTexID().norm);

                for (TextureAtlasSprite textureatlassprite9 : this.listAnimatedSprites)
                {
                    if (textureatlassprite9.spriteNormal != null && this.isTerrainAnimationActive(textureatlassprite9))
                    {
                        if (textureatlassprite9 == TextureUtils.iconClock || textureatlassprite9 == TextureUtils.iconCompass)
                        {
                            textureatlassprite9.spriteNormal.frameCounter = textureatlassprite9.frameCounter;
                        }

                        textureatlassprite9.spriteNormal.updateAnimation();

                        if (textureatlassprite9.spriteNormal.isAnimationActive())
                        {
                            ++i4;
                        }
                    }
                }
            }

            if (flag4)
            {
                TextureUtil.bindTexture(this.getMultiTexID().spec);

                for (TextureAtlasSprite textureatlassprite10 : this.listAnimatedSprites)
                {
                    if (textureatlassprite10.spriteSpecular != null && this.isTerrainAnimationActive(textureatlassprite10))
                    {
                        if (textureatlassprite10 == TextureUtils.iconClock || textureatlassprite10 == TextureUtils.iconCompass)
                        {
                            textureatlassprite10.spriteNormal.frameCounter = textureatlassprite10.frameCounter;
                        }

                        textureatlassprite10.spriteSpecular.updateAnimation();

                        if (textureatlassprite10.spriteSpecular.isAnimationActive())
                        {
                            ++i4;
                        }
                    }
                }
            }

            if (flag3 || flag4)
            {
                TextureUtil.bindTexture(this.getGlTextureId());
            }
        }

        int j4 = Config.getMinecraft().entityRenderer.frameCount;

        if (j4 != this.frameCountAnimations)
        {
            this.countAnimationsActive = i4;
            this.frameCountAnimations = j4;
        }

        if (SmartAnimations.isActive())
        {
            SmartAnimations.resetSpritesRendered();
        }
    }

    public TextureAtlasSprite registerSprite(ResourceLocation location)
    {
        if (location == null)
        {
            throw new IllegalArgumentException("Location cannot be null!");
        }
        else
        {
            TextureAtlasSprite textureatlassprite6 = this.mapRegisteredSprites.get(location.toString());

            if (textureatlassprite6 == null)
            {
                textureatlassprite6 = TextureAtlasSprite.makeAtlasSprite(location);
                this.mapRegisteredSprites.put(location.toString(), textureatlassprite6);
                textureatlassprite6.updateIndexInMap(this.counterIndexInMap);

                if (Config.isEmissiveTextures())
                {
                    this.checkEmissive(location, textureatlassprite6);
                }
            }

            return textureatlassprite6;
        }
    }

    public void tick()
    {
        this.updateAnimations();
    }

    public void setMipmapLevels(int mipmapLevelsIn)
    {
        this.mipmapLevels = mipmapLevelsIn;
    }

    public TextureAtlasSprite getMissingSprite()
    {
        return this.missingImage;
    }

    @Nullable
    public TextureAtlasSprite getTextureExtry(String p_getTextureExtry_1_)
    {
        return this.mapRegisteredSprites.get(p_getTextureExtry_1_);
    }

    public boolean setTextureEntry(TextureAtlasSprite p_setTextureEntry_1_)
    {
        String s1 = p_setTextureEntry_1_.getIconName();

        if (!this.mapRegisteredSprites.containsKey(s1))
        {
            this.mapRegisteredSprites.put(s1, p_setTextureEntry_1_);
            p_setTextureEntry_1_.updateIndexInMap(this.counterIndexInMap);
            return true;
        }
        else
        {
            return false;
        }
    }

    public String getBasePath()
    {
        return this.basePath;
    }

    public int getMipmapLevels()
    {
        return this.mipmapLevels;
    }

    private boolean isAbsoluteLocation(ResourceLocation p_isAbsoluteLocation_1_)
    {
        String s1 = p_isAbsoluteLocation_1_.getPath();
        return this.isAbsoluteLocationPath(s1);
    }

    private boolean isAbsoluteLocationPath(String p_isAbsoluteLocationPath_1_)
    {
        String s1 = p_isAbsoluteLocationPath_1_.toLowerCase();
        return s1.startsWith("mcpatcher/") || s1.startsWith("optifine/");
    }

    public TextureAtlasSprite getSpriteSafe(String p_getSpriteSafe_1_)
    {
        ResourceLocation resourcelocation1 = new ResourceLocation(p_getSpriteSafe_1_);
        return this.mapRegisteredSprites.get(resourcelocation1.toString());
    }

    public TextureAtlasSprite getRegisteredSprite(ResourceLocation p_getRegisteredSprite_1_)
    {
        return this.mapRegisteredSprites.get(p_getRegisteredSprite_1_.toString());
    }

    private boolean isTerrainAnimationActive(TextureAtlasSprite p_isTerrainAnimationActive_1_)
    {
        if (p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterFlow)
        {
            if (p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaFlow)
            {
                if (p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer0 && p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer1)
                {
                    if (p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal)
                    {
                        return Config.isAnimatedPortal();
                    }
                    else
                    {
                        return p_isTerrainAnimationActive_1_ != TextureUtils.iconClock && p_isTerrainAnimationActive_1_ != TextureUtils.iconCompass ? Config.isAnimatedTerrain() : true;
                    }
                }
                else
                {
                    return Config.isAnimatedFire();
                }
            }
            else
            {
                return Config.isAnimatedLava();
            }
        }
        else
        {
            return Config.isAnimatedWater();
        }
    }

    public int getCountRegisteredSprites()
    {
        return this.counterIndexInMap.getValue();
    }

    private int detectMaxMipmapLevel(Map p_detectMaxMipmapLevel_1_, IResourceManager p_detectMaxMipmapLevel_2_)
    {
        int i4 = this.detectMinimumSpriteSize(p_detectMaxMipmapLevel_1_, p_detectMaxMipmapLevel_2_, 20);

        if (i4 < 16)
        {
            i4 = 16;
        }

        i4 = MathHelper.smallestEncompassingPowerOfTwo(i4);

        if (i4 > 16)
        {
            Config.log("Sprite size: " + i4);
        }

        int j4 = MathHelper.log2(i4);

        if (j4 < 4)
        {
            j4 = 4;
        }

        return j4;
    }

    private int detectMinimumSpriteSize(Map p_detectMinimumSpriteSize_1_, IResourceManager p_detectMinimumSpriteSize_2_, int p_detectMinimumSpriteSize_3_)
    {
        Map map1 = new HashMap();
        for (Object entry : p_detectMinimumSpriteSize_1_.entrySet())
        {
            TextureAtlasSprite textureatlassprite6 = (TextureAtlasSprite)((Entry) entry).getValue();
            ResourceLocation resourcelocation1 = new ResourceLocation(textureatlassprite6.getIconName());
            ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation1);

            if (!textureatlassprite6.hasCustomLoader(p_detectMinimumSpriteSize_2_, resourcelocation1))
            {
                try
                {
                    IResource iresource1 = p_detectMinimumSpriteSize_2_.getResource(resourcelocation2);

                    if (iresource1 != null)
                    {
                        InputStream inputstream = iresource1.getInputStream();

                        if (inputstream != null)
                        {
                            Dimension dimension = TextureUtils.getImageSize(inputstream, "png");

                            if (dimension != null)
                            {
                                int k3 = dimension.width;
                                int l3 = MathHelper.smallestEncompassingPowerOfTwo(k3);

                                if (!map1.containsKey(Integer.valueOf(l3)))
                                {
                                    map1.put(Integer.valueOf(l3), Integer.valueOf(1));
                                }
                                else
                                {
                                    int i4 = ((Integer)map1.get(Integer.valueOf(l3))).intValue();
                                    map1.put(Integer.valueOf(l3), Integer.valueOf(i4 + 1));
                                }
                            }
                        }
                    }
                }
                catch (Exception var17)
                {
                    ;
                }
            }
        }

        int l4 = 0;
        Set set = map1.keySet();
        Set set1 = new TreeSet(set);
        int l5;

        for (Iterator iterator = set1.iterator(); iterator.hasNext(); l4 += l5)
        {
            int j5 = ((Integer)iterator.next()).intValue();
            l5 = ((Integer)map1.get(Integer.valueOf(j5))).intValue();
        }

        int i5 = 16;
        int k5 = 0;
        l5 = l4 * p_detectMinimumSpriteSize_3_ / 100;
        Iterator iterator1 = set1.iterator();

        while (iterator1.hasNext())
        {
            int i6 = ((Integer)iterator1.next()).intValue();
            int j6 = ((Integer)map1.get(Integer.valueOf(i6))).intValue();
            k5 += j6;

            if (i6 > i5)
            {
                i5 = i6;
            }

            if (k5 > l5)
            {
                return i5;
            }
        }

        return i5;
    }

    private int getMinSpriteSize()
    {
        int i4 = 1 << this.mipmapLevels;

        if (i4 < 8)
        {
            i4 = 8;
        }

        return i4;
    }

    private int[] getMissingImageData(int p_getMissingImageData_1_)
    {
        BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
        bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.MISSING_TEXTURE_DATA, 0, 16);
        BufferedImage bufferedimage1 = TextureUtils.scaleImage(bufferedimage, p_getMissingImageData_1_);
        int[] aint = new int[p_getMissingImageData_1_ * p_getMissingImageData_1_];
        bufferedimage1.getRGB(0, 0, p_getMissingImageData_1_, p_getMissingImageData_1_, aint, 0, p_getMissingImageData_1_);
        return aint;
    }

    public boolean isTextureBound()
    {
        int i4 = GlStateManager.getBoundTexture();
        int j4 = this.getGlTextureId();
        return i4 == j4;
    }

    private void updateIconGrid(int p_updateIconGrid_1_, int p_updateIconGrid_2_)
    {
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGrid = null;

        if (this.iconGridSize > 0)
        {
            this.iconGridCountX = p_updateIconGrid_1_ / this.iconGridSize;
            this.iconGridCountY = p_updateIconGrid_2_ / this.iconGridSize;
            this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
            this.iconGridSizeU = 1.0D / (double)this.iconGridCountX;
            this.iconGridSizeV = 1.0D / (double)this.iconGridCountY;

            for (TextureAtlasSprite textureatlassprite6 : this.mapUploadedSprites.values())
            {
                double d0 = 0.5D / (double)p_updateIconGrid_1_;
                double d1 = 0.5D / (double)p_updateIconGrid_2_;
                double d2 = (double)Math.min(textureatlassprite6.getMinU(), textureatlassprite6.getMaxU()) + d0;
                double d3 = (double)Math.min(textureatlassprite6.getMinV(), textureatlassprite6.getMaxV()) + d1;
                double d4 = (double)Math.max(textureatlassprite6.getMinU(), textureatlassprite6.getMaxU()) - d0;
                double d5 = (double)Math.max(textureatlassprite6.getMinV(), textureatlassprite6.getMaxV()) - d1;
                int i4 = (int)(d2 / this.iconGridSizeU);
                int j4 = (int)(d3 / this.iconGridSizeV);
                int k4 = (int)(d4 / this.iconGridSizeU);
                int l4 = (int)(d5 / this.iconGridSizeV);

                for (int i5 = i4; i5 <= k4; ++i5)
                {
                    if (i5 >= 0 && i5 < this.iconGridCountX)
                    {
                        for (int j5 = j4; j5 <= l4; ++j5)
                        {
                            if (j5 >= 0 && j5 < this.iconGridCountX)
                            {
                                int k5 = j5 * this.iconGridCountX + i5;
                                this.iconGrid[k5] = textureatlassprite6;
                            }
                            else
                            {
                                Config.warn("Invalid grid V: " + j5 + ", icon: " + textureatlassprite6.getIconName());
                            }
                        }
                    }
                    else
                    {
                        Config.warn("Invalid grid U: " + i5 + ", icon: " + textureatlassprite6.getIconName());
                    }
                }
            }
        }
    }

    public TextureAtlasSprite getIconByUV(double p_getIconByUV_1_, double p_getIconByUV_3_)
    {
        if (this.iconGrid == null)
        {
            return null;
        }
        else
        {
            int i4 = (int)(p_getIconByUV_1_ / this.iconGridSizeU);
            int j4 = (int)(p_getIconByUV_3_ / this.iconGridSizeV);
            int k4 = j4 * this.iconGridCountX + i4;
            return k4 >= 0 && k4 <= this.iconGrid.length ? this.iconGrid[k4] : null;
        }
    }

    private void checkEmissive(ResourceLocation p_checkEmissive_1_, TextureAtlasSprite p_checkEmissive_2_)
    {
        String s1 = EmissiveTextures.getSuffixEmissive();

        if (s1 != null)
        {
            if (!p_checkEmissive_1_.getPath().endsWith(s1))
            {
                ResourceLocation resourcelocation1 = new ResourceLocation(p_checkEmissive_1_.getNamespace(), p_checkEmissive_1_.getPath() + s1);
                ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation1);

                if (Config.hasResource(resourcelocation2))
                {
                    TextureAtlasSprite textureatlassprite6 = this.registerSprite(resourcelocation1);
                    textureatlassprite6.isEmissive = true;
                    p_checkEmissive_2_.spriteEmissive = textureatlassprite6;
                }
            }
        }
    }

    public int getCountAnimations()
    {
        return this.listAnimatedSprites.size();
    }

    public int getCountAnimationsActive()
    {
        return this.countAnimationsActive;
    }
}
