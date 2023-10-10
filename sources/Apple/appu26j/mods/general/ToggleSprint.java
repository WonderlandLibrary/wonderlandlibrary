package appu26j.mods.general;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.google.common.eventbus.Subscribe;

import appu26j.events.entity.EventTick;
import appu26j.events.mc.EventKey;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import club.marshadow.ColorUtil;
import net.minecraft.client.network.NetworkPlayerInfo;

@ModInterface(name = "Toggle Sprint", description = "Allows you to toggle automatic sprinting.", category = Category.GENERAL, width = 109, height = 15)
public class ToggleSprint extends Mod
{
	private boolean sprint = true, sneak = true, flag1 = false, flag2 = false;
	
	public ToggleSprint()
	{
		this.addSetting(new Setting("GUI", this, false));
		this.addSetting(new Setting("Hide GUI when player is not sprinting", this, false));
		this.addSetting(new Setting("Background", this, false));
        this.addSetting(new Setting("Background Transparency", this, false));
        this.addSetting(new Setting("Background Rounded Corners", this, false));
		this.addSetting(new Setting("Text Shadow", this, true));
		this.addSetting(new Setting("Text Color (RGB)", this, new int[]{255, 255, 255}));
		this.addSetting(new Setting("Rainbow Text Color", this, false));
		this.addSetting(new Setting("Toggle Sneak", this, false));
		this.addSetting(new Setting("Size", this, 0.5F, 1, 2, 0.25F));
	}
	
	@Subscribe
	public void onTick(EventTick e)
	{
	    this.width = this.getStringWidth("[Sprinting (Toggled)]") + 6;
	    
		if (this.sprint)
		{
			this.mc.gameSettings.keyBindSprint.setPressed(true);
		}
		
		else
		{
			if (this.flag1)
			{
				this.mc.gameSettings.keyBindSprint.setPressed(false);
				this.flag1 = false;
			}
		}
		
		if (this.getSetting("Toggle Sneak").getCheckBoxValue())
		{
			if (this.sneak)
			{
				this.mc.gameSettings.keyBindSneak.setPressed(true);
			}
			
			else
			{
				if (this.flag2)
				{
					this.mc.gameSettings.keyBindSneak.setPressed(false);
					this.flag2 = false;
				}
			}
		}
	}
	
	@Subscribe
	public void onKey(EventKey e)
	{
		if (e.getKey() == this.mc.gameSettings.keyBindSprint.getKeyCode())
		{
			this.sprint = !this.sprint;
			
			if (!this.sprint)
			{
				this.flag1 = true;
			}
		}
		
		if (this.getSetting("Toggle Sneak").getCheckBoxValue())
		{
			if (e.getKey() == this.mc.gameSettings.keyBindSneak.getKeyCode())
			{
				this.sneak = !this.sneak;
				
				if (!this.sneak)
				{
					this.flag2 = true;
				}
			}
		}
	}
	
	@Override
	public void onRender()
	{
		super.onRender();
		float size = this.getSetting("Size").getSliderValue();
		
		if (this.getSetting("GUI").getCheckBoxValue())
		{
			int[] colors = this.getSetting("Text Color (RGB)").getColors();
			int color = new Color(colors[0], colors[1], colors[2]).getRGB();
			
			if (this.getSetting("Rainbow Text Color").getCheckBoxValue())
			{
				color = ColorUtil.getRainbowColor();
			}
			
			if (this.getSetting("Hide GUI when player is not sprinting").getCheckBoxValue())
			{
				if (this.mc.thePlayer.isSneaking() && this.getSetting("Toggle Sneak").getCheckBoxValue())
				{
					this.drawRect(this.x, this.y, this.x + (this.width * size), this.y + (this.height * size), this.backgroundColour);
					this.drawString("[Sneaking (" + (this.sneak ? "Toggled" : "Vanilla") + ")]", this.x + (4 * size), this.y + (4 * size), 8 * size, color);
				}
				
				else if (this.mc.thePlayer.isSprinting())
				{
					this.drawRect(this.x, this.y, this.x + (this.width * size), this.y + (this.height * size), this.backgroundColour);
					this.drawString("[Sprinting (" + (this.sprint ? "Toggled" : "Vanilla") + ")]", this.x + ((this.sprint ? 3.5F : 6.5F) * size), this.y + (4 * size), 8 * size, color);
				}
			}
			
			else
			{
				if (this.mc.thePlayer.isSneaking() && this.getSetting("Toggle Sneak").getCheckBoxValue())
				{
					this.drawRect(this.x, this.y, this.x + (this.width * size), this.y + (this.height * size), this.backgroundColour);
					this.drawString("[Sneaking (" + (this.sneak ? "Toggled" : "Vanilla") + ")]", this.x + (4 * size), this.y + (4 * size), 8 * size, color);
				}
				
				else
				{
					this.drawRect(this.x, this.y, this.x + (this.width * size), this.y + (this.height * size), this.backgroundColour);
					this.drawString("[Sprinting (" + (this.sprint ? "Toggled" : "Vanilla") + ")]", this.x + ((this.sprint ? 3.5F : 6.5F) * size), this.y + (4 * size), 8 * size, color);
				}
			}
		}
	}
	
	@Override
	public void onDisable()
	{
		super.onDisable();
		
		if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
		{
			this.mc.gameSettings.keyBindSprint.setPressed(false);
		}
	}
}
