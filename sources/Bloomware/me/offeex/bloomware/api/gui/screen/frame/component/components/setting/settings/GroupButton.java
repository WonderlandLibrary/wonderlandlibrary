package me.offeex.bloomware.api.gui.screen.frame.component.components.setting.settings;

import me.offeex.bloomware.api.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.api.gui.screen.frame.component.components.setting.SettingButton;
import me.offeex.bloomware.api.manager.managers.FontManagerr;
import me.offeex.bloomware.api.structure.ColorMutable;
import me.offeex.bloomware.client.setting.Setting;
import me.offeex.bloomware.client.setting.settings.*;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;

public class GroupButton extends SettingButton {
    private final SettingGroup setting = (SettingGroup) super.setting;
    private final ArrayList<SettingButton> settingButtons = new ArrayList<>();
    private boolean open;

    public GroupButton(final SettingGroup setting, ModuleButton button, int offsetY) {
        super(setting, button, offsetY);
        int settingY = this.offsetY + COMPONENT_HEIGHT;
        for (Setting s : setting.getSettings()) {
            if (s instanceof SettingGroup) settingButtons.add(new GroupButton((SettingGroup) s, button, settingY));
            else if (s instanceof SettingEnum) settingButtons.add(new ModeButton((SettingEnum) s, button, settingY));
            else if (s instanceof SettingMap) settingButtons.add(new BooleanButton((SettingMap) s, button, settingY));
            else if (s instanceof SettingBool) settingButtons.add(new BooleanButton((SettingBool) s, button, settingY));
            else if (s instanceof SettingNumber)
                settingButtons.add(new SliderButton((SettingNumber) s, button, settingY));
            else if (s instanceof SettingColor) settingButtons.add(new ColorButton((SettingColor) s, button, settingY));
        }
    }

    @Override
    public int getHeight() {
        return (open ? settingButtons.stream().map(sb -> sb.isActive() ? sb.getHeight() : 0).reduce(0, Integer::sum) : 0) + COMPONENT_HEIGHT;
    }

    @Override
    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
        int settingY = this.offsetY + COMPONENT_HEIGHT;
        for (SettingButton sb : settingButtons) {
            if (!sb.isActive()) continue;
            sb.setOffsetY(settingY);
            settingY += sb.getHeight();
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            super.mouseClicked(mouseX, mouseY, button);
            if (button == 0 && setting.getToggleable()) setting.setToggled(!setting.getToggled());
            if (button == 1) {
                open = !open;
                this.button.frame.update();
            }
        }

        if (open && this.button.isOpen()) settingButtons.forEach(c -> {
            if (c.isActive()) c.mouseClicked(mouseX, mouseY, button);
        });
    }

    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
        super.mouseReleased(mouseX, mouseY, button);
        if (open && this.button.isOpen()) settingButtons.forEach(c -> {
            if (c.isActive()) c.mouseReleased(mouseX, mouseY, button);
        });
    }

    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        super.updateComponent(mouseX, mouseY);
        if (open && this.button.isOpen()) settingButtons.forEach(c -> {
            if (c.isActive()) c.updateComponent(mouseX, mouseY);
        });
    }

    @Override
    public void render(MatrixStack matrix) {
        super.render(matrix);
        FontManagerr.INSTANCE.drawString(
                matrix,
                setting.getName(),
                button.frame.getX() + offsetNested,
                button.frame.getY() + offsetY - (FontManagerr.INSTANCE.height(setting.getName()) - 7) / 2f,
                setting.getToggleable() && setting.getToggled() ? colors.getText().getColor() : ColorMutable.Companion.getWHITE(), null
        );

        String text = !open ? "..." : "|";
        FontManagerr.INSTANCE.drawString(
                matrix,
                text,
                button.frame.getX() + button.frame.getWidth() - 2 - FontManagerr.INSTANCE.width(text),
                button.frame.getY() + offsetY - (FontManagerr.INSTANCE.height(text) - 7) / 2f,
                ColorMutable.Companion.getWHITE(), null);

        if (open && this.button.isOpen()) settingButtons.forEach(c -> {
            if (c.isActive()) c.render(matrix);
        });
    }
}
