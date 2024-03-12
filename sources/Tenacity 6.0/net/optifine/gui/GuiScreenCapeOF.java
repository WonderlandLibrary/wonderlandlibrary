// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.gui;

import com.mojang.authlib.exceptions.InvalidCredentialsException;
import net.optifine.Lang;
import java.net.URI;
import java.math.BigInteger;
import java.util.Random;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.src.Config;
import net.minecraft.client.gui.IFontRenderer;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenCapeOF extends GuiScreenOF
{
    private final GuiScreen parentScreen;
    private String title;
    private String message;
    private long messageHideTimeMs;
    private String linkUrl;
    private GuiButtonOF buttonCopyLink;
    private IFontRenderer fontRenderer;
    
    public GuiScreenCapeOF(final GuiScreen parentScreenIn) {
        this.fontRenderer = Config.getMinecraft().fontRendererObj;
        this.parentScreen = parentScreenIn;
    }
    
    @Override
    public void initGui() {
        int i = 0;
        this.title = I18n.format("of.options.capeOF.title", new Object[0]);
        i += 2;
        this.buttonList.add(new GuiButtonOF(210, this.width / 2 - 155, this.height / 6 + 24 * (i >> 1), 150, 20, I18n.format("of.options.capeOF.openEditor", new Object[0])));
        this.buttonList.add(new GuiButtonOF(220, this.width / 2 - 155 + 160, this.height / 6 + 24 * (i >> 1), 150, 20, I18n.format("of.options.capeOF.reloadCape", new Object[0])));
        i += 6;
        this.buttonCopyLink = new GuiButtonOF(230, this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, I18n.format("of.options.capeOF.copyEditorLink", new Object[0]));
        this.buttonCopyLink.visible = (this.linkUrl != null);
        this.buttonList.add(this.buttonCopyLink);
        i += 4;
        this.buttonList.add(new GuiButtonOF(200, this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        if (button.enabled) {
            if (button.id == 200) {
                this.mc2.displayGuiScreen(this.parentScreen);
            }
            if (button.id == 210) {
                try {
                    final String s = this.mc2.getSession().getProfile().getName();
                    final String s2 = this.mc2.getSession().getProfile().getId().toString().replace("-", "");
                    final String s3 = this.mc2.getSession().getToken();
                    final Random random = new Random();
                    final Random random2 = new Random(System.identityHashCode(new Object()));
                    final BigInteger biginteger = new BigInteger(128, random);
                    final BigInteger biginteger2 = new BigInteger(128, random2);
                    final BigInteger biginteger3 = biginteger.xor(biginteger2);
                    final String s4 = biginteger3.toString(16);
                    this.mc2.getSessionService().joinServer(this.mc2.getSession().getProfile(), s3, s4);
                    final String s5 = "https://optifine.net/capeChange?u=" + s2 + "&n=" + s + "&s=" + s4;
                    final boolean flag = Config.openWebLink(new URI(s5));
                    if (flag) {
                        this.showMessage(Lang.get("of.message.capeOF.openEditor"), 10000L);
                    }
                    else {
                        this.showMessage(Lang.get("of.message.capeOF.openEditorError"), 10000L);
                        this.setLinkUrl(s5);
                    }
                }
                catch (InvalidCredentialsException invalidcredentialsexception) {
                    Config.showGuiMessage(I18n.format("of.message.capeOF.error1", new Object[0]), I18n.format("of.message.capeOF.error2", invalidcredentialsexception.getMessage()));
                    Config.warn("Mojang authentication failed");
                    Config.warn(invalidcredentialsexception.getClass().getName() + ": " + invalidcredentialsexception.getMessage());
                }
                catch (Exception exception) {
                    Config.warn("Error opening OptiFine cape link");
                    Config.warn(exception.getClass().getName() + ": " + exception.getMessage());
                }
            }
            if (button.id == 220) {
                this.showMessage(Lang.get("of.message.capeOF.reloadCape"), 15000L);
                if (this.mc2.thePlayer != null) {
                    final long i = 15000L;
                    final long j = System.currentTimeMillis() + i;
                    this.mc2.thePlayer.setReloadCapeTimeMs(j);
                }
            }
            if (button.id == 230 && this.linkUrl != null) {
                GuiScreen.setClipboardString(this.linkUrl);
            }
        }
    }
    
    private void showMessage(final String msg, final long timeMs) {
        this.message = msg;
        this.messageHideTimeMs = System.currentTimeMillis() + timeMs;
        this.setLinkUrl(null);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
        if (this.message != null) {
            this.drawCenteredString(this.fontRenderer, this.message, this.width / 2, this.height / 6 + 60, 16777215);
            if (System.currentTimeMillis() > this.messageHideTimeMs) {
                this.message = null;
                this.setLinkUrl(null);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void setLinkUrl(final String linkUrl) {
        this.linkUrl = linkUrl;
        this.buttonCopyLink.visible = (linkUrl != null);
    }
}
