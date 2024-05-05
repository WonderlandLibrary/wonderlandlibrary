package tech.drainwalk.gui.menu;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import tech.drainwalk.animation.Animation;
import tech.drainwalk.animation.AnimationUtility;
import tech.drainwalk.animation.EasingList;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.client.theme.Theme;
import tech.drainwalk.client.theme.ThemeSetting;
import tech.drainwalk.events.EventRender2D;
import tech.drainwalk.events.UpdateEvent;
import tech.drainwalk.gui.menu.window.Window;
import tech.drainwalk.gui.menu.window.windows.aboutwindow.AboutWindow;
import tech.drainwalk.gui.menu.window.windows.colorpickerwindow.ColorPickerWindow;
import tech.drainwalk.gui.menu.window.windows.menuwindow.MenuWindow;
import tech.drainwalk.gui.menu.window.windows.settingwindow.SettingWindow;
import tech.drainwalk.utility.color.ColorUtility;
import tech.drainwalk.utility.math.MathUtility;
import tech.drainwalk.utility.render.GLUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuMain extends GuiScreen {
    @Getter
    private static final List<Window> windowList = new ArrayList<>();
    public static ThemeSetting<Theme> selectedTheme = new ThemeSetting<>("Themes", Theme.BLACK);
    private final Animation toUnregister = new Animation();
    public static MenuWindow menuWindow;
    public static AboutWindow aboutWindow;
    public static SettingWindow settingWindow;
    public static ColorPickerWindow colorPickerWindow;

    static {
        windowList.add(menuWindow = new MenuWindow());
        windowList.add(aboutWindow = new AboutWindow());
        windowList.add(settingWindow = new SettingWindow());
        windowList.add(colorPickerWindow = new ColorPickerWindow());
    }

    @Override
    public void initGui() {
        EventManager.unregister(this);
        for (Window window : windowList) {
            window.getAnimation().setValue(0);
            window.getAnimation().setPrevValue(0);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        toUnregister.animate(0, 1, 0.15f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);
        ClientColor.panel = ColorUtility.interpolateColor(ClientColor.prevPanel, selectedTheme.getValue().getPanel(), ClientColor.animation.getAnimationValue());
        ClientColor.panelMain = ColorUtility.interpolateColor(ClientColor.prevPanelMain, selectedTheme.getValue().getPanelMain(), ClientColor.animation.getAnimationValue());
        ClientColor.panelLines = ColorUtility.interpolateColor(ClientColor.prevPanelLines, selectedTheme.getValue().getPanelLines(), ClientColor.animation.getAnimationValue());

        ClientColor.object = ColorUtility.interpolateColor(ClientColor.prevObject, selectedTheme.getValue().getObject(), ClientColor.animation.getAnimationValue());

        ClientColor.textMain = ColorUtility.interpolateColor(ClientColor.prevTextMain, selectedTheme.getValue().getTextMain(), ClientColor.animation.getAnimationValue());
        ClientColor.textStay = ColorUtility.interpolateColor(ClientColor.prevTextStay, selectedTheme.getValue().getTextStay(), ClientColor.animation.getAnimationValue());
        ClientColor.main = ColorUtility.interpolateColor(ClientColor.prevMain, selectedTheme.getValue().getMain(), ClientColor.animation.getAnimationValue());
        ClientColor.mainStay = ColorUtility.interpolateColor(ClientColor.prevMainStay, selectedTheme.getValue().getMainStay(), ClientColor.animation.getAnimationValue());
        ClientColor.category = ColorUtility.interpolateColor(ClientColor.prevCategory, selectedTheme.getValue().getCategory(), ClientColor.animation.getAnimationValue());
        ClientColor.checkBoxStayBG = ColorUtility.interpolateColor(ClientColor.prevCheckBoxStayBG, selectedTheme.getValue().getCheckBoxStayBG(), ClientColor.animation.getAnimationValue());
        ClientColor.checkBoxStay = ColorUtility.interpolateColor(ClientColor.prevCheckBoxStay, selectedTheme.getValue().getCheckBoxStay(), ClientColor.animation.getAnimationValue());
        int mX = (int) (Mouse.getX() / 2f);
        int mY = (int) ((mc.displayHeight - Mouse.getY()) / 2f);
        GLUtility.INSTANCE.rescale(2);
        for (Window window : windowList) {

            GlStateManager.pushMatrix();
            if (window.isActiveDragging()) {
                window.setWindowX((int) (mX - window.getActiveX()));
                window.setWindowY((int) (mY - window.getActiveY()));
            }
            window.setWindowX((int) MathUtility.clamp(window.getWindowX(), 0, (Minecraft.getMinecraft().displayWidth / 2f) - window.getWindowWidth()));
            window.setWindowY((int) MathUtility.clamp(window.getWindowY(), 0, (Minecraft.getMinecraft().displayHeight / 2f) - window.getWindowHeight()));
            window.getAnimation().animate(0, 1, 0.15f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);
            AnimationUtility.scaleAnimation(
                    window.getWindowX(),
                    window.getWindowY(),
                    window.getWindowWidth(),
                    window.getWindowHeight(),
                    window.getAnimation().getAnimationValue()
            );

            window.renderWindow(mX, mY, mc.getTimer().renderPartialTicks);
            GlStateManager.popMatrix();

        }
        GLUtility.INSTANCE.rescaleMC();
    }

    @Override
    public void updateScreen() {
        toUnregister.update(true);
        settingWindow.setWindowActive(MenuMain.menuWindow.getSelectedModule() != null);
        for (Window window : windowList) {
            window.getAnimation().update(window.isWindowActive());
            if (window.isWindowActive()) {
                int mX = (int) (Mouse.getX() / 2f);
                int mY = (int) ((mc.displayHeight - Mouse.getY()) / 2f);
                window.updateScreen(mX, mY);
            }
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        EventManager.register(this);
        for (Window window : windowList) {
            window.setActiveDragging(false);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int mX = (int) (Mouse.getX() / 2f);
        int mY = (int) ((mc.displayHeight - Mouse.getY()) / 2f);
        if (!settingWindow.isInWindowBound() && !colorPickerWindow.isInWindowBound() && menuWindow.isInWindowBound()) {
            MenuMain.menuWindow.setSelectedModule(null);
        }
        if(!colorPickerWindow.isInWindowBound() && menuWindow.isInWindowBound()) {
            colorPickerWindow.setWindowActive(false);
        }

        for (Window window : windowList) {
            if (window.isWindowActive()) {
                if (!settingWindow.isInWindowBound() && !colorPickerWindow.isInWindowBound()) {
                    if (window.isCanDragging() && window.isWindowDraggable()) {
                        if (window.isInWindowBound() && mouseButton == 0) {
                            window.setActiveDragging(true);
                            window.setActiveX(mX - window.getWindowX());
                            window.setActiveY(mY - window.getWindowY());
                            break;
                        }
                    }
                }
                window.mouseClicked(mX, mY, mouseButton);
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        int mX = (int) (Mouse.getX() / 2f);
        int mY = (int) ((mc.displayHeight - Mouse.getY()) / 2f);
        for (Window window : windowList) {
            if (window.getAnimation().getAnimationValue() == 1) {
                window.setActiveDragging(false);
                window.mouseReleased(mX, mY, state);
            }
        }
    }

    @EventTarget
    public void updateAnimation(UpdateEvent eventUpdate) {
        toUnregister.update(false);
        GLUtility.INSTANCE.rescale(2);
        for (Window window : windowList) {
            window.getAnimation().update(false);

        }
        GLUtility.INSTANCE.rescaleMC();
    }

    @EventTarget
    public void closeAnimation(EventRender2D eventRender2D) {
        toUnregister.animate(0, 1, 0.15f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);
        GLUtility.INSTANCE.rescale(2);
        for (Window window : windowList) {
            GlStateManager.pushMatrix();
            window.getAnimation().animate(0, 1, 0.15f, EasingList.BACK_OUT, mc.getTimer().renderPartialTicks);
            AnimationUtility.scaleAnimation(window.getWindowX(), window.getWindowY(), window.getWindowWidth(), window.getWindowHeight(), window.getAnimation().getAnimationValue());
            window.renderWindow(0, 0, 0);

            GlStateManager.popMatrix();

        }
        GLUtility.INSTANCE.rescaleMC();
        if (toUnregister.getAnimationValue() < 0.05f) {
            EventManager.unregister(this);
        }
    }
}
