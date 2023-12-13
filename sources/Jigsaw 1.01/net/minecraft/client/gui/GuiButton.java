package net.minecraft.client.gui;

import java.awt.Color;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.font.Fonts;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui
{
	public boolean jigsawButton;
	public boolean jigsawFont = true;
	
    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

    /** Button width in pixels */
    protected int width;

    /** Button height in pixels */
    protected int height;

    /** The x position of this control. */
    public int x;

    /** The y position of this control. */
    public int y;

    /** The string displayed on this control. */
    public String displayString;
    public int id;

    /** True if this control is enabled, false to disable. */
    public boolean enabled;

    /** Hides the button completely if false. */
    public boolean visible;
    protected boolean hovered;

    public GuiButton(int buttonId, int x, int y, String buttonText)
    {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
        this(buttonId, x, y, widthIn, heightIn, buttonText, true);
    }
    
    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean jigsawButton)
    {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.x = x;
        this.y = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
        this.jigsawButton = jigsawButton;
    }
    
    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean jigsawButton, boolean jigsawFont)
    {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.x = x;
        this.y = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
        this.jigsawButton = jigsawButton;
        this.jigsawFont = jigsawFont;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        int i = 1;

        if (!this.enabled)
        {
            i = 0;
        }
        else if (mouseOver)
        {
            i = 2;
        }

        return i;
    }
    
    private double hoverTime = 1;

    public void drawButton(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = p_191745_1_.fontRenderer;
            p_191745_1_.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = p_191745_2_ >= this.x && p_191745_3_ >= this.y && p_191745_2_ < this.x + this.width && p_191745_3_ < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            
            if(Jigsaw.ghostMode || (Minecraft.getMinecraft().player == null && !this.jigsawButton)) {
            	this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
            	this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            }
            else {
            	double ticks = p_191745_4_;
            	
            	if (hovered) {
        			hoverTime+=0.4 * ticks;
        		}
            	else {
            		hoverTime-=0.4 * ticks;
            	}
        		if (hoverTime > 1.0) {
        			hoverTime = 1.0;
        		}
        		if (hoverTime < -1.0) {
        			hoverTime = -1.0;
        		}
            	
            	GuiUtils.drawBlurBuffer(this.x, this.y, this.width + this.x, this.height + this.y, true);
                
                GuiUtils.enableDefaults();
                
//                GuiUtils.renderShadowHorizontal(0.2, 6, this.y, this.x, this.x + this.width, true, true);
//                GuiUtils.renderShadowHorizontal(0.2, 6, this.y + this.height, this.x, this.x + this.width, false, true);
//                GuiUtils.renderShadowVertical(0.2, 6, this.x, this.y, this.y + this.height, false, true);
//                GuiUtils.renderShadowVertical(0.2, 6, this.x + this.width, this.y, this.y + this.height, true, true);
                
                drawRect(this.x, this.y, this.width + this.x, this.height + this.y, GuiUtils.toARGB(new Color(0f, 0f, 0f, 0.7f)));
                
                int animationWidth = (int) Math.round(width / 2 * 
                		(
                				Math.sin((hoverTime * 1.5))
                				+ 1) / 2
                );
        		
        		int middle = x + width / 2;
        		
        		if(enabled && animationWidth != 0) {
//        			drawRect(middle - animationWidth, y, middle + animationWidth, y + height, GuiUtils.toARGB(ClientSettings.getForeGroundGuiColor()));
        			this.drawHorizontalLine(middle - animationWidth, middle + animationWidth - 1, y + height - 1, GuiUtils.toARGB(ClientSettings.getForeGroundGuiColor(1f)));
        		}
        		
            }
            
            this.mouseDragged(p_191745_1_, p_191745_2_, p_191745_3_);
            int j = 14737632;

            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
//                j = 16777120;
            }
            
            GuiUtils.enableTextureDefaults();
            
            if(jigsawFont && !Jigsaw.ghostMode) {
            	this.drawCenteredString(jigsawButton ? Jigsaw.ghostMode ? fontrenderer : Fonts.font19 : fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
            }
            else {
            	this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
            }
            
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY)
    {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver()
    {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY)
    {
    }

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public int getButtonWidth()
    {
        return this.width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }
}
