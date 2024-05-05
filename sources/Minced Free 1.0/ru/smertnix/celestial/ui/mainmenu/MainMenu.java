package ru.smertnix.celestial.ui.mainmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.ui.altmanager.GuiAltManager;
import ru.smertnix.celestial.ui.button.GuiMainMenuButton;
import ru.smertnix.celestial.ui.changelog.ChangeLog;
import ru.smertnix.celestial.ui.changelog.ChangeManager;
import ru.smertnix.celestial.ui.particle.ParticleUtils;
import ru.smertnix.celestial.utils.math.animations.Animation;
import ru.smertnix.celestial.utils.math.animations.impl.DecelerateAnimation;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;
import ru.smertnix.celestial.utils.render.animbackground;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import javafx.animation.Interpolator;

import java.awt.*;
import java.io.IOException;

public class MainMenu extends GuiScreen {
	private int width;
    public float scale = 2;
    private int height;
    
    private long initTime = System.currentTimeMillis();
    private Animation initAnimation;
    private animbackground backgroundShader;
    public static float progress;
    private long lastMS;
    public static boolean s = true;
    public static boolean pinned;
    public MainMenu() {
    	  this.initTime = System.currentTimeMillis();
    	  try {
              this.backgroundShader = new animbackground("/assets/minecraft/celestial/shaders/background.fsh");
          }
          catch (IOException var2) {
              throw new IllegalStateException("Failed to load backgound shader", var2);
          }
    }
    
    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }
    @Override
    public void initGui() {
    	this.progress = 0.0f;
    	
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.width = sr.getScaledWidth();
        this.height = sr.getScaledHeight();
        initAnimation = new DecelerateAnimation(300, 1);
        this.buttonList.add(new GuiMainMenuButton(5, (this.width / 2) - 100, this.height / 2 - 75, 200, 9, "1"));
        this.buttonList.add(new GuiMainMenuButton(0, (this.width / 2) - 100, this.height / 2 - 50, 200, 9, "Singleplayer"));
        this.buttonList.add(new GuiMainMenuButton(1, this.width / 2 - 100, this.height / 2 - 25, 200, 9, "Multiplayer"));
        this.buttonList.add(new GuiMainMenuButton(2, this.width / 2 - 100, this.height / 2, 200, 9, "Alt Manager"));
        this.buttonList.add(new GuiMainMenuButton(3, this.width / 2 - 100, this.height / 2 + 35, 98, 9, "Options..."));
        this.buttonList.add(new GuiMainMenuButton(4, this.width / 2 + 2, this.height / 2 + 35, 98, 9, "Quit Game"));


    }
    
    
    public static double animate(double target, double current, double speed) {
        boolean bl = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }

        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }

        current = bl ? current + factor : current - factor;
        return current;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
      /*  GlStateManager.disableCull();
        this.backgroundShader.useShader(sr.getScaledWidth() + 800, sr.getScaledHeight(), mouseX, mouseY, (float) (System.currentTimeMillis() - this.initTime) /
                1000.0f);
        GL11.glBegin(7);
        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glVertex2f(-1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, -1.0f);
        GL11.glEnd();
        GL20.glUseProgram(0);

        GlStateManager.disableCull();*/
    	RenderUtils.drawImage(new ResourceLocation("celestial/pesun3.jpg"), 0,0,sr.getScaledWidth() + 5, sr.getScaledHeight(), new Color(155,155,155));



    	GlStateManager.pushMatrix();
    	
    	GL11.glScaled(0.125f, 0.125,0);
    	  GL11.glDisable(GL11.GL_DEPTH_TEST);
          GL11.glEnable(GL11.GL_BLEND);
          GL11.glDepthMask(false);
          RenderUtils.drawImage(new ResourceLocation("celestial/images/atomwhite.png"), (sr.getScaledWidth() / 2 - 20) * 8,(sr.getScaledHeight()/ 2 - 120) * 8, 33 * 8, 33 * 8, new Color(0,0,0));

      	RenderUtils.drawImage(new ResourceLocation("celestial/images/atomwhite.png"), (sr.getScaledWidth() / 2 - 20) * 8,(sr.getScaledHeight()/ 2 - 121.5f) * 8, 33 * 8, 33 * 8, new Color(-1));

          GL11.glDepthMask(true);
          GL11.glDisable(GL11.GL_BLEND);
          GL11.glEnable(GL11.GL_DEPTH_TEST);

    	GlStateManager.popMatrix();
    	boolean a;
    	a = mouseX < 207 && mouseY < 107;
    	
    	progress = (float)Interpolator.LINEAR.interpolate((double)this.progress, pinned ? 300 : a ? 300 : 0, 0.3);
    	
    	int y2 = 29;
    	for (ChangeLog c : ChangeManager.changeLogs) {
    		y2 += 10;
    	}
    	int s2 = y2;
    	
    	if (s) {
    		RenderUtils.drawShadow(10,1, () -> {
    			RoundedUtil.drawRound(progress + 7 - 300, 7, 200,s2 + 8,3,new Color(-1));
            });
            RenderUtils.drawBlur(10, () -> {
            	RoundedUtil.drawRound(progress + 7 - 300, 7, 200,s2 + 8,3,new Color(-1));
            });
    	} else {
    		RoundedUtil.drawRound(progress + 7 - 300, 7, 200,y2 + 8,0,new Color(0,0,0,100));
    	}
    	
    	RenderUtils.drawBlurredShadow(progress + 7 - 100 - 8, 9, 5, 5, 7, pinned ? new Color(255,0,255,155) : new Color(255,255,255,155));
    	
    	RoundedUtil.drawRound(progress + 7 - 100 - 8.5f, 8.5f, 6, 6, 2.8f, new Color(0,0,0,255));
    	
    	RoundedUtil.drawRound(progress + 7 - 100 - 8, 9, 5, 5, 2.3f, pinned ? new Color(255,0,255,255) : new Color(255,255,255,255));
    
    	
    	
    	
    	mc.mntsb_25.drawStringWithShadow("ChangeLog", progress + 7 - 300 + 10, 8 + 4, -1);
    	
    	float x = progress + 7 - 300 + 10;
    	float y = 10;
    	int y3 = 30;
    	mc.mntsb_16.drawStringWithShadow(TextFormatting.GRAY + "----- Version #2.2.0 (February 11, 2023) -----", x, 29, -1);
    	for (ChangeLog c : ChangeManager.changeLogs) {

        	RenderUtils.drawBlurredShadow(x, y + y3, 5, 5, 7, c.getTypeColor());
        	
        	RoundedUtil.drawRound(x - .5f, y + y3 - .5f, 6, 6, 2.8f, new Color(0,0,0,255));
        	
        	RoundedUtil.drawRound(x, y + y3, 5, 5, 2.3f, c.getTypeColor());
        
    		Minecraft.getMinecraft().mntsb_15.drawStringWithShadow(c.getLogName(), x + 8, y + y3 + 0.5f, -1);
    		y3 += 10;
    	}super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    	if (mouseButton == 0 && mouseX > progress + 7 - 100 - 8 && mouseY > 9 && mouseX < progress + 7 - 100 - 8 + 7 && mouseY < 9 + 7) {
    		pinned = !pinned;
    		Celestial.pin = pinned;
    	}
    }

    public void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiWorldSelection(this));
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiAltManager());
                break;
            case 3:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 4:
                Celestial.instance.configManager.saveConfig("default");
                Celestial.instance.fileManager.saveFiles();
                System.exit(0);
                break;
            case 5:
            	s = !s;
            	Celestial.shaders = s;
                break;
        }

        super.actionPerformed(button);
    }
}