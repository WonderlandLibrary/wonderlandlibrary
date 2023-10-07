// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;

public abstract class GuiListExtended extends GuiSlot
{
    private static final String __OBFID = "CL_00000674";
    
    public GuiListExtended(final Minecraft mcIn, final int p_i45010_2_, final int p_i45010_3_, final int p_i45010_4_, final int p_i45010_5_, final int p_i45010_6_) {
        super(mcIn, p_i45010_2_, p_i45010_3_, p_i45010_4_, p_i45010_5_, p_i45010_6_);
    }
    
    @Override
    protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return false;
    }
    
    @Override
    protected void drawBackground() {
    }
    
    @Override
    protected void drawSlot(final int p_180791_1_, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int p_180791_5_, final int p_180791_6_) {
        this.getListEntry(p_180791_1_).drawEntry(p_180791_1_, p_180791_2_, p_180791_3_, this.getListWidth(), p_180791_4_, p_180791_5_, p_180791_6_, this.getSlotIndexFromScreenCoords(p_180791_5_, p_180791_6_) == p_180791_1_);
    }
    
    @Override
    protected void func_178040_a(final int p_178040_1_, final int p_178040_2_, final int p_178040_3_) {
        this.getListEntry(p_178040_1_).setSelected(p_178040_1_, p_178040_2_, p_178040_3_);
    }
    
    public boolean func_148179_a(final int p_148179_1_, final int p_148179_2_, final int p_148179_3_) {
        if (this.isMouseYWithinSlotBounds(p_148179_2_)) {
            final int var4 = this.getSlotIndexFromScreenCoords(p_148179_1_, p_148179_2_);
            if (var4 >= 0) {
                final int var5 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
                final int var6 = this.top + 4 - this.getAmountScrolled() + var4 * this.slotHeight + this.headerPadding;
                final int var7 = p_148179_1_ - var5;
                final int var8 = p_148179_2_ - var6;
                if (this.getListEntry(var4).mousePressed(var4, p_148179_1_, p_148179_2_, p_148179_3_, var7, var8)) {
                    this.setEnabled(false);
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean func_148181_b(final int p_148181_1_, final int p_148181_2_, final int p_148181_3_) {
        for (int var4 = 0; var4 < this.getSize(); ++var4) {
            final int var5 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            final int var6 = this.top + 4 - this.getAmountScrolled() + var4 * this.slotHeight + this.headerPadding;
            final int var7 = p_148181_1_ - var5;
            final int var8 = p_148181_2_ - var6;
            this.getListEntry(var4).mouseReleased(var4, p_148181_1_, p_148181_2_, p_148181_3_, var7, var8);
        }
        this.setEnabled(true);
        return false;
    }
    
    public abstract IGuiListEntry getListEntry(final int p0);
    
    public interface IGuiListEntry
    {
        void setSelected(final int p0, final int p1, final int p2);
        
        void drawEntry(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6, final boolean p7);
        
        boolean mousePressed(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
        
        void mouseReleased(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    }
}
