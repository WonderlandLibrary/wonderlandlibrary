package vip.radium.gui.alt.impl;

import vip.radium.RadiumClient;
import vip.radium.alt.Alt;
import vip.radium.gui.alt.GuiAltScreen;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.utils.SessionUtils;
import vip.radium.utils.Wrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public final class GuiAltManager extends GuiAltScreen {

    private Alt selectedAlt;

    public GuiAltManager(GuiScreen parent) {
        super(parent);
    }

    @Override
    public void initGui() {
        final int buttonWidth = 100;
        final int buttonHeight = 20;
        final int margin = 2;

        final int secondRowHeight = height - (buttonHeight + margin);
        final int firstRowHeight = secondRowHeight - (buttonHeight + margin);
        final int middle = width / 2 - (buttonWidth / 2);
        this.buttonList.add(new GuiButton(0, margin, secondRowHeight,
                buttonWidth, buttonHeight, "Back"));
        this.buttonList.add(new GuiButton(1, middle, secondRowHeight,
                buttonWidth, buttonHeight, "Direct Login"));
        this.buttonList.add(new GuiButton(2, middle + buttonWidth + margin, secondRowHeight,
                buttonWidth, buttonHeight, "Login"));
        this.buttonList.add(new GuiButton(3, middle - buttonWidth - margin, secondRowHeight,
                buttonWidth, buttonHeight, "Add"));
        this.buttonList.add(new GuiButton(4, margin, margin, buttonWidth,
                buttonHeight, "TheAltening"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                Wrapper.getMinecraft().displayGuiScreen(parent);
                break;
            case 1:
                Wrapper.getMinecraft().displayGuiScreen(new GuiDirectLogin(this));
                break;
            case 2:
                if (selectedAlt != null)
                    SessionUtils.logIn(selectedAlt);
                else
                    RadiumClient.getInstance().getNotificationManager().add(new Notification("Please select an account", 1000, NotificationType.ERROR));
                break;
            case 3:
                Wrapper.getMinecraft().displayGuiScreen(new GuiAddAlt(this));
                break;
            case 4:
                Wrapper.getMinecraft().displayGuiScreen(new GuiAltening(this));
                break;
        }
    }
}
