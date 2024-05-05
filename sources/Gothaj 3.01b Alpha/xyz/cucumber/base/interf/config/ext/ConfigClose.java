package xyz.cucumber.base.interf.config.ext;

import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;

public class ConfigClose extends ConfigButton{

	private int animation;
	
	public ConfigClose(int id,double x, double y, double width, double height) {
		this.position = new PositionUtils(x,y,width,height,1);
		this.id = id;
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		

		if(this.position.isInside(mouseX, mouseY)) {
			animation = (animation * 9 + 80) /10;
		}else {
			animation = (animation * 9 + 50) /10;
		}
		RenderUtils.drawRoundedRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), ColorUtils.getAlphaColor(0x35181818, animation), 3);
		RenderUtils.drawOutlinedRoundedRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), ColorUtils.getAlphaColor(0x35181818, animation), 3, 1);
		RenderUtils.cross(this.position.getX()+this.position.getWidth()/2, this.position.getY()+this.position.getHeight()/2, this.position.getHeight()/2-5, 2, 0xffaaaaaa);
		
	}

	@Override
	public void onClick(int mouseX, int mouseY, int b) {
		// TODO Auto-generated method stub
		
	}
}
