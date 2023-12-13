// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.auth;

import org.lwjgl.input.Keyboard;
import vip.Resolute.ui.MainMenu;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import vip.Resolute.ui.notification.Notification;
import vip.Resolute.ui.notification.NotificationType;
import vip.Resolute.Resolute;
import net.minecraft.client.gui.GuiButton;
import java.io.IOException;
import vip.Resolute.util.font.FontUtil;
import vip.Resolute.util.render.TranslationUtils;
import vip.Resolute.util.font.MinecraftFontRenderer;
import vip.Resolute.ui.shader.GLSLSandboxShader;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class LoginScreen extends GuiScreen
{
    private GuiScreen previousScreen;
    private Authentication thread;
    private GuiTextField username;
    private PasswordField password;
    private GLSLSandboxShader backgroundShader;
    private long initTime;
    MinecraftFontRenderer fontRenderer;
    public static LoginScreen instance;
    public TranslationUtils translate;
    public static String progression;
    
    public LoginScreen() {
        this.initTime = System.currentTimeMillis();
        this.fontRenderer = FontUtil.clientfont;
        this.previousScreen = this.previousScreen;
        try {
            this.backgroundShader = new GLSLSandboxShader("/mainMenuShader.frag");
        }
        catch (IOException e) {
            throw new IllegalStateException("Failed to load backgound shader", e);
        }
    }
    
    public static LoginScreen getInstance() {
        return LoginScreen.instance;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        if (button.id == 0) {
            LoginScreen.progression = "Initializing...";
            (this.thread = new Authentication(this.username.getText(), this.password.getText())).start();
        }
        if (button.id == 69) {
            GuiScreen.setClipboardString(Authentication.getEncryptedAuthString(this.username.getText(), this.password.getText(), Authentication.key));
            Resolute.getNotificationManager().add(new Notification("Success", "Copied key to clipboard", 5000L, NotificationType.SUCCESS));
        }
    }
    
    @Override
    public void drawScreen(final int x2, final int y2, final float z2) {
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        final int i = 274;
        final int j = LoginScreen.width / 2 - i / 2;
        final int k = 30;
        this.backgroundShader.useShader(LoginScreen.width * 2, LoginScreen.height * 2, (float)x2, (float)y2, (System.currentTimeMillis() - this.initTime) / 1000.0f);
        GL11.glBegin(7);
        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glVertex2f(-1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, -1.0f);
        GL11.glEnd();
        GL20.glUseProgram(0);
        this.translate.interpolate((float)LoginScreen.width, (float)LoginScreen.height, 2.0);
        final double xmod = LoginScreen.height - this.translate.getY();
        GL11.glPushMatrix();
        GlStateManager.translate(0.0, 0.5 * xmod, 0.0);
        Gui.drawRect(LoginScreen.width / 2 - 100, LoginScreen.height / 2 + 55, LoginScreen.width / 2 + 100, LoginScreen.height / 2 - 55, -1895825408);
        FontUtil.tahoma.drawCenteredString(LoginScreen.progression, (float)(LoginScreen.width / 2), (float)(LoginScreen.height / 2 - 45), -1);
        this.username.drawTextBox();
        this.password.drawTextBox();
        super.drawScreen(x2, y2, z2);
        GL11.glPopMatrix();
        if (Resolute.authorized) {
            try {
                this.mc.displayGuiScreen(new MainMenu());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void initGui() {
        final int var3 = LoginScreen.height / 4 + 24;
        this.translate = new TranslationUtils(0.0f, 0.0f);
        this.buttonList.add(new GuiButton(0, LoginScreen.width / 2 - 73, LoginScreen.height / 2 + 25, 150, 20, "Login"));
        this.username = new GuiTextField(var3, this.mc.fontRendererObj, LoginScreen.width / 2 - 73, LoginScreen.height / 2 - 25, 150, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, LoginScreen.width / 2 - 73, LoginScreen.height / 2, 150, 20);
        this.buttonList.add(new GuiButton(69, 5, 5, 90, 20, "Copy Key"));
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    protected void keyTyped(final char character, final int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            }
            else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }
    
    @Override
    protected void mouseClicked(final int x2, final int y2, final int button) {
        try {
            super.mouseClicked(x2, y2, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x2, y2, button);
        this.password.mouseClicked(x2, y2, button);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }
    
    static {
        LoginScreen.instance = new LoginScreen();
        LoginScreen.progression = "Idle...";
    }
}
