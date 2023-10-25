/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.ui;

import java.io.IOException;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.ui.Alt;
import me.Tengoku.Terror.ui.AltLoginThread;
import me.Tengoku.Terror.ui.AltManager;
import me.Tengoku.Terror.ui.GuiAddAlt;
import me.Tengoku.Terror.ui.GuiAltLogin;
import me.Tengoku.Terror.ui.GuiRenameAlt;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiAltManager
extends GuiScreen {
    private GuiButton remove;
    private AltLoginThread loginThread;
    private GuiButton rename;
    private String status = (Object)((Object)EnumChatFormatting.GRAY) + "No alts selected";
    private int offset;
    public Alt selectedAlt = null;
    private GuiButton login;

    @Override
    public void drawScreen(int n, int n2, float f) {
        if (Mouse.hasWheel()) {
            int n3 = Mouse.getDWheel();
            if (n3 < 0) {
                this.offset += 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            } else if (n3 > 0) {
                this.offset -= 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }
        this.drawDefaultBackground();
        this.drawString(this.fontRendererObj, this.mc.session.getUsername(), 10, 10, -7829368);
        FontRenderer fontRenderer = this.fontRendererObj;
        StringBuilder stringBuilder = new StringBuilder("Account Manager - ");
        this.drawCenteredString(fontRenderer, stringBuilder.append(AltManager.registry.size()).append(" alts").toString(), width / 2, 10, -1);
        this.drawCenteredString(this.fontRendererObj, this.loginThread == null ? this.status : this.loginThread.getStatus(), width / 2, 20, -1);
        Gui.drawRect(50.0, 33.0, width - 50, height - 50, -16777216);
        GL11.glPushMatrix();
        this.prepareScissorBox(0.0f, 33.0f, width, height - 50);
        GL11.glEnable((int)3089);
        int n4 = 38;
        AltManager altManager = Exodus.INSTANCE.altManager;
        for (Alt alt : AltManager.registry) {
            String string;
            if (!this.isAltInArea(n4)) continue;
            String string2 = alt.getMask().equals("") ? alt.getUsername() : alt.getMask();
            String string3 = string = alt.getPassword().equals("") ? "\ufffdcCracked" : alt.getPassword().replaceAll(".", "*");
            if (alt == this.selectedAlt) {
                if (this.isMouseOverAlt(n, n2, n4 - this.offset) && Mouse.isButtonDown((int)0)) {
                    Gui.drawRect(52.0, n4 - this.offset - 4, width - 52, n4 - this.offset + 20, -2142943931);
                } else if (this.isMouseOverAlt(n, n2, n4 - this.offset)) {
                    Gui.drawRect(52.0, n4 - this.offset - 4, width - 52, n4 - this.offset + 20, -2142088622);
                } else {
                    Gui.drawRect(52.0, n4 - this.offset - 4, width - 52, n4 - this.offset + 20, -2144259791);
                }
            } else if (this.isMouseOverAlt(n, n2, n4 - this.offset) && Mouse.isButtonDown((int)0)) {
                Gui.drawRect(52.0, n4 - this.offset - 4, width - 52, n4 - this.offset + 20, -16777216);
            } else if (this.isMouseOverAlt(n, n2, n4 - this.offset)) {
                Gui.drawRect(52.0, n4 - this.offset - 4, width - 52, n4 - this.offset + 20, -16777216);
            }
            this.drawCenteredString(this.fontRendererObj, string2, width / 2, n4 - this.offset, -1);
            this.drawCenteredString(this.fontRendererObj, string, width / 2, n4 - this.offset + 10, 0x555555);
            n4 += 26;
        }
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        super.drawScreen(n, n2, f);
        if (this.selectedAlt == null) {
            this.login.enabled = false;
            this.remove.enabled = false;
            this.rename.enabled = false;
        } else {
            this.login.enabled = true;
            this.remove.enabled = true;
            this.rename.enabled = true;
        }
        if (Keyboard.isKeyDown((int)200)) {
            this.offset -= 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        } else if (Keyboard.isKeyDown((int)208)) {
            this.offset += 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
    }

    private boolean isAltInArea(int n) {
        return n - this.offset <= height - 50;
    }

    private boolean isMouseOverAlt(int n, int n2, int n3) {
        return n >= 52 && n2 >= n3 - 4 && n <= width - 52 && n2 <= n3 + 20 && n >= 0 && n2 >= 33 && n <= width && n2 <= height - 50;
    }

    public void prepareScissorBox(float f, float f2, float f3, float f4) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int n = scaledResolution.getScaleFactor();
        GL11.glScissor((int)((int)(f * (float)n)), (int)((int)(((float)scaledResolution.getScaledHeight() - f4) * (float)n)), (int)((int)((f3 - f) * (float)n)), (int)((int)((f4 - f2) * (float)n)));
    }

    @Override
    public void actionPerformed(GuiButton guiButton) throws IOException {
        switch (guiButton.id) {
            case 0: {
                if (this.loginThread == null) {
                    this.mc.displayGuiScreen(null);
                    break;
                }
                if (!this.loginThread.getStatus().equals((Object)((Object)EnumChatFormatting.YELLOW) + "Attempting to log in") && !this.loginThread.getStatus().equals((Object)((Object)EnumChatFormatting.RED) + "Do not hit back!" + (Object)((Object)EnumChatFormatting.YELLOW) + " Logging in...")) {
                    this.mc.displayGuiScreen(null);
                    break;
                }
                this.loginThread.setStatus((Object)((Object)EnumChatFormatting.RED) + "Failed to login! Please try again!" + (Object)((Object)EnumChatFormatting.YELLOW) + " Logging in...");
                break;
            }
            case 1: {
                String string = this.selectedAlt.getUsername();
                String string2 = this.selectedAlt.getPassword();
                this.loginThread = new AltLoginThread(string, string2);
                this.loginThread.start();
                break;
            }
            case 2: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                AltManager altManager = Exodus.INSTANCE.altManager;
                AltManager.registry.remove(this.selectedAlt);
                this.status = "\ufffdaRemoved.";
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
            case 6: {
                this.mc.displayGuiScreen(new GuiRenameAlt(this));
            }
        }
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 + 4 + 50, height - 24, 100, 20, "Cancel"));
        this.login = new GuiButton(1, width / 2 - 154, height - 48, 100, 20, "Login");
        this.buttonList.add(this.login);
        this.remove = new GuiButton(2, width / 2 - 154, height - 24, 100, 20, "Remove");
        this.buttonList.add(this.remove);
        this.buttonList.add(new GuiButton(3, width / 2 + 4 + 50, height - 48, 100, 20, "Add"));
        this.buttonList.add(new GuiButton(4, width / 2 - 50, height - 48, 100, 20, "Direct Login"));
        this.rename = new GuiButton(6, width / 2 - 50, height - 24, 100, 20, "Edit");
        this.buttonList.add(this.rename);
        this.login.enabled = false;
        this.remove.enabled = false;
        this.rename.enabled = false;
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        if (this.offset < 0) {
            this.offset = 0;
        }
        int n4 = 38 - this.offset;
        AltManager altManager = Exodus.INSTANCE.altManager;
        for (Alt alt : AltManager.registry) {
            if (this.isMouseOverAlt(n, n2, n4)) {
                if (alt == this.selectedAlt) {
                    this.actionPerformed((GuiButton)this.buttonList.get(1));
                    return;
                }
                this.selectedAlt = alt;
            }
            n4 += 26;
        }
        try {
            super.mouseClicked(n, n2, n3);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
}

