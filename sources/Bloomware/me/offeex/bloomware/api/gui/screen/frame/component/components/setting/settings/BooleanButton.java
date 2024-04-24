package me.offeex.bloomware.api.gui.screen.frame.component.components.setting.settings;

import me.offeex.bloomware.api.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.api.gui.screen.frame.component.components.setting.SettingButton;
import me.offeex.bloomware.api.manager.managers.FontManagerr;
import me.offeex.bloomware.api.structure.ColorMutable;
import me.offeex.bloomware.client.setting.settings.SettingBool;
import net.minecraft.client.util.math.MatrixStack;

public class BooleanButton extends SettingButton {
    private final SettingBool setting = (SettingBool) super.setting;

    public BooleanButton(SettingBool setting, ModuleButton button, int offsetY) {
        super(setting, button, offsetY);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY) && button == 0 && super.button.isOpen()) {
            super.mouseClicked(mouseX, mouseY, button);
            setting.setToggled(!setting.getToggled());
        }
    }

    @Override
    public void render(MatrixStack matrix) {
        super.render(matrix);
        FontManagerr.INSTANCE.drawString(matrix, setting.getName(), button.frame.getX() + offsetNested, button.frame.getY() + offsetY - (FontManagerr.INSTANCE.height(setting.getName()) - 7) / 2f, setting.getToggled() ? colors.getText().getColor() : ColorMutable.Companion.getWHITE(), null);
    }
}
