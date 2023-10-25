package tech.drainwalk.gui.menu.window.windows.menuwindow.components.panel;

import net.minecraft.util.ResourceLocation;
import tech.drainwalk.DrainWalk;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.gui.menu.MenuMain;
import tech.drainwalk.gui.menu.hovered.Hovered;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.gui.menu.window.windows.menuwindow.MenuWindow;
import tech.drainwalk.gui.menu.window.windows.menuwindow.components.Component;
import tech.drainwalk.utility.render.RenderUtility;

public class PanelComponent extends Component {

    public PanelComponent(float x, float y, float width, float height, MenuWindow parent) {
        super(x, y, width, height, parent);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {



        RenderUtility.drawRoundedRect(x, y, width, height, 11, 200, 54, ClientColor.panel, ClientColor.panelMain);
        if (DrainWalk.getInstance().isRoflMode()) {
            RenderUtility.drawRoundedTexture(new ResourceLocation("drainwalk/images/deus_mode.png"), x, y, width, height, 11, 0.1f);
        }

        FontManager.MEDIUM_20.drawString(DrainWalk.CLIENT_NAME.toLowerCase(), x + 55 / 2f, y + 15, ClientColor.textMain);
        RenderUtility.drawRect(x + 102.5f, y, 1, height, ClientColor.panelLines);
        RenderUtility.drawRect(x + 102.5f, y + 28f, width - 103f, 1, ClientColor.panelLines);

        
        FontManager.ICONS_21.drawString("f", x + 431, y + 12, ClientColor.textMain);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (Hovered.isHovered(mouseX, mouseY, x + 431, y + 9, 10, 10) && mouseButton == 0) {
            MenuMain.aboutWindow.setWindowActive(!MenuMain.getWindowList().get(1).isWindowActive());
            if (MenuMain.aboutWindow.isWindowActive()) {
                MenuMain.aboutWindow.setWindowX(x + width + 10);
                MenuMain.aboutWindow.setWindowY(y);
                MenuMain.aboutWindow.getAnimation().setPrevValue(0);
                MenuMain.aboutWindow.getAnimation().setValue(0);
            }
        }
    }

    @Override
    public void updateScreen(int mouseX, int mouseY) {
        if (Hovered.isHovered(mouseX, mouseY, x + 431, y + 9, 10, 10)) {
            canDragging = false;
        } else {
            canDragging = true;
        }
    }
}
