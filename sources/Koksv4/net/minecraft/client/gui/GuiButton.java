package net.minecraft.client.gui;

import koks.api.Methods;
import koks.api.font.Fonts;
import koks.api.utils.Animation;
import koks.api.utils.RenderUtil;
import koks.api.utils.Resolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.optifine.gui.GuiOptionSliderOF;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiButton extends Gui implements Methods {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    /**
     * Button width in pixels
     */
    protected int width;

    /**
     * Button height in pixels
     */
    protected int height;

    /**
     * The x position of this control.
     */
    public int xPosition;

    /**
     * The y position of this control.
     */
    public int yPosition;

    public int moveY;

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
    protected boolean hovered, rainbow;

    public GuiButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }


    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        animation.setX(x);
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

    final Animation animation = new Animation();

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition + moveY && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height + moveY;
            int i = this.getHoverState(this.hovered);
            int j = 14737632;

            if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = 16777120;
            }
            if (Minecraft.getMinecraft().developerMode) {
                mc.getTextureManager().bindTexture(buttonTextures);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(770, 771);
                this.drawTexturedModalRect(this.xPosition, this.yPosition + moveY, 0, 46 + i * 20, this.width / 2, this.height);
                this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition + moveY, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
                this.mouseDragged(mc, mouseX, mouseY);
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2 + moveY, j);
            } else {
                GlStateManager.resetColor();
                Color start = new Color(35, 35, 35, 200);
                Color end = new Color(39, 39, 39, 200);
                if(hovered) {
                    start = new Color(39,39,39, 200);
                    end = new Color(43, 43, 43, 200);
                }

                j = new Color(203, 203, 203, 200).getRGB();
                if (!this.enabled) {
                    j = 10526880;
                } else if (this.hovered) {
                    j = new Color(255, 255, 255, 200).getRGB();
                }

                drawGradientRect(xPosition, yPosition + moveY, xPosition + width, yPosition + moveY + height, start.getRGB(), end.getRGB());
                if (rainbow)
                    for (int w = 0; w < width; w += 1) {
                        drawRect(xPosition + w, yPosition + moveY, xPosition + w + 1, yPosition + moveY + 2, getRainbow((xPosition + w) * 50, 50000, 0.7F, 1F).getRGB());
                    }
                final Resolution resolution = Resolution.getResolution();
                final int calcSpeed = 100 * (resolution.getWidth() * 100 / Toolkit.getDefaultToolkit().getScreenSize().width) / 100 + (Mouse.isButtonDown(0) && isMouseOver() ? (Math.abs(Mouse.getDX())) : 0);
                animation.setSpeed(MathHelper.clamp_float((float) Math.abs(Math.cos(Math.toRadians(Math.abs(Math.abs(animation.getX()) - Math.abs(animation.getGoalX())))) * calcSpeed) * 1.2F, 56F, 100F));
                if(this instanceof GuiOptionSlider) {
                    final GuiOptionSlider optionSlider = (GuiOptionSlider) this;
                    animation.setGoalX(this.xPosition + (int)(optionSlider.sliderValue * (float)(this.width)));
                    drawRect(this.xPosition, this.yPosition + (rainbow ? 2 : 0), (int) animation.getAnimationX(), this.yPosition + height, Integer.MIN_VALUE);
                } else if(this instanceof GuiScreenOptionsSounds.Button) {
                    final GuiScreenOptionsSounds.Button button = (GuiScreenOptionsSounds.Button) this;
                    animation.setGoalX(this.xPosition + (int)(button.field_146156_o * (float)(this.width)));
                    drawRect(this.xPosition, this.yPosition + (rainbow ? 2 : 0), (int) animation.getAnimationX(), this.yPosition + height, Integer.MIN_VALUE);
                }
                if(animation.isFinished())
                    animation.setX(animation.getGoalX());
                this.mouseDragged(mc, mouseX, mouseY, false);
                Fonts.arial18.drawString(displayString, xPosition + width / 2F - Fonts.arial18.getStringWidth(displayString) / 2, yPosition + (height - 8) / 2F + moveY, new Color(j), true);
            }

        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY, boolean render) {

    }

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
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition + moveY && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height + moveY;
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
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
