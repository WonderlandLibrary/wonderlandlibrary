package me.aidanmees.trivia.client.minimeme;
import java.awt.Font;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import io.netty.util.internal.ThreadLocalRandom;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.tools.FontUtils;
import me.aidanmees.trivia.client.tools.Timer1;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.CustomButton.GuiButtonDark;
import net.minecraft.client.gui.CustomButton.SimpleResourceButton;
import net.minecraft.util.ResourceLocation;

public final class CookieClicker
extends GuiScreen {
    private final GuiScreen previousScreen;
    private GuiTextField username;
    private int clickcount = 0;
    Timer1 time = new Timer1();
    boolean canDo = false;
    FontUtils fu_default = new FontUtils("Audiowide", Font.PLAIN, 20);
    boolean mouse;
    boolean previousmouse;
    int helper = 0;

    public CookieClicker(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiButtonDark(1, width / 2 - 50, height / 2 + 100, 98, 20, "Back"));
        Gui.drawRect(0.0, 0.0, 0.0, 0.0, -1);
        this.buttonList.add(new SimpleResourceButton(88, width / 2 - 70, height / 2 - 80, 140, 140, "", new ResourceLocation("trivia/cookie.png")));
        Gui.drawRect(0.0, 0.0, 0.0, 0.0, -1);
        Keyboard.enableRepeatEvents((boolean)true);
    }
    @Override
    public void initGui(){
    	 int var3 = height / 4 + 48;
    	this.addSingleplayerMultiplayerButtons(var3, 24);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.previousScreen);
        }
        if (button.id == 88) {
        	canDo = true;
        
            ++this.clickcount;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0.0, 0.0, 0.0, 0.0, -1);
        ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        ScaledResolution scaledRes1 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        
            this.mc.getTextureManager().bindTexture(trivia.triviaImage2);
        
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, scaledRes1.getScaledWidth(), scaledRes1.getScaledHeight(), scaledRes1.getScaledWidth(), scaledRes1.getScaledHeight(), scaledRes1.getScaledWidth(), scaledRes1.getScaledHeight());
        
        fu_default.drawString("Clicks: " + this.clickcount, (float)(width / 2) - fu_default.getStringWidth("Clicks: " + this.clickcount) / 2.0f, 30.0f, 0x0ffffff);
        Gui.drawRect(0.0, 0.0, 0.0, 0.0, -1);
        if (canDo) {
            this.onCookieClicker();
            canDo = false;
            
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void drawCenteredString(FontRenderer fontRendererIn, String text, float f, float g, int color) {
        fontRendererIn.drawStringWithShadow(text, f - (float)(fontRendererIn.getStringWidth(text) / 2), g, color);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getRandomIntInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private void onCookieClicker() {
        
            fu_default.drawString("+1", width / 2 + this.getRandomIntInRange(-200, 200), height / 2 + this.getRandomIntInRange(-80, 80), -1);
       
        
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        try {
            super.mouseClicked(x, y, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }
}

