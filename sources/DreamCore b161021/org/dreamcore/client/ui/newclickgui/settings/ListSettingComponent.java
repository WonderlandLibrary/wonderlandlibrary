package org.dreamcore.client.ui.newclickgui.settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.dreamcore.client.helpers.render.ScreenHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.ui.newclickgui.FeaturePanel;
import org.dreamcore.client.ui.newclickgui.Theme;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListSettingComponent extends Component {

    private final ScreenHelper screenHelper;
    private final Theme theme = new Theme();

    public ListSettingComponent(FeaturePanel featurePanel, ListSetting setting) {
        this.featurePanel = featurePanel;
        this.setting = setting;
        screenHelper = new ScreenHelper(0, 0);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getInstance());

        RectHelper.drawSmoothRect(x - mc.fontRenderer.getStringWidth((((ListSetting) setting).getCurrentMode())) - 15, y + 1, x, y + height - 3, new Color(26, 26, 26).getRGB());
        mc.circleregular.drawStringWithOutline(setting.getName(), scaledResolution.getScaledWidth() / 2F - 140, y + height / 2 - mc.fontRenderer.getFontHeight() / 2F, theme.textColor.getRGB());
        mc.clickguismall.drawStringWithOutline(((ListSetting) setting).getCurrentMode(), x - mc.clickguismall.getStringWidth(((ListSetting) setting).getCurrentMode()) - mc.fontRenderer.getStringWidth(">") - 10F, y + height / 2 - mc.fontRenderer.getFontHeight() / 2F, theme.textColor.getRGB());

        double xMid2 = x - mc.fontRenderer.getStringWidth(">") / 2F - 0.75F;
        double yMid2 = this.y + height / 2 + 0.75F;

        if (extended) {
            screenHelper.interpolate(0, 90, 4);
        } else {
            screenHelper.interpolate(0, 0, 4);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(xMid2, yMid2, 0);
        GlStateManager.rotate(screenHelper.getY(), 0, 0, 1);
        GlStateManager.translate(-xMid2, -yMid2, 0);
        float extendedOffset = extended ? 3 : 1.5f;
        float extendedYOffset = extended ? 2 : -1;
        mc.fontRenderer.drawStringWithShadow(">", x - mc.fontRenderer.getStringWidth(">") - extendedOffset, y + height / 2 - mc.fontRenderer.getFontHeight() / 2F + extendedYOffset, theme.textColor.getRGB());
        GlStateManager.popMatrix();

        if (!extended)
            return;

        int yPlus = (int) height;
        for (String sld : ((ListSetting) setting).getModes()) {
            if (!((ListSetting) setting).getCurrentMode().equals(sld)) {
                ArrayList<String> modesArray = new ArrayList<>(((ListSetting) setting).getModes());
                String max = Collections.max(modesArray, Comparator.comparing(String::length));
                RectHelper.drawSmoothRect(x - mc.clickguismall.getStringWidth(max) - 10, y + yPlus, x, y + height + yPlus - 1, new Color(29, 29, 29).getRGB());
                mc.clickguismall.drawStringWithOutline(sld, x - mc.clickguismall.getStringWidth(max) - 6, y + yPlus + height / 2 - mc.fontRenderer.getFontHeight() / 2F + 1, theme.textColor.getRGB());
                yPlus += height - 1;
            }
        }

        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovering(mouseX, mouseY) && mouseButton == 0) {
            extended = !extended;
        }
        if (!extended)
            return;

        int yPlus = (int) height;
        for (String modes : ((ListSetting) setting).getModes()) {
            if (!((ListSetting) setting).getCurrentMode().equals(modes)) {
                if (mouseX > x - width - 5 && mouseX < x && mouseY > y + yPlus && mouseY < y + yPlus + height && mouseButton == 0) {
                    ((ListSetting) setting).setListMode(modes);
                }
                yPlus += height;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX > x - width - 15 && mouseX < x && mouseY > y && mouseY < y + height - 1;
    }
}
