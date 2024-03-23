package dev.zprestige.prestige.client.ui.drawables.gui.field;

import dev.zprestige.prestige.client.Prestige;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import dev.zprestige.prestige.client.util.impl.RenderHelper;
import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class CustomTextField {

    public String text;
    public float x;
    public float y;
    public float width;
    public float height;
    public String value;
    public boolean active;
    public int maxLenght;
    public long time;

    public CustomTextField(String string) {
        this.text = string;
        this.value = "";
        this.maxLenght = Integer.MAX_VALUE;
    }

    public void render(int n, int n2, float f) {
        render(n, n2, f, 0.8f);
    }

    public void render(int n, int n2, float f, float f2) {
        RenderUtil.renderColoredQuad(x, y, x + width, y + height, RenderUtil.getColor(-2, f));
        RenderUtil.renderRoundedRectOutline(x, y, x + width, y + height, RenderUtil.getColor(-5, f), 0);
        if (isInsideCross(n, n2)) {
            RenderUtil.renderColoredQuad(x, y, x + width, y + height, RenderUtil.getColor(Color.BLACK, 0.1f * f));
        }
        RenderUtil.setScissorRegion(x, y, x + width - 2.5f, y + height);
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        matrixStack.scale(f2, f2, f2);
        Prestige.Companion.getFontManager().getFontRenderer().drawString((value.isEmpty() ? text : value) + (active ? typeIndicator() : ""), (x + 2.5f - Math.max(Prestige.Companion.getFontManager().getFontRenderer().getStringWidth(value) * f2 + 5 - (width - 2.5f), 0)) / f2, (y + height / 2 - Prestige.Companion.getFontManager().getFontRenderer().getStringHeight() * f2 / 1.5f) / f2, value.isEmpty() ? new Color(0.5f, 0.5f, 0.5f, f) : new Color(1, 1, 1, f));
        matrixStack.pop();
        GL11.glDisable(3089);
    }

    public void charTyped(char c) {
        if (active && c != '§' && c >= ' ' && c != '' && c != '/') {
            value = value + c;
            if (value.length() > maxLenght) {
                value = value.substring(0, maxLenght);
            }
        }
    }

    public void mouseClicked(int n, int n2, int n3) {
        if (n == 0) {
            active = isInsideCross(n2, n3) ? !active : false;
        }
    }

    public void keyPressed(int n) {
        if (active) {
            String object = value;
            switch (n) {
                case 86 -> {
                    if (GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), 341) == 1) {
                        try {
                            object = object + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                        } catch (UnsupportedFlavorException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                case 259 -> {
                    if (!object.isEmpty()) {
                        object = object.substring(0, object.length() - 1);
                    }
                }
                case 257 -> active = false;
            }
            if (object.length() > maxLenght) {
                object = object.substring(0, maxLenght);
            }
            value = object;
        }
    }

    boolean isInsideCross(int n, int n2) {
        return n > x && n < x + width && n2 > y && n2 < y + height;
    }

    String typeIndicator() {
        if (System.currentTimeMillis() - time >= 1000L) {
            time = System.currentTimeMillis();
            return "|";
        }
        return System.currentTimeMillis() - time > 500L ? "|" : "";
    }

    public void setPosition(float f, float f2) {
        x = f;
        y = f2;
    }

    public void setDimension(float f, float f2) {
        this.width = f;
        this.height = f2;
    }

    public String getValue() {
        return this.value;
    }


    public void setValue(String string) {
        this.value = string;
    }

    public void clearValue() {
        this.value = "";
    }

    public boolean isValueNotEmpty() {
        return !value.isEmpty();
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean bl) {
        this.active = bl;
    }

    public int getMaxLenght() {
        return this.maxLenght;
    }


    public void setMaxLenght(int n) {
        this.maxLenght = n;
    }
}
