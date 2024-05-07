package com.masterof13fps.features.ui.guiscreens;

import com.masterof13fps.Client;
import com.masterof13fps.Methods;
import com.masterof13fps.Wrapper;
import com.masterof13fps.manager.fontmanager.FontManager;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import com.masterof13fps.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringJoiner;

public class GuiCredits extends GuiScreen implements Wrapper, Methods {

    public GuiScreen parent;
    FontManager fM = Client.main().fontMgr();

    float scrollAmount = (-Mouse.getDWheel()) * 0.07F;
    String result;
    private GuiScreen parentScreen;

    public GuiCredits(GuiScreen parentScreen) {
        parent = parentScreen;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.add(new GuiButton(1, width / 2 - 220, height - 25, 200, 20, "Zurück"));
        buttonList.add(new GuiButton(2, width / 2 + 20, height - 25, 200, 20, "Reload"));

        Thread creditsThread = new Thread(() -> {
            try {
                URL url = new URL(getClientCredits());
                InputStreamReader isr = new InputStreamReader(url.openStream());
                BufferedReader reader = new BufferedReader(isr);
                StringJoiner sb = new StringJoiner("");
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.add(line + System.lineSeparator());
                }
                result = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        creditsThread.start();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Methods.mc.displayGuiScreen(parentScreen);
        }
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                Methods.mc.displayGuiScreen(parent);
                break;
            case 2:
                Methods.mc.displayGuiScreen(this);
                notify.debug("Reloaded Credits screen");
                break;
        }
    }

    public void drawScreen(int posX, int posY, float f) {
        ScaledResolution sr = new ScaledResolution(Wrapper.mc);

        int scroll = Mouse.getDWheel() / 12;
        float bottom = sr.height() - 103;

        scrollAmount += scroll;
        if (scrollAmount >= bottom) scrollAmount = bottom;

        Methods.mc.getTextureManager().bindTexture(new ResourceLocation(Client.main().getClientBackground()));
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, sr.width(), sr.height(),
                width, height, sr.width(), sr.height());

        RenderUtils.drawRect(5, 0, width - 5, height - 30, new Color(0, 0, 0, 155).getRGB());
        RenderUtils.drawRect(5, height - 30, width - 5, height, new Color(45, 45, 45, 155).getRGB());

        UnicodeFontRenderer cabin35 = fM.font("Century Gothic", 35, Font.PLAIN);
        UnicodeFontRenderer cabin23 = fM.font("Century Gothic", 23, Font.PLAIN);

        String title = "Credits";
        cabin35.drawStringWithShadow(title, width / 2 - cabin35.getStringWidth(title) / 2, 10, -1);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.scissor(5, 35, sr.width() - 5, sr.height() - 30);
        try {
            cabin23.drawStringWithShadow(result, 10, 40 + scrollAmount, -1);
        } catch (Exception ignored) {
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        super.drawScreen(posX, posY, f);
    }
}