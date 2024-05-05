package epsilon.ClickGUI.dropdown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.Color;

import epsilon.Epsilon;
import epsilon.modules.render.Theme;
import epsilon.settings.setting.ModeSetting;
import epsilon.util.EpsilonColorUtils;
import epsilon.util.ShapeUtils;
import epsilon.util.Font.Fonts;
import net.minecraft.client.gui.GuiScreen;

public class ModeSettingGUI extends GuiScreen {
	
	private final ShapeUtils render = new ShapeUtils();
	private final ModeSetting display;
	private String hoveredModule;
	private final String name;
	
	public ModeSettingGUI (final ModeSetting display, final String name) {
		this.display = display;
		this.name = name;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int squareY = display.modes.size()*7;
		
		int offset = 0;
		int ySet = 0;
		int resets=0;
		int jumps = 0;
		render.rect( 0, 0, this.width, this.height,new Color(44, 44, 44, 215));
		render.roundedRectNonFilled(this.width/2.8-1.5f, this.height/2.3f-2, this.width/9+4 +(display.modes.size()>4 ? 80 : 0), squareY+4, 5, EpsilonColorUtils.getColor(0, Theme.getTheme(), 0.25F), true, true, true, true);
		render.roundedRect(this.width/2.8, this.height/2.3f, this.width/9+(display.modes.size()>4 ? 80 : 0),  squareY, 5, new Color(88, 88, 88, 225));

		Fonts.Segation18.drawStringWithShadow(name + ": " + display.name + ": " + display.getMode(), this.width/2.8+10, this.height/2.4f, -1);
		for(String d : display.modes) {
			int color ;
			if(mouseOver((float) (this.width/2.8+10+offset), this.height/2.27f + ySet, mc.fontRendererObj.getStringWidth(d),5, mouseX,mouseY)) {
				hoveredModule = d;
				color = EpsilonColorUtils.getColorAsInt(0, Theme.getTheme(), 0.25f);
				render.roundedRectNonFilled(this.width/2.8+5+offset, this.height/2.3f + ySet, (d.length()+2)*5.4, 15, 5,new Color(255,255, 255, 111), true, true, true,true);
			}else color = -1;
			Fonts.Segation18.drawStringWithShadow("(" +d+")", this.width/2.8+10+offset, this.height/2.27f + ySet, color);
			
			offset+=(d.length()+2)*8;
			jumps+=(d.length()+2);
			if(jumps>this.width/60) {
				resets+=1;
				jumps = 0;
				offset = 0;
			}
			
			ySet=(int) (resets*this.height/41);
			

			
		}
		
		
		
		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mB) throws IOException {

		if(hoveredModule!=null) {
			display.setMode(hoveredModule);
		}
		
		
		super.mouseClicked(mouseX, mouseY, mouseY);
	}
	
	public void initGui() {
		super.initGui();
	}
	
	public void closeGui() {

		
		super.onGuiClosed();

	}
	
	public boolean mouseOver(final float posX, final float posY, final float width, final float height, final float mouseX, final float mouseY) {
		return mouseX > posX && mouseX < posX + width && mouseY > posY && mouseY < posY + height;
	}
	
	
	
	

}
