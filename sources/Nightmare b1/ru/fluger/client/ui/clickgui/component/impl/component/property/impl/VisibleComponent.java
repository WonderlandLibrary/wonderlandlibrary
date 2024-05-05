// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.clickgui.component.impl.component.property.impl;

import ru.fluger.client.settings.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import java.awt.Color;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.ui.clickgui.component.impl.component.property.PropertyComponent;
import ru.fluger.client.ui.clickgui.component.Component;

public class VisibleComponent extends Component implements PropertyComponent
{
    public Feature feature;
    private int opacity;
    
    public VisibleComponent(final Feature feature, final Component parent, final float x, final float y, final float width, final float height) {
        super(parent, "", x, y, width, height);
        this.opacity = 120;
        this.feature = feature;
    }
    
    @Override
    public void drawComponent(final bit scaledResolution, final int mouseX, final int mouseY) {
        final float x = (float)this.getX();
        final float y = (float)this.getY();
        final boolean hovered = this.isHovered(mouseX, mouseY);
        final float height = this.getHeight();
        final float width = this.getWidth();
        if (hovered) {
            if (this.opacity < 200) {
                this.opacity += 5;
            }
        }
        else if (this.opacity > 120) {
            this.opacity -= 5;
        }
        final Color onecolor = new Color(ClickGui.color.getColor());
        final Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        final int color = c.getRGB();
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 0).getRGB());
        RectHelper.drawGradientRect(x, y, x + width, y + height, RenderHelper.injectAlpha(new Color(color).darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(color).darker().darker().darker().darker(), 120).getRGB());
        VisibleComponent.mc.smallfontRenderer.drawStringWithShadow("Visible: " + ChatFormatting.GRAY + this.feature.visible, x + 4.0f, y + height / 3.0f, -1);
        super.drawComponent(scaledResolution, mouseX, mouseY);
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        if (this.isHovered(mouseX, mouseY) && button == 0) {
            this.feature.setVisible(!this.feature.isVisible());
        }
        super.onMouseClick(mouseX, mouseY, button);
    }
    
    @Override
    public void onMouseRelease(final int button) {
        super.onMouseRelease(button);
    }
    
    @Override
    public void onKeyPress(final int keyCode) {
        super.onKeyPress(keyCode);
    }
    
    @Override
    public Setting getProperty() {
        return null;
    }
}
