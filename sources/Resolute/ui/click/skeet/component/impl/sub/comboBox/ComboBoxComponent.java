// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet.component.impl.sub.comboBox;

import java.util.Iterator;
import vip.Resolute.util.font.MinecraftFontRenderer;
import java.util.List;
import java.util.Collections;
import vip.Resolute.util.font.FontUtil;
import org.lwjgl.opengl.GL11;
import vip.Resolute.util.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import vip.Resolute.ui.click.skeet.SkeetUI;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.ui.click.skeet.framework.Component;
import vip.Resolute.ui.click.skeet.framework.PredicateComponent;
import vip.Resolute.ui.click.skeet.framework.ButtonComponent;

public abstract class ComboBoxComponent extends ButtonComponent implements PredicateComponent
{
    private boolean expanded;
    String currentMode;
    
    public ComboBoxComponent(final Component parent, final float x, final float y, final float width, final float height) {
        super(parent, x, y, width, height);
    }
    
    private String getDisplayString() {
        return this.getMode().toString();
    }
    
    @Override
    public void drawComponent(final ScaledResolution lockedResolution, final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        final float width = this.getWidth();
        final float height = this.getHeight();
        Gui.drawRect(x, y, x + width, y + height, SkeetUI.getColor(855309));
        final boolean hovered = this.isHovered(mouseX, mouseY);
        RenderUtils.drawGradientRect(x + 0.5f, y + 0.5f, x + width - 0.5f, y + height - 0.5f, false, SkeetUI.getColor(hovered ? RenderUtils.darker(1973790, 1.4f) : 1973790), SkeetUI.getColor(hovered ? RenderUtils.darker(2302755, 1.4f) : 2302755));
        GL11.glColor4f(0.6f, 0.6f, 0.6f, (float)SkeetUI.getAlpha() / 255.0f);
        RenderUtils.drawAndRotateArrow(x + width - 5.0f, y + height / 2.0f - 0.5f, 3.0f, this.isExpanded());
        if (SkeetUI.shouldRenderText()) {
            GL11.glEnable(3089);
            RenderUtils.startScissorBox(lockedResolution, (int)x + 2, (int)y + 1, (int)width - 8, (int)height - 1);
            FontUtil.tahomaVerySmall.drawString((this.currentMode == null) ? this.getDisplayString() : this.currentMode, x + 2.0f, y + height / 3.0f, SkeetUI.getColor(9868950));
            GL11.glDisable(3089);
        }
        if (this.expanded) {
            GL11.glTranslatef(0.0f, 0.0f, 2.0f);
            final List<String> values = this.getModes();
            final float dropDownHeight = values.size() * height;
            Gui.drawRect(x, y + height, x + width, y + height + dropDownHeight + 0.5f, SkeetUI.getColor(855309));
            float valueBoxHeight = height;
            final List<String> var13;
            final List<String> enums = var13 = this.getModes();
            for (int var14 = enums.size(), var15 = 0; var15 < var14; ++var15) {
                final List<String> value = Collections.singletonList(var13.get(var15));
                final boolean valueBoxHovered = mouseX >= x && mouseY >= y + valueBoxHeight && mouseX <= x + width && mouseY < y + valueBoxHeight + height;
                Gui.drawRect(x + 0.5f, y + valueBoxHeight, x + width - 0.5f, y + valueBoxHeight + height, SkeetUI.getColor(valueBoxHovered ? RenderUtils.darker(2302755, 0.7f) : 2302755));
                final boolean selected = value == this.getMode();
                final int color = selected ? SkeetUI.getColor() : SkeetUI.getColor(14474460);
                final MinecraftFontRenderer fr = FontUtil.tahomaVerySmall;
                fr.drawString(value.toString(), x + 2.0f, y + valueBoxHeight + 4.0f, color);
                valueBoxHeight += height;
            }
            GL11.glTranslatef(0.0f, 0.0f, -2.0f);
        }
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        if (this.isHovered(mouseX, mouseY)) {
            this.onPress(button);
        }
        if (this.isExpanded() && button == 0) {
            final float x = this.getX();
            final float y = this.getY();
            final float height = this.getHeight();
            final float width = this.getWidth();
            float valueBoxHeight = height;
            for (final Object o : this.getModes()) {
                if (mouseX >= x && mouseY >= y + valueBoxHeight && mouseX <= x + width && mouseY <= y + valueBoxHeight + height) {
                    this.setSelected((String)o);
                    this.setCurrentMode((String)o);
                    return;
                }
                valueBoxHeight += height;
            }
        }
    }
    
    private void expandOrClose() {
        this.setExpanded(!this.isExpanded());
    }
    
    @Override
    public void onPress(final int mouseButton) {
        if (mouseButton == 1) {
            this.expandOrClose();
        }
    }
    
    public void setCurrentMode(final String mode) {
        this.currentMode = mode;
    }
    
    public String getSelectedMode() {
        return this.currentMode;
    }
    
    public float getExpandedX() {
        return this.getX();
    }
    
    public abstract List<String> getMode();
    
    public abstract void setSelected(final String p0);
    
    public abstract List<String> getModes();
    
    public boolean isExpanded() {
        return this.expanded;
    }
    
    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }
    
    public float getExpandedWidth() {
        return this.getWidth();
    }
    
    public float getExpandedHeight() {
        final float height = this.getHeight();
        return height + this.getModes().size() * height + height;
    }
}
