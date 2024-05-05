/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.StringUtils
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.ui.client.clickgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.Element;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public abstract class Panel
extends MinecraftInstance {
    private final String name;
    public int x;
    public int y;
    public int x2;
    public int y2;
    private final int width;
    private final int height;
    private int scroll;
    private int dragged;
    private boolean open;
    public boolean drag;
    private boolean scrollbar;
    private final List<Element> elements;
    private boolean visible;
    private float elementsHeight;
    private float fade;

    public Panel(String name, int x, int y, int width, int height, boolean open) {
        this.name = name;
        this.elements = new ArrayList<Element>();
        this.scrollbar = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.open = open;
        this.visible = true;
        this.setupItems();
    }

    public abstract void setupItems();

    public void drawScreen(int mouseX, int mouseY, float button) {
        boolean scrollbar;
        if (!this.visible) {
            return;
        }
        int maxElements = (Integer)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).maxElementsValue.get();
        if (this.drag) {
            int nx = this.x2 + mouseX;
            int ny = this.y2 + mouseY;
            if (nx > -1) {
                this.x = nx;
            }
            if (ny > -1) {
                this.y = ny;
            }
        }
        this.elementsHeight = this.getElementsHeight() - 1;
        boolean bl = scrollbar = this.elements.size() >= maxElements;
        if (this.scrollbar != scrollbar) {
            this.scrollbar = scrollbar;
        }
        LiquidBounce.clickGui.style.drawPanel(mouseX, mouseY, this);
        int y = this.y + this.height - 2;
        int count = 0;
        for (Element element : this.elements) {
            if (++count > this.scroll && count < this.scroll + (maxElements + 1) && this.scroll < this.elements.size()) {
                element.setLocation(this.x, y);
                element.setWidth(this.getWidth());
                if ((float)y <= (float)this.getY() + this.fade) {
                    element.drawScreen(mouseX, mouseY, button);
                }
                y += element.getHeight() + 1;
                element.setVisible(true);
                continue;
            }
            element.setVisible(false);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!this.visible) {
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            mc.getSoundHandler().playSound("random.bow", 1.0f);
            return;
        }
        for (Element element : this.elements) {
            if (!((float)element.getY() <= (float)this.getY() + this.fade)) continue;
            element.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (!this.visible) {
            return;
        }
        this.drag = false;
        if (!this.open) {
            return;
        }
        for (Element element : this.elements) {
            element.mouseReleased(mouseX, mouseY, state);
        }
    }

    public boolean handleScroll(int mouseX, int mouseY, int wheel) {
        int maxElements = (Integer)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).maxElementsValue.get();
        if (mouseX >= this.getX() && mouseX <= this.getX() + 100 && mouseY >= this.getY() && (float)mouseY <= (float)(this.getY() + 19) + this.elementsHeight) {
            if (wheel < 0 && this.scroll < this.elements.size() - maxElements) {
                ++this.scroll;
                if (this.scroll < 0) {
                    this.scroll = 0;
                }
            } else if (wheel > 0) {
                --this.scroll;
                if (this.scroll < 0) {
                    this.scroll = 0;
                }
            }
            if (wheel < 0) {
                if (this.dragged < this.elements.size() - maxElements) {
                    ++this.dragged;
                }
            } else if (wheel > 0 && this.dragged >= 1) {
                --this.dragged;
            }
            return true;
        }
        return false;
    }

    void updateFade(int delta) {
        if (this.open) {
            if (this.fade < this.elementsHeight) {
                this.fade += 0.4f * (float)delta;
            }
            if (this.fade > this.elementsHeight) {
                this.fade = (int)this.elementsHeight;
            }
        } else {
            if (this.fade > 0.0f) {
                this.fade -= 0.4f * (float)delta;
            }
            if (this.fade < 0.0f) {
                this.fade = 0.0f;
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int dragX) {
        this.x = dragX;
    }

    public void setY(int dragY) {
        this.y = dragY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean getScrollbar() {
        return this.scrollbar;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean getOpen() {
        return this.open;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public List<Element> getElements() {
        return this.elements;
    }

    public int getFade() {
        return (int)this.fade;
    }

    public int getDragged() {
        return this.dragged;
    }

    private int getElementsHeight() {
        int height = 0;
        int count = 0;
        for (Element element : this.elements) {
            if (count >= (Integer)((ClickGUI)Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).maxElementsValue.get()) continue;
            height += element.getHeight() + 1;
            ++count;
        }
        return height;
    }

    boolean isHovering(int mouseX, int mouseY) {
        float textWidth = (float)mc.getFontRendererObj().getStringWidth(StringUtils.func_76338_a((String)this.name)) - 100.0f;
        return (float)mouseX >= (float)this.x - textWidth / 2.0f - 19.0f && (float)mouseX <= (float)this.x - textWidth / 2.0f + (float)mc.getFontRendererObj().getStringWidth(StringUtils.func_76338_a((String)this.name)) + 19.0f && mouseY >= this.y && mouseY <= this.y + this.height - (this.open ? 2 : 0);
    }
}

