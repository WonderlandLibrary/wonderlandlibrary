package cn.hanabi.modules;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.font.noway.ttfr.HFontRenderer;
import cn.hanabi.gui.notifications.Notification;
import cn.hanabi.modules.modules.render.HUD;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.SoundFxPlayer;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.utils.Translate;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventManager;
import java.awt.Button;
import java.awt.CheckboxMenuItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;

public abstract class Mod {
   protected static final Minecraft mc = Minecraft.getMinecraft();
   protected static Random rand = new Random();
   static TimeHelper saveTimer = new TimeHelper();
   public String name;
   protected Category category;
   private final boolean canBeEnabled;
   private boolean hidden;
   public String displayName;
   public float posX;
   public float posY;
   public float lastY;
   public float posYRend;
   public float displaywidth;
   public float namewidth;
   public int valueSize;
   public int valueSize1;
   public Button modButton;
   public Translate translate;
   public boolean keepReg;
   public boolean isReg;
   protected boolean state;
   private int keybind;
   private boolean isHidden;
   private CheckboxMenuItem checkboxMenuItem;
   private int color;

   public Mod(String name, Category Category) {
      this(name, Category, true, false, 0);
   }

   public Mod(String name, Category Category, boolean canBeEnabled, boolean hidden, int keybind) {
      this.valueSize = 0;
      this.valueSize1 = 0;
      this.translate = new Translate(0.0F, 0.0F);
      this.keepReg = false;
      this.isReg = false;
      this.name = name;
      this.category = Category;
      this.canBeEnabled = canBeEnabled;
      this.hidden = hidden;
      this.keybind = keybind;
      this.setColor(RenderUtil.createGermanColor());
   }

   public int getColor() {
      return this.color;
   }

   public void setColor(int color) {
      this.color = color;
   }

   public long getCurrentMS() {
      return System.nanoTime() / 1000000L;
   }

   public int randomInt(int number) {
      return number > 0 ? rand.nextInt(number) : 0;
   }

   public String getName() {
      return this.name;
   }

   public Category getCategory() {
      return this.category;
   }

   public boolean isCanBeEnabled() {
      return this.canBeEnabled;
   }

   public boolean isHidden() {
      return !this.hidden;
   }

   public void setHidden(boolean isHidden) {
      this.hidden = isHidden;
   }

   public int getKeybind() {
      return this.keybind;
   }

   public void setKeybind(int keybind) {
      this.keybind = keybind;
   }

   public boolean getState() {
      return this.state;
   }

   public void setState(boolean state) {
      this.setState(state, true);
   }

   public boolean isEnabled() {
      return this.state;
   }

   public void set(boolean state) {
      this.setState(state);
   }

