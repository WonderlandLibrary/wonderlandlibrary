package moonsense.hud.mod.impl;

import java.awt.Color;
import java.text.Format;

import org.lwjgl.opengl.GL11;

import moonsense.hud.mod.HudMod;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;

public class ArmorStatusMod extends HudMod {

	public ArmorStatusMod() {
		super("ArmorStatus", 5, 200);
	}
	
	@Override
	public void draw() {
		this.getArmorValues();
		
		super.draw();
	}
	
	public void getArmorValues() {
		int number = 3;
		
		int iValue = 0;
		
		for (int i = 0; i < this.mc.thePlayer.inventory.armorInventory.length; i++) {
			ItemStack itemStack = this.mc.thePlayer.inventory.armorInventory[i];
			this.renderArmor(number - i, itemStack);
			++iValue;
		}

	}
	
	public void renderArmor(int i, ItemStack is) {
		if(is != null) {
			GL11.glPushMatrix();
			int yValue = this.getY() + this.fr.FONT_HEIGHT * i * 2;
			int xValue = this.getX();
			
			if(is.getItem().isDamageable()) {
				double damage = (double)(is.getMaxDamage() - is.getItemDamage()) / (double)is.getMaxDamage() * 100.0D;
				this.fr.drawStringWithShadow(String.format("%.2f%%", damage), (float)(xValue + 20), (float)(yValue + 4), -1);
			} else {
				String number = String.valueOf(is.stackSize);
				if(!number.equals("1") && !number.equals("0")) {
					this.fr.drawStringWithShadow(number, (float)(xValue + 20), (float)(yValue + 4), -1);
				}
			}
			
			RenderHelper.enableGUIStandardItemLighting();
			mc.getRenderItem().renderItemAndEffectIntoGUI(is, xValue, yValue);
			GL11.glPopMatrix();
			
		}
		
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {
		fr.drawStringWithShadow(name, getX(), getY(), -1);
		
		super.renderDummy(mouseX, mouseY);
	}
	
	@Override
	public int getWidth() {
		return fr.getStringWidth(name);
	}
	
	@Override
	public int getHeight() {
		return fr.FONT_HEIGHT;
	}

}
