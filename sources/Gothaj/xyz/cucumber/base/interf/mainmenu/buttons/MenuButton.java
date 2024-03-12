package xyz.cucumber.base.interf.mainmenu.buttons;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class MenuButton {
	
	private String name;
	
	private PositionUtils position;
	
	private double imageAnimation, textAnimation;
	
	private float startTime;
	
	private int id;
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	public MenuButton(String name,PositionUtils position, int id) {

		this.position = position;
		this.id = id;
		this.name = name;
	}
	
	public void draw(int mouseX, int mouseY) {
		if(position.isInside(mouseX, mouseY)) {
			imageAnimation = (imageAnimation * (4) + 3)/5;
			textAnimation = (textAnimation * 9 + 3) /10;
		}else {
			imageAnimation = (imageAnimation * (4))/5;
			textAnimation = (textAnimation * 9) /10;
		}
		GL11.glPushMatrix();
		GL11.glTranslated(this.position.getX()+this.position.getWidth()/2-Fonts.getFont("rb-m").getWidth(name)/2*1.05, this.position.getY()+5, 0);
		GL11.glScaled(1.05,1.05, 1);
		Fonts.getFont("rb-m").drawString(name, -(0.5), 0.5, 0x90000000);
		Fonts.getFont("rb-m").drawString(name, 0,0, ColorUtils.mix(0xffffffff, 0xffaaaaaa, textAnimation,3));
		GL11.glPopMatrix();
		
	}
	public void clicked(int mouseX, int mouseY, int button) {
		
	}
	
	public PositionUtils getPosition() {
		return position;
	}

	public void setPosition(PositionUtils position) {
		this.position = position;
	}

	public int getId() {
		
		return id;
	}
	
	
	
}
