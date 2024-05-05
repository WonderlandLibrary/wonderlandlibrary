package club.dortware.client.gui.alt;

import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

public final class GUIAltManager
        extends GuiScreen {
    private GuiTextField password;
    private final GuiScreen previousScreen;
    private GuiTextField username;
    private LoginThread thread;

    public GUIAltManager(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        try {
            switch (button.id) {
                case 1: {
                    mc.displayGuiScreen(previousScreen);
                    break;
                }
                case 0: {
                    thread = new LoginThread(username.getText(), password.getText());
                    thread.start();
                    break;
                }
                case 2: {
                    String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (!data.contains(":")) break;
                    String[] credentials = data.split(":");
                    username.setText(credentials[0]);
                    password.setText(credentials[1]);
                }
                default: {
                    break;
                }
            }
        } catch (Throwable var11) {
            //REMOVE ME LATER: throw new RuntimeException();
        }
    }

    @Override
    public void drawScreen(int x, int y2, float z) {
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        Gui.drawRect(0.0F, 0.0F, res.getScaledWidth(), res.getScaledHeight(), Color.black.getRGB());
        username.drawTextBox();
        password.drawTextBox();
        drawCenteredString(mc.fontRendererObj, "Alt Login", width / 2, 20, -1);
        drawCenteredString(mc.fontRendererObj, thread == null ? EnumChatFormatting.GRAY + "Idle..." : thread.getStatus(), width / 2, 29, -1);
        if (username.getText().isEmpty()) {
            drawString(mc.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
        }
        if (password.getText().isEmpty()) {
            drawString(mc.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x, y2, z);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
        buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 24, I18n.format("gui.cancel")));
        buttonList.add(new GuiButton(2, width / 2 - 100, var3 + 72 + 12 + 48, "Import user:pass"));
        username = new GuiTextField(var3, mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        password = new GuiTextField(var3, mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!username.isFocused() && !password.isFocused()) {
                username.setFocused(true);
            } else {
                username.setFocused(password.isFocused());
                password.setFocused(!username.isFocused());
            }
        }
        if (character == '\r') {
            actionPerformed((GuiButton) buttonList.get(0));
        }
        username.textboxKeyTyped(character, key);
        password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x, int y2, int button) {
        try {
            super.mouseClicked(x, y2, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
        username.mouseClicked(x, y2, button);
        password.mouseClicked(x, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        username.updateCursorCounter();
        password.updateCursorCounter();
    }
}
