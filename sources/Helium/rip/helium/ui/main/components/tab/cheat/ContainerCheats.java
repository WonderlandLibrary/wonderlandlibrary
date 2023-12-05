package rip.helium.ui.main.components.tab.cheat;

import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.ui.main.Interface;
import rip.helium.ui.main.components.Component;
import rip.helium.ui.main.components.base.BaseContainer;
import rip.helium.ui.main.tab.cheat.TabDefaultCheat;
import rip.helium.utils.Draw;
import rip.helium.utils.font.Fonts;

import java.awt.*;

/**
 * @author antja03
 */
public class ContainerCheats extends BaseContainer {

    public CheatCategory cheatCategory;
    private final TabDefaultCheat parentTab;
    private int scrollIndex;

    public ContainerCheats(Interface theInterface, TabDefaultCheat parentTab, CheatCategory cheatCategory, double x, double y, double width, double height) {
        super(theInterface, x, y, width, height);
        this.parentTab = parentTab;
        this.cheatCategory = cheatCategory;
        this.scrollIndex = 0;
        double modY = y;
        for (Cheat cheat : Helium.instance.cheatManager.getCheatRegistry().values()) {
            if (parentTab.getSelectedCheat() == null) {
                parentTab.setSelectedCheat(cheat);
            }
            if (cheat.getCategory().equals(cheatCategory)) {
                components.add(new ButtonCheat(theInterface, parentTab, this, cheat, x, modY, 148, 25, null));
                modY += 20;
            }
        }
    }

    public void drawComponent(double x, double y) {

        // Draw.drawRectangle(x + maxWidth - 1.5, y, x + maxWidth, theInterface.getPositionY() + maxHeight, theInterface.getColor(111, 110, 112, 255));
        if (components.size() > 8) {
            double barHeight = maxHeight;
            double div = barHeight / components.size();
            if (components.size() > 8) {
                barHeight -= (components.size() - 8) * div;
            }
            double barPosition = div * scrollIndex;

            //Draw.drawRectangle(x + maxWidth - 1.5, theInterface.getPositionY() + barPosition + 1, x + maxWidth, theInterface.getPositionY() + barPosition + barHeight - 1, theInterface.getColor(110, 110, 110, 255));
        }
        String cat = cheatCategory.name()
                .replaceAll("MOVEMENT", "Movement")
                .replaceAll("VISUAL", "Render")
                .replaceAll("COMBAT", "Combat")
                .replaceAll("PLAYER", "Player")
                .replaceAll("MISC", "Misc");
        Draw.drawRectangle(theInterface.getPositionX() + 25, theInterface.getPositionY() - 1, theInterface.getPositionX() + 173, theInterface.getPositionY() + 251, new Color(32, 31, 32).getRGB());
        Draw.drawCircle(Fonts.verdanaGui.getStringWidth(cat) + 50 + theInterface.getPositionX(), theInterface.getPositionY() + 26, 13, Interface.categoryColor(cheatCategory).getRGB());
        Fonts.verdanaGui.drawStringWithShadow(cat, theInterface.getPositionX() + 35, theInterface.getPositionY() + 20, new Color(255, 255, 255).getRGB());
        if (Helium.instance.cheatManager.searchRegistry(cheatCategory).size() < 10) {
            Fonts.verdanaGui.drawString("" + Helium.instance.cheatManager.searchRegistry(cheatCategory).size(), Fonts.verdanaGui.getStringWidth(cat) + 50 + theInterface.getPositionX() - Fonts.verdanaGui.getStringWidth(Helium.instance.cheatManager.searchRegistry(cheatCategory).size() + "") + 4, theInterface.getPositionY() + 20, new Color(255, 255, 255).getRGB());
        } else {
            Fonts.verdanaGui.drawString("" + Helium.instance.cheatManager.searchRegistry(cheatCategory).size(), Fonts.verdanaGui.getStringWidth(cat) + 50 + theInterface.getPositionX() - Fonts.verdanaGui.getStringWidth(Helium.instance.cheatManager.searchRegistry(cheatCategory).size() + "") + 9, theInterface.getPositionY() + 20, new Color(255, 255, 255).getRGB());
        }
        int index = 0;
        for (Component component : this.components) {
            if (components.indexOf(component) >= scrollIndex && components.indexOf(component) < scrollIndex + 8) {
                component.drawComponent(theInterface.getPositionX() + component.positionX, 50 + theInterface.getPositionY() + (25 * index));
                index += 1;
            }
        }
    }

    public boolean mouseButtonClicked(int button) {
        for (Component component : this.components) {
            if (components.indexOf(component) >= scrollIndex && components.indexOf(component) < scrollIndex + 8) {
                if (component.mouseButtonClicked(button)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void mouseReleased() {
        for (Component component : this.components) {
            if (components.indexOf(component) >= scrollIndex && components.indexOf(component) < scrollIndex + 8) {
                component.mouseReleased();
            }
        }
    }

    public void mouseScrolled(final int scrollDirection) {
        if (theInterface.getCurrentFrameMouseX() < theInterface.getPositionX() + positionX || theInterface.getCurrentFrameMouseX() > theInterface.getPositionX() + maxWidth)
            return;

        if (scrollDirection == 1) {
            if (scrollIndex < components.size() - 8) {
                scrollIndex += 1;
            }
        } else {
            if (scrollIndex > 0) {
                scrollIndex -= 1;
            }
        }

        for (Component component : this.components) {
            if (components.indexOf(component) >= scrollIndex && components.indexOf(component) < scrollIndex + 8) {
                component.mouseScrolled(scrollDirection);
            }
        }
    }

    public boolean keyTyped(char typedChar, int keyCode) {
        for (Component component : this.components) {
            if (components.indexOf(component) >= scrollIndex && components.indexOf(component) < scrollIndex + 8) {
                if (component.keyTyped(typedChar, keyCode)) {
                    return true;
                }
            }
        }
        return false;
    }
}
