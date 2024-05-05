package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import space.lunaclient.luna.impl.gui.LunaFontRenderer;
import space.lunaclient.luna.impl.managers.FontManager;

public class GuiButton
  extends Gui
{
  protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
  protected int width;
  protected int height;
  public int xPosition;
  public int yPosition;
  public String displayString;
  private int transition;
  public int id;
  public boolean enabled;
  public boolean visible;
  protected boolean hovered;
  private static final String __OBFID = "CL_00000668";
  
  public GuiButton(int buttonId, int x, int y, String buttonText)
  {
    this(buttonId, x, y, 200, 20, buttonText);
  }
  
  public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
  {
    this.width = 200;
    this.height = 20;
    this.enabled = true;
    this.visible = true;
    this.id = buttonId;
    this.xPosition = x;
    this.yPosition = y;
    this.width = widthIn;
    this.height = heightIn;
    this.displayString = buttonText;
  }
  
  protected int getHoverState(boolean mouseOver)
  {
    byte var2 = 1;
    if (!this.enabled) {
      var2 = 0;
    } else if (mouseOver) {
      var2 = 2;
    }
    return var2;
  }
  
  public void setTransition(int transition)
  {
    this.transition = transition;
  }
  
  private int getTransition()
  {
    return this.transition;
  }
  
  public void drawButton(Minecraft mc, int mouseX, int mouseY)
  {
    if (this.visible)
    {
      FontRenderer var4 = Minecraft.fontRendererObj;
      mc.getTextureManager().bindTexture(buttonTextures);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.hovered = ((mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + this.height));
      int var5 = getHoverState(this.hovered);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.blendFunc(770, 771);
      if (getTransition() > 0) {
        this.transition = (getTransition() - 1);
      }
      if (!this.enabled)
      {
        drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 1073741824);
      }
      else if (this.hovered)
      {
        drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -2137308428);
        this.transition = (Minecraft.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(this.displayString)) - 10);
      }
      else
      {
        drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -1879048192);
      }
      mouseDragged(mc, mouseX, mouseY);
      int var6 = 14737632;
      if (!this.enabled) {
        var6 = 10526880;
      } else if (this.hovered) {
        var6 = 16777120;
      }
      FontManager.fontRenderer.drawCenteredString(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, var6);
    }
  }
  
  protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {}
  
  public void mouseReleased(int mouseX, int mouseY) {}
  
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
  {
    return (this.enabled) && (this.visible) && (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + this.height);
  }
  
  public boolean isMouseOver()
  {
    return this.hovered;
  }
  
  public void drawButtonForegroundLayer(int mouseX, int mouseY) {}
  
  public void playPressSound(SoundHandler soundHandlerIn)
  {
    soundHandlerIn.playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0F));
  }
  
  public int getButtonWidth()
  {
    return this.width;
  }
  
  public void func_175211_a(int p_175211_1_)
  {
    this.width = p_175211_1_;
  }
}
