package net.minecraft.client.gui;

import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.ui.ingame.IngameGUI;
import dev.rise.util.render.ColorUtil;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiButton extends Gui {
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

    boolean bg = true;

    public GuiButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(final int buttonId, final int x, final int y, final String buttonText, boolean backGround) {
        this(buttonId, x, y, 200, 20, buttonText);
        bg = backGround;
    }

    public GuiButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
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
    protected int getHoverState(final boolean mouseOver) {
        int i = 1;

        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }

        return i;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (Rise.INSTANCE.destructed) {

            if (this.visible) {
                FontRenderer fontrenderer = mc.fontRendererObj;
                mc.getTextureManager().bindTexture(buttonTextures);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int i = this.getHoverState(this.hovered);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(770, 771);
                this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
                this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
                this.mouseDragged(mc, mouseX, mouseY);
                int j = 14737632;

                if (!this.enabled) {
                    j = 10526880;
                } else if (this.hovered) {
                    j = 16777120;
                }

                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
            }
        } else {

            if (mc.theWorld == null) {
                if (this.visible) {
                    mc.getTextureManager().bindTexture(buttonTextures);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    GlStateManager.blendFunc(770, 771);

                    if (bg)
                        RenderUtil.roundedRect(this.xPosition, this.yPosition, this.width, this.height, 8, new Color(0, 0, 0, 65));

                    if (hovered && bg)
                        RenderUtil.roundedRect(this.xPosition, this.yPosition, this.width, this.height, 8, new Color(0, 0, 0, 65));

                    this.mouseDragged(mc, mouseX, mouseY);

                    CustomFont.drawCenteredString(this.displayString, this.xPosition + this.width / 2.0F, this.yPosition + (this.height - 8) / 2.0F, Color.WHITE.hashCode());
                }
            } else if (this.visible) {
                mc.getTextureManager().bindTexture(buttonTextures);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(770, 771);

                if (bg)
                    RenderUtil.roundedRectCustom(this.xPosition, this.yPosition, this.width, this.height, 4, new Color(0, 0, 0, 65), true, true, false, false);

                if (hovered && bg)
                    RenderUtil.roundedRectCustom(this.xPosition, this.yPosition, this.width, this.height, 4, new Color(0, 0, 0, 65), true, true, false, false);

                // Get the current theme of the client.
                final ModeSetting setting = ((ModeSetting) Rise.INSTANCE.getModuleManager().getSetting("Interface", "Theme"));

                // If the setting was null return to prevent crashes bc shit setting system.
                if (setting == null) return;

                // Get the theme and convert it to lower case.
                final String theme = setting.getMode().toLowerCase();

                // Get the color theme of the client and adjust based on it.
                final int color = ThemeUtil.getThemeColorInt(ThemeType.GENERAL);

                if (theme.contains("blend")) {
                    final Color color1 = new Color(78, 161, 253, 255);
                    final Color color2 = new Color(78, 253, 154, 255);

                    final Color cock = ColorUtil.mixColors(color1, color2, (Math.sin(IngameGUI.ticks + 0 * 0.4f) + 1) * 0.5f);
                    final Color cock2 = ColorUtil.mixColors(color1, color2, (Math.sin(IngameGUI.ticks + this.width * 0.4f) + 1) * 0.5f);

                    RenderUtil.gradientSideways(this.xPosition, this.yPosition + this.height, this.width, 1, cock, cock2);

                    for (int i = 0; i <= 7; ++i) {
                        RenderUtil.gradientSideways(this.xPosition - i, this.yPosition + this.height - i, this.width + i * 2, 1 + i * 2, new Color(cock.getRed(), cock.getGreen(), cock.getBlue(), 4), new Color(cock2.getRed(), cock2.getGreen(), cock2.getBlue(), 3));
                    }
                } else if (theme.contains("rainbow")) {
                    final Color cock = new Color(ColorUtil.getColor(1 * 0 * 1.4f, 0.5f, 1));
                    final Color cock2 = new Color(ColorUtil.getColor(1 * this.width * 1.4f, 0.5f, 1));

                    RenderUtil.gradientSideways(this.xPosition, this.yPosition + this.height, this.width, 1, cock, cock2);
                    for (int i = 0; i <= 7; ++i) {
                        RenderUtil.gradientSideways(this.xPosition - i, this.yPosition + this.height - i, this.width + i * 2, 1 + i * 2, new Color(cock.getRed(), cock.getGreen(), cock.getBlue(), 4), new Color(cock2.getRed(), cock2.getGreen(), cock2.getBlue(), 3));
                    }
                } else {
                    final Color c = new Color(color);
                    RenderUtil.rect(this.xPosition, this.yPosition + this.height, this.width, 1, c);
                    for (int i = 0; i <= 7; ++i) {
                        RenderUtil.rect(this.xPosition - i, this.yPosition + this.height - i, this.width + i * 2, 1 + i * 2, new Color(c.getRed(), c.getGreen(), c.getBlue(), 4));
                    }
                }

                this.mouseDragged(mc, mouseX, mouseY);

                CustomFont.drawCenteredString(this.displayString, this.xPosition + this.width / 2.0F, this.yPosition + (this.height - 8) / 2.0F, Color.WHITE.hashCode());
            }
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(final int mouseX, final int mouseY) {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
    }

    public void playPressSound(final SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }
}
