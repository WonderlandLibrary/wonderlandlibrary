package cn.hanabi.gui.newclickui;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.font.noway.ttfr.HFontRenderer;
import cn.hanabi.gui.newclickui.impl.BoolValue;
import cn.hanabi.gui.newclickui.impl.DoubleValue;
import cn.hanabi.gui.newclickui.impl.ModeValue;
import cn.hanabi.gui.newclickui.impl.Panel;
import cn.hanabi.gui.newclickui.misc.ClickEffect;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.BlurUtil;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.utils.TranslateUtil;
import cn.hanabi.utils.fontmanager.HanabiFonts;
import cn.hanabi.value.Value;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ClickUI extends GuiScreen {
   public Panel combat;
   public Panel movement;
   public Panel player;
   public Panel render;
   public Panel world;
   public Panel ghost;
   List panels = new ArrayList();
   List clickEffects = new ArrayList();
   public static Mod currentMod;
   public static int real = Mouse.getDWheel();
   public static int settingwheel;
   public static TranslateUtil settingtranslate = new TranslateUtil(0.0F, 0.0F);
   public static TranslateUtil animatranslate = new TranslateUtil(0.0F, 0.0F);
   public ArrayList modBooleanValue = new ArrayList();
   public ArrayList modModeValue = new ArrayList();
   public ArrayList modDoubleValue = new ArrayList();
   public static Map booleanValueMap = new HashMap();
   public static Map doubleValueMap = new HashMap();
   public static Map modeValueMap = new HashMap();
   public TimeHelper timer = new TimeHelper();
   public String input;
   public static String searchcontent;
   public static boolean isSearching;

   public ClickUI() {
      currentMod = null;

      for(int i = 0; i < Category.values().length; ++i) {
         Panel panel = new Panel((float)(10 + 150 * i), 50.0F, 100L * (long)(i + 1), Category.values()[i]);
         this.panels.add(panel);
      }

      this.input = "";
      searchcontent = "";
      isSearching = false;
   }

   public void initGui() {
      if (searchcontent.equalsIgnoreCase("") || searchcontent == null) {
         isSearching = false;
      }

      this.clickEffects.clear();
      if (currentMod == null && !isSearching) {
         Iterator var1 = this.panels.iterator();

         while(var1.hasNext()) {
            Panel panel = (Panel)var1.next();
            panel.resetAnimation();
         }

         animatranslate.setXY(0.0F, 0.0F);
      }

      Keyboard.enableRepeatEvents(true);
   }

   public void drawScreen(int mouseX, int mouseY, float par) {
      if (this.timer.isDelayComplete(500L)) {
         this.input = this.input.equals("_") ? "" : "_";
         this.timer.reset();
      }

      RenderUtil.drawRect(0.0F, 0.0F, (float)this.width, (float)this.height, (new Color(255, 255, 255, 30)).getRGB());
      real = Mouse.getDWheel();
      Iterator var4 = this.panels.iterator();

      while(var4.hasNext()) {
         Panel panel = (Panel)var4.next();
         panel.drawShadow((float)mouseX, (float)mouseY);
      }

      RenderUtil.drawRect((float)(this.width / 2 - 60), 0.0F, (float)(this.width / 2 + 60), 20.0F, (new Color(0, 0, 0, 120)).getRGB());
      if (!searchcontent.equalsIgnoreCase("") && searchcontent != null) {
         isSearching = true;
      }

      HFontRenderer font = Hanabi.INSTANCE.fontManager.wqy18;
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.startGlScissor(this.width / 2 - 55, 0, 110, 20);
      if (isSearching) {
         font.drawString(searchcontent + this.input, (float)Math.min(this.width / 2 - 55, this.width / 2 + 50 - font.getStringWidth(searchcontent)), 4.0F, (new Color(255, 255, 255, 255)).getRGB());
      } else {
         font.drawString("Search", (float)this.width / 2.0F - 55.0F, 4.0F, (new Color(180, 180, 180, 255)).getRGB());
      }

      RenderUtil.stopGlScissor();
      GL11.glDisable(3089);
      GL11.glPopMatrix();
      RenderUtil.drawRect((float)(this.width / 2 + 80), 0.0F, (float)(this.width / 2 + 130), 20.0F, (new Color(0, 0, 0, 120)).getRGB());
      Hanabi.INSTANCE.fontManager.wqy18.drawCenteredString("Reset Gui", (float)(this.width / 2 + 105), 4.0F, (new Color(255, 255, 255, 255)).getRGB());
      Hanabi.INSTANCE.fontManager.usans30.drawCenteredString("Hanabi Build 3.1", (float)(this.width / 2), (float)(this.height - 20), (new Color(0, 0, 0, 255)).getRGB());
      Iterator clickEffectIterator;
      if (this.clickEffects.size() > 0) {
         clickEffectIterator = this.clickEffects.iterator();

         while(clickEffectIterator.hasNext()) {
            ClickEffect clickEffect = (ClickEffect)clickEffectIterator.next();
            clickEffect.draw();
            if (clickEffect.canRemove()) {
               clickEffectIterator.remove();
            }
         }
      }

      BlurUtil.doBlur(7.0F);
      Hanabi.INSTANCE.fontManager.usans30.drawCenteredString("Hanabi Build 3.1", (float)(this.width / 2), (float)(this.height - 20), (new Color(255, 255, 255, 255)).getRGB());
      clickEffectIterator = this.panels.iterator();

      while(clickEffectIterator.hasNext()) {
         Panel panel = (Panel)clickEffectIterator.next();
         panel.draw((float)mouseX, (float)mouseY);
      }

      boolean searchHover = isHover((float)mouseX, (float)mouseY, (float)(this.width / 2 - 60), 0.0F, (float)(this.width / 2 + 60), 20.0F) && currentMod == null;
      RenderUtil.drawRect((float)(this.width / 2 - 60), 0.0F, (float)(this.width / 2 + 60), 20.0F, (new Color(0, 0, 0, searchHover ? 80 : 60)).getRGB());
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.startGlScissor(this.width / 2 - 55, 0, 110, 20);
      if (isSearching) {
         font.drawString(searchcontent + this.input, (float)Math.min(this.width / 2 - 55, this.width / 2 + 50 - font.getStringWidth(searchcontent)), 4.0F, (new Color(255, 255, 255, 255)).getRGB());
      } else {
         font.drawString("Search", (float)(this.width / 2 - 55), 4.0F, (new Color(180, 180, 180, 255)).getRGB());
      }

      RenderUtil.stopGlScissor();
      GL11.glDisable(3089);
      GL11.glPopMatrix();
      boolean resetHover = isHover((float)mouseX, (float)mouseY, (float)(this.width / 2 + 80), 0.0F, (float)(this.width / 2 + 130), 20.0F) && currentMod == null;
      RenderUtil.drawRect((float)(this.width / 2 + 80), 0.0F, (float)(this.width / 2 + 130), 20.0F, (new Color(0, 0, 0, resetHover ? 80 : 60)).getRGB());
      Hanabi.INSTANCE.fontManager.wqy18.drawCenteredString("Reset Gui", (float)(this.width / 2 + 105), 4.0F, (new Color(255, 255, 255, 255)).getRGB());
      if (currentMod != null) {
         animatranslate.interpolate(0.0F, (float)this.height, 0.2F);
      } else {
         animatranslate.interpolate(0.0F, 0.0F, 0.4F);
      }

      if (animatranslate.getY() > 0.0F) {
         float startX = (float)(this.width / 2 - 120);
         float startY = (float)(this.height + this.height / 2 - 140) - animatranslate.getY();
         RenderUtil.drawRoundRect((double)startX, (double)startY, (double)(startX + 240.0F), (double)(startY + 280.0F), 4, (new Color(30, 30, 30, 255)).getRGB());
         BlurUtil.doBlur(7.0F);
         RenderUtil.drawRoundRect((double)startX, (double)startY, (double)(startX + 240.0F), (double)(startY + 280.0F), 4, (new Color(30, 30, 30, 255)).getRGB());
         if (currentMod != null) {
            Hanabi.INSTANCE.fontManager.usans25.drawString(currentMod.getName(), startX + 15.0F, startY + 12.0F, (new Color(255, 255, 255, 255)).getRGB());
            String iconstr = "";
            switch (currentMod.getCategory().toString()) {
               case "Combat":
                  iconstr = HanabiFonts.ICON_CLICKGUI_COMBAT;
                  break;
               case "Movement":
                  iconstr = HanabiFonts.ICON_CLICKGUI_MOVEMENT;
                  break;
               case "Player":
                  iconstr = HanabiFonts.ICON_CLICKGUI_PLAYER;
                  break;
               case "Render":
                  iconstr = HanabiFonts.ICON_CLICKGUI_RENDER;
                  break;
               case "World":
                  iconstr = HanabiFonts.ICON_CLICKGUI_WORLD;
                  break;
               case "Ghost":
                  iconstr = HanabiFonts.ICON_CLICKGUI_GHOST;
            }

            Hanabi.INSTANCE.fontManager.icon30.drawString(iconstr, startX + 210.0F, startY + 12.0F, (new Color(255, 255, 255, 255)).getRGB());
            BlurUtil.doBlur(startX + 10.0F, startY + 5.0F, 220.0F, 270.0F, 7.0F, 0.0F, 1.0F);
            this.drawValue((float)mouseX, (float)mouseY, startX, startY);
         }
      }

      if (this.clickEffects.size() > 0) {
         Iterator clickEffectIterator = this.clickEffects.iterator();

         while(clickEffectIterator.hasNext()) {
            ClickEffect clickEffect = (ClickEffect)clickEffectIterator.next();
            clickEffect.draw();
            if (clickEffect.canRemove()) {
               clickEffectIterator.remove();
            }
         }
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int key) {
      ClickEffect clickEffect = new ClickEffect((float)mouseX, (float)mouseY);
      this.clickEffects.add(clickEffect);
      Iterator var5 = this.panels.iterator();

      while(var5.hasNext()) {
         Panel panel = (Panel)var5.next();
         panel.handleMouseClicked((float)mouseX, (float)mouseY, key);
      }

      boolean searchHover = isHover((float)mouseX, (float)mouseY, (float)(this.width / 2 - 60), 0.0F, (float)(this.width / 2 + 60), 20.0F) && currentMod == null;
      if (searchHover && key == 0) {
         isSearching = true;
      }

      if (!searchHover && key == 0 && (searchcontent.equalsIgnoreCase("") || searchcontent == null)) {
         isSearching = false;
      }

      boolean resetHover = isHover((float)mouseX, (float)mouseY, (float)(this.width / 2 + 80), 0.0F, (float)(this.width / 2 + 130), 20.0F) && currentMod == null;
      Iterator var7;
      if (resetHover) {
         var7 = this.panels.iterator();

         Panel panel;
         while(var7.hasNext()) {
            panel = (Panel)var7.next();
            panel.setXY((float)(10 + 150 * this.panels.indexOf(panel)), 50.0F);
         }

         var7 = this.panels.iterator();

         while(var7.hasNext()) {
            panel = (Panel)var7.next();
            panel.resetAnimation();
         }
      }

      if (currentMod != null && key == 0) {
         float startX = (float)(this.width / 2 - 120);
         float startY = (float)(this.height + this.height / 2 - 140) - animatranslate.getY();
         boolean valueHover = isHover((float)mouseX, (float)mouseY, startX, startY, startX + 240.0F, startY + 280.0F);
         if (!valueHover) {
            currentMod = null;
         }
      }

      if (currentMod != null) {
         var7 = this.modBooleanValue.iterator();

         Value values2;
         while(var7.hasNext()) {
            values2 = (Value)var7.next();
            if (booleanValueMap.containsKey(values2)) {
               BoolValue o = (BoolValue)booleanValueMap.get(values2);
               o.handleMouse((float)mouseX, (float)mouseY, key);
            }
         }

         var7 = this.modModeValue.iterator();

         while(var7.hasNext()) {
            values2 = (Value)var7.next();
            if (modeValueMap.containsKey(values2)) {
               ModeValue o = (ModeValue)modeValueMap.get(values2);
               o.handleMouse((float)mouseX, (float)mouseY, key);
            }
         }

         var7 = this.modDoubleValue.iterator();

         while(var7.hasNext()) {
            values2 = (Value)var7.next();
            if (doubleValueMap.containsKey(values2)) {
               DoubleValue o = (DoubleValue)doubleValueMap.get(values2);
               o.handleMouse((float)mouseX, (float)mouseY, key);
            }
         }
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int key) {
      Iterator var4 = this.panels.iterator();

      while(var4.hasNext()) {
         Panel panel = (Panel)var4.next();
         panel.handleMouseReleased((float)mouseX, (float)mouseY, key);
      }

   }

   protected void keyTyped(char typedChar, int keyCode) {
      if (keyCode == 1) {
         this.mc.displayGuiScreen((GuiScreen)null);
      }

      Iterator var3;
      Panel panel;
      if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && isSearching) {
         searchcontent = searchcontent + typedChar;
         var3 = this.panels.iterator();

         while(var3.hasNext()) {
            panel = (Panel)var3.next();
            panel.resetTranslate();
         }
      }

      if (keyCode == 47 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown() && isSearching) {
         searchcontent = searchcontent + GuiScreen.getClipboardString();
         var3 = this.panels.iterator();

         while(var3.hasNext()) {
            panel = (Panel)var3.next();
            panel.resetTranslate();
         }
      }

      if (keyCode == 14 && isSearching) {
         int length = searchcontent.length();
         if (length != 0) {
            searchcontent = searchcontent.substring(0, length - 1);
            Iterator var7 = this.panels.iterator();

            while(var7.hasNext()) {
               Panel panel = (Panel)var7.next();
               panel.resetTranslate();
            }
         }
      }

   }

   public void onGuiClosed() {
      try {
         Hanabi.INSTANCE.fileManager.save();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      Keyboard.enableRepeatEvents(false);
      super.onGuiClosed();
   }

   public static boolean isHover(float mouseX, float mouseY, float x1, float y1, float x2, float y2) {
      return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
   }

   public void drawValue(float mouseX, float mouseY, float startX, float startY) {
      float vstartY = startY + 40.0F;
      Hanabi.INSTANCE.fontManager.usans25.drawString(currentMod.getName(), startX + 15.0F, startY + 12.0F, (new Color(255, 255, 255, 255)).getRGB());
      String iconstr = "";
      switch (currentMod.getCategory().toString()) {
         case "Combat":
            iconstr = HanabiFonts.ICON_CLICKGUI_COMBAT;
            break;
         case "Movement":
            iconstr = HanabiFonts.ICON_CLICKGUI_MOVEMENT;
            break;
         case "Player":
            iconstr = HanabiFonts.ICON_CLICKGUI_PLAYER;
            break;
         case "Render":
            iconstr = HanabiFonts.ICON_CLICKGUI_RENDER;
            break;
         case "World":
            iconstr = HanabiFonts.ICON_CLICKGUI_WORLD;
            break;
         case "Ghost":
            iconstr = HanabiFonts.ICON_CLICKGUI_GHOST;
      }

      Hanabi.INSTANCE.fontManager.icon30.drawString(iconstr, startX + 210.0F, startY + 12.0F, (new Color(255, 255, 255, 255)).getRGB());
      float valueY = settingtranslate.getX();
      if (!this.modBooleanValue.isEmpty()) {
         this.modBooleanValue.clear();
      }

      if (!this.modModeValue.isEmpty()) {
         this.modModeValue.clear();
      }

      if (!this.modDoubleValue.isEmpty()) {
         this.modDoubleValue.clear();
      }

      Iterator var14 = Value.list.iterator();

      Value values2;
      while(var14.hasNext()) {
         values2 = (Value)var14.next();
         if (values2.getValueName().split("_")[0].equalsIgnoreCase(currentMod.getName())) {
            Mod curMod = currentMod;
            ++curMod.valueSize;
            if (values2.isValueDouble) {
               this.modDoubleValue.add(values2);
            }

            if (values2.isValueMode) {
               this.modModeValue.add(values2);
            }

            if (values2.isValueBoolean) {
               this.modBooleanValue.add(values2);
            }
         }
      }

      GL11.glPushMatrix();
      RenderUtil.startGlScissor((int)startX, (int)vstartY, 240, 235);

      ModeValue o;
      for(var14 = this.modModeValue.iterator(); var14.hasNext(); valueY += o.getLength()) {
         values2 = (Value)var14.next();
         if (modeValueMap.containsKey(values2)) {
            o = (ModeValue)modeValueMap.get(values2);
         } else {
            o = new ModeValue(values2);
            modeValueMap.put(values2, o);
         }

         o.draw(startX, vstartY + valueY, mouseX, mouseY);
      }

      DoubleValue o;
      for(var14 = this.modDoubleValue.iterator(); var14.hasNext(); valueY += o.getLength()) {
         values2 = (Value)var14.next();
         if (doubleValueMap.containsKey(values2)) {
            o = (DoubleValue)doubleValueMap.get(values2);
         } else {
            o = new DoubleValue(values2);
            doubleValueMap.put(values2, o);
         }

         o.draw(startX, vstartY + valueY, mouseX, mouseY);
         o.handleMouseinRender(mouseX, mouseY, 1);
      }

      BoolValue o;
      for(var14 = this.modBooleanValue.iterator(); var14.hasNext(); valueY += o.getLength()) {
         values2 = (Value)var14.next();
         if (booleanValueMap.containsKey(values2)) {
            o = (BoolValue)booleanValueMap.get(values2);
         } else {
            o = new BoolValue(values2);
            booleanValueMap.put(values2, o);
         }

         o.draw(startX, vstartY + valueY, mouseX, mouseY);
      }

      RenderUtil.stopGlScissor();
      GL11.glPopMatrix();
      float moduleHeight = valueY - settingtranslate.getX() - 1.0F;
      if (Mouse.hasWheel() && isHover(mouseX, mouseY, startX, startY, startX + 240.0F, vstartY + 235.0F) && currentMod != null) {
         int i;
         if (real > 0 && settingwheel < 0) {
            for(i = 0; i < 10 && settingwheel < 0; ++i) {
               settingwheel += 5;
            }
         } else {
            for(i = 0; i < 10 && real < 0 && moduleHeight > 235.0F && (float)Math.abs(settingwheel) < moduleHeight - 235.0F; ++i) {
               settingwheel -= 5;
            }
         }
      }

      settingtranslate.interpolate((float)settingwheel, 0.0F, 0.2F);
      float sliderh = Math.min(235.0F, 55225.0F / moduleHeight);
      float slidert = -(235.0F - sliderh) * settingtranslate.getX() / (moduleHeight - 235.0F);
      if (sliderh < 235.0F) {
         GL11.glPushMatrix();
         GL11.glEnable(3089);
         RenderUtil.doGlScissor((int)startX + 229, (int)vstartY, 1, 235);
         RenderUtil.drawRect(startX + 229.0F, vstartY + slidert, startX + 230.0F, vstartY + slidert + sliderh, (new Color(255, 255, 255, 255)).getRGB());
         GL11.glDisable(3089);
         GL11.glPopMatrix();
      }

   }
}
