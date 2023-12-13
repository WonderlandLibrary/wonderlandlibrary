package team.massacre.utils;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import team.massacre.Massacre;
import team.massacre.api.manager.FontManager;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.impl.event.EventKeyPress;
import team.massacre.impl.module.render.Hud;

public class HudUtils {
   private Minecraft mc = Minecraft.getMinecraft();
   private int tab;
   private boolean expanded;

   public void drawBuild(ScaledResolution scaledResolution, DecimalFormat decimalFormat, String bps) {
      if (this.mc.currentScreen instanceof GuiChat) {
         Massacre.INSTANCE.getFontManager().getLatoRegularMedium().drawStringWithShadow("§f§l" + Massacre.INSTANCE.clientBuild + "§7| User: " + Massacre.INSTANCE.bloodyHell, (float)scaledResolution.getScaledWidth() - Massacre.INSTANCE.getFontManager().getLatoRegularMedium().getWidth("§f§l" + Massacre.INSTANCE.clientBuild + "§7| User: " + Massacre.INSTANCE.bloodyHell) + 6.0F, (float)scaledResolution.getScaledHeight() - Massacre.INSTANCE.getFontManager().getLatoRegularMedium().getHeight("§f§l" + Massacre.INSTANCE.clientBuild + "§7| User: " + Massacre.INSTANCE.bloodyHell) - 16.0F, -1);
         Massacre.INSTANCE.getFontManager().getLatoRegularMedium().drawStringWithShadow("FPS: §f" + Minecraft.getDebugFPS() + " §7BPS: §f" + bps + " §7X: §f" + decimalFormat.format(this.mc.thePlayer.posX) + " §7Y: §f" + decimalFormat.format(this.mc.thePlayer.posY) + " §7Z: §f" + decimalFormat.format(this.mc.thePlayer.posZ), 2.0F, (float)scaledResolution.getScaledHeight() - Massacre.INSTANCE.getFontManager().getLatoRegularMedium().getHeight("FPS: §f" + Minecraft.getDebugFPS() + " X: §f" + decimalFormat.format(this.mc.thePlayer.posX) + " Y: §f" + decimalFormat.format(this.mc.thePlayer.posY) + " Z: §f" + decimalFormat.format(this.mc.thePlayer.posZ)) - 16.0F, Color.lightGray.getRGB());
      } else {
         Massacre.INSTANCE.getFontManager().getLatoRegularMedium().drawStringWithShadow("§f§l" + Massacre.INSTANCE.clientBuild + "§7| User: " + Massacre.INSTANCE.bloodyHell, (float)scaledResolution.getScaledWidth() - Massacre.INSTANCE.getFontManager().getLatoRegularMedium().getWidth("§f§l" + Massacre.INSTANCE.clientBuild + "§7| User: " + Massacre.INSTANCE.bloodyHell) + 6.0F, (float)scaledResolution.getScaledHeight() - Massacre.INSTANCE.getFontManager().getLatoRegularMedium().getHeight("§f§l" + Massacre.INSTANCE.clientBuild + "§7| User: " + Massacre.INSTANCE.bloodyHell) - 3.0F, -1);
         Massacre.INSTANCE.getFontManager().getLatoRegularMedium().drawStringWithShadow("X: §f" + decimalFormat.format(this.mc.thePlayer.posX) + " §7Y: §f" + decimalFormat.format(this.mc.thePlayer.posY) + " §7Z: §f" + decimalFormat.format(this.mc.thePlayer.posZ), 2.0F, (float)scaledResolution.getScaledHeight() - Massacre.INSTANCE.getFontManager().getLatoRegularMedium().getHeight("X: §f" + decimalFormat.format(this.mc.thePlayer.posX) + " Y: §f" + decimalFormat.format(this.mc.thePlayer.posY) + " Z: §f" + decimalFormat.format(this.mc.thePlayer.posZ)) - 3.0F, Color.lightGray.getRGB());
         Massacre.INSTANCE.getFontManager().getLatoRegularMedium().drawStringWithShadow("FPS: §f" + Minecraft.getDebugFPS() + " §7BPS: §f" + bps, 2.0F, (float)(scaledResolution.getScaledHeight() - 23), Color.lightGray.getRGB());
      }

   }

