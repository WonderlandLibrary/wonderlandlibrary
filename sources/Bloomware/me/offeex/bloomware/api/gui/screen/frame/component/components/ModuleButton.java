package me.offeex.bloomware.api.gui.screen.frame.component.components;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.gui.screen.frame.Frame;
import me.offeex.bloomware.api.gui.screen.frame.component.Component;
import me.offeex.bloomware.api.gui.screen.frame.component.components.setting.SettingButton;
import me.offeex.bloomware.api.gui.screen.frame.component.components.setting.settings.*;
import me.offeex.bloomware.api.manager.managers.FontManagerr;
import me.offeex.bloomware.api.structure.ColorMutable;
import me.offeex.bloomware.api.util.RenderUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.Setting;
import me.offeex.bloomware.client.setting.settings.*;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;



public class ModuleButton extends Component {
    public Module module;
    public Frame frame;
    private int offsetY;
    private boolean hovered;
    private final ArrayList<SettingButton> settingButtons;
    private boolean open;

    public ModuleButton(Module module, Frame frame, int offsetY) {
        this.module = module;
        this.frame = frame;
        this.offsetY = offsetY;
        this.settingButtons = new ArrayList<>();
        this.open = false;
        int settingY = this.offsetY + Component.COMPONENT_HEIGHT;
        module.getSettings().forEach(s -> {
            boolean isInGroup = false;
            for (Setting setting : module.getSettings()) {
                if (!(setting instanceof SettingGroup)) continue;
                for (Setting setting1 : ((SettingGroup) setting).getSettings())
                    if (setting1.equals(s)) {
                        isInGroup = true;
                        break;
                    }
            }

            if (!isInGroup) {
                if (s instanceof SettingGroup) settingButtons.add(new GroupButton((SettingGroup) s, this, settingY));
                else if (s instanceof SettingEnum) settingButtons.add(new ModeButton((SettingEnum) s, this, settingY));
                else if (s instanceof SettingMap) settingButtons.add(new BooleanButton((SettingMap) s, this, settingY));
                else if (s instanceof SettingBool)
                    settingButtons.add(new BooleanButton((SettingBool) s, this, settingY));
                else if (s instanceof SettingNumber)
                    settingButtons.add(new SliderButton((SettingNumber) s, this, settingY));
                else if (s instanceof SettingColor)
                    settingButtons.add(new ColorButton((SettingColor) s, this, settingY));
            }
        });
        settingButtons.add(new KeyButton(this, settingY));
    }

    @Override
    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
        int settingY = this.offsetY + SettingButton.COMPONENT_HEIGHT;
        for (SettingButton sb : settingButtons) {
            if (!sb.isActive()) continue;
            sb.setOffsetY(settingY);
            settingY += sb.getHeight();
        }
    }

    @Override
    public int getHeight() {
        return (open ? settingButtons.stream().map(sb -> sb.isActive() ? sb.getHeight() : 0).reduce(0, Integer::sum) : 0) + Component.COMPONENT_HEIGHT;
    }

    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        hovered = isHovered(mouseX, mouseY);
        for (SettingButton sb : settingButtons) {
            if (!sb.isActive()) continue;
            sb.updateComponent(mouseX, mouseY);
        }
    }

    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        for (SettingButton sb : settingButtons) {
            if (!sb.isActive() || !open) continue;
            sb.mouseClicked(mouseX, mouseY, button);
        }
        if (isHovered(mouseX, mouseY) && button == 0) {
            module.toggle();
            pressed = true;
        }
        if (isHovered(mouseX, mouseY) && button == 1) {
            open = !open;
            frame.update();
        }
    }

    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int button) {
        for (SettingButton sb : settingButtons) {
            if (!sb.isActive() || !open) continue;
            sb.mouseReleased(mouseX, mouseY, button);
        }
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void keyTyped(int key) {
        if (key == GLFW.GLFW_KEY_ESCAPE) pressed = false;
        settingButtons.forEach(component -> component.keyTyped(key));
    }

    @Override
    public void render(MatrixStack matrix) {
        DrawableHelper.fill(matrix, frame.getX(), frame.getY() + offsetY, frame.getX() + frame.getWidth(), frame.getY() + offsetY + Component.COMPONENT_HEIGHT,
                pressed ? colors.getPressed().getColor().getArgb() : hovered ? colors.getHovered().getColor().getArgb() : colors.getList().getColor().getArgb());

        if (module.getSettings().size() > 1) {
            DrawableHelper.fill(matrix, frame.getX() + frame.getWidth() - 3, frame.getY() + offsetY + 2, frame.getX() + frame.getWidth(), frame.getY() + offsetY + 12, colors.getBorders().getColor().getArgb());
        }

//        module name
        float offsetWidth = 0;
        switch (gui.getModuleOffset().getSelected()) {
            case "Center" -> offsetWidth = frame.getWidth() / 2f - FontManagerr.INSTANCE.width(module.getName()) / 2f;
            case "Left" -> offsetWidth = 3;
            case "Right" -> offsetWidth = frame.getWidth() - FontManagerr.INSTANCE.width(module.getName()) - 3;
        }

        if (module.getEnabled()) {
            switch (gui.getEnabledStyle().getSelected()) {
                case "Glow" -> RenderUtil.INSTANCE.drawGlow2D(matrix, (int) (frame.getX() + offsetWidth + 2), frame.getY() + offsetY, (int) (frame.getX() + offsetWidth + FontManagerr.INSTANCE.width(module.getName())) - 2, frame.getY() + offsetY + Component.COMPONENT_HEIGHT, colors.getText().getColor().withAlphaToRGBA(50));
                case "Fill" -> DrawableHelper.fill(matrix, frame.getX(), frame.getY() + offsetY, frame.getX() + frame.getWidth(), frame.getY() + offsetY + COMPONENT_HEIGHT,  colors.getText().getColor().getArgb());
            }
        }
        FontManagerr.INSTANCE.drawString(matrix, module.getName(), frame.getX() + offsetWidth, frame.getY() + offsetY, module.getEnabled() && !gui.getEnabledStyle().like("Fill") ?  colors.getText().getColor() : ColorMutable.Companion.getWHITE(), null);

        if (open) {
            for (SettingButton sb : settingButtons) {
                if (!sb.isActive()) continue;
                sb.render(matrix);
            }
        }

//        description
        if (hovered) FontManagerr.INSTANCE.drawString(matrix, module.getDescription(), Bloomware.INSTANCE.getMc().getWindow().getScaledWidth() / 2f - FontManagerr.INSTANCE.width(module.getDescription()) / 2f, Bloomware.INSTANCE.getMc().getWindow().getScaledHeight() - 100, ColorMutable.Companion.getWHITE(), null);
    }

    public boolean isHovered(final double x, final double y) {
        return x > this.frame.getX() && x < this.frame.getX() + this.frame.getWidth() && y > this.frame.getY() + this.offsetY && y < this.frame.getY() + Component.COMPONENT_HEIGHT + this.offsetY;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public ArrayList<SettingButton> getSettingButtons() {
        return settingButtons;
    }

    public boolean isOpen() {
        return open;
    }
}