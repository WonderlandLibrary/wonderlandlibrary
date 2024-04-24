package me.offeex.bloomware.api.gui.screen.frame.component.components.setting;

import kotlin.Unit;
import me.offeex.bloomware.api.gui.screen.frame.component.Component;
import me.offeex.bloomware.api.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.api.gui.screen.frame.component.components.setting.settings.KeyButton;
import me.offeex.bloomware.client.setting.Setting;
import me.offeex.bloomware.client.setting.SettingsContainer;
import me.offeex.bloomware.client.setting.settings.SettingGroup;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public abstract class SettingButton extends Component {
    protected Setting setting;
    protected final ModuleButton button;
    protected boolean isHovered;
    protected int offsetY;
    protected int x;
    protected int y;

    public SettingButton(Setting setting, ModuleButton button, int offsetY) {
        this.setting = setting;
        this.button = button;
        this.x = button.frame.getX() + button.frame.getWidth();
        this.y = button.frame.getY() + button.getOffsetY();
        this.offsetY = offsetY;
        setting.getUpdateBus().subscribe(() -> {
            button.frame.update();
            return Unit.INSTANCE;
        });
        if (!(this instanceof KeyButton))
            offsetNested += 3 * calcNestingLevel(0, setting.parent);

    }

    public SettingButton(ModuleButton button, int offsetY) {
        this.button = button;
        this.x = button.frame.getX() + button.frame.getWidth();
        this.y = button.frame.getY() + button.getOffsetY();
        this.offsetY = offsetY;
    }

    @Override
    public void render(MatrixStack matrix) {
        DrawableHelper.fill(matrix,
                button.frame.getX(),
                button.frame.getY() + offsetY,
                button.frame.getX() + button.frame.getWidth(),
                button.frame.getY() + offsetY + COMPONENT_HEIGHT,
                pressed ? colors.getPressed().getColor().getArgb() : isHovered
                        ? colors.getHovered().getColor().getArgb() : colors.getList().getColor().getArgb()
        );
    }

    public boolean isActive() {
        return this instanceof KeyButton || setting.getActive();
    }

    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + button.frame.getWidth() && y > this.y && y < this.y + COMPONENT_HEIGHT;
    }

    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        isHovered = isHovered(mouseX, mouseY);
        x = button.frame.getX();
        y = button.frame.getY() + this.offsetY;
    }

    private int calcNestingLevel(int baseLevel, SettingsContainer parent) {
        if (parent instanceof SettingGroup sg) return calcNestingLevel(baseLevel + 1, sg.parent);
        else return baseLevel;
    }

    public Setting getSetting() {
        return setting;
    }

    @Override
    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }
}
