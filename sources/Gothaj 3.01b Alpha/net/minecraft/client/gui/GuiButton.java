package net.minecraft.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.render.BlurUtils;
import xyz.cucumber.base.utils.render.Fonts;
import xyz.cucumber.base.utils.render.StencilUtils;

public class GuiButton extends Gui
{
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    protected int width;
    protected int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean visible;
    protected boolean hovered;
    private int lastX, lastY;
    
    private long animation;

    public GuiButton(int buttonId, int x, int y, String buttonText)
    {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
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

    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            //mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX > this.xPosition && mouseY > this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            RenderUtils.drawOutlinedRect(this.xPosition, this.yPosition, this.xPosition+this.width, this.yPosition+this.height,0xff303030,1);
            StencilUtils.initStencil();
            GL11.glEnable(GL11.GL_STENCIL_TEST);
            StencilUtils.bindWriteStencilBuffer();
            RenderUtils.drawOutlinedRect(this.xPosition, this.yPosition, this.xPosition+this.width, this.yPosition+this.height,0xff222222,1);
    		
    		StencilUtils.bindReadStencilBuffer(1);
    		
    		GL11.glPushMatrix();
    		
    		RenderUtils.start2D();
    		
    		GL11.glShadeModel(GL11.GL_SMOOTH);
    		
    		RenderUtils.color(0xffffffff);
    		
    		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
    		GL11.glVertex2d(mouseX, mouseY);
    		
    		for(double z = 0; z <=360; z+=5) {
    			RenderUtils.color(0x00ffffff);
    			GL11.glVertex2d(mouseX+Math.sin(z * Math.PI /180)* 30, mouseY-Math.cos(z * Math.PI /180)* 30);
    		}
    		GL11.glEnd();
    		RenderUtils.stop2D();
    		GlStateManager.resetColor();
    		
    		GL11.glPopMatrix();
    		
    		StencilUtils.uninitStencilBuffer();
            
            
            double t = 4 - Math.pow(Math.sqrt((animation - System.currentTimeMillis())/100), 2);

            
            RenderUtils.drawRect(this.xPosition, this.yPosition, this.xPosition+this.width, this.yPosition+this.height, 0x45222222);
            
            Fonts.getFont("rb-b").drawString(displayString, this.xPosition+ this.width/2-Fonts.getFont("rb-b").getWidth(displayString)/2, this.yPosition+ this.height/2-Fonts.getFont("rb-b").getHeight(displayString)/2-1, -1);
        }
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
    }

    public void mouseReleased(int mouseX, int mouseY)
    {
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    public boolean isMouseOver()
    {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY)
    {
    }

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
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
