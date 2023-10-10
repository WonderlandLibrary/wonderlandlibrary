package today.flux.gui.hud.window;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.opengl.Display;
import today.flux.gui.clickgui.classic.BlurBuffer;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.module.implement.Render.HudWindowMod;
import today.flux.utility.ColorUtils;
import today.flux.utility.PlayerUtils;
import today.flux.utility.SmoothAnimationTimer;
import today.flux.utility.TimeHelper;

import java.awt.*;

public class HudWindow {
    public float x;
    public float y;

    public Minecraft mc = Minecraft.getMinecraft();

    // Dragging
    public float x2;
    public float y2;
    public boolean drag = false;
    public float draggableHeight;

    public String windowID;
    public float width;
    public float height;
    public String title;

    public boolean resizeable;
    public float minWidth;
    public float minHeight;
    public boolean resizing = false;

    //Icon and offset shit
    public String icon;
    public float iconOffX;
    public float iconOffY;

    public boolean hide = false;
    public long lastClickTime = 0;
    public boolean focused = false;

    public static TimeHelper motionHelper = new TimeHelper();
    private static final SmoothAnimationTimer animationTimer = new SmoothAnimationTimer(50, 0.085f);

    public static Color titleBGColor;
    public static Color frameBGColor;

    static {
        animationTimer.setValue(20);
        updateColor();
    }

    public static void updateColor() {
        titleBGColor = new Color(0, 0, 0, (int) (animationTimer.getValue()));
        frameBGColor = new Color(20, 20, 20, (int) (animationTimer.getValue()));
    }

    public HudWindow(String windowID, float x, float y, float width, float height, String title) {
        this(windowID, x, y, width, height, title, "", 15, 0, 0, false, 0, 0);
    }

    public HudWindow(String windowID, float x, float y, float width, float height, String title, String icon) {
        this(windowID, x, y, width, height, title, icon, 15, 0, 0, false, 0, 0);
    }

    public HudWindow(String windowID, float x, float y, float width, float height, String title, boolean resizeable, float minWidth, float minHeight) {
        this(windowID, x, y, width, height, title, "", 15, 0, 0, resizeable, minWidth, minHeight);
    }

    public HudWindow(String windowID, float x, float y, float width, float height, String title, String icon, float draggableHeight, float iconOffX, float iconOffY) {
        this(windowID, x, y, width, height, title, icon, draggableHeight, iconOffX, iconOffY, false, 0, 0);
    }

