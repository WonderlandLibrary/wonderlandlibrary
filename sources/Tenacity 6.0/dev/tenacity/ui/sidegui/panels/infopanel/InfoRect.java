// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.panels.infopanel;

import java.util.Iterator;
import dev.tenacity.utils.misc.MathUtils;
import java.awt.Color;
import dev.tenacity.utils.render.StencilUtil;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import java.util.ArrayList;
import dev.tenacity.utils.objects.Scroll;
import java.util.List;
import dev.tenacity.ui.Screen;

public class InfoRect implements Screen
{
    public float x;
    public float y;
    public float width;
    public float height;
    public float alpha;
    public final List<InfoButton> faqButtons;
    private Scroll infoScroll;
    
    public InfoRect() {
        this.infoScroll = new Scroll();
        this.faqButtons = new ArrayList<InfoButton>();
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        RoundedUtil.drawRound(this.x, this.y, this.width, this.height, 5.0f, ColorUtil.tripleColor(27, this.alpha));
        if (HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY)) {
            this.infoScroll.onScroll(35);
        }
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(this.x, this.y, this.width, this.height, 5.0f, Color.WHITE);
        StencilUtil.readStencilBuffer(1);
        float count = 0.0f;
        for (final InfoButton button : this.faqButtons) {
            button.setX(this.x + 5.0f);
            button.setWidth(this.width - 10.0f);
            button.setHeight(20.0f);
            button.setY((float)(this.y + 5.0f + count * button.getHeight() + MathUtils.roundToHalf(this.infoScroll.getScroll())));
            button.setAlpha(this.alpha);
            button.drawScreen(mouseX, mouseY);
            count += button.getCount() + 0.25f;
        }
        final float hiddenHeight = count * 20.0f - (this.height - 5.0f);
        this.infoScroll.setMaxScroll(Math.max(0.0f, hiddenHeight));
        StencilUtil.uninitStencilBuffer();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        this.faqButtons.forEach(faqButton -> faqButton.mouseClicked(mouseX, mouseY, button));
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
}
