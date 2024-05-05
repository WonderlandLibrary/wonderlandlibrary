package net.minecraft.client.gui;

import java.awt.Color;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import appu26j.Apple;
import appu26j.interfaces.MinecraftInterface;
import appu26j.mods.visuals.FPSSpoofer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui implements MinecraftInterface
{
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
    private float index = 0;
    private ResourceLocation texture = null;
    private boolean temp = true;

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

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean enabled)
    {
        this.width = 200;
        this.height = 20;
        this.enabled = enabled;
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

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            
            if (this.hovered)
            {
                if (this.index < 1)
                {
                    float fps = this.mc.getDebugFPS();
                    FPSSpoofer fpsSpoofer = (FPSSpoofer) Apple.CLIENT.getModsManager().getMod("FPS Spoofer");
                    
                    if (fpsSpoofer.isEnabled())
                    {
                        fps /= fpsSpoofer.getSetting("Multipler").getSliderValue();
                    }
                    
                    float delta = 1F / fps;
                    this.index += 10 * delta;
                    this.index = this.index > 1 ? 1 : this.index;
                }
            }
            
            else
            {
                if (this.index > 0)
                {
                    float fps = this.mc.getDebugFPS();
                    FPSSpoofer fpsSpoofer = (FPSSpoofer) Apple.CLIENT.getModsManager().getMod("FPS Spoofer");
                    
                    if (fpsSpoofer.isEnabled())
                    {
                        fps /= fpsSpoofer.getSetting("Multipler").getSliderValue();
                    }
                    
                    float delta = 1F / fps;
                    this.index -= 10 * delta;
                    this.index = this.index < 0 ? 0 : this.index;
                }
            }
            
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            int temp = i == 2 ? 1 : i;
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + temp * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + temp * 20, this.width / 2, this.height);
            
            if (this.index != 0 && this.enabled && i != 0)
            {
                GlStateManager.color(1, 1, 1, this.index);
                this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + 40, this.width / 2, this.height);
                this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + 40, this.width / 2, this.height);
            }
            
            this.mouseDragged(mc, mouseX, mouseY);
            boolean aBoolean = this.mc.getResourcePackRepository().getRepositoryEntries().size() != 0 && Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntries().get(0).getResourcePackName().equals("1.14 Textures") && i == 2;
            int j = new Color(225, 225, 225).getRGB();

            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered && !aBoolean)
            {
                j = new Color(225 + (int) (30 * this.index), 225 + (int) (30 * this.index), 225 - (int) (65 * this.index)).getRGB();
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
            
            if (this.id == 2626)
            {
                if (this.texture == null && this.temp)
                {
                    Minecraft.getMinecraft().getSkinManager().loadProfileTextures(Minecraft.getMinecraft().getSession().getProfile(), new SkinManager.SkinAvailableCallback()
                    {
                        public void skinAvailable(Type p_180521_1_, ResourceLocation location, MinecraftProfileTexture profileTexture)
                        {
                            if (p_180521_1_.equals(Type.SKIN))
                            {
                                GuiButton.this.texture = location;
                            }
                        }
                    }, true);
                    
                    this.temp = false;
                }
                
                if (this.texture != null)
                {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(this.texture);
                    this.drawModalRectWithCustomSizedTexture(this.xPosition + 50, this.yPosition + 3, 14, 14, 14, 14, 112, 112);
                }
                
                else
                {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/entity/steve.png"));
                    this.drawModalRectWithCustomSizedTexture(this.xPosition + 50, this.yPosition + 3, 14, 14, 14, 14, 112, 112);
                }
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
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
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
