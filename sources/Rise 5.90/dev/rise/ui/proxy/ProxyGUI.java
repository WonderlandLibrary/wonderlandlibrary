package dev.rise.ui.proxy;

import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.font.fontrenderer.TTFFontRenderer;
import dev.rise.proxy.ProxyThread;
import dev.rise.ui.mainmenu.MainMenu;
import dev.rise.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomPanorama;
import net.optifine.CustomPanoramaProperties;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.awt.*;
import java.io.IOException;

// Credits to Hideri chan.
public final class ProxyGUI extends GuiScreen {

    private static final TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("Dreamscape 96");
    public static GuiTextField portField;
    public static GuiTextField ipField;
    public static GuiTextField proxyTypeField;
    public static ProxyThread thread;
    public static int port;
    public static String ip;
    public static float smoothedX, smoothedY;
    public static float xOffSet;
    public static float yOffSet;
    private static String proxyType;
    private final float buttonWidth = 50;
    private final float buttonHeight = 20;
    private final float gap = 4;
    public GuiScreen previousScreen;
    private float x;
    private float y;
    private float screenWidth;
    private float screenHeight;
    private ScaledResolution sr;

    public ProxyGUI(final GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    public static boolean isProxyFieldAvailable() {
        return ipField != null && ipField.getText() != "" && portField != null && portField.getText() != "";
    }

    public static boolean isProxyAvailable() {
        return ip != null && String.valueOf(port) != null;
    }

    public static GuiTextField getPortField() {
        return portField;
    }

    public static void setPortField(final GuiTextField portField) {
        ProxyGUI.portField = portField;
    }

    public static GuiTextField getIpField() {
        return ipField;
    }

    public static void setIpField(final GuiTextField ipField) {
        ProxyGUI.ipField = ipField;
    }

    public static GuiTextField getProxyTypeField() {
        return proxyTypeField;
    }

    public static void setProxyTypeField(final GuiTextField proxyTypeField) {
        ProxyGUI.proxyTypeField = proxyTypeField;
    }

    public static String getProxyType() {
        return proxyType;
    }

    public static void setProxyType(final String proxyType) {
        ProxyGUI.proxyType = proxyType;
    }

    @Override
    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            }
            case 0: {
                if (!isProxyFieldAvailable()) {
                    return;
                }

                ip = ipField.getText();

                if (!proxyTypeField.getText().equalsIgnoreCase("HTTP")
                        && !proxyTypeField.getText().equalsIgnoreCase("SOCKS4")
                        && !proxyTypeField.getText().equalsIgnoreCase("SOCKS5")) {
                    proxyType = "SOCKS4";
                } else {
                    proxyType = proxyTypeField.getText().toUpperCase();
                }

                try {
                    port = Integer.parseInt(portField.getText());
                } catch (final NumberFormatException nfe) {
                    System.out.println("!!! NumberFormatError !!! Remember to use numbers for PORT when setting a Proxy !!!");
                }
                thread = new ProxyThread(ip, String.valueOf(port), proxyType);
                thread.start();
                break;
            }
            case 2: {
                ip = null;
                break;
            }
        }
    }

    @Override
    public void drawScreen(final int x2, final int y2, final float z2) {
        ++MainMenu.panoramaTimer;

        RenderUtil.color(Rise.INSTANCE.getGuiTheme().getThemeColor());
        mc.getTextureManager().bindTexture(new ResourceLocation("rise/backgrounds/blue.png"));

        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        final float scale = 1.66f;
        final float amount = 1500;

        if (MainMenu.panoramaTimer % 100 == 0) {
            MainMenu.xOffSet = (float) (Math.random() - 0.5f) * amount;
            MainMenu.yOffSet = (float) (Math.random() - 0.5f) * amount;
        }

        MainMenu.smoothedX = (MainMenu.smoothedX * 250 + MainMenu.xOffSet) / 259;
        MainMenu.smoothedY = (MainMenu.smoothedY * 250 + MainMenu.yOffSet) / 259;

        drawModalRectWithCustomSizedTexture(0, 0, width / scale + MainMenu.smoothedX - 150, height / scale + MainMenu.smoothedY - 100, width, height, width * scale, height * scale);

        final int var3 = height / 4 + 24;

        final int widthOfText = (int) (buttonWidth * 2);
        final int widthAndGap = (int) (buttonHeight + gap);

        x = (sr.getScaledWidth() / 2.0F);
        y = (sr.getScaledHeight() / 2.0F) - (widthOfText / 2.0F) - 6;

        RenderUtil.roundedRect((int) (x - widthOfText / 2), (int) y, widthOfText, (int) buttonHeight, 10, new Color(255, 255, 255, 35));
        RenderUtil.roundedRect((int) (x - widthOfText / 2), (int) y + widthAndGap, widthOfText, (int) buttonHeight, 10, new Color(255, 255, 255, 35));
        RenderUtil.roundedRect((int) (x - widthOfText / 2), (int) y + widthAndGap * 2, widthOfText, (int) buttonHeight, 10, new Color(255, 255, 255, 35));
        RenderUtil.roundedRect((int) (x - widthOfText / 2), (int) y + widthAndGap * 3, widthOfText, (int) buttonHeight, 10, new Color(255, 255, 255, 35));
        RenderUtil.roundedRect((int) (x - widthOfText / 2), (int) y + widthAndGap * 4, widthOfText, (int) buttonHeight, 10, new Color(255, 255, 255, 35));
        RenderUtil.roundedRect((int) (x - widthOfText / 2), (int) y + widthAndGap * 5, widthOfText, (int) buttonHeight, 10, new Color(255, 255, 255, 35));

        ipField.drawTextBoxWithoutBackground();
        portField.drawTextBoxWithoutBackground();
        proxyTypeField.drawTextBoxWithoutBackground();

        CustomFont.drawCenteredString("Proxy Thread (Work in progress)", width / 2, 20, new Color(255, 255, 255, 210).hashCode());
        CustomFont.drawCenteredString(thread == null ? "Waiting For Proxy..." : thread.getStatus(), width / 2, 29, new Color(255, 255, 255, 210).hashCode());

        // Box
        //RenderUtil.roundedRect(x - 63, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 - 108, 125, 163, 10, new Color(0, 0, 0, 35));

        if (ipField.getText().isEmpty()) {
            CustomFont.drawCenteredString("ip", (int) (x), y + CustomFont.getHeight() / 2, new Color(255, 255, 255, 210).hashCode());
        }
        if (portField.getText().isEmpty()) {
            CustomFont.drawCenteredString("port", (int) (x), y + widthAndGap + CustomFont.getHeight() / 2, new Color(255, 255, 255, 210).hashCode());
        }
        if (proxyTypeField.getText().isEmpty()) {
            CustomFont.drawCenteredString("http / socks4 / socks5", (int) (x), y + widthAndGap * 2 + CustomFont.getHeight() / 2, new Color(255, 255, 255, 210).hashCode());
        }

        final String message = "Made with <3 by ALLAH, zajchu, 6Sence, Tecnio, Strikeless, Nicklas, P3rZ3r0 and Auth";
        final String version = Rise.CLIENT_NAME + " " + Rise.CLIENT_VERSION;

        if (sr.getScaledHeight() > 300) {
            CustomFont.drawString(message, sr.getScaledWidth() - CustomFont.getWidth(message) - 2, sr.getScaledHeight() - 12.5, new Color(255, 255, 255, 180).hashCode());
            CustomFont.drawString(version, 2, sr.getScaledHeight() - 12.5, new Color(255, 255, 255, 180).hashCode());
        }

        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        final int var3 = height / 4 + 24;
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        final int widthOfText = (int) (buttonWidth * 2);
        final int widthAndGap = (int) (buttonHeight + gap);

        x = (sr.getScaledWidth() / 2.0F);
        y = (sr.getScaledHeight() / 2.0F) - (widthOfText / 2.0F) - 6;

        this.buttonList.add(new GuiButton(0, width / 2 - 100, (int) y + widthAndGap * 3, "Set Proxy", false));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, (int) y + widthAndGap * 4, "Back", false));
        this.buttonList.add(new GuiButton(2, width / 2 - 100, (int) y + widthAndGap * 5, "Reset Proxy", false));

        ipField = new GuiTextField(var3, this.mc.fontRendererObj, (int) (x - widthOfText / 2), (int) y, widthOfText, (int) buttonHeight, false);
        portField = new GuiTextField(var3, this.mc.fontRendererObj, (int) (x - widthOfText / 2), (int) y + widthAndGap, widthOfText, 20, false);
        proxyTypeField = new GuiTextField(var3, this.mc.fontRendererObj, (int) (x - widthOfText / 2), (int) y + widthAndGap * 2, widthOfText, 20, false);
        ipField.setFocused(true);

        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void keyTyped(final char character, final int key) {
        try {
            super.keyTyped(character, key);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!ipField.isFocused() && !portField.isFocused() && !proxyTypeField.isFocused()) {
                ipField.setFocused(true);
            } else {
                ipField.setFocused(portField.isFocused());
                portField.setFocused(!ipField.isFocused());
                portField.setFocused(false);
            }
        }
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        ipField.textboxKeyTyped(character, key);
        portField.textboxKeyTyped(character, key);
        proxyTypeField.textboxKeyTyped(character, key);
    }

    private void drawPanorama(final int p_73970_1_, final int p_73970_2_, final float p_73970_3_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        final int i = 8;
        int j = 64;

        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null) {
            j = custompanoramaproperties.getBlur1();
        }

        for (int k = 0; k < j; ++k) {
            GlStateManager.pushMatrix();

            final float f = ((float) (k % i) / (float) i - 0.5F) / 64.0F;
            final float f1 = ((float) (k / i) / (float) i - 0.5F) / 64.0F;
            final float f2 = 0.0F;

            GlStateManager.translate(f, f1, f2);
            GlStateManager.rotate(MathHelper.sin(((float) MainMenu.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float) MainMenu.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int l = 0; l < 6; ++l) {
                GlStateManager.pushMatrix();

                if (l == 1) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 3) {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 4) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (l == 5) {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                ResourceLocation[] aresourcelocation = null;

                if (custompanoramaproperties != null) {
                    aresourcelocation = custompanoramaproperties.getPanoramaLocations();
                }

                this.mc.getTextureManager().bindTexture(aresourcelocation[l]);
                worldrenderer.begin(7, DefaultVertexFormats.field_181709_i);
                final int i1 = 255 / (k + 1);
                final float f3 = 0.0F;
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).func_181673_a(0.0D, 0.0D).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).func_181673_a(1.0D, 0.0D).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(1.0D, 1.0D, 1.0D).func_181673_a(1.0D, 1.0D).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(-1.0D, 1.0D, 1.0D).func_181673_a(0.0D, 1.0D).color(255, 255, 255, i1).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void renderSkybox(final int p_73971_1_, final int p_73971_2_, final float p_73971_3_) {
        GlStateManager.disableAlpha();
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        int i = 3;
        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null) {
            i = custompanoramaproperties.getBlur3();
        }

        for (int j = 0; j < i; ++j) {
            this.rotateAndBlurSkybox(p_73971_3_);
            this.rotateAndBlurSkybox(p_73971_3_);
        }

        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        final float f2 = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
        final float f = (float) this.height * f2 / 256.0F;
        final float f1 = (float) this.width * f2 / 256.0F;
        final int k = this.width;
        final int l = this.height;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.field_181709_i);
        worldrenderer.pos(0.0D, l, zLevel).func_181673_a(0.5F - f, 0.5F + f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(k, l, zLevel).func_181673_a(0.5F - f, 0.5F - f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(k, 0.0D, zLevel).func_181673_a(0.5F + f, 0.5F - f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(0.0D, 0.0D, zLevel).func_181673_a(0.5F + f, 0.5F + f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
        GlStateManager.enableAlpha();
    }

    private void rotateAndBlurSkybox(final float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(Rise.INSTANCE.getBackgroundTexture());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.field_181709_i);
        GlStateManager.disableAlpha();
        final int i = 3;
        final int j = 3;
        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        for (int k = 0; k < j; ++k) {
            final float f = 1.0F / (float) (k + 1);
            final int l = this.width;
            final int i1 = this.height;
            final float f1 = (float) (k - i / 2) / 256.0F;
            worldrenderer.pos(l, i1, zLevel).func_181673_a(0.0F + f1, 1.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(l, 0.0D, zLevel).func_181673_a(1.0F + f1, 1.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, 0.0D, zLevel).func_181673_a(1.0F + f1, 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, i1, zLevel).func_181673_a(0.0F + f1, 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    @Override
    public void mouseClicked(final int x2, final int y2, final int button) {
        try {
            super.mouseClicked(x2, y2, button);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        ipField.mouseClicked(x2, y2, button);
        portField.mouseClicked(x2, y2, button);
        proxyTypeField.mouseClicked(x2, y2, button);
    }

    @Override
    public void onGuiClosed() {
        thread = null;
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        ipField.updateCursorCounter();
        portField.updateCursorCounter();
        proxyTypeField.updateCursorCounter();
    }
}
