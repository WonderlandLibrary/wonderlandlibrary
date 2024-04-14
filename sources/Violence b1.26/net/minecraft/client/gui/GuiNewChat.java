package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import de.violence.gui.VSetting;
import de.violence.module.Module;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiNewChat extends Gui {
   private static final Logger logger = LogManager.getLogger();
   private final Minecraft mc;
   private final List sentMessages = Lists.newArrayList();
   private final List chatLines = Lists.newArrayList();
   private final List field_146253_i = Lists.newArrayList();
   private int scrollPos;
   private boolean isScrolled;

   public GuiNewChat(Minecraft mcIn) {
      this.mc = mcIn;
   }

   public void drawChat(int p_146230_1_) {
      if(this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
         int i = this.getLineCount();
         boolean flag = false;
         int j = 0;
         int k = this.field_146253_i.size();
         float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
         if(k > 0) {
            if(this.getChatOpen()) {
               flag = true;
            }

            float f1 = this.getChatScale();
            int l = MathHelper.ceiling_float_int((float)this.getChatWidth() / f1);
            GlStateManager.pushMatrix();
            GlStateManager.translate(2.0F, 20.0F, 0.0F);
            GlStateManager.scale(f1, f1, 1.0F);

            int k2;
            int i3;
            int k3;
            int l3;
            for(k2 = 0; k2 + this.scrollPos < this.field_146253_i.size() && k2 < i; ++k2) {
               ChatLine l2 = (ChatLine)this.field_146253_i.get(k2 + this.scrollPos);
               if(l2 != null) {
                  if(l2.x > 0) {
                     l2.x -= 3;
                  }

                  i3 = p_146230_1_ - l2.getUpdatedCounter();
                  if(i3 < 200 || flag) {
                     double j3 = (double)i3 / 200.0D;
                     j3 = 1.0D - j3;
                     j3 *= 10.0D;
                     j3 = MathHelper.clamp_double(j3, 0.0D, 1.0D);
                     j3 *= j3;
                     k3 = (int)(255.0D * j3);
                     if(flag) {
                        k3 = 255;
                     }

                     k3 = (int)((float)k3 * f);
                     ++j;
                     if(k3 > 3) {
                        l3 = 0;
                        int j2 = -k2 * 9 - 10;
                        drawRect(l3, j2 - 9, l3 + l + 4, j2, k3 / 2 << 24);
                        String s = l2.getChatComponent().getFormattedText();
                        GlStateManager.enableBlend();
                        if(VSetting.getByName("Chat Animations", Module.getByName("Hud")).isToggled()) {
                           l3 -= l2.x;
                        }

                        this.mc.fontRendererObj.drawStringWithShadow(s, (float)l3, (float)(j2 - 8), 16777215 + (k3 << 24));
                        GlStateManager.disableAlpha();
                        GlStateManager.disableBlend();
                     }
                  }
               }
            }

            if(flag) {
               k2 = this.mc.fontRendererObj.FONT_HEIGHT;
               GlStateManager.translate(-3.0F, 0.0F, 0.0F);
               int var18 = k * k2 + k;
               i3 = j * k2 + j;
               int var19 = this.scrollPos * i3 / k;
               int k1 = i3 * i3 / var18;
               if(var18 != i3) {
                  k3 = var19 > 0?170:96;
                  l3 = this.isScrolled?13382451:3355562;
                  drawRect(0, -var19, 2, -var19 - k1, l3 + (k3 << 24));
                  drawRect(2, -var19, 1, -var19 - k1, 13421772 + (k3 << 24));
               }
            }

            GlStateManager.popMatrix();
         }
      }

   }

   public void clearChatMessages() {
      this.field_146253_i.clear();
      this.chatLines.clear();
      this.sentMessages.clear();
   }

   public void printChatMessage(IChatComponent p_146227_1_) {
      this.printChatMessageWithOptionalDeletion(p_146227_1_, 0);
   }

   public void printChatMessageWithOptionalDeletion(IChatComponent p_146234_1_, int p_146234_2_) {
      this.setChatLine(p_146234_1_, p_146234_2_, this.mc.ingameGUI.getUpdateCounter(), false);
      logger.info("[CHAT] " + p_146234_1_.getUnformattedText());
   }

   private void setChatLine(IChatComponent p_146237_1_, int p_146237_2_, int p_146237_3_, boolean p_146237_4_) {
      if(p_146237_2_ != 0) {
         this.deleteChatLine(p_146237_2_);
      }

      int i = MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale());
      List list = GuiUtilRenderComponents.func_178908_a(p_146237_1_, i, this.mc.fontRendererObj, false, false);
      boolean flag = this.getChatOpen();

      IChatComponent ichatcomponent;
      for(Iterator var9 = list.iterator(); var9.hasNext(); this.field_146253_i.add(0, new ChatLine(p_146237_3_, ichatcomponent, p_146237_2_))) {
         ichatcomponent = (IChatComponent)var9.next();
         if(flag && this.scrollPos > 0) {
            this.isScrolled = true;
            this.scroll(1);
         }
      }

      while(this.field_146253_i.size() > 100) {
         this.field_146253_i.remove(this.field_146253_i.size() - 1);
      }

      if(!p_146237_4_) {
         this.chatLines.add(0, new ChatLine(p_146237_3_, p_146237_1_, p_146237_2_));

         while(this.chatLines.size() > 100) {
            this.chatLines.remove(this.chatLines.size() - 1);
         }
      }

   }

   public void refreshChat() {
      this.field_146253_i.clear();
      this.resetScroll();

      for(int i = this.chatLines.size() - 1; i >= 0; --i) {
         ChatLine chatline = (ChatLine)this.chatLines.get(i);
         this.setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
      }

   }

   public List getSentMessages() {
      return this.sentMessages;
   }

   public void addToSentMessages(String p_146239_1_) {
      if(this.sentMessages.isEmpty() || !((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals(p_146239_1_)) {
         this.sentMessages.add(p_146239_1_);
      }

   }

   public void resetScroll() {
      this.scrollPos = 0;
      this.isScrolled = false;
   }

   public void scroll(int p_146229_1_) {
      this.scrollPos += p_146229_1_;
      int i = this.field_146253_i.size();
      if(this.scrollPos > i - this.getLineCount()) {
         this.scrollPos = i - this.getLineCount();
      }

      if(this.scrollPos <= 0) {
         this.scrollPos = 0;
         this.isScrolled = false;
      }

   }

   public IChatComponent getChatComponent(int p_146236_1_, int p_146236_2_) {
      if(!this.getChatOpen()) {
         return null;
      } else {
         new ScaledResolution(this.mc);
         int i = ScaledResolution.getScaleFactor();
         float f = this.getChatScale();
         int j = p_146236_1_ / i - 9;
         int k = p_146236_2_ / i - 40;
         j = MathHelper.floor_float((float)j / f);
         k = MathHelper.floor_float((float)k / f);
         if(j >= 0 && k >= 0) {
            int l = Math.min(this.getLineCount(), this.field_146253_i.size());
            if(j <= MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale()) && k < this.mc.fontRendererObj.FONT_HEIGHT * l + l) {
               int i1 = k / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;
               if(i1 >= 0 && i1 < this.field_146253_i.size()) {
                  ChatLine chatline = (ChatLine)this.field_146253_i.get(i1);
                  int j1 = 0;
                  if(VSetting.getByName("Chat Animations", Module.getByName("Hud")).isToggled()) {
                     j1 -= chatline.x;
                  }

                  Iterator var13 = chatline.getChatComponent().iterator();

                  while(var13.hasNext()) {
                     IChatComponent ichatcomponent = (IChatComponent)var13.next();
                     if(ichatcomponent instanceof ChatComponentText) {
                        j1 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)ichatcomponent).getChatComponentText_TextValue(), false));
                        if(j1 > j) {
                           return ichatcomponent;
                        }
                     }
                  }
               }

               return null;
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   public boolean getChatOpen() {
      return this.mc.currentScreen instanceof GuiChat;
   }

   public void deleteChatLine(int p_146242_1_) {
      Iterator iterator = this.field_146253_i.iterator();

      ChatLine chatline1;
      while(iterator.hasNext()) {
         chatline1 = (ChatLine)iterator.next();
         if(chatline1.getChatLineID() == p_146242_1_) {
            iterator.remove();
         }
      }

      iterator = this.chatLines.iterator();

      while(iterator.hasNext()) {
         chatline1 = (ChatLine)iterator.next();
         if(chatline1.getChatLineID() == p_146242_1_) {
            iterator.remove();
            break;
         }
      }

   }

   public int getChatWidth() {
      return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
   }

   public int getChatHeight() {
      return calculateChatboxHeight(this.getChatOpen()?this.mc.gameSettings.chatHeightFocused:this.mc.gameSettings.chatHeightUnfocused);
   }

   public float getChatScale() {
      return this.mc.gameSettings.chatScale;
   }

   public static int calculateChatboxWidth(float p_146233_0_) {
      short i = 320;
      byte j = 40;
      return MathHelper.floor_float(p_146233_0_ * (float)(i - j) + (float)j);
   }

   public static int calculateChatboxHeight(float p_146243_0_) {
      short i = 180;
      byte j = 20;
      return MathHelper.floor_float(p_146243_0_ * (float)(i - j) + (float)j);
   }

   public int getLineCount() {
      return this.getChatHeight() / 9;
   }
}
