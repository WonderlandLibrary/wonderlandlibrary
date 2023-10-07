package cc.novoline.gui.screen.login;

import cc.novoline.Initializer;
import cc.novoline.Novoline;
import cc.novoline.gui.button.HydraButton;
import cc.novoline.gui.screen.login.textbox.UIDField;
import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.fonts.impl.Fonts;
import cc.novoline.utils.shader.GLSLSandboxShader;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.io.*;


public class GuiLogin extends GuiScreen {

    private String status;


    private long ticks;
    private boolean launched = true;
    private boolean authenticated = false;
    private boolean darkTheme = false;
    private boolean falseError;
    private String token = null;
    private float fraction;
    public int alpha = 0;

    private GLSLSandboxShader shader;
    private long initTime = System.currentTimeMillis(); // Initialize as a failsafe

    private final Color blackish = new Color(20, 23, 26);
    private final Color black = new Color(40, 46, 51);
    private final Color blueish = new Color(0, 150, 135);
    private final Color blue = new Color(0xFF2B71B1);
    private final String path = Novoline.getInstance().getPathString() + "theme.txt";

    public GuiLogin() {
        status = "Idle";

        try {
            this.shader = new GLSLSandboxShader("/assets/minecraft/shaders/program/novoline_menu.fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load backgound shader", e);
        }
        initTime = System.currentTimeMillis();
    }

    @Override
    public void initGui() {
        Display.setTitle("Novoline - Not logged in");
        buttonList.add(button);
        field = new UIDField(1, mc.fontRendererObj, (int) hWidth - 70, (int) hHeight - 35, 140, 30, "idk");
        alpha = 100;

        darkTheme = true;

        super.initGui();
    }

    private float hHeight = 540;
    private float hWidth = 960;
    private float errorBoxHeight = 0;

