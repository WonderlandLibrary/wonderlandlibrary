package me.offeex.bloomware.api.gui.screen.frame.component.components.setting.settings;

import me.offeex.bloomware.api.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.api.gui.screen.frame.component.components.setting.SettingButton;
import me.offeex.bloomware.api.manager.managers.FontManagerr;
import me.offeex.bloomware.client.setting.settings.SettingEnum;
import net.minecraft.client.util.math.MatrixStack;

public class ModeButton extends SettingButton {
    private final SettingEnum setting = (SettingEnum) super.setting;

    public ModeButton(final SettingEnum setting, ModuleButton button, int offsetY) {
        super(setting, button, offsetY);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY) && this.button.isOpen()) {
            super.mouseClicked(mouseX, mouseY, button);
            int index = setting.getModes().indexOf(setting.getSelected());
            int size = setting.getModes().size();
            setting.setSelected(setting.getModes().get(Math.abs(button == 0 ? ++index : --index) % size));
        }
    }

    @Override
    public void render(MatrixStack matrix) {
        super.render(matrix);
        FontManagerr.INSTANCE.drawString(matrix, setting.getName(), button.frame.getX() + offsetNested, button.frame.getY() + offsetY - (FontManagerr.INSTANCE.height(setting.getName()) - 7) / 2f, colors.getText().getColor(), null);
        FontManagerr.INSTANCE.drawString(matrix, setting.getSelected(), button.frame.getX() + button.frame.getWidth() - 2 - FontManagerr.INSTANCE.width(setting.getSelected()), button.frame.getY() + offsetY - (FontManagerr.INSTANCE.height(setting.getSelected()) - 7) / 2f, colors.getText().getColor(), null);
    }
}
