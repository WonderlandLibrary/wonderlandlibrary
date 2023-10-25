package org.dreamcore.client.ui.clickgui.component.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.Setting;
import org.dreamcore.client.ui.clickgui.component.Component;
import org.dreamcore.client.ui.clickgui.component.PropertyComponent;

import java.awt.*;

public class VisibleComponent extends Component implements PropertyComponent {

    public Feature feature;

    public VisibleComponent(Feature feature, Component parent, int x, int y, int width, int height) {
        super(parent, "", x, y, width, height);
        this.feature = feature;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        int x = getX();
        int y = getY();

        int height = this.getHeight();
        int width = this.getWidth();

        RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 160).getRGB());
        mc.circleregularSmall.drawStringWithShadow("Visible: " + ChatFormatting.WHITE + feature.visible, x + 2, y + height / 2.5F - 2, Color.LIGHT_GRAY.getRGB());
        super.drawComponent(scaledResolution, mouseX, mouseY);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (this.isHovered(mouseX, mouseY) && button == 0) {
            feature.setVisible(!feature.isVisible());
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
    public Setting getSetting() {
        return null;
    }
}
