package best.azura.client.impl.ui.gui.proxy;

import best.azura.client.impl.Client;
import best.azura.client.impl.rpc.DiscordRPCImpl;
import best.azura.client.impl.ui.gui.impl.buttons.ButtonImpl;
import best.azura.client.impl.ui.gui.impl.buttons.TextButton;
import best.azura.client.impl.module.impl.render.BlurModule;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.other.TimeFormatting;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.util.render.StencilUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class MainProxy extends GuiScreen {

    private final ArrayList<ButtonImpl> buttons = new ArrayList<>();
    private final GuiScreen parent;
    private double animation = 0;
    private long start = 0;

    public MainProxy(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        DiscordRPCImpl.updateNewPresence("Azura Proxy","Changing Proxy connection");
        start = System.currentTimeMillis();
        animation = 1;

        buttons.clear();
        String[] strings = new String[]{"Connect", "Disconnect","Back"};
        int calcWidth = 0;
        for (String s : strings) {
            int width = Fonts.INSTANCE.arial20.getStringWidth(s);
            buttons.add(new ButtonImpl(s, mc.displayWidth / 2 - 170 + calcWidth, mc.displayHeight / 2 + 150, width + 40, 40, 3));
            calcWidth += width + 40 + 5;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
        animation = -1 * Math.pow(anim - 1, 6) + 1;

        GlStateManager.pushMatrix();
        glEnable(GL_BLEND);
        drawDefaultBackground();

        RenderUtil.INSTANCE.scaleFix(1.0);
        double scaleAnimation = Math.max(Math.min(animation * 10, 1), 0);
        double buttonAnimation = Math.max(Math.min((9 - animation * 10), 1), 0);
        double loadingAnimation = Math.max(Math.min((4 - animation * 5), 1), 0);
        double disappearAnimation = Math.max(Math.min((animation * 6), 1), 0);
        loadingAnimation = Math.min(disappearAnimation, loadingAnimation);
        scaleAnimation = (1 - scaleAnimation);
        buttonAnimation = (1 - buttonAnimation);
        final ScaledResolution sr = new ScaledResolution(mc);
        final boolean blur = Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled() && BlurModule.blurMenu.getObject();
        if (blur) {
            GL11.glPushMatrix();
            RenderUtil.INSTANCE.invertScaleFix(1.0);
            StencilUtil.initStencilToWrite();
            GlStateManager.scale(1.0 / sr.getScaleFactor(), 1.0 / sr.getScaleFactor(), 1);
        }
        RenderUtil.INSTANCE.drawRoundedRect(mc.displayWidth / 2.0 - 200 - 300 * scaleAnimation, mc.displayHeight / 2.0 - 200 - 200 * scaleAnimation, 400 + 600 * scaleAnimation, 400 + 400 * scaleAnimation, 10, new Color(0, 0, 0, 170));
        if (blur) {
            GlStateManager.scale(sr.getScaleFactor(), sr.getScaleFactor(), 1);
            StencilUtil.readStencilBuffer(1);
            BlurModule.blurShader.blur();
            StencilUtil.uninitStencilBuffer();
            RenderUtil.INSTANCE.scaleFix(1.0);
            GL11.glPopMatrix();
        }
        for (ButtonImpl button : buttons) {
            button.animation = buttonAnimation;
            button.draw(mouseX, mouseY);
        }

        String[] texts = {"Azura VPN",
                ProxyManager.getProxy() == null ? "Not connected." : "Currently connected to "
                        + ((InetSocketAddress) ProxyManager.getProxy().address()).getAddress().getHostAddress(),
                ProxyManager.getProxy() != null ? "Connected for "
                        + TimeFormatting.convertMillisToString(System.currentTimeMillis() - ProxyManager.getConnectTime()) : "",
                ProxyManager.getProxy() != null ? "Ping: " + ProxyManager.getPing() + "ms" : ""};
        int yOffset = 0;

        for (String text : texts) {
            Fonts.INSTANCE.arial20.drawString(text, (int)(mc.displayWidth / 2.0 - Fonts.INSTANCE.arial20.getStringWidth(text) / 2F), mc.displayHeight / 2.0 - 190 + yOffset, new Color(255, 255, 255, (int) (255 * buttonAnimation)).getRGB());
            yOffset += Fonts.INSTANCE.arial20.FONT_HEIGHT;
        }

        int lineColor = new Color(255, 255, 255, (int) (255 * loadingAnimation)).getRGB();
        if (loadingAnimation != 0) {
            RenderUtil.INSTANCE.drawLine(mc.displayWidth / 2.0 - 50 * loadingAnimation, mc.displayHeight / 2F - 50 * loadingAnimation, mc.displayWidth / 2. + 50 * loadingAnimation, mc.displayHeight / 2. + 50 * loadingAnimation, (float) (7.0f * loadingAnimation), lineColor);
            RenderUtil.INSTANCE.drawLine(mc.displayWidth / 2.0 + 50 * loadingAnimation, mc.displayHeight / 2F - 50 * loadingAnimation, mc.displayWidth / 2. - 50 * loadingAnimation, mc.displayHeight / 2. + 50 * loadingAnimation, (float) (7.0f * loadingAnimation), lineColor);
        }
        glDisable(GL_BLEND);
        GlStateManager.popMatrix();

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        for (ButtonImpl button : buttons) {
            if (button.hovered) {
                switch (button.text) {
                    case "Disconnect":
                        ProxyManager.setProxy(null);
                        break;
                    case "Connect":
                        String generated = ProxyManager.generateProxy();
                        ProxyManager.setProxy(generated.split(":")[0], generated.split(":").length < 2 ? "8080" : generated.split(":")[1]);
                        break;
                    case "Back":
                        mc.displayGuiScreen(parent);
                        break;
                }
            }
            button.mouseClicked();
        }

    }

    @Override
    public void confirmClicked(boolean result, int id) {

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        for (ButtonImpl button : buttons) {
            if (button instanceof TextButton) button.keyTyped(typedChar, keyCode);
        }
    }
}
