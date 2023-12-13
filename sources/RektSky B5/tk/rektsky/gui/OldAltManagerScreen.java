/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui;

import cc.hyperium.utils.HyperiumFontRenderer;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import tk.rektsky.Client;

public class OldAltManagerScreen
extends GuiScreen {
    GuiScreen parentScreen;
    String username = "";
    Color bgCol = new Color(63, 63, 63, 166);
    static char[] validChars = new char[]{'q', 'w', 'e', 'r', 't', 'z', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'y', 'x', 'c', 'v', 'b', 'n', 'm', 'Q', 'W', 'E', 'R', 'T', 'Z', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Y', 'X', 'C', 'V', 'B', 'N', 'M', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', ',', ';', '?', '.', ':', '-', '*', '\u00a7', '\'', '\"', '+', '!', '%', '/', '=', '(', ')', '\\'};
    static ArrayList<Character> validCharsArr = OldAltManagerScreen.asArray(validChars);

    @Override
    public void initGui() {
        this.username = "";
    }

    public OldAltManagerScreen(GuiScreen parent) {
        this.parentScreen = parent;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        HyperiumFontRenderer fr = Client.getFont();
        ScaledResolution sr = new ScaledResolution(this.mc);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.3f, 1.3f, 0.0f);
        OldAltManagerScreen.Translate3DLayerEffect(this.width, this.height, mouseX, mouseY, 5);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("rektsky/images/background.png"));
        OldAltManagerScreen.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, this.width, this.height, this.width, this.height);
        GlStateManager.popMatrix();
        fr.drawString("Logged in as: " + this.mc.session.getUsername(), (float)(sr.getScaledWidth() / 2) - fr.getWidth("Logged in as: " + this.mc.session.getUsername()) / 2.0f, 120.0f, -1);
        int nameW = (int)fr.getWidth(this.username);
        int rectW = Math.min(100, nameW + 9);
        Gui.drawRect(sr.getScaledWidth() / 2 - rectW / 2, 120 + fr.FONT_HEIGHT + fr.FONT_HEIGHT - 2, sr.getScaledWidth() / 2 - rectW / 2 + rectW, 120 + fr.FONT_HEIGHT + fr.FONT_HEIGHT + fr.FONT_HEIGHT, this.bgCol.getRGB());
        fr.drawString(this.username, sr.getScaledWidth() / 2 - rectW / 2 + 4, 120 + fr.FONT_HEIGHT + 8, -1);
        Gui.drawRect((int)((float)(sr.getScaledWidth() / 2) - fr.getWidth(this.username) / 2.0f), 120 + fr.FONT_HEIGHT + fr.FONT_HEIGHT + fr.FONT_HEIGHT + 4, (int)((float)(sr.getScaledWidth() / 2) - fr.getWidth(this.username) / 2.0f + 4.0f + fr.getWidth("Login")), 120 + fr.FONT_HEIGHT + fr.FONT_HEIGHT + fr.FONT_HEIGHT + 6 + fr.FONT_HEIGHT, this.bgCol.getRGB());
        fr.drawString("Login", (int)((float)(sr.getScaledWidth() / 2) - fr.getWidth(this.username) / 2.0f), 120 + fr.FONT_HEIGHT + fr.FONT_HEIGHT + fr.FONT_HEIGHT + 4, -1);
    }

    public static void Translate3DLayerEffect(int width, int height, int mouseX, int mouseY, int layer) {
        GlStateManager.translate(((float)(-mouseX) + (float)width / 2.0f) / ((float)layer * 20.0f), ((float)(-mouseY) + (float)height / 2.0f) / ((float)layer * 20.0f), 0.0f);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        HyperiumFontRenderer fr = Client.getFont();
        ScaledResolution sr = new ScaledResolution(this.mc);
        int fx = (int)((float)(sr.getScaledWidth() / 2) - fr.getWidth(this.username) / 2.0f);
        int fy = 120 + fr.FONT_HEIGHT + fr.FONT_HEIGHT + fr.FONT_HEIGHT + 4;
        int fx2 = (int)((float)(sr.getScaledWidth() / 2) - fr.getWidth(this.username) / 2.0f + 4.0f + fr.getWidth("Login"));
        int fy2 = 120 + fr.FONT_HEIGHT + fr.FONT_HEIGHT + fr.FONT_HEIGHT + 6 + fr.FONT_HEIGHT;
        if (mouseX > fx && mouseY > fy && mouseX < fx2 && mouseY < fy2) {
            if (this.username.contains(":")) {
                try {
                    MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                    MicrosoftAuthResult result = authenticator.loginWithCredentials(this.username.split(":")[0], this.username.split(":")[1]);
                    this.mc.session = new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "mojang");
                }
                catch (Exception ex) {
                    try {
                        Client.sessionManager.setUser(this.username.split(":")[0], this.username.split(":")[1]);
                    }
                    catch (Exception exception) {}
                }
            } else {
                this.mc.session = new Session(this.username, this.mc.session.getPlayerID(), this.mc.session.getToken(), "legacy");
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == 14 && this.username.length() > 0) {
            this.username = this.username.substring(0, this.username.length() - 1);
        }
        if (keyCode == 47 && OldAltManagerScreen.isCtrlKeyDown()) {
            try {
                String data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                this.username = this.username + data;
            }
            catch (Exception exception) {}
        } else if (validCharsArr.contains(Character.valueOf(typedChar))) {
            this.username = this.username + typedChar;
        }
    }

    private static ArrayList<Character> asArray(char[] chars) {
        ArrayList<Character> arr = new ArrayList<Character>();
        for (char obj : chars) {
            arr.add(Character.valueOf(obj));
        }
        return arr;
    }
}

