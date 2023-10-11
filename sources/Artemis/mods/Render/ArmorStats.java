package artemis.mods.Render;

import java.awt.Font;

import org.lwjgl.opengl.GL11;

import artemis.Category;
import artemis.module;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ArmorStats extends module{
	 FontRenderer fr = mc.fontRendererObj;
	public ArmorStats() {
		super("ArmorStats", "Shows your armor", 0, 435, Category.RENDER);
	}
	
	
	public void onUpdate() {
		if(this.isToggled()) {
			
			
			
		}
	}
	
	public void drawArmor() {
		for(int i = 0; i < mc.thePlayer.inventory.armorInventory.length; i++) {
			
			ItemStack itemStack = mc.thePlayer.inventory.armorInventory[i];
			renderItemStack(i, itemStack);
		}
		
		
		
	}


	private void renderItemStack(int i, ItemStack is) {
		if(is == null) {
			return;
		}
		
		GL11.glPushMatrix();
		
		int yAdd = (-16 * i) + 48;
		
		/*if(is.getItem().isDamageable()) {
			double damage = ((is.getMaxDamage() - is.getItemDamage()) / (double) is.getMaxDamage()) * 100;
			fr.drawString(String.format("%.2f%%", damage), 30 + 20, 30 + yAdd + 5, -1);
		}*/
		
		RenderHelper.enableGUIStandardItemLighting();
		mc.getRenderItem().renderItemAndEffectIntoGUI(is, 30, 30 + yAdd);
		
		GL11.glPopMatrix();
		
	}

}
