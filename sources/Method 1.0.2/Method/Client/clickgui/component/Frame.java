package Method.Client.clickgui.component;

import Method.Client.Main;
import Method.Client.clickgui.ClickGui;
import Method.Client.clickgui.component.components.Button;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.misc.GuiModule;
import Method.Client.module.misc.ModSettings;
import Method.Client.utils.font.CFont;
import Method.Client.utils.font.CFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;


public class Frame {

    public ArrayList<Component> components;
    public Category category;
    private boolean open;
    private int width;
    private int y;
    private int x;
    private int bottomY;
    private int scrollpos;
    private int CollapseRate;
    private final int barHeight;
    private boolean isDragging;
    private boolean isDraggingBot;
    public int dragX;
    public int dragScrollstop;
    public int dragY;
    protected Minecraft mc = Minecraft.getMinecraft();
    int scroll;
    boolean wasopen;
    public static CFontRenderer FrameFont;

    public Frame(Category cat) {
        this.components = new ArrayList<>();
        this.category = cat;
        this.width = 88;
        this.x = 5;
        this.y = 20;
        this.barHeight = 12;
        this.CollapseRate = 0;
        this.dragX = 0;
        this.open = false;
        this.isDragging = false;
        this.isDraggingBot = false;
        this.wasopen = false;

        int tY = this.barHeight;
        for (Module mod : ModuleManager.getModulesInCategory(category)) {
            Button modButton = new Button(mod, this, tY);
            this.components.add(modButton);
            tY += this.barHeight;
        }
        this.bottomY = tY;
    }

    public static void updateFont() {
        if (GuiModule.Frame.getValString().equalsIgnoreCase("Arial"))
            FrameFont = CFont.afontRenderer26;
        if (GuiModule.Frame.getValString().equalsIgnoreCase("Times"))
            FrameFont = CFont.tfontRenderer26;
        if (GuiModule.Frame.getValString().equalsIgnoreCase("Impact"))
            FrameFont = CFont.ifontRenderer26;
    }

    public void refresh() {
        int off = this.barHeight;
        for (Component comp : this.components) {
            comp.setOff(off);
            off += comp.getHeight();
        }
    }

    public void updateRefresh() {
        int tY = this.barHeight;
        this.components.clear();
        for (Module mod : ModuleManager.getModulesInCategory(category)) {
            Button modButton = new Button(mod, this, tY);
            this.components.add(modButton);
            tY += this.barHeight;
        }
        this.bottomY = tY;
    }

