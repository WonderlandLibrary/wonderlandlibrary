package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;

public class GuiLockIconButton
  extends GuiButton
{
  private boolean field_175231_o = false;
  private static final String __OBFID = "CL_00001952";
  
  public GuiLockIconButton(int p_i45538_1_, int p_i45538_2_, int p_i45538_3_)
  {
    super(p_i45538_1_, p_i45538_2_, p_i45538_3_, 20, 20, "");
  }
  
  public boolean func_175230_c()
  {
    return this.field_175231_o;
  }
  
  public void func_175229_b(boolean p_175229_1_)
  {
    this.field_175231_o = p_175229_1_;
  }
  
  public void drawButton(Minecraft mc, int mouseX, int mouseY)
  {
    if (this.visible)
    {
      mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      boolean var4 = (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + this.height);
      Icon var5;
      Icon var5;
      if (this.field_175231_o)
      {
        Icon var5;
        if (!this.enabled)
        {
          var5 = Icon.LOCKED_DISABLED;
        }
        else
        {
          Icon var5;
          if (var4) {
            var5 = Icon.LOCKED_HOVER;
          } else {
            var5 = Icon.LOCKED;
          }
        }
      }
      else
      {
        Icon var5;
        if (!this.enabled)
        {
          var5 = Icon.UNLOCKED_DISABLED;
        }
        else
        {
          Icon var5;
          if (var4) {
            var5 = Icon.UNLOCKED_HOVER;
          } else {
            var5 = Icon.UNLOCKED;
          }
        }
      }
      drawTexturedModalRect(this.xPosition, this.yPosition, var5.func_178910_a(), var5.func_178912_b(), this.width, this.height);
    }
  }
  
  static enum Icon
  {
    private final int field_178914_g;
    private final int field_178920_h;
    private static final Icon[] $VALUES = { LOCKED, LOCKED_HOVER, LOCKED_DISABLED, UNLOCKED, UNLOCKED_HOVER, UNLOCKED_DISABLED };
    private static final String __OBFID = "CL_00001951";
    
    private Icon(String p_i45537_1_, int p_i45537_2_, int p_i45537_3_, int p_i45537_4_)
    {
      this.field_178914_g = p_i45537_3_;
      this.field_178920_h = p_i45537_4_;
    }
    
    public int func_178910_a()
    {
      return this.field_178914_g;
    }
    
    public int func_178912_b()
    {
      return this.field_178920_h;
    }
  }
}
