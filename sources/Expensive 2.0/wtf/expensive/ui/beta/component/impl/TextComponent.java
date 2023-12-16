package wtf.expensive.ui.beta.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.SharedConstants;
import org.lwjgl.glfw.GLFW;
import wtf.expensive.modules.settings.imp.TextSetting;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.math.KeyMappings;
import wtf.expensive.util.render.RenderUtil;

import java.awt.*;

public class TextComponent extends Component {

    public TextSetting setting;
    public boolean isTyping;

    public TextComponent(TextSetting setting) {
        this.setting = setting;
        this.s = setting;
    }

    @Override
    public void drawComponent(MatrixStack matrixStack, int mouseX, int mouseY) {
        height -= 3;
        y += 1;
        String text = setting.get();

        float width = Fonts.gilroy[14].getWidth(text) + 4;
        RenderUtil.Render2D.drawRoundedCorner(x + 5, y + 2, width, 10, 2, isTyping ? new Color(17, 18, 21).brighter().brighter().getRGB() : new Color(17, 18, 21).brighter().getRGB());
        Fonts.gilroy[14].drawCenteredString(matrixStack, text, x + 5 + (width / 2), y + 6, -1);
        Fonts.gilroy[14].drawString(matrixStack, setting.getName(), x + 5 + width + 3, y + 6, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX,mouseY)) {
            isTyping = !isTyping;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (!setting.text.isEmpty())
                setting.text = setting.text.substring(0, setting.text.length() - 1);
        } else if (keyCode == GLFW.GLFW_KEY_ENTER) {
            isTyping = false;
        }
        if (isTyping) {
            if (Screen.isCopy(keyCode)) {
                Minecraft.getInstance().keyboardListener.setClipboardString(setting.text);
            } else if (Screen.isPaste(keyCode)) {
                setting.text += Minecraft.getInstance().keyboardListener.getClipboardString();
            }
        }

    }

    @Override
    public void charTyped(char codePoint, int modifiers) {
        if (isTyping) {
            if (SharedConstants.isAllowedCharacter(codePoint))
                setting.text += Character.toString(codePoint);
        }
    }
}