   public void drawWatermark(ScaledResolution scaledResolution) {
      this.mc.getTextureManager().bindTexture(new ResourceLocation("massacre/MainLogo.png"));
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      Gui.drawModalRectWithCustomSizedTexture(2, -16, 0.0F, 0.0F, 112, 63, 112.0F, 63.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableAlpha();
      GlStateManager.disableBlend();
   }

   public void drawWatermarkNew(ScaledResolution scaledResolution) {
      FontManager fontManager = Massacre.INSTANCE.getFontManager();
      Hud hud = (Hud)Massacre.INSTANCE.getModuleManager().getModule(Hud.class);
      ColorUtil colorUtil = Massacre.INSTANCE.getColorUtil();
      fontManager.getLatoRegularMainMenu().drawStringWithShadow((Boolean)hud.jinthium.getValue() ? "M§fassacree" : "M§fassacre", 2.0F, 2.0F, hud.colorMode.getValue() == Hud.ColorMode.CLIENT ? colorUtil.getGradientOffset((new Color(225, 225, 225, 65)).brighter(), (new Color(225, 225, 225, 255)).darker().darker(), (double)Math.abs(System.currentTimeMillis() / 8L) / 100.0D).getRGB() : colorUtil.rainbow(-8).getRGB());
   }

   public void drawArraylist(ScaledResolution scaledResolution) {
      Hud hud = (Hud)Massacre.INSTANCE.getModuleManager().getModule(Hud.class);
      ColorUtil colorUtil = Massacre.INSTANCE.getColorUtil();
      float y = 1.0F;
      ArrayList<Module> enabledMods = new ArrayList();

      Iterator var6;
      Module module;
      for(var6 = Massacre.INSTANCE.getModuleManager().getModules().iterator(); var6.hasNext(); enabledMods.sort((m1, m2) -> {
         return (int)(Massacre.INSTANCE.getFontManager().getLatoRegularMedium().getWidth(m2.getDisplayName()) - Massacre.INSTANCE.getFontManager().getLatoRegularMedium().getWidth(m1.getDisplayName()));
      })) {
         module = (Module)var6.next();
         if (module.getState()) {
            enabledMods.add(module);
         }
      }

      for(var6 = enabledMods.iterator(); var6.hasNext(); y += 10.0F) {
         module = (Module)var6.next();
         float width = Massacre.INSTANCE.getFontManager().getLatoRegularMedium().getWidth(module.getDisplayName());
         GuiUtils.drawRect((double)((float)scaledResolution.getScaledWidth() - width - 2.0F), (double)y, (double)scaledResolution.getScaledWidth(), (double)(y + 10.0F), hud.clientTheme.getValue() == Hud.ClientTheme.LIGHT ? (new Color(225, 225, 225, 65)).getRGB() : (new Color(55, 55, 55, 65)).getRGB());
         GuiUtils.drawRect((double)(scaledResolution.getScaledWidth() - 1), (double)y, (double)scaledResolution.getScaledWidth(), (double)(y + 10.0F), hud.colorMode.getValue() == Hud.ColorMode.CLIENT ? colorUtil.getGradientOffset((new Color(225, 225, 225, 65)).brighter(), (new Color(225, 225, 225, 255)).darker().darker(), (double)Math.abs(System.currentTimeMillis() / 8L) / 100.0D + (double)(y / (Massacre.INSTANCE.getFontManager().getLatoRegularMedium().getHeight(module.getDisplayName()) - 54.0F))).getRGB() : colorUtil.rainbow((int)(-y * 8.0F)).getRGB());
         Massacre.INSTANCE.getFontManager().getLatoRegularMedium().drawStringWithShadow(module.getDisplayName(), (float)(scaledResolution.getScaledWidth() - 1) - width, y + 1.0F, hud.colorMode.getValue() == Hud.ColorMode.CLIENT ? colorUtil.getGradientOffset((new Color(225, 225, 225, 65)).brighter(), (new Color(225, 225, 225, 255)).darker().darker(), (double)Math.abs(System.currentTimeMillis() / 8L) / 100.0D + (double)(y / (Massacre.INSTANCE.getFontManager().getLatoRegularMedium().getHeight(module.getDisplayName()) - 54.0F))).getRGB() : colorUtil.rainbow((int)(-y * 8.0F)).getRGB());
      }

   }

   public void drawTabGui(float x, float y) {
      Hud hud = (Hud)Massacre.INSTANCE.getModuleManager().getModule(Hud.class);
      int color = (new Color(32, 127, 223, 255)).getRGB();
      float offset = 0.0F;
      Gui.drawRect(0, 0, 0, 0, 0);
      TTFFontRenderer font = Massacre.INSTANCE.getFontManager().getLatoRegularMedium();
      Category category = Category.values()[this.tab];
      switch((Hud.HudMode)hud.hudMode.getValue()) {
      case OLD:
         GuiUtils.drawRoundedRectSmooth((double)x, (double)y, (double)(x + 68.0F), (double)(y + (float)Category.values().length * 13.0F), 10.0D, hud.clientTheme.getValue() == Hud.ClientTheme.LIGHT ? (new Color(225, 225, 225, 65)).getRGB() : (new Color(5, 5, 5, 65)).getRGB());
         GuiUtils.drawRoundedRectSmooth((double)(x + 2.0F), (double)(y + (float)this.tab * 13.0F + 2.0F), (double)(x + 4.0F), (double)y + 12.5D + (double)((float)this.tab * 13.0F) - 2.0D, 2.0D, hud.clientTheme.getValue() == Hud.ClientTheme.LIGHT ? (new Color(225, 225, 225, 65)).getRGB() : (new Color(5, 5, 5, 65)).getRGB());
         break;
      case NEW:
         GuiUtils.drawRoundedRectSmooth((double)x, (double)y, (double)(x + 58.0F), (double)(y + (float)Category.values().length * 13.0F), 2.0D, hud.clientTheme.getValue() == Hud.ClientTheme.LIGHT ? (new Color(225, 225, 225, 65)).getRGB() : (new Color(5, 5, 5, 65)).getRGB());
         GuiUtils.drawRoundedRectSmooth((double)x, (double)(y + (float)this.tab * 13.0F), (double)(x + 2.0F), (double)y + 12.5D + (double)((float)this.tab * 13.0F), 2.0D, hud.clientTheme.getValue() == Hud.ClientTheme.LIGHT ? (new Color(225, 225, 225, 65)).getRGB() : (new Color(5, 5, 5, 65)).getRGB());
      }

      int count = 0;
      Category[] var9 = Category.values();
      int var10 = var9.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         Category c = var9[var11];
         switch((Hud.HudMode)hud.hudMode.getValue()) {
         case OLD:
            if (c.render == category.render) {
               font.drawStringWithShadow(c.render, x + 6.0F, y + 2.0F + (float)(count * 13), -1);
            } else {
               font.drawStringWithShadow(c.render, x + 2.0F, y + 2.0F + (float)(count * 13), -1);
            }
            break;
         case NEW:
            if (c.render == category.render) {
               font.drawStringWithShadow(c.render, x + 3.0F, y + 2.0F + (float)(count * 13), -1);
            } else {
               font.drawStringWithShadow(c.render, x + 1.0F, y + 2.0F + (float)(count * 13), -1);
            }
         }

         ++count;
      }

      if (this.expanded) {
         List<Module> elementList = Massacre.INSTANCE.getModuleManager().getModulesInCategory(category);
         Module element = (Module)elementList.get(category.elementIndex);
         if (elementList.size() == 0) {
            return;
         }

         switch((Hud.HudMode)hud.hudMode.getValue()) {
         case OLD:
            GuiUtils.drawRoundedRectSmooth((double)(x + 70.0F), (double)y, (double)(x + 70.0F), (double)(y + (float)(elementList.size() * 13)), 10.0D, hud.clientTheme.getValue() == Hud.ClientTheme.LIGHT ? (new Color(225, 225, 225, 65)).getRGB() : (new Color(5, 5, 5, 65)).getRGB());
            GuiUtils.drawRoundedRectSmooth((double)(x + 72.0F), (double)(y + (float)(category.elementIndex * 13) + 2.0F), (double)(x + 74.0F), (double)y + 12.5D + (double)(category.elementIndex * 13) - 2.0D, 2.0D, hud.clientTheme.getValue() == Hud.ClientTheme.LIGHT ? (new Color(225, 225, 225, 65)).getRGB() : (new Color(5, 5, 5, 65)).getRGB());
            break;
         case NEW:
            GuiUtils.drawRoundedRectSmooth((double)(x + 59.0F), (double)y, (double)(x + 124.0F), (double)(y + (float)(elementList.size() * 13)), 2.0D, hud.clientTheme.getValue() == Hud.ClientTheme.LIGHT ? (new Color(225, 225, 225, 65)).getRGB() : (new Color(5, 5, 5, 65)).getRGB());
            GuiUtils.drawRoundedRectSmooth((double)(x + 59.0F), (double)(y + (float)(category.elementIndex * 13)), (double)(x + 61.0F), (double)y + 12.5D + (double)(category.elementIndex * 13), 2.0D, hud.clientTheme.getValue() == Hud.ClientTheme.LIGHT ? (new Color(225, 225, 225, 65)).getRGB() : (new Color(5, 5, 5, 65)).getRGB());
         }

         count = 0;
         new ScaledResolution(this.mc);

         for(Iterator var16 = elementList.iterator(); var16.hasNext(); ++count) {
            Module m = (Module)var16.next();
            switch((Hud.HudMode)hud.hudMode.getValue()) {
            case OLD:
               if (m.getName() != element.getName() && !m.getState()) {
                  font.drawStringWithShadow(m.getName(), x + 72.0F, y + 2.0F + (float)(count * 13), (new Color(255, 255, 255, 255)).getRGB());
               }

               if (m.getName() != element.getName() && m.getState()) {
                  font.drawStringWithShadow(m.getName(), x + 72.0F, y + 2.0F + (float)(count * 13), (new Color(192, 192, 192, 255)).getRGB());
               }

               if (m.getName() == element.getName() && !m.getState()) {
                  font.drawStringWithShadow(m.getName(), x + 76.0F, y + 2.0F + (float)(count * 13), (new Color(255, 255, 255, 255)).getRGB());
               }

               if (m.getName() == element.getName() && m.getState()) {
                  font.drawStringWithShadow(m.getName(), x + 76.0F, y + 2.0F + (float)(count * 13), (new Color(192, 192, 192, 255)).getRGB());
               }
               break;
            case NEW:
               if (m.getName() != element.getName() && !m.getState()) {
                  font.drawStringWithShadow(m.getName(), x + 60.0F, y + 2.0F + (float)(count * 13), (new Color(255, 255, 255, 255)).getRGB());
               }

               if (m.getName() != element.getName() && m.getState()) {
                  font.drawStringWithShadow(m.getName(), x + 60.0F, y + 2.0F + (float)(count * 13), (new Color(192, 192, 192, 255)).getRGB());
               }

               if (m.getName() == element.getName() && !m.getState()) {
                  font.drawStringWithShadow(m.getName(), x + 62.0F, y + 2.0F + (float)(count * 13), (new Color(255, 255, 255, 255)).getRGB());
               }

               if (m.getName() == element.getName() && m.getState()) {
                  font.drawStringWithShadow(m.getName(), x + 62.0F, y + 2.0F + (float)(count * 13), (new Color(192, 192, 192, 255)).getRGB());
               }
            }
         }
      }

   }

