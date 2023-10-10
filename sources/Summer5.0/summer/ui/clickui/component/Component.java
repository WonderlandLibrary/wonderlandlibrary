package summer.ui.clickui.component;

import summer.Summer;
import summer.base.font.FontManager;
import summer.ui.clickuiutils.MouseUtil;

/**
 * @author : AmirCC
 * @created : 1:25 PM, 10/27/2020, Tuesday
 **/
public abstract class Component<T> {

    private T object;
    private float x, y, staticX, staticY, width, height, offsetX, offsetY;
    public FontManager fm = Summer.INSTANCE.getFontManager();

    public Component(T object, float staticX, float staticY, float width, float height, float offsetX, float offsetY) {
        this.object = object;
        this.x = staticX + offsetX;
        this.y = staticY + offsetY;
        this.staticX = staticX;
        this.staticY = staticY;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public abstract void updatePosition(float x, float y);

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public void handleMouseInput() {

    }

    public void keyTyped(char typedChar, int keyCode) {

    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
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

    public float getStaticX() {
        return staticX;
    }

    public void setStaticX(float staticX) {
        this.staticX = staticX;
    }

    public float getStaticY() {
        return staticY;
    }

    public void setStaticY(float staticY) {
        this.staticY = staticY;
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

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public boolean isHovered(float x, float y, float x2, float y2){
        return MouseUtil.INSTANCE.isHovered(x, y, x2, y2);
    }

}
