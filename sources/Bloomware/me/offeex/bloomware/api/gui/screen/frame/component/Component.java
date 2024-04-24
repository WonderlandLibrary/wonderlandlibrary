package me.offeex.bloomware.api.gui.screen.frame.component;

import me.offeex.bloomware.client.module.client.Colors;
import me.offeex.bloomware.client.module.client.Gui;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public abstract class Component {
    public final Colors colors = Colors.INSTANCE;
    public final Gui gui = Gui.INSTANCE;
    public final static int COMPONENT_HEIGHT = 14;
    public int offsetNested = 5;
    protected boolean pressed;

    public abstract void render(MatrixStack matrix);

    public void updateComponent(double mouseX, double mouseY) {}

    public void mouseClicked(double mouseX, double mouseY, int button) {
        pressed = true;
    }

    public void mouseReleased(double mouseX,  double mouseY,  int mouseButton) {
        pressed = false;
    }

    public void keyTyped(int key) {
        if (key == GLFW.GLFW_KEY_ESCAPE) pressed = false;
    }

    public void setOffsetY(int offsetY) {}

    public int getHeight() { return COMPONENT_HEIGHT; }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
