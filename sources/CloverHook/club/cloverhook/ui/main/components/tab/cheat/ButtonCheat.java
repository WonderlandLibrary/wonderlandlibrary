package club.cloverhook.ui.main.components.tab.cheat;

import org.lwjgl.input.Keyboard;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.ui.main.Interface;
import club.cloverhook.ui.main.components.base.BaseButton;
import club.cloverhook.ui.main.tab.cheat.TabDefaultCheat;
import club.cloverhook.utils.ColorCreator;
import club.cloverhook.utils.Draw;
import club.cloverhook.utils.Stopwatch;
import club.cloverhook.utils.font.Fonts;

/**
 * @author antja03
 */
public class ButtonCheat extends BaseButton {

    private TabDefaultCheat parentTab;
    private ContainerCheats parentContainer;

    private Cheat cheat;
    private Stopwatch stopwatch = new Stopwatch();
    private int opacityOffset = 0;
    private boolean listeningForKey = false;

    public ButtonCheat(Interface theInterface, TabDefaultCheat parentTab, ContainerCheats parentContainer, Cheat cheat, double x, double y, double width, double height, Action action) {
        super(theInterface, x, y, width, height, action);
        this.parentTab = parentTab;
        this.parentContainer = parentContainer;
        this.cheat = cheat;
    }

    @Override
    public void drawComponent(double x, double y) {
        this.positionX = x - theInterface.getPositionX();
        this.positionY = y - theInterface.getPositionY();

        if (stopwatch.hasPassed(50)) {
            if (isMouseOver()) {
                if (opacityOffset < 80) {
                    opacityOffset += 16;
                }
            } else {
                if (opacityOffset > 0) {
                    opacityOffset -= 16;
                }
            }
            stopwatch.reset();
        }

        int rgb = parentTab.getSelectedCheat() == cheat ? 18 : 24;

        Draw.drawRectangle(x, y, x + maxWidth, y + maxHeight, theInterface.getColor(rgb, rgb, rgb, 255));
        Fonts.f16.drawString(cheat.getId(), x + 5, y + 7, theInterface.getColor(220, 220, 220));

        String extraInfo = "";

        if (cheat.getPropertyRegistry().isEmpty())
            extraInfo = "No options";
        else
            extraInfo = cheat.getPropertyRegistry().size() + (cheat.getPropertyRegistry().size() > 1 ? " options" : " option");


        Fonts.f12.drawString(extraInfo, x + 5, y + 17, theInterface.getColor(100, 100, 100));

        //State selector
        {
            Draw.drawRectangle(x + maxWidth - 25, y + 4, x + maxWidth - 5, y + 12, theInterface.getColor(40, 40, 40));
            //Draw.drawRectangle(x + maxWidth - 15.25, y + 4, x + maxWidth - 14.75, y + 12, theInterface.getColor(0, 0, 0, 50));
            if (cheat.getState())
                Draw.drawRectangle(x + maxWidth - 14, y + 5, x + maxWidth - 6, y + 11, theInterface.getColor(80, 150, 80));
            else
                Draw.drawRectangle(x + maxWidth - 24, y + 5, x + maxWidth - 16, y + 11, theInterface.getColor(200, 80, 80));
        }

        //Bind
        {
            String bindText = cheat.getBind() != 0 ? Keyboard.getKeyName(cheat.getBind()) : "  ";
            if (listeningForKey) {
                bindText = "...";
            }
            Fonts.f14.drawString("[" + bindText + "]", x + maxWidth - 5 - Fonts.f14.getStringWidth("[" + bindText + "]"), y + 16.5, theInterface.getColor(80, 80, 80));
        }
    }

    @Override
    public boolean mouseButtonClicked(int button) {
        String bindText = cheat.getBind() != 0 ? Keyboard.getKeyName(cheat.getBind()) : "  ";
        if (listeningForKey) {
            bindText = "...";
        }
        if (theInterface.isMouseInBounds(
                theInterface.getPositionX() + positionX + maxWidth - 25,
                theInterface.getPositionX() + positionX + maxWidth - 5,
                theInterface.getPositionY() + positionY + 4,
                theInterface.getPositionY() + positionY + 12)) {
            cheat.setState(!cheat.getState(), true);
            return true;
        } else if (theInterface.isMouseInBounds(
                theInterface.getPositionX() + positionX + maxWidth - 7 - Fonts.f14.getStringWidth("[" + bindText + "]"),
                theInterface.getPositionX() + positionX + maxWidth - 3,
                theInterface.getPositionY() + positionY + 14.5,
                theInterface.getPositionY() + positionY + 24)) {
            listeningForKey = true;
            return true;
        } else if (theInterface.isMouseInBounds(
                theInterface.getPositionX() + positionX,
                theInterface.getPositionX() + positionX + maxWidth,
                theInterface.getPositionY() + positionY,
                theInterface.getPositionY() + positionY + maxHeight)) {
            if (!cheat.getPropertyRegistry().isEmpty()) {
                parentTab.setSelectedCheat(cheat);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        if (listeningForKey) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                cheat.setBind(0);
                listeningForKey = false;
            } else {
                cheat.setBind(keyCode);
                listeningForKey = false;
            }
            return true;
        }
        return false;
    }
}
