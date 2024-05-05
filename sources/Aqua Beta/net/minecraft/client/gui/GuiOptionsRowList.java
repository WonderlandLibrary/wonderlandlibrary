package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiOptionsRowList;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionsRowList
extends GuiListExtended {
    private final List<Row> field_148184_k = Lists.newArrayList();

    public GuiOptionsRowList(Minecraft mcIn, int p_i45015_2_, int p_i45015_3_, int p_i45015_4_, int p_i45015_5_, int p_i45015_6_, GameSettings.Options ... p_i45015_7_) {
        super(mcIn, p_i45015_2_, p_i45015_3_, p_i45015_4_, p_i45015_5_, p_i45015_6_);
        this.field_148163_i = false;
        for (int i = 0; i < p_i45015_7_.length; i += 2) {
            GameSettings.Options gamesettings$options = p_i45015_7_[i];
            GameSettings.Options gamesettings$options1 = i < p_i45015_7_.length - 1 ? p_i45015_7_[i + 1] : null;
            GuiButton guibutton = this.func_148182_a(mcIn, p_i45015_2_ / 2 - 155, 0, gamesettings$options);
            GuiButton guibutton1 = this.func_148182_a(mcIn, p_i45015_2_ / 2 - 155 + 160, 0, gamesettings$options1);
            this.field_148184_k.add((Object)new Row(guibutton, guibutton1));
        }
    }

    private GuiButton func_148182_a(Minecraft mcIn, int p_148182_2_, int p_148182_3_, GameSettings.Options p_148182_4_) {
        if (p_148182_4_ == null) {
            return null;
        }
        int i = p_148182_4_.returnEnumOrdinal();
        return p_148182_4_.getEnumFloat() ? new GuiOptionSlider(i, p_148182_2_, p_148182_3_, p_148182_4_) : new GuiOptionButton(i, p_148182_2_, p_148182_3_, p_148182_4_, mcIn.gameSettings.getKeyBinding(p_148182_4_));
    }

    public Row getListEntry(int index) {
        return (Row)this.field_148184_k.get(index);
    }

    protected int getSize() {
        return this.field_148184_k.size();
    }

    public int getListWidth() {
        return 400;
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 32;
    }
}
