package wtf.expensive.ui.beta.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import wtf.expensive.config.Config;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.settings.Setting;
import wtf.expensive.modules.settings.imp.*;
import wtf.expensive.ui.beta.ClickGui;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.SmartScissor;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static wtf.expensive.util.render.RenderUtil.reAlphaInt;

public class ConfigComponent extends Component {

    public Config config;
    public boolean selected;
    public float animation;
    public float animation1;

    public ConfigComponent(Config config) {
        this.config = config;
    }


    @Override
    public void drawComponent(MatrixStack matrixStack, int mouseX, int mouseY) {
        updateAnimation(mouseX, mouseY);
        drawBackground();
        drawFileName(matrixStack);
        drawButtons(matrixStack);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isSaveLoadButtonClicked(mouseX, mouseY)) {
            handleSaveLoadButtonClick();
        } else if (isDeleteButtonClicked(mouseX, mouseY)) {
            handleDeleteButtonClick();
        }
    }

    private void updateAnimation(int mouseX, int mouseY) {
        boolean isInRegion = RenderUtil.isInRegion(mouseX, mouseY, x + width - 35 - 2, y + 2, 35 - 2, 32 / 2f);
        animation = AnimationMath.lerp(animation, (selected | isInRegion) ? 1 : 0, 10);

        isInRegion = RenderUtil.isInRegion(mouseX, mouseY, x + width - 35 - 35 - 2, y + 2, 35 - 2, 32 / 2f);
        animation1 = AnimationMath.lerp(animation1, (selected | isInRegion) ? 1 : 0, 10);

    }

    private void drawBackground() {
        RenderUtil.Render2D.drawShadow(x + 3, y, width - 6, height, 12, new Color(32, 36, 42).darker().darker().getRGB());
        RenderUtil.Render2D.drawRoundedCorner(x + 3, y, width - 5, height, 4, new Color(32, 36, 42).getRGB());
    }

    private void drawFileName(MatrixStack matrixStack) {
        Fonts.gilroy[16].drawString(matrixStack, config.getFile().getName(), x + 8, y + 7, -1);
    }

    private void drawButtons(MatrixStack matrixStack) {
        RenderUtil.Render2D.drawRoundedCorner(x + width - 35 - 35 - 2, y + 2, 35 - 2, 32 / 2f, 4, ColorUtil.interpolateColor(RenderUtil.IntColor.rgba(74, 166, 218, 255), RenderUtil.IntColor.rgba(22, 24, 28, 255), 1 - animation1));
        RenderUtil.Render2D.drawRoundedCorner(x + width - 35 - 2, y + 2, 35 - 2, 32 / 2f, 4, ColorUtil.interpolateColor(RenderUtil.IntColor.rgba(74, 166, 218, 255), RenderUtil.IntColor.rgba(22, 24, 28, 255), 1 - animation));
        Fonts.gilroy[14].drawCenteredString(matrixStack, selected ? "Save" : "Load", x + width - 35 - 1.5F + 32F / 2F, y + 8, -1);
        Fonts.gilroy[14].drawCenteredString(matrixStack, "Delete", x + width - 35 - 35 - 1.5F + 32F / 2F, y + 8, -1);
    }

    private boolean isSaveLoadButtonClicked(int mouseX, int mouseY) {
        return RenderUtil.isInRegion(mouseX, mouseY, x + width - 35 - 2, y + 2, 35 - 2, 32 / 2f);
    }

    private void handleSaveLoadButtonClick() {
        if (!selected) {
            Managment.CONFIG_MANAGER.loadConfiguration(config.getFile().getName().replace(".cfg", ""), false);
            ClickGui.confign = config.getFile().getName();
        } else {
            Managment.CONFIG_MANAGER.saveConfiguration(config.getFile().getName().replace(".cfg", ""));
        }
    }

    private boolean isDeleteButtonClicked(int mouseX, int mouseY) {
        return RenderUtil.isInRegion(mouseX, mouseY, x + width - 35 - 35 - 2, y + 2, 35 - 2, 32 / 2f);
    }

    private void handleDeleteButtonClick() {
        Managment.CONFIG_MANAGER.deleteConfig(config.getFile().getName().replace(".cfg", ""));
        parent.cfg.clear();
        for (String config : Managment.CONFIG_MANAGER.getAllConfigurations()) {
            parent.cfg.add(new ConfigComponent(Managment.CONFIG_MANAGER.findConfig(config)));
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {

    }

    @Override
    public void charTyped(char codePoint, int modifiers) {

    }
}
