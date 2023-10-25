/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.clickgui.component.impl.component.property.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import org.celestial.client.feature.Feature;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.settings.Setting;
import org.celestial.client.ui.clickgui.component.Component;
import org.celestial.client.ui.clickgui.component.impl.component.property.PropertyComponent;

public class VisibleComponent
extends Component
implements PropertyComponent {
    public Feature feature;
    private int opacity = 120;

    public VisibleComponent(Feature feature, Component parent, float x, float y, float width, float height) {
        super(parent, "", x, y, width, height);
        this.feature = feature;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        float x = (float)this.getX();
        float y = (float)this.getY();
        boolean hovered = this.isHovered(mouseX, mouseY);
        float height = this.getHeight();
        float width = this.getWidth();
        if (hovered) {
            if (this.opacity < 200) {
                this.opacity += 5;
            }
        } else if (this.opacity > 120) {
            this.opacity -= 5;
        }
        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 160).getRGB());
        VisibleComponent.mc.smallfontRenderer.drawStringWithShadow("Visible: " + (Object)((Object)ChatFormatting.GRAY) + this.feature.visible, x + 2.0f, y + height / 3.0f, -1);
        super.drawComponent(scaledResolution, mouseX, mouseY);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (this.isHovered(mouseX, mouseY) && button == 0) {
            this.feature.setVisible(!this.feature.isVisible());
        }
        super.onMouseClick(mouseX, mouseY, button);
    }

    @Override
    public void onMouseRelease(int button) {
        super.onMouseRelease(button);
    }

    @Override
    public void onKeyPress(int keyCode) {
        super.onKeyPress(keyCode);
    }

    @Override
    public Setting getProperty() {
        return null;
    }
}

