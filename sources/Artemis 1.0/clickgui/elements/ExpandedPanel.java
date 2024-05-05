package artemis.clickgui.elements;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import artemis.Artemis;
import artemis.module;
import artemis.clickgui.ClickGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
public class ExpandedPanel extends ClickGui{
	module m;
	artemis.mods.Movement.Fly fly;
	
	GuiScreen guiscreen;
	
	FontRenderer fr;
	
	

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for(int o = 1; o < Artemis.getModules().size(); o++){
		if(Artemis.getModules().get(o).getName().equalsIgnoreCase("Fly")) {
			System.out.println("Fly");
		        int id = 1; 
			Gui.drawRect(300, 60, 386, 150, 0x99000000);
		 }else if(Artemis.getModules().get(o).getName().equalsIgnoreCase("Killaura")) {
			 System.out.println("Killaura");
				Gui.drawRect(300, 60, 386, 150, 0x99000000);
				
		 }
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
      }
	
	@Override
	public void initGui() {
		for(int e = 1; e < Artemis.getModules().size(); e++){
			if(Artemis.getModules().get(e).getName().equalsIgnoreCase("Step")) {
				System.out.println("Step");
			       new GuiButton(56456, 30, 30, 20, 20, "Hypixel");
			 }else if(Artemis.getModules().get(e).getName().equalsIgnoreCase("Killaura")) {
				 new GuiButton(56777, 30, 30, 20, 20, "Packet");
			 }
			}
		
		
		
		super.initGui();
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		
		if(button.id == 56456) {
			
				fly.FlyMode = "Hypixel";	
		
	}
		if(button.id == 56777) {
			
			System.out.println("packet");	
	
}
	
}
}

/*
for(int i = 1; i < artemis_client.getModules().size(); i++){
		if(m.getName().equals("Fly")) {
	    System.out.println("fly");
		Gui.drawRect(300, 60, 386, 150, 0x99000000);
		mc.fontRendererObj.drawString("Flight", 452, 4, 0xffffffff);
		
		}
		if(m.getName().equals("Killaura")) {
			Gui.drawRect(300, 60, 386, 150, 0x99000000);
			mc.fontRendererObj.drawString("Killaura", 452, 4, 0xffffffff);
			System.out.println("killaura");
			}
		}


*/