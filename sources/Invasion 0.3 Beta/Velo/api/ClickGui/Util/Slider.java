package Velo.api.ClickGui.Util;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import Velo.api.Util.Render.RenderUtil;
import Velo.api.Util.Render.ScaledRes;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;


public class Slider extends Component {



	private NumberSetting op;



	

	private boolean hovered;
	
	private int x;
	private int y;
	private boolean dragging = false;

	private double renderWidth;
	public Slider(NumberSetting option, int x, int y) {
		this.op = option;
		this.x = x;
		this.y = y;
	
	}


	
	@Override
	public void renderComponent() {
		
	//	Gui.drawRect(x + 2, y + 10, x + 10, y + 10 + 12, this.hovered ? 0xFF222222 : 0xFF111111);
		 final int drag = (int)(this.op.getValue() / this.op.maximum * 98);
	//	Gui.drawRect(x + 2,y + 5, x + (int) renderWidth,y + 5 + 12,hovered ? 0xFF555555 : 0xFF444444);
		//Gui.drawRect(x,y + 5, x + 2,y + 5 + 12, 0xFF111111);
			double numberbar = (op.getValue() - op.getMinimum())/(op.getMaximum() - op.getMinimum()) * 98;
		Gui.drawRect(x , y + 3, x+ 100, y + 5, 0xFF111111);
		Gui.drawRect(x, y + 3, x  + (int) renderWidth + 100, y + 5, hovered ? 0xFF555555 : 0xFF444444);
		Gui.drawRect(x, y + 3, x + numberbar, y + 5, 0xff116699);
		//Gui.drawRect(ScaledRes.getScaledWidth()/2f - 210, ScaledRes.getScaledHeight()/2f + count3*16 - 157, ScaledRes.getScaledWidth()/2f + 52, ScaledRes.getScaledHeight()/2f + count3*16 - 144, 0x901f1f1f);
		//Gui.drawRect(ScaledRes.getScaledWidth()/2f - 210, ScaledRes.getScaledHeight()/2f + count3*16 - 157, (float) (ScaledRes.getScaledWidth()/2f - 210 + (numberbar * 262)), ScaledRes.getScaledHeight()/2f + count3*16 - 144, 0xff0090ff);

		RenderUtil.drawFilledCircle((int) (x  + (int) renderWidth + numberbar), y + 4, 2, Color.WHITE);
		
		Fonts.targethud.drawString(this.op.getName() + ": " + this.op.getValue() , x, y + 6, -1);
		
		
	}
	

	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButtonD(mouseX, mouseY);

		
		//double diff = Math.min(88, Math.max(0, mouseX - this.x + 100));
		double diff = op.getMaximum() - op.getMinimum();
		double min = op.getMinimum();
		double max = op.getMaximum();
		double val4 = op.getMinimum() + (MathHelper.clamp_double((mouseX - ScaledRes.getScaledWidth()/2f + 209) / (ScaledRes.getScaledWidth()/2f - 389), 0, 1)) * diff;
		
		renderWidth = (88) *  (op.getValue() - op.getMinimum());
		
		if (dragging) {
			if (diff == 0) {
				op.setValue(op.getMinimum());
			}
			else {
				double newValue = roundToPlace(((diff / 88) * (max - min) + min), 2);
				op.setValue(val4);
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
		if(x > this.x - 100 && x < this.x - 12 && y > this.y + 23 && y < this.y + 31) {
			return true;
		}
		return false;
	}

	public boolean isMouseOnButtonI(int x, int y) {
		if(x > this.x - 100 && x < this.x - 12 && y > this.y + 23 && y < this.y + 31) {
			return true;
		}
		return false;
	}	
	}
