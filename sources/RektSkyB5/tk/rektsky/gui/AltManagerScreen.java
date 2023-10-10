/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui;

import cc.hyperium.utils.HyperiumFontRenderer;
import com.thealtening.auth.service.AlteningServiceType;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.opengl.GL11;
import org.yaml.snakeyaml.Yaml;
import tk.rektsky.Client;
import tk.rektsky.utils.file.FileManager;

public class AltManagerScreen
extends GuiScreen {
    GuiScreen parentScreen;
    String username = "";
    Color bgCol = new Color(63, 63, 63, 166);
    Color btnColor = new Color(5, 153, 217);
    Color hoverColor = new Color(212, 212, 212);
    private GuiTextField usernameField;
    private GuiTextField passwordField;
    private GuiTextField proxyHost;
    private GuiTextField proxyPort;
    boolean altManagerScreen = false;
    boolean proxyScreen = false;
    Yaml yaml = new Yaml();
    static char[] validChars = new char[]{'q', 'w', 'e', 'r', 't', 'z', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'y', 'x', 'c', 'v', 'b', 'n', 'm', 'Q', 'W', 'E', 'R', 'T', 'Z', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Y', 'X', 'C', 'V', 'B', 'N', 'M', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', ',', ';', '?', '.', ':', '-', '*', '\u00a7', '\'', '\"', '+', '!', '%', '/', '=', '(', ')', '\\', '@'};
    static ArrayList<Character> validCharsArr = AltManagerScreen.asArray(validChars);

    @Override
    public void initGui() {
        this.username = "";
        this.yaml = new Yaml();
        this.usernameField = new GuiTextField(0, this.mc.fontRendererObj, 4, 4, 200, 20);
        this.passwordField = new GuiTextField(1, this.mc.fontRendererObj, 4, 4, 200, 20);
        this.proxyHost = new GuiTextField(2, this.mc.fontRendererObj, 4, 4, 200, 20);
        this.proxyPort = new GuiTextField(3, this.mc.fontRendererObj, 4, 4, 200, 20);
        try {
            FileManager.altInfo = (Map)this.yaml.load(new FileInputStream(FileManager.ALTS_PATH));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        if (FileManager.altInfo == null) {
            FileManager.altInfo = new HashMap<String, String>();
        }
    }

    public AltManagerScreen(GuiScreen parent) {
        this.parentScreen = parent;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int offset1X;
        int yy;
        int xx;
        int hi;
        int wi;
        HyperiumFontRenderer fr = Client.getFont();
        ScaledResolution sr = new ScaledResolution(this.mc);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.3f, 1.3f, 0.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("rektsky/images/background.png"));
        AltManagerScreen.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, this.width, this.height, this.width, this.height);
        GlStateManager.popMatrix();
        int w2 = 100;
        this.mc.fontRendererObj.drawString("", -1, -1, -1);
        Gui.drawRect(0, 0, w2, sr.getScaledHeight(), this.bgCol.getRGB());
        if (this.mc.getSession().getUsername().length() > 7) {
            Client.getFontBig().drawString("Alts - " + this.mc.getSession().getUsername().substring(0, 7) + "...", 4.0f, 4.0f, -1);
        } else {
            Client.getFontBig().drawString("Alts - " + this.mc.getSession().getUsername(), 4.0f, 4.0f, -1);
        }
        if (mouseX > 4 && mouseY > 25) {
            if (mouseX < 29 && mouseY < 50) {
                this.drawRoundedRect(4, 25, 35, 15, 5, this.hoverColor);
            } else {
                this.drawRoundedRect(4, 25, 35, 15, 5, Color.WHITE);
            }
        } else {
            this.drawRoundedRect(4, 25, 35, 15, 5, Color.WHITE);
        }
        if (mouseX > 43 && mouseY > 25) {
            if (mouseX < 68 && mouseY < 50) {
                this.drawRoundedRect(43, 25, 37, 15, 5, this.hoverColor);
            } else {
                this.drawRoundedRect(43, 25, 37, 15, 5, Color.WHITE);
            }
        } else {
            this.drawRoundedRect(43, 25, 37, 15, 5, Color.WHITE);
        }
        Client.getFont().drawString("Add", 13.0f, 28.0f, this.btnColor.getRGB());
        Client.getFont().drawString("Proxies", 45.0f, 28.0f, this.btnColor.getRGB());
        if (this.altManagerScreen) {
            wi = 170;
            hi = 70;
            xx = (sr.getScaledWidth() - wi * 2) / 2;
            yy = 100;
            this.drawRoundedRect(xx, yy, xx + wi, yy + hi, 7, this.bgCol);
            this.usernameField.xPosition = xx + 135;
            this.usernameField.yPosition = yy + 15;
            this.passwordField.xPosition = xx + 135;
            this.passwordField.yPosition = yy + 45;
            this.usernameField.drawTextBox();
            this.passwordField.drawTextBox();
            offset1X = 125;
            int offset1Y = 150;
            Color c1 = Color.WHITE;
            int offset2X = offset1X + 65;
            int offset2Y = 150;
            Color c2 = Color.WHITE;
            int offset3X = offset2X + 50;
            int offset3Y = 150;
            Color c3 = Color.WHITE;
            if (mouseX > xx + offset1X && mouseY > yy + offset1Y && mouseX < xx + offset1X + (int)(9.0f + Client.getFontBig().getWidth("Add Alt")) && mouseY < yy + offset1Y + 18) {
                c1 = this.hoverColor;
            }
            if (mouseX > xx + offset2X && mouseY > yy + offset2Y && mouseX < xx + offset2X + (int)(9.0f + Client.getFontBig().getWidth("Login")) && mouseY < yy + offset2Y + 18) {
                c2 = this.hoverColor;
            }
            if (mouseX > xx + offset3X && mouseY > yy + offset3Y && mouseX < xx + offset3X + (int)(9.0f + Client.getFontBig().getWidth("MSA Web login")) && mouseY < yy + offset3Y + 18) {
                c3 = this.hoverColor;
            }
            this.drawRoundedRect(xx + offset1X, yy + offset1Y, (int)(9.0f + Client.getFontBig().getWidth("Add Alt")), 18, 5, c1);
            Client.getFontBig().drawString("Add Alt", xx + 4 + offset1X, yy + 2 + offset1Y, this.btnColor.getRGB());
            this.drawRoundedRect(xx + offset2X, yy + offset2Y, (int)(9.0f + Client.getFontBig().getWidth("Login")), 18, 5, c2);
            Client.getFontBig().drawString("Login", xx + 4 + offset2X, yy + 2 + offset2Y, this.btnColor.getRGB());
            this.drawRoundedRect(xx + offset3X, yy + offset3Y, (int)(9.0f + Client.getFontBig().getWidth("MSA Web login")), 18, 5, c3);
            Client.getFontBig().drawString("MSA Web login", xx + 4 + offset3X, yy + 2 + offset3Y, this.btnColor.getRGB());
        }
        if (this.proxyScreen) {
            wi = 170;
            hi = 70;
            xx = (sr.getScaledWidth() - wi * 2) / 2;
            yy = 100;
            this.drawRoundedRect(xx, yy, xx + wi, yy + hi, 7, this.bgCol);
            this.proxyHost.xPosition = xx + 135;
            this.proxyHost.yPosition = yy + 15;
            this.proxyPort.xPosition = xx + 135;
            this.proxyPort.yPosition = yy + 45;
            this.proxyHost.drawTextBox();
            this.proxyPort.drawTextBox();
            offset1X = 125;
            int offset2X = offset1X + 94;
            int offset2Y = 150;
            Color c2 = Color.WHITE;
            if (mouseX > xx + offset2X && mouseY > yy + offset2Y && mouseX < xx + offset2X + (int)(9.0f + Client.getFontBig().getWidth("Use")) && mouseY < yy + offset2Y + 18) {
                c2 = this.hoverColor;
            }
            this.drawRoundedRect(xx + offset2X, yy + offset2Y, (int)(9.0f + Client.getFontBig().getWidth("Use")), 18, 5, c2);
            Client.getFontBig().drawString("Use", xx + 4 + offset2X, yy + 2 + offset2Y, this.btnColor.getRGB());
        }
        int y2 = 50;
        for (String username : FileManager.altInfo.keySet()) {
            String password = FileManager.altInfo.get(username);
            boolean cracked = password.isEmpty();
            if (username.length() > 8) {
                Gui.drawRect(0, y2, 100, y2 + 50, this.bgCol.getRGB());
                Client.getFont().drawString("Email: " + username.substring(0, 8) + "...", 4.0f, y2 + 4, Color.WHITE.getRGB());
                Client.getFont().drawString("Cracked: " + cracked, 4.0f, y2 + 4 + Client.getFont().FONT_HEIGHT + 4, Color.WHITE.getRGB());
                if (mouseX > 4 && mouseY > y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8) {
                    if (mouseX < 39 && mouseY < y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8 + 15) {
                        this.drawRoundedRect(4, y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8, 35, 15, 5, this.hoverColor);
                    } else {
                        this.drawRoundedRect(4, y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8, 35, 15, 5, Color.WHITE);
                    }
                } else {
                    this.drawRoundedRect(4, y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8, 35, 15, 5, Color.WHITE);
                }
                Client.getFont().drawString("Use", 12.0f, y2 + 3 + Client.getFont().FONT_HEIGHT * 2 + 8 + 4, this.btnColor.getRGB());
                if (mouseX > 45 && mouseY > y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8) {
                    if (mouseX < 80 && mouseY < y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8 + 15) {
                        this.drawRoundedRect(45, y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8, 35, 15, 5, this.hoverColor);
                    } else {
                        this.drawRoundedRect(45, y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8, 35, 15, 5, Color.WHITE);
                    }
                } else {
                    this.drawRoundedRect(45, y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8, 35, 15, 5, Color.WHITE);
                }
                Client.getFont().drawString("Delete", 48.0f, y2 + 3 + Client.getFont().FONT_HEIGHT * 2 + 8 + 4, Color.RED.getRGB());
            } else {
                Gui.drawRect(0, y2, 100, y2 + 50, this.bgCol.getRGB());
                Client.getFont().drawString("Username: " + username, 4.0f, y2 + 4, Color.WHITE.getRGB());
                Client.getFont().drawString("Cracked: " + cracked, 4.0f, y2 + 4 + Client.getFont().FONT_HEIGHT + 4, Color.WHITE.getRGB());
                if (mouseX > 4 && mouseY > y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8) {
                    if (mouseX < 39 && mouseY < y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8 + 15) {
                        this.drawRoundedRect(4, y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8, 35, 15, 5, this.hoverColor);
                    } else {
                        this.drawRoundedRect(4, y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8, 35, 15, 5, Color.WHITE);
                    }
                } else {
                    this.drawRoundedRect(4, y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8, 35, 15, 5, Color.WHITE);
                }
                Client.getFont().drawString("Use", 12.0f, y2 + 3 + Client.getFont().FONT_HEIGHT * 2 + 8 + 4, this.btnColor.getRGB());
                if (mouseX > 45 && mouseY > y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8) {
                    if (mouseX < 80 && mouseY < y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8 + 15) {
                        this.drawRoundedRect(45, y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8, 35, 15, 5, this.hoverColor);
                    } else {
                        this.drawRoundedRect(45, y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8, 35, 15, 5, Color.WHITE);
                    }
                } else {
                    this.drawRoundedRect(45, y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8, 35, 15, 5, Color.WHITE);
                }
                Client.getFont().drawString("Delete", 48.0f, y2 + 3 + Client.getFont().FONT_HEIGHT * 2 + 8 + 4, Color.RED.getRGB());
            }
            y2 += 50;
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("rektsky/alux.png"));
        this.drawRoundedRect(sr.getScaledWidth() / 2 + 20, sr.getScaledHeight() / 2 + 15, 100, 100, 5, new Color(50, 50, 50, 150));
        GlStateManager.color(255.0f, 255.0f, 255.0f);
        Gui.drawModalRectWithCustomSizedTexture(sr.getScaledWidth() / 2 + 25, sr.getScaledHeight() / 2 + 20, 0.0f, 0.0f, 90, 90, 90.0f, 90.0f);
        this.drawRoundedRect(sr.getScaledWidth() / 2 - 137, sr.getScaledHeight() / 2 + 130, (int)Client.getFontBig().getWidth("partnered with AluxServices, a high quality alt shop and generator") + 6, Client.getFontBig().FONT_HEIGHT + 6, 2, new Color(50, 50, 50, 150));
        Client.getFontBig().drawCenteredString("partnered with AluxServices, a high quality alt shop and generator", sr.getScaledWidth() / 2 + 72, sr.getScaledHeight() / 2 + 130, -1);
    }

    public static void Translate3DLayerEffect(int width, int height, int mouseX, int mouseY, int layer) {
        GlStateManager.translate(((float)(-mouseX) + (float)width / 2.0f) / ((float)layer * 20.0f), ((float)(-mouseY) + (float)height / 2.0f) / ((float)layer * 20.0f), 0.0f);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int offset1X;
        int yy;
        int xx;
        int hi;
        int wi;
        HyperiumFontRenderer fr = Client.getFont();
        ScaledResolution sr = new ScaledResolution(this.mc);
        if (this.altManagerScreen) {
            this.usernameField.mouseClicked(mouseX, mouseY, mouseButton);
            this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
            wi = 170;
            hi = 70;
            xx = (sr.getScaledWidth() - wi * 2) / 2;
            yy = 100;
            this.drawRoundedRect(xx, yy, xx + wi, yy + hi, 7, this.bgCol);
            offset1X = 125;
            int offset1Y = 150;
            int offset2X = offset1X + 65;
            int offset2Y = 150;
            if (mouseX > xx + offset1X && mouseY > yy + offset1Y && mouseX < xx + offset1X + (int)(9.0f + Client.getFontBig().getWidth("Add Alt")) && mouseY < yy + offset1Y + 18) {
                this.altManagerScreen = false;
                boolean success = false;
                if (this.passwordField.getText().isEmpty()) {
                    if (!this.usernameField.getText().isEmpty()) {
                        FileManager.altInfo.put(this.usernameField.getText(), "");
                        success = true;
                    }
                } else {
                    try {
                        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                        MicrosoftAuthResult result = authenticator.loginWithCredentials(this.usernameField.getText(), this.passwordField.getText());
                        success = true;
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        try {
                            success = Client.sessionManager.validateUser(this.usernameField.getText(), this.passwordField.getText());
                        }
                        catch (Exception result) {
                            // empty catch block
                        }
                    }
                }
                if (success) {
                    FileManager.altInfo.put(this.usernameField.getText(), this.passwordField.getText());
                    try {
                        String yamlInfo = this.yaml.dump(FileManager.altInfo);
                        FileOutputStream fos = new FileOutputStream(FileManager.ALTS_PATH);
                        fos.write(yamlInfo.getBytes(StandardCharsets.UTF_8));
                        fos.close();
                    }
                    catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            if (mouseX > xx + offset2X && mouseY > yy + offset2Y && mouseX < xx + offset2X + (int)(9.0f + Client.getFontBig().getWidth("Login")) && mouseY < yy + offset2Y + 18) {
                this.altManagerScreen = false;
                if (this.passwordField.getText().isEmpty() && !this.usernameField.getText().endsWith("@alt.com")) {
                    if (!this.usernameField.getText().isEmpty()) {
                        Client.sessionManager.setUserOffline(this.usernameField.getText());
                    }
                } else {
                    try {
                        if (this.usernameField.getText().endsWith("@alt.com")) {
                            Client.theAltening.updateService(AlteningServiceType.THEALTENING);
                            Client.sessionManager.setUser(this.usernameField.getText(), "a a");
                        } else {
                            MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                            MicrosoftAuthResult result = authenticator.loginWithCredentials(this.usernameField.getText(), this.passwordField.getText());
                            Client.theAltening.updateService(AlteningServiceType.MOJANG);
                            this.mc.session = new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "mojang");
                        }
                    }
                    catch (Exception ex) {
                        try {
                            Client.theAltening.updateService(AlteningServiceType.MOJANG);
                            Client.sessionManager.setUser(this.usernameField.getText(), this.passwordField.getText());
                        }
                        catch (Exception result) {
                            // empty catch block
                        }
                    }
                }
            }
        }
        if (this.proxyScreen) {
            this.proxyHost.mouseClicked(mouseX, mouseY, mouseButton);
            this.proxyPort.mouseClicked(mouseX, mouseY, mouseButton);
        }
        wi = 170;
        hi = 70;
        xx = (sr.getScaledWidth() - wi * 2) / 2;
        yy = 100;
        if (this.proxyScreen) {
            offset1X = 125;
            int offset2X = offset1X + 94;
            int offset2Y = 150;
            if (mouseX > xx + offset2X && mouseY > yy + offset2Y && mouseX < xx + offset2X + (int)(9.0f + Client.getFontBig().getWidth("Use")) && mouseY < yy + offset2Y + 18) {
                this.proxyScreen = false;
                this.mc.proxy = this.proxyHost.getText().isEmpty() || this.proxyPort.getText().isEmpty() ? Proxy.NO_PROXY : new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.proxyHost.getText(), Integer.parseInt(this.proxyPort.getText())));
            }
        }
        boolean shouldClose = mouseX > xx && (double)mouseX < (double)xx + (double)wi * 2.8 && mouseY > yy && (double)mouseY < (double)yy + (double)hi * 2.3;
        boolean bl = shouldClose = !shouldClose;
        if (mouseX > 4 && mouseY > 25) {
            if (mouseX < 29 && mouseY < 50) {
                this.altManagerScreen = true;
                this.proxyScreen = false;
            } else if (shouldClose) {
                this.altManagerScreen = false;
                this.proxyScreen = false;
            }
        } else if (shouldClose) {
            this.altManagerScreen = false;
            this.proxyScreen = false;
        }
        if (mouseX > 43 && mouseY > 25 && mouseX < 68 && mouseY < 50) {
            this.proxyScreen = true;
        }
        int y2 = 50;
        for (String username : FileManager.altInfo.keySet()) {
            String password = FileManager.altInfo.get(username);
            boolean cracked = password.isEmpty();
            if (mouseX > 4 && mouseY > y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8 && mouseX < 39 && mouseY < y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8 + 15) {
                if (cracked) {
                    Client.sessionManager.setUserOffline(username);
                } else {
                    try {
                        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                        MicrosoftAuthResult result = authenticator.loginWithCredentials(username, password);
                        this.mc.session = new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "mojang");
                    }
                    catch (Exception ex) {
                        try {
                            Client.sessionManager.setUser(username, password);
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                }
            }
            if (mouseX > 45 && mouseY > y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8 && mouseX < 80 && mouseY < y2 + 4 + Client.getFont().FONT_HEIGHT * 2 + 8 + 15) {
                FileManager.altInfo.remove(username);
            }
            y2 += 50;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.usernameField.textboxKeyTyped(typedChar, keyCode);
        this.passwordField.textboxKeyTyped(typedChar, keyCode);
        this.proxyHost.textboxKeyTyped(typedChar, keyCode);
        this.proxyPort.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
        if (keyCode == 14 && this.username.length() > 0) {
            this.username = this.username.substring(0, this.username.length() - 1);
        }
        if (keyCode == 47 && AltManagerScreen.isCtrlKeyDown()) {
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

    private void drawRoundedRect(int x2, int y2, int width, int height, int cornerRadius, Color color) {
        Gui.drawRect(x2, y2 + cornerRadius, x2 + cornerRadius, y2 + height - cornerRadius, color.getRGB());
        Gui.drawRect(x2 + cornerRadius, y2, x2 + width - cornerRadius, y2 + height, color.getRGB());
        Gui.drawRect(x2 + width - cornerRadius, y2 + cornerRadius, x2 + width, y2 + height - cornerRadius, color.getRGB());
        this.drawArc(x2 + cornerRadius, y2 + cornerRadius, cornerRadius, 0, 90, color);
        this.drawArc(x2 + width - cornerRadius, y2 + cornerRadius, cornerRadius, 270, 360, color);
        this.drawArc(x2 + width - cornerRadius, y2 + height - cornerRadius, cornerRadius, 180, 270, color);
        this.drawArc(x2 + cornerRadius, y2 + height - cornerRadius, cornerRadius, 90, 180, color);
    }

    private void drawArc(int x2, int y2, int radius, int startAngle, int endAngle, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        worldRenderer.begin(6, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x2, y2, 0.0).endVertex();
        for (int i2 = (int)((double)startAngle / 360.0 * 100.0); i2 <= (int)((double)endAngle / 360.0 * 100.0); ++i2) {
            double angle = Math.PI * 2 * (double)i2 / 100.0 + Math.toRadians(180.0);
            worldRenderer.pos((double)x2 + Math.sin(angle) * (double)radius, (double)y2 + Math.cos(angle) * (double)radius, 0.0).endVertex();
        }
        Tessellator.getInstance().draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
}

