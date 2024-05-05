package artemis.mods.Render;

import artemis.Category;
import artemis.module;
import artemis.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PlayerStats extends module{
	 EntityPlayer p;
	private static ResourceLocation fileloc = new ResourceLocation("textures/head.png");
	public PlayerStats() {
		super("PlayerStats", "Shows nearby players stats", 0, 54, Category.RENDER);
	}
	 public int offset;
		
			
	
	
	
	Gui gui;
	 public void onUpdate() {
		    if (isToggled()) {
		    	
	       } 
	 
	 }
	 
	 public void drawStats() {
		 if(this.isToggled()) {
		 FontRenderer fr = mc.fontRendererObj;
		 for(Object e : mc.theWorld.loadedEntityList) {
				if (e instanceof EntityPlayer) {
     			if(e != mc.thePlayer) {
					EntityPlayer p = (EntityPlayer) e;
					if(!p.isInvisible()) {
 					if(mc.thePlayer.getDistanceToEntity(p) <= 3) {
 						
 						if (p.getHealth() >= 19)
 							offset = 0;
 						if (p.getHealth() >= 15 && !(p.getHealth() >= 19))
 							offset = 20;
 						if (p.getHealth() >= 13 && !(p.getHealth() >= 15))
 							offset = 33;
 						if (p.getHealth() >= 10 && !(p.getHealth() >= 15))
 							offset = 47;
 						if (p.getHealth() >= 7 && !(p.getHealth() >= 10))
 							offset = 60;
 						if (p.getHealth() >= 5 && !(p.getHealth() >= 10))
 							offset = 106;
 						if (p.getHealth() >= 2 && !(p.getHealth() >= 5))
 							offset = 137; 
 						if (p.getHealth() >= 0 && !(p.getHealth() >= 2))
 							offset = 140; 	// this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/head.png"));
 					       
 						  
 						//Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, this.width, this.height, this.width, this.height, this.width, this.height);
 						
 						RenderUtil.drawImage(mc.ingameGUI, fileloc, 310, 139, 32, 32, 32, 32, 0, 0);
 						
 						gui.drawRect(310, 139, 450, 199, 0x99000000);	
 						gui.drawRect(310, 194, 450 - offset, 199,   0x99990000);	
 						
 						
					     fr.drawString(p.getName(), 350, 150, -1);
					     fr.drawString("" + p.getHealth(), 317, 184, 0xfc0303);
					     if(mc.thePlayer.getHealth() >= p.getHealth()) {
					     fr.drawString("You may win", 350, 170, 0xfc0303);
 					}else if(mc.thePlayer.getHealth() < p.getHealth()) {
 						 fr.drawString("You may lose", 350, 170, 0xfc0303);
 					}
					     }
     			    }
	                  }
				}
                }
		 }
		 
	 }
	
		
}
	   