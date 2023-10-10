package today.flux.gui.clickgui.classic.window;


import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import today.flux.Flux;
import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.component.Component;
import today.flux.gui.clickgui.classic.component.PresetList;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.module.implement.Render.Hud;
import today.flux.utility.AnimationTimer;
import today.flux.utility.MathUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Window {
    public String title;
    public String id;
    public boolean isEnabled;
    public int x;
    public int y;
    @Getter
    public int width;
    @Getter
    public int height;
    private int grabX;
    private int grabY;
    private boolean isDragging;
    public int scrollY;
    private float scrollAmount;
    public boolean scrollbarEnabled;
    private int componentsHeight;
    private boolean isScrolling;
    private int toScrollY;
    private boolean openHover;
    private boolean wasMousePressed;
    public List<today.flux.gui.clickgui.classic.component.Component> children = new ArrayList<>();

    public AnimationTimer feedTimer = new AnimationTimer(20);

    public float crX, crY, crXX, crYY;

    public Window(String title, int x, int y) {
        this.x = x;
        this.y = y;
        this.title = title;
        this.id = title;
        this.width = ClickGUI.defaultWidth;
        this.height = ClickGUI.defaultHeight;
    }

    public void repositionComponents() {
        int maxWidth = 0;
        int y = ClickGUI.defaultHeight;

        //caluclate height
        today.flux.gui.clickgui.classic.component.Component c;
        Iterator var4;
        for (var4 = this.children.iterator(); var4.hasNext(); maxWidth = Math.max(maxWidth, c.width)) {
            c = (today.flux.gui.clickgui.classic.component.Component) var4.next();
            c.offY = y;
            y += c.isVisible() ? c.height : 0;
        }

        this.width = Math.max(ClickGUI.defaultWidth, maxWidth);

        for (var4 = this.children.iterator(); var4.hasNext(); c.width = this.width) {
            c = (today.flux.gui.clickgui.classic.component.Component) var4.next();
        }

        this.height = Math.min(ClickGUI.maxWindowHeight, y);
        this.componentsHeight = y - ClickGUI.defaultHeight;

        if (this.toScrollY > this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight)) {
            this.toScrollY = this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight);
        }
        if (this.toScrollY < 0) {
            this.toScrollY = 0;
        }
    }

    public void update(int mouseX, int mouseY) {
        if (!this.isEnabled)
            feedTimer.update(false);

        if (this.isDragging) {
            this.x = mouseX - this.grabX;
            this.y = mouseY - this.grabY;
        }

        this.scrollbarEnabled = this.componentsHeight + ClickGUI.defaultHeight > ClickGUI.maxWindowHeight;

        for (today.flux.gui.clickgui.classic.component.Component component : this.children) {
            component.update(mouseX, mouseY);
        }

        if (this.scrollbarEnabled && this.isScrolling) {
            this.scrollAmount = MathUtils.map((float) (mouseY - this.y), (float) (ClickGUI.defaultHeight + 3) + (float) ClickGUI.scrollbarHeight / 2.0F, (float) (ClickGUI.maxWindowHeight - 3) - (float) ClickGUI.scrollbarHeight / 2.0F, 0.0F, 1.0F);
            if (this.scrollAmount > 1.0F) {
                this.scrollAmount = 1.0F;
            }

            if (this.scrollAmount < 0.0F) {
                this.scrollAmount = 0.0F;
            }

            this.scrollY = (int) (this.scrollAmount * (float) (this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight)));
            this.toScrollY = this.scrollY;
        }

        if (Math.abs(this.toScrollY - this.scrollY) < 4) {
            this.scrollY = this.toScrollY;
            this.scrollAmount = (float) this.scrollY / (float) (this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight));
        } else if (this.scrollY > this.toScrollY) {
            this.scrollY -= 4;
            this.scrollAmount = (float) this.scrollY / (float) (this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight));
        } else if (this.scrollY < this.toScrollY) {
            this.scrollY += 4;
            this.scrollAmount = (float) this.scrollY / (float) (this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight));
        }
    }


    public void render(int mouseX, int mouseY) {
        if (this.isDragging) {
            if (feedTimer.getValue() > 0.5)
                feedTimer.update(false);
        } else {
            if (feedTimer.getValue() < 1.0)
                feedTimer.update(true);
        }

        Color rawColor = new Color(Hud.isLightMode ? ClickGUI.lightMainColor : ClickGUI.mainColor);

        int color = new Color(rawColor.getRed(), rawColor.getGreen(), rawColor.getBlue(), (int) (feedTimer.getValue() * 255)).getRGB();

        int height = ClickGUI.defaultHeight;

        //draw title bar
        final float expand = 1f;
        //BlurBuffer.blurArea(x - 1, y, width + 2, height, true);
        //GuiRenderUtils.drawRoundedRectOnlyUp((float) this.x, (float) this.y, (float) this.width, (float) height, 3, color, 0.0f, color);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableAlpha();

        //draw background
        int totalHeight = 0;
        for (today.flux.gui.clickgui.classic.component.Component i : this.children)
            if (i.isEnabled() && i.isVisible())
                totalHeight += i.height;

        if (this.scrollbarEnabled)
            totalHeight = ClickGUI.maxWindowHeight - ClickGUI.defaultHeight;

        //title bar text
        GuiRenderUtils.drawRect(this.x - expand, this.y, this.width + (expand * 2.0f), height, color);
        FontManager.small.drawCenteredStringWithShadow(this.title, this.x + (int) ((this.width - 2) / 2.0), this.y + height / 2 - FontManager.small.getHeight(this.title) / 2 + 0.5f, 16777215);

        //draw background
        GuiRenderUtils.drawRect(this.x, this.y + height, this.width, totalHeight, Hud.isLightMode ? ClickGUI.lightBackgroundColor : ClickGUI.backgroundColor);

        GlStateManager.disableAlpha();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiRenderUtils.doGlScissor((int) (this.x), this.y + height, this.width, this.height - ClickGUI.defaultHeight, 2);

        //Crazy CODE
        this.crX = (float) this.x;
        this.crY = (float) this.y + (float) ((double) (this.height - ClickGUI.defaultHeight)) + (float) ClickGUI.defaultHeight;
        this.crXX = (float) this.width;
        this.crYY = (float) (this.height - ClickGUI.defaultHeight);

        //draw components
        for (today.flux.gui.clickgui.classic.component.Component i : this.children) {
            if (i.isEnabled()) {
                i.doRender(mouseX, mouseY);
            }
        }

        //draw scroll bar
        if (this.scrollbarEnabled) {
            int c2 = (int) MathUtils.map(this.scrollAmount, 0.0F, 1.0F, 1.0F, (float) (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight - ClickGUI.scrollbarHeight) - 1) + this.y + ClickGUI.defaultHeight;
            GuiRenderUtils.drawRoundedRect((float) (this.x + this.width - ClickGUI.scrollbarWidth), (float) c2, (float) ClickGUI.scrollbarWidth, (float) ClickGUI.scrollbarHeight , 2, ClickGUI.scrollbarColor, .5f, ClickGUI.scrollbarColor);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }


    public void handleMouseUpdates(int mouseX, int mouseY, boolean isPressed) {
        /*if (isPressed && !this.wasMousePressed && this.overScrollbar(mouseX, mouseY)) {
            this.isScrolling = true;
        }

        if (!isPressed) {
            this.isScrolling = false;
        }*/

        if (this.mouseOver(mouseX, mouseY)) {
            int barHeight = ClickGUI.defaultHeight;
            this.openHover = MathUtils.contains((float) mouseX, (float) mouseY, (float) (this.x + this.width - barHeight), (float) this.y, (float) (this.x + this.width), (float) (this.y + barHeight));
            if (!this.wasMousePressed && isPressed) {
                this.bringToFront();
            }

            if (this.openHover && !this.wasMousePressed && isPressed) {
                //Closed!
            }

            boolean overBar = MathUtils.contains((float) mouseX, (float) mouseY, (float) this.x, (float) this.y, (float) (this.x + this.width), (float) (this.y + barHeight));


            if (!this.openHover && overBar && !this.wasMousePressed && isPressed) {
                this.isDragging = true;
                this.grabX = mouseX - this.x;
                this.grabY = mouseY - this.y;
            } else if (!isPressed) {
                this.isDragging = false;
            }

            today.flux.gui.clickgui.classic.component.Component c;
            Iterator<today.flux.gui.clickgui.classic.component.Component> childIter = this.children.iterator();
            if (!overBar) {
                while (childIter.hasNext()) {
                    c = childIter.next();
                    if (c.isEnabled() && c.isVisible()) {
                        c.mouseUpdates(mouseX, mouseY, isPressed);
                    }
                }
            } else {
                while (childIter.hasNext()) {
                    c = childIter.next();
                    if (c.isEnabled() && c.isVisible()) {
                        c.noMouseUpdates();
                    }
                }
            }
        } else if (this.isDragging) {
            if (!isPressed) {
                this.isDragging = false;
            }
        } else {
            this.noMouseUpdates();
        }

        this.wasMousePressed = isPressed;
    }

    public void bringToFront() {
        ArrayList<Window> copy = new ArrayList<Window>(Flux.INSTANCE.getClickGUI().windows);
        copy.remove(this);
        copy.add(this);
        Flux.INSTANCE.getClickGUI().windows = copy;
    }

    public void handleWheelUpdates(int mouseX, int mouseY, boolean b) {
        if (this.mouseOver(mouseX, mouseY)) {
            int wheelEvent = Mouse.getEventDWheel();
            if (wheelEvent != 0) {
                if (wheelEvent > 0) {
                    wheelEvent = -1;
                } else {
                    wheelEvent = 1;
                }

                this.toScrollY += wheelEvent * ClickGUI.defaultHeight;
                if (this.toScrollY > this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight)) {
                    this.toScrollY = this.componentsHeight - (ClickGUI.maxWindowHeight - ClickGUI.defaultHeight);
                }

                if (this.toScrollY < 0) {
                    this.toScrollY = 0;
                }
            }

            this.children.stream().filter(c -> c instanceof PresetList).map(c -> (PresetList) c).forEach(c -> c.mouseWheelUpdate(mouseX, mouseY, Mouse.getEventDWheel()));
        }
    }

    public void noWheelUpdates() {
    }

    public boolean contains(int mouseX, int mouseY) {
        return MathUtils.contains((float) mouseX, (float) mouseY, (float) this.x, (float) this.y, (float) (this.x + this.width), (float) (this.y + this.height));
    }

    public boolean mouseOver(int mouseX, int mouseY) {
        Window latest = null;
        for (Window window : Flux.INSTANCE.getClickGUI().windows) {
            if (window.isEnabled && window.contains(mouseX, mouseY)) {
                latest = window;
            }
        }

        return latest != null && latest.id.equals(this.id);
    }

    protected void keepInBounds() {
        this.x = Math.max(this.x, 0);
        this.y = Math.max(this.y, 0);
        this.x = Math.min(this.x, GuiRenderUtils.getDisplayWidth() - ClickGUI.defaultWidth);
        this.y = Math.min(this.y, GuiRenderUtils.getDisplayHeight() - ClickGUI.defaultHeight);
    }

    public void noMouseUpdates() {
        this.openHover = false;
        this.isDragging = false;

        this.children.stream().filter(c -> c.isEnabled() && c.isVisible()).forEach(Component::noMouseUpdates);

        //this.isScrolling = Mouse.isButtonDown(0);
    }

    public boolean overScrollbar(int mouseX, int mouseY) {
        return this.scrollbarEnabled && this.mouseOver(mouseX, mouseY) && MathUtils.contains((float) mouseX, (float) mouseY, (float) (this.x + this.width - ClickGUI.scrollbarWidth), (float) (this.y + ClickGUI.defaultHeight), (float) (this.x + this.width), (float) (this.y + this.height));
    }
}
