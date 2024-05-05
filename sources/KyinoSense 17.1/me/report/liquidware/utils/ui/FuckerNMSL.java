/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 */
package me.report.liquidware.utils.ui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import me.report.liquidware.gui.GuiMainMenu;
import me.report.liquidware.utils.ui.utils.EmptyInputBox;
import me.report.liquidware.utils.ui.utils.RenderUtils;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.ColorUti;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import obfuscator.NativeMethod;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

@NativeMethod.Obfuscation(flags="")
public class FuckerNMSL
extends GuiScreen {
    public static EmptyInputBox username;
    static EmptyInputBox password;
    private boolean loginsuccessfully = false;
    int anim = 140;
    String hwid = this.getHWID();
    public static String Name;
    public static String version;
    public GuiButton loginButton;
    public GuiButton freeButton;
    public String UserName = null;
    public String Password = null;
    public static boolean isload;
    private float fraction;
    public String HWID = null;
    public static int LOVEU;
    public static boolean Passed;
    public static String process;
    public static String Now;
    public GuiScreen prevGui;
    private float hHeight = 540.0f;
    private float hWidth = 960.0f;
    private float errorBoxHeight = 0.0f;

    public void func_73866_w_() {
        super.func_73866_w_();
        username = new EmptyInputBox(4, this.field_146297_k.field_71466_p, 20, 150, 100, 20);
        password = new EmptyInputBox(4, this.field_146297_k.field_71466_p, 20, 180, 100, 20);
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        Display.setTitle((String)"KyinoClient | Username: Enter your name");
        ScaledResolution sr = new ScaledResolution(this.field_146297_k);
        RenderUtils.drawImage(0, 0, sr.func_78326_a(), sr.func_78328_b(), new ResourceLocation("liquidbounce/loginbackground.png"));
        FuckerNMSL.username.yPosition = 100;
        FuckerNMSL.password.yPosition = FuckerNMSL.username.yPosition + 30;
        RenderUtils.drawRect(0.0f, 0.0f, 140.0f, sr.func_78328_b(), new Color(68, 68, 68, 150).getRGB());
        Fonts.font40.func_78276_b("Login to KyinoClient", 30, 65, ColorUti.rainbow(36000000000L, 255, 0.5f).getRGB());
        RenderUtils.drawRoundRect(FuckerNMSL.username.xPosition, FuckerNMSL.username.yPosition, FuckerNMSL.username.xPosition + username.getWidth(), FuckerNMSL.username.yPosition + 20, username.isFocused() ? new Color(71, 71, 71).getRGB() : new Color(149, 149, 149).getRGB());
        RenderUtils.drawRoundRect((float)FuckerNMSL.username.xPosition + 0.5f, (float)FuckerNMSL.username.yPosition + 0.5f, (float)(FuckerNMSL.username.xPosition + username.getWidth()) - 0.5f, (float)(FuckerNMSL.username.yPosition + 20) - 0.5f, new Color(33, 33, 33, 180).getRGB());
        if (!username.isFocused() && username.getText().isEmpty()) {
            Fonts.font35.func_78276_b("USERNAME", FuckerNMSL.username.xPosition + 4, FuckerNMSL.username.yPosition + 6, new Color(180, 180, 180).getRGB());
        }
        RenderUtils.drawRoundRect(FuckerNMSL.password.xPosition, FuckerNMSL.password.yPosition, FuckerNMSL.password.xPosition + password.getWidth(), FuckerNMSL.password.yPosition + 20, password.isFocused() ? new Color(71, 71, 71).getRGB() : new Color(149, 149, 149).getRGB());
        RenderUtils.drawRoundRect((float)FuckerNMSL.password.xPosition + 0.5f, (float)FuckerNMSL.password.yPosition + 0.5f, (float)(FuckerNMSL.password.xPosition + password.getWidth()) - 0.5f, (float)(FuckerNMSL.password.yPosition + 20) - 0.5f, new Color(68, 68, 68, 180).getRGB());
        if (!password.isFocused() && password.getText().isEmpty()) {
            Fonts.font35.func_78276_b("PASSWORD", FuckerNMSL.password.xPosition + 4, FuckerNMSL.password.yPosition + 6, new Color(180, 180, 180).getRGB());
        } else {
            String xing = "";
            for (char c : password.getText().toCharArray()) {
                xing = xing + "*";
            }
            Fonts.font35.func_78276_b(xing, FuckerNMSL.password.xPosition + 4, FuckerNMSL.password.yPosition + 6, new Color(180, 180, 180).getRGB());
        }
        username.drawTextBox();
        if (FuckerNMSL.isHovered(FuckerNMSL.password.xPosition, FuckerNMSL.password.yPosition + 30, FuckerNMSL.password.xPosition + password.getWidth(), FuckerNMSL.password.yPosition + 50, mouseX, mouseY)) {
            if (Mouse.isButtonDown((int)0)) {
                this.verify();
            }
            RenderUtils.drawRoundRect(FuckerNMSL.password.xPosition, FuckerNMSL.password.yPosition + 30, FuckerNMSL.password.xPosition + password.getWidth(), FuckerNMSL.password.yPosition + 50, new Color(107, 141, 205).getRGB());
            Fonts.font35.drawCenteredString("LOGIN", FuckerNMSL.password.xPosition + password.getWidth() / 2, FuckerNMSL.password.yPosition + 38, ColorUti.rainbow(36000000000L, 255, 0.5f).getRGB());
        } else {
            RenderUtils.drawRoundRect(FuckerNMSL.password.xPosition, FuckerNMSL.password.yPosition + 30, FuckerNMSL.password.xPosition + password.getWidth(), FuckerNMSL.password.yPosition + 50, new Color(77, 111, 175).getRGB());
            Fonts.font35.drawCenteredString("LOGIN", FuckerNMSL.password.xPosition + password.getWidth() / 2, FuckerNMSL.password.yPosition + 38, ColorUti.rainbow(36000000000L, 255, 0.5f).getRGB());
        }
        if (FuckerNMSL.isHovered(FuckerNMSL.password.xPosition + password.getWidth() - Fonts.font14.func_78256_a("Copy hwid"), FuckerNMSL.password.yPosition + 60, FuckerNMSL.password.xPosition + password.getWidth(), FuckerNMSL.password.yPosition + 70, mouseX, mouseY)) {
            if (Mouse.isButtonDown((int)0)) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                FuckerNMSL.func_146275_d((String)this.hwid);
            }
            Fonts.font35.func_78276_b("Copy hwid", FuckerNMSL.password.xPosition + password.getWidth() - Fonts.font14.func_78256_a("Copy hwid"), FuckerNMSL.password.yPosition + 60, ColorUti.rainbow(36000000000L, 255, 0.5f).getRGB());
        } else {
            Fonts.font35.func_78276_b("Copy hwid", FuckerNMSL.password.xPosition + password.getWidth() - Fonts.font14.func_78256_a("Copy hwid"), FuckerNMSL.password.yPosition + 60, new Color(68, 68, 68, 180).getRGB());
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        switch (keyCode) {
            case 15: {
                if (!username.isFocused() || keyCode != 15) break;
                password.setFocused(true);
                username.setFocused(false);
                return;
            }
            case 28: {
                this.verify();
                break;
            }
            default: {
                if (username.isFocused()) {
                    username.textboxKeyTyped(typedChar, keyCode);
                }
                if (!password.isFocused()) break;
                password.textboxKeyTyped(typedChar, keyCode);
            }
        }
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        username.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    private void verify() {
        try {
            LOVEU *= 10;
            this.HWID = this.getHWID();
            if (!(username.getText().isEmpty() || password.getText().isEmpty() || this.HWID.isEmpty())) {
                LOVEU *= 10;
                this.UserName = username.getText();
                this.Password = password.getText();
                String Verify = "[" + this.UserName + "]" + this.HWID + ":" + this.Password;
                if (this.get("https://github.com/Reportsrc/dev/blob/main/betausers.txt").contains(Verify)) {
                    LOVEU *= 10;
                    isload = true;
                    Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiMainMenu());
                    Display.setTitle((String)("KyinoClient | B17.1 | BY: Report. | Username: " + this.UserName));
                } else {
                    isload = false;
                }
            }
        }
        catch (Throwable var4) {
            var4.printStackTrace();
        }
    }

    private String get(String url) throws IOException {
        String inputLine;
        HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            response.append("\n");
        }
        in.close();
        return response.toString();
    }

    private String getHWID() {
        try {
            StringBuilder s = new StringBuilder();
            String main = System.getenv("PROCESS_IDENTIFIER") + System.getenv("COMPUTERNAME");
            byte[] bytes = main.getBytes("UTF-8");
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5 = messageDigest.digest(bytes);
            int i = 0;
            for (byte b : md5) {
                s.append(Integer.toHexString(b & 0xFF | 0x300), 0, 3);
                if (i != md5.length - 1) {
                    s.append("-");
                }
                ++i;
            }
            LOVEU *= 10;
            return s.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int interpolateColor(Color color1, Color color2, float fraction) {
        int red = (int)((float)color1.getRed() + (float)(color2.getRed() - color1.getRed()) * fraction);
        int green = (int)((float)color1.getGreen() + (float)(color2.getGreen() - color1.getGreen()) * fraction);
        int blue = (int)((float)color1.getBlue() + (float)(color2.getBlue() - color1.getBlue()) * fraction);
        int alpha = (int)((float)color1.getAlpha() + (float)(color2.getAlpha() - color1.getAlpha()) * fraction);
        try {
            return new Color(red, green, blue, alpha).getRGB();
        }
        catch (Exception ex) {
            return -1;
        }
    }

    static {
        Name = "KyinoClient";
        version = "17.1";
        isload = false;
        LOVEU = 1;
        Passed = false;
        process = "[Waiting For Login]";
        Now = "KyinoClient-Login";
    }
}

