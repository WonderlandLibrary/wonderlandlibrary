package org.dreamcore.client.ui.newclickgui.settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.feature.impl.hud.ClickGui;
import org.dreamcore.client.helpers.math.MathematicHelper;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.AnimationHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.helpers.render.ScreenHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.impl.NumberSetting;
import org.dreamcore.client.ui.newclickgui.FeaturePanel;
import org.dreamcore.client.ui.newclickgui.Theme;

import java.awt.*;

public class NumberSettingComponent extends Component {

    private final Theme theme = new Theme();
    private final ScreenHelper screenHelper;
    public float currentValueAnimate = 0f;
    private boolean dragging;
    private float renderOffset;

    public NumberSettingComponent(FeaturePanel featurePanel, NumberSetting setting) {
        this.featurePanel = featurePanel;
        this.setting = setting;
        this.screenHelper = new ScreenHelper(0, 0);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getInstance());

        float min = ((NumberSetting) setting).getMinValue();
        float max = ((NumberSetting) setting).getMaxValue();
        float value = (float) MathematicHelper.round(((NumberSetting) setting).getNumberValue(), ((NumberSetting) setting).getIncrement());
        float amountWidth = ((((NumberSetting) setting).getNumberValue()) - min) / (max - min);

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

        mc.circleregular.drawStringWithOutline(setting.getName(), scaledResolution.getScaledWidth() / 2F - 140, y + height / 2 - mc.circleregular.getFontHeight() / 2F - 1, theme.textColor.getRGB());
        mc.circleregular.drawStringWithOutline(setting.getName(), scaledResolution.getScaledWidth() / 2F - 140, y + height / 2 - mc.circleregular.getFontHeight() / 2F - 1, theme.textColor.getRGB());

        currentValueAnimate = AnimationHelper.animation(currentValueAnimate, amountWidth, 0);
        RectHelper.drawSmoothRect(x, y + height / 2 - 1, x + width, y + height / 2 + 2, theme.panelColor.getRGB());

        RectHelper.drawSmoothGradientRect(x, y + height / 2 - 1, (x + currentValueAnimate * width), y + height / 2 + 2, color, new Color(color).darker().getRGB());

        String valueString = "";

        NumberSetting.NumberType numberType = ((NumberSetting) setting).getType();

        switch (numberType) {
            case PERCENTAGE:
                valueString += '%';
                break;
            case MS:
                valueString += "ms";
                break;
            case DISTANCE:
                valueString += 'm';
            case SIZE:
                valueString += "SIZE";
            case APS:
                valueString += "APS";
                break;
            default:
                valueString = "";
        }

        if (isHovering(mouseX, mouseY) || dragging) {
            screenHelper.interpolate(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight() + 5, 6);
            if (renderOffset < 0.5F) {
                renderOffset += 0.002 * RectHelper.delta;
            }
        } else {
            screenHelper.interpolate(0, 0, 6);
            renderOffset = 0;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef((x + currentValueAnimate * width), y + height / 2, 0);
        GL11.glScaled(0.5 + screenHelper.getX() / scaledResolution.getScaledWidth() - renderOffset, 0.5 + screenHelper.getY() / scaledResolution.getScaledHeight() - renderOffset, 0);
        GL11.glTranslatef(-(x + currentValueAnimate * width), -(y + height / 2), 0);

        if (screenHelper.getX() > 250) {
            RenderHelper.drawCircle((x + currentValueAnimate * width), y + height / 2 + 0.5F, 2, true, Color.WHITE);
        }

        GL11.glPopMatrix();

        if (isHovering(mouseX, mouseY)) {
            mc.clickguismall.drawStringWithOutline(value + valueString, x - 5 - mc.clickguismall.getStringWidth(value + valueString), y + 5, theme.textColor.getRGB());
        }

        if (dragging) {
            ((NumberSetting) setting).setValueNumber((float) MathematicHelper.round((mouseX - x) * (max - min) / width + min, ((NumberSetting) setting).getIncrement()));
            if (((NumberSetting) setting).getNumberValue() > max) {
                ((NumberSetting) setting).setValueNumber(max);
            } else if (((NumberSetting) setting).getNumberValue() < min) {
                ((NumberSetting) setting).setValueNumber(min);
            }
        }

        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovering(mouseX, mouseY) && mouseButton == 0) {
            dragging = true;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX > x - 10 && mouseX < x + width && mouseY > y + height / 2 - 5 && mouseY < y + height / 2 + 5;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
        super.mouseReleased(mouseX, mouseY, state);
    }
}


