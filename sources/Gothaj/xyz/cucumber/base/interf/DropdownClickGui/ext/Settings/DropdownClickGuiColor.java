package xyz.cucumber.base.interf.DropdownClickGui.ext.Settings;

import java.awt.Color;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModuleSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class DropdownClickGuiColor extends DropdownClickGuiSettings{
	
	private ColorSettings setting;
	
	private PositionUtils color1 = new PositionUtils(0,0,50,50,1);
	private PositionUtils hue1 = new PositionUtils(0,0,50,4,1);
	
	double h1 = 0,h2 = 0;
	
	private double[] radiusFromTo = new double[] {
		37,43	
	};
	
	private boolean open;
	
	private double defaultheight = 12;
	private PositionUtils mode = new PositionUtils(0,0,100,10, 0);
	private PositionUtils color2 = new PositionUtils(0,0,50,50,1);
	private PositionUtils hue2 = new PositionUtils(0,0,50,4,1);
	
	private PositionUtils alpha = new PositionUtils(0,0,50,4,1);
	
	private PositionUtils color1Point = new PositionUtils(0,0,4,4,1);
	private PositionUtils color2Point = new PositionUtils(0,0,4,4,1);
	
	private PositionUtils hue1Point = new PositionUtils(0,0,4,4,1);
	private PositionUtils hue2Point = new PositionUtils(0,0,4,4,1);
	
	private PositionUtils alphaPoint = new PositionUtils(0,0,4,4,1);
	
	
	
	public DropdownClickGuiColor(ModuleSettings setting,PositionUtils positionUtils) {
		this.setting = (ColorSettings) setting;
		this.position = positionUtils;
		this.mainSetting =setting;
		Color c = new Color(this.setting.getMainColor());
		h1 = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null)[0];
		Color c1 = new Color(this.setting.getMainColor());
		h2 = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null)[0];
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		Fonts.getFont("rb-m-13").drawString(setting.getName(), this.position.getX()+3, this.position.getY()+3, -1);
		RenderUtils.drawMixedRoundedRect(this.position.getX2()-20,this.position.getY()+2,this.position.getX2()-5,this.position.getY()+defaultheight-2, setting.getMainColor(), setting.getSecondaryColor(),2);
		if(open) {
			this.getPosition().setHeight(150);
			this.color1.setX(this.getPosition().getX()+25);
			this.color1.setY(this.getPosition().getY()+defaultheight);
			this.hue1.setX(this.getPosition().getX()+25);
			this.hue1.setY(this.getPosition().getY()+defaultheight+50);

			this.color2.setX(this.getPosition().getX()+25);
			this.color2.setY(this.getPosition().getY()+defaultheight+60);
			this.hue2.setX(this.getPosition().getX()+25);
			this.hue2.setY(this.getPosition().getY()+defaultheight+110);
			
			this.alpha.setX(this.getPosition().getX()+25);
			this.alpha.setY(this.getPosition().getY()+defaultheight+110+10);
			
			RenderUtils.drawColorPicker(this.color1.getX(), this.color1.getY(), this.color1.getWidth(),this.color1.getHeight(), (float) h1);
			RenderUtils.drawColorSlider(this.hue1.getX(), this.hue1.getY(), this.hue1.getWidth(),this.hue1.getHeight());
			
			RenderUtils.drawColorPicker(this.color2.getX(), this.color2.getY(), this.color2.getWidth(),this.color2.getHeight(), (float) h2);
			RenderUtils.drawColorSlider(this.hue2.getX(), this.hue2.getY(), this.hue2.getWidth(),this.hue2.getHeight());
			
			RenderUtils.drawImage(this.alpha.getX(), this.alpha.getY(), this.alpha.getWidth(),this.alpha.getHeight(), new ResourceLocation("client/images/alpha.png"), -1);
			GlStateManager.pushMatrix();
			
			RenderUtils.start2D();
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glBegin(GL11.GL_QUADS);
			
			
			RenderUtils.color(0xffff0000);
			GL11.glVertex2d(this.alpha.getX(), this.alpha.getY()-1);
			RenderUtils.color(0x00ff0000);
			GL11.glVertex2d(this.alpha.getX2(), this.alpha.getY()-1);
			GL11.glVertex2d(this.alpha.getX2(), this.alpha.getY2());
			RenderUtils.color(0xffff0000);
			GL11.glVertex2d(this.alpha.getX(), this.alpha.getY2());
			
			
			GL11.glEnd();
			
			RenderUtils.stop2D();
			
			GlStateManager.popMatrix();
			
			Color c = new Color(setting.getMainColor());
			float[] a = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
			
			color1Point.setX(color1.getX()+color1.getWidth()*a[1]-color1Point.getWidth()/2);
			color1Point.setY(color1.getY()+color1.getHeight()*(1-a[2])-color1Point.getHeight()/2);
			RenderUtils.drawRoundedRect(color1Point.getX()-0.5,color1Point.getY()-0.5, color1Point.getX2()+0.5, color1Point.getY2()+0.5,-1, 1f);
			RenderUtils.drawRoundedRect(color1Point.getX(),color1Point.getY(), color1Point.getX2(), color1Point.getY2(),c.getRGB(), 1f);
			
			hue1Point.setX(hue1.getX()+hue1.getWidth()*h1-hue1Point.getHeight()/2);
			hue1Point.setY(hue1.getY());
			RenderUtils.drawRoundedRect(hue1Point.getX()-1,hue1Point.getY()-1, hue1Point.getX2()+1, hue1Point.getY2()+1,-1, 1f);
			RenderUtils.drawRoundedRect(hue1Point.getX()-0.5,hue1Point.getY()-0.5, hue1Point.getX2()+0.5, hue1Point.getY2()+0.5,Color.HSBtoRGB((float) h2, 1, 1), 1f);
			
			c = new Color(setting.getSecondaryColor());
			a = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
			
			color2Point.setX(color2.getX()+color2.getWidth()*a[1]-color2Point.getWidth()/2);
			color2Point.setY(color2.getY()+color2.getHeight()*(1-a[2])-color2Point.getHeight()/2);
			RenderUtils.drawRoundedRect(color2Point.getX()-0.5,color2Point.getY()-0.5, color2Point.getX2()+0.5, color2Point.getY2()+0.5,-1, 1f);
			RenderUtils.drawRoundedRect(color2Point.getX(),color2Point.getY(), color2Point.getX2(), color2Point.getY2(),c.getRGB(), 1f);
			
			hue2Point.setX(hue2.getX()+hue2.getWidth()*h2-hue2Point.getHeight()/2);
			hue2Point.setY(hue2.getY());
			RenderUtils.drawRoundedRect(hue2Point.getX()-1,hue2Point.getY()-1, hue2Point.getX2()+1, hue2Point.getY2()+1,-1, 1f);
			RenderUtils.drawRoundedRect(hue2Point.getX()-0.5,hue2Point.getY()-0.5, hue2Point.getX2()+0.5, hue2Point.getY2()+0.5,Color.HSBtoRGB((float) h2, 1, 1), 1f);
			
			
			if(color1.isInside(mouseX, mouseY)) {
				for(double x = color1.getX(); x < color1.getX2(); x+=0.5) {
					for(double y = color1.getY(); y < color1.getY2(); y+=0.5) {
						if(mouseX > x && mouseX < x+1 && mouseY > y && mouseY < y+1 && Mouse.isButtonDown(0)) {
							double diffX = x -color1.getX();
							double diffY = color1.getY2()-y;
							setting.setMainColor(Color.HSBtoRGB((float) h1, (float)(diffX/color1.getWidth()), (float)(diffY/color1.getHeight())));
						}
					}
				}
			}
			
			if(color2.isInside(mouseX, mouseY)) {
				for(double x = color2.getX(); x < color2.getX2(); x+=0.5) {
					for(double y = color2.getY(); y < color2.getY2(); y+=0.5) {
						if(mouseX > x && mouseX < x+1 && mouseY > y && mouseY < y+1 && Mouse.isButtonDown(0)) {
							double diffX = x -color2.getX();
							double diffY = color2.getY2()-y;
							setting.setSecondaryColor(Color.HSBtoRGB((float) h2, (float)(diffX/color1.getWidth()), (float)(diffY/color1.getHeight())));
						}
					}
				}
			}
			if(hue1.isInside(mouseX, mouseY)) {
				for(double x = hue1.getX(); x < hue1.getX2(); x+=0.5) {
					if(mouseX > x && mouseX < x+1 && mouseY > hue1.getY() && mouseY < hue1.getY2() && Mouse.isButtonDown(0)) {
						double diffX = x -hue1.getX();
						
						h1 = (float) (diffX / hue1.getWidth());
					}
				}
			}
			if(hue2.isInside(mouseX, mouseY)) {
				for(double x = hue2.getX(); x < hue2.getX2(); x+=0.5) {
					if(mouseX > x && mouseX < x+1 && mouseY > hue2.getY() && mouseY < hue2.getY2() && Mouse.isButtonDown(0)) {
						double diffX = x -hue2.getX();
						
						h2 = (float) (diffX / hue2.getWidth());
					}
				}
			}
			if(alpha.isInside(mouseX, mouseY)) {
				for(double x = alpha.getX(); x < alpha.getX2(); x+=0.5) {
					if(mouseX > x && mouseX < x+1 && mouseY > alpha.getY() && mouseY < alpha.getY2() && Mouse.isButtonDown(0)) {
						double diffX = x -alpha.getX();
						
						
						setting.setAlpha((int) ((alpha.getWidth()-diffX) / alpha.getWidth() * 100));
					}
				}
			}
			this.mode.setX(this.getPosition().getX());
			this.mode.setY(this.getPosition().getY()+140);
			
			Fonts.getFont("rb-m-13").drawString("Mode", this.mode.getX()+7, this.mode.getY()+3, -1);
			
			RenderUtils.drawRoundedRectWithCorners(this.mode.getX()+Fonts.getFont("rb-m-13").getWidth("Mode")+5-1+4, this.mode.getY()+this.mode.getHeight()/2-4,  this.mode.getX()+4+Fonts.getFont("rb-m-13").getWidth("Mode")+6+Fonts.getFont("rb-m-13").getWidth(setting.getMode()),this.mode.getY()+this.mode.getHeight()/2+4, 0xff343a40, 2, true,true,true,true);
			Fonts.getFont("rb-m-13").drawString(setting.getMode(), this.mode.getX()+4+Fonts.getFont("rb-m-13").getWidth("Mode")+5.5, this.mode.getY()+3, -1);
		}else {
			this.getPosition().setHeight(defaultheight);
		}
		
	}

	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		if(button == 1) {
			open = !open;
		}
		if(button == 0) {
			if(mode.isInside(mouseX, mouseY)) {
				setting.cycleModes();
			}
		}
	}

	@Override
	public void onRelease(int mouseX, int mouseY, int button) {
		
	}
	
	public void drawHueCircle(double centerX, double centerY) {
		GlStateManager.pushMatrix();
		
		RenderUtils.start2D();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glBegin(GL11.GL_QUADS);
		
		for(double i = 0; i <= 360; i+=1) {
			double x = Math.sin(i * Math.PI / 180);
			double y = Math.cos(i * Math.PI / 180);
			RenderUtils.color(Color.HSBtoRGB((float) (i/360F), 1f,1f));
			GL11.glVertex2d(centerX+x*radiusFromTo[0], centerY-y*radiusFromTo[0]);
			GL11.glVertex2d(centerX+x*radiusFromTo[1], centerY-y*radiusFromTo[1]);
		}
		
		GL11.glEnd();
		
		RenderUtils.stop2D();
		
		GlStateManager.popMatrix();
	}
	
}
