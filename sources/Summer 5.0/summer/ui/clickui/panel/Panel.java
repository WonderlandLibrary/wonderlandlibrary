package summer.ui.clickui.panel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import summer.Summer;
import summer.base.font.FontManager;
import summer.ui.clickuiutils.AnimationUtil;
import summer.ui.clickuiutils.MouseUtil;

/**
 * @author : AmirCC
 * @created : 1:18 PM, 10/27/2020, Tuesday
 **/
public class Panel {

    private float x, y, width, height, lastX, lastY;
    private boolean dragging, expand;
    private ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
    public FontManager fm = Summer.INSTANCE.getFontManager();

    //animations
    private float xAnimate = 0.0f;
    private float yAnimate = 0.0f;

    public Panel(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void updatePosition(float x, float y) {

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        resolution = new ScaledResolution(Minecraft.getMinecraft());
        if (isDragging()) {
            xAnimate = AnimationUtil.moveUD(getX(), mouseX + getLastX(), 0.000542f);
            yAnimate = AnimationUtil.moveUD(getY(), mouseY + getLastY(), 0.000542f);
            setX(xAnimate);
            setY(yAnimate);
            if (getX() < 0) setX(0);
            if (getY() < 0) setY(0);
            if (getX() + getWidth() > getResolution().getScaledWidth())
                setX(getResolution().getScaledWidth() - getWidth());
            if (getY() + getHeight() > getResolution().getScaledHeight())
                setY(getResolution().getScaledHeight() - getHeight());
            updatePosition(getX(), getY());
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(getX(), getY(), getX() + getWidth(), getY() + 14)) {
            if (mouseButton == 0) {
                setDragging(true);
                setLastX(getX() - mouseX);
                setLastY(getY() - mouseY);
            }
            if (mouseButton == 1) {
                setExpand(!isExpand());
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (isDragging() && state == 0)
            setDragging(false);
    }

    public void handleMouseInput() {

    }

    public void keyTyped(char typedChar, int keyCode) {

    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getLastX() {
        return lastX;
    }

    public void setLastX(float lastX) {
        this.lastX = lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public ScaledResolution getResolution() {
        return resolution;
    }

    public boolean isHovered(float x, float y, float x2, float y2) {
        return MouseUtil.INSTANCE.isHovered(x, y, x2, y2);
    }

}
