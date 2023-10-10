package today.flux.gui.hud.window;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import today.flux.gui.hud.window.impl.WindowPlayerInventory;
import today.flux.gui.hud.window.impl.WindowRadar;
import today.flux.gui.hud.window.impl.WindowScoreBoard;
import today.flux.gui.hud.window.impl.WindowSessInfo;

import java.util.ArrayList;
import java.util.Comparator;

public class HudWindowManager {
    public static final ArrayList<HudWindow> windows = new ArrayList<>();

    public Minecraft mc = Minecraft.getMinecraft();

    public HudWindow hoveredWindow;
    public HudWindow focusedWindow;

    public HudWindowManager() {
        register(new WindowSessInfo());
        register(new WindowPlayerInventory());
        register(new WindowScoreBoard());
        register(new WindowRadar());
    }

    public void register(HudWindow window) {
        windows.add(window);
    }

    public void draw() {
        for(HudWindow window : windows) {
            if(window.hide) continue;
            window.draw();
            window.postDraw();
        }
    }

    public void updateScreen() {
        for(HudWindow window : windows) {
            if(window.hide) continue;
            window.updateScreen();
        }
    }

    public void handleMouseInput(int width, int height) {
        int xx = Mouse.getEventX() * width / this.mc.displayWidth;
        int yy = height - Mouse.getEventY() * height / this.mc.displayHeight - 1;
        this.mouseMove(xx, yy);
    }

    public void mouseMove(int mouseX, int mouseY) {
        hoveredWindow = null;
        for (int i = windows.size() - 1; i >= 0; i--) {
            HudWindow window = windows.get(i);
            if (window.isOnFrame(mouseX, mouseY) && !window.hide) {
                hoveredWindow = window;
                break;
            }
        }
    }

    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        updateFocusedWindow();
        if (hoveredWindow != null)
            hoveredWindow.mouseClick(mouseX, mouseY, mouseButton);
        updateWindowOrder();
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (hoveredWindow != null)
            hoveredWindow.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    public void mouseCoordinateUpdate(int mouseX, int mouseY) {
        for(HudWindow window : windows) {
            if(window.hide) continue;
            window.mouseCoordinateUpdate(mouseX, mouseY);
        }
    }

    public void mouseRelease(int mouseX, int mouseY, int state) {
        if (hoveredWindow != null)
            hoveredWindow.mouseReleased(mouseX, mouseY, state);
    }

    public void updateFocusedWindow() {
        if (hoveredWindow == null && focusedWindow != null) {
            focusedWindow.setFocused(false);
            focusedWindow = null;
            return;
        }

        if (focusedWindow != hoveredWindow) {
            if (focusedWindow != null)
                focusedWindow.setFocused(false);
            focusedWindow = hoveredWindow;
            focusedWindow.setFocused(true);
        }
    }

    public static HudWindow getWindowByID(String windowID) {
        for(HudWindow window : windows) {
            if(window.windowID.equals(windowID)) {
                return window;
            }
        }

        return null;
    }

    public void updateWindowOrder() {
        windows.sort(Comparator.comparingLong(window -> window.lastClickTime));
    }

}
