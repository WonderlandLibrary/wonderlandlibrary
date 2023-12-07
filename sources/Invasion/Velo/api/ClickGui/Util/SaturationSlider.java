package Velo.api.ClickGui.Util;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.opengl.GL11;

import Velo.api.Util.Other.ColorUtil;
import Velo.api.Util.Render.RenderUtil;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

//Your Imports

public class SaturationSlider extends Component {

	private boolean hovered;

	private NumberSetting set;

	
	private int x;
	private int y;
	private boolean dragging = false;

	private double renderWidth;
	
	public SaturationSlider(NumberSetting set, int x, int y) {
		this.set = set;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void renderComponent() {
		
		  final float hue = (float) (ColorUtil.getClickGUIColor());
		  //                           colorSaturation  colorBrightness 
	        int color = Color.HSBtoRGB(hue, 1.0F, 1.0F);
		//Gui.drawRect(x + 2, y + 10, x + 12, y + 10 + 12, this.hovered ? 0x80000000 : 0x60000000);
		 final int drag = (int)(this.set.getValue() / this.set.getMaximum() * 12);
		 
		 //Gui.drawRect(x + 2, y + offset + 6, x + (int) renderWidth, y + offset + 10,this.hovered ? 0x20000000 : 0x40000000);
		//RenderUtil.instance.draw2DImage(new ResourceLocation("textures/hue.png"), x - 100,  y + 10 + 6, 84, 8, Color.WHITE);
			RenderUtil.drawGradientSideways(x ,y + 13, x + (int) 90,y + 5 + 16, -1, color);
		 //RenderUtil.instance.draw2DImage(new ResourceLocation("textures/bright.png"), x ,  y + 10, 91, 8, Color.WHITE);
		//RenderUtil.instance.draw2DImage(new ResourceLocation("textures/hue.png"), x + 49 ,  y + 10, 42, 8, Color.WHITE);
			GL11.glPushMatrix();
			GL11.glColor3d(255, 255, 255);
			 Gui.drawRect(x + 2 + (int) renderWidth - 2, y + 11, x + (int) renderWidth + 3, y + 10 + 12,this.hovered ? -1 : -1);
			GL11.glPopMatrix();
		//Gui.drawRect(x, y + 10, x + 2, y + 10 + 12, 0x80000000);
		
			Fonts.targethud.drawString(this.set.getName() + ": " +  this.set.getValue() , x, y + 2, -1);
		

	}
	

	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY);

		
		double diff = Math.min(88, Math.max(0, mouseX - this.x));

		double min = 0;
		double max = 1;
		
		renderWidth = (88) * (set.getValue() - min) / (max - min);
		
		if (dragging) {
			if (diff == 0) {
				set.setValue(0);
			}
			else {
				double newValue = roundToPlace(((diff / 88) * (max - min) + min), 2);
				set.setValue(newValue);
			}
		}
}
	
	
	private static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButtonD(mouseX, mouseY) && button == 0) {
			dragging = true;
		}
		if(isMouseOnButtonI(mouseX, mouseY) && button == 0) {
			dragging = true;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		dragging = false;
	}
	
	public boolean isMouseOnButtonD(int x, int y) {
		if(x > this.x && x < this.x + (12 / 2 + 1) && y > this.y && y < this.y + 23) {
			return true;
		}
		return false;
	}

	public boolean isMouseOnButtonI(int x, int y) {
		if(x > this.x + 12 / 2 && x < this.x + 90 && y > this.y && y < this.y + 18) {
			return true;
		}
		return false;
	}
}
