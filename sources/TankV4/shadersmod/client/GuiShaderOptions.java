package shadersmod.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.Config;
import optifine.GuiScreenOF;
import optifine.Lang;
import optifine.StrUtils;

public class GuiShaderOptions extends GuiScreenOF {
   private GuiScreen prevScreen;
   protected String title;
   private GameSettings settings;
   private int lastMouseX;
   private int lastMouseY;
   private long mouseStillTime;
   private String screenName;
   private String screenText;
   private boolean changed;
   public static final String OPTION_PROFILE = "<profile>";
   public static final String OPTION_EMPTY = "<empty>";
   public static final String OPTION_REST = "*";

   public GuiShaderOptions(GuiScreen var1, GameSettings var2) {
      this.lastMouseX = 0;
      this.lastMouseY = 0;
      this.mouseStillTime = 0L;
      this.screenName = null;
      this.screenText = null;
      this.changed = false;
      this.title = "Shader Options";
      this.prevScreen = var1;
      this.settings = var2;
   }

   public GuiShaderOptions(GuiScreen var1, GameSettings var2, String var3) {
      this(var1, var2);
      this.screenName = var3;
      if (var3 != null) {
         this.screenText = Shaders.translate("screen." + var3, var3);
      }

   }

   public void initGui() {
      this.title = I18n.format("of.options.shaderOptionsTitle");
      byte var1 = 100;
      boolean var2 = false;
      byte var3 = 30;
      byte var4 = 20;
      int var5 = width - 130;
      byte var6 = 120;
      byte var7 = 20;
      int var8 = 2;
      ShaderOption[] var9 = Shaders.getShaderPackOptions(this.screenName);
      if (var9 != null) {
         if (var9.length > 18) {
            var8 = var9.length / 9 + 1;
         }

         for(int var10 = 0; var10 < var9.length; ++var10) {
            ShaderOption var11 = var9[var10];
            if (var11 != null && var11.isVisible()) {
               int var12 = var10 % var8;
               int var13 = var10 / var8;
               int var14 = Math.min(width / var8, 200);
               int var20 = (width - var14 * var8) / 2;
               int var15 = var12 * var14 + 5 + var20;
               int var16 = var3 + var13 * var4;
               int var17 = var14 - 10;
               String var18 = this.getButtonText(var11, var17);
               GuiButtonShaderOption var19 = new GuiButtonShaderOption(var1 + var10, var15, var16, var17, var7, var11, var18);
               var19.enabled = var11.isEnabled();
               this.buttonList.add(var19);
            }
         }
      }

      this.buttonList.add(new GuiButton(201, width / 2 - var6 - 20, height / 6 + 168 + 11, var6, var7, I18n.format("controls.reset")));
      this.buttonList.add(new GuiButton(200, width / 2 + 20, height / 6 + 168 + 11, var6, var7, I18n.format("gui.done")));
   }

