// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.compact;

import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.render.RoundedUtil;
import java.awt.Color;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.Tenacity;
import dev.tenacity.module.ModuleCollection;
import dev.tenacity.utils.objects.Scroll;
import java.util.HashMap;
import dev.tenacity.ui.clickguis.compact.impl.ModuleRect;
import java.util.List;
import dev.tenacity.module.Category;
import dev.tenacity.ui.Screen;

public class ModulePanel implements Screen
{
    public float x;
    public float y;
    public float rectWidth;
    public float rectHeight;
    public Category currentCat;
    public List<ModuleRect> moduleRects;
    private HashMap<Category, Scroll> scrollHashMap;
    private boolean draggingScrollBar;
    public boolean typing;
    
    @Override
    public void initGui() {
        this.scrollHashMap = new HashMap<Category, Scroll>();
        for (final Category category : Category.values()) {
            this.scrollHashMap.put(category, new Scroll());
        }
        this.scrollHashMap.put(null, new Scroll());
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        this.moduleRects.forEach(moduleRect -> moduleRect.keyTyped(typedChar, keyCode));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        this.typing = false;
        if (ModuleCollection.reloadModules) {
            this.initGui();
            ModuleCollection.reloadModules = false;
            return;
        }
        int count = 0;
        float leftSideHeight = 0.0f;
        float rightSideHeight = 0.0f;
        final float maxScrollbarHeight = this.rectHeight - 10.0f;
        final Scroll scroll = this.scrollHashMap.get(this.currentCat);
        if (!Tenacity.INSTANCE.getSideGui().isFocused()) {
            scroll.onScroll(35);
        }
        for (final ModuleRect moduleRect : this.moduleRects) {
            final boolean rightSide = count % 2 == 1;
            moduleRect.rectWidth = (this.rectWidth - 130.0f) / 2.0f;
            moduleRect.width = this.rectWidth;
            moduleRect.height = this.rectHeight;
            moduleRect.x = this.x + 100.0f + (rightSide ? (moduleRect.rectWidth + 10.0f) : 0.0f);
            moduleRect.y = (float)(this.y + 10.0f + (rightSide ? rightSideHeight : leftSideHeight) + MathUtils.roundToHalf(scroll.getScroll()));
            moduleRect.drawScreen(mouseX, mouseY);
            if (!this.typing) {
                this.typing = moduleRect.typing;
            }
            if (rightSide) {
                rightSideHeight += moduleRect.rectHeight + 30.0f;
            }
            else {
                leftSideHeight += moduleRect.rectHeight + 30.0f;
            }
            ++count;
        }
        scroll.setMaxScroll(Math.max(0.0f, Math.max(leftSideHeight, rightSideHeight) - 100.0f));
        float scrollBarHeight = maxScrollbarHeight * (this.rectHeight / scroll.getMaxScroll());
        scrollBarHeight = Math.min(this.rectHeight - 10.0f, scrollBarHeight);
        final float scrollYMath = -scroll.getScroll() / scroll.getMaxScroll() * (maxScrollbarHeight - scrollBarHeight);
        final boolean hoveredScrollBar = HoveringUtil.isHovering(this.x + this.rectWidth - 10.0f, this.y + 5.0f + scrollYMath, 5.0f, scrollBarHeight, mouseX, mouseY);
        RoundedUtil.drawRound(this.x + this.rectWidth - 10.0f, this.y + 5.0f, 5.0f, maxScrollbarHeight, 2.0f, new Color(32, 32, 32));
        final Color scrollColor = new Color(41, 43, 47);
        RoundedUtil.drawRound(this.x + this.rectWidth - 10.0f, this.y + 5.0f + scrollYMath, 5.0f, scrollBarHeight, 2.0f, (hoveredScrollBar || this.draggingScrollBar) ? ColorUtil.brighter(scrollColor, 0.8f) : scrollColor);
        Gui.drawRect2(this.x + this.rectWidth - 9.0f, this.y + 5.0f + scrollYMath + scrollBarHeight / 2.0f - 2.0f, 3.0, 0.5, new Color(64, 68, 75).getRGB());
        Gui.drawRect2(this.x + this.rectWidth - 9.0f, this.y + 5.0f + scrollYMath + scrollBarHeight / 2.0f - 0.5, 3.0, 0.5, new Color(64, 68, 75).getRGB());
        Gui.drawRect2(this.x + this.rectWidth - 9.0f, this.y + 5.0f + scrollYMath + scrollBarHeight / 2.0f + 1.0f, 3.0, 0.5, new Color(64, 68, 75).getRGB());
        if (this.draggingScrollBar) {
            final float percentOfScrollableHeight = (this.y + 5.0f - mouseY) / maxScrollbarHeight;
            scroll.setRawScroll(Math.max(Math.min(0.0f, scroll.getMaxScroll() * percentOfScrollableHeight), -scroll.getMaxScroll()));
        }
    }
    
    public void drawTooltips(final int mouseX, final int mouseY) {
        this.moduleRects.forEach(moduleRect -> moduleRect.tooltipObject.drawScreen(mouseX, mouseY));
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final float maxScrollbarHeight = this.rectHeight - 10.0f;
        final Scroll scroll = this.scrollHashMap.get(this.currentCat);
        final float scrollBarHeight = maxScrollbarHeight * (this.rectHeight / scroll.getMaxScroll());
        final float scrollYMath = -scroll.getScroll() / scroll.getMaxScroll() * (maxScrollbarHeight - scrollBarHeight);
        final boolean hoveredScrollBar = HoveringUtil.isHovering(this.x + this.rectWidth - 10.0f, this.y + 5.0f + scrollYMath, 5.0f, scrollBarHeight, mouseX, mouseY);
        if (hoveredScrollBar && button == 0) {
            this.draggingScrollBar = true;
        }
        this.moduleRects.forEach(moduleRect -> moduleRect.mouseClicked(mouseX, mouseY, button));
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.draggingScrollBar) {
            this.draggingScrollBar = false;
        }
        this.moduleRects.forEach(moduleRect -> moduleRect.mouseReleased(mouseX, mouseY, state));
    }
}
