package appu26j.mods.visuals;

import java.awt.Color;

import com.google.common.eventbus.Subscribe;

import appu26j.events.entity.EventAttack2;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import appu26j.utils.TimeUtil;
import club.marshadow.ColorUtil;
import net.minecraft.entity.EntityLivingBase;

@ModInterface(name = "Combo Display", description = "Displays how many combos you have.", category = Category.VISUALS, width = 53, height = 15)
public class Combo extends Mod
{
	private TimeUtil timeUtil = new TimeUtil();
    private EntityLivingBase prevEntity;
	private boolean aBoolean = false;
	private int combos = 0;
	
	public Combo()
	{
		this.addSetting(new Setting("Background", this, false));
		this.addSetting(new Setting("Background Transparency", this, false));
        this.addSetting(new Setting("Background Rounded Corners", this, false));
		this.addSetting(new Setting("Text Shadow", this, true));
		this.addSetting(new Setting("Text Color (RGB)", this, new int[]{255, 255, 255}));
		this.addSetting(new Setting("Rainbow Text Color", this, false));
		this.addSetting(new Setting("Size", this, 0.5F, 1, 2, 0.25F));
	}
	
	@Override
	public void onRender()
	{
		super.onRender();
		float size = this.getSetting("Size").getSliderValue();
		int[] colors = this.getSetting("Text Color (RGB)").getColors();
		int color = new Color(colors[0], colors[1], colors[2]).getRGB();
		EntityLivingBase entity = null;
		
		try
		{
		    entity = (EntityLivingBase) this.mc.pointedEntity;
		}
		
		catch (Exception e)
		{
		    ;
		}
		
		if (entity != null && entity.hurtResistantTime == entity.maxHurtResistantTime)
		{
		    if (this.aBoolean)
		    {
	            this.combos++;
	            this.timeUtil.reset();
	            this.aBoolean = false;
		    }
		    
		    this.prevEntity = entity;
		}
		
		else
		{
		    this.aBoolean = true;
		}
		
		if (this.getSetting("Rainbow Text Color").getCheckBoxValue())
		{
			color = ColorUtil.getRainbowColor();
		}
		
		if (this.mc.thePlayer.hurtTime != 0)
		{
			this.combos = 0;
		}
		
		if (this.timeUtil.hasTimePassed(3000) || (this.prevEntity != null && this.prevEntity.isDead))
		{
			if (this.combos > 0)
			{
				this.combos--;
			}
		}
		
		this.width = this.getStringWidth("Combos: " + this.combos) + 7;
		this.drawRect(this.x, this.y, this.x + (this.width * size), this.y + (this.height * size), this.backgroundColour);
		this.drawString("Combos: " + this.combos, this.x + (4 * size), this.y + (4 * size), 8 * size, color);
	}
}
