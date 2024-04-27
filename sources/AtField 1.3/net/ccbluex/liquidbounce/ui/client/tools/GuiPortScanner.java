/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.tools;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.TabUtils;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import org.lwjgl.input.Keyboard;

public class GuiPortScanner
extends WrappedGuiScreen {
    private IGuiButton buttonToggle;
    private final List ports = new ArrayList();
    private int minPort;
    private String status = "\u00a77Waiting...";
    private final IGuiScreen prevGui;
    private IGuiTextField hostField;
    private String host;
    private boolean running;
    private IGuiTextField maxPortField;
    private IGuiTextField threadsField;
    private int currentPort;
    private IGuiTextField minPortField;
    private int maxPort;
    private int checkedPort;

    @Override
    public void updateScreen() {
        this.hostField.updateCursorCounter();
        this.minPortField.updateCursorCounter();
        this.maxPortField.updateCursorCounter();
        this.threadsField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        this.running = false;
        super.onGuiClosed();
    }

    @Override
    public void keyTyped(char c, int n) throws IOException {
        if (1 == n) {
            mc.displayGuiScreen(this.prevGui);
            return;
        }
        if (15 == n) {
            TabUtils.tab(this.hostField, this.minPortField, this.maxPortField);
        }
        if (this.running) {
            return;
        }
        if (this.hostField.isFocused()) {
            this.hostField.textboxKeyTyped(c, n);
        }
        if (this.minPortField.isFocused() && !Character.isLetter(c)) {
            this.minPortField.textboxKeyTyped(c, n);
        }
        if (this.maxPortField.isFocused() && !Character.isLetter(c)) {
            this.maxPortField.textboxKeyTyped(c, n);
        }
        if (this.threadsField.isFocused() && !Character.isLetter(c)) {
            this.threadsField.textboxKeyTyped(c, n);
        }
        super.keyTyped(c, n);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.hostField = classProvider.createGuiTextField(0, Fonts.roboto40, this.representedScreen.getWidth() / 2 - 100, 60, 200, 20);
        this.hostField.setFocused(true);
        this.hostField.setMaxStringLength(Integer.MAX_VALUE);
        this.hostField.setText("localhost");
        this.minPortField = classProvider.createGuiTextField(1, Fonts.roboto40, this.representedScreen.getHeight() / 2 - 100, 90, 90, 20);
        this.minPortField.setMaxStringLength(5);
        this.minPortField.setText(String.valueOf(1));
        this.maxPortField = classProvider.createGuiTextField(2, Fonts.roboto40, this.representedScreen.getWidth() / 2 + 10, 90, 90, 20);
        this.maxPortField.setMaxStringLength(5);
        this.maxPortField.setText(String.valueOf(65535));
        this.threadsField = classProvider.createGuiTextField(3, Fonts.roboto40, this.representedScreen.getWidth() / 2 - 100, 120, 200, 20);
        this.threadsField.setMaxStringLength(Integer.MAX_VALUE);
        this.threadsField.setText(String.valueOf(500));
        this.buttonToggle = classProvider.createGuiButton(1, this.representedScreen.getWidth() / 2 - 100, this.representedScreen.getHeight() / 4 + 95, this.running ? "Stop" : "Start");
        this.representedScreen.getButtonList().add(this.buttonToggle);
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(0, this.representedScreen.getWidth() / 2 - 100, this.representedScreen.getHeight() / 4 + 120, "Back"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(2, this.representedScreen.getWidth() / 2 - 100, this.representedScreen.getHeight() / 4 + 155, "Export"));
        super.initGui();
    }

    @Override
    public void actionPerformed(IGuiButton iGuiButton) throws IOException {
        switch (iGuiButton.getId()) {
            case 0: {
                mc.displayGuiScreen(this.prevGui);
                break;
            }
            case 1: {
                if (this.running) {
                    this.running = false;
                } else {
                    int n;
                    this.host = this.hostField.getText();
                    if (this.host.isEmpty()) {
                        this.status = "\u00a7cInvalid host";
                        return;
                    }
                    try {
                        this.minPort = Integer.parseInt(this.minPortField.getText());
                    }
                    catch (NumberFormatException numberFormatException) {
                        this.status = "\u00a7cInvalid min port";
                        return;
                    }
                    try {
                        this.maxPort = Integer.parseInt(this.maxPortField.getText());
                    }
                    catch (NumberFormatException numberFormatException) {
                        this.status = "\u00a7cInvalid max port";
                        return;
                    }
                    try {
                        n = Integer.parseInt(this.threadsField.getText());
                    }
                    catch (NumberFormatException numberFormatException) {
                        this.status = "\u00a7cInvalid threads";
                        return;
                    }
                    this.ports.clear();
                    this.currentPort = this.minPort - 1;
                    this.checkedPort = this.minPort;
                    for (int i = 0; i < n; ++i) {
                        new Thread(this::lambda$actionPerformed$0).start();
                    }
                    this.running = true;
                }
                this.buttonToggle.setDisplayString(this.running ? "Stop" : "Start");
                break;
            }
            case 2: {
                File file = MiscUtils.saveFileChooser();
                if (file == null || file.isDirectory()) {
                    return;
                }
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write("Portscan\r\n");
                    fileWriter.write("Host: " + this.host + "\r\n\r\n");
                    fileWriter.write("Ports (" + this.minPort + " - " + this.maxPort + "):\r\n");
                    for (Integer n : this.ports) {
                        fileWriter.write(n + "\r\n");
                    }
                    fileWriter.flush();
                    fileWriter.close();
                    JOptionPane.showMessageDialog(null, "Exported successfully!", "Port Scanner", 1);
                    break;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + exception.getClass().getName() + "\nMessage: " + exception.getMessage());
                }
            }
        }
        super.actionPerformed(iGuiButton);
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.representedScreen.drawBackground(0);
        Fonts.roboto40.drawCenteredString("Port Scanner", (float)this.representedScreen.getWidth() / 2.0f, 34.0f, 0xFFFFFF);
        Fonts.roboto35.drawCenteredString(this.running ? "\u00a77" + this.checkedPort + " \u00a78/ \u00a77" + this.maxPort : (this.status == null ? "" : this.status), (float)this.representedScreen.getWidth() / 2.0f, (float)this.representedScreen.getHeight() / 4.0f + 80.0f, 0xFFFFFF);
        this.buttonToggle.setDisplayString(this.running ? "Stop" : "Start");
        this.hostField.drawTextBox();
        this.minPortField.drawTextBox();
        this.maxPortField.drawTextBox();
        this.threadsField.drawTextBox();
        Fonts.roboto40.drawString("\u00a7c\u00a7lPorts:", 2, 2, Color.WHITE.hashCode());
        List list = this.ports;
        synchronized (list) {
            int n3 = 12;
            for (Integer n4 : this.ports) {
                Fonts.roboto35.drawString(String.valueOf(n4), 2, n3, Color.WHITE.hashCode());
                n3 += Fonts.roboto35.getFontHeight();
            }
        }
        super.drawScreen(n, n2, f);
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) throws IOException {
        this.hostField.mouseClicked(n, n2, n3);
        this.minPortField.mouseClicked(n, n2, n3);
        this.maxPortField.mouseClicked(n, n2, n3);
        this.threadsField.mouseClicked(n, n2, n3);
        super.mouseClicked(n, n2, n3);
    }

    public GuiPortScanner(IGuiScreen iGuiScreen) {
        this.prevGui = iGuiScreen;
    }

    private void lambda$actionPerformed$0() {
        try {
            while (this.running && this.currentPort < this.maxPort) {
                int n = ++this.currentPort;
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(this.host, n), 500);
                    socket.close();
                    List list = this.ports;
                    synchronized (list) {
                        if (!this.ports.contains(n)) {
                            this.ports.add(n);
                        }
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (this.checkedPort >= n) continue;
                this.checkedPort = n;
            }
            this.running = false;
            this.buttonToggle.setDisplayString("Start");
        }
        catch (Exception exception) {
            this.status = "\u00a7a\u00a7l" + exception.getClass().getSimpleName() + ": \u00a7c" + exception.getMessage();
        }
    }
}

