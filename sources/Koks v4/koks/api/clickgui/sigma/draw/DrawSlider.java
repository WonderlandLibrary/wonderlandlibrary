package koks.api.clickgui.sigma.draw;

import koks.api.clickgui.Element;
import koks.api.font.Fonts;
import koks.api.manager.value.Value;
import koks.api.manager.value.ValueManager;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.Animation;
import koks.api.utils.RenderUtil;
import koks.api.utils.Resolution;
import koks.module.gui.ClickGUI;
import net.minecraft.client.gui.Gui;

import java.awt.*;

/**
 * @author kroko
 * @created on 13.02.2021 : 03:01
 */
public class DrawSlider extends Element {
    public boolean dragging;

    public DrawSlider(Value<?> value) {
        super(Fonts.arial18);
        this.value = value;
    }

    public Animation animation = new Animation();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        height = 20 + 5;
        final RenderUtil renderUtil = RenderUtil.getInstance();
        final Resolution resolution = Resolution.getResolution();
        final String name = value.getDisplayName() != null ? value.getDisplayName() : value.getName();

        int elementWidth = width / 2;

        fr.drawString(name, x + 40, y - height / 2F - 3 - fr.getStringHeight(name) + height, new Color(0xFFFFFFFF), true);
        fr.drawString((value.castDouble() + "").replace(".", ","), x + width - fr.getStringWidth((value.castDouble() + "").replace(".", ",")) / 2F + 7, y - height / 2F - 3 - fr.getStringHeight(name) + height, Color.white, true);

        resetButtonDistance = (int) (40 + fr.getStringWidth(name)) + 2;
        resetButtonHeight = -1;
        hasResetButton = !value.getValue().equals(value.getDefaultValue());

        Gui.drawRect(x + 45, y + height / 2, (x + elementWidth + 45), y + height / 2 + 3, 0xFF101010);

        double currentValue = (value.castDouble() - value.getMinimum()) / (value.getMaximum() - value.getMinimum());

        final ClickGUI clickGUI = ModuleRegistry.getModule(ClickGUI.class);
        final Color customColor = new Color(ValueManager.getInstance().getValue("Color", clickGUI).castInteger());

        renderUtil.drawRect(x + 45, y + height / 2F, (x + (currentValue * elementWidth) + 45), (float) (y + height / 2 + 3), customColor.getRGB());

        animation.setSpeed(165F);
        if(isHovered(mouseX, mouseY) || dragging) {
            animation.setGoalX(3);
            float radius = animation.getAnimationX();
            renderUtil.drawCircle((x + (currentValue * elementWidth) + 45 - radius / 2), (float) (y + height / 2 + radius / 2), radius, customColor.getRGB());
        } else {
            animation.setX(0);
        }

        if (dragging)
            updateValue(mouseX);
    }

    public void updateValue(double mouseX) {
        int elementWidth = width / 2;
        float currentValue = (float) (Math.round((float) Math.max(Math.min((mouseX - (x + 45)) / elementWidth * (value.getMaximum() - value.getMinimum()) + value.getMinimum(), value.getMaximum()), value.getMinimum()) * 100.0F) / 100.0D);
        value.castIfPossible(value.getValue() instanceof Integer ? Math.round(currentValue) + "" : currentValue + "");
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x + 45 && mouseX <= x + width / 2 + 45 && mouseY >= y + height / 2 && mouseY <= y + height / 2 + 3;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                dragging = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }
}
