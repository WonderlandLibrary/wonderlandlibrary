package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;

public abstract class GuiListExtended
  extends GuiSlot
{
  private static final String __OBFID = "CL_00000674";
  
  public GuiListExtended(Minecraft mcIn, int p_i45010_2_, int p_i45010_3_, int p_i45010_4_, int p_i45010_5_, int p_i45010_6_)
  {
    super(mcIn, p_i45010_2_, p_i45010_3_, p_i45010_4_, p_i45010_5_, p_i45010_6_);
  }
  
  protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}
  
  protected boolean isSelected(int slotIndex)
  {
    return false;
  }
  
  protected void drawBackground() {}
  
  protected void drawSlot(int p_180791_1_, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_)
  {
    getListEntry(p_180791_1_).drawEntry(p_180791_1_, p_180791_2_, p_180791_3_, getListWidth(), p_180791_4_, p_180791_5_, p_180791_6_, getSlotIndexFromScreenCoords(p_180791_5_, p_180791_6_) == p_180791_1_);
  }
  
  protected void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_)
  {
    getListEntry(p_178040_1_).setSelected(p_178040_1_, p_178040_2_, p_178040_3_);
  }
  
  public boolean func_148179_a(int p_148179_1_, int p_148179_2_, int p_148179_3_)
  {
    if (isMouseYWithinSlotBounds(p_148179_2_))
    {
      int var4 = getSlotIndexFromScreenCoords(p_148179_1_, p_148179_2_);
      if (var4 >= 0)
      {
        int var5 = this.left + this.width / 2 - getListWidth() / 2 + 2;
        int var6 = this.top + 4 - getAmountScrolled() + var4 * this.slotHeight + this.headerPadding;
        int var7 = p_148179_1_ - var5;
        int var8 = p_148179_2_ - var6;
        if (getListEntry(var4).mousePressed(var4, p_148179_1_, p_148179_2_, p_148179_3_, var7, var8))
        {
          setEnabled(false);
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean func_148181_b(int p_148181_1_, int p_148181_2_, int p_148181_3_)
  {
    for (int var4 = 0; var4 < getSize(); var4++)
    {
      int var5 = this.left + this.width / 2 - getListWidth() / 2 + 2;
      int var6 = this.top + 4 - getAmountScrolled() + var4 * this.slotHeight + this.headerPadding;
      int var7 = p_148181_1_ - var5;
      int var8 = p_148181_2_ - var6;
      getListEntry(var4).mouseReleased(var4, p_148181_1_, p_148181_2_, p_148181_3_, var7, var8);
    }
    setEnabled(true);
    return false;
  }
  
  public abstract IGuiListEntry getListEntry(int paramInt);
  
  public static abstract interface IGuiListEntry
  {
    public abstract void setSelected(int paramInt1, int paramInt2, int paramInt3);
    
    public abstract void drawEntry(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean);
    
    public abstract boolean mousePressed(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
    
    public abstract void mouseReleased(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  }
}
