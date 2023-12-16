package wtf.expensive.ui.beta.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.settings.Setting;
import wtf.expensive.ui.beta.ClickGui;
import wtf.expensive.ui.beta.component.IComponent;

/**
 * @author dedinside
 * @since 10.06.2023
 */
public abstract class Component implements IComponent {
    public float x, y, width, height;
    public Function function;
    public ClickGui parent;
    public Setting s;


    public boolean isHovered(int mouseX, int mouseY, float width, float height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public boolean isHovered(int mouseX, int mouseY, float x,float y, float width, float height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public boolean isHovered(int mouseX, int mouseY, float height) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public void setPosition(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public abstract void drawComponent(MatrixStack matrixStack, int mouseX, int mouseY);

    @Override
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    @Override
    public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);

    @Override
    public abstract void keyTyped(int keyCode, int scanCode, int modifiers);

    @Override
    public abstract void charTyped(char codePoint, int modifiers);

    public void onConfigUpdate() {

    }
}
