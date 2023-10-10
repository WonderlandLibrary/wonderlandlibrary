// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.ui.altlogin;

import net.minecraft.util.Session;
import net.minecraft.client.Minecraft;
import org.json.JSONObject;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.awt.Color;
import java.io.IOException;
import java.io.File;
import net.minecraft.client.gui.GuiButton;
import net.augustus.ui.widgets.CustomButton;
import net.augustus.ui.widgets.CustomLoginButton;
import net.minecraft.client.gui.ScaledResolution;
import net.augustus.Augustus;
import net.augustus.ui.widgets.PasswordTextField;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class AltLogin extends GuiScreen
{
    private String username;
    private String password;
    private GuiTextField emailTextField;
    private PasswordTextField passwordTextField;
    private String loginStatus;
    private LoginThread loginThread;
    private int clickCounter;
    private GuiScreen parent;
    
    public AltLogin(final GuiScreen parent) {
        this.loginStatus = "Waiting...";
        this.parent = parent;
    }
    
    public GuiScreen start(final GuiScreen parent) {
        this.parent = parent;
        return this;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.clickCounter = Augustus.getInstance().getLastAlts().size() - 1;
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.emailTextField = new GuiTextField(5, this.fontRendererObj, sr.getScaledWidth() / 2 - 100, 55, 200, 20);
        this.passwordTextField = new PasswordTextField(6, this.fontRendererObj, sr.getScaledWidth() / 2 - 100, 85, 200, 20);
        this.emailTextField.setMaxStringLength(1337);
        this.passwordTextField.setMaxStringLength(1337);
        this.buttonList.add(new CustomLoginButton(1, sr.getScaledWidth() / 2 - 100, 135, 200, 20, "Login", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(2, sr.getScaledWidth() / 2 - 100, 165, 200, 20, "Last Alt", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomLoginButton(3, sr.getScaledWidth() / 2 - 100, 195, 200, 20, "Clipboard Login", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomLoginButton(6, sr.getScaledWidth() / 2 - 100, 225, 200, 20, "Generate", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(5, sr.getScaledWidth() - 100, sr.getScaledHeight() - 20, 100, 20, "Reconnect", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(1337, 0, sr.getScaledHeight() - 20, 100, 20, "Token", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(4, sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() - sr.getScaledHeight() / 10, 200, 20, "Back", Augustus.getInstance().getClientColor()));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            this.buttonLogin();
        }
        if (button.id == 2) {
            this.lastAlt();
        }
        if (button.id == 1337) {
            loginToken(GuiScreen.getClipboardString());
        }
        if (button.id == 3) {
            this.clipBoardLogin();
        }
        if (button.id == 4) {
            this.mc.displayGuiScreen(this.parent);
        }
        if (button.id == 6) {
            if (GuiScreen.getClipboardString().contains("api-")) {
                Augustus.getInstance().getConverter().apiSaver(GuiScreen.getClipboardString());
            }
            if (Augustus.getInstance().getConverter().apiLoader() != null) {
                this.username = Augustus.getInstance().getConverter().apiLoader();
                this.password = "Password1";
                this.login();
            }
        }
        if (button.id == 5) {
            try {
                final File dir = new File(this.mc.mcDataDir, "augustus");
                final ProcessBuilder builder = new ProcessBuilder(new String[] { "wscript", "ip_changer_fritzbox.vbs" });
                builder.directory(dir);
                builder.start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawBackground(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.emailTextField.drawTextBox();
        this.passwordTextField.drawTextBox();
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final int scaledWidth = sr.getScaledWidth();
        this.fontRendererObj.drawStringWithShadow("Alt Login", scaledWidth / 2.0f - this.fontRendererObj.getStringWidth("Alt Login") / 2.0f, 10.0f, Color.lightGray.getRGB());
        Color loginStatusColor = Color.green;
        if (this.loginStatus.equals("Waiting...")) {
            loginStatusColor = Color.gray;
        }
        if (this.loginThread != null) {
            this.loginStatus = this.loginThread.getStatus();
            loginStatusColor = this.loginThread.getColor();
        }
        this.fontRendererObj.drawStringWithShadow(this.loginStatus, scaledWidth / 2.0f - this.fontRendererObj.getStringWidth(this.loginStatus) / 2.0f, 22.0f, loginStatusColor.getRGB());
        if (this.emailTextField.isFocused()) {
            this.passwordTextField.setFocused(false);
        }
        if (this.passwordTextField.isFocused()) {
            this.emailTextField.setFocused(false);
        }
        if (this.emailTextField.getText().isEmpty() && !this.emailTextField.isFocused()) {
            this.fontRendererObj.drawStringWithShadow("E-Mail / E-Mail:Password / Alt-Token", scaledWidth / 2.0f - 96.0f, 61.0f, Color.gray.getRGB());
        }
        if (this.passwordTextField.getText().isEmpty() && !this.passwordTextField.isFocused()) {
            this.fontRendererObj.drawStringWithShadow("Password", scaledWidth / 2.0f - 96.0f, 91.0f, Color.gray.getRGB());
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 28) {
            this.buttonLogin();
        }
        this.emailTextField.textboxKeyTyped(typedChar, keyCode);
        this.passwordTextField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.emailTextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.passwordTextField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void updateScreen() {
        this.emailTextField.updateCursorCounter();
        this.passwordTextField.updateCursorCounter();
        super.updateScreen();
    }
    
    private void lastAlt() {
        final List<String> lastAlts = Augustus.getInstance().getLastAlts();
        if (lastAlts.size() <= 0) {
            return;
        }
        if (lastAlts.size() <= this.clickCounter) {
            this.clickCounter = lastAlts.size() - 1;
        }
        final String[] s = lastAlts.get(Math.max(0, this.clickCounter)).split(":");
        this.emailTextField.setText(s[0]);
        this.passwordTextField.setText((s.length > 1) ? s[1] : "");
        --this.clickCounter;
        if (this.clickCounter < 0) {
            this.clickCounter = lastAlts.size() - 1;
        }
    }
    
    private void clipBoardLogin() {
        final String[] s = GuiScreen.getClipboardString().split(":");
        this.username = s[0];
        this.password = ((s.length > 1) ? s[1] : "");
        this.login();
    }
    
    private void buttonLogin() {
        if (this.emailTextField.getText().isEmpty() && this.passwordTextField.getText().isEmpty()) {
            final StringBuilder randomName = new StringBuilder();
            final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789_";
            for (int random = ThreadLocalRandom.current().nextInt(8, 16), i = 0; i < random; ++i) {
                randomName.append(alphabet.charAt(ThreadLocalRandom.current().nextInt(1, alphabet.length())));
            }
            this.username = String.valueOf(randomName);
            this.password = this.passwordTextField.getText();
        }
        else {
            this.username = this.emailTextField.getText();
            this.password = this.passwordTextField.getText();
        }
        this.login();
    }
    
    private void login() {
        (this.loginThread = new LoginThread(this.username, this.password)).start();
    }
    
    public static boolean loginToken(final String token) {
        URL url = null;
        try {
            url = new URL("https://api.minecraftservices.com/minecraft/profile/");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)url.openConnection();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        try {
            conn.setRequestMethod("GET");
        }
        catch (ProtocolException e3) {
            e3.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        catch (IOException e4) {
            e4.printStackTrace();
            return false;
        }
        final StringBuilder response = new StringBuilder();
        while (true) {
            String output;
            try {
                if ((output = in.readLine()) == null) {
                    break;
                }
            }
            catch (IOException e5) {
                e5.printStackTrace();
                return false;
            }
            response.append(output);
        }
        final String jsonString = response.toString();
        final JSONObject obj = new JSONObject(jsonString);
        final String username = obj.getString("name");
        final String uuid = obj.getString("id");
        Minecraft.getMinecraft().session = new Session(username, uuid, token, "null");
        try {
            in.close();
        }
        catch (IOException e6) {
            e6.printStackTrace();
        }
        return true;
    }
}
