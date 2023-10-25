package org.dreamcore.client.ui.newclickgui.settings;

import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.feature.impl.hud.ClickGui;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.ScreenHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.ui.newclickgui.FeaturePanel;
import org.dreamcore.client.ui.newclickgui.Theme;

import java.awt.*;

public class BooleanSettingComponent extends Component {

    private final Theme theme = new Theme();
    private final ScreenHelper screenHelper;
    private float renderOffset;

    public BooleanSettingComponent(FeaturePanel featurePanel, BooleanSetting setting) {
        this.featurePanel = featurePanel;
        this.setting = setting;
        this.screenHelper = new ScreenHelper(0, 0);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        RectHelper.drawSmoothRect(x + width - 1, y + height / 2 - 5, x + width + 9, y + height / 2 + 5, theme.guiColor3.getRGB());

        mc.circleregular.drawStringWithOutline(setting.getName(), x, y + height / 2 - mc.circleregular.getFontHeight() / 2F + 0.5F, theme.textColor.getRGB());
        mc.circleregular.drawStringWithOutline(setting.getName(), x, y + height / 2 - mc.circleregular.getFontHeight() / 2F + 0.5F, theme.textColor.getRGB());

        int color = 0;
        Color onecolor = new Color(ClickGui.color.getColorValue());
        Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
        double speed = ClickGui.speed.getNumberValue();
        switch (ClickGui.clickGuiColor.currentMode) {
            case "Client":
                color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + height * 6L / 60 * 2) % 2) - 1));
                break;
            case "Fade":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + height * 6L / 60F * 2) % 2) - 1));
                break;
            case "Color Two":
                color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + height * 6L / 60F * 2) % 2) - 1));
                break;
            case "Astolfo":
                color = PaletteHelper.astolfo(true, (int) height).getRGB();
                break;
            case "Static":
                color = onecolor.getRGB();
                break;
            case "Rainbow":
                color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                break;
        }

        if (((BooleanSetting) setting).getBoolValue()) {
            screenHelper.interpolate(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight() + 5, 6);
            if (renderOffset < 0.5F) {
                renderOffset += 0.002 * RectHelper.delta;
            }
        } else {
            screenHelper.interpolate(0, 0, 6);
            renderOffset = 0;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef((x + width + 2), y + height / 2 - 4, 0);
        GL11.glScaled(screenHelper.getX() / scaledResolution.getScaledWidth() - renderOffset, screenHelper.getY() / scaledResolution.getScaledHeight() - renderOffset, 0);
        GL11.glTranslatef(-(x + width + 2), -(y + height / 2 - 4), 0);

        if (screenHelper.getX() > 250) {
            RectHelper.drawVerticalGradientSmoothRect(x + width - 1, y + height / 2 - 5, x + width + 9, y + height / 2 + 5, color, new Color(color).darker().getRGB());
        }

        GL11.glPopMatrix();

        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovering(mouseX, mouseY) && mouseButton == 0) {
            ((BooleanSetting) setting).setBoolValue(!((BooleanSetting) setting).getBoolValue());
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX > x + width && mouseX < x + width + 12 && mouseY > y && mouseY < y + height;
    }

}
