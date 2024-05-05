package xyz.cucumber.base.interf.altmanager.impl;

import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class AltManagerClickable extends AltManagerButton{

	private String text;
	
	private int animation;
	
	public AltManagerClickable(int id,String text,double x, double y, double width, double height) {
		this.position = new PositionUtils(x,y,width,height,1);
		this.id = id;
		this.text = text;
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		
		if(this.position.isInside(mouseX, mouseY)) {
			animation = (animation * 9 + 50) /10;
		}else {
			animation = (animation * 9 + 30) /10;
		}
		RenderUtils.drawRoundedRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), ColorUtils.getAlphaColor(0x35aaaaaa, animation), 2);
		Fonts.getFont("rb-m").drawString(text, this.position.getX()+this.position.getWidth()/2-Fonts.getFont("rb-m").getWidth(text)/2, this.position.getY()+this.position.getHeight()/2-Fonts.getFont("rb-m").getHeight(text)/2-1, 0xffaaaaaa);
	}

}
