/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.wintware.client.clickgui.panel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.wintware.client.Main;
import me.wintware.client.clickgui.panel.AnimationState;
import me.wintware.client.clickgui.panel.component.Component;
import me.wintware.client.clickgui.panel.component.impl.ModuleComponent;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.animation.AnimationUtil;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public final class Panel {
    public static final int HEADER_SIZE = 20;
    public static final int HEADER_OFFSET = 2;
    public final Category category;
    public final List components = new ArrayList();
    public final int width;
    public double scissorBoxHeight;
    public int x;
    public int lastX;
    public int y;
    public int lastY;
    public int height;
    public AnimationState state = AnimationState.STATIC;
    public boolean dragging;
    public int activeRectAnimate;
    public double scalling;

    public Panel(Category category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = 100;
        int componentY = 20;
        List<Module> modulesForCategory = Arrays.asList(Main.instance.moduleManager.getModulesInCategory(category));
        int modulesForCategorySize = modulesForCategory.size();
        for (int i = 0; i < modulesForCategorySize; ++i) {
            Module module = modulesForCategory.get(i);
            ModuleComponent component = new ModuleComponent(module, this, 0, componentY, this.width, 15);
            this.components.add(component);
            componentY += 15;
        }
        this.height = componentY - 20;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private void updateComponentHeight() {
        int componentY = 20;
        List componentList = this.components;
        int componentListSize = componentList.size();
        for (int i = 0; i < componentListSize; ++i) {
            Component component = (Component)componentList.get(i);
            component.setY(componentY);
            componentY = (int)((double)componentY + (double)component.getHeight() + component.getOffset());
        }
        this.height = componentY - 20;
    }

    public final void onDraw(int mouseX, int mouseY) {
        int x = this.x;
        int y = this.y;
        int width = this.width;
        this.updateComponentHeight();
        this.handleScissorBox();
        this.handleDragging(mouseX, mouseY);
        double scissorBoxHeight = this.scissorBoxHeight;
        int backgroundColor = new Color(17, 17, 17).getRGB();
        this.activeRectAnimate = (int)AnimationUtil.animate(this.activeRectAnimate, this.dragging ? -1.879048192E9 : (double)backgroundColor, 0.1f);
        RenderUtil.drawSmoothRect(x - 2, y, x + width + 2, (float)((double)(y + 20) + scissorBoxHeight), this.dragging ? -1879048192 : backgroundColor);
        RenderUtil.drawSmoothRect(x - 2, y, x + width + 2, y + 20, this.dragging ? -1879048192 : backgroundColor);
        String name = "wintware/" + this.category.name + ".png";
        RenderUtil.drawSmoothRect(this.x + -2, this.y + -2, this.x + this.width + 2, this.y, this.category.getColor());
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(name));
        Gui.drawModalRectWithCustomSizedTexture(x + this.getWidth() - 20, this.y - -2, 0.0f, 0.0f, 13, 13, 13.0f, 13.0f);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.category.name, x + 2, (float)y + 10.0f - 3.0f, -1);
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        RenderUtil.prepareScissorBox(x - 2, y + 20 - 2, x + width + 2, (float)((double)(y + 20) + scissorBoxHeight));
        List components = this.components;
        int componentsSize = components.size();
        for (int i = 0; i < componentsSize; ++i) {
            ((Component)components.get(i)).onDraw(mouseX, mouseY);
            if (i == componentsSize - 1) continue;
            RenderUtil.prepareScissorBox(x - 2, y + 20, x + width + 2, (float)((double)(y + 20) + scissorBoxHeight));
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
    }

    public final void onMouseClick(int mouseX, int mouseY, int mouseButton) {
        int x = this.x;
        int y = this.y;
        int width = this.width;
        double scissorBoxHeight = this.scissorBoxHeight;
        if (mouseX > x - 2 && mouseX < x + width + 2 && mouseY > y && mouseY < y + 20) {
            if (mouseButton == 1) {
                if (scissorBoxHeight > 0.0 && (this.state == AnimationState.EXPANDING || this.state == AnimationState.STATIC)) {
                    this.state = AnimationState.RETRACTING;
                } else if (scissorBoxHeight < (double)(this.height + 2) && (this.state == AnimationState.EXPANDING || this.state == AnimationState.STATIC)) {
                    this.state = AnimationState.EXPANDING;
                }
            } else if (mouseButton == 0 && !this.dragging) {
                this.lastX = x - mouseX;
                this.lastY = y - mouseY;
                this.dragging = true;
            }
        }
        List components = this.components;
        int componentsSize = components.size();
        for (int i = 0; i < componentsSize; ++i) {
            Component component = (Component)components.get(i);
            int componentY = component.getY();
            if (!((double)componentY < scissorBoxHeight + 20.0)) continue;
            component.onMouseClick(mouseX, mouseY, mouseButton);
        }
    }

    public final void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
        if (this.dragging) {
            this.dragging = false;
        }
        if (this.scissorBoxHeight > 0.0) {
            List components = this.components;
            int componentsSize = components.size();
            for (int i = 0; i < componentsSize; ++i) {
                ((Component)components.get(i)).onMouseRelease(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void setY(int y) {
        this.y = y;
    }

    public final void onKeyPress(char typedChar, int keyCode) {
        if (this.scissorBoxHeight > 0.0) {
            List components = this.components;
            int componentsSize = components.size();
            for (int i = 0; i < componentsSize; ++i) {
                ((Component)components.get(i)).onKeyPress(typedChar, keyCode);
            }
        }
    }

    private void handleDragging(int mouseX, int mouseY) {
        if (this.dragging) {
            this.x = mouseX + this.lastX;
            this.y = mouseY + this.lastY;
        }
    }

    private void handleScissorBox() {
        int height = this.height;
        switch (this.state) {
            case EXPANDING: {
                if (this.scissorBoxHeight < (double)(height + 2)) {
                    this.scissorBoxHeight = AnimationUtil.animate(height + 2, this.scissorBoxHeight, 0.07);
                    break;
                }
                if (!(this.scissorBoxHeight >= (double)(height + 2))) break;
                this.state = AnimationState.STATIC;
                break;
            }
            case RETRACTING: {
                if (this.scissorBoxHeight > 0.0) {
                    this.scissorBoxHeight = AnimationUtil.animate(0.0, this.scissorBoxHeight, 0.07);
                    break;
                }
                if (!(this.scissorBoxHeight <= 0.0)) break;
                this.state = AnimationState.STATIC;
                break;
            }
            case STATIC: {
                if (this.scissorBoxHeight > 0.0 && this.scissorBoxHeight != (double)(height + 2)) {
                    this.scissorBoxHeight = AnimationUtil.animate(height + 2, this.scissorBoxHeight, 0.07);
                }
                this.scissorBoxHeight = this.clamp(this.scissorBoxHeight, height + 2);
            }
        }
    }

    private double clamp(double a, double max) {
        if (a < 0.0) {
            return 0.0;
        }
        return Math.min(a, max);
    }
}

