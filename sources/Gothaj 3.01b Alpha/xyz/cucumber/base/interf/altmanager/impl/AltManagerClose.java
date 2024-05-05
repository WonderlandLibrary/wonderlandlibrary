package xyz.cucumber.base.interf.altmanager.impl;

import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;

public class AltManagerClose extends AltManagerButton{

	private int animation;
	
	public AltManagerClose(int id,double x, double y, double width, double height) {
		this.position = new PositionUtils(x,y,width,height,1);
		this.id = id;
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		

		if(this.position.isInside(mouseX, mouseY)) {
			animation = (animation * 9 + 50) /10;
		}else {
			animation = (animation * 9 + 30) /10;
		}
		RenderUtils.drawRoundedRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), ColorUtils.getAlphaColor(0x35aaaaaa, animation), 3);
		RenderUtils.cross(this.position.getX()+this.position.getWidth()/2, this.position.getY()+this.position.getHeight()/2, this.position.getHeight()/2-5, 2, 0xffaaaaaa);
		
	}
}
