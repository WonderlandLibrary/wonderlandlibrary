package net.minecraft.client.gui;

import cc.novoline.Novoline;
import cc.novoline.gui.button.HydraButton;
import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.fonts.impl.Fonts;
import cc.novoline.utils.shader.GLSLSandboxShader;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    float hHeight = 540;
    float hWidth = 960;
    public static int BUTTON_COUNT = 5;
    public float totalHeight = BUTTON_COUNT * 20 + 20 + BUTTON_COUNT * 3;
    public float halfTotalHeight = totalHeight / 2, fraction = 0;
    public boolean darkTheme = true, guiWasSwitched;
    public int alpha = 0;
    private final String path = Novoline.getInstance().getPathString() + "theme.txt";


    private GLSLSandboxShader shader;

    private final Color blackish = new Color(20, 23, 26);
    private final Color black = new Color(40, 46, 51);
    private final Color blueish = new Color(0, 150, 135);
    private final Color blue = new Color(0xFF2B71B1);
    private final Color whiteish = new Color(0xFFF4F5F8);

    HydraButton singleplayerworlds = new HydraButton(1, (int) hWidth - 70, (int) hHeight - (int) halfTotalHeight + 10, 140, 20, "Singleplayer");
    HydraButton multiplayer = new HydraButton(2, (int) hWidth - 70, (int) hHeight - (int) halfTotalHeight + 33, 140, 20, "Multiplayer");
    HydraButton altrepository = new HydraButton(1337, (int) hWidth - 70, (int) hHeight - (int) halfTotalHeight + 56, 140, 20, "Alt Repository");
    HydraButton options = new HydraButton(0, (int) hWidth - 70, (int) hHeight - (int) halfTotalHeight + 79, 140, 20, "Options");
    HydraButton shutdown = new HydraButton(4, (int) hWidth - 70, (int) hHeight - (int) halfTotalHeight + 102, 140, 20, "Shutdown");

    private long initTime = System.currentTimeMillis();

    public GuiMainMenu() {
        try {
            this.shader = new GLSLSandboxShader("/assets/minecraft/shaders/program/novoline_menu.fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load backgound shader", e);
        }
        initTime = System.currentTimeMillis();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void initGui() {
        buttonList.add(singleplayerworlds);
        buttonList.add(multiplayer);
        buttonList.add(altrepository);
        buttonList.add(options);
        buttonList.add(shutdown);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        hWidth = scaledResolution.getScaledWidth() / 2;
        alpha = 100;

        if (mc.previousScreen != this && !guiWasSwitched) {
            guiWasSwitched = true;
        }

        darkTheme = true;

        super.initGui();
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (guiWasSwitched && darkTheme && fraction != 1.0049993F) {
            fraction = 1.0049993F;
        }

        if (darkTheme && fraction < 1) {
            fraction += 0.015;
        } else if (!darkTheme && fraction > 0) {
            fraction -= 0.015;
        }

        if (mouseX <= 20 && mouseY <= 20 && alpha < 255) {
            alpha++;
        } else if (alpha > 100) {
            alpha--;
        }

        GlStateManager.disableCull();
        shader.useShader(this.width, this.height, mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);
        GL11.glEnd();
        GL20.glUseProgram(0);

        singleplayerworlds.setColor(interpolateColor(blue, blueish, fraction));
        multiplayer.setColor(interpolateColor(blue, blueish, fraction));
        altrepository.setColor(interpolateColor(blue, blueish, fraction));
        options.setColor(interpolateColor(blue, blueish, fraction));
        shutdown.setColor(interpolateColor(blue, blueish, fraction));

        singleplayerworlds.setColor(interpolateColor(
                singleplayerworlds.hovered(mouseX, mouseY) ? blue.brighter() : blue,
                singleplayerworlds.hovered(mouseX, mouseY) ? blueish.brighter() : blueish,
                fraction));
        multiplayer.setColor(interpolateColor(
                multiplayer.hovered(mouseX, mouseY) ? blue.brighter() : blue,
                multiplayer.hovered(mouseX, mouseY) ? blueish.brighter() : blueish,
                fraction));
        altrepository.setColor(interpolateColor(
                altrepository.hovered(mouseX, mouseY) ? blue.brighter() : blue,
                altrepository.hovered(mouseX, mouseY) ? blueish.brighter() : blueish,
                fraction));
        options.setColor(interpolateColor(
                options.hovered(mouseX, mouseY) ? blue.brighter() : blue,
                options.hovered(mouseX, mouseY) ? blueish.brighter() : blueish,
                fraction));
        shutdown.setColor(interpolateColor(
                shutdown.hovered(mouseX, mouseY) ? blue.brighter() : blue,
                shutdown.hovered(mouseX, mouseY) ? blueish.brighter() : blueish,
                fraction));


        singleplayerworlds.updateCoordinates(hWidth - 70, hHeight - halfTotalHeight + 10);
        multiplayer.updateCoordinates(hWidth - 70, hHeight - halfTotalHeight + 33);
        altrepository.updateCoordinates(hWidth - 70, hHeight - halfTotalHeight + 56);
        options.updateCoordinates(hWidth - 70, hHeight - halfTotalHeight + 79);
        shutdown.updateCoordinates(hWidth - 70, hHeight - halfTotalHeight + 102);

        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaledWidthScaled = scaledResolution.getScaledWidth();
        int scaledHeightScaled = scaledResolution.getScaledHeight();
        hHeight = hHeight + (scaledHeightScaled / 2 - hHeight) * 0.02f;
        hWidth = scaledWidthScaled / 2;

        Gui.drawRect(0, 0, scaledWidthScaled, scaledHeightScaled, new Color(0,0,0,150).getRGB());
        RenderUtils.drawBorderedRect(
                hWidth - 80,
                hHeight - halfTotalHeight,
                hWidth + 80,
                hHeight + halfTotalHeight,
                0.3f,
                new Color(0,0,0,50).getRGB(),
                new Color(0,0,0,50).getRGB());


        // LOGO
        Fonts.OXIDE.OXIDE_55.OXIDE_55.drawString(
                "NOVOLINE",
                hWidth - Fonts.OXIDE.OXIDE_55.OXIDE_55.stringWidth("NOVOLINE") / 2,
                hHeight - halfTotalHeight - 35,
                interpolateColor(blue, blueish, fraction));

        Color vis = new Color(interpolateColor(blue, blueish, fraction));

       // Fonts.ICONFONT.ICONFONT_35.ICONFONT_35.drawString("M", 5, 5, new Color(vis.getRed(), vis.getGreen(), vis.getBlue(), alpha).getRGB());

        singleplayerworlds.drawButton(mc, mouseX, mouseY);
        multiplayer.drawButton(mc, mouseX, mouseY);
        altrepository.drawButton(mc, mouseX, mouseY);
        options.drawButton(mc, mouseX, mouseY);
        shutdown.drawButton(mc, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1337:
                this.mc.displayGuiScreen(this.mc.getNovoline().getAltRepositoryGUI());
                break;
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 5:
                this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
                break;

            case 1:
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;

            case 2:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;

            case 4:
                this.mc.shutdown();
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private int interpolateColor(Color color1, Color color2, float fraction) {
        int red = (int) (color1.getRed() + (color2.getRed() - color1.getRed()) * fraction);
        int green = (int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * fraction);
        int blue = (int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * fraction);
        int alpha = (int) (color1.getAlpha() + (color2.getAlpha() - color1.getAlpha()) * fraction);
        try {
            return new Color(red, green, blue, alpha).getRGB();
        } catch (Exception ex) {
            return 0xffffffff;
        }
    }

}