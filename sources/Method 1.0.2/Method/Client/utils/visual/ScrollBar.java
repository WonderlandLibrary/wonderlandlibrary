package Method.Client.utils.visual;

import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.Serializable;

public class ScrollBar implements Serializable {
    // Bar = on the side to click and drag
    // width and height is of the bar size
    // travel is the amount the bar can move
    // scroll max is the max amount the bar can move, always pos 0-Max
    // scrollfactor is our output scroll factor for (1) to 1 or (5) to 1
    // min/max is screen space that we can scroll in. aka make a box from 10,10 to 20,20 we can scroll in that box
    // direction is the direction the bar can move pos = up neg = down

    public ScrollBar(boolean bar, int width, int height, int travel, int scrollmax, int scrollfactor, int minX, int maxX, int minY, int maxY, boolean directionPositive) {
        this.bar = bar;
        this.width = width;
        this.height = height;
        this.scrollfactor = scrollfactor;
        this.travel = travel;
        this.scrollmax = scrollmax;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.directionPositive = directionPositive;
    }

    public boolean bar;
    public boolean noScroll = false;
    public boolean horizontal = false;
    public int travel;
    public int width;
    public int height;
    public int scroll = 0;
    public int lastscroll = 0;
    public int scrollfactor;
    public int scrollmax;
    public int minX;
    public int maxX;
    public int minY;
    public int maxY;

    public boolean directionPositive;
    public boolean scrolldragging = false;

    // This is from @DrawScreen
    public void scrollCheck(int mouseX, int mouseY, int posX, int posY) {
        if (!noScroll)
            if (withinBounds(mouseX, mouseY)) {
                int wheel = Mouse.getDWheel();
                if (wheel < 0)
                    changeScroll(-1);
                else if (wheel > 0)
                    changeScroll(+1);
            }
        if (bar) {
            if (scrolldragging) {
                if (!horizontal) {
                    if (mouseY < posY + (height / 2) + (scroll * (travel)))
                        changeScroll(+1);
                    else if (mouseY > posY - (height / 2) + (scroll * travel))
                        changeScroll(-1);
                } else {
                    if (mouseX < posX + (width / 2) + (scroll * (travel)))
                        changeScroll(+1);
                    else if (mouseX > posX - (width / 2) + (scroll * travel))
                        changeScroll(-1);
                }
            }
            if (scroll > scrollmax)
                scroll = scrollmax;
            else if (scroll < 0)
                scroll = 0;

            //minx,miny,maxx,maxy
            if (!horizontal)
                Gui.drawRect(posX - width / 2, posY + height / 2 + (scroll * travel), posX + width / 2, posY - height / 4 + (scroll * travel), new Color(255, 255, 255, 255).getRGB());
            else
                Gui.drawRect(posX + width / 2 + (scroll * travel), posY - height / 2, posX - width / 4 + (scroll * travel), posY + height / 2, new Color(255, 255, 255, 255).getRGB());
            if (!horizontal)
                RenderUtils.drawRectOutline(posX - width / 2, posY + height / 2 + (scroll * travel), posX + width / 2, posY - height / 4 + (scroll * travel), 1, ColorUtils.rainbow().getRGB());
            else
                RenderUtils.drawRectOutline(posX + width / 2 + (scroll * travel), posY - height / 2, posX - width / 4 + (scroll * travel), posY + height / 2, 1, ColorUtils.rainbow().getRGB());
        }
    }

    //This is from @MouseClicked && bar
    public void MouseClicked(int mouseX, int mouseY, int posX, int posY) {
        if (!horizontal) {
            if (mouseX > posX - width / 2 && mouseX < posX + width / 2) {
                if (mouseY > posY - height / 2 + (scroll * travel) && mouseY < posY + height / 4 + (scroll * travel)) {
                    scrolldragging = true;
                }
            }
        } else if (mouseX > posX - width / 2 + (scroll * travel) && mouseX < posX + width / 2 + (scroll * travel)) {
            if (mouseY > posY - height / 2 && mouseY < posY + height / 2) {
                scrolldragging = true;
            }
        }
    }

    // This is from @MouseRelease
    public void MouseRelease() {
        scrolldragging = false;
    }

    private void changeScroll(int i) {
        scroll += directionPositive ? i : -i;
    }

    // within bounds void
    public boolean withinBounds(int mouseX, int mouseY) {
        return mouseX > minX && mouseX < maxX && mouseY > minY && mouseY < maxY;
    }

    public int getScroll() {
        return (directionPositive) ? 1 : -1 * scroll * scrollfactor;
    }

    // get last scroll which is lastscroll - scroll
    public int getLastScroll() {
        return (directionPositive) ? 1 : -1 * (lastscroll - scroll) * scrollfactor;
    }

}