    public HudWindow(String windowID, float x, float y, float width, float height, String title, String icon, float draggableHeight, float iconOffX, float iconOffY, boolean resizeable, float minWidth, float minHeight) {
        this.windowID = windowID;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
        this.icon = icon;
        this.draggableHeight = draggableHeight;
        this.iconOffX = iconOffX;
        this.iconOffY = iconOffY;
        this.resizeable = resizeable;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public void draw() {
        if (PlayerUtils.MovementInput()) {
            animationTimer.target = HudWindowMod.movingAlpha.getValue().intValue();
            motionHelper.reset();
        }

        if (motionHelper.isDelayComplete(200)) {
            animationTimer.target = HudWindowMod.staticAlpha.getValue().intValue();
        }

        animationTimer.update(true);

        //Window Frame
        updateColor();

        if (BlurBuffer.blurEnabled())
            BlurBuffer.blurArea(x, y + (HudWindowMod.title.getValue() ? 0 : draggableHeight), width, height + (HudWindowMod.title.getValue() ? draggableHeight : 0), true);

        if(HudWindowMod.title.getValue())
        GuiRenderUtils.drawRect(this.x, this.y, width, draggableHeight, titleBGColor);

        if(HudWindowMod.border.getValue())
        GuiRenderUtils.drawBorderedRect(this.x, this.y + draggableHeight, width, height,0.5f, frameBGColor, HudWindowMod.borderColours.getColor());
        else {
            GuiRenderUtils.drawRect(this.x, this.y + draggableHeight, width, height, frameBGColor);
        }

        boolean displayIcon = !icon.isEmpty();
        if (displayIcon) {
            FontManager.icon20.drawString(this.icon, this.x + iconOffX, this.y + iconOffY, ColorUtils.WHITE.c);
        }

        if(HudWindowMod.title.getValue())
            FontManager.sans13_2.drawString(this.title, this.x + 3 + (displayIcon ? 8 : 0), this.y + (draggableHeight / 2f) - (FontManager.sans13_2.getFontHeight() / 2f), ColorUtils.WHITE.c);

        if(mc.currentScreen instanceof GuiChat) {
            GuiRenderUtils.drawRect(this.x, this.y, width, draggableHeight, 0xff9B59B6);
            FontManager.icon20.drawString("P", this.x + iconOffX, this.y + iconOffY, ColorUtils.WHITE.c);
            FontManager.sans13_2.drawString("Move", x + 12, this.y + (draggableHeight / 2f) - (FontManager.sans13_2.getFontHeight() / 2f), ColorUtils.WHITE.c);
        }
    }

    public void postDraw() {
        if(resizeable && mc.currentScreen instanceof GuiChat) {
            FontManager.icon14.drawString("l", this.x + width - 8, this.y + draggableHeight + height - 8, ColorUtils.WHITE.c);

            if(resizing) {
                String gay = (int) this.width + ", " + (int) this.height;
                RenderUtil.drawRect(this.x + this.width + 2, this.y + this.draggableHeight + this.height - 12, this.x + this.width + 2 + FontManager.sans16.getStringWidth(gay) + 4, this.y + this.draggableHeight + this.height, 0x88000000);
                FontManager.sans16.drawString(gay, this.x + this.width + 4, this.y + this.draggableHeight + this.height - 12, ColorUtils.WHITE.c);
            }
        }
    }

    public void updateScreen() {

    }

    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isHovering(mouseX, mouseY, x, y, x + width, y + draggableHeight)) {
            this.drag = true;
            lastClickTime = System.currentTimeMillis();
            this.x2 = mouseX - this.x;
            this.y2 = mouseY - this.y;
        }

        if(RenderUtil.isHovering(mouseX, mouseY, x + width - 8, y + draggableHeight + height - 8, x + width, y + draggableHeight + height) && this.resizeable) {
            this.resizing = true;
            lastClickTime = System.currentTimeMillis();
            this.x2 = mouseX;
            this.y2 = mouseY;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.drag) {
            this.drag = false;
        }

        if(this.resizing) {
            this.resizing = false;
        }
    }

    public void mouseCoordinateUpdate(int mouseX, int mouseY) {
        if (this.drag) {
            this.x = mouseX - this.x2;
            this.y = mouseY - this.y2;
        }

        if(this.resizing) {
            this.width += mouseX - x2;

            if (width < minWidth) {
                width = minWidth;
            } else {
                this.x2 = mouseX;
            }

            this.height += mouseY - y2;

            if (height < minHeight) {
                height = minHeight;
            } else {
                this.y2 = mouseY;
            }

            if(!Display.isActive()) {
                this.resizing = false;
            }
        }
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }

    public void makeCenter(float scrW, float scrH) {
        this.x = scrW / 2f - (width / 2f);
        this.y = scrH / 2f - (height / 2f);
    }

    public void hide() {
        hide = true;
    }

    public void show() {
        hide = false;
    }

    public boolean isOnFrame(int mouseX, int mouseY) {
        if(this.resizing) {
            return RenderUtil.isHovering(mouseX, mouseY, x, y, x + width, y + draggableHeight + height);
        } else {
            return resizeable ? (RenderUtil.isHovering(mouseX, mouseY, x, y, x + width, y + draggableHeight) || RenderUtil.isHovering(mouseX, mouseY, x + width - 8, y + draggableHeight + height - 8, x + width, y + draggableHeight + height))  : RenderUtil.isHovering(mouseX, mouseY, x, y, x + width, y + draggableHeight);
        }
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return this.focused;
    }
}
