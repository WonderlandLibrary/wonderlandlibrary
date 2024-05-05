package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.settings.GameSettings;

public static class GuiOptionsRowList.Row
implements GuiListExtended.IGuiListEntry {
    private final Minecraft field_148325_a = Minecraft.getMinecraft();
    private final GuiButton field_148323_b;
    private final GuiButton field_148324_c;

    public GuiOptionsRowList.Row(GuiButton p_i45014_1_, GuiButton p_i45014_2_) {
        this.field_148323_b = p_i45014_1_;
        this.field_148324_c = p_i45014_2_;
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        if (this.field_148323_b != null) {
            this.field_148323_b.yPosition = y;
            this.field_148323_b.drawButton(this.field_148325_a, mouseX, mouseY);
        }
        if (this.field_148324_c != null) {
            this.field_148324_c.yPosition = y;
            this.field_148324_c.drawButton(this.field_148325_a, mouseX, mouseY);
        }
    }

    public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
        if (this.field_148323_b.mousePressed(this.field_148325_a, p_148278_2_, p_148278_3_)) {
            if (this.field_148323_b instanceof GuiOptionButton) {
                this.field_148325_a.gameSettings.setOptionValue(((GuiOptionButton)this.field_148323_b).returnEnumOptions(), 1);
                this.field_148323_b.displayString = this.field_148325_a.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions((int)this.field_148323_b.id));
            }
            return true;
        }
        if (this.field_148324_c != null && this.field_148324_c.mousePressed(this.field_148325_a, p_148278_2_, p_148278_3_)) {
            if (this.field_148324_c instanceof GuiOptionButton) {
                this.field_148325_a.gameSettings.setOptionValue(((GuiOptionButton)this.field_148324_c).returnEnumOptions(), 1);
                this.field_148324_c.displayString = this.field_148325_a.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions((int)this.field_148324_c.id));
            }
            return true;
        }
        return false;
    }

    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
        if (this.field_148323_b != null) {
            this.field_148323_b.mouseReleased(x, y);
        }
        if (this.field_148324_c != null) {
            this.field_148324_c.mouseReleased(x, y);
        }
    }

    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
    }
}
