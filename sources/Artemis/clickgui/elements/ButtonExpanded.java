package artemis.clickgui.elements;

import artemis.Artemis;
import artemis.module;
import artemis.util.ParticleUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

public class ButtonExpanded extends ExpandedPanel{

	
	
	
	@Override
	public void initGui() {
			for(int o = 1; o < Artemis.getModules().size(); o++){
			if(Artemis.getModules().get(o).getName().equalsIgnoreCase("Fly")) {
				buttonList.add(new GuiButton(56456, 300, 300, 20, 12, "Hiypixel"));
				System.out.println("Fly hiypixl");
			 }else if(Artemis.getModules().get(o).getName().equalsIgnoreCase("Killaura")) {
				 buttonList.add(new GuiButton(56456, 300, 300, 20, 12, "Packet"));
				 System.out.println("killaura packet");
			 }
			}
		
		
		
		super.initGui();
	}
}
