package client.ui;

import client.Client;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;

public class GuiAccountManager extends GuiScreen {

    private final GuiScreen parent;
    private GuiTextField username;
    private GuiTextField password;

    public GuiAccountManager(final GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        username.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        drawDefaultBackground();
        this.drawCenteredString(this.mc.fontRendererObj, "Alt Login", width / 2, 20, -1);
     //   this.drawCenteredString(this.mc.fontRendererObj, "Current username is: " + auth.getSelectedProfile().getName(), width / 2, 29, -1);
        username.drawTextBox();
        password.drawTextBox();


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (username.isFocused()) username.textboxKeyTyped(typedChar, keyCode);
        if (password.isFocused()) password.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == 1) {
            mc.displayGuiScreen(parent);
            if (parent == null) mc.setIngameFocus();
        }
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        username = new GuiTextField(0, mc.fontRendererObj, width / 2 - 100, height / 2 - 22, 98, 20);
        username.setFocused(true);
        username.setMaxStringLength(64);
        password = new GuiTextField(0, mc.fontRendererObj, width / 2 - 100, height / 2 + 2, 98, 20);
        password.setMaxStringLength(64);
        buttonList.add(new GuiButton(0, width / 2 + 2, height / 2 - 22, 98, 20, "Login"));
        buttonList.add(new GuiButton(1, width / 2 + 2, height / 2 + 2, 98, 20, "Exit"));
        buttonList.add(new GuiButton(2, width / 2 + 2, height / 2 + 26, 98, 20, "MushGen"));
    }

    @Override
    public void updateScreen() {
        username.updateCursorCounter();
        password.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                if (username.getText().equals("")) {
                    new Thread(() -> {
                        try {
                            final MicrosoftAuthResult result = new MicrosoftAuthenticator().loginWithWebview();
                            mc.setSession(new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "msa"));
                        } catch (MicrosoftAuthenticationException e) {
                            if (Client.DEVELOPMENT_SWITCH) e.printStackTrace();
                        }
                    }).start();
                } else if (username.getText().contains(":")) {
                    new Thread(() -> {
                        try {
                            final MicrosoftAuthResult result = new MicrosoftAuthenticator().loginWithCredentials(username.getText().split(":")[0], username.getText().split(":")[1]);
                            mc.setSession(new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "msa"));
                        } catch (MicrosoftAuthenticationException e) {
                            if (Client.DEVELOPMENT_SWITCH) e.printStackTrace();
                        }
                    }).start();
                } else if (!password.getText().equals("")) {
                    new Thread(() -> {
                        try {
                            final MicrosoftAuthResult result = new MicrosoftAuthenticator().loginWithCredentials(username.getText(), password.getText());
                            mc.setSession(new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "msa"));
                        } catch (MicrosoftAuthenticationException e) {
                            if (Client.DEVELOPMENT_SWITCH) e.printStackTrace();
                        }
                    }).start();
                } else mc.setSession(new Session(username.getText(), username.getText(), "0", "legacy"));
                break;
            }
            case 1: {
                mc.displayGuiScreen(parent);
                break;
            }
            case 2: {
                final BufferedReader br = new BufferedReader(new InputStreamReader(new URL("https://rufacisten.space/Te2sZLgGMhjs7JuG8cxWG2WxLPf4b4hnY4Kfk3ed").openStream()));
                final StringBuilder sb = new StringBuilder();
                String s;
                while ((s = br.readLine()) != null) sb.append(s);
                br.close();
                username.setText(sb.toString());
                break;
            }
        }
    }
}
