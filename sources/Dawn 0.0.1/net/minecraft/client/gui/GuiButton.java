package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import wtf.dawn.utils.animations.simple.SimpleAnimation;
import wtf.dawn.utils.font.FontUtil;

import java.awt.*;

public class GuiButton extends Gui
{

    int opacity;
    private SimpleAnimation lineHoverAnimation = new SimpleAnimation(0.0F);


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
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);

            this.mouseDragged(mc, mouseX, mouseY);
            int j = new Color(20, 20, 20, opacity).getRGB();

            if(!this.enabled) {
                j = new Color(20, 15, 15).getRGB();
            }
            if(hovered) {
                opacity += 25;
                if(opacity > 120) opacity = 120;
            } else {
                opacity -= 25;
                if(opacity < 50) opacity = 50;
            }


            Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, j);

            if(this.hovered && this.enabled) {
                if (!(lineHoverAnimation.getValue() > width / 2 - 2)) {
                    lineHoverAnimation.setAnimation(width / 2 - 2, 10);

                    this.drawHorizontalLine(this.xPosition + width / 2 , (int) ((xPosition + width/2) - lineHoverAnimation.getValue()), this.yPosition + height - 1, -1);
                    this.drawHorizontalLine(this.xPosition + width / 2 , (int) ((xPosition + width/2) + lineHoverAnimation.getValue()), this.yPosition + height - 1, -1);


                }
                else if(lineHoverAnimation.getValue() >= width / 2) {
                    this.drawHorizontalLine(this.xPosition + width / 2 , (int) ((xPosition + width/2) - lineHoverAnimation.getValue()), this.yPosition + height - 1, -1);
                    this.drawHorizontalLine(this.xPosition + width / 2 , (int) ((xPosition + width/2) + lineHoverAnimation.getValue()), this.yPosition + height - 1, -1);

                }

            }
            else if(!hovered){
                lineHoverAnimation.setAnimation(0, 4);
                if(!(lineHoverAnimation.getValue() <= 0 || lineHoverAnimation.getValue() <= 2)) {
                    this.drawHorizontalLine(this.xPosition + width / 2 , (int) ((xPosition + width/2) - lineHoverAnimation.getValue() - 1), this.yPosition + height - 1, -1);
                    this.drawHorizontalLine(this.xPosition + width / 2 , (int) ((xPosition + width/2) + lineHoverAnimation.getValue() + 1), this.yPosition + height - 1, -1);
                }

            }
            FontUtil.normal.drawCenteredString(this.displayString, this.xPosition + (float) this.width / 2, this.yPosition + (this.height - 8) / 2, Color.WHITE.getRGB());
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
