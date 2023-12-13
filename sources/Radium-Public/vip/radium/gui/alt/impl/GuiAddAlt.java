package vip.radium.gui.alt.impl;

import com.thealtening.auth.service.AlteningServiceType;
import vip.radium.RadiumClient;
import vip.radium.alt.Alt;
import vip.radium.gui.alt.GuiAltScreen;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.utils.SessionUtils;
import vip.radium.utils.Wrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public final class GuiAddAlt extends GuiAltScreen {

    private GuiTextField emailField;
    private GuiTextField passwordField;

    public GuiAddAlt(GuiScreen parent) {
        super(parent);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        final int buttonWidth = 200;
        final int buttonHeight = 20;
        final int margin = 2;

        final int offset = buttonHeight + margin;

        final int middleX = width / 2 - (buttonWidth / 2);
        final int middleY = height / 4 + 48;
        this.emailField = new GuiTextField(3, Wrapper.getMinecraftFontRenderer(),
                "Email", middleX, middleY, buttonWidth, buttonHeight - margin);
        this.passwordField = new GuiTextField(4, Wrapper.getMinecraftFontRenderer(),
                "Password", middleX, middleY + offset, buttonWidth, buttonHeight - margin);

        this.textFields.add(emailField);
        this.textFields.add(passwordField);

        this.buttonList.add(new GuiButton(1, middleX, middleY + offset * 2, "Add"));
        this.buttonList.add(new GuiButton(2, middleX, middleY + offset * 3, "Login and Add"));
        this.buttonList.add(new GuiButton(0, middleX, middleY + offset * 4, "Back"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        String email = emailField.getText();
        String pw = passwordField.getText();

        Alt alt = new Alt(email, pw);

        switch (button.id) {
            case 0:
                Wrapper.getMinecraft().displayGuiScreen(parent);
                break;
            case 1:
                if (email.length() >= 5 && pw.length() > 0)
                    add(alt);
                else
                    loginError();
                break;
            case 2:
                if (email.length() >= 5 && pw.length() > 0) {
                    add(alt);
                    SessionUtils.switchService(AlteningServiceType.MOJANG);
                    SessionUtils.logIn(alt);
                } else {
                    loginError();
                }
                break;
        }
    }

    private void add(Alt alt) {
        RadiumClient.getInstance().getAltManager().addAlt(alt);
    }

    private void loginError() {
        RadiumClient.getInstance().getNotificationManager().add(new Notification(
                "Login Error",
                "Enter email and password",
                1000, NotificationType.ERROR));
    }
}