   public void getTabGuiKeys(EventKeyPress eventKeyPress) {
      int code = eventKeyPress.key;
      Category category = Category.values()[this.tab];
      List<Module> moduleList = Massacre.INSTANCE.getModuleManager().getModulesInCategory(category);
      if (code == 200) {
         if (!this.expanded) {
            if (this.tab <= 0) {
               this.tab = Category.values().length - 1;
            } else {
               --this.tab;
            }
         } else if (category.elementIndex <= 0) {
            category.elementIndex = moduleList.size() - 1;
         } else {
            --category.elementIndex;
         }
      }

      if (code == 208) {
         if (!this.expanded) {
            if (this.tab > Category.values().length - 2) {
               this.tab = 0;
            } else {
               ++this.tab;
            }
         } else if (category.elementIndex >= moduleList.size() - 1) {
            category.elementIndex = 0;
         } else {
            ++category.elementIndex;
         }
      }

      if (code == 205) {
         if (this.expanded && moduleList.size() != 0) {
            Module module = (Module)moduleList.get(category.elementIndex);
            if (!this.expanded || moduleList.isEmpty() || !((Module)moduleList.get(category.elementIndex)).expanded) {
               module.toggle();
            }
         } else {
            this.expanded = true;
         }
      }

      if (code == 203) {
         if (this.expanded && !moduleList.isEmpty() && ((Module)moduleList.get(category.elementIndex)).expanded) {
            ((Module)moduleList.get(category.elementIndex)).expanded = false;
         } else {
            this.expanded = false;
         }
      }

   }
}
