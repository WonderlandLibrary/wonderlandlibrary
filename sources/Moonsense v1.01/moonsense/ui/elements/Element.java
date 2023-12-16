// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements;

import java.io.IOException;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import moonsense.ui.elements.module.ElementModule;
import net.minecraft.client.Minecraft;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.function.Consumer;

public abstract class Element
{
    public int x;
    public int y;
    public int width;
    public int height;
    public final boolean shouldScissor;
    public Consumer<Element> consumer;
    public final AbstractGuiScreen parent;
    private int xOffset;
    private int yOffset;
    public boolean enabled;
    public boolean hovered;
    protected int mouseX;
    protected int mouseY;
    private int touchValue;
    private int eventButton;
    private long lastMouseEvent;
    protected final Minecraft mc;
    
    public Element(final int x, final int y, final int width, final int height, final Consumer<Element> consumer) {
        this(x, y, width, height, false, consumer, null);
    }
    
    public Element(final int x, final int y, final int width, final int height, final boolean shouldScissor) {
        this(x, y, width, height, shouldScissor, null, null);
    }
    
    public Element(final int x, final int y, final int width, final int height, final boolean shouldScissor, final Consumer<Element> consumer) {
        this(x, y, width, height, shouldScissor, consumer, null);
    }
    
    public Element(final int x, final int y, final int width, final int height, final boolean shouldScissor, final Consumer<Element> consumer, final AbstractGuiScreen parent) {
        this.mc = Minecraft.getMinecraft();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.shouldScissor = shouldScissor;
        this.consumer = consumer;
        this.parent = parent;
        this.enabled = true;
    }
    
    public void init() {
    }
    
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        if (!(this instanceof ElementModule)) {
            this.hovered(mouseX, mouseY);
        }
        this.renderBackground(partialTicks);
        this.renderElement(partialTicks);
        if (this.enabled) {
            this.mouseDragged(mouseX, mouseY);
        }
    }
    
    public void renderElement(final float partialTicks) {
    }
    
    public void renderBackground(final float partialTicks) {
        GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 2.0f, new Color(0, 0, 0, 75).getRGB());
    }
    
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.enabled && this.hovered && this.consumer != null) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            this.consumer.accept(this);
        }
        return this.enabled && this.hovered;
    }
    
    public void mouseDragged(final int mouseX, final int mouseY) {
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public void handleMouseInput() throws IOException {
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    public boolean hovered(final int mouseX, final int mouseY) {
        return this.hovered = (mouseX >= this.getX() && mouseX <= this.getX() + this.width && mouseY >= this.getY() && mouseY <= this.getY() + this.height);
    }
    
    public boolean hovered(final int x, final int y, final int width, final int height, final int mouseX, final int mouseY) {
        return this.hovered = (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height);
    }
    
    public void update() {
    }
    
    public int getX() {
        return this.x + this.xOffset;
    }
    
    public int getY() {
        return this.y + this.yOffset;
    }
    
    public void addOffsetToX(final int add) {
        this.xOffset += add;
    }
    
    public void addOffsetToY(final int add) {
        this.yOffset += add;
    }
    
    public Element setXOffset(final int xOffset) {
        this.xOffset = xOffset;
        return this;
    }
    
    public void setYOffset(final int yOffset) {
        this.yOffset = yOffset;
    }
    
    public int getXOffset() {
        return this.xOffset;
    }
    
    public int getYOffset() {
        return this.yOffset;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
}
