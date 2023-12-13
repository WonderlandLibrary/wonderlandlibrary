package shadersmod.client;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.Config;
import optifine.Lang;
import org.lwjgl.Sys;

public class GuiShaders extends GuiScreen {
   protected GuiScreen parentGui;
   protected String screenTitle = "Shaders";
   private int updateTimer = -1;
   private GuiSlotShaders shaderList;
   private boolean saved = false;
   private static float[] QUALITY_MULTIPLIERS = new float[]{0.5F, 0.70710677F, 1.0F, 1.4142135F, 2.0F};
   private static String[] QUALITY_MULTIPLIER_NAMES = new String[]{"0.5x", "0.7x", "1x", "1.5x", "2x"};
   private static float[] HAND_DEPTH_VALUES = new float[]{0.0625F, 0.125F, 0.25F};
   private static String[] HAND_DEPTH_NAMES = new String[]{"0.5x", "1x", "2x"};
   public static final int EnumOS_UNKNOWN = 0;
   public static final int EnumOS_WINDOWS = 1;
   public static final int EnumOS_OSX = 2;
   public static final int EnumOS_SOLARIS = 3;
   public static final int EnumOS_LINUX = 4;
   private static volatile int[] $SWITCH_TABLE$shadersmod$client$EnumShaderOption;

   public GuiShaders(GuiScreen var1, GameSettings var2) {
      this.parentGui = var1;
   }

