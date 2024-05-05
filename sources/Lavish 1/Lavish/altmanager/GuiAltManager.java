// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.altmanager;

import net.minecraft.client.gui.ScaledResolution;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Mouse;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import Lavish.utils.math.Random;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiAltManager extends GuiScreen
{
    private GuiButton login;
    private GuiButton remove;
    private GuiButton clipboard;
    private GuiButton randomcracked;
    private AltLoginThread loginThread;
    private int offset;
    public Alt selectedAlt;
    private String status;
    
    public GuiAltManager() {
        this.selectedAlt = null;
        this.status = EnumChatFormatting.GRAY + "Lavish Client";
    }
    
    public void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                if (this.loginThread == null) {
                    this.mc.displayGuiScreen(null);
                    break;
                }
                if (!this.loginThread.getStatus().equals(EnumChatFormatting.YELLOW + "Attempting to log in") && !this.loginThread.getStatus().equals(EnumChatFormatting.RED + "Do not hit back!" + EnumChatFormatting.YELLOW + " Logging in...")) {
                    this.mc.displayGuiScreen(null);
                    break;
                }
                this.loginThread.setStatus(EnumChatFormatting.RED + "Failed to login! Please try again!" + EnumChatFormatting.YELLOW + " Logging in...");
                break;
            }
            case 1: {
                final String user = this.selectedAlt.getUsername();
                final String pass = this.selectedAlt.getPassword();
                (this.loginThread = new AltLoginThread(user, pass)).start();
                break;
            }
            case 2: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                AltManager.registry.remove(this.selectedAlt);
                this.status = "§aRemoved.";
                this.selectedAlt = null;
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            }
            case 4: {
                this.mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            }
            case 5: {
                final String randomUsername = Random.generateRandomString();
                (this.loginThread = new AltLoginThread(randomUsername, "")).start();
                break;
            }
            case 6: {
                final Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                try {
                    if (((String)c.getData(DataFlavor.stringFlavor)).contains(":")) {
                        final String[] acc = ((String)c.getData(DataFlavor.stringFlavor)).split(":", 2);
                        (this.loginThread = new AltLoginThread(acc[0], acc[1])).start();
                    }
                }
                catch (UnsupportedFlavorException | IOException ex2) {
                    final Exception ex;
                    final Exception e = ex;
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        this.drawDefaultBackground();
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
            else if (wheel > 0) {
                this.offset -= 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }
        this.drawString(this.fontRendererObj, this.mc.session.getUsername(), 10.0f, 10.0f, -7829368);
        final FontRenderer fontRendererObj = this.fontRendererObj;
        final StringBuilder sb2 = new StringBuilder("Lavish Alt Manager - ");
        this.drawCenteredString(fontRendererObj, sb2.append(AltManager.registry.size()).append(" alts").toString(), GuiAltManager.width / 2, 10, -1);
        this.drawCenteredString(this.fontRendererObj, (this.loginThread == null) ? this.status : this.loginThread.getStatus(), GuiAltManager.width / 2, 20, -1);
        GL11.glPushMatrix();
        this.prepareScissorBox(0.0f, 33.0f, (float)GuiAltManager.width, (float)(GuiAltManager.height - 50));
        GL11.glEnable(3089);
        int y2 = 38;
        for (final Alt alt2 : AltManager.registry) {
            if (!this.isAltInArea(y2)) {
                continue;
            }
            final String name = alt2.getMask().equals("") ? alt2.getUsername() : alt2.getMask();
            final String pass = alt2.getPassword().equals("") ? "§cCracked" : alt2.getPassword().replaceAll(".", "*");
            this.drawCenteredString(this.fontRendererObj, name, GuiAltManager.width / 2, y2 - this.offset, -1);
            this.drawCenteredString(this.fontRendererObj, pass, GuiAltManager.width / 2, y2 - this.offset + 10, 5592405);
            y2 += 26;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        super.drawScreen(par1, par2, par3);
        if (this.selectedAlt == null) {
            this.login.enabled = false;
            this.remove.enabled = false;
        }
        else {
            this.login.enabled = true;
            this.remove.enabled = true;
        }
        if (Keyboard.isKeyDown(200)) {
            this.offset -= 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
        else if (Keyboard.isKeyDown(208)) {
            this.offset += 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, GuiAltManager.width / 2 + 4 + 50, GuiAltManager.height - 24, 100, 20, "Cancel"));
        this.login = new GuiButton(1, GuiAltManager.width / 2 - 154, GuiAltManager.height - 48, 100, 20, "Login");
        this.buttonList.add(this.login);
        this.remove = new GuiButton(2, GuiAltManager.width / 2 - 154, GuiAltManager.height - 24, 100, 20, "Remove");
        this.buttonList.add(this.remove);
        this.buttonList.add(new GuiButton(3, GuiAltManager.width / 2 + 4 + 50, GuiAltManager.height - 48, 100, 20, "Add"));
        this.buttonList.add(new GuiButton(4, GuiAltManager.width / 2 - 50, GuiAltManager.height - 48, 100, 20, "Direct Login"));
        this.randomcracked = new GuiButton(5, GuiAltManager.width / 2 - 480, GuiAltManager.height - 24, 100, 20, "Random Cracked Acc");
        this.clipboard = new GuiButton(6, GuiAltManager.width / 2 - 50, GuiAltManager.height - 24, 100, 20, "Clipboard");
        this.buttonList.add(this.clipboard);
        this.buttonList.add(this.randomcracked);
        this.login.enabled = false;
        this.remove.enabled = false;
    }
    
    private boolean isAltInArea(final int y2) {
        return y2 - this.offset <= GuiAltManager.height - 50;
    }
    
    private boolean isMouseOverAlt(final int x2, final int y2, final int y1) {
        return x2 >= 52 && y2 >= y1 - 4 && x2 <= GuiAltManager.width - 52 && y2 <= y1 + 20 && x2 >= 0 && y2 >= 33 && x2 <= GuiAltManager.width && y2 <= GuiAltManager.height - 50;
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        if (this.offset < 0) {
            this.offset = 0;
        }
        int y2 = 38 - this.offset;
        for (final Alt alt2 : AltManager.registry) {
            if (this.isMouseOverAlt(par1, par2, y2)) {
                if (alt2 == this.selectedAlt) {
                    this.actionPerformed(this.buttonList.get(1));
                    return;
                }
                this.selectedAlt = alt2;
            }
            y2 += 26;
        }
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void prepareScissorBox(final float x2, final float y2, final float x22, final float y22) {
        final ScaledResolution scale = new ScaledResolution(this.mc);
        final int factor = scale.getScaleFactor();
        GL11.glScissor((int)(x2 * factor), (int)((scale.getScaledHeight() - y22) * factor), (int)((x22 - x2) * factor), (int)((y22 - y2) * factor));
    }
}
