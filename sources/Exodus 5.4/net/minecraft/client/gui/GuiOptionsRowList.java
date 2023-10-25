/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionsRowList
extends GuiListExtended {
    private final List<Row> field_148184_k = Lists.newArrayList();

    public GuiOptionsRowList(Minecraft minecraft, int n, int n2, int n3, int n4, int n5, GameSettings.Options ... optionsArray) {
        super(minecraft, n, n2, n3, n4, n5);
        this.field_148163_i = false;
        int n6 = 0;
        while (n6 < optionsArray.length) {
            GameSettings.Options options = optionsArray[n6];
            GameSettings.Options options2 = n6 < optionsArray.length - 1 ? optionsArray[n6 + 1] : null;
            GuiButton guiButton = this.func_148182_a(minecraft, n / 2 - 155, 0, options);
            GuiButton guiButton2 = this.func_148182_a(minecraft, n / 2 - 155 + 160, 0, options2);
            this.field_148184_k.add(new Row(guiButton, guiButton2));
            n6 += 2;
        }
    }

    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 32;
    }

    @Override
    public Row getListEntry(int n) {
        return this.field_148184_k.get(n);
    }

    private GuiButton func_148182_a(Minecraft minecraft, int n, int n2, GameSettings.Options options) {
        if (options == null) {
            return null;
        }
        int n3 = options.returnEnumOrdinal();
        return options.getEnumFloat() ? new GuiOptionSlider(n3, n, n2, options) : new GuiOptionButton(n3, n, n2, options, Minecraft.gameSettings.getKeyBinding(options));
    }

    @Override
    protected int getSize() {
        return this.field_148184_k.size();
    }

    @Override
    public int getListWidth() {
        return 400;
    }

    public static class Row
    implements GuiListExtended.IGuiListEntry {
        private final Minecraft field_148325_a = Minecraft.getMinecraft();
        private final GuiButton field_148324_c;
        private final GuiButton field_148323_b;

        @Override
        public void setSelected(int n, int n2, int n3) {
        }

        @Override
        public void mouseReleased(int n, int n2, int n3, int n4, int n5, int n6) {
            if (this.field_148323_b != null) {
                this.field_148323_b.mouseReleased(n2, n3);
            }
            if (this.field_148324_c != null) {
                this.field_148324_c.mouseReleased(n2, n3);
            }
        }

        @Override
        public void drawEntry(int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
            if (this.field_148323_b != null) {
                this.field_148323_b.yPosition = n3;
                this.field_148323_b.drawButton(this.field_148325_a, n6, n7);
            }
            if (this.field_148324_c != null) {
                this.field_148324_c.yPosition = n3;
                this.field_148324_c.drawButton(this.field_148325_a, n6, n7);
            }
        }

        public Row(GuiButton guiButton, GuiButton guiButton2) {
            this.field_148323_b = guiButton;
            this.field_148324_c = guiButton2;
        }

        @Override
        public boolean mousePressed(int n, int n2, int n3, int n4, int n5, int n6) {
            if (this.field_148323_b.mousePressed(this.field_148325_a, n2, n3)) {
                if (this.field_148323_b instanceof GuiOptionButton) {
                    Minecraft.gameSettings.setOptionValue(((GuiOptionButton)this.field_148323_b).returnEnumOptions(), 1);
                    this.field_148323_b.displayString = Minecraft.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(this.field_148323_b.id));
                }
                return true;
            }
            if (this.field_148324_c != null && this.field_148324_c.mousePressed(this.field_148325_a, n2, n3)) {
                if (this.field_148324_c instanceof GuiOptionButton) {
                    Minecraft.gameSettings.setOptionValue(((GuiOptionButton)this.field_148324_c).returnEnumOptions(), 1);
                    this.field_148324_c.displayString = Minecraft.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(this.field_148324_c.id));
                }
                return true;
            }
            return false;
        }
    }
}

