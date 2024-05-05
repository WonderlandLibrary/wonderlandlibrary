package rip.helium.ui.main.components.base;

import org.lwjgl.input.Keyboard;
import rip.helium.ui.main.Interface;
import rip.helium.ui.main.components.Component;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.font.FontRenderer;

/**
 * @author antja03
 */
public abstract class BaseTextEntry extends Component {

    protected String content = "";
    protected boolean focused = false;
    protected FontRenderer fontRenderer;
    private final Stopwatch backStopwatch = new Stopwatch();
    private final Stopwatch tickStopwatch = new Stopwatch();
    private int ticksBackDown = 0;

    public BaseTextEntry(Interface theInterface, FontRenderer fontRenderer, double x, double y, double width, double height) {
        super(theInterface, x, y, width, height);
        this.fontRenderer = fontRenderer;
    }

    public abstract void drawComponent(double x, double y);

    public boolean mouseButtonClicked(int button) {
        if (theInterface.isMouseInBounds(
                theInterface.getPositionX() + positionX,
                theInterface.getPositionX() + positionX + maxWidth,
                theInterface.getPositionY() + positionY,
                theInterface.getPositionY() + positionY + maxHeight)) {
            focused = true;
            return true;
        }
        focused = false;
        return false;
    }

    public boolean keyTyped(char typedChar, int keyCode) {
        if (focused) {
            String specialChars = "/*!@#$%^&*()\";:'{}_[]|\\?/<>,.";
            if (keyCode == Keyboard.KEY_BACK) {
                if (content.length() > 1) {
                    content = content.substring(0, content.length() - 1);
                } else if (content.length() == 1) {
                    content = "";
                }
            } else if (keyCode == Keyboard.KEY_RETURN) {
                execute();
            } else if (Character.isLetterOrDigit(typedChar) || Character.isSpaceChar(typedChar) || specialChars.contains(Character.toString(typedChar))) {
                if (fontRenderer.getStringWidth(content) < maxWidth - 1) {
                    content += Character.toString(typedChar);
                }
            }
            return true;
        }
        return false;
    }

    public void onGuiClose() {
        focused = false;
    }

    protected void backspace() {
        if (tickStopwatch.hasPassed(50)) {
            if (Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
                ticksBackDown++;
            } else {
                ticksBackDown = 0;
            }
            tickStopwatch.reset();
        }

        float delay = 300 - (ticksBackDown > 10 ? (ticksBackDown * 8) : 0);
        if (delay < 50) {
            delay = 50;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_BACK) && backStopwatch.hasPassed(delay) && ticksBackDown > 10) {
            if (content.length() > 1) {
                content = content.substring(0, content.length() - 1);
            } else if (content.length() == 1) {
                content = "";
            }
            backStopwatch.reset();
        }
    }

    public abstract void execute();

    public String getContent() {
        return content;
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

}
