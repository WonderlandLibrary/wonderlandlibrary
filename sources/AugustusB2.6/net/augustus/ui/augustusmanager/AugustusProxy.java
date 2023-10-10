// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.ui.augustusmanager;

import java.awt.Color;
import org.lwjgl.opengl.GL11;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import net.minecraft.client.gui.GuiButton;
import net.augustus.ui.widgets.CustomButton;
import net.augustus.Augustus;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiScreen;

public class AugustusProxy extends GuiScreen
{
    private GuiScreen parent;
    public static String type;
    
    public AugustusProxy(final GuiScreen parent) {
        this.parent = parent;
    }
    
    public GuiScreen start(final GuiScreen parent) {
        this.parent = parent;
        return this;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final int scaledWidth = sr.getScaledWidth();
        final int scaledHeight = sr.getScaledHeight();
        final int startHeight = Math.min(40 + scaledHeight / 7, 135);
        this.buttonList.add(new CustomButton(1, scaledWidth / 2 - 100, startHeight, 200, 20, "ClipboardLogin", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(2, scaledWidth / 2 - 100, scaledHeight - scaledHeight / 10, 200, 20, "Back", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(3, scaledWidth / 2 - 100, startHeight + 30, 200, 20, AugustusProxy.type, Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(4, scaledWidth / 2 - 100, startHeight + 60, 200, 20, "Reset", Augustus.getInstance().getClientColor()));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            final String clipboard = GuiScreen.getClipboardString();
            if (clipboard.trim().isEmpty()) {
                Augustus.getInstance().setProxy(null);
            }
            final String[] split = clipboard.split(":");
            if (split.length >= 2 && !split[0].contains("@")) {
                final String hostname = split[0];
                final int port = Integer.parseInt(split[1]);
                final InetSocketAddress address = new InetSocketAddress(hostname, port);
                Augustus.getInstance().setProxy(new Proxy(Proxy.Type.SOCKS, address));
            }
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(this.parent);
        }
        if (button.id == 3) {
            AugustusProxy.type = (AugustusProxy.type.equals("Socks4") ? "Socks5" : "Socks4");
            button.displayString = AugustusProxy.type;
        }
        if (button.id == 4) {
            Augustus.getInstance().setProxy(null);
        }
        super.actionPerformed(button);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawBackground(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sr = new ScaledResolution(this.mc);
        GL11.glPushMatrix();
        GL11.glScaled(2.0, 2.0, 1.0);
        this.fontRendererObj.drawStringWithShadow("Augustus Proxy", sr.getScaledWidth() / 4.0f - this.fontRendererObj.getStringWidth("Augustus Proxy") / 2.0f, 10.0f, Color.lightGray.getRGB());
        GL11.glScaled(1.0, 1.0, 1.0);
        GL11.glPopMatrix();
        sr = new ScaledResolution(this.mc);
        String status = "Waiting...";
        if (Augustus.getInstance().getProxy() != null) {
            status = "Logged in Proxy";
        }
        this.fontRendererObj.drawStringWithShadow(status, sr.getScaledWidth() / 2.0f - this.fontRendererObj.getStringWidth(status) / 2.0f, sr.getScaledHeight() - sr.getScaledHeight() / 10.0f - 20.0f, Color.green.getRGB());
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1 && this.mc.theWorld == null) {
            this.mc.displayGuiScreen(this.parent);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    static {
        AugustusProxy.type = "Socks4";
    }
}
