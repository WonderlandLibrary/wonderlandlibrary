// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.ui;

import java.io.IOException;
import net.minecraft.block.BlockCactus;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import Lavish.utils.auth.DiscordRequest;
import Lavish.Client;
import Lavish.utils.render.Render;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import Lavish.utils.misc.HWIDChecker;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class NotWhitelisted extends GuiScreen
{
    public String hwid;
    private GuiTextField uid;
    public static boolean isVerified;
    public static boolean failed;
    public static boolean isCopied;
    public static boolean failed2;
    public static boolean failed3;
    public static String AuthURL;
    public int seconds;
    public int ticks;
    
    static {
        NotWhitelisted.isVerified = false;
        NotWhitelisted.failed = false;
        NotWhitelisted.isCopied = false;
        NotWhitelisted.failed2 = false;
        NotWhitelisted.AuthURL = "https://pastebin.com/raw/2xpDhMHA";
    }
    
    public NotWhitelisted() {
        this.hwid = "Please wait";
        this.seconds = 10;
        this.ticks = 0;
    }
    
    @Override
    public void initGui() {
        try {
            this.hwid = HWIDChecker.getHWID();
        }
        catch (Exception e) {
            this.hwid = "ERROR";
        }
        Keyboard.enableRepeatEvents(true);
        NotWhitelisted.failed = false;
        NotWhitelisted.failed2 = false;
        this.uid = new GuiTextField(this.eventButton, this.mc.fontRendererObj, NotWhitelisted.width / 2 - 100, 200, 200, 20);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("Lavish/LavishBG.jpg"));
        Gui.drawModalRectWithCustomSizedTexture(0.0f, 0.0f, 0.0f, 0.0f, NotWhitelisted.width, NotWhitelisted.height, (float)NotWhitelisted.width, (float)NotWhitelisted.height);
        GlStateManager.pushMatrix();
        GlStateManager.translate(NotWhitelisted.width / 2.0f, NotWhitelisted.height / 10.0f, 0.0f);
        GlStateManager.scale(3.0f, 3.0f, 1.0f);
        GlStateManager.translate(-(NotWhitelisted.width / 2.0f), -(NotWhitelisted.height / 2.0f), 0.0f);
        this.drawString(this.mc.fontRendererObj, "L" + EnumChatFormatting.WHITE + "avish", sr.getScaledWidth() / 2.0f - 14.5f, sr.getScaledHeight() / 2.0f + 40.0f, -13485057);
        GlStateManager.popMatrix();
        Render.color(-16777216);
        Render.drawRoundedRect(sr.getScaledHeight() - 135, 198.0, 220.0, 25.0, 10.0f);
        this.uid.drawTextBox();
        if (this.uid.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Enter UID", (float)(NotWhitelisted.width / 2 - 96), 206.0f, -7829368);
        }
        if (NotWhitelisted.failed) {
            if (!NotWhitelisted.failed3) {
                try {
                    DiscordRequest.sendMessage("`FAILED AUTH`\n`HWID` " + HWIDChecker.getHWID() + "\n`UID` " + Client.instance.uid);
                    NotWhitelisted.failed3 = true;
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            this.mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.WHITE + "Failed to login, please contact" + EnumChatFormatting.RESET + " tear#9999 " + EnumChatFormatting.WHITE + "for support", (float)(this.mc.displayWidth / 2 / 2 - 132), 230.0f, -48574);
            this.mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.WHITE + "Your" + EnumChatFormatting.RESET + " HWID" + EnumChatFormatting.WHITE + " has been copied to your clipboard", (float)(this.mc.displayWidth / 2 / 2 - 115), 245.0f, -48574);
            if (!NotWhitelisted.isCopied) {
                try {
                    this.hwid = HWIDChecker.getHWID();
                }
                catch (Exception e2) {
                    this.hwid = "ERROR";
                }
                final String hwid1 = this.hwid;
                final StringSelection stringSelection = new StringSelection(hwid1);
                final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                NotWhitelisted.isCopied = true;
            }
        }
        if (NotWhitelisted.failed2) {
            if (this.ticks == 0) {
                this.seconds = 10;
            }
            if (this.ticks == 30) {
                this.seconds = 9;
            }
            if (this.ticks == 60) {
                this.seconds = 8;
            }
            if (this.ticks == 90) {
                this.seconds = 7;
            }
            if (this.ticks == 120) {
                this.seconds = 6;
            }
            if (this.ticks == 150) {
                this.seconds = 5;
            }
            if (this.ticks == 180) {
                this.seconds = 4;
            }
            if (this.ticks == 210) {
                this.seconds = 3;
            }
            if (this.ticks == 240) {
                this.seconds = 2;
            }
            if (this.ticks == 270) {
                this.seconds = 1;
            }
            if (this.ticks == 300) {
                this.mc.shutdownMinecraftApplet();
            }
            ++this.ticks;
            this.mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.WHITE + "Closing in " + EnumChatFormatting.RESET + this.seconds + "s", (float)(this.mc.displayWidth / 2 / 2 - 35), 265.0f, -48574);
        }
        if (!NotWhitelisted.failed) {
            this.mc.fontRendererObj.drawString(EnumChatFormatting.WHITE + "Press " + EnumChatFormatting.RESET + "ENTER " + EnumChatFormatting.WHITE + "to login", (float)(this.mc.displayWidth / 2 / 2 - 46), 230.0f, -13485057, true);
        }
    }
    
    @Override
    public void onGuiClosed() {
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) {
        if (par2 == 14) {
            this.uid.textboxKeyTyped(par1, par2);
        }
        else {
            try {
                Integer.parseInt(new StringBuilder(String.valueOf(par1)).toString());
                this.uid.textboxKeyTyped(par1, par2);
            }
            catch (Exception ex) {}
        }
        if (par1 == '\t' && this.uid.isFocused()) {
            this.uid.setFocused(!this.uid.isFocused());
        }
        if (par1 == '\r') {
            Client.instance.uid = this.uid.getText();
            BlockCactus.getState();
        }
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.uid.mouseClicked(par1, par2, par3);
    }
}
