package wtf.expensive.util.render;

import com.google.common.collect.Lists;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public final class SmartScissor {
    private static class State implements Cloneable {
        public boolean enabled;
        public int transX;
        public int transY;
        public int x;
        public int y;
        public int width;
        public int height;

        @Override
        public State clone() {
            try {
                return (State) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }
    }

    private static State state = new State();

    private static List<State> stateStack = Lists.newArrayList();

    public static void push() {
        stateStack.add(state.clone());
        GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
    }

    public static void pop() {
        state = stateStack.remove(stateStack.size() - 1);
        GL11.glPopAttrib();
    }

    public static void unset() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        state.enabled = false;
    }

    public static void setFromComponentCoordinates(int x, int y, int width, int height) {
        MainWindow res = Minecraft.getInstance().getMainWindow();
        int scaleFactor = 2;

        int screenX = x * scaleFactor;
        int screenY = y * scaleFactor;
        int screenWidth = width * scaleFactor;
        int screenHeight = height * scaleFactor;
        screenY = Minecraft.getInstance().getMainWindow().getHeight() - screenY - screenHeight;
        set(screenX, screenY, screenWidth, screenHeight);
    }

    public static void setFromComponentCoordinates(double x, double y, double width, double height) {
        MainWindow res = Minecraft.getInstance().getMainWindow();
        int scaleFactor = 2;

        int screenX = (int) (x * scaleFactor);
        int screenY = (int) (y * scaleFactor);
        int screenWidth = (int) (width * scaleFactor);
        int screenHeight = (int) (height * scaleFactor);
        screenY = Minecraft.getInstance().getMainWindow().getHeight() - screenY - screenHeight;
        set(screenX, screenY, screenWidth, screenHeight);
    }

    public static void setFromComponentCoordinates(double x, double y, double width, double height, float scale) {
        MainWindow res = Minecraft.getInstance().getMainWindow();
        float scaleFactor = 2;

        int screenX = (int) (x * scaleFactor);
        int screenY = (int) (y * scaleFactor);
        int screenWidth = (int) (width * scaleFactor);
        int screenHeight = (int) (height * scaleFactor);
        screenY = Minecraft.getInstance().getMainWindow().getHeight() - screenY - screenHeight;
        set(screenX, screenY, screenWidth, screenHeight);
    }

    public static void set(int x, int y, int width, int height) {
        Rectangle screen = new Rectangle(0, 0, Minecraft.getInstance().getMainWindow().getWidth(), Minecraft.getInstance().getMainWindow().getHeight());
        Rectangle current;
        if (state.enabled) {
            current = new Rectangle(state.x, state.y, state.width, state.height);
        } else {
            current = screen;
        }
        Rectangle target = new Rectangle(x + state.transX, y + state.transY, width, height);
        Rectangle result = current.intersection(target);
        result = result.intersection(screen);
        if (result.width < 0) result.width = 0;
        if (result.height < 0) result.height = 0;
        state.enabled = true;
        state.x = result.x;
        state.y = result.y;
        state.width = result.width;
        state.height = result.height;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(result.x, result.y, result.width, result.height);
    }

    public static void translate(int x, int y) {
        state.transX = x;
        state.transY = y;
    }

    public static void translateFromComponentCoordinates(int x, int y) {
        MainWindow res = Minecraft.getInstance().getMainWindow();
        int totalHeight = res.getScaledHeight();
        int scaleFactor = (int) res.getGuiScaleFactor();

        int screenX = x * scaleFactor;
        int screenY = y * scaleFactor;
        screenY = (totalHeight * scaleFactor) - screenY;
        translate(screenX, screenY);
    }

    private SmartScissor() {
    }

}