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
import java.util.Map.Entry;
import java.util.concurrent.Callable;
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
import optifine.Config;
import optifine.ConnectedTextures;
import optifine.Reflector;
import optifine.TextureUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

public class TextureMap extends AbstractTexture implements ITickableTextureObject {
   private final List listAnimatedSprites;
   private static final boolean ENABLE_SKIP = Boolean.parseBoolean(System.getProperty("fml.skipFirstTextureLoad", "true"));
   private final Map mapRegisteredSprites;
   private boolean skipFirst;
   private static final Logger logger = LogManager.getLogger();
   public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
   private int counterIndexInMap;
   private final TextureAtlasSprite missingImage;
   private TextureAtlasSprite[] iconGrid;
   private int iconGridCountY;
   public static final ResourceLocation locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
   private int iconGridSize;
   private final String basePath;
   private int iconGridCountX;
   private final IIconCreator iconCreator;
   private final Map mapUploadedSprites;
   public int atlasHeight;
   private double iconGridSizeU;
   private int mipmapLevels;
   private double iconGridSizeV;
   private static final String __OBFID = "CL_00001058";
   public int atlasWidth;

   public void loadSprites(IResourceManager var1, IIconCreator var2) {
      this.mapRegisteredSprites.clear();
      this.counterIndexInMap = 0;
      var2.registerSprites(this);
      if (this.mipmapLevels >= 4) {
         this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, var1);
         Config.log("Mipmap levels: " + this.mipmapLevels);
      }

