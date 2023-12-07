package Velo.impl.Modules.visuals.hud;

import org.lwjgl.opengl.GL11;

import Velo.api.Module.Module;
import Velo.impl.Event.EventRender;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class Inventory extends Module{

	public Inventory() {
		super("Inventory", "Inventory", 0, Category.VISUALS);
	}
	
	
	@Override
	public void onRenderUpdate(EventRender event) {
		ScaledResolution sr = new ScaledResolution(mc);
		int count = 0;
		int yFix = 0;
		int yFix2 = 0;
		
		 for (int i1 = 9; i1 < 36; ++i1)
	     {
			    if(i1 == 18) {
			    	yFix = 17;
			    	count = 0;
			    }
			    if(count == 144) {
			    	yFix2 = 17;
			    	count = 0;
			    }
	            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i1);
	            renderItemStack(sr.getScaledWidth() / 2 + count, 10 + yFix + yFix2, stack);
	            
	          // System.out.println(count);
	            count += 16;
	     }
		 int count2 = 0;
			int yFix22 = 0;
			int yFix222 = 0;
			 for (int i1 = 9; i1 < 36; ++i1)
		     {
				    if(i1 == 18) {
				    	yFix22 = 17;
				    	count2 = 0;
				    }
				    if(count2 == 144) {
				    	yFix222 = 17;
				    	count2 = 0;
				    }
		            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i1);
		            try {
		             GL11.glPushMatrix();
		            RenderHelper.enableStandardItemLighting();
		        	mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, sr.getScaledWidth() / 2 + count2, 10 + yFix22 + yFix222);
		        	GL11.glPopMatrix();
		        	RenderHelper.disableStandardItemLighting();
		            }catch(Exception e) {
		            	e.printStackTrace();
		            }
		        	//  mc.fontRendererObj.drawStringWithShadow("aaaa" + "",  count2, 10 + yFix22 + yFix222, -1);
		            
		          // System.out.println(count);
		            count2 += 16;
		     }
		super.onRenderUpdate(event);
	}
	

	
	private void renderItemStack(int x, int y, ItemStack itemstack) {
	    ScaledResolution sr = new ScaledResolution(mc);
	if(itemstack == null) {
		return;
	}
	GL11.glPushMatrix();
	RenderHelper.enableGUIStandardItemLighting();
	mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, x, y);
	GL11.glPopMatrix();
	
}
	
}
