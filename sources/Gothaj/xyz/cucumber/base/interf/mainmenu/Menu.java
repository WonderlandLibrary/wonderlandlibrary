package xyz.cucumber.base.interf.mainmenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.interf.altmanager.AltManager;
import xyz.cucumber.base.interf.mainmenu.buttons.MenuButton;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.BlurUtils;
import xyz.cucumber.base.utils.render.Fonts;
import xyz.cucumber.base.utils.render.Particle;

public class Menu extends GuiScreen{
	
	private List<MenuButton> buttons = new ArrayList();
	private List<Particle> particles = new ArrayList();
	
	private float startTime;
	
	public AltManager altmanager;	
	@Override
	public void initGui() {
		buttons.clear();
		particles.clear();
		
		//init button
		startTime = System.nanoTime();
		altmanager = new AltManager(this);
		
		buttons.add(new MenuButton("Single Player", new PositionUtils(this.width/2-50, this.height/2-80, 100, 20,1), 1));
		buttons.add(new MenuButton("Multi Player", new PositionUtils(this.width/2-50, this.height/2-60, 100, 20,1), 2));
		buttons.add(new MenuButton("Alt Manager", new PositionUtils(this.width/2-50, this.height/2-40, 100, 20,1), 3));
		buttons.add(new MenuButton("Settings", new PositionUtils(this.width/2-50, this.height/2-20, 100, 20,1), 4));
		buttons.add(new MenuButton("Exit", new PositionUtils(this.width/2-50, this.height/2, 100, 20,1), 5));
	}



	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		RenderUtils.drawRect(0, 0, this.width, this.height, 0xff000000);
		RenderUtils.drawOtherBackground(0, 0, this.width, this.height,(System.nanoTime()-startTime)/1000000000F);
		if(particles.size() < 200) {
			int needed = 200-particles.size();
			for(int i = 0; i < needed; i++) {
				particles.add(new Particle(RandomUtils.nextInt(0, this.width), RandomUtils.nextInt(0, this.height), RandomUtils.nextInt(2, 4)/2,2, 0xffffffff, RandomUtils.nextInt(0, 360), RandomUtils.nextInt(1000, 3000)));
			}
		}
		
		
		Iterator<Particle> iterator = particles.iterator();
		while (iterator.hasNext()) {
			boolean b = iterator.next().draw();
		    if (b) {
		        iterator.remove();
		    }
		}
		
		for(Particle p : particles) {
			double diffX = p.getX() - mouseX;
			double diffY = p.getY() - mouseY;
			double dist = Math.sqrt(diffX*diffX+diffY*diffY);
			if(dist < 50) {
				RenderUtils.drawLine(p.getX(), p.getY(), mouseX, mouseY, 0x20ffffff, (float) 0.5);
			}
			for(Particle p2 : particles) {
				if(p2 == p) continue;
				double difX = p.getX() - p2.getX();
				double difY = p.getY() - p2.getY();
				double dit = Math.sqrt(difX*difX+difY*difY);
				if(dit < 30) {
					RenderUtils.drawLine(p.getX(), p.getY(), p2.getX(), p2.getY(), 0x05ffffff, (float) 0.5);
				}
			}
		}
		Fonts.getFont("rb-b-h").drawString("Gothaj", width/2-Fonts.getFont("rb-b-h").getWidth("Gothaj")/2+0.5, this.height/2-90-20+0.5, 0x90000000);
		Fonts.getFont("rb-b-h").drawString("Gothaj", width/2-Fonts.getFont("rb-b-h").getWidth("Gothaj")/2, this.height/2-90-20, 0xffcccccc);
		for(MenuButton b : buttons) {
			b.draw(mouseX, mouseY);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}



	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for(MenuButton b : buttons) {
			if(b.getPosition().isInside(mouseX, mouseY)) {
				switch(b.getId()) {
				case 1:
					mc.displayGuiScreen(new GuiSelectWorld(this));
					break;
				case 2:
					mc.displayGuiScreen(new GuiMultiplayer(this));
					break;
				case 3:
					mc.displayGuiScreen(altmanager);
					return;
				case 5:
					this.mc.shutdown();
					return;
				case 4:
					mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
					break;
				}
			}
		}
			
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
