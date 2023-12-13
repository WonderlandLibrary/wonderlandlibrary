// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.Minecraft;
import java.util.List;

public class GuiOptionsRowList extends GuiListExtended
{
    private final List field_148184_k;
    private static final String __OBFID = "CL_00000677";
    
    public GuiOptionsRowList(final Minecraft mcIn, final int p_i45015_2_, final int p_i45015_3_, final int p_i45015_4_, final int p_i45015_5_, final int p_i45015_6_, final GameSettings.Options... p_i45015_7_) {
        super(mcIn, p_i45015_2_, p_i45015_3_, p_i45015_4_, p_i45015_5_, p_i45015_6_);
        this.field_148184_k = Lists.newArrayList();
        this.field_148163_i = false;
        for (int var8 = 0; var8 < p_i45015_7_.length; var8 += 2) {
            final GameSettings.Options var9 = p_i45015_7_[var8];
            final GameSettings.Options var10 = (var8 < p_i45015_7_.length - 1) ? p_i45015_7_[var8 + 1] : null;
            final GuiButton var11 = this.func_148182_a(mcIn, p_i45015_2_ / 2 - 155, 0, var9);
            final GuiButton var12 = this.func_148182_a(mcIn, p_i45015_2_ / 2 - 155 + 160, 0, var10);
            this.field_148184_k.add(new Row(var11, var12));
        }
    }
    
    private GuiButton func_148182_a(final Minecraft mcIn, final int p_148182_2_, final int p_148182_3_, final GameSettings.Options p_148182_4_) {
        if (p_148182_4_ == null) {
            return null;
        }
        final int var5 = p_148182_4_.returnEnumOrdinal();
        return p_148182_4_.getEnumFloat() ? new GuiOptionSlider(var5, p_148182_2_, p_148182_3_, p_148182_4_) : new GuiOptionButton(var5, p_148182_2_, p_148182_3_, p_148182_4_, mcIn.gameSettings.getKeyBinding(p_148182_4_));
    }
    
    public Row func_180792_c(final int p_180792_1_) {
        return this.field_148184_k.get(p_180792_1_);
    }
    
    @Override
    protected int getSize() {
        return this.field_148184_k.size();
    }
    
    @Override
    public int getListWidth() {
        return 400;
    }
    
    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 32;
    }
    
    @Override
    public IGuiListEntry getListEntry(final int p_148180_1_) {
        return this.func_180792_c(p_148180_1_);
    }
    
    public static class Row implements IGuiListEntry
    {
        private final Minecraft field_148325_a;
        private final GuiButton field_148323_b;
        private final GuiButton field_148324_c;
        private static final String __OBFID = "CL_00000678";
        
        public Row(final GuiButton p_i45014_1_, final GuiButton p_i45014_2_) {
            this.field_148325_a = Minecraft.getMinecraft();
            this.field_148323_b = p_i45014_1_;
            this.field_148324_c = p_i45014_2_;
        }
        
        @Override
        public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
            if (this.field_148323_b != null) {
                this.field_148323_b.yPosition = y;
                this.field_148323_b.drawButton(this.field_148325_a, mouseX, mouseY);
            }
            if (this.field_148324_c != null) {
                this.field_148324_c.yPosition = y;
                this.field_148324_c.drawButton(this.field_148325_a, mouseX, mouseY);
            }
        }
        
        @Override
        public boolean mousePressed(final int p_148278_1_, final int p_148278_2_, final int p_148278_3_, final int p_148278_4_, final int p_148278_5_, final int p_148278_6_) {
            if (this.field_148323_b.mousePressed(this.field_148325_a, p_148278_2_, p_148278_3_)) {
                if (this.field_148323_b instanceof GuiOptionButton) {
                    this.field_148325_a.gameSettings.setOptionValue(((GuiOptionButton)this.field_148323_b).returnEnumOptions(), 1);
                    this.field_148323_b.displayString = this.field_148325_a.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(this.field_148323_b.id));
                }
                return true;
            }
            if (this.field_148324_c != null && this.field_148324_c.mousePressed(this.field_148325_a, p_148278_2_, p_148278_3_)) {
                if (this.field_148324_c instanceof GuiOptionButton) {
                    this.field_148325_a.gameSettings.setOptionValue(((GuiOptionButton)this.field_148324_c).returnEnumOptions(), 1);
                    this.field_148324_c.displayString = this.field_148325_a.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(this.field_148324_c.id));
                }
                return true;
            }
            return false;
        }
        
        @Override
        public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
            if (this.field_148323_b != null) {
                this.field_148323_b.mouseReleased(x, y);
            }
            if (this.field_148324_c != null) {
                this.field_148324_c.mouseReleased(x, y);
            }
        }
        
        @Override
        public void setSelected(final int p_178011_1_, final int p_178011_2_, final int p_178011_3_) {
        }
    }
}
