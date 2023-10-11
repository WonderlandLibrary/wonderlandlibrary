package artemis.clickgui;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.SwingUtilities;

import org.lwjgl.input.Mouse;

import artemis.Category;
import artemis.Artemis;
import artemis.module;
import artemis.altmanager.GuiAltManager;
import artemis.clickgui.elements.Button;
import artemis.clickgui.elements.CheckBox;
import artemis.clickgui.elements.ExpandedPanel;
import artemis.mods.Movement.Fly;
import artemis.mods.Render.ClickGUI;
import artemis.settings.Setting;
import artemis.util.ParticleUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen{
	private ParticleUtil particle;
	Fly Fly;
	Color temp;
	module m;
	GuiButton button;
	String[] args;
	public boolean visible = false;
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		drawDefaultBackground();
		particle.drawParticle();
		
		
		
		//int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 170).getRGB();
		
		
		//new GuiButton(545345, 130, 2, 220, 14, "");
		Gui.drawRect(130, 2, 220, 17, 0xff000000);
		Gui.drawRect(130, 2, 132, 17,  rainbow(300));
		Gui.drawRect(218, 2, 220, 17,  rainbow(300));
		mc.fontRendererObj.drawString("Combat", 152, 4, 0xffffffff);
		Gui.drawRect(230, 2, 320, 17, 0xff000000);
		Gui.drawRect(230, 2, 232, 17,  rainbow(300));
		Gui.drawRect(318, 2, 320, 17,  rainbow(300));
		mc.fontRendererObj.drawString("Movement", 252, 4, 0xffffffff);
		Gui.drawRect(330, 2, 420, 17, 0xff000000);
		Gui.drawRect(330, 2, 332, 17,  rainbow(300));
		Gui.drawRect(418, 2, 420, 17,  rainbow(300));
		mc.fontRendererObj.drawString("Render", 352, 4, 0xffffffff);
		Gui.drawRect(430, 2, 520, 17, 0xff000000);
		Gui.drawRect(430, 2, 432, 17,  rainbow(300));
		Gui.drawRect(518, 2, 520, 17,  rainbow(300));
		mc.fontRendererObj.drawString("Other", 452, 4, 0xffffffff);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	  public static int rainbow(int delay) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public int placeForHackY(module m) {
		if(m.getCategory().equals(Category.COMBAT)) return Category.placeInListCombat(m) * 14 + 3;
		if(m.getCategory().equals(Category.MOVEMENT)) return Category.placeInListMovement(m) * 14 + 3;
		if(m.getCategory().equals(Category.RENDER)) return (Category.placeInListRender(m) + 1) * 14 + 3;
		if(m.getCategory().equals(Category.OTHER)) return (Category.placeInListOther(m) * 14) + 3;
		
		return 0;
	}

	public int placeForHackX(module m) {
		if(m.getCategory().equals(Category.COMBAT)) return 130;
		if(m.getCategory().equals(Category.MOVEMENT)) return 230;
		if(m.getCategory().equals(Category.RENDER)) return 330;
		if(m.getCategory().equals(Category.OTHER)) return 430;
		
		return 0;
	}
	
	@Override
	public void initGui() {
		this.particle = new ParticleUtil(this.width, this.height);
		visible = false;
		new GuiButton(545345, 130, 2, 220, 14, "Combat");
		if(visible = true) {
		for(int i = 1; i < Artemis.getModules().size(); i++){
			module m = Artemis.getModules().get(i);
			
			buttonList.add(new GuiButton(i, placeForHackX(m), placeForHackY(m), 70, 14, m.getName()));
		}	
		}else if(visible = false) {
				System.out.println("Closed");
			}

		if(visible = true) {
		for(int o = 1; o < Artemis.getModules().size(); o++){
			module m2 = Artemis.getModules().get(o);
			
		buttonList.add(new GuiButton(o, placeForHackX(m2) + 70, placeForHackY(m2), 20, 12, "§7>"));
		}
	}else if(visible = false) {
		
	}
		}
		
		//new GuiButton(545345, 130, 2, 220, 14, "Combat");
		@Override
		public void actionPerformed(GuiButton button) throws IOException {
			/*if(button.id == 545345) {
				if(visible = false) {
					visible = true;
				}else {
					visible = false;
				}
			}*/
			for(int i = 1; i < Artemis.getModules().size(); i++){
				if(button.id == i) {
					if(Mouse.isButtonDown(0)) {
						Artemis.getModules().get(i).toggle();		
	             }
					
				
					for(int o = 1; o < Artemis.getModules().size(); o++){
						if(button.id == o) {
							System.out.println("Open Settings");
							if(Artemis.getModules().get(o).getName().equalsIgnoreCase("Fly")) {
								System.out.println("Fly");
								this.mc.displayGuiScreen(new ExpandedPanel());    
							}
				        }
			          }							
	             }    
	           }
	        }	
        }



 


