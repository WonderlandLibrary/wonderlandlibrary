package net.minecraft.client.gui;

import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.utility.AnimationTimer;
import today.flux.utility.DelayTimer;

import java.awt.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui {
	protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

	/** Button width in pixels */
	public int width;

	/** Button height in pixels */
	public int height;

	/** The x position of this control. */
	public int xPosition;

	/** The y position of this control. */
	public int yPosition;

	/** The string displayed on this control. */
	public String displayString;
	public int id;

	/** True if this control is enabled, false to disable. */
	public boolean enabled;

	/** Hides the button completely if false. */
	public boolean visible;
	protected boolean hovered;

	public GuiButton(int buttonId, int x, int y, String buttonText) {
		this(buttonId, x, y, 200, 20, buttonText);
	}

	public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
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
	 * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this
	 * button and 2 if it IS hovering over this button.
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

	private DelayTimer timer = new DelayTimer();
	private AnimationTimer anim = new AnimationTimer(5);

	/**
	 * Draws this button to the screen.
	 */
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		// Client
		if (this.visible) {
			int borderColor = new Color(255, 255, 255).getRGB();
			int borderColorDIsabled = new Color(177, 177, 177).getRGB();

			// wolfram style!
			mc.getTextureManager().bindTexture(buttonTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);

			this.mouseDragged(mc, mouseX, mouseY);

			int alpha = (int) (this.anim.getValue() * 100.0D);
			if (alpha < 1 || !this.enabled)
				alpha = 1;
			
			GuiRenderUtils.drawBorderedRect(this.xPosition + 0.4f, this.yPosition + 0.4f, this.width, this.height, 1.0F,
					new Color(255, 255, 255, alpha).getRGB(), 0x000000);

			GuiRenderUtils.drawRect(xPosition, yPosition, width, height, 0x88000000);

			GuiRenderUtils.drawBorderedRect(this.xPosition, this.yPosition, this.width, this.height, 1.0F,
					new Color(255, 255, 255, alpha).getRGB(), this.enabled ? borderColor : borderColorDIsabled);
			
			FontManager.wqy18.drawCenteredStringWithShadow(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height  - 8) / 2f - 1.5f, !this.enabled ? GuiRenderUtils.darker(0xFFFFFFFF, 50) : this.hovered && this.enabled ? 0xff4286f5 : 0xFFFFFFFF);

			if(this.enabled && this.hovered) {
				GuiRenderUtils.drawBorderedRect(this.xPosition, this.yPosition, this.width, this.height, 1.0F,
						new Color(255, 255, 255, alpha).getRGB(), 0xff4286f5);
			}
			}
	}

	/**
	 * Fired when the mouse button is dragged. Equivalent of
	 * MouseListener.mouseDragged(MouseEvent e).
	 */
	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
	}

	/**
	 * Fired when the mouse button is released. Equivalent of
	 * MouseListener.mouseReleased(MouseEvent e).
	 */
	public void mouseReleased(int mouseX, int mouseY) {
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of
	 * MouseListener.mousePressed(MouseEvent e).
	 */
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition
				&& mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
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
