package de.violence.gui.mains;

import de.violence.Violence;
import de.violence.font.FontManager;
import de.violence.gui.VSetting;
import de.violence.gui.mains.Component;
import de.violence.mcgui.utils.GuiMains;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import de.violence.ui.Colours;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

public class Button {
   private Module module;
   public int x;
   public int y;
   private Minecraft mc = Minecraft.getMinecraft();
   public int width;
   public int heigth;
   private boolean hovered;
   private boolean extend;
   private boolean keybindChange;
   public int componentWidth;
   public int defaultWidth;
   private List componentList = new ArrayList();

   public List getComponentList() {
      return this.componentList;
   }

   public boolean isExtend() {
      return this.extend;
   }

   public Button(Module module, int width) {
      this.module = module;
      this.width = width;
      this.heigth = 10;
      this.componentWidth = this.getComponentWidth();
      this.defaultWidth = this.getComponentWidth();
      this.initComponents();
      this.rescaleComponents();
   }

   private int getComponentWidth() {
      int w = 0;
      Iterator var3 = VSetting.getSettings().iterator();

      while(true) {
         VSetting vSetting;
         do {
            do {
               if(!var3.hasNext()) {
                  return w;
               }

               vSetting = (VSetting)var3.next();
            } while(vSetting.getModule() != this.module);

            w = Math.max(w, FontManager.clickGUI.getStringWidth(vSetting.getName()));
            if(vSetting.getSettingType() == VSetting.SettingType.SLIDER) {
               w += 2;
            }

            if(vSetting.getSettingType() == VSetting.SettingType.BUTTON) {
               w += 2;
            }
         } while(vSetting.getSettingType() != VSetting.SettingType.MODE);

         String modes;
         for(Iterator var5 = vSetting.getModeList().iterator(); var5.hasNext(); w = Math.max(w, FontManager.clickGUI.getStringWidth(modes)) + 1) {
            modes = (String)var5.next();
         }
      }
   }

   private void initComponents() {
      Iterator var2 = VSetting.getSettings().iterator();

      while(var2.hasNext()) {
         VSetting vSetting = (VSetting)var2.next();
         if(vSetting.getModule() == this.module) {
            this.componentList.add(new Component(this, vSetting, this.getComponentWidth()));
         }
      }

   }

   private void hideSettings(Component component, String hideSettings) {
      if(component.getVSetting().getSettingType() == VSetting.SettingType.BUTTON) {
         VSetting setting = VSetting.getByName(hideSettings.split("-")[0].replace("!", ""), Module.getByName(hideSettings.split("-")[1]));
         if(hideSettings.split("-")[0].startsWith("!")) {
            if(setting != null) {
               setting.show = !component.getVSetting().isToggled() && component.getVSetting().show;
            }
         } else if(setting != null) {
            setting.show = component.getVSetting().isToggled() && component.getVSetting().show;
         }
      } else if(component.getVSetting().getSettingType() == VSetting.SettingType.MODE) {
         String setting1 = hideSettings.split("-")[0];
         String module = hideSettings.split("-")[1];
         String mode = hideSettings.split("-")[2];
         VSetting vSetting = VSetting.getByName(setting1.replace("!", ""), Module.getByName(module));
         if(setting1.startsWith("!")) {
            if(vSetting != null) {
               vSetting.show = !component.getVSetting().getActiveMode().equalsIgnoreCase(mode);
            }
         } else if(vSetting != null) {
            vSetting.show = component.getVSetting().getActiveMode().equalsIgnoreCase(mode);
         }
      }

   }

