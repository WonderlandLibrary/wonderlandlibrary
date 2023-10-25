package org.dreamcore.client.ui.clickgui.component.impl;

import net.minecraft.client.gui.ScaledResolution;
import org.dreamcore.client.feature.impl.hud.ClickGui;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.AnimationHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.Setting;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.ui.clickgui.component.Component;
import org.dreamcore.client.ui.clickgui.component.PropertyComponent;

import java.awt.*;

public class BooleanSettingComponent extends Component implements PropertyComponent {

    public BooleanSetting booleanSetting;
    public float textHoverAnimate = 0f;
    public float leftRectAnimation = 0;
    public float rightRectAnimation = 0;

    public BooleanSettingComponent(Component parent, BooleanSetting booleanSetting, int x, int y, int width, int height) {
        super(parent, booleanSetting.getName(), x, y, width, height);
        this.booleanSetting = booleanSetting;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        if (booleanSetting.isVisible()) {
            int x = getX();
            int y = getY();
            int width = getWidth();
            int height = getHeight();
            int middleHeight = getHeight() / 2;
            boolean hovered = isHovered(mouseX, mouseY);
            int color = 0;
            Color onecolor = new Color(ClickGui.color.getColorValue());
            Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
            double speed = ClickGui.speed.getNumberValue();
            switch (ClickGui.clickGuiColor.currentMode) {
                case "Client":
                    color = PaletteHelper.fadeColor(ClientHelper.getClientColor().getRGB(), (ClientHelper.getClientColor().darker().getRGB()), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y * 6L / 60 * 2) % 2) - 1));
                    break;
                case "Fade":
                    color = PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1));
                    break;
                case "Color Two":
                    color = PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + y + height * 6L / 60F * 2) % 2) - 1));
                    break;
                case "Astolfo":
                    color = PaletteHelper.astolfo(true, y).getRGB();
                    break;
                case "Static":
                    color = onecolor.getRGB();
                    break;
                case "Rainbow":
                    color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                    break;
            }

            RectHelper.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 160).getRGB());
            mc.circleregularSmall.drawStringWithShadow(getName(), x + 3, y + middleHeight - 2, Color.GRAY.getRGB());
            textHoverAnimate = AnimationHelper.animation(textHoverAnimate, hovered ? 2.3f : 2, 0);
            leftRectAnimation = AnimationHelper.animation(leftRectAnimation, booleanSetting.getBoolValue() ? 10 : 17, 0);
            rightRectAnimation = AnimationHelper.animation(rightRectAnimation, (booleanSetting.getBoolValue() ? 3 : 10), 0);
            RectHelper.drawSmoothRect(x + width - 18, y + 5, x + width - 2, y + height - 5, new Color(14, 14, 14).getRGB());
            RectHelper.drawSmoothRect(x + width - leftRectAnimation, y + 6, x + width - rightRectAnimation, y + height - 6, booleanSetting.getBoolValue() ? color : new Color(50, 50, 50).getRGB());
            if (hovered) {
                if (booleanSetting.getDesc() != null) {
                    RectHelper.drawBorderedRect(x + 120, y + height / 1.5F + 3.5F, x + 138 + mc.fontRendererObj.getStringWidth(booleanSetting.getDesc()) - 10, y + 3.5F, 0.5F, new Color(30, 30, 30, 255).getRGB(), color, true);
                    mc.fontRendererObj.drawStringWithShadow(booleanSetting.getDesc(), x + 124, y + height / 1.5F - 6F, -1);
                }
            }
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && isHovered(mouseX, mouseY) && booleanSetting.isVisible()) {
            booleanSetting.setBoolValue(!booleanSetting.getBoolValue());
        }
    }

    @Override
    public Setting getSetting() {
        return booleanSetting;
    }
}