   private String getButtonText(ShaderOption var1, int var2) {
      String var3 = var1.getNameText();
      if (var1 instanceof ShaderOptionScreen) {
         ShaderOptionScreen var7 = (ShaderOptionScreen)var1;
         return var3 + "...";
      } else {
         Config.getMinecraft();
         FontRenderer var4 = Minecraft.fontRendererObj;

         for(int var5 = var4.getStringWidth(": " + Lang.getOff()) + 5; var4.getStringWidth(var3) + var5 >= var2 && var3.length() > 0; var3 = var3.substring(0, var3.length() - 1)) {
         }

         String var8 = var1.isChanged() ? var1.getValueColor(var1.getValue()) : "";
         String var6 = var1.getValueText(var1.getValue());
         return var3 + ": " + var8 + var6;
      }
   }

   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id < 200 && var1 instanceof GuiButtonShaderOption) {
            GuiButtonShaderOption var2 = (GuiButtonShaderOption)var1;
            ShaderOption var3 = var2.getShaderOption();
            if (var3 instanceof ShaderOptionScreen) {
               String var8 = var3.getName();
               GuiShaderOptions var5 = new GuiShaderOptions(this, this.settings, var8);
               this.mc.displayGuiScreen(var5);
               return;
            }

            if (isShiftKeyDown()) {
               var3.resetValue();
            } else {
               var3.nextValue();
            }

            this.updateAllButtons();
            this.changed = true;
         }

         if (var1.id == 201) {
            ShaderOption[] var6 = Shaders.getChangedOptions(Shaders.getShaderPackOptions());

            for(int var7 = 0; var7 < var6.length; ++var7) {
               ShaderOption var4 = var6[var7];
               var4.resetValue();
               this.changed = true;
            }

            this.updateAllButtons();
         }

         if (var1.id == 200) {
            if (this.changed) {
               Shaders.saveShaderPackOptions();
               this.changed = false;
               Shaders.uninit();
            }

            this.mc.displayGuiScreen(this.prevScreen);
         }
      }

   }

   protected void actionPerformedRightClick(GuiButton var1) {
      if (var1 instanceof GuiButtonShaderOption) {
         GuiButtonShaderOption var2 = (GuiButtonShaderOption)var1;
         ShaderOption var3 = var2.getShaderOption();
         if (isShiftKeyDown()) {
            var3.resetValue();
         } else {
            var3.prevValue();
         }

         this.updateAllButtons();
         this.changed = true;
      }

   }

   public void onGuiClosed() {
      super.onGuiClosed();
      if (this.changed) {
         Shaders.saveShaderPackOptions();
         this.changed = false;
         Shaders.uninit();
      }

   }

   private void updateAllButtons() {
      Iterator var2 = this.buttonList.iterator();

      while(var2.hasNext()) {
         GuiButton var1 = (GuiButton)var2.next();
         if (var1 instanceof GuiButtonShaderOption) {
            GuiButtonShaderOption var3 = (GuiButtonShaderOption)var1;
            ShaderOption var4 = var3.getShaderOption();
            if (var4 instanceof ShaderOptionProfile) {
               ShaderOptionProfile var5 = (ShaderOptionProfile)var4;
               var5.updateProfile();
            }

            var3.displayString = this.getButtonText(var4, var3.getButtonWidth());
         }
      }

   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      if (this.screenText != null) {
         this.drawCenteredString(this.fontRendererObj, this.screenText, width / 2, 15, 16777215);
      } else {
         this.drawCenteredString(this.fontRendererObj, this.title, width / 2, 15, 16777215);
      }

      super.drawScreen(var1, var2, var3);
      if (Math.abs(var1 - this.lastMouseX) <= 5 && Math.abs(var2 - this.lastMouseY) <= 5) {
         this.drawTooltips(var1, var2, this.buttonList);
      } else {
         this.lastMouseX = var1;
         this.lastMouseY = var2;
         this.mouseStillTime = System.currentTimeMillis();
      }

   }

   private void drawTooltips(int var1, int var2, List var3) {
      short var4 = 700;
      if (System.currentTimeMillis() >= this.mouseStillTime + (long)var4) {
         int var5 = width / 2 - 150;
         int var6 = height / 6 - 7;
         if (var2 <= var6 + 98) {
            var6 += 105;
         }

         int var7 = var5 + 150 + 150;
         int var8 = var6 + 84 + 10;
         GuiButton var9 = getSelectedButton(var3, var1, var2);
         if (var9 instanceof GuiButtonShaderOption) {
            GuiButtonShaderOption var10 = (GuiButtonShaderOption)var9;
            ShaderOption var11 = var10.getShaderOption();
            String[] var12 = this.makeTooltipLines(var11, var7 - var5);
            if (var12 == null) {
               return;
            }

            drawGradientRect((double)var5, (double)var6, (float)var7, (float)var8, -536870912, -536870912);

            for(int var13 = 0; var13 < var12.length; ++var13) {
               String var14 = var12[var13];
               int var15 = 14540253;
               if (var14.endsWith("!")) {
                  var15 = 16719904;
               }

               this.fontRendererObj.drawStringWithShadow(var14, (float)(var5 + 5), (float)(var6 + 5 + var13 * 11), var15);
            }
         }
      }

   }

   private String[] makeTooltipLines(ShaderOption var1, int var2) {
      if (var1 instanceof ShaderOptionProfile) {
         return null;
      } else {
         String var3 = var1.getNameText();
         String var4 = Config.normalize(var1.getDescriptionText()).trim();
         String[] var5 = this.splitDescription(var4);
         String var6 = null;
         if (!var3.equals(var1.getName()) && this.settings.advancedItemTooltips) {
            var6 = "§8" + Lang.get("of.general.id") + ": " + var1.getName();
         }

         String var7 = null;
         if (var1.getPaths() != null && this.settings.advancedItemTooltips) {
            var7 = "§8" + Lang.get("of.general.from") + ": " + Config.arrayToString((Object[])var1.getPaths());
         }

         String var8 = null;
         if (var1.getValueDefault() != null && this.settings.advancedItemTooltips) {
            String var9 = var1.isEnabled() ? var1.getValueText(var1.getValueDefault()) : Lang.get("of.general.ambiguous");
            var8 = "§8" + Lang.getDefault() + ": " + var9;
         }

         ArrayList var11 = new ArrayList();
         var11.add(var3);
         var11.addAll(Arrays.asList(var5));
         if (var6 != null) {
            var11.add(var6);
         }

         if (var7 != null) {
            var11.add(var7);
         }

         if (var8 != null) {
            var11.add(var8);
         }

         String[] var10 = this.makeTooltipLines(var2, var11);
         return var10;
      }
   }

   private String[] splitDescription(String var1) {
      if (var1.length() <= 0) {
         return new String[0];
      } else {
         var1 = StrUtils.removePrefix(var1, "//");
         String[] var2 = var1.split("\\. ");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = "- " + var2[var3].trim();
            var2[var3] = StrUtils.removeSuffix(var2[var3], ".");
         }

         return var2;
      }
   }

   private String[] makeTooltipLines(int var1, List var2) {
      Config.getMinecraft();
      FontRenderer var3 = Minecraft.fontRendererObj;
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < var2.size(); ++var5) {
         String var6 = (String)var2.get(var5);
         if (var6 != null && var6.length() > 0) {
            Iterator var8 = var3.listFormattedStringToWidth(var6, var1).iterator();

            while(var8.hasNext()) {
               Object var7 = var8.next();
               var4.add((String)var7);
            }
         }
      }

      String[] var9 = (String[])var4.toArray(new String[var4.size()]);
      return var9;
   }
}
