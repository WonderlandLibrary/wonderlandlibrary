/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client.clickgui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.elements.Element;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.Style;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.LiquidSenseStyle1;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SideOnly(Side.CLIENT)
public abstract class Panel extends MinecraftInstance {

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
    private int add = 0;
    private boolean visible;

    private int elementsHeight;

    private float fade;

    public Panel(String name, int x, int y, int width, int height, boolean open) {
        this.name = name;
        this.elements = new ArrayList<>();
        this.scrollbar = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.open = open;
        this.visible = true;

        setupItems();
    }

    public abstract void setupItems();

    public void drawScreen(int mouseX, int mouseY, float button) {
        if (!visible)
            return;

        final int maxElements = ((ClickGUI) Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).maxElementsValue.get();

        // Drag
        if (drag) {
            int nx = x2 + mouseX;
            int ny = y2 + mouseY;
            if (nx > -1)
                x = nx;

            if (ny > -1)
                y = ny;
        }

        //elementsHeight = getElementsHeight() - 1;
        elementsHeight = 0;
        boolean scrollbar = elements.size() >= maxElements;
        if (this.scrollbar != scrollbar)
            this.scrollbar = scrollbar;

        LiquidBounce.clickGui.style.drawPanel(mouseX, mouseY, this);

        int y = this.y + height - 2;
        int y2 = 0;
        int count = 0;

        for (final Element element : elements) {
            if (++count > scroll && count < scroll + (maxElements + 1) && scroll < elements.size() && elementsHeight < getElementsHeight() + y2) {
                element.setLocation(x, y);
                element.setWidth(getWidth());
                if (y <= getY() + fade + y2) {
                    element.drawScreen(mouseX, mouseY, button);
                    final Style style = LiquidBounce.clickGui.style;
                    if (style instanceof LiquidSenseStyle1) {
                        y += ((LiquidSenseStyle1) style).settingHeight;
                        y2 += ((LiquidSenseStyle1) style).settingHeight;
                    }
                }

                y += element.getHeight() + 1;
                elementsHeight = y + 2 - (this.y + height);
                element.setVisible(true);
            } else
                element.setVisible(false);
        }
        add = y2 / 18;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!visible)
            return;

        if (mouseButton == 1 && isHovering(mouseX, mouseY)) {
            open = !open;
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("random.bow"), 1.0F));
            return;
        }

        for (final Element element : elements)
            if (element.getY() <= getY() + fade)
                element.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (!visible)
            return;

        drag = false;

        if (!open)
            return;

        for (final Element element : elements)
            element.mouseReleased(mouseX, mouseY, state);
    }

    public boolean handleScroll(int mouseX, int mouseY, int wheel) {
        final int maxElements = ((ClickGUI) Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).maxElementsValue.get();
        final boolean liquidsense = LiquidBounce.clickGui.style instanceof LiquidSenseStyle1;
        if (mouseX >= getX() && mouseX <= getX() + 100 && mouseY >= getY() && mouseY <= getY() + 19 + elementsHeight) {
            if (wheel < 0 && scroll < (liquidsense ? elements.size() + add : elements.size()) - maxElements) {
                ++scroll;
                if (scroll < 0)
                    scroll = 0;
            } else if (wheel > 0) {
                --scroll;
                if (scroll < 0)
                    scroll = 0;
            }

            if (wheel < 0) {
                if (dragged < (liquidsense ? elements.size() + add : elements.size()) - maxElements)
                    ++dragged;
            } else if (wheel > 0 && dragged >= 1) {
                --dragged;
            }

            return true;
        }
        return false;
    }

    void updateFade(final int delta) {
        if (open) {
            if (fade < elementsHeight) fade += 0.4F * delta;
            if (fade > elementsHeight) fade = (int) elementsHeight;
        } else {
            if (fade > 0) fade -= 0.4F * delta;
            if (fade < 0) fade = 0;
        }
    }

    public String getName() {
        return name;
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
        return visible;
    }

    public List<Element> getElements() {
        return elements;
    }

    public int getFade() {
        return (int) fade;
    }

    public int getDragged() {
        return dragged;
    }

    private int getElementsHeight() {
//        return elementsHeight;
        int height = 0;
        int count = 0;
        for (final Element element : elements) {
            if (count >= ((ClickGUI) Objects.requireNonNull(LiquidBounce.moduleManager.getModule(ClickGUI.class))).maxElementsValue.get())
                continue;
            height += element.getHeight() + 1;
            ++count;
        }
        return height;
    }

    boolean isHovering(int mouseX, int mouseY) {
        final float textWidth = mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(name)) - 100F;
        return mouseX >= x - textWidth / 2F - 19F && mouseX <= x - textWidth / 2F + mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(name)) + 19F && mouseY >= y && mouseY <= y + height - (open ? 2 : 0);
    }
}
