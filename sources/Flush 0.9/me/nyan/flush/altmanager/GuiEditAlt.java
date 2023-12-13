package me.nyan.flush.altmanager;

import me.nyan.flush.Flush;
import me.nyan.flush.ui.elements.Button;
import me.nyan.flush.ui.elements.TextBox;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class GuiEditAlt extends GuiScreen {
    private final AltManager altmgr = Flush.getInstance().getAltManager();

    private final GuiScreen previousScreen;
    private final AccountInfo account;
    private final ArrayList<Button> buttons = new ArrayList<>();
    private TextBox usernameField;
    private TextBox passwordField;

    public GuiEditAlt(GuiScreen previousScreen, AccountInfo account) {
        this.previousScreen = previousScreen;
        this.account = account;
    }

    @Override
    public void initGui() {
        usernameField = new TextBox(width / 2f - 100, height / 4f, 200, 22);
        passwordField = new TextBox(width / 2f - 100, height / 4f + 24, 200, 22);
        usernameField.setText(account.getUsername());
        passwordField.setText(account.isCracked() ? "" : account.getPassword());

        buttons.clear();
        buttons.add(new Button("Done", width / 2f - 100, height - height / 6f - 24, 200F, 20F));
        buttons.add(new Button("Cancel", width / 2f - 100, height - height / 6f, 200F, 20F));
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawFlush(mouseX, mouseY);
        Flush.getFont("GoogleSansDisplay", 24)
                .drawXCenteredString("Editing account " + account, width / 2f, 20f, -1, true);
        for (Button button : buttons) {
            button.drawButton(mouseX, mouseY);
        }
        usernameField.draw();
        passwordField.draw();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\r') {
            altmgr.getAlts().remove(account);
            altmgr.addAlt(new AccountInfo(usernameField.getText(), passwordField.getText(),
                    account.getType(), account.getCreationDate(), account.getDisplayName()));
            mc.displayGuiScreen(previousScreen);
        }

        usernameField.keyTyped(character, key);
        passwordField.keyTyped(character, key);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Button button : buttons) {
            if (!button.mouseClicked(mouseX, mouseY, mouseButton)) {
                continue;
            }
            Flush.playClickSound();
            switch (button.getName().toLowerCase()) {
                case "done":
                    altmgr.getAlts().remove(account);
                    altmgr.addAlt(new AccountInfo(usernameField.getText(), passwordField.getText(),
                            account.getType(), account.getCreationDate(), account.getDisplayName()));
                    mc.displayGuiScreen(previousScreen);
                    break;

                case "cancel":
                    mc.displayGuiScreen(previousScreen);
                    break;
            }
        }
        usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        passwordField.mouseClicked(mouseX, mouseY, mouseButton);
    }
}