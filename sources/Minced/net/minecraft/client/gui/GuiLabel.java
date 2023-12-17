// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import com.google.common.collect.Lists;
import java.util.List;

public class GuiLabel extends Gui
{
    protected int width;
    protected int height;
    public int x;
    public int y;
    private final List<String> labels;
    public int id;
    private boolean centered;
    public boolean visible;
    private boolean labelBgEnabled;
    private final int textColor;
    private int backColor;
    private int ulColor;
    private int brColor;
    private final FontRenderer fontRenderer;
    private int border;
    
    public GuiLabel(final FontRenderer fontRendererObj, final int labelId, final int xIn, final int yIn, final int widthIn, final int heightIn, final int colorIn) {
        this.width = 200;
        this.height = 20;
        this.visible = true;
        this.fontRenderer = fontRendererObj;
        this.id = labelId;
        this.x = xIn;
        this.y = yIn;
        this.width = widthIn;
        this.height = heightIn;
        this.labels = (List<String>)Lists.newArrayList();
        this.centered = false;
        this.labelBgEnabled = false;
        this.textColor = colorIn;
        this.backColor = -1;
        this.ulColor = -1;
        this.brColor = -1;
        this.border = 0;
    }
    
    public void addLine(final String p_175202_1_) {
        this.labels.add(I18n.format(p_175202_1_, new Object[0]));
    }
    
    public GuiLabel setCentered() {
        this.centered = true;
        return this;
    }
    
    public void drawLabel(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.drawLabelBackground(mc, mouseX, mouseY);
            final int i = this.y + this.height / 2 + this.border / 2;
            final int j = i - this.labels.size() * 10 / 2;
            for (int k = 0; k < this.labels.size(); ++k) {
                if (this.centered) {
                    this.drawCenteredString(this.fontRenderer, this.labels.get(k), this.x + this.width / 2, j + k * 10, this.textColor);
                }
                else {
                    this.drawString(this.fontRenderer, this.labels.get(k), this.x, j + k * 10, this.textColor);
                }
            }
        }
    }
    
    protected void drawLabelBackground(final Minecraft mcIn, final int mouseX, final int mouseY) {
        if (this.labelBgEnabled) {
            final int i = this.width + this.border * 2;
            final int j = this.height + this.border * 2;
            final int k = this.x - this.border;
            final int l = this.y - this.border;
            Gui.drawRect((float)k, (float)l, (float)(k + i), (float)(l + j), this.backColor);
            this.drawHorizontalLine(k, k + i, l, this.ulColor);
            this.drawHorizontalLine(k, k + i, l + j, this.brColor);
            this.drawVerticalLine(k, l, l + j, this.ulColor);
            this.drawVerticalLine(k + i, l, l + j, this.brColor);
        }
    }
}
