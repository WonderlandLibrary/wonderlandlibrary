package appu26j.mods.visuals;

import java.awt.Color;

import appu26j.Cache;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import club.marshadow.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

@ModInterface(name = "Armor Status", description = "Allows you to see the current armor you are wearing.", category = Category.VISUALS, width = 50, height = 65)
public class ArmorStatus extends Mod
{
	public ArmorStatus()
	{
		this.addSetting(new Setting("Background", this, false));
		this.addSetting(new Setting("Background Transparency", this, false));
        this.addSetting(new Setting("Background Rounded Corners", this, false));
		this.addSetting(new Setting("Text Shadow", this, true));
		this.addSetting(new Setting("Text Color (RGB)", this, new int[]{255, 255, 255}));
		this.addSetting(new Setting("Rainbow Text Color", this, false));
		this.addSetting(new Setting("Hide when no armor is equipped", this, true));
		this.addSetting(new Setting("Size", this, 0.5F, 1, 2, 0.25F));
		this.y = ((Cache.getSR()).getScaledHeight() / 2) - 32.5F;
	}
	
	@Override
	public void onRender()
	{
		super.onRender();
		boolean itemStackIsNull = true;
		
		for (int i = 0; i < this.mc.thePlayer.inventory.armorInventory.length; i++)
		{
			ItemStack itemStack = this.mc.thePlayer.inventory.armorInventory[i];
			
			if (itemStack != null)
			{
				itemStackIsNull = false;
			}
			
		}
		
		if (!(itemStackIsNull && this.getSetting("Hide when no armor is equipped").getCheckBoxValue()))
		{
			float size = this.getSetting("Size").getSliderValue();
			int[] colors = this.getSetting("Text Color (RGB)").getColors();
			int color = new Color(colors[0], colors[1], colors[2]).getRGB();
			
			if (this.getSetting("Rainbow Text Color").getCheckBoxValue())
			{
				color = ColorUtil.getRainbowColor();
			}
			
			int offset = 3;
			GlStateManager.pushMatrix();
			GlStateManager.scale(size, size, size);
			
			for (int i = 0; i < this.mc.thePlayer.inventory.armorInventory.length; i++)
			{
				ItemStack itemStack = this.mc.thePlayer.inventory.armorInventory[i];
				
				if (itemStack != null)
				{
					this.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int) (this.x / size) + 2, (int) (this.y / size) + 2 + (offset * 15));
				}
				
				offset--;
			}
			
			GlStateManager.popMatrix();
			GlStateManager.color(1, 1, 1, 1);
			GlStateManager.enableAlpha();
			this.drawRect(this.x, this.y, this.x + (this.width * size), this.y + (this.height * size), this.backgroundColour);
			offset = 3;
			
			for (int i = 0; i < this.mc.thePlayer.inventory.armorInventory.length; i++)
			{
				ItemStack itemStack = this.mc.thePlayer.inventory.armorInventory[i];
				
				if (itemStack != null)
				{
					if (itemStack.isItemStackDamageable())
					{
						this.drawString((int) (((itemStack.getMaxDamage() - itemStack.getItemDamage()) / (float) itemStack.getMaxDamage()) * 100) + "%", this.x + (20 * size), this.y + 2 + (4 * size) + (offset * (15 * size)), 8 * size, color);
					}
				}
				
				offset--;
			}
			
			if (itemStackIsNull)
			{
				offset = 3;
				
				for (int i = 0; i < this.mc.thePlayer.inventory.armorInventory.length; i++)
				{
					switch (offset)
					{
						case 1:
						{
							this.drawString("No", this.x + (17 * size), this.y + 2 + (4 * size) + (offset * (15 * size)), 12 * size, color);
							break;
						}
						
						case 2:
						{
							this.drawString("Armor", this.x + (3 * size), this.y + 2 + (4 * size) + (offset * (15 * size)), 12 * size, color);
							break;
						}
					}
					
					offset--;
				}
			}
		}
	}
}
