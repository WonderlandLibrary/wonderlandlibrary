package best.azura.client.impl.ui.gui.impl.buttons;

import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.ui.gui.impl.buttons.ButtonImpl;
import best.azura.client.util.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class TextButton extends ButtonImpl {

    public String fontText;

    boolean hideText;

    public TextButton(String text, int x, int y, int width, int height, boolean hideText) {
        super(text, x, y, width, height);
        this.fontText = "";
        this.hideText = hideText;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        this.hovered = RenderUtil.INSTANCE.isHoveredScaled(x, y, width, height, mouseX, mouseY, 1.0);
        Color color = hovered ? hoverColor : normalColor;
        color = selected ? electedColor : color;


        StringBuilder stringBuilder = new StringBuilder();
        if (!fontText.isEmpty() && hideText) {
            for (int i = 0; i < fontText.length(); i++) {
                stringBuilder.append("*");
            }
        }

        RenderUtil.INSTANCE.drawRoundedRect(x, y, width, height, 10, RenderUtil.INSTANCE.modifiedAlpha(color, (int) (color.getAlpha() * animation)));
	    Fonts.INSTANCE.arial15bold.drawString(fontText.isEmpty() && !selected ? text : hideText ? stringBuilder.toString() : fontText, x + 5, y + height / 2.0 - Fonts.INSTANCE.arial15bold.FONT_HEIGHT / 2.0, new Color(255, 255, 255, (int) (255 * animation)).getRGB());
        if(selected) {
            float speed = (1.5f * 1000);
            //float function = ((System.nanoTime()/1000000.0f) % speed) / speed;
            float function = ((System.currentTimeMillis() % speed) / speed);
            function *= 2;
            function -= 1;
            function = (float) (-1 * Math.sqrt(function * function) + 1);
            function = Math.max(0, Math.min(1, function));
            RenderUtil.INSTANCE.drawRect(x+5+Fonts.INSTANCE.arial15bold.getStringWidth(hideText ? stringBuilder.toString() : fontText), y+height/2.0-10,x+5+Fonts.INSTANCE.arial15bold.getStringWidth(hideText ? stringBuilder.toString() : fontText)+2, y+height/2.0+10, new Color(255, 255, 255, (int) (255*function)));
        }
    }

    @Override
    public void keyTyped(char typed, int keyCode) {
        if(selected) {
            if (keyCode == Keyboard.KEY_V && GuiScreen.isCtrlKeyDown())
                fontText = GuiScreen.getClipboardString();
            if(keyCode == Keyboard.KEY_BACK && !fontText.equals("")) {
                fontText = fontText.substring(0, fontText.length()-1);
            } else if(keyCode != Keyboard.KEY_BACK && ChatAllowedCharacters.isAllowedCharacter(typed)) fontText += typed;
            if (keyCode == Keyboard.KEY_RETURN) selected = false;
        }
    }

    @Override
    public void mouseClicked() {
        this.selected = this.hovered;
    }
}
