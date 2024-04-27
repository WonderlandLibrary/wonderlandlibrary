/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 */
package Verify1;

import Verify1.GuiPasswordField;
import Verify1.GuiUserField;
import Verify1.font.FontLoaders;
import Verify1.utils.HWIDUtils;
import Verify1.utils.SystemUtils;
import Verify1.utils.WebUtils;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.swing.JOptionPane;
import me.utils.render.UiUtils2;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.client.GuiMainMenu;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GuiLogin
extends WrappedGuiScreen {
    static final Base64.Encoder encoder = Base64.getEncoder();
    static final Base64.Decoder decoder = Base64.getDecoder();
    int alpha = 0;
    private boolean i = false;
    private boolean j = false;
    public String UserName = null;
    public String Password = null;
    public static GuiPasswordField password;
    public static GuiUserField username;
    public static boolean render;
    private float currentX;
    private float currentY;
    public boolean drag = false;
    public static boolean Passed;
    public static boolean UnDisCheck;
    String hwid;
    public static boolean isload;
    public static String HWID;
    public static int LOVEU;
    public static String process;

    public GuiLogin() {
        try {
            this.hwid = HWIDUtils.getHWID();
        }
        catch (IOException | NoSuchAlgorithmException ioexception) {
            ioexception.printStackTrace();
        }
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        Display.setTitle((String)"LRQ | \u770b\u4f60\u5988\u554a\u6ca1\u767b\u5f55\u5c31\u73a9\u554a");
        if (this.i && this.alpha < 255) {
            this.alpha += 5;
        }
        int h = classProvider.createScaledResolution(mc).getScaledHeight();
        int w = classProvider.createScaledResolution(mc).getScaledWidth();
        RenderUtils.drawGradientSideways(0.0, 0.0, w, h, new Color(60, 96, 203).getRGB(), new Color(51, 201, 217).getRGB());
        this.representedScreen.drawBackground(0);
        IScaledResolution sr = classProvider.createScaledResolution(mc);
        float xDiff = ((float)(i - h / 2) - this.currentX) / (float)sr.getScaleFactor();
        float yDiff = ((float)(j - w / 2) - this.currentY) / (float)sr.getScaleFactor();
        this.currentX += xDiff * 0.3f;
        this.currentY += yDiff * 0.3f;
        this.drag = Mouse.isButtonDown((int)0);
        RenderUtils.drawGradientSideways(this.representedScreen.getWidth() / 2 + 30, this.representedScreen.getHeight() / 2 + 47, this.representedScreen.getWidth() / 2 + 155, this.representedScreen.getHeight() / 2 + 70, new Color(94, 212, 255).getRGB(), new Color(253, 222, 90).getRGB());
        if (Mouse.isButtonDown((int)0) && i > this.representedScreen.getWidth() / 2 + 120 && i < this.representedScreen.getWidth() / 2 + 155 && j > this.representedScreen.getHeight() / 2 + 75 && j < this.representedScreen.getHeight() / 2 + 82) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            GuiScreen.func_146275_d((String)this.hwid);
        }
        RenderUtils.drawRect((float)(this.representedScreen.getWidth() / 2 - 180), (float)(this.representedScreen.getHeight() / 2 - 115), (float)(this.representedScreen.getWidth() / 2 + 180), (float)(this.representedScreen.getHeight() / 2 + 115), new Color(89, 89, 89, 171).getRGB());
        RenderUtils.drawShadow(this.representedScreen.getWidth() / 2 - 180, this.representedScreen.getHeight() / 2 - 115, 360, 230);
        UiUtils2.drawImage(new ResourceLocation("liquidbounce/login.png"), this.representedScreen.getWidth() / 2 - 180, this.representedScreen.getHeight() / 2 - 115, 360, 230);
        RenderUtils.drawGradientSideways(this.representedScreen.getWidth() / 2 + 30, this.representedScreen.getHeight() / 2 + 47, this.representedScreen.getWidth() / 2 + 155, this.representedScreen.getHeight() / 2 + 70, new Color(0, 111, 255).getRGB(), new Color(255, 125, 198).getRGB());
        RenderUtils.drawGradientSideways(this.representedScreen.getWidth() / 2 + 30, this.representedScreen.getHeight() / 2 - 9, this.representedScreen.getWidth() / 2 + 155, this.representedScreen.getHeight() / 2 - 8, new Color(0, 111, 255, 255).getRGB(), new Color(255, 125, 198).getRGB());
        RenderUtils.drawGradientSideways(this.representedScreen.getWidth() / 2 + 30, this.representedScreen.getHeight() / 2 + 30, this.representedScreen.getWidth() / 2 + 155, this.representedScreen.getHeight() / 2 + 31, new Color(0, 111, 255, 255).getRGB(), new Color(255, 125, 198).getRGB());
        if (!username.getText().isEmpty() && Mouse.isButtonDown((int)0) && this.drag && i > this.representedScreen.getWidth() / 2 + 30 && i < this.representedScreen.getWidth() / 2 + 155 && j > this.representedScreen.getHeight() / 2 + 47 && j < this.representedScreen.getHeight() / 2 + 70) {
            GuiScreen.func_146275_d((String)this.hwid);
            this.verify();
            Fonts.neverlose35.drawString("Login", (float)(this.representedScreen.getWidth() / 2 + 10), (float)(this.representedScreen.getHeight() / 2 - 75), -1);
        }
        FontLoaders.xyz20.drawString("Login", this.representedScreen.getWidth() / 2 + 80, this.representedScreen.getHeight() / 2 + 55, new Color(255, 255, 255).getRGB());
        FontLoaders.xyz16.drawString("Click Login to get HWID", this.representedScreen.getWidth() / 2 + 60, this.representedScreen.getHeight() / 2 + 53 + 25, new Color(255, 255, 255).getRGB());
        FontLoaders.xyz16.drawString("Log in to your account so that we can", this.representedScreen.getWidth() / 2 + 10, this.representedScreen.getHeight() / 2 - 61, new Color(255, 255, 255).getRGB());
        FontLoaders.xyz16.drawString("check your identity.", this.representedScreen.getWidth() / 2 + 10, this.representedScreen.getHeight() / 2 - 53, new Color(255, 255, 255).getRGB());
        FontLoaders.xyz32.drawString("Login to client", this.representedScreen.getWidth() / 2 - 165, this.representedScreen.getHeight() / 2 - 105, new Color(255, 255, 255).getRGB());
        FontLoaders.xyz28.drawString("Welcome", this.representedScreen.getWidth() / 2 - 150, this.representedScreen.getHeight() / 2 + 40 + 20, new Color(255, 255, 255, 255).getRGB());
        FontLoaders.xyz16.drawString("LRQ", this.representedScreen.getWidth() / 2 - 150, this.representedScreen.getHeight() / 2 + 53 + 23, new Color(255, 255, 255).getRGB());
        username.drawTextBox2();
        FontLoaders.xyz16.drawString("ByJiuXuan", this.representedScreen.getWidth() / 2 - 150, this.representedScreen.getHeight() / 2 + 53 + 31, new Color(255, 255, 255).getRGB());
        password.drawTextBox2();
        if (password.getText().isEmpty() && !password.isFocused()) {
            FontLoaders.xyz16.drawString("Your Password", this.representedScreen.getWidth() / 2 + 35, this.representedScreen.getHeight() / 2 + 20, new Color(255, 255, 255).getRGB());
        }
        if (username.getText().isEmpty() && !username.isFocused()) {
            FontLoaders.xyz16.drawString("Your Username", this.representedScreen.getWidth() / 2 + 35, this.representedScreen.getHeight() / 2 - 19, new Color(255, 255, 255).getRGB());
        }
        super.drawScreen(i, j, f);
    }

    @Override
    public void initGui() {
        FontRenderer fontrenderer = GuiLogin.mc2.field_71466_p;
        super.initGui();
        render = true;
        username = new GuiUserField(fontrenderer, this.representedScreen.getWidth() / 2 + 30, this.representedScreen.getHeight() / 2 - 30, 125, 20);
        password = new GuiPasswordField(fontrenderer, this.representedScreen.getWidth() / 2 + 30, this.representedScreen.getHeight() / 2 + 10, 125, 20);
    }

    @Override
    public void keyTyped(char c0, int i) {
        if (c0 == '\t') {
            if (!username.isFocused()) {
                username.setFocused(true);
            } else {
                username.setFocused(username.isFocused());
                password.setFocused(!username.isFocused());
            }
        }
        if (c0 == '\u001b') {
            // empty if block
        }
        username.textboxKeyTyped(c0, i);
        password.textboxKeyTyped(c0, i);
    }

    private void verify() {
        try {
            LOVEU *= 10;
            HWID = HWIDUtils.getHWID();
            GuiScreen.func_146275_d((String)this.hwid);
            if (!username.getText().isEmpty() && !HWID.isEmpty()) {
                LOVEU *= 10;
                this.UserName = username.getText();
                this.Password = password.getText();
                String throwable = "[" + this.UserName + "]" + HWIDUtils.getHWID() + ":" + this.Password;
                if (WebUtils.get("https://gitee.com/WuuYuas/test/blob/master/LRQhwid").contains(throwable)) {
                    LOVEU *= 10;
                    isload = true;
                    mc2.func_147108_a((GuiScreen)new GuiMainMenu().representedScreen);
                    Display.setTitle((String)("LRQ 1.0 | User: " + this.UserName));
                } else {
                    isload = false;
                    GuiScreen.func_146275_d((String)this.hwid);
                    SystemUtils.displayTray("ERROR", "If you have never registered or forgotten your password, please contact the administrator", TrayIcon.MessageType.ERROR);
                }
            }
        }
        catch (Throwable throwable) {
            try {
                LOVEU *= 10;
                HWID = HWIDUtils.getHWID();
                if (!username.getText().isEmpty() && !HWID.isEmpty()) {
                    LOVEU *= 10;
                    this.UserName = username.getText();
                    this.Password = password.getText();
                    GuiScreen.func_146275_d((String)this.hwid);
                    String throwable1 = "[" + this.UserName + "]" + HWIDUtils.getHWID() + ":" + this.Password;
                    if (WebUtils.get("https://gitee.com/WuuYuas/test/blob/master/LRQhwid").contains(throwable1)) {
                        LOVEU *= 10;
                        isload = true;
                        mc2.func_147108_a((GuiScreen)new GuiMainMenu().representedScreen);
                    } else {
                        isload = false;
                        SystemUtils.displayTray("ERROR", "If you have never registered or forgotten your password, please contact the administrator", TrayIcon.MessageType.ERROR);
                    }
                }
            }
            catch (Throwable throwable1) {
                throwable1.printStackTrace();
                JOptionPane.showMessageDialog(null, "ERROR");
                GuiScreen.func_146275_d((String)this.hwid);
                mc2.func_147108_a((GuiScreen)new GuiMainMenu().representedScreen);
            }
        }
    }

    @Override
    public void mouseClicked(int i, int j, int k) {
        try {
            super.mouseClicked(i, j, k);
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
        username.mouseClicked(i, j, k);
        password.mouseClicked(i, j, k);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    @Override
    public void updateScreen() {
        username.updateCursorCounter();
        password.updateCursorCounter();
    }

    public static String decode(String encodedText) {
        String text = null;
        try {
            text = new String(decoder.decode(encodedText), "UTF-8");
        }
        catch (UnsupportedEncodingException unsupportedencodingexception) {
            unsupportedencodingexception.printStackTrace();
        }
        return text;
    }

    static {
        render = false;
        Passed = false;
        UnDisCheck = false;
        isload = false;
        HWID = null;
        LOVEU = 1;
        process = "[Waiting For Login...]";
    }
}

