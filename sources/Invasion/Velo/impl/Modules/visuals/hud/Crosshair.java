package Velo.impl.Modules.visuals.hud;

import java.awt.Color;
import java.util.ArrayList;

import Velo.api.Module.Module;
import Velo.api.Module.Module.Category;
import Velo.api.Util.Render.RenderUtil;
import Velo.impl.Event.EventRender;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;

public class Crosshair extends Module{
	
	  public NumberSetting x;
	  
	  public NumberSetting y;
	  
	  public ModeSetting mode = new ModeSetting("Mode", "Normal", "Normal", "Dot", "Square");

	public Crosshair() {
		super("Crosshair", "Crosshair", 0, Category.VISUALS);
		loadSettings(mode);
	

	}
	
	@Override
	public void onRenderUpdate(EventRender event) {
		 float wMiddle = event.getWidth() / 2;
	        float hMiddle = event.getHeight() / 2 + 3;
	        float hMiddle2 = event.getHeight() / 2;
	 if(this.mode.equalsIgnorecase("Normal")) {
		// Left
     RenderUtil.drawBorderedRect(wMiddle - 7, hMiddle - 3, wMiddle - 2, hMiddle - 2, 0.5f, Color.WHITE.getRGB(), Color.BLACK.getRGB(), false);
     // Right
     RenderUtil.drawBorderedRect(wMiddle + 3, hMiddle - 3, wMiddle + 8, hMiddle - 2, 0.5f, Color.WHITE.getRGB(), Color.BLACK.getRGB(), false);
     // Top
     RenderUtil.drawBorderedRect(wMiddle, hMiddle - 10, wMiddle + 1, hMiddle - 5, 0.5f, Color.WHITE.getRGB(), Color.BLACK.getRGB(), false);
     // Bottom
     RenderUtil.drawBorderedRect(wMiddle, hMiddle, wMiddle + 1, hMiddle + 5, 0.5f, Color.WHITE.getRGB(), Color.BLACK.getRGB(), false);
	 }else if(this.mode.equalsIgnorecase("Dot")) {
	        RenderUtil.drawBorderedRect(wMiddle, hMiddle2, wMiddle ,
	                hMiddle, 1f, Color.WHITE.getRGB(), Color.BLACK.getRGB(), false);
	 }
	
		super.onRenderUpdate(event);
	}
	

	
}
	

	