   private void rescaleComponents() {
      int y = this.y + 10;
      int x = this.x;
      Iterator var4 = this.componentList.iterator();

      while(var4.hasNext()) {
         Component components = (Component)var4.next();
         components.x = x;
         components.y = y;
         components.width = this.width - 10;
         Iterator var6 = components.getVSetting().hideSettings.iterator();

         while(var6.hasNext()) {
            String settingType = (String)var6.next();
            this.hideSettings(components, settingType);
         }

         if(components.getVSetting().show) {
            VSetting.SettingType settingType1 = components.getVSetting().getSettingType();
            if(settingType1 == VSetting.SettingType.BUTTON) {
               y += 8;
            } else if(settingType1 == VSetting.SettingType.SLIDER) {
               y += 11;
            } else if(settingType1 == VSetting.SettingType.MODE) {
               if(components.getVSetting().extend) {
                  y += components.getVSetting().getModeList().size() * 9 + 10;
               } else {
                  y += 9;
               }
            }
         }
      }

   }

   private void checkForKeybindEvent() {
      if(this.keybindChange) {
         while(Keyboard.next()) {
            int k = Keyboard.getEventKey() == 0?Keyboard.getEventCharacter() + 256:Keyboard.getEventKey();
            if(k == 1) {
               this.keybindChange = false;
               return;
            }

            if(k == 57) {
               k = 0;
            }

            this.module.setKeybind(k);
            Violence.getViolence().sendChat("Keybind from " + this.module.getName() + " setted to " + Keyboard.getKeyName(k));
            this.keybindChange = false;
         }
      }

   }

   private void renderComponents(int mouseX, int mouseY) {
      if(this.extend) {
         Iterator var4 = this.componentList.iterator();

         while(var4.hasNext()) {
            Component components = (Component)var4.next();
            if(components.getVSetting().show) {
               components.onRender(mouseX, mouseY);
            }
         }
      }

   }

   public void onRender(int mouseX, int mouseY) {
      this.hovered = GuiMains.isHovered(mouseX, mouseY, this.x, this.y, this.width, this.heigth - 4);
      String name = this.module.getName();
      if(this.keybindChange) {
         name = "Waiting";
      }

      this.rescaleComponents();
      this.renderComponents(mouseX, mouseY);
      this.checkForKeybindEvent();
      GlStateManager.pushMatrix();
      GlStateManager.disableTexture2D();
      GuiScreen var10000 = this.mc.currentScreen;
      GuiScreen.drawRect(this.x, this.y, this.x + this.width, this.y + this.heigth, Colours.getColor(0, 0, 0, 255));
      int color = Integer.MIN_VALUE;
      if(this.getModule().isToggled()) {
         color = Colours.getLighterMain(255);
      }

      var10000 = this.mc.currentScreen;
      GuiScreen.drawRect(this.x + 1, this.y, this.x + this.width - 1, this.y + this.heigth, color);
      GlStateManager.enableTexture2D();
      GlStateManager.popMatrix();
      int colorString = -1;
      if(this.hovered) {
         colorString = Colours.getColor(255, 170, 0, 255);
      }

      FontManager.clickGUI.drawCenteredString(name, this.x + this.width / 2, this.y, colorString);
      if(this.isExtend()) {
         FontManager.clickGUI.drawString("-", this.x + this.width - 5, this.y, -1);
      }

   }

   public void onClick(int mouseX, int mouseY, int mouseButton) {
      if(this.hovered) {
         if(mouseButton == 0) {
            if(this.module.getCategory() == Category.SETTINGS) {
               return;
            }

            this.module.onToggle();
         } else if(mouseButton == 1) {
            if(!this.componentList.isEmpty()) {
               this.extend = !this.extend;
            }
         } else if(mouseButton == 2) {
            if(this.module.getCategory() == Category.SETTINGS) {
               return;
            }

            if(!this.keybindChange) {
               this.keybindChange = true;
            }
         }
      }

      if(this.extend) {
         Iterator var5 = this.componentList.iterator();

         while(var5.hasNext()) {
            Component components = (Component)var5.next();
            components.onClick(mouseX, mouseY, mouseButton);
         }
      }

   }

   public Module getModule() {
      return this.module;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeigth() {
      return this.heigth;
   }
}
