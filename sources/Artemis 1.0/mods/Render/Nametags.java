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

public class Nametags extends module{
	 EntityPlayer p;
	private static ResourceLocation fileloc = new ResourceLocation("textures/head.png");
	public Nametags() {
		super("Nametags", "Makes nametags larger", 0, 545, Category.RENDER);
	}
	
	Gui gui;
	
	public void drawNametags() {
		if(this.isToggled()) {
			 FontRenderer fr = mc.fontRendererObj;
			 for(Object e : mc.theWorld.loadedEntityList) {
					if (e instanceof EntityPlayer) {
	     			if(e != mc.thePlayer) {
						EntityPlayer p = (EntityPlayer) e;
						if(!p.isInvisible()) {
							double x = p.posX;
							double y = p.posY;
							double z = p.posZ;
							float string_width = fr.getStringWidth(p.getName()) / 2;
							
							fr.drawString(p.getName(), x, y + 10, -1);
							gui.drawRect(-fr.getStringWidth(p.getName()) / 2 - 2, -2, string_width + 2, 9, -1);

						}
	     			}
			   }
			}
		}
	}	
}


	