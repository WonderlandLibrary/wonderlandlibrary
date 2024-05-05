package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.impl.elements.render.NameProtect;
import space.lunaclient.luna.impl.managers.ElementManager;
import space.lunaclient.luna.util.MathUtils;

public class GuiNewChat
  extends Gui
{
  private static final Logger logger = ;
  private final Minecraft mc;
  public static List sentMessages = Lists.newArrayList();
  public static List chatLines = Lists.newArrayList();
  private final List field_146253_i = Lists.newArrayList();
  private int scrollPos;
  private boolean isScrolled;
  private static final String __OBFID = "CL_00000669";
  
  public GuiNewChat(Minecraft mcIn)
  {
    this.mc = mcIn;
  }
  
  public void drawChat(int p_146230_1_)
  {
    if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN)
    {
      int var2 = getLineCount();
      boolean var3 = false;
      int var4 = 0;
      int var5 = this.field_146253_i.size();
      float var6 = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
      if (var5 > 0)
      {
        if (getChatOpen()) {
          var3 = true;
        }
        float var7 = getChatScale();
        int var8 = MathHelper.ceiling_float_int(getChatWidth() / var7);
        GlStateManager.pushMatrix();
        GlStateManager.translate(2.0F, 20.0F, 0.0F);
        GlStateManager.scale(var7, var7, 1.0F);
        for (int var9 = 0; (var9 + this.scrollPos < this.field_146253_i.size()) && (var9 < var2); var9++)
        {
          ChatLine var10 = (ChatLine)this.field_146253_i.get(var9 + this.scrollPos);
          if (var10 != null)
          {
            int var11 = p_146230_1_ - var10.getUpdatedCounter();
            if ((var11 < 200) || (var3))
            {
              double var12 = var11 / 200.0D;
              var12 = 1.0D - var12;
              var12 *= 10.0D;
              var12 = MathHelper.clamp_double(var12, 0.0D, 1.0D);
              var12 *= var12;
              int var14 = (int)(255.0D * var12);
              if (var3) {
                var14 = 255;
              }
              var14 = (int)(var14 * var6);
              var4++;
              if (var14 > 3)
              {
                byte var15 = 0;
                int var16 = -var9 * 9;
                drawRect(var15, var16 - 9, var15 + var8 + 4, var16, var14 / 2 << 24);
                String var17 = var10.getChatComponent().getFormattedText();
                if (Luna.INSTANCE.ELEMENT_MANAGER.getElement(NameProtect.class).isToggled()) {
                  if (var17.contains(Minecraft.thePlayer.getName())) {
                    var17 = var17.replace(Minecraft.thePlayer.getName(), NameProtect.name);
                  } else if (var17.contains("game_")) {
                    var17 = var17.replace("game_", "game_" + MathUtils.getRandom(666));
                  }
                }
                GlStateManager.enableBlend();
                Minecraft.fontRendererObj.func_175063_a(var17, var15, var16 - 8, 16777215 + (var14 << 24));
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
              }
            }
          }
        }
        if (var3)
        {
          var9 = Minecraft.fontRendererObj.FONT_HEIGHT;
          GlStateManager.translate(-3.0F, 0.0F, 0.0F);
          int var18 = var5 * var9 + var5;
          int var11 = var4 * var9 + var4;
          int var19 = this.scrollPos * var11 / var5;
          int var13 = var11 * var11 / var18;
          if (var18 != var11)
          {
            int var14 = var19 > 0 ? 170 : 96;
            int var20 = this.isScrolled ? 13382451 : 3355562;
            drawRect(0.0D, -var19, 2.0D, -var19 - var13, var20 + (var14 << 24));
            drawRect(2.0D, -var19, 1.0D, -var19 - var13, 13421772 + (var14 << 24));
          }
        }
        GlStateManager.popMatrix();
      }
    }
  }
  
  public void clearChatMessages()
  {
    this.field_146253_i.clear();
    chatLines.clear();
    sentMessages.clear();
  }
  
  public void printChatMessage(IChatComponent p_146227_1_)
  {
    printChatMessageWithOptionalDeletion(p_146227_1_, 0);
  }
  
  public void printChatMessageWithOptionalDeletion(IChatComponent p_146234_1_, int p_146234_2_)
  {
    setChatLine(p_146234_1_, p_146234_2_, this.mc.ingameGUI.getUpdateCounter(), false);
    logger.info("[CHAT] " + p_146234_1_.getUnformattedText());
    space.lunaclient.luna.util.ChatUtils.chatHistoryString = p_146234_1_.getUnformattedText();
  }
  
  private void setChatLine(IChatComponent p_146237_1_, int p_146237_2_, int p_146237_3_, boolean p_146237_4_)
  {
    if (p_146237_2_ != 0) {
      deleteChatLine(p_146237_2_);
    }
    int var5 = MathHelper.floor_float(getChatWidth() / getChatScale());
    List var6 = GuiUtilRenderComponents.func_178908_a(p_146237_1_, var5, Minecraft.fontRendererObj, false, false);
    boolean var7 = getChatOpen();
    IChatComponent var9;
    for (Iterator var8 = var6.iterator(); var8.hasNext(); this.field_146253_i.add(0, new ChatLine(p_146237_3_, var9, p_146237_2_)))
    {
      var9 = (IChatComponent)var8.next();
      if ((var7) && (this.scrollPos > 0))
      {
        this.isScrolled = true;
        scroll(1);
      }
    }
    while (this.field_146253_i.size() > 100) {
      this.field_146253_i.remove(this.field_146253_i.size() - 1);
    }
    if (!p_146237_4_)
    {
      chatLines.add(0, new ChatLine(p_146237_3_, p_146237_1_, p_146237_2_));
      while (chatLines.size() > 100) {
        chatLines.remove(chatLines.size() - 1);
      }
    }
  }
  
  public void refreshChat()
  {
    this.field_146253_i.clear();
    resetScroll();
    for (int var1 = chatLines.size() - 1; var1 >= 0; var1--)
    {
      ChatLine var2 = (ChatLine)chatLines.get(var1);
      setChatLine(var2.getChatComponent(), var2.getChatLineID(), var2.getUpdatedCounter(), true);
    }
  }
  
  public List getSentMessages()
  {
    return sentMessages;
  }
  
  public void addToSentMessages(String p_146239_1_)
  {
    if ((sentMessages.isEmpty()) || (!sentMessages.get(sentMessages.size() - 1).equals(p_146239_1_))) {
      sentMessages.add(p_146239_1_);
    }
  }
  
  public void resetScroll()
  {
    this.scrollPos = 0;
    this.isScrolled = false;
  }
  
  public void scroll(int p_146229_1_)
  {
    this.scrollPos += p_146229_1_;
    int var2 = this.field_146253_i.size();
    if (this.scrollPos > var2 - getLineCount()) {
      this.scrollPos = (var2 - getLineCount());
    }
    if (this.scrollPos <= 0)
    {
      this.scrollPos = 0;
      this.isScrolled = false;
    }
  }
  
  public IChatComponent getChatComponent(int p_146236_1_, int p_146236_2_)
  {
    if (!getChatOpen()) {
      return null;
    }
    ScaledResolution var3 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
    int var4 = var3.getScaleFactor();
    float var5 = getChatScale();
    int var6 = p_146236_1_ / var4 - 3;
    int var7 = p_146236_2_ / var4 - 27;
    var6 = MathHelper.floor_float(var6 / var5);
    var7 = MathHelper.floor_float(var7 / var5);
    if ((var6 >= 0) && (var7 >= 0))
    {
      int var8 = Math.min(getLineCount(), this.field_146253_i.size());
      if ((var6 <= MathHelper.floor_float(getChatWidth() / getChatScale())) && (var7 < Minecraft.fontRendererObj.FONT_HEIGHT * var8 + var8))
      {
        int var9 = var7 / Minecraft.fontRendererObj.FONT_HEIGHT + this.scrollPos;
        if ((var9 >= 0) && (var9 < this.field_146253_i.size()))
        {
          ChatLine var10 = (ChatLine)this.field_146253_i.get(var9);
          int var11 = 0;
          Iterator var12 = var10.getChatComponent().iterator();
          while (var12.hasNext())
          {
            IChatComponent var13 = (IChatComponent)var12.next();
            if ((var13 instanceof ChatComponentText))
            {
              var11 += Minecraft.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)var13).getChatComponentText_TextValue(), false));
              if (var11 > var6) {
                return var13;
              }
            }
          }
        }
        return null;
      }
      return null;
    }
    return null;
  }
  
  public boolean getChatOpen()
  {
    return this.mc.currentScreen instanceof GuiChat;
  }
  
  public void deleteChatLine(int p_146242_1_)
  {
    Iterator var2 = this.field_146253_i.iterator();
    while (var2.hasNext())
    {
      ChatLine var3 = (ChatLine)var2.next();
      if (var3.getChatLineID() == p_146242_1_) {
        var2.remove();
      }
    }
    var2 = chatLines.iterator();
    while (var2.hasNext())
    {
      ChatLine var3 = (ChatLine)var2.next();
      if (var3.getChatLineID() == p_146242_1_) {
        var2.remove();
      }
    }
  }
  
  public int getChatWidth()
  {
    return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
  }
  
  public int getChatHeight()
  {
    return calculateChatboxHeight(getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
  }
  
  public float getChatScale()
  {
    return this.mc.gameSettings.chatScale;
  }
  
  public static int calculateChatboxWidth(float p_146233_0_)
  {
    short var1 = 320;
    byte var2 = 40;
    return MathHelper.floor_float(p_146233_0_ * (var1 - var2) + var2);
  }
  
  public static int calculateChatboxHeight(float p_146243_0_)
  {
    short var1 = 180;
    byte var2 = 20;
    return MathHelper.floor_float(p_146243_0_ * (var1 - var2) + var2);
  }
  
  public int getLineCount()
  {
    return getChatHeight() / 9;
  }
}
