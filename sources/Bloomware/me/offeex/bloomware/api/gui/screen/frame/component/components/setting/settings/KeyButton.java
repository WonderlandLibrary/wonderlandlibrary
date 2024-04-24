package me.offeex.bloomware.api.gui.screen.frame.component.components.setting.settings;

import me.offeex.bloomware.api.gui.screen.frame.component.components.ModuleButton;
import me.offeex.bloomware.api.gui.screen.frame.component.components.setting.SettingButton;
import me.offeex.bloomware.api.manager.managers.FontManagerr;
import me.offeex.bloomware.api.structure.ColorMutable;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;

public class KeyButton extends SettingButton {
    private boolean binding;

    public KeyButton(ModuleButton button, int offsetY) {
        super(button, offsetY);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY) && button == 0 && this.button.isOpen()) {
            super.mouseClicked(mouseX, mouseY, button);
            binding = !binding;
        }
    }

    @Override
    public void keyTyped(final int key) {
        if (this.binding) {
            if (key == GLFW.GLFW_KEY_BACKSPACE || key == GLFW.GLFW_KEY_DELETE || key == GLFW.GLFW_KEY_ESCAPE) button.module.setKey(-1);
            else button.module.setKey(key);
            this.binding = false;
        }
    }

    private Text getKeyString() {
        String[] key = String.valueOf(InputUtil.fromKeyCode(button.module.getKey(), -1)).split("\\.");
        return Text.literal(key[key.length - 1].toUpperCase(Locale.ROOT));
    }

    @Override
    public void render(MatrixStack matrix) {
        super.render(matrix);
        FontManagerr.INSTANCE.drawString(matrix, "Key", button.frame.getX() + offsetNested, button.frame.getY() + offsetY - (FontManagerr.INSTANCE.height("Key") - 7) / 2f, colors.getText().getColor(), null);
        if (binding)
            FontManagerr.INSTANCE.drawString(matrix, "...", button.frame.getX() + button.frame.getWidth() - 2 - FontManagerr.INSTANCE.width("..."), button.frame.getY() + offsetY - (FontManagerr.INSTANCE.height("...") - 7) / 2f, colors.getText().getColor(), null);
        else {
            Text keyString = switch (button.module.getKey()) {
                case 341 -> Text.literal("CTRL");
                case 344 -> Text.literal("RSHIFT");
                case 345 -> Text.literal("RCTRL");
                case 346 -> Text.literal("RALT");
                case 256, 0, -1 -> Text.literal("NONE");
                default -> getKeyString();
            };
            FontManagerr.INSTANCE.drawString(matrix, keyString.getString(), button.frame.getX() + button.frame.getWidth() - 2 - FontManagerr.INSTANCE.width(keyString.getString()), button.frame.getY() + offsetY - (FontManagerr.INSTANCE.height(keyString.getString()) - 7) / 2f, ColorMutable.Companion.getWHITE(), null);
        }
    }
}
