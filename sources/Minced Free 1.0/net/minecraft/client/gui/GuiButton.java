package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import ru.smertnix.celestial.ui.mainmenu.MainMenu;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;

import java.awt.*;

public class GuiButton extends Gui {
    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

    /**
     * Button width in pixels
     */
    protected int width;

    /**
     * Button height in pixels
     */
    public int height;

    /**
     * The x position of this control.
     */
    public int xPosition;
    private int opacity = 40;

    /**
     * The y position of this control.
     */
    public int yPosition;

    /**
     * The string displayed on this control.
     */
    public String displayString;
    public int id;

    /**
     * True if this control is enabled, false to disable.
     */
    public boolean enabled;

    /**
     * Hides the button completely if false.
     */
    public boolean visible;
    protected boolean hovered;
    private int fade = 20;
    private int fadeOutline = 20;

    public static ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

    public GuiButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver) {
        int i = 1;

        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }

        return i;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float mouseButton) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            if (hovered) {
                if (this.opacity < 40) {
                    this.opacity += 1;
                }
            } else if (this.opacity > 22) {
                this.opacity -= 1;
            }

            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height; // Flag, tells if your mouse is hovering the button
            Color color = new Color(30, 30, 30, 240);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);

            
            RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width, height,0, new Color(0,0,0,100));
            mc.mntsb_16.drawCenteredStringWithShadow(this.displayString.equals("2") ? (GuiChat.hide ? "Hide information: true" : "Hide information: false") : this.displayString, this.xPosition + this.width / 2F, (this.yPosition) + (this.height / 2 - 2), !this.enabled ? Color.GRAY.getRGB() : (hovered ? new Color(255,255,150).getRGB() : -1));             
      
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY) {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}