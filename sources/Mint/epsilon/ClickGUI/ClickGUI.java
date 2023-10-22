
package epsilon.ClickGUI;

import java.io.IOException;
import java.util.List;

import org.lwjgl.util.Color;

import epsilon.Epsilon;
import epsilon.modules.Module;
import epsilon.modules.Module.Category;
import epsilon.settings.Setting;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NoteSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.ShapeUtils;
import epsilon.util.Font.Fonts;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class ClickGUI extends GuiScreen{


	private boolean dragging, expanded;
    private int count = 0;
    private Category selectedCategory = Category.COMBAT, lastSelectedCategory;
    private Module selectedModule;

	
	
	ShapeUtils render = new ShapeUtils();
	private int huec;

	private int lastMouseX = 0;

	private Color huecb = new Color (61, 255, 138, 255);

	private float baseposWidth1, baseposHeight1, baseposWidth2, baseposHeight2, offsetwid, offsetheight;
	private float startClickPosX;
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		FontRenderer fr = mc.fontRendererObj;
		huec = 0xff000000;
		baseposWidth1 = midwidth()-355;baseposHeight1 = midheight()-200; baseposWidth2 = midwidth()+55;baseposHeight2 = midheight() + 150;
		



		
		render.roundedRect(baseposWidth1, baseposHeight1, baseposWidth2, baseposHeight2, 25, new Color ( 66, 66, 66, 255));
		//render.roundedRectCustom(midwidth() - 355, midheight() - 200, midwidth() - 575, midheight() - 300, 25, new Color ( 0, 0, 0, 55), true, false, false, true); 
		
	    final ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
	    

	    
	    count = 0;
		for(Category c : Module.Category.values()) {
			float trannie = 0;
			
			if(mouseOver(baseposWidth1 + 10, baseposHeight1 + 75 + count*65, baseposWidth1 - 200, baseposHeight1 - 130, mouseX,mouseY)) {
				c.canExpand= true;
			
			 
			}else {
				c.canExpand= false;
			}
			
			if(selectedCategory == c) {
				huecb = new Color (43, 183, 101, 255);
				lastSelectedCategory = c;
				trannie = 35;
				
			
			}else 
				huecb = new Color (61, 255, 138, 255);
			
			
			render.roundedRect(baseposWidth1 + 10 - trannie, baseposHeight1 + 75 + count*65, baseposWidth1 - 200, baseposHeight1 - 130, 4, huecb);
				
			fr.drawString(c.name, baseposWidth1 + 25 - trannie, baseposHeight1 + 85 + count*65, count);
			
			Category category = Module.Category.values()[Module.getCategoryAsInt(selectedCategory)];
			List<Module> modules = Epsilon.getModulesbyCategory(category);
			

			render.roundedRect(baseposWidth1 + 200, baseposHeight1 + 23, baseposWidth2-550, baseposHeight2 + modules.size()*20 - 490, 50, new Color (61, 255, 138, 255));
			render.roundedRect(baseposWidth1 + 202, baseposHeight1 + 25, baseposWidth2-554, baseposHeight2 + modules.size()*20 - 494, 48, new Color (60, 204, 135, 255));
			
			int moduleCount = 0;
			for(Module m : modules) {
				
				int moduleHue = -1;
				
				if(m.toggled)
					moduleHue = 0xff435E3C;
				fr.drawString(m.name, baseposWidth1 + 215, baseposHeight1 + 35 + moduleCount*20, moduleHue);
				
				m.hovered = mouseOver(baseposWidth1 + 215, baseposHeight1 + 35 + moduleCount*20, mc.fontRendererObj.getStringWidth(m.name), 10, mouseX, mouseY);
				
				
				if(selectedModule !=null && selectedModule.getCategory() == c && selectedModule == m) {
					

					int index = 0, maxLength = 0, offset = 0, offsety = 0;

					for(Setting setting : m.settings) {
						
						Color cc = new Color ();
						
						
						if(setting instanceof BooleanSetting) {
							BooleanSetting bool = (BooleanSetting) setting;
							cc = !bool.enabled ? new Color (255, 82, 58, 255) : new Color (41, 255, 0, 255);
							
							render.circle(baseposWidth1 + 440 + offset, baseposHeight1 + 37 + index*16 + offsety, 5, cc);
							
							
							setting.hovered = mouseOver(baseposWidth1 + 437 + offset, baseposHeight1 + offsety+ 35 + index*16, fr.getStringWidth(setting.name) + 10,10,mouseX,mouseY);
							
							fr.drawString(setting.name, baseposWidth1 + 450 + offset, baseposHeight1 + 35+ index*16 + offsety, -1);
						}
						
						if(setting instanceof NoteSetting) {
							NoteSetting note = (NoteSetting) setting;
							
							fr.drawString(setting.name, baseposWidth1 + 450 + offset, baseposHeight1 + 35+ index*16 + offsety, -1);
							
						}
						
						
						if(setting instanceof ModeSetting ) {

							ModeSetting mode = (ModeSetting) setting;

							setting.hovered = mouseOver(baseposWidth1 + 437 + offset, baseposHeight1 + offsety+ 49 + index*16, fr.getStringWidth(setting.name) + 10,10,mouseX,mouseY);
							
							fr.drawString(setting.name, baseposWidth1 + 440 + offset, baseposHeight1 + 35+ index*16 + offsety, -1);

							index++;
							fr.drawString(" §2<§f"+ mode.getMode() + "§2>", baseposWidth1 + 440 + offset, baseposHeight1 + 35+ index*16 + offsety, -1);

						}
						
						if(setting instanceof NumberSetting ) {
							
							NumberSetting numba = (NumberSetting) setting;

							
							fr.drawString(setting.name + " " + numba.value, baseposWidth1 + 440 + offset, baseposHeight1 + 35+ index*16 + offsety, -1);

							index++;

							setting.hovered = mouseOver(baseposWidth1 + 435 + offset + numba.sliderValue, baseposHeight1 + 31+ index*16 + offsety, 20 ,15, mouseX,mouseY);
							render.roundedRect(baseposWidth1 + 440 + offset, baseposHeight1 + 35+ index*16 + offsety, 75,  7, 5, new Color(60, 204, 135, 255));
							
							
							render.roundedRect(baseposWidth1 + 440 + offset + numba.sliderValue, baseposHeight1 + 35+ index*16 + offsety, 75 - numba.sliderValue,  7, 5, new Color(255, 82, 58, 255));

							
							render.roundedRect(baseposWidth1 + 440 + offset + numba.sliderValue, baseposHeight1 + 31+ index*16 + offsety, 5,  15 , 5, new Color(0, 255, 148, 255));
						}

						index++;
						if(index*10>baseposWidth1) {
							offset = 120;
							offsety = -465; 
						}
						if(index*5>baseposWidth1) {

							offset = 240;
							offsety = -930;
						}
						
					}
					
				}
				
				moduleCount++;
			}
			
				
			count++;
		}
		
		
		


		super.drawScreen(mouseX, mouseY, partialTicks);

		
		
	}
	
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceMouseClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceMouseClick);
		
		Category category = Module.Category.values()[Module.getCategoryAsInt(selectedCategory)];
		List<Module> modules = Epsilon.getModulesbyCategory(category);
		if(clickedMouseButton == 0) {
			for(Module m : modules)  {
				for(Setting setting : m.settings) {
					if(setting.hovered ) {
						if(setting instanceof NumberSetting ) {
							
							NumberSetting numba = (NumberSetting) setting;
							
							if(mouseX - startClickPosX <72 && mouseX - startClickPosX >-1 )
								numba.sliderValue = mouseX - startClickPosX;
							
							if(mouseX == startClickPosX)
								numba.setValue(numba.minimum);
							
							if(lastMouseX == mouseX)
								return;
							
							if(lastMouseX<mouseX)
								numba.increment(true);
							else
								numba.increment(false);

							/*
							 * Didnt do this the right way 
							 * but i gotta go finish a test so ill fix after
							*/
							lastMouseX = mouseX;
							
						}
					}
				}
			}
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		startClickPosX = mouseX;

		Category category = Module.Category.values()[Module.getCategoryAsInt(selectedCategory)];
		List<Module> modules = Epsilon.getModulesbyCategory(category);
		FontRenderer fr = mc.fontRendererObj;
		if(mouseButton == 0) {
			
			
			
			for(Module m : modules)  {
				for(Setting setting : m.settings) {
					if(setting.hovered ) {
						if(setting instanceof BooleanSetting) {

							BooleanSetting bool = (BooleanSetting) setting;
							bool.toggle();
							mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 0.4F + 0.8F));
						}

						if(setting instanceof ModeSetting ) {
							ModeSetting mode = (ModeSetting) setting;
							mode.cycle(true);
						}

					}
				}
				if(m.hovered) {
					m.toggle();
					mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 0.4F + 0.8F));
				}
			}
			
			for(Category c : Module.Category.values()) {
				if(c.canExpand) {
					if(selectedCategory == c) 
						return;
					
						
					selectedCategory = c;
					mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 0.4F + 0.8F));
					
					
					
				}
				
			}
		}
		if(mouseButton == 1) {
			for(Module m : modules)  {
				
				for(Setting setting : m.settings) {
					if(setting.hovered ) {

						if(setting instanceof ModeSetting ) {
							ModeSetting mode = (ModeSetting) setting;
							mode.cycle(false);
						}

					}
				}
				
				if(m.hovered) {
					mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("random.anvil_land"), 0.4F + 0.8F));
					if(selectedModule == m) {
						selectedModule = null;
						return;
					}
					selectedModule = m;
				}
			}
		}
		
	}
	
	public void initGui() {
		super.initGui();
	}
	
	public boolean mouseOver(final float posX, final float posY, final float width, final float height, final float mouseX, final float mouseY) {
		return mouseX > posX && mouseX < posX + width && mouseY > posY && mouseY < posY + height;
	}
	
	private int midwidth() {
		return this.width/2;
	}
	
	private int midheight() {
		return this.height/2;
	}
	
}