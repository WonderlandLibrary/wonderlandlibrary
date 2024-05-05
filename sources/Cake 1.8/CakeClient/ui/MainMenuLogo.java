package CakeClient.ui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class MainMenuLogo {
	public static void drawString(double scale,String text,float x,float y,int color)
	{
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale,scale,scale);
		Minecraft.getMinecraft().fontRendererObj.drawString(text,x,y,color);
		GlStateManager.popMatrix();
		
	}

}