    public void renderFrame() {
        if ((this.category.equals(Category.ONSCREEN) && mc.currentScreen instanceof ClickGui)) {
            this.setX((int) ((mc.displayWidth / 7.8)));
            this.setY(2);
        }
        if ((category.equals(Category.HISTORY))) {
            this.setX((int) ((mc.displayWidth / 3)));
            this.setY(2);
        }
        glEnable(GL_BLEND);
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, GuiModule.Framecolor.getcolor());
        if (this.open && !this.category.equals(Category.PROFILES) && this.CollapseRate < 5)
            Gui.drawRect(this.x, this.bottomY + this.scrollpos + this.y, this.x + this.width, this.bottomY + this.barHeight + this.scrollpos + this.y, GuiModule.Framecolor.getcolor());
        GlStateManager.pushMatrix();
        fontSelect(this.category.name(), this.x + 3, (float) (this.y + 2.5f - 1.5), -1);
        if (!this.category.equals(Category.ONSCREEN) && !this.category.equals(Category.HISTORY))
            fontSelect(this.open ? "-" : "+", this.x + this.width - 9, (float) ((this.y + 2.5f) - 1.5), -1);
        GlStateManager.popMatrix();
        mc.getTextureManager().bindTexture(new ResourceLocation(Main.MODID, this.getName().toLowerCase() + ".png"));
        Gui.drawModalRectWithCustomSizedTexture(this.x + this.width - this.barHeight - 6, this.y + 1, 0, 0, this.barHeight, this.barHeight, this.barHeight, this.barHeight);
        if ((this.open || (this.wasopen && GuiModule.Animations.getValBoolean())) && !this.components.isEmpty()) {
            if (!this.open) {
                if (this.CollapseRate + (this.barHeight * 2) < (this.bottomY + this.scrollpos - this.barHeight) * 2)
                    this.CollapseRate += ModSettings.GuiSpeed.getValDouble();
                else {
                    this.wasopen = false;
                    return;
                }
            } else // Contained we are open
                this.CollapseRate = this.CollapseRate > 0 ? (int) (this.CollapseRate - ModSettings.GuiSpeed.getValDouble()) : 0;
            if (this.category.equals(Category.PROFILES)) // Proflies is so small we just dont want a scroll bar
                this.components.forEach(Component::renderComponent);
            else {
                glEnable(GL_SCISSOR_TEST);
                /*
                ScaledResolution res = new ScaledResolution(mc);
                double scaleW = mc.displayWidth / res.getScaledWidth_double();
                double scaleH = mc.displayHeight / res.getScaledHeight_double();

                 */
                // (mc.displayHeight - ((this.Bottomy + this.y + this.scrollpos) * 2)) + this.CollapseRate
                GL11.glScissor((int) (this.x * 2),
                        (int) (mc.displayHeight - ((this.bottomY + this.y + this.scrollpos) * 2)) + this.CollapseRate,
                        (int) (this.width * 2),
                        (int) ((this.bottomY + this.scrollpos - this.barHeight) * 2) - this.CollapseRate);
                this.components.forEach(Component::renderComponent);
                glDisable(GL_SCISSOR_TEST);
            }
        }
    }

    public void handleScrollinput() {
        if (this.category.equals(Category.HISTORY) || this.category.equals(Category.ONSCREEN) && mc.currentScreen instanceof ClickGui)
            return;
        int off = this.barHeight;
        for (Component component : this.components) {
            component.setOff(off - (this.barHeight * this.scroll));
            off += component.getHeight();
        }
        // WE can scroll if all compenent height plus bar > bottom +scrollpos
        boolean Canscoll = off - ((this.barHeight) * this.scroll) >= this.bottomY + this.scrollpos;
        int wheel = Mouse.getDWheel();
        if (wheel < 0 && Canscoll)
            this.scroll++;
        else if (wheel > 0)
            this.scroll--;
        if (this.scroll < 0)
            this.scroll = 0;

        if ((this.scrollpos < 0 && this.scrollpos > -12) || (this.scrollpos > 0 && this.scrollpos < 12))
            this.scrollpos = 0;

        if (this.scrollpos < -this.bottomY + this.barHeight)
            this.scrollpos = -this.bottomY + this.barHeight;

        if (this.scrollpos + this.bottomY > off + this.barHeight - 1)
            this.setScrollpos(this.scrollpos - this.barHeight);

        if (!((off - (this.barHeight * this.scroll)) > this.bottomY + this.scrollpos - this.barHeight) && this.scroll > 0)
            this.scroll--;

    }

    public void fontSelect(String name, float x, float y, int color) {
        if (GuiModule.Frame.getValString().equalsIgnoreCase("MC"))
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(name, x, y, color);
        else
            FrameFont.drawStringWithShadow(name, x, y, color);
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (this.isWithinBounds(mouseX, mouseY)) {
            this.handleScrollinput();
            this.getComponents().forEach(component -> component.updateComponent(mouseX, mouseY));
        } else
            this.getComponents().forEach(component -> component.hovered = false);

        this.getComponents().forEach(Component::runAnimation);

        if (this.isDragging && (!this.category.equals(Category.ONSCREEN) && mc.currentScreen instanceof ClickGui) && !this.category.equals(Category.HISTORY)) {
            // This is checking for bounds of the screen at either display width or zero
            this.setX(Math.max(-this.width / 2, Math.min(mouseX - dragX, (mc.displayWidth / 2) - (this.width / 2))));
            // This is checking for bounds of the screen at either Height width or zero
            this.setY(Math.max(0, Math.min(mouseY - dragY, mc.displayHeight / 2)));
        }
        int off = this.barHeight;

        for (Component component : this.components) {
            off += component.getHeight();
        }
        if (this.isDraggingBot) {
            if (!(mouseY + 6 > off + this.y + this.barHeight))
                this.setScrollpos(mouseY - this.dragScrollstop);
            handleScrollinput();
        }
    }

    public boolean isWithinHeader(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }

    public boolean isWithinFooter(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.bottomY + this.scrollpos + this.y && y <= this.bottomY + this.scrollpos + this.barHeight + this.y;
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y + this.barHeight && y <= this.y + this.bottomY + ((this.category != Category.PROFILES) ? this.scrollpos : 1000);
    }

    public int getX() {
        return x;
    }

    public String getName() {
        return this.category.name();
    }

    public Category getCategory() {
        return this.category;
    }

    public int getY() {
        return y;
    }

    public int getScrollpos() {
        return scrollpos;
    }

    public int getWidth() {
        return width;
    }

    public int getBarHeight() {
        return barHeight;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setScrollpos(int NewY) {
        this.scrollpos = NewY;
    }

    public void setDrag(boolean drag) {
        this.isDragging = drag;
    }

    public void setDragBot(boolean drag) {
        this.isDraggingBot = drag;
    }

    public boolean isOpen() {
        return open;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void setOpen(boolean open) {
        this.open = open;
        if (open)
            this.wasopen = true;
    }
}