    HydraButton button = new HydraButton(0, (int) hWidth - 70, (int) (hHeight + 5), 140, 30, "Log In");
    UIDField field;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableCull();
        shader.useShader(this.width, this.height, mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);
        GL11.glEnd();
        GL20.glUseProgram(0);
        if (launched && darkTheme && fraction != 1.0049993F) {
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

        Color whiteish = new Color(0xFFF4F5F8);
        Color white = Color.WHITE;
        Color shitGray = new Color(150, 150, 150);

        button.setColor(interpolateColor(
                button.hovered(mouseX, mouseY) ? blue.brighter() : blue,
                button.hovered(mouseX, mouseY) ? blueish.brighter() : blueish,
                fraction));
        field.setColor(interpolateColor(white, black, fraction));
        field.setTextColor(interpolateColor(shitGray, white, fraction));

        ScaledResolution scaledResolution = new ScaledResolution(mc);
        button.updateCoordinates(hWidth - 70, hHeight + 5);
        field.updateCoordinates(hWidth - 70, hHeight - 35);
        int scaledWidthScaled = scaledResolution.getScaledWidth();
        int scaledHeightScaled = scaledResolution.getScaledHeight();

        hHeight = hHeight + (scaledHeightScaled / 2 - hHeight) * 0.02f;
        hWidth = scaledWidthScaled / 2;

        Gui.drawRect(0, 0, scaledWidthScaled, scaledHeightScaled, new Color(0,0,0,150).getRGB());

        Color vis = new Color(interpolateColor(blue, blueish, fraction));

     //   Fonts.ICONFONT.ICONFONT_35.ICONFONT_35.drawString("M", 5, 5, new Color(vis.getRed(), vis.getGreen(), vis.getBlue(), alpha).getRGB());

        RenderUtils.drawBorderedRect(hWidth - 90, hHeight - 55, hWidth + 90, hHeight + 55, 0.3f, new Color(0,0,0,80).getRGB(),
                new Color(0,0,0,50).getRGB());

        // LOGO
        Fonts.OXIDE.OXIDE_55.OXIDE_55.drawString(
                "NOVOLINE",
                hWidth - Fonts.OXIDE.OXIDE_55.OXIDE_55.stringWidth("NOVOLINE") / 2 + 12,
                hHeight - 90,
                interpolateColor(blue, blueish, fraction));
        Fonts.ICONFONT.ICONFONT_50.ICONFONT_50.drawString("L", hWidth - 72, hHeight - 90, interpolateColor(blue, blueish, fraction));


        // LOG IN BUTTON
        button.drawButton(mc, mouseX, mouseY);

        //STATUS
        if (status.startsWith("Idle") || status.startsWith("Initializing") || status.startsWith("Logging")) {
            Fonts.SF.SF_16.SF_16.drawString(status, hWidth - Fonts.SF.SF_16.SF_16.stringWidth(status) / 2, hHeight + 45, interpolateColor(new Color(150, 150, 150), white, fraction));
            errorBoxHeight = 0;
        } else {
            if (status.equals("Success")) {
                errorBoxHeight = errorBoxHeight + (10 - errorBoxHeight) * 0.01f;
                RenderUtils.drawBorderedRect(hWidth - Fonts.SF.SF_16.SF_16.stringWidth(status) / 2 - 10, errorBoxHeight, hWidth + Fonts.SF.SF_16.SF_16.stringWidth(status) / 2 + 10, errorBoxHeight + 12, 1f, new Color(170, 253, 126).getRGB(), interpolateColor(new Color(232, 255, 213), new Color(232, 255, 213).darker().darker(), fraction));
                Fonts.SF.SF_16.SF_16.drawString(status, hWidth - Fonts.SF.SF_16.SF_16.stringWidth(status) / 2, errorBoxHeight + 7 - Fonts.SF.SF_16.SF_16.getHeight() / 2, new Color(201, 255, 167).darker().getRGB(), true);
            } else {
                errorBoxHeight = errorBoxHeight + (10 - errorBoxHeight) * 0.01f;
                RenderUtils.drawBorderedRect(hWidth - Fonts.SF.SF_16.SF_16.stringWidth(status) / 2 - 10, errorBoxHeight, hWidth + Fonts.SF.SF_16.SF_16.stringWidth(status) / 2 + 10, errorBoxHeight + 12, 1f, 0xFFF5DAE1, interpolateColor(new Color(0xFFF8E5E8), new Color(0xFFF8E5E8).darker().darker(), fraction));
                Fonts.SF.SF_16.SF_16.drawString(status, hWidth - Fonts.SF.SF_16.SF_16.stringWidth(status) / 2, errorBoxHeight + 7 - Fonts.SF.SF_16.SF_16.getHeight() / 2, 0XFFEB6E85, true);
            }
        }

        // UID TEXTBOX
        field.drawTextBox();

        // CREDITS
        Fonts.SF.SF_18.SF_18.drawString("made by gast and tasteful. protected by skidunion", hWidth - Fonts.SF.SF_18.SF_18.stringWidth("made by gast and tasteful. protected by skidunion") / 2, scaledHeightScaled - Fonts.SF.SF_18.SF_18.getHeight() - 4, new Color(150, 150, 150).getRGB());

//        if (authenticated) {
            this.status = "Success";
            if (System.currentTimeMillis() - ticks > 250) {
                Initializer.getInstance().onProtection(token);
            }
//        }

        if (falseError) {
            try {
                ScaledResolution sr = new ScaledResolution(mc);
                mouseClicked(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 + 20, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            falseError = false;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (field.isFocused() && keyCode >= 2 && keyCode <= 11 || keyCode == 14 /* number check */) {
            field.textboxKeyTyped(typedChar, keyCode);
        }

        if (keyCode == 64) {
            mc.displayGuiScreen(this);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        field.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            new Thread(() -> {
                button.enabled = false;
                try {
                    status = "Initializing";
//                    Novoline.getInstance().setProtection(new Protection(this, true, true));

                    status = "Logging in";
//                    Novoline.getInstance().getProtection().login(field.getText());
                    mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("random.orb"), 1F));
                } catch (Throwable t) {
                    t.printStackTrace();

                    if (t.getMessage().contains("ConcurrentModificationException")) {
                        falseError = true;
                    }

                    status = t.getMessage();
                    button.enabled = true;
                    mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("random.orb"), -1F));
                }
            }, "SAL Authentication Thread").start();
        }

        super.actionPerformed(button);
    }

//    @Override
//    public void onProtection(String token) {
//        ticks = System.currentTimeMillis();
//        authenticated = true;
//        this.token = token;
//    }

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