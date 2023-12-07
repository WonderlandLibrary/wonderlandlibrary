package net.minecraft.client.gui;

import java.awt.Color;

import org.darkstorm.minecraft.gui.util.RenderUtil;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui {
	protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

	/** Button width in pixels */
	protected int width;

	/** Button height in pixels */
	protected int height;

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
		this(buttonId, x, y, widthIn, heightIn, buttonText, false);
	}
	
	private boolean jigsawButton;
	
	public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean jigsawButton) {
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
		this.jigsawButton = jigsawButton;
	}

	/**
	 * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over
	 * this button and 2 if it IS hovering over this button.
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

	/**
	 * Draws this button to the screen.
	 */
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
//		drawJigsawButton(mc, mouseX, mouseY);
		if(this.jigsawButton) {
			this.drawJigsawButton(mc, mouseX, mouseY);
			return;
		}
		if (this.visible) {
			FontRenderer fontrenderer = mc.fontRendererObj;
			mc.getTextureManager().bindTexture(buttonTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
					&& mouseY < this.yPosition + this.height;
			int i = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2,
					46 + i * 20, this.width / 2, this.height);
			this.mouseDragged(mc, mouseX, mouseY);
			int j = 14737632;

			if (!this.enabled) {
				j = 10526880;
			} else if (this.hovered) {
				j = 16777120;
			}
			if(Jigsaw.ghostMode) {
				this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2,
						this.yPosition + (this.height - 8) / 2, j);
			}
			else {
				Fonts.font18.drawCenteredString(this.displayString, this.xPosition + this.width / 2,
						this.yPosition + (this.height - 8) / 2, j);
			}
		}
	}

	private double hoverTime = 0;
	boolean goingUp = false;

	public void drawJigsawButton(Minecraft mc, int mouseX, int mouseY) {
		if (!this.visible) {
			return;
		}
		FontRenderer fontrenderer = mc.fontRendererObj;
		mc.getTextureManager().bindTexture(buttonTextures);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
				&& mouseY < this.yPosition + this.height;
		int i = this.getHoverState(this.hovered);
		
		if (hovered) {
			if(!goingUp) {
				goingUp = true;
			}
			hoverTime++;
		}
		if(!hovered && goingUp) {
			hoverTime++;
			if(hoverTime > 7) {
				goingUp = false;
			}
		}
		if (!hovered && !goingUp) {
			hoverTime--;
		}
		if (hoverTime > 8.0) {
			hoverTime = 8.0;
		}
		if (hoverTime < 0) {
			hoverTime = 0;
		}
		
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.blendFunc(770, 771);
		this.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x2c000000);
		this.drawHorizontalLine(xPosition + 1, xPosition + width, yPosition + height, 0xff000000);
		this.drawVerticalLine(xPosition + width, yPosition, yPosition + height, 0xff000000);
		
		int animationWidth = (int) Math.abs(hoverTime - 9.0);
		
		int middle = xPosition + width / 2;
		
		if(animationWidth < 9.0) {
			this.drawHorizontalLine((middle - width / (2 * animationWidth)), (middle + width / (2 * animationWidth)) - 1, (yPosition + height) - 2, 0xfff0f0f0);
			this.drawHorizontalLine((middle - width / (2 * animationWidth)), (middle + width / (2 * animationWidth)) - 1, (yPosition + height) - 1, 0xfff0f0f0);
		}
		
		this.mouseDragged(mc, mouseX, mouseY);
		int j = 14737632;

		if (!this.enabled) {
			j = 10526880;
		} else if (this.hovered) {
//			j = 16777120;
		}
		
		Fonts.font18.drawCenteredString(this.displayString, this.xPosition + this.width / 2,
				this.yPosition + (this.height - 8) / 2, j);
//		this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2,
//				this.yPosition + (this.height - 8) / 2, j);
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
