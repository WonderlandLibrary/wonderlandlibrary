package net.optifine.gui;

import com.mojang.authlib.exceptions.InvalidCredentialsException;
import java.math.BigInteger;
import java.net.URI;
import java.util.Random;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.src.Config;
import net.optifine.Lang;
import net.optifine.gui.GuiButtonOF;
import net.optifine.gui.GuiScreenOF;

public class GuiScreenCapeOF
extends GuiScreenOF {
    private final GuiScreen parentScreen;
    private String title;
    private String message;
    private long messageHideTimeMs;
    private String linkUrl;
    private GuiButtonOF buttonCopyLink;
    private FontRenderer fontRenderer;

    public GuiScreenCapeOF(GuiScreen parentScreenIn) {
        this.fontRenderer = Config.getMinecraft().fontRendererObj;
        this.parentScreen = parentScreenIn;
    }

    public void initGui() {
        int i = 0;
        this.title = I18n.format((String)"of.options.capeOF.title", (Object[])new Object[0]);
        this.buttonList.add((Object)new GuiButtonOF(210, width / 2 - 155, height / 6 + 24 * ((i += 2) >> 1), 150, 20, I18n.format((String)"of.options.capeOF.openEditor", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButtonOF(220, width / 2 - 155 + 160, height / 6 + 24 * (i >> 1), 150, 20, I18n.format((String)"of.options.capeOF.reloadCape", (Object[])new Object[0])));
        this.buttonCopyLink = new GuiButtonOF(230, width / 2 - 100, height / 6 + 24 * ((i += 6) >> 1), 200, 20, I18n.format((String)"of.options.capeOF.copyEditorLink", (Object[])new Object[0]));
        this.buttonCopyLink.visible = this.linkUrl != null;
        this.buttonList.add((Object)this.buttonCopyLink);
        this.buttonList.add((Object)new GuiButtonOF(200, width / 2 - 100, height / 6 + 24 * ((i += 4) >> 1), I18n.format((String)"gui.done", (Object[])new Object[0])));
    }

    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 200) {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            if (button.id == 210) {
                try {
                    String s = this.mc.getSession().getProfile().getName();
                    String s1 = this.mc.getSession().getProfile().getId().toString().replace((CharSequence)"-", (CharSequence)"");
                    String s2 = this.mc.getSession().getToken();
                    Random random = new Random();
                    Random random1 = new Random((long)System.identityHashCode((Object)new Object()));
                    BigInteger biginteger = new BigInteger(128, random);
                    BigInteger biginteger1 = new BigInteger(128, random1);
                    BigInteger biginteger2 = biginteger.xor(biginteger1);
                    String s3 = biginteger2.toString(16);
                    this.mc.getSessionService().joinServer(this.mc.getSession().getProfile(), s2, s3);
                    String s4 = "https://optifine.net/capeChange?u=" + s1 + "&n=" + s + "&s=" + s3;
                    boolean flag = Config.openWebLink((URI)new URI(s4));
                    if (flag) {
                        this.showMessage(Lang.get((String)"of.message.capeOF.openEditor"), 10000L);
                    } else {
                        this.showMessage(Lang.get((String)"of.message.capeOF.openEditorError"), 10000L);
                        this.setLinkUrl(s4);
                    }
                }
                catch (InvalidCredentialsException invalidcredentialsexception) {
                    Config.showGuiMessage((String)I18n.format((String)"of.message.capeOF.error1", (Object[])new Object[0]), (String)I18n.format((String)"of.message.capeOF.error2", (Object[])new Object[]{invalidcredentialsexception.getMessage()}));
                    Config.warn((String)"Mojang authentication failed");
                    Config.warn((String)(invalidcredentialsexception.getClass().getName() + ": " + invalidcredentialsexception.getMessage()));
                }
                catch (Exception exception) {
                    Config.warn((String)"Error opening OptiFine cape link");
                    Config.warn((String)(exception.getClass().getName() + ": " + exception.getMessage()));
                }
            }
            if (button.id == 220) {
                this.showMessage(Lang.get((String)"of.message.capeOF.reloadCape"), 15000L);
                if (this.mc.thePlayer != null) {
                    long i = 15000L;
                    long j = System.currentTimeMillis() + i;
                    this.mc.thePlayer.setReloadCapeTimeMs(j);
                }
            }
            if (button.id == 230 && this.linkUrl != null) {
                GuiScreenCapeOF.setClipboardString((String)this.linkUrl);
            }
        }
    }

    private void showMessage(String msg, long timeMs) {
        this.message = msg;
        this.messageHideTimeMs = System.currentTimeMillis() + timeMs;
        this.setLinkUrl(null);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.title, width / 2, 20, 0xFFFFFF);
        if (this.message != null) {
            this.drawCenteredString(this.fontRenderer, this.message, width / 2, height / 6 + 60, 0xFFFFFF);
            if (System.currentTimeMillis() > this.messageHideTimeMs) {
                this.message = null;
                this.setLinkUrl(null);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
        this.buttonCopyLink.visible = linkUrl != null;
    }
}
