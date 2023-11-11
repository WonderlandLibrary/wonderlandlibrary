package me.nyan.flush.ui.elements;

import me.nyan.flush.Flush;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.PboUtils;
import me.nyan.flush.utils.render.RenderUtils;
import me.nyan.flush.utils.render.Stencil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class Button {
    private String name;
    private float x;
    private float y;
    private float width;
    private float height;
    private float hoveredLevel;
    private int cornersRadius;
    private boolean enabled;

    private int color = 0xFF1E1E1E, focusedColor = 0xFF353535;

    public Button(String name, float x, float y, float width, float height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        cornersRadius = 3;
        enabled = true;
    }

    public void drawButton(int mouseX, int mouseY) {
        if (isHovered(mouseX, mouseY)) {
            if (hoveredLevel < 1) {
                hoveredLevel += RenderUtils.calculateSpeed(0.1F);
                if (hoveredLevel > 1) {
                    hoveredLevel = 1;
                }
            }
        } else if (hoveredLevel > 0) {
            hoveredLevel -= RenderUtils.calculateSpeed(0.1F);
            if (hoveredLevel < 0) {
                hoveredLevel = 0;
            }
        }

        glEnable(GL_SCISSOR_TEST);
        RenderUtils.glScissor(x, y, x + width, y + height);

        glPushMatrix();

        RenderUtils.fillRoundRect(x, y, width, height, cornersRadius, color);

        if (hoveredLevel > 0) {
            Stencil.setup();
            RenderUtils.fillRoundRect(x, y, width, height, cornersRadius, 0);
            Stencil.draw();
            RenderUtils.fillCircle(x + width / 2, y + height / 2, hoveredLevel * width, focusedColor);
            Stencil.finish();
        }

        RenderUtils.glColor(-1);
        glPopMatrix();

        if (name != null) {
            Flush.getFont("GoogleSansDisplay", 18)
                    .drawXYCenteredString(name, x + width / 2f, y + height / 2f,
                            enabled ? ColorUtils.contrast(color) : 0xFFAAAAAA);
        }
        glDisable(GL_SCISSOR_TEST);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        return enabled && isHovered(mouseX, mouseY) && mouseButton == 0;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setCornersRadius(int cornersRadius) {
        this.cornersRadius = cornersRadius;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private boolean isHovered(int mouseX, int mouseY) {
        return enabled && mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setFocusedColor(int focusedColor) {
        this.focusedColor = focusedColor;
    }
}
