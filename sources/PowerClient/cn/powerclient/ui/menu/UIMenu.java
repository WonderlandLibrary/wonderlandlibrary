package me.AveReborn.ui.menu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import me.AveReborn.Client;
import me.AveReborn.ui.menu.UIMenuSlot;
import me.AveReborn.util.Colors;
import me.AveReborn.util.RenderUtil;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import me.AveReborn.util.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class UIMenu {
   private MouseInputHandler handler = new MouseInputHandler(0);
   public float x;
   private int width;
   private int height;
   private ArrayList slots;

   public UIMenu(ArrayList slots) {
      this.slots = slots;
      this.x = 0.0F;
      this.width = 200;
   }

   public void draw(int mouseX, int mouseY) {
      this.width = 150;
      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      UnicodeFontRenderer font = Client.fontManager.tahoma60;
      this.height = res.getScaledHeight();
      Gui.drawRainbowRectVertical((int)this.x, 0, (int)this.x + this.width + 2, this.height, Integer.MAX_VALUE, 1.0F);
      Gui.drawRect(this.x, 0.0F, this.x + (float)this.width, (float)this.height, -14145496);
      if(this.x == (float)(-this.width)) {
         RenderUtil.drawImage(new ResourceLocation("Ave/slidermenu_icon.png"), (int)this.x + this.width + 5, res.getScaledHeight() / 2 - 3, 6, 6);
      }

      float newX = (float)(this.isHovering(mouseX, mouseY)?0:-this.width);
      RenderUtil var10001 = RenderUtil.INSTANCE;
      this.x = (float)RenderUtil.getAnimationState((double)this.x, (double)newX, (double)(this.isHovering(mouseX, mouseY)?(100.0F - this.x + 1.0F) * 10.0F:(Math.abs(this.x) + 1.0F) * 30.0F));
      Gui.drawCircle((int)this.x + this.width / 2 + 1, 36, 15, new Color(255, 255, 255));
      font.drawString(Client.CLIENT_NAME.substring(0, 1), this.x + (float)((this.width - font.getStringWidth(Client.CLIENT_NAME.substring(0, 1))) / 2) + 1.0F, 16.0F, -1);
      byte tab_height = 20;
      int yStart = res.getScaledHeight() / 2 - this.slots.size() / 2 * tab_height - 5;
      font = Client.fontManager.tahoma25;

      for(Iterator var9 = this.slots.iterator(); var9.hasNext(); yStart += 21) {
         UIMenuSlot slot = (UIMenuSlot)var9.next();
         boolean hover = this.isHovering(mouseX, mouseY) && mouseY >= yStart && mouseY < yStart + tab_height;
         var10001 = RenderUtil.INSTANCE;
         slot.animationX = RenderUtil.getAnimationState(slot.animationX, (double)(hover?1.0F:0.0F), 25.0D * slot.animationX + 1.0D);
         if(hover) {
            font.drawString(slot.text, this.x + (float)((this.width - font.getStringWidth(slot.text)) / 2), (float)yStart, Colors.GREY.c);
         } else {
            font.drawString(slot.text, this.x + (float)((this.width - font.getStringWidth(slot.text)) / 2), (float)yStart, -1);
         }

         Gui.drawRect(this.x + (float)((this.width - font.getStringWidth(slot.text)) / 2), (float)(yStart + font.FONT_HEIGHT) + 1.5F, this.x + (float)((this.width - font.getStringWidth(slot.text)) / 2) + (float)((int)((double)font.getStringWidth(slot.text) * slot.animationX)), (float)(yStart + font.FONT_HEIGHT + 2), -7748364);
         if(hover && this.handler.canExcecute()) {
            slot.run();
         }
      }

   }

   public boolean isHovering(int mouseX, int mouseY) {
      return (float)mouseX <= this.x + (float)this.width + 12.0F;
   }
}
