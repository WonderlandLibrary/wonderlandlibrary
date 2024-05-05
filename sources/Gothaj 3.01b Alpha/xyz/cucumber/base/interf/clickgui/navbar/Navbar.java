package xyz.cucumber.base.interf.clickgui.navbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.interf.clickgui.navbar.ex.CategoryButton;
import xyz.cucumber.base.interf.clickgui.navbar.ex.ClientButtons;
import xyz.cucumber.base.interf.clickgui.navbar.ex.NavbarButtons;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.BloomUtils;
import xyz.cucumber.base.utils.render.Fonts;
import xyz.cucumber.base.utils.render.StencilUtils;

public class Navbar {
	
	private PositionUtils position;
	
	public List<CategoryButton> categories = new ArrayList();
	public List<ClientButtons> client = new ArrayList();
	
	public NavbarButtons active;
	
	private double yanimation;
	BloomUtils bloom = new BloomUtils();

	public Navbar(PositionUtils position) {
		this.position = position;
		categories.clear();
		
		for(Category c : Category.values()) {
			categories.add(new CategoryButton(c, this));
		}
		active = categories.get(0);
		
		client.add(new ClientButtons("Configs", this));
	}

	public void draw(int mouseX, int mouseY) {
		
		StencilUtils.initStencil();
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        StencilUtils.bindWriteStencilBuffer();
        RenderUtils.drawRoundedRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), 0xff252725, 1);
		
		StencilUtils.bindReadStencilBuffer(1);
		
		
		RenderUtils.drawRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), 0xee252725);
		RenderUtils.drawRect(this.position.getX(), this.position.getY2()-25, this.position.getX2(), this.position.getY2(), 0xff222222);
		RenderUtils.drawRect(this.position.getY(), this.position.getY(), this.position.getX2(), this.position.getY()+25, 0xff222222);
		
		
		RenderUtils.drawCircle(this.position.getX()+14, this.position.getY()+25/2, 9, 0xff191919, 5);
		RenderUtils.drawImage(this.position.getX()+14-9, this.position.getY()+25/2-8, 18, 18, new ResourceLocation("client/images/gothaj.png"), 0xffffffff);
		
		
		Fonts.getFont("rb-b").drawString("GOTHAJ", this.position.getX()+14+18+5, this.position.getY()+25-Fonts.getFont("rb-b").getWidth("GOTHAJ")/2, 0xff4269f5);
		
		
		double i = 15;
		RenderUtils.drawRoundedRect(active.getPosition().getX(), yanimation , active.getPosition().getX2(),yanimation+active.getPosition().getHeight(), 0xff4269f5, 2);
		i += drawCategory(mouseY, mouseY, this.position.getX(), this.position.getY()+30+i, this.position.getX2())+10;
		drawClientButtons(mouseY, mouseY, this.position.getX(), this.position.getY()+30+i, this.position.getX2());
		
        StencilUtils.uninitStencilBuffer();
	}
	public void clicked(int mouseX, int mouseY, int button) {
		if(position.isInside(mouseX, mouseY)) {
			for(CategoryButton c : categories) {
				
				c.clicked(mouseX, mouseY, button);
			}
			for(NavbarButtons c : client) {
				
				c.clicked(mouseX, mouseY, button);
			}
		}
	}

	public PositionUtils getPosition() {
		return position;
	}

	public void setPosition(PositionUtils position) {
		this.position = position;
	}
	
	private double drawCategory(int mouseX, int mouseY,double startX, double startY, double endX) {
		double s = 0;
		double w = (endX-startX)-10;
		RenderUtils.drawLine(startX+5, startY, startX+5+w/2-Fonts.getFont("rb-r").getWidth("Category")/2-2, startY, 0xffaaaaaa, 1);
		RenderUtils.drawLine(startX+5+w/2+Fonts.getFont("rb-r").getWidth("Category")/2+2, startY, endX-5, startY, 0xffaaaaaa, 1);
		Fonts.getFont("rb-r").drawString("Category", startX+5+w/2-Fonts.getFont("rb-r").getWidth("Category")/2, startY-2.5, 0xffaaaaaa);
		s+= 5;
		yanimation = ( yanimation * 9 + active.getPosition().getY()) /10;
		
		for(CategoryButton c : categories) {
			c.getPosition().setX(startX+5);
			c.getPosition().setY(startY+s);
			
			c.draw(mouseX, mouseY);
			s+= c.getPosition().getHeight();
		}
		
		return s;
	}
	private double drawClientButtons(int mouseX, int mouseY,double startX, double startY, double endX) {
		double s = 0;
		double w = (endX-startX)-10;
		RenderUtils.drawLine(startX+5, startY, startX+5+w/2-Fonts.getFont("rb-r").getWidth("Client")/2-2, startY, 0xffaaaaaa, 1);
		RenderUtils.drawLine(startX+5+w/2+Fonts.getFont("rb-r").getWidth("Client")/2+2, startY, endX-5, startY, 0xffaaaaaa, 1);
		Fonts.getFont("rb-r").drawString("Client", startX+5+w/2-Fonts.getFont("rb-r").getWidth("Client")/2, startY-2.5, 0xffaaaaaa);
		s+= 5;
		yanimation = ( yanimation * 9 + active.getPosition().getY()) /10;
		
		for(ClientButtons c : client) {
			c.getPosition().setX(startX+5);
			c.getPosition().setY(startY+s);
			
			c.draw(mouseX, mouseY);
			s+= c.getPosition().getHeight();
		}
		
		return s;
	}
	
}
