package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsScrolledSelectionList;

public class GuiSlotRealmsProxy extends GuiSlot {
  private final RealmsScrolledSelectionList selectionList;
  
  public GuiSlotRealmsProxy(RealmsScrolledSelectionList selectionListIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
    super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
    this.selectionList = selectionListIn;
  }
  
  protected int getSize() {
    return this.selectionList.getItemCount();
  }
  
  protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
    this.selectionList.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
  }
  
  protected boolean isSelected(int slotIndex) {
    return this.selectionList.isSelectedItem(slotIndex);
  }
  
  protected void drawBackground() {
    this.selectionList.renderBackground();
  }
  
  protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
    this.selectionList.renderItem(entryID, p_180791_2_, p_180791_3_, p_180791_4_, mouseXIn, mouseYIn);
  }
  
  public int func_154338_k() {
    return this.width;
  }
  
  public int func_154339_l() {
    return this.mouseY;
  }
  
  public int func_154337_m() {
    return this.mouseX;
  }
  
  protected int getContentHeight() {
    return this.selectionList.getMaxPosition();
  }
  
  protected int getScrollBarX() {
    return this.selectionList.getScrollbarPosition();
  }
  
  public void handleMouseInput() {
    super.handleMouseInput();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\gui\GuiSlotRealmsProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */