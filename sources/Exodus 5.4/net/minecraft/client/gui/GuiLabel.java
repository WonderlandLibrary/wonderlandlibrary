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
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiLabel
extends Gui {
    protected int field_146167_a = 200;
    private int field_146169_o;
    private int field_146168_n;
    private FontRenderer fontRenderer;
    public boolean visible = true;
    private boolean centered;
    private boolean labelBgEnabled;
    private int field_146166_p;
    public int field_146174_h;
    private List<String> field_146173_k;
    private int field_146165_q;
    public int field_146162_g;
    protected int field_146161_f = 20;
    public int field_175204_i;
    private int field_146163_s;

    public GuiLabel setCentered() {
        this.centered = true;
        return this;
    }

    public GuiLabel(FontRenderer fontRenderer, int n, int n2, int n3, int n4, int n5, int n6) {
        this.fontRenderer = fontRenderer;
        this.field_175204_i = n;
        this.field_146162_g = n2;
        this.field_146174_h = n3;
        this.field_146167_a = n4;
        this.field_146161_f = n5;
        this.field_146173_k = Lists.newArrayList();
        this.centered = false;
        this.labelBgEnabled = false;
        this.field_146168_n = n6;
        this.field_146169_o = -1;
        this.field_146166_p = -1;
        this.field_146165_q = -1;
        this.field_146163_s = 0;
    }

    public void func_175202_a(String string) {
        this.field_146173_k.add(I18n.format(string, new Object[0]));
    }

    public void drawLabel(Minecraft minecraft, int n, int n2) {
        if (this.visible) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            this.drawLabelBackground(minecraft, n, n2);
            int n3 = this.field_146174_h + this.field_146161_f / 2 + this.field_146163_s / 2;
            int n4 = n3 - this.field_146173_k.size() * 10 / 2;
            int n5 = 0;
            while (n5 < this.field_146173_k.size()) {
                if (this.centered) {
                    this.drawCenteredString(this.fontRenderer, this.field_146173_k.get(n5), this.field_146162_g + this.field_146167_a / 2, n4 + n5 * 10, this.field_146168_n);
                } else {
                    this.drawString(this.fontRenderer, this.field_146173_k.get(n5), this.field_146162_g, n4 + n5 * 10, this.field_146168_n);
                }
                ++n5;
            }
        }
    }

    protected void drawLabelBackground(Minecraft minecraft, int n, int n2) {
        if (this.labelBgEnabled) {
            int n3 = this.field_146167_a + this.field_146163_s * 2;
            int n4 = this.field_146161_f + this.field_146163_s * 2;
            int n5 = this.field_146162_g - this.field_146163_s;
            int n6 = this.field_146174_h - this.field_146163_s;
            GuiLabel.drawRect(n5, n6, n5 + n3, n6 + n4, this.field_146169_o);
            GuiLabel.drawHorizontalLine(n5, n5 + n3, n6, this.field_146166_p);
            GuiLabel.drawHorizontalLine(n5, n5 + n3, n6 + n4, this.field_146165_q);
            this.drawVerticalLine(n5, n6, n6 + n4, this.field_146166_p);
            this.drawVerticalLine(n5 + n3, n6, n6 + n4, this.field_146165_q);
        }
    }
}