      this.initMissingImage();
      this.deleteGlTexture();
      this.loadTextureAtlas(var1);
   }

   private int detectMinimumSpriteSize(Map var1, IResourceManager var2, int var3) {
      HashMap var4 = new HashMap();
      Iterator var6 = var1.entrySet().iterator();

      int var13;
      while(var6.hasNext()) {
         Object var5 = var6.next();
         TextureAtlasSprite var7 = (TextureAtlasSprite)((Entry)var5).getValue();
         ResourceLocation var8 = new ResourceLocation(var7.getIconName());
         ResourceLocation var9 = this.completeResourceLocation(var8, 0);
         if (!var7.hasCustomLoader(var2, var8)) {
            try {
               IResource var10 = var2.getResource(var9);
               if (var10 != null) {
                  InputStream var11 = var10.getInputStream();
                  if (var11 != null) {
                     Dimension var12 = TextureUtils.getImageSize(var11, "png");
                     if (var12 != null) {
                        var13 = var12.width;
                        int var14 = MathHelper.roundUpToPowerOfTwo(var13);
                        if (!var4.containsKey(var14)) {
                           var4.put(var14, 1);
                        } else {
                           int var15 = (Integer)var4.get(var14);
                           var4.put(var14, var15 + 1);
                        }
                     }
                  }
               }
            } catch (Exception var16) {
            }
         }
      }

      int var17 = 0;
      Set var18 = var4.keySet();
      TreeSet var19 = new TreeSet(var18);

      int var20;
      int var23;
      for(Iterator var21 = var19.iterator(); var21.hasNext(); var17 += var20) {
         var23 = (Integer)var21.next();
         var20 = (Integer)var4.get(var23);
      }

      int var22 = 16;
      var23 = 0;
      var20 = var17 * var3 / 100;
      Iterator var24 = var19.iterator();

      do {
         if (!var24.hasNext()) {
            return var22;
         }

         int var25 = (Integer)var24.next();
         var13 = (Integer)var4.get(var25);
         var23 += var13;
         if (var25 > var22) {
            var22 = var25;
         }
      } while(var23 <= var20);

      return var22;
   }

   private boolean isAbsoluteLocationPath(String var1) {
      String var2 = var1.toLowerCase();
      return var2.startsWith("mcpatcher/") || var2.startsWith("optifine/");
   }

   public boolean isTextureBound() {
      int var1 = GlStateManager.getBoundTexture();
      int var2 = this.getGlTextureId();
      return var1 == var2;
   }

   private int getMinSpriteSize() {
      int var1 = 1 << this.mipmapLevels;
      if (var1 < 8) {
         var1 = 8;
      }

      return var1;
   }

   private int[] getMissingImageData(int var1) {
      BufferedImage var2 = new BufferedImage(16, 16, 2);
      var2.setRGB(0, 0, 16, 16, TextureUtil.missingTextureData, 0, 16);
      BufferedImage var3 = TextureUtils.scaleToPowerOfTwo(var2, var1);
      int[] var4 = new int[var1 * var1];
      var3.getRGB(0, 0, var1, var1, var4, 0, var1);
      return var4;
   }

   public void tick() {
      this.updateAnimations();
   }

   public TextureAtlasSprite getAtlasSprite(String var1) {
      TextureAtlasSprite var2 = (TextureAtlasSprite)this.mapUploadedSprites.get(var1);
      if (var2 == null) {
         var2 = this.missingImage;
      }

      return var2;
   }

   public ResourceLocation completeResourceLocation(ResourceLocation var1, int var2) {
      return this.isAbsoluteLocation(var1) ? (var2 == 0 ? new ResourceLocation(var1.getResourceDomain(), var1.getResourcePath() + ".png") : new ResourceLocation(var1.getResourceDomain(), var1.getResourcePath() + "mipmap" + var2 + ".png")) : (var2 == 0 ? new ResourceLocation(var1.getResourceDomain(), String.format("%s/%s%s", this.basePath, var1.getResourcePath(), ".png")) : new ResourceLocation(var1.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", this.basePath, var1.getResourcePath(), var2, ".png")));
   }

   public TextureMap(String var1) {
      this(var1, (IIconCreator)null);
   }

   public TextureAtlasSprite getSpriteSafe(String var1) {
      ResourceLocation var2 = new ResourceLocation(var1);
      return (TextureAtlasSprite)this.mapRegisteredSprites.get(var2.toString());
   }

   public void loadTextureAtlas(IResourceManager var1) {
      Config.dbg("Multitexture: " + Config.isMultiTexture());
      if (Config.isMultiTexture()) {
         Iterator var3 = this.mapUploadedSprites.values().iterator();

         while(var3.hasNext()) {
            Object var2 = var3.next();
            ((TextureAtlasSprite)var2).deleteSpriteTexture();
         }
      }

      ConnectedTextures.updateIcons(this);
      int var26 = Minecraft.getGLMaximumTextureSize();
      Stitcher var27 = new Stitcher(var26, var26, true, 0, this.mipmapLevels);
      this.mapUploadedSprites.clear();
      this.listAnimatedSprites.clear();
      int var4 = Integer.MAX_VALUE;
      Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, this);
      int var5 = this.getMinSpriteSize();
      int var6 = 1 << this.mipmapLevels;
      Iterator var8 = this.mapRegisteredSprites.entrySet().iterator();

      while(true) {
         while(var8.hasNext()) {
            Object var7 = var8.next();
            TextureAtlasSprite var9 = (TextureAtlasSprite)((Entry)var7).getValue();
            ResourceLocation var10 = new ResourceLocation(var9.getIconName());
            ResourceLocation var11 = this.completeResourceLocation(var10, 0);
            if (!var9.hasCustomLoader(var1, var10)) {
               try {
                  IResource var12 = var1.getResource(var11);
                  BufferedImage[] var13 = new BufferedImage[1 + this.mipmapLevels];
                  var13[0] = TextureUtil.readBufferedImage(var12.getInputStream());
                  if (this.mipmapLevels > 0 && var13 != null) {
                     int var14 = var13[0].getWidth();
                     var13[0] = TextureUtils.scaleToPowerOfTwo(var13[0], var5);
                     int var15 = var13[0].getWidth();
                     if (!TextureUtils.isPowerOfTwo(var14)) {
                        Config.log("Scaled non power of 2: " + var9.getIconName() + ", " + var14 + " -> " + var15);
                     }
                  }

                  TextureMetadataSection var44 = (TextureMetadataSection)var12.getMetadata("texture");
                  if (var44 != null) {
                     List var47 = var44.getListMipmaps();
                     int var17;
                     if (!var47.isEmpty()) {
                        int var16 = var13[0].getWidth();
                        var17 = var13[0].getHeight();
                        if (MathHelper.roundUpToPowerOfTwo(var16) != var16 || MathHelper.roundUpToPowerOfTwo(var17) != var17) {
                           throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                        }
                     }

                     Iterator var50 = var47.iterator();

                     while(var50.hasNext()) {
                        var17 = (Integer)var50.next();
                        if (var17 > 0 && var17 < var13.length - 1 && var13[var17] == null) {
                           ResourceLocation var18 = this.completeResourceLocation(var10, var17);

                           try {
                              var13[var17] = TextureUtil.readBufferedImage(var1.getResource(var18).getInputStream());
                           } catch (IOException var23) {
                              logger.error("Unable to load miplevel {} from: {}", var17, var18, var23);
                           }
                        }
                     }
                  }

                  AnimationMetadataSection var48 = (AnimationMetadataSection)var12.getMetadata("animation");
                  var9.loadSprite(var13, var48);
               } catch (RuntimeException var24) {
                  logger.error((String)("Unable to parse metadata from " + var11), (Throwable)var24);
                  continue;
               } catch (IOException var25) {
                  logger.error("Using missing texture, unable to load " + var11 + ", " + var25.getClass().getName());
                  continue;
               }

               var4 = Math.min(var4, Math.min(var9.getIconWidth(), var9.getIconHeight()));
               int var37 = Math.min(Integer.lowestOneBit(var9.getIconWidth()), Integer.lowestOneBit(var9.getIconHeight()));
               if (var37 < var6) {
                  logger.warn("Texture {} with size {}x{} limits mip level from {} to {}", var11, var9.getIconWidth(), var9.getIconHeight(), MathHelper.calculateLogBaseTwo(var6), MathHelper.calculateLogBaseTwo(var37));
                  var6 = var37;
               }

               var27.addSprite(var9);
            } else if (!var9.load(var1, var10)) {
               var4 = Math.min(var4, Math.min(var9.getIconWidth(), var9.getIconHeight()));
               var27.addSprite(var9);
            }
         }

         int var28 = Math.min(var4, var6);
         int var29 = MathHelper.calculateLogBaseTwo(var28);
         if (var29 < 0) {
            var29 = 0;
         }

         if (var29 < this.mipmapLevels) {
            logger.info("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.basePath, this.mipmapLevels, var29, var28);
            this.mipmapLevels = var29;
         }

         Iterator var32 = this.mapRegisteredSprites.values().iterator();

         while(var32.hasNext()) {
            Object var30 = var32.next();
            TextureAtlasSprite var34 = (TextureAtlasSprite)var30;

            try {
               var34.generateMipmaps(this.mipmapLevels);
            } catch (Throwable var22) {
               CrashReport var41 = CrashReport.makeCrashReport(var22, "Applying mipmap");
               CrashReportCategory var45 = var41.makeCategory("Sprite being mipmapped");
               var45.addCrashSectionCallable("Sprite name", new Callable(this, var34) {
                  private static final String __OBFID = "CL_00001059";
                  final TextureMap this$0;
                  private final TextureAtlasSprite val$textureatlassprite2;

                  public Object call() throws Exception {
                     return this.call();
                  }

                  public String call() throws Exception {
                     return this.val$textureatlassprite2.getIconName();
                  }

                  {
                     this.this$0 = var1;
                     this.val$textureatlassprite2 = var2;
                  }
               });
               var45.addCrashSectionCallable("Sprite size", new Callable(this, var34) {
                  private static final String __OBFID = "CL_00001060";
                  final TextureMap this$0;
                  private final TextureAtlasSprite val$textureatlassprite2;

                  public Object call() throws Exception {
                     return this.call();
                  }

                  {
                     this.this$0 = var1;
                     this.val$textureatlassprite2 = var2;
                  }

                  public String call() throws Exception {
                     return this.val$textureatlassprite2.getIconWidth() + " x " + this.val$textureatlassprite2.getIconHeight();
                  }
               });
               var45.addCrashSectionCallable("Sprite frames", new Callable(this, var34) {
                  private static final String __OBFID = "CL_00001061";
                  private final TextureAtlasSprite val$textureatlassprite2;
                  final TextureMap this$0;

                  {
                     this.this$0 = var1;
                     this.val$textureatlassprite2 = var2;
                  }

                  public String call() throws Exception {
                     return this.val$textureatlassprite2.getFrameCount() + " frames";
                  }

                  public Object call() throws Exception {
                     return this.call();
                  }
               });
               var45.addCrashSection("Mipmap levels", this.mipmapLevels);
               throw new ReportedException(var41);
            }
         }

         this.missingImage.generateMipmaps(this.mipmapLevels);
         var27.addSprite(this.missingImage);

         try {
            var27.doStitch();
         } catch (StitcherException var21) {
            throw var21;
         }

         logger.info("Created: {}x{} {}-atlas", var27.getCurrentWidth(), var27.getCurrentHeight(), this.basePath);
         TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, var27.getCurrentWidth(), var27.getCurrentHeight());
         HashMap var31 = Maps.newHashMap(this.mapRegisteredSprites);
         Iterator var36 = var27.getStichSlots().iterator();

         Object var33;
         while(var36.hasNext()) {
            var33 = var36.next();
            TextureAtlasSprite var39 = (TextureAtlasSprite)var33;
            String var42 = var39.getIconName();
            var31.remove(var42);
            this.mapUploadedSprites.put(var42, var39);

            try {
               TextureUtil.uploadTextureMipmap(var39.getFrameTextureData(0), var39.getIconWidth(), var39.getIconHeight(), var39.getOriginX(), var39.getOriginY(), false, false);
            } catch (Throwable var20) {
               CrashReport var49 = CrashReport.makeCrashReport(var20, "Stitching texture atlas");
               CrashReportCategory var52 = var49.makeCategory("Texture being stitched together");
               var52.addCrashSection("Atlas path", this.basePath);
               var52.addCrashSection("Sprite", var39);
               throw new ReportedException(var49);
            }

            if (var39.hasAnimationMetadata()) {
               this.listAnimatedSprites.add(var39);
            }
         }

         var36 = var31.values().iterator();

         while(var36.hasNext()) {
            var33 = var36.next();
            ((TextureAtlasSprite)var33).copyFrom(this.missingImage);
         }

         if (Config.isMultiTexture()) {
            int var35 = var27.getCurrentWidth();
            int var38 = var27.getCurrentHeight();
            Iterator var43 = var27.getStichSlots().iterator();

            while(var43.hasNext()) {
               Object var40 = var43.next();
               TextureAtlasSprite var46 = (TextureAtlasSprite)var40;
               var46.sheetWidth = var35;
               var46.sheetHeight = var38;
               var46.mipmapLevels = this.mipmapLevels;
               TextureAtlasSprite var51 = var46.spriteSingle;
               if (var51 != null) {
                  var51.sheetWidth = var35;
                  var51.sheetHeight = var38;
                  var51.mipmapLevels = this.mipmapLevels;
                  var46.bindSpriteTexture();
                  boolean var53 = false;
                  boolean var54 = true;
                  TextureUtil.uploadTextureMipmap(var51.getFrameTextureData(0), var51.getIconWidth(), var51.getIconHeight(), var51.getOriginX(), var51.getOriginY(), var53, var54);
               }
            }

            Config.getMinecraft().getTextureManager().bindTexture(locationBlocksTexture);
         }

         Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, this);
         if (Config.equals(System.getProperty("saveTextureMap"), "true")) {
            TextureUtil.saveGlTexture(this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, var27.getCurrentWidth(), var27.getCurrentHeight());
         }

         return;
      }
   }

   public void setMipmapLevels(int var1) {
      this.mipmapLevels = var1;
   }

   public void updateAnimations() {
      if (Config.isShaders()) {
         ShadersTex.updatingTex = this.getMultiTexID();
      }

      boolean var1 = false;
      boolean var2 = false;
      TextureUtil.bindTexture(this.getGlTextureId());
      Iterator var4 = this.listAnimatedSprites.iterator();

      Object var3;
      TextureAtlasSprite var5;
      while(var4.hasNext()) {
         var3 = var4.next();
         var5 = (TextureAtlasSprite)var3;
         if (this != var5) {
            var5.updateAnimation();
            if (var5.spriteNormal != null) {
               var1 = true;
            }

            if (var5.spriteSpecular != null) {
               var2 = true;
            }
         }
      }

      if (Config.isMultiTexture()) {
         var4 = this.listAnimatedSprites.iterator();

         label120:
         while(true) {
            TextureAtlasSprite var6;
            do {
               do {
                  if (!var4.hasNext()) {
                     TextureUtil.bindTexture(this.getGlTextureId());
                     break label120;
                  }

                  var3 = var4.next();
                  var5 = (TextureAtlasSprite)var3;
               } while(this == var5);

               var6 = var5.spriteSingle;
            } while(var6 == null);

            if (var5 == TextureUtils.iconClock || var5 == TextureUtils.iconCompass) {
               var6.frameCounter = var5.frameCounter;
            }

            var5.bindSpriteTexture();
            var6.updateAnimation();
         }
      }

      if (Config.isShaders()) {
         if (var1) {
            TextureUtil.bindTexture(this.getMultiTexID().norm);
            var4 = this.listAnimatedSprites.iterator();

            label99:
            while(true) {
               do {
                  do {
                     if (!var4.hasNext()) {
                        break label99;
                     }

                     var3 = var4.next();
                     var5 = (TextureAtlasSprite)var3;
                  } while(var5.spriteNormal == null);
               } while(this == var5);

               if (var5 == TextureUtils.iconClock || var5 == TextureUtils.iconCompass) {
                  var5.spriteNormal.frameCounter = var5.frameCounter;
               }

               var5.spriteNormal.updateAnimation();
            }
         }

         if (var2) {
            TextureUtil.bindTexture(this.getMultiTexID().spec);
            var4 = this.listAnimatedSprites.iterator();

            label78:
            while(true) {
               do {
                  do {
                     if (!var4.hasNext()) {
                        break label78;
                     }

                     var3 = var4.next();
                     var5 = (TextureAtlasSprite)var3;
                  } while(var5.spriteSpecular == null);
               } while(this == var5);

               if (var5 == TextureUtils.iconClock || var5 == TextureUtils.iconCompass) {
                  var5.spriteNormal.frameCounter = var5.frameCounter;
               }

               var5.spriteSpecular.updateAnimation();
            }
         }

         if (var1 || var2) {
            TextureUtil.bindTexture(this.getGlTextureId());
         }
      }

      if (Config.isShaders()) {
         ShadersTex.updatingTex = null;
      }

   }

   public int getMipmapLevels() {
      return this.mipmapLevels;
   }

   public boolean setTextureEntry(TextureAtlasSprite var1) {
      return this.setTextureEntry(var1.getIconName(), var1);
   }

   private void initMissingImage() {
      int var1 = this.getMinSpriteSize();
      int[] var2 = this.getMissingImageData(var1);
      this.missingImage.setIconWidth(var1);
      this.missingImage.setIconHeight(var1);
      int[][] var3 = new int[this.mipmapLevels + 1][];
      var3[0] = var2;
      this.missingImage.setFramesTextureData(Lists.newArrayList((Object[])(var3)));
      this.missingImage.setIndexInMap(this.counterIndexInMap++);
   }

   public TextureAtlasSprite getMissingSprite() {
      return this.missingImage;
   }

   public void loadTexture(IResourceManager var1) throws IOException {
      ShadersTex.resManager = var1;
      if (this.iconCreator != null) {
         this.loadSprites(var1, this.iconCreator);
      }

   }

   public TextureMap(String var1, IIconCreator var2) {
      this(var1, var2, false);
   }

   public TextureMap(String var1, IIconCreator var2, boolean var3) {
      this.skipFirst = false;
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
      this.basePath = var1;
      this.iconCreator = var2;
      this.skipFirst = var3 && ENABLE_SKIP;
   }

   public TextureAtlasSprite getTextureExtry(String var1) {
      ResourceLocation var2 = new ResourceLocation(var1);
      return (TextureAtlasSprite)this.mapRegisteredSprites.get(var2.toString());
   }

   private int detectMaxMipmapLevel(Map var1, IResourceManager var2) {
      int var3 = this.detectMinimumSpriteSize(var1, var2, 20);
      if (var3 < 16) {
         var3 = 16;
      }

      var3 = MathHelper.roundUpToPowerOfTwo(var3);
      if (var3 > 16) {
         Config.log("Sprite size: " + var3);
      }

      int var4 = MathHelper.calculateLogBaseTwo(var3);
      if (var4 < 4) {
         var4 = 4;
      }

      return var4;
   }

   public TextureMap(String var1, boolean var2) {
      this(var1, (IIconCreator)null, var2);
   }

   public int getCountRegisteredSprites() {
      return this.counterIndexInMap;
   }

   public boolean setTextureEntry(String var1, TextureAtlasSprite var2) {
      if (!this.mapRegisteredSprites.containsKey(var1)) {
         this.mapRegisteredSprites.put(var1, var2);
         if (var2.getIndexInMap() < 0) {
            var2.setIndexInMap(this.counterIndexInMap++);
         }

         return true;
      } else {
         return false;
      }
   }

   private void updateIconGrid(int var1, int var2) {
      this.iconGridCountX = -1;
      this.iconGridCountY = -1;
      this.iconGrid = null;
      if (this.iconGridSize > 0) {
         this.iconGridCountX = var1 / this.iconGridSize;
         this.iconGridCountY = var2 / this.iconGridSize;
         this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
         this.iconGridSizeU = 1.0D / (double)this.iconGridCountX;
         this.iconGridSizeV = 1.0D / (double)this.iconGridCountY;
         Iterator var4 = this.mapUploadedSprites.values().iterator();

         while(var4.hasNext()) {
            Object var3 = var4.next();
            TextureAtlasSprite var5 = (TextureAtlasSprite)var3;
            double var6 = 0.5D / (double)var1;
            double var8 = 0.5D / (double)var2;
            double var10 = (double)Math.min(var5.getMinU(), var5.getMaxU()) + var6;
            double var12 = (double)Math.min(var5.getMinV(), var5.getMaxV()) + var8;
            double var14 = (double)Math.max(var5.getMinU(), var5.getMaxU()) - var6;
            double var16 = (double)Math.max(var5.getMinV(), var5.getMaxV()) - var8;
            int var18 = (int)(var10 / this.iconGridSizeU);
            int var19 = (int)(var12 / this.iconGridSizeV);
            int var20 = (int)(var14 / this.iconGridSizeU);
            int var21 = (int)(var16 / this.iconGridSizeV);

            for(int var22 = var18; var22 <= var20; ++var22) {
               if (var22 >= 0 && var22 < this.iconGridCountX) {
                  for(int var23 = var19; var23 <= var21; ++var23) {
                     if (var23 >= 0 && var23 < this.iconGridCountX) {
                        int var24 = var23 * this.iconGridCountX + var22;
                        this.iconGrid[var24] = var5;
                     } else {
                        Config.warn("Invalid grid V: " + var23 + ", icon: " + var5.getIconName());
                     }
                  }
               } else {
                  Config.warn("Invalid grid U: " + var22 + ", icon: " + var5.getIconName());
               }
            }
         }
      }

   }

   private boolean isAbsoluteLocation(ResourceLocation var1) {
      String var2 = var1.getResourcePath();
      return this.isAbsoluteLocationPath(var2);
   }

   public TextureAtlasSprite registerSprite(ResourceLocation var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Location cannot be null!");
      } else {
         TextureAtlasSprite var2 = (TextureAtlasSprite)this.mapRegisteredSprites.get(var1.toString());
         if (var2 == null) {
            var2 = TextureAtlasSprite.makeAtlasSprite(var1);
            this.mapRegisteredSprites.put(var1.toString(), var2);
            if (var2.getIndexInMap() < 0) {
               var2.setIndexInMap(this.counterIndexInMap++);
            }
         }

         return var2;
      }
   }

   public String getBasePath() {
      return this.basePath;
   }

   public TextureAtlasSprite getIconByUV(double var1, double var3) {
      if (this.iconGrid == null) {
         return null;
      } else {
         int var5 = (int)(var1 / this.iconGridSizeU);
         int var6 = (int)(var3 / this.iconGridSizeV);
         int var7 = var6 * this.iconGridCountX + var5;
         return var7 >= 0 && var7 <= this.iconGrid.length ? this.iconGrid[var7] : null;
      }
   }
}
