package net.minecraft.client.gui;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.ui.altmanager.GuiAltManager;
import ru.smertnix.celestial.ui.button.GuiMainMenuButton;
import ru.smertnix.celestial.ui.mainmenu.MainMenu;
import ru.smertnix.celestial.utils.math.animations.Animation;
import ru.smertnix.celestial.utils.math.animations.impl.DecelerateAnimation;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.animbackground;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GuiMainMenu extends GuiScreen {
	private int width;
    public float scale = 2;
    private int height;
    
    private long initTime = System.currentTimeMillis();
    private Animation initAnimation;
    private final animbackground backgroundShader;
    public static float progress;
    private long lastMS;
    public GuiMainMenu() {
    	  this.initTime = System.currentTimeMillis();
    	 try {
             this.backgroundShader = new animbackground("/assets/minecraft/celestial/shaders/shader.fsh");
         }
         catch (IOException var2) {
             throw new IllegalStateException("Failed to load backgound shader", var2);
         }
  }
    
    @Override
    public void initGui() {
    	  this.mc.displayGuiScreen(new MainMenu());
        ScaledResolution sr = new ScaledResolution(this.mc);
      	this.lastMS = System.currentTimeMillis();
        this.width = sr.getScaledWidth();
        this.height = sr.getScaledHeight();
        initAnimation = new DecelerateAnimation(300, 1);
        this.buttonList.add(new GuiMainMenuButton(0, (this.width / 2) - 250, this.height / 2 + 223, 90, 15, "Singleplayer"));
        this.buttonList.add(new GuiMainMenuButton(1, this.width / 2 - 150, this.height / 2 + 223, 90, 15, "Multiplayer"));
        this.buttonList.add(new GuiMainMenuButton(2, this.width / 2 - 50, this.height / 2 + 223, 90, 15, "Alt Manager"));
        this.buttonList.add(new GuiMainMenuButton(3, this.width / 2 + 50, this.height / 2 + 223, 90, 15, "Options"));
        this.buttonList.add(new GuiMainMenuButton(4, this.width / 2 + 150, this.height / 2 + 223, 90, 15, "Quit"));


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

        ScaledResolution res = new ScaledResolution(mc);
        int u2 = 0;
        int d2 = 0;
        int r2 = 0;
        int l2 = 0;
        int u = -20 + mouseY / 50;
        int d = res.getScaledHeight() + 20 + mouseY / 30;
        int r = res.getScaledWidth() + 20 + mouseX / 30;
        int l = -20 + mouseX / 50;
        u2 = (int)animate((double)u, (double)u2, 3.0 * Celestial.deltaTime());
        d2 = (int)animate((double)d, (double)d2, 3.0 * Celestial.deltaTime());
        r2 = (int)animate((double)r, (double)r2, 3.0 * Celestial.deltaTime());
        l2 = (int)animate((double)l, (double)l2, 3.0 * Celestial.deltaTime());

        GlStateManager.disableCull();
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.backgroundShader.useShader((int) (sr.getScaledWidth()), (int) (sr.getScaledHeight() * progress), mouseX * progress, mouseY * progress, (float) (System.currentTimeMillis() - this.initTime) / 1500.0f);
        GL11.glBegin(7);
        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glVertex2f(-1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, -1.0f);
        GL11.glEnd();
        GL20.glUseProgram(0);

        GlStateManager.disableCull();


       //Gui.drawRect(0, height / 2 + 213, width / 2 + 500, height / 2 + 215, ClientHelper.getClientColor().getRGB());
        //ParticleUtils.drawParticles(Mouse.getX() * this.width / this.mc.displayWidth, this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1);

        //mc.rubik_18.drawStringWithOutline("������ ������� - " + Rich.instance.version, 2.0F, (float) (sr.getScaledHeight() - mc.neverlose900_16.getFontHeight() - 2), -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
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
                System.exit(0);
                Celestial.instance.configManager.saveConfig("default");
                Celestial.instance.fileManager.saveFiles();
                break;
        }

        super.actionPerformed(button);
    }
}