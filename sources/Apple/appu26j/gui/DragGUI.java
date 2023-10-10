package appu26j.gui;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import appu26j.Apple;
import appu26j.mods.Mod;
import appu26j.mods.visuals.FPSSpoofer;
import appu26j.utils.BlurUtil;
import appu26j.utils.SoundUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import quexii.RoundedUtil;

public class DragGUI extends GuiScreen
{
	private boolean tempVariableForAnimation = false, closingGui = false, displayingClickGUI = false;
	private float dragX = 0, dragY = 0, index = 0;
	public ClickGUI clickGUI = new ClickGUI();
	private Mod draggingMod = null;
	
	public DragGUI displayClickGUIWhenExiting()
	{
		this.tempVariableForAnimation = true;
		return this;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (BlurUtil.enabled())
        {
            BlurUtil.blur(BlurUtil.intensity());
        }
        
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 75).getRGB(), this.tempVariableForAnimation || this.displayingClickGUI ? 75 : (int) (this.index * 75));
		
		if (this.closingGui)
		{
			if (this.index >= 0)
			{
			    float fps = this.mc.getDebugFPS();
                FPSSpoofer fpsSpoofer = (FPSSpoofer) Apple.CLIENT.getModsManager().getMod("FPS Spoofer");
                
                if (fpsSpoofer.isEnabled())
                {
                    fps /= fpsSpoofer.getSetting("Multipler").getSliderValue();
                }
                
                float delta = 1F / fps;
                this.index -= 7.5F * delta;
				
				if (this.index < 0)
				{
					this.index = 0;
				}
				
				if (this.index == 0)
				{
					if (this.displayingClickGUI)
					{
						this.mc.displayGuiScreen(this.clickGUI);
					}
					
					else
					{
			            this.mc.displayGuiScreen(null);

			            if (this.mc.currentScreen == null)
			            {
			                this.mc.setIngameFocus();
			            }
					}
		            
					this.closingGui = false;
				}
			}
		}
		
		else
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
				this.index += 7.5F * delta;
				
				if (this.index > 1)
				{
					this.index = 1;
				}
				
				if (this.index == 1)
				{
					this.displayingClickGUI = false;
				}
			}
		}
		
		if (this.draggingMod != null)
		{
			this.draggingMod.setPosition(mouseX - this.dragX, mouseY - this.dragY);
			
			if (this.draggingMod.hasSetting("Size"))
			{
				float size = this.draggingMod.getSetting("Size").getSliderValue();
				
				if (this.draggingMod.getX() < 2.5F)
				{
					this.draggingMod.setPosition(2.5F, this.draggingMod.getY());
				}
				
				if (this.draggingMod.getY() < 2.5F)
				{
					this.draggingMod.setPosition(this.draggingMod.getX(), 2.5F);
				}
				
				if (this.draggingMod.getX() > (this.width - (this.draggingMod.getWidth() * size)) - 2.5F)
				{
					this.draggingMod.setPosition((this.width - (this.draggingMod.getWidth() * size)) - 2.5F, this.draggingMod.getY());
				}
				
				if (this.draggingMod.getY() > (this.height - (this.draggingMod.getHeight() * size)) - 2.5F)
				{
					this.draggingMod.setPosition(this.draggingMod.getX(), (this.height - (this.draggingMod.getHeight() * size)) - 2.5F);
				}
			}
			
			else
			{
				if (this.draggingMod.getX() < 2.5F)
				{
					this.draggingMod.setPosition(2.5F, this.draggingMod.getY());
				}
				
				if (this.draggingMod.getY() < 2.5F)
				{
					this.draggingMod.setPosition(this.draggingMod.getX(), 2.5F);
				}
				
				if (this.draggingMod.getX() > (this.width - this.draggingMod.getWidth()) - 2.5F)
				{
					this.draggingMod.setPosition((this.width - this.draggingMod.getWidth()) - 2.5F, this.draggingMod.getY());
				}
				
				if (this.draggingMod.getY() > (this.height - this.draggingMod.getHeight()) - 2.5F)
				{
					this.draggingMod.setPosition(this.draggingMod.getX(), (this.height - this.draggingMod.getHeight()) - 2.5F);
				}
			}
		}
		
		for (Mod mod : Apple.CLIENT.getModsManager().getMods())
		{
			if (mod.isEnabled() && mod.hasGUI())
			{
				mod.onRender();
				
				if (mod.hasSetting("GUI"))
				{
					if (mod.getSetting("GUI").getCheckBoxValue())
					{
						if (mod.hasSetting("Size"))
						{
							float size = mod.getSetting("Size").getSliderValue();
							
							if (this.isInsideBox(mouseX, mouseY, mod.getX(), mod.getY(), mod.getX() + (mod.getWidth() * size), mod.getY() + (mod.getHeight() * size)))
							{
								this.drawRect(mod.getX(), mod.getY(), mod.getX() + (mod.getWidth() * size), mod.getY() + (mod.getHeight() * size), new Color(0, 0, 0, this.draggingMod != null ? 75 : 25).getRGB());
							}
						}
						
						else
						{
							if (this.isInsideBox(mouseX, mouseY, mod.getX(), mod.getY(), mod.getX() + mod.getWidth(), mod.getY() + mod.getHeight()))
							{
								this.drawRect(mod.getX(), mod.getY(), mod.getX() + mod.getWidth(), mod.getY() + mod.getHeight(), new Color(0, 0, 0, this.draggingMod != null ? 75 : 25).getRGB());
							}
						}
					}
				}
				
				else
				{
					if (mod.hasSetting("Size"))
					{
						float size = mod.getSetting("Size").getSliderValue();
						
						if (this.isInsideBox(mouseX, mouseY, mod.getX(), mod.getY(), mod.getX() + (mod.getWidth() * size), mod.getY() + (mod.getHeight() * size)))
						{
							this.drawRect(mod.getX(), mod.getY(), mod.getX() + (mod.getWidth() * size), mod.getY() + (mod.getHeight() * size), new Color(0, 0, 0, this.draggingMod != null ? 75 : 25).getRGB());
						}
					}
					
					else
					{
						if (this.isInsideBox(mouseX, mouseY, mod.getX(), mod.getY(), mod.getX() + mod.getWidth(), mod.getY() + mod.getHeight()))
						{
							this.drawRect(mod.getX(), mod.getY(), mod.getX() + mod.getWidth(), mod.getY() + mod.getHeight(), new Color(0, 0, 0, this.draggingMod != null ? 75 : 25).getRGB());
						}
					}
				}
			}
		}
		
		float i = this.width / 2;
		float j = this.height / 2;
		float animation = 0.9F + (this.index * 0.1F); // Goes from 0.9 to 1
		GlStateManager.pushMatrix();
		GlStateManager.scale(animation, animation, animation);
		i /= animation;
		j /= animation;
		mouseX /= animation;
		mouseY /= animation;
		Color temp1 = new Color(this.backgroundColourDarkened, true);
        Color temp2 = new Color(this.backgroundColour, true);
		Color backgroundColourDarkened = new Color(temp1.getRed(), temp1.getGreen(), temp1.getBlue(), (int) (this.index * 200));
        Color backgroundColour = new Color(temp2.getRed(), temp2.getGreen(), temp2.getBlue(), (int) (this.index * 200));
		
		if (this.isInsideBox(mouseX, mouseY, i - 50, j - 12.5F, i + 50, j + 12.5F))
		{
		    RoundedUtil.drawRoundedRect(i - 50, j - 12.5F, i + 50, j + 12.5F, 3, backgroundColourDarkened.getRGB());
		}
		
		else
		{
		    RoundedUtil.drawRoundedRect(i - 50, j - 12.5F, i + 50, j + 12.5F, 3, backgroundColour.getRGB());
		}
		
		this.drawStringAlpha("SETTINGS", i - (this.getStringWidth("SETTINGS", 12) / 2), j - 5, 12, -1, (int) (this.index * 255));
		this.mc.getTextureManager().bindTexture(new ResourceLocation("icons/icon_32x32.png"));
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1, this.index);
		this.drawModalRectWithCustomSizedTexture(i - 40, j - 90, 0, 0, 80, 80, 80, 80);
		GlStateManager.popMatrix();
    }
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		int i = this.width / 2;
		int j = this.height / 2;
		
		for (Mod mod : Apple.CLIENT.getModsManager().getMods())
		{
			if (mod.isEnabled() && mod.hasGUI())
			{
				if (mod.hasSetting("GUI"))
				{
					if (mod.getSetting("GUI").getCheckBoxValue())
					{
						if (mod.hasSetting("Size"))
						{
							float size = mod.getSetting("Size").getSliderValue();
							
							if (this.isInsideBox(mouseX, mouseY, mod.getX(), mod.getY(), mod.getX() + (mod.getWidth() * size), mod.getY() + (mod.getHeight() * size)))
							{
								SoundUtil.playClickSound();
								this.draggingMod = mod;
								this.dragX = mouseX - mod.getX();
								this.dragY = mouseY - mod.getY();
							}
						}
						
						else
						{
							if (this.isInsideBox(mouseX, mouseY, mod.getX(), mod.getY(), mod.getX() + mod.getWidth(), mod.getY() + mod.getHeight()))
							{
								SoundUtil.playClickSound();
								this.draggingMod = mod;
								this.dragX = mouseX - mod.getX();
								this.dragY = mouseY - mod.getY();
							}
						}
					}
				}
				
				else
				{
					if (mod.hasSetting("Size"))
					{
						float size = mod.getSetting("Size").getSliderValue();
						
						if (this.isInsideBox(mouseX, mouseY, mod.getX(), mod.getY(), mod.getX() + (mod.getWidth() * size), mod.getY() + (mod.getHeight() * size)))
						{
							SoundUtil.playClickSound();
							this.draggingMod = mod;
							this.dragX = mouseX - mod.getX();
							this.dragY = mouseY - mod.getY();
						}
					}
					
					else
					{
						if (this.isInsideBox(mouseX, mouseY, mod.getX(), mod.getY(), mod.getX() + mod.getWidth(), mod.getY() + mod.getHeight()))
						{
							SoundUtil.playClickSound();
							this.draggingMod = mod;
							this.dragX = mouseX - mod.getX();
							this.dragY = mouseY - mod.getY();
						}
					}
				}
			}
		}
		
		if (this.isInsideBox(mouseX, mouseY, i - 50, j - 12.5F, i + 50, j + 12.5F) && mouseButton == 0)
		{
			SoundUtil.playClickSound();
			this.displayingClickGUI = true;
			this.closingGui = true;
		}
    }
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
    {
		super.mouseReleased(mouseX, mouseY, state);
		
		if (this.draggingMod != null)
		{
			this.draggingMod = null;
		}
    }
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		if (this.draggingMod != null)
		{
			this.draggingMod = null;
		}
		
		if (this.tempVariableForAnimation)
		{
			Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 3);
			this.tempVariableForAnimation = false;
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
		if (keyCode == 1)
		{
			this.closingGui = true;
		}
    }
	
	@Override
	public boolean doesGuiPauseGame()
    {
		return false;
    }
	
	public boolean isInsideBox(int mouseX, int mouseY, float x, float y, float width, float height)
	{
		return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
	}
}
