package com.masterof13fps.features.ui.guiscreens;

import com.masterof13fps.Client;
import com.masterof13fps.utils.render.GLSLSandboxShader;
import com.masterof13fps.utils.render.RenderUtils;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    private final GLSLSandboxShader shader;

    double onlineVer;
    double localVer = Client.main().getClientVersion();

    public GuiMainMenu() {
        try {
            shader = new GLSLSandboxShader("/assets/minecraft/client/shader/main.fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load the Shader");
        }
    }

    public void initGui() {
        buttonList.add(new GuiButton(0, width / 2 - 120, height / 2 - 60, 120, 20, I18n.format("menu.singleplayer")));
        buttonList.add(new GuiButton(1, width / 2 + 10, height / 2 - 60, 120, 20, I18n.format("menu.multiplayer")));
        buttonList.add(new GuiButton(2, width / 2 - 120, height / 2, 120, 20, I18n.format("menu.options")));
        buttonList.add(new GuiButton(3, width / 2 + 10, height / 2, 120, 20, I18n.format("menu.quit")));
        buttonList.add(new GuiButton(4, width / 2 - 120, height / 2 - 30, 250, 20, "Client"));

        Thread versionCheckThread = new Thread(() -> {
            try {
                String versionCheck = "https://pastebin.com/raw/tcuBTU3x";
                URL url = new URL(versionCheck);

                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                onlineVer = Double.parseDouble(reader.readLine());

                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        versionCheckThread.start();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (button.id == 1) {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 2) {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        }
        if (button.id == 3) {
            mc.shutdown();
        }
        if (button.id == 4) {
            mc.displayGuiScreen(new GuiClient(this));
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();

        shader.useShader(width, height, mouseX, mouseY, (System.currentTimeMillis() - Client.main().getInitTime()) / 1000F);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();

        GL20.glUseProgram(0);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        ScaledResolution sr = new ScaledResolution(mc);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (width / 2 + 90), 70.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
        float f = 1.8F - MathHelper.abs(
                MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
        GlStateManager.scale(f, f, f);
        GlStateManager.popMatrix();

        /**
         * START OF DRAWING STUFF
         *
         * ORDER:
         * - Background for Buttons
         * - Client Name
         * - Version Check [latest or not?]
         */

        UnicodeFontRenderer font1 = Client.main().fontMgr().font("BigNoodleTitling", 60, Font.BOLD);
        UnicodeFontRenderer font2 = Client.main().fontMgr().font("Comfortaa", 20, Font.PLAIN);

        RenderUtils.drawRoundedRect(width / 2 - 130, height / 2 - 70, 265, 95, 10, new Color(45,45,45).getRGB());

        String name = Client.main().getClientName();
        font1.drawStringWithShadow(name, 2, 2, -1);

        String outdatedVer = "Du benutzt eine veraltete Version (§4" + Client.main().getClientVersion() + " §8/ §6" + onlineVer + "§r) - Update noch jetzt um die neusten Features zu erhalten!";
        String latestVer = "Du benutzt die neuste Version (§c" + Client.main().getClientVersion() + " §8/ §6" + onlineVer + "§r)";
        String newerVer = "Du benutzt eine neuere Version als erschienen ist (§a" + Client.main().getClientVersion() + " §8/ §6" + onlineVer + "§r) - Zeitreisender?";

        if (onlineVer > localVer) {
            font2.drawStringWithShadow(outdatedVer, 2, sr.height() - 10, -1);
        }
        if (onlineVer == localVer) {
            font2.drawStringWithShadow(latestVer, 2, sr.height() - 10, -1);
        }
        if (onlineVer < localVer) {
            font2.drawStringWithShadow(newerVer, 2, sr.height() - 10, -1);
        }


        /**
         * END OF DRAWING STUFF
         */

        float scale = 5.0F;
        GL11.glScalef(scale, scale, scale);
        GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}