   public void initGui() {
      this.screenTitle = I18n.format("of.options.shadersTitle");
      if (Shaders.shadersConfig == null) {
         Shaders.loadConfig();
      }

      byte var1 = 120;
      byte var2 = 20;
      int var3 = width - var1 - 10;
      byte var4 = 30;
      byte var5 = 20;
      int var6 = width - var1 - 20;
      this.shaderList = new GuiSlotShaders(this, var6, height, var4, height - 50, 16);
      this.shaderList.registerScrollButtons(7, 8);
      this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.ANTIALIASING, var3, 0 * var5 + var4, var1, var2));
      this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.NORMAL_MAP, var3, 1 * var5 + var4, var1, var2));
      this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SPECULAR_MAP, var3, 2 * var5 + var4, var1, var2));
      this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.RENDER_RES_MUL, var3, 3 * var5 + var4, var1, var2));
      this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.SHADOW_RES_MUL, var3, 4 * var5 + var4, var1, var2));
      this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.HAND_DEPTH_MUL, var3, 5 * var5 + var4, var1, var2));
      this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_HAND_LIGHT, var3, 6 * var5 + var4, var1, var2));
      this.buttonList.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_LIGHTING, var3, 7 * var5 + var4, var1, var2));
      int var7 = Math.min(150, var6 / 2 - 10);
      this.buttonList.add(new GuiButton(201, var6 / 4 - var7 / 2, height - 25, var7, var2, Lang.get("of.options.shaders.shadersFolder")));
      this.buttonList.add(new GuiButton(202, var6 / 4 * 3 - var7 / 2, height - 25, var7, var2, I18n.format("gui.done")));
      this.buttonList.add(new GuiButton(203, var3, height - 25, var1, var2, Lang.get("of.options.shaders.shaderOptions")));
      this.updateButtons();
   }

   public void updateButtons() {
      boolean var1 = Config.isShaders();
      Iterator var3 = this.buttonList.iterator();

      while(var3.hasNext()) {
         GuiButton var2 = (GuiButton)var3.next();
         if (var2.id != 201 && var2.id != 202 && var2.id != EnumShaderOption.ANTIALIASING.ordinal()) {
            var2.enabled = var1;
         }
      }

   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.shaderList.handleMouseInput();
   }

   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1 instanceof GuiButtonEnumShaderOption) {
            GuiButtonEnumShaderOption var2 = (GuiButtonEnumShaderOption)var1;
            switch($SWITCH_TABLE$shadersmod$client$EnumShaderOption()[var2.getEnumShaderOption().ordinal()]) {
            case 1:
               Shaders.nextAntialiasingLevel();
               Shaders.uninit();
               break;
            case 2:
               Shaders.configNormalMap = !Shaders.configNormalMap;
               this.mc.scheduleResourcesRefresh();
               break;
            case 3:
               Shaders.configSpecularMap = !Shaders.configSpecularMap;
               this.mc.scheduleResourcesRefresh();
               break;
            case 4:
               float var3 = Shaders.configRenderResMul;
               float[] var4 = QUALITY_MULTIPLIERS;
               String[] var5 = QUALITY_MULTIPLIER_NAMES;
               int var6 = getValueIndex(var3, var4);
               if (isShiftKeyDown()) {
                  --var6;
                  if (var6 < 0) {
                     var6 = var4.length - 1;
                  }
               } else {
                  ++var6;
                  if (var6 >= var4.length) {
                     var6 = 0;
                  }
               }

               Shaders.configRenderResMul = var4[var6];
               Shaders.scheduleResize();
               break;
            case 5:
               float var7 = Shaders.configShadowResMul;
               float[] var8 = QUALITY_MULTIPLIERS;
               String[] var9 = QUALITY_MULTIPLIER_NAMES;
               int var10 = getValueIndex(var7, var8);
               if (isShiftKeyDown()) {
                  --var10;
                  if (var10 < 0) {
                     var10 = var8.length - 1;
                  }
               } else {
                  ++var10;
                  if (var10 >= var8.length) {
                     var10 = 0;
                  }
               }

               Shaders.configShadowResMul = var8[var10];
               Shaders.scheduleResizeShadow();
               break;
            case 6:
               float var11 = Shaders.configHandDepthMul;
               float[] var12 = HAND_DEPTH_VALUES;
               String[] var13 = HAND_DEPTH_NAMES;
               int var14 = getValueIndex(var11, var12);
               if (isShiftKeyDown()) {
                  --var14;
                  if (var14 < 0) {
                     var14 = var12.length - 1;
                  }
               } else {
                  ++var14;
                  if (var14 >= var12.length) {
                     var14 = 0;
                  }
               }

               Shaders.configHandDepthMul = var12[var14];
               break;
            case 7:
               Shaders.configCloudShadow = !Shaders.configCloudShadow;
               break;
            case 8:
               Shaders.configOldHandLight.nextValue();
               break;
            case 9:
               Shaders.configOldLighting.nextValue();
               Shaders.updateBlockLightLevel();
               this.mc.scheduleResourcesRefresh();
            case 10:
            case 14:
            case 15:
            case 16:
            default:
               break;
            case 11:
               Shaders.configTweakBlockDamage = !Shaders.configTweakBlockDamage;
               break;
            case 12:
               Shaders.configShadowClipFrustrum = !Shaders.configShadowClipFrustrum;
               var1.displayString = "ShadowClipFrustrum: " + toStringOnOff(Shaders.configShadowClipFrustrum);
               ShadersTex.updateTextureMinMagFilter();
               break;
            case 13:
               Shaders.configTexMinFilB = (Shaders.configTexMinFilB + 1) % 3;
               Shaders.configTexMinFilN = Shaders.configTexMinFilS = Shaders.configTexMinFilB;
               var1.displayString = "Tex Min: " + Shaders.texMinFilDesc[Shaders.configTexMinFilB];
               ShadersTex.updateTextureMinMagFilter();
               break;
            case 17:
               Shaders.configTexMagFilN = (Shaders.configTexMagFilN + 1) % 2;
               var1.displayString = "Tex_n Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilN];
               ShadersTex.updateTextureMinMagFilter();
               break;
            case 18:
               Shaders.configTexMagFilS = (Shaders.configTexMagFilS + 1) % 2;
               var1.displayString = "Tex_s Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilS];
               ShadersTex.updateTextureMinMagFilter();
            }

            var2.updateButtonText();
         } else {
            switch(var1.id) {
            case 201:
               switch(getOSType()) {
               case 1:
                  String var18 = String.format("cmd.exe /C start \"Open file\" \"%s\"", Shaders.shaderpacksdir.getAbsolutePath());

                  try {
                     Runtime.getRuntime().exec(var18);
                     return;
                  } catch (IOException var17) {
                     var17.printStackTrace();
                     break;
                  }
               case 2:
                  try {
                     Runtime.getRuntime().exec(new String[]{"/usr/bin/open", Shaders.shaderpacksdir.getAbsolutePath()});
                     return;
                  } catch (IOException var16) {
                     var16.printStackTrace();
                  }
               }

               boolean var19 = false;

               try {
                  Class var21 = Class.forName("java.awt.Desktop");
                  Object var22 = var21.getMethod("getDesktop").invoke((Object)null);
                  var21.getMethod("browse", URI.class).invoke(var22, (new File(this.mc.mcDataDir, Shaders.shaderpacksdirname)).toURI());
               } catch (Throwable var15) {
                  var15.printStackTrace();
                  var19 = true;
               }

               if (var19) {
                  Config.dbg("Opening via system class!");
                  Sys.openURL("file://" + Shaders.shaderpacksdir.getAbsolutePath());
               }
               break;
            case 202:
               new File(Shaders.shadersdir, "current.cfg");
               Shaders.storeConfig();
               this.saved = true;
               this.mc.displayGuiScreen(this.parentGui);
               break;
            case 203:
               GuiShaderOptions var20 = new GuiShaderOptions(this, Config.getGameSettings());
               Config.getMinecraft().displayGuiScreen(var20);
               break;
            default:
               this.shaderList.actionPerformed(var1);
            }
         }
      }

   }

   public void onGuiClosed() {
      super.onGuiClosed();
      if (!this.saved) {
         Shaders.storeConfig();
      }

   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.shaderList.drawScreen(var1, var2, var3);
      if (this.updateTimer <= 0) {
         this.shaderList.updateList();
         this.updateTimer += 20;
      }

      this.drawCenteredString(this.fontRendererObj, this.screenTitle + " ", width / 2, 15, 16777215);
      String var4 = "OpenGL: " + Shaders.glVersionString + ", " + Shaders.glVendorString + ", " + Shaders.glRendererString;
      int var5 = this.fontRendererObj.getStringWidth(var4);
      if (var5 < width - 5) {
         this.drawCenteredString(this.fontRendererObj, var4, width / 2, height - 40, 8421504);
      } else {
         this.drawString(this.fontRendererObj, var4, 5, height - 40, 8421504);
      }

      super.drawScreen(var1, var2, var3);
   }

   public void updateScreen() {
      super.updateScreen();
      --this.updateTimer;
   }

   public Minecraft getMc() {
      return this.mc;
   }

   public void drawCenteredString(String var1, int var2, int var3, int var4) {
      this.drawCenteredString(this.fontRendererObj, var1, var2, var3, var4);
   }

   public static String toStringOnOff(boolean var0) {
      String var1 = Lang.getOn();
      String var2 = Lang.getOff();
      return var0 ? var1 : var2;
   }

   public static String toStringAa(int var0) {
      return var0 == 2 ? "FXAA 2x" : (var0 == 4 ? "FXAA 4x" : Lang.getOff());
   }

   public static String toStringValue(float var0, float[] var1, String[] var2) {
      int var3 = getValueIndex(var0, var1);
      return var2[var3];
   }

   public static int getValueIndex(float var0, float[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         float var3 = var1[var2];
         if (var3 >= var0) {
            return var2;
         }
      }

      return var1.length - 1;
   }

   public static String toStringQuality(float var0) {
      return toStringValue(var0, QUALITY_MULTIPLIERS, QUALITY_MULTIPLIER_NAMES);
   }

   public static String toStringHandDepth(float var0) {
      return toStringValue(var0, HAND_DEPTH_VALUES, HAND_DEPTH_NAMES);
   }

   public static int getOSType() {
      String var0 = System.getProperty("os.name").toLowerCase();
      return var0.contains("win") ? 1 : (var0.contains("mac") ? 2 : (var0.contains("solaris") ? 3 : (var0.contains("sunos") ? 3 : (var0.contains("linux") ? 4 : (var0.contains("unix") ? 4 : 0)))));
   }

   static int[] $SWITCH_TABLE$shadersmod$client$EnumShaderOption() {
      int[] var10000 = $SWITCH_TABLE$shadersmod$client$EnumShaderOption;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EnumShaderOption.values().length];

         try {
            var0[EnumShaderOption.ANTIALIASING.ordinal()] = 1;
         } catch (NoSuchFieldError var18) {
         }

         try {
            var0[EnumShaderOption.CLOUD_SHADOW.ordinal()] = 7;
         } catch (NoSuchFieldError var17) {
         }

         try {
            var0[EnumShaderOption.HAND_DEPTH_MUL.ordinal()] = 6;
         } catch (NoSuchFieldError var16) {
         }

         try {
            var0[EnumShaderOption.NORMAL_MAP.ordinal()] = 2;
         } catch (NoSuchFieldError var15) {
         }

         try {
            var0[EnumShaderOption.OLD_HAND_LIGHT.ordinal()] = 8;
         } catch (NoSuchFieldError var14) {
         }

         try {
            var0[EnumShaderOption.OLD_LIGHTING.ordinal()] = 9;
         } catch (NoSuchFieldError var13) {
         }

         try {
            var0[EnumShaderOption.RENDER_RES_MUL.ordinal()] = 4;
         } catch (NoSuchFieldError var12) {
         }

         try {
            var0[EnumShaderOption.SHADER_PACK.ordinal()] = 10;
         } catch (NoSuchFieldError var11) {
         }

         try {
            var0[EnumShaderOption.SHADOW_CLIP_FRUSTRUM.ordinal()] = 12;
         } catch (NoSuchFieldError var10) {
         }

         try {
            var0[EnumShaderOption.SHADOW_RES_MUL.ordinal()] = 5;
         } catch (NoSuchFieldError var9) {
         }

         try {
            var0[EnumShaderOption.SPECULAR_MAP.ordinal()] = 3;
         } catch (NoSuchFieldError var8) {
         }

         try {
            var0[EnumShaderOption.TEX_MAG_FIL_B.ordinal()] = 16;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[EnumShaderOption.TEX_MAG_FIL_N.ordinal()] = 17;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[EnumShaderOption.TEX_MAG_FIL_S.ordinal()] = 18;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[EnumShaderOption.TEX_MIN_FIL_B.ordinal()] = 13;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[EnumShaderOption.TEX_MIN_FIL_N.ordinal()] = 14;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EnumShaderOption.TEX_MIN_FIL_S.ordinal()] = 15;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[EnumShaderOption.TWEAK_BLOCK_DAMAGE.ordinal()] = 11;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$shadersmod$client$EnumShaderOption = var0;
         return var0;
      }
   }
}
