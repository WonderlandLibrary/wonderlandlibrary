package appu26j.mods;

import java.awt.Color;
import java.util.ArrayList;

import appu26j.Apple;
import appu26j.fontrenderer.SizedFontRenderer;
import appu26j.interfaces.GUIInterface;
import appu26j.interfaces.MinecraftInterface;
import appu26j.interfaces.ModInterface;
import appu26j.settings.Setting;
import net.minecraft.client.gui.Gui;
import quexii.RoundedUtil;

public class Mod implements MinecraftInterface, GUIInterface
{
	private ModInterface modInterface = this.getClass().getAnnotation(ModInterface.class);
	private String name = this.modInterface.name(), description = this.modInterface.description();
	protected float width = this.modInterface.width(), height = this.modInterface.height();
	protected float x = this.modInterface.x(), y = this.modInterface.y();
	private Category category = this.modInterface.category();
	private boolean enabled = false;
	
	/**
	 * If the mod is enabled, disable it; otherwise, if the mod is disabled, enable it.
	 */
	public void toggle()
	{
		if (this.enabled)
		{
			this.onDisable();
			this.enabled = false;
		}
		
		else
		{
			this.onEnable();
			this.enabled = true;
		}
	}
	
	/**
	 * Register mod
	 */
	public void onEnable()
	{
		Apple.CLIENT.getEventBus().register(this);
	}
	
	/**
	 * Unregister mod
	 */
	public void onDisable()
	{
		Apple.CLIENT.getEventBus().unregister(this);
	}
	
	/**
	 * This method is only used for the mods with a GUI
	 */
	public void onRender()
	{
		;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public float getX()
	{
		return this.x;
	}
	
	public float getY()
	{
		return this.y;
	}
	
	/**
	 * Sets the position of the mod
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float getWidth()
	{
		return this.width;
	}
	
	public float getHeight()
	{
		return this.height;
	}
	
	public Category getCategory()
	{
		return this.category;
	}
	
	public boolean isEnabled()
	{
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled)
	{
		if (enabled)
		{
			if (!this.enabled)
			{
				this.toggle();
			}
		}
		
		else
		{
			if (this.enabled)
			{
				this.toggle();
			}
		}
	}
	
	public boolean hasGUI()
	{
		return this.width != 0 && this.height != 0;
	}
	
	public void drawRect(float x, float y, float width, float height, int color)
	{
		if (this.hasSetting("Background") && !this.getSetting("Background").getCheckBoxValue())
		{
			return;
		}
		
		if (this.hasSetting("Background Transparency") && this.getSetting("Background Transparency").getCheckBoxValue())
		{
			Color tempColor = new Color(color, true);
			color = new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue(), 150).getRGB();
		}
		
		if (this.hasSetting("Background Rounded Corners") && this.getSetting("Background Rounded Corners").getCheckBoxValue())
        {
	        RoundedUtil.drawRoundedRect(x, y, width, height, 3, color);
        }
		
		else
		{
	        Gui.drawRect(x, y, width, height, color);
		}
	}
	
	public void drawString(String text, float x, float y, int color)
	{
		if (this.hasSetting("Text Shadow") && this.getSetting("Text Shadow").getCheckBoxValue())
		{
			this.drawStringWithShadow(text, x, y, 8, color);
			return;
		}
		
		this.drawString(text, x, y, 8, color);
	}

	public void drawString(String text, float x, float y, float size, int color)
	{
		if (this.hasSetting("Text Shadow") && this.getSetting("Text Shadow").getCheckBoxValue())
		{
			this.drawStringWithShadow(text, x, y, size, color);
			return;
		}
		
		SizedFontRenderer.drawString(text, x, y, size, color);
	}
	
	public void drawStringWithShadow(String text, float x, float y, int color)
	{
		if (this.hasSetting("Text Shadow") && !this.getSetting("Text Shadow").getCheckBoxValue())
		{
			this.drawString(text, x, y, 8, color);
			return;
		}
		
		this.drawStringWithShadow(text, x, y, 8, color);
	}

	public void drawStringWithShadow(String text, float x, float y, float size, int color)
	{
		if (this.hasSetting("Text Shadow") && !this.getSetting("Text Shadow").getCheckBoxValue())
		{
			this.drawString(text, x, y, size, color);
			return;
		}
		
		SizedFontRenderer.drawStringWithShadow(text, x, y, size, color);
	}
	
	public float getStringWidth(String text)
	{
		return this.getStringWidth(text, 8);
	}
	
	public float getStringWidth(String text, float size)
	{
		return SizedFontRenderer.getStringWidth(text, size);
	}
	
	public ArrayList<Setting> getSettings()
	{
		return Apple.CLIENT.getSettingsManager().getSettings(this);
	}
	
    public void addSetting(Setting setting)
    {
        Apple.CLIENT.getSettingsManager().addSetting(setting);
    }
    
    public Setting getSetting(String name)
    {
        return Apple.CLIENT.getSettingsManager().getSetting(name, this);
    }
    
    public boolean hasSetting(String name)
    {
    	return Apple.CLIENT.getSettingsManager().getSetting(name, this) != null;
    }
}