   public void set(boolean state, boolean noti) {
      if (noti) {
         this.setState(state);
      } else {
         try {
            if (mc.thePlayer != null && saveTimer.isDelayComplete(10000L)) {
               Hanabi.INSTANCE.fileManager.save();
               saveTimer.reset();
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         if (state) {
            this.state = true;
            if (mc.thePlayer != null) {
               this.onEnable();
            }

            if (!this.isReg) {
               this.isReg = true;
               EventManager.register(this);
            }
         } else {
            this.state = false;
            if (mc.thePlayer != null) {
               this.onDisable();
            }

            if (!this.keepReg && this.isReg) {
               this.isReg = false;
               EventManager.unregister(this);
            }
         }
      }

   }

   public int getValueSize() {
      List i = new ArrayList();
      Iterator var2 = Value.list.iterator();

      while(var2.hasNext()) {
         Value value = (Value)var2.next();
         String valueMod = value.getValueName().split("_")[0];
         String valueName = value.getValueName().split("_")[1];
         if (valueMod.equalsIgnoreCase(this.getName())) {
            i.add(value);
         }
      }

      return i.size();
   }

   public double getAnimationState(double animation, double finalState, double speed) {
      float add = (float)((double)RenderUtil.delta * speed);
      if (animation < finalState) {
         if (animation + (double)add < finalState) {
            animation += (double)add;
         } else {
            animation = finalState;
         }
      } else if (animation - (double)add > finalState) {
         animation -= (double)add;
      } else {
         animation = finalState;
      }

      return animation;
   }

   public void onRenderArray() {
      HFontRenderer font = HUD.hudMode.isCurrentMode("Classic") ? Hanabi.INSTANCE.fontManager.raleway17 : Hanabi.INSTANCE.fontManager.usans18;
      if (this.namewidth == 0.0F) {
         this.namewidth = (float)font.getStringWidth(this.name);
      }

      if (this.lastY - this.posY > 0.0F) {
         this.posYRend = 14.0F;
      }

      if (this.lastY - this.posY < 0.0F) {
         this.posYRend = -14.0F;
      }

      if (this.posYRend != 0.0F) {
         this.posYRend = (float)RenderUtil.getAnimationStateSmooth(0.0, (double)this.posYRend, (double)(16.0F / (float)Minecraft.getDebugFPS()));
      }

      float modwidth = this.displayName != null ? (float)(font.getStringWidth(this.displayName) + 3 + font.getStringWidth(this.name)) : this.namewidth;
      if (this.isEnabled()) {
         this.posX = (float)RenderUtil.getAnimationStateSmooth((double)modwidth, (double)this.posX, (double)(16.0F / (float)Minecraft.getDebugFPS()));
      } else {
         this.posX = (float)RenderUtil.getAnimationStateSmooth(-15.0, (double)this.posX, (double)(16.0F / (float)Minecraft.getDebugFPS()));
      }

   }

   public void toggleModule() {
      if (this.checkboxMenuItem != null) {
         this.checkboxMenuItem.setState(!this.checkboxMenuItem.getState());
      }

      this.setState(!this.isEnabled());
   }

   public void setCheckboxMenuItem(CheckboxMenuItem checkboxMenuItem) {
      this.checkboxMenuItem = checkboxMenuItem;
   }

   public void setState(boolean state, boolean save) {
      try {
         if (save && mc.thePlayer != null && saveTimer.isDelayComplete(10000L)) {
            Hanabi.INSTANCE.fileManager.save();
            saveTimer.reset();
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      if (state) {
         this.state = true;
         if (mc.thePlayer != null) {
            this.onEnable();
         }

         if (!this.getName().equals("ClickGUI") && !this.getName().equals("HUD") && !this.getName().equals("APaletteGui") && mc.thePlayer != null) {
            if (((HUD)ModManager.getModule("HUD")).sound.isCurrentMode("Custom1")) {
               (new SoundFxPlayer()).playSound(SoundFxPlayer.SoundType.Enable, -9.0F);
            } else if (((HUD)ModManager.getModule("HUD")).sound.isCurrentMode("Custom2")) {
               (new SoundFxPlayer()).playSound(SoundFxPlayer.SoundType.Enable2, -9.0F);
            } else {
               mc.thePlayer.playSound("random.click", 0.2F, 0.6F);
            }

            ClientUtil.sendClientMessage(this.name + " Enabled", Notification.Type.SUCCESS);
         }

         if (!this.isReg) {
            this.isReg = true;
            EventManager.register(this);
         }
      } else {
         this.state = false;
         if (mc.thePlayer != null) {
            this.onDisable();
         }

         if (!this.getName().equals("ClickGUI") && !this.getName().equals("HUD") && !this.getName().equals("APaletteGui")) {
            if (mc.thePlayer != null) {
               if (((HUD)ModManager.getModule("HUD")).sound.isCurrentMode("Custom1")) {
                  (new SoundFxPlayer()).playSound(SoundFxPlayer.SoundType.Disable, -9.0F);
               } else if (((HUD)ModManager.getModule("HUD")).sound.isCurrentMode("Custom2")) {
                  (new SoundFxPlayer()).playSound(SoundFxPlayer.SoundType.Disable2, -9.0F);
               } else {
                  mc.thePlayer.playSound("random.click", 0.2F, 0.5F);
               }
            }

            ClientUtil.sendClientMessage(this.name + " Disabled", Notification.Type.ERROR);
         }

         if (!this.keepReg && this.isReg) {
            this.isReg = false;
            EventManager.unregister(this);
         }
      }

   }

   protected void onEnable() {
   }

   protected void onDisable() {
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void setDisplayName(String displayName) {
      HFontRenderer font = HUD.hudMode.isCurrentMode("Classic") ? Hanabi.INSTANCE.fontManager.raleway17 : Hanabi.INSTANCE.fontManager.usans18;
      if (this.displayName == null) {
         this.displayName = displayName;
         this.displaywidth = (float)font.getStringWidth(displayName);
         this.namewidth = (float)font.getStringWidth(this.name);
         this.posX = -15.0F;
         ModManager.needsort = true;
      }

      if (!this.displayName.equals(displayName)) {
         this.displayName = displayName;
         this.displaywidth = (float)font.getStringWidth(displayName);
         this.namewidth = (float)font.getStringWidth(this.name);
         this.posX = -15.0F;
         ModManager.needsort = true;
      }

   }

   public boolean hasValues() {
      Iterator var1 = Value.list.iterator();

      String name;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         Value value = (Value)var1.next();
         name = value.getValueName().split("_")[0];
      } while(!name.equalsIgnoreCase(this.getName()));

      return true;
   }
}
