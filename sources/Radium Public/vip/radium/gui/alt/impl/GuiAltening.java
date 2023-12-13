package vip.radium.gui.alt.impl;

import com.google.gson.JsonSyntaxException;
import com.thealtening.api.TheAlteningException;
import com.thealtening.api.response.Account;
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

import java.io.IOException;

public final class GuiAltening extends GuiAltScreen {

    private GuiTextField tokenField;

    public GuiAltening(GuiScreen parent) {
        super(parent);
    }

    @Override
    public void initGui() {
        final int buttonWidth = 200;
        final int buttonHeight = 20;
        final int margin = 2;

        final int offset = buttonHeight + margin;

        final int middleX = width / 2 - (buttonWidth / 2);
        final int middleY = height / 4 + 48;
        this.tokenField = new GuiTextField(3, Wrapper.getMinecraftFontRenderer(),
                "API Key", middleX, middleY, buttonWidth, buttonHeight - margin);

        this.textFields.add(tokenField);

        this.tokenField.setText(RadiumClient.getInstance().getAltManager().getAPIKey());
        this.buttonList.add(new GuiButton(2, middleX, middleY + offset, "Update API Key"));
        this.buttonList.add(new GuiButton(1, middleX, middleY + offset * 2, "Generate"));
        this.buttonList.add(new GuiButton(0, middleX, middleY + offset * 3, "Back"));

        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                Wrapper.getMinecraft().displayGuiScreen(parent);
                break;
            case 1:
                SessionUtils.switchService(AlteningServiceType.THEALTENING);
                try {
                    Account account = RadiumClient.getInstance().getAltManager()
                            .getAlteningAltFetcher().getAccount();

                    SessionUtils.logIn(new Alt(account.getToken(), "A"));
                } catch (TheAlteningException | JsonSyntaxException ignored) {
                    RadiumClient.getInstance().getNotificationManager().add(new Notification("TheAltening Exception", "Failed to generate account",
                            1000, NotificationType.ERROR));
                }
                break;
            case 2:
                RadiumClient.getInstance().getAltManager().saveAPIKey(tokenField.getText());
                break;
        }
    }
}